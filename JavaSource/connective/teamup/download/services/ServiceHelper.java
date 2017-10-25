/*
 * 05/06/2005 - Created
 * 05/24/2005 - Renamed from AL3DownloadHelper to ServiceHelper, added FileInfoParser -- kwm
 */
package connective.teamup.download.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import connective.teamup.al3.AcordDataGroup;
import connective.teamup.al3.AcordFactory;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.ClaimFileTransactionInfo;
import connective.teamup.download.db.DirectBillTransactionInfo;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.db.TransactionInfo;
import connective.teamup.download.ws.objects.DownloadClaimFileInfo;
import connective.teamup.download.ws.objects.DownloadDirectBillTransInfo;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.download.ws.objects.DownloadFileInfoInternal;
import connective.teamup.download.ws.objects.DownloadTransactionInfo;

/**
 * @author mccrearyk
 */
public class ServiceHelper
{
	private static final Logger LOGGER = Logger.getLogger(ServiceHelper.class);
	
	private AcordFactory acordFactory = null;
	
	private static ServiceHelper _the_instance = null;


	/**
	 * Constructor for ServiceHelper
	 */
	protected ServiceHelper()
	{
		super();

		// instantiate the ACORD factory and load the group definitions
		try
		{
			acordFactory = new AcordFactory();
			InputStream instr = getClass().getResourceAsStream("/al3.xml");
			acordFactory.loadGroupDefinitions(instr);
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			System.out.println(e.getMessage());
		}
	}

	public static ServiceHelper getInstance()
	{
		if (_the_instance == null)
			_the_instance = new ServiceHelper();
		return _the_instance;
	}

	/**
	 * Returns the file contents as a byte array.  Performs any updates to the
	 * ACORD data that may be required for either a partial file download OR a
	 * resend/synchronization download.
	 * 
	 * @param fileInfo - The file info
	 * @return byte[]
	 */
	public byte[] getAL3Contents(FileInfo fileInfo) throws Exception
	{
		return getAL3Contents(fileInfo, null, null);
	}

	/**
	 * Returns the file contents as a byte array.  Performs any updates to the
	 * ACORD data that may be required for either a partial file download OR a
	 * resend/synchronization download.
	 * 
	 * @param fileInfo - The file info
	 * @param transList - An array list for passing back the transactions included in the file
	 * @return byte[]
	 */
	public byte[] getAL3Contents(FileInfo fileInfo, ArrayList transList) throws Exception
	{
		return getAL3Contents(fileInfo, transList, null);
	}

	/**
	 * Returns the file contents as a byte array.  Performs any updates to the
	 * ACORD data that may be required for either a partial file download OR a
	 * resend/synchronization download.
	 * 
	 * @param fileInfo - The file info
	 * @param transList - An array list for passing back the transactions included in the file
	 * @param serverInfo - The server info object
	 * @return byte[]
	 */
	public byte[] getAL3Contents(FileInfo fileInfo, ArrayList transList, ServerInfo serverInfo) throws Exception
	{
		// Attempt the contents retrieval a maximum of 5 times before reporting 
		// the error.  If any attempts are successful, return the contents without
		// reporting the error to the agent or carrier. -- 04/21/2004, kwm
		Exception exception = null;
		int transactionCount =0;
//		for (int errCount=0; errCount < 5; errCount++)
//		{
			try
			{
				byte[] fileBytes = fileInfo.getFileContents(transList);
				int transCount = fileInfo.getTransactionsRetrievedCount();
				transactionCount =  fileInfo.getTransactionCount();
				if(fileInfo.isDirectBill()) {
					transactionCount = 1;
				}
				// Only parse AL3 data if this is a partial file download or a
				// resend/synchronization download from the archive
				if (!fileInfo.isClaimFile() && (transCount != transactionCount ||
					fileInfo.getDownloadStatus().equals(DownloadStatus.RESEND)))
				{
					// Parse file and do any updating required for either a partial
					// file download OR a resend/synchronization download
					ByteArrayInputStream instr = new ByteArrayInputStream(fileBytes);
					AcordDataGroup[] transactions = acordFactory.parseFile(instr);
					if (transactions.length > 0)
					{
						boolean fileUpdated = false;
						
						AcordDataGroup header = (AcordDataGroup) transactions[0].getAttribute(AcordDataGroup.MESSAGE_HEADER);
						AcordDataGroup footer = (AcordDataGroup) transactions[0].getAttribute(AcordDataGroup.MESSAGE_FOOTER);
						
						// Check for partial file download
						if (transactions.length != transactionCount)
						{
							if (footer != null && footer.getElementNumericValue("3MTG01") > 0)
							{
								int dataCount = 0;
								String countUnitCode = header.getElementValue("1MHG09");
								if (countUnitCode != null && countUnitCode.equals("1"))
								{
									// Count the number of element groups (ACORD records) in the file
									dataCount = 2;  // header and footer records
									dataCount += transactions.length;	// 2TRG records
									for (int i=0; i < transactions.length; i++)
										dataCount += transactions[i].getSubgroupCount();
								}
								else
								{
									// Count the number of characters in the file
									dataCount = fileBytes.length;
								}
								String count = String.valueOf(dataCount).trim();
								while (count.length() < 8)
									count = "0" + count;
								
								// Update the footer record with the new "Total Data in
								// Message" counter
								footer.setElementValue("3MTG01", count);
								footer.rebuildRawData();
								fileUpdated = true;
							}
						}
						
						// Check for resend / synch download
						if (fileInfo.getDownloadStatus().equals(DownloadStatus.RESEND))
						{
							for (int i=0; i < transactions.length; i++)
							{
								// Set the transaction function code to synch
								transactions[i].setElementValue("2TRG08", "SYN");
								
								// Set the cycle business purpose code to synch
								transactions[i].setElementValue("2TRG25", "SYN");
								
								transactions[i].rebuildRawData();
							}
							fileUpdated = true;
						}
						
						if (fileUpdated)
						{
							// Regenerate the byte array from the parsed file data
							StringBuffer fileData = new StringBuffer(header.getRawData());
							for (int i=0; i < transactions.length; i++)
							{
								fileData.append(transactions[i].getRawData());
								for (int s=0; s < transactions[i].getSubgroupCount(); s++)
									fileData.append(transactions[i].getSubgroup(s).getRawData());
							}
							fileData.append(footer.getRawData());
							
							return fileData.toString().getBytes();
						}
					}
				}
				
				return fileBytes;
			}
			catch (Exception e)
			{
				LOGGER.error(e);
				// Save the exception
				exception = e;
				
				// Send a tech support email
				DateFormat df = DateFormat.getDateTimeInstance();
				long timestamp = System.currentTimeMillis();
				String subject = CarrierInfo.getInstance().getShortName() +
								 " Download: Error retrieving AL3 file transaction";
				String message = "Application:  ";
				if (serverInfo == null)
					message += "Agency Java Client App";
				else
					message += serverInfo.getAppName();
				message += "\nAgent ID:  " + fileInfo.getAgentId() +
						   "\nFilename:  " + fileInfo.getOriginalFilename() +
						   "\nFile creation date:  " + df.format(new Date(fileInfo.getCreatedDate())) +
						   "\nError timestamp:  " + df.format(new Date(timestamp));
				EmailService.getInstance().sendTechSupportEmail(subject, message, e);
			}
//		}
		
		if (exception != null)
			throw exception;
		
		return null;
	}

	/**
	 * Converts the FileInfo database layer object to the DownloadFileInfoInternal object 
	 * used by the download service classes.
	 */
	public DownloadFileInfoInternal getDownloadFileInfo(FileInfo file)
	{
		DownloadFileInfoInternal dlFile = new DownloadFileInfoInternal();
		dlFile.setFilename(file.getFilename());
		dlFile.setOriginalFilename(file.getOriginalFilename());
		dlFile.setBatchNumber(file.getBatchNumber());
		dlFile.setCreatedDate(file.getCreatedDate());
		dlFile.setImportedDate(file.getImportedDate());
		dlFile.setLastDownloadedDate(file.getLastDownloadDate());
		dlFile.setMsgSeq(file.getMessageSequence());
		dlFile.setParticipantCode(file.getParticipantCode());
		dlFile.setStatus(file.getDownloadStatus().getCode());
		dlFile.setTestFile(file.isTestFile());
		dlFile.setTransactionCount(file.getTransactionCount());
		
		return dlFile;
	}

	/**
	 * Converts the TransactionInfo database layer object to the DownloadTransactionInfo object 
	 * used by the download service classes.
	 */
	public DownloadTransactionInfo getDownloadTransInfo(TransactionInfo transaction)
	{
		DownloadTransactionInfo dlTrans = new DownloadTransactionInfo();
		dlTrans.setCustomerId(transaction.getCustomerId());
		dlTrans.setDataFormat(transaction.getFileType());
		dlTrans.setDescription(transaction.getDescription());
		dlTrans.setDlStatus(transaction.getDownloadStatus().getCode());
		dlTrans.setInsuredName(transaction.getNamedInsured());
		dlTrans.setLastDownloadDate(transaction.getLastDownloadDate());
		dlTrans.setLob(transaction.getLob());
		dlTrans.setPolicyEffDate(transaction.getPolicyEffDate());
		dlTrans.setPolicyNumber(transaction.getPolicyNumber());
		dlTrans.setSequence(transaction.getSequence());
		dlTrans.setTransactionEffDate(transaction.getTransEffDate());
		dlTrans.setTransactionPremium(transaction.getTransPremium());
		dlTrans.setTransactionSeq(transaction.getTransSequence());
		dlTrans.setTransactionType(transaction.getTransType());
		
		return dlTrans;
	}

	public DownloadFileInfo[] getFilesFromXML(InputStream is) throws SAXException, IOException, ParserConfigurationException
	{
		DlFileHandler handler = new DlFileHandler();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(is, handler);
		
		return handler.getDownloadFiles();
	}
	
	public String writeFileToXML(DownloadFileInfo file)
	{
		// Convert the single file info bean into an array and then write it to XML
		DownloadFileInfo[] files = new DownloadFileInfo[1];
		files[0] = file;
		
		return writeFilesToXML(files);
	}
	
	public String writeFilesToXML(DownloadFileInfo[] files)
	{
		DlFileHandler handler = new DlFileHandler();
		String xml = handler.writeToXML(files);
		
		return xml;
	}
	/**
	 * Converts the Direct Bill TransactionInfo database layer object to the DownloadDirectBillTransInfo object 
	 * used by the download service classes.
	 */
	public DownloadDirectBillTransInfo getDownloadDirectBillTransInfo(DirectBillTransactionInfo transaction)
	{
		DownloadDirectBillTransInfo dlTrans = new DownloadDirectBillTransInfo();
		dlTrans.setItemNumber(transaction.getItemNumber());
		dlTrans.setInsuredName(transaction.getInsuredName());
		dlTrans.setAgentId(transaction.getAgentId());
		dlTrans.setProducerSubCode(transaction.getProducerSubCode());
		dlTrans.setPolicyNumber(transaction.getPolicyNumber());
		dlTrans.setTransEffectiveDate(transaction.getTransEffectiveDate());
		dlTrans.setTransTypeCode(transaction.getTransTypeCode());
		dlTrans.setGrossAmount(transaction.getGrossAmount());
		dlTrans.setComissionRate(transaction.getComissionRate());
		dlTrans.setComissionAmount(transaction.getComissionAmount());
		dlTrans.setInstallMentNumber(transaction.getInstallMentNumber());
		dlTrans.setPaymentPlanCode(transaction.getPaymentPlanCode());
		dlTrans.setLob(transaction.getLob());
		dlTrans.setLobSubCode(transaction.getLobSubCode());
		dlTrans.setCompanyProducerCode(transaction.getCompanyProducerCode());
		dlTrans.setPolicyEffectiveDate(transaction.getPolicyEffectiveDate());
		dlTrans.setPolicyExpiryDate(transaction.getPolicyExpiryDate());
		dlTrans.setPolicyVersion(transaction.getPolicyVersion());
		dlTrans.setBillingAccountNumber(transaction.getBillingAccountNumber());
		dlTrans.setComissionAdjustedAmount(transaction.getComissionAdjustedAmount());
		dlTrans.setCustomerId(transaction.getCustomerId());
		//dlTrans.setDataFormat(transaction.getFileType());
		dlTrans.setInsuredName(transaction.getInsuredName());
		dlTrans.setSequence(transaction.getSequence());
		dlTrans.setCreatedDate(transaction.getCreatedDate());
		return dlTrans;
	}
	 /* Converts the Direct Bill TransactionInfo database layer object to the DownloadDirectBillTransInfo object 
	 * used by the download service classes.
	 */
	public DownloadClaimFileInfo getDownloadClaimTransInfo(ClaimFileTransactionInfo transaction)
	{
		DownloadClaimFileInfo dlTrans = new DownloadClaimFileInfo();
		
		dlTrans.setAgentId(transaction.getAgentId());
		dlTrans.setOrigFileName(transaction.getOrigFileName());
		dlTrans.setSequence(transaction.getSequence());
		dlTrans.setTransactionSeq(transaction.getSequence());
		dlTrans.setDlStatus(transaction.getDlStatus());
		dlTrans.setCreatedDate(transaction.getCreatedDate());
		dlTrans.setLastDownloadDate(transaction.getLastDownloadDate());
		
		dlTrans.setInsuredName(transaction.getInsuredName());
		dlTrans.setLob(transaction.getLob());
		
		dlTrans.setPolicyNumber(transaction.getPolicyNumber());
		
		dlTrans.setPolicyEffectiveDate(transaction.getPolicyEffectiveDate());
		dlTrans.setPolicyExpiryDate(transaction.getPolicyExpiryDate());

		
		//dlTrans.setTransEffectiveDate(transaction.getTransEffectiveDate());
		dlTrans.setTransTypeCode(transaction.getTransTypeCode());
		
		dlTrans.setFileType(transaction.getFileType());
		dlTrans.setClaimNumber(transaction.getClaimNumber());
		dlTrans.setClaimStatus(transaction.getClaimStatus());
		dlTrans.setEventDate(transaction.getEventDate());
		dlTrans.setReportedDate(transaction.getReportedDate());
		dlTrans.setBussinessPCode(transaction.getBussinessPCode());
		
		
		//dlTrans.setDataFormat(transaction.getFileType());
		

		return dlTrans;
	}
}
