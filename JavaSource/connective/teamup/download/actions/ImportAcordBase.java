package connective.teamup.download.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Vector;

import org.apache.log4j.Logger;

import connective.teamup.al3.AcordDataGroup;
import connective.teamup.al3.AcordFactory;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileImportStatus;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.db.TransactionInfo;
import connective.teamup.download.services.EmailService;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ImportAcordBase
{
	private static final Logger LOGGER = Logger.getLogger(ImportAcordBase.class);
	
	protected AcordFactory factory = null;


	/**
	 * Constructor for UploadFile.
	 */
	public ImportAcordBase() 
	{
		super();

		// instantiate the factory and load the group definitions
		try
		{
			factory = new AcordFactory();
			InputStream instr = getClass().getResourceAsStream("/al3.xml");
			factory.loadGroupDefinitions(instr);
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Import an AL3 file.
	 * 
	 * @param serverInfo The server info bean
	 * @param filename The original name of the file being imported
	 * @param defaultAgent The agent ID; 'null' to parse agent ID from the AL3 data
	 * @param createdDate The original created date of the file being imported
	 * @param testFile True if this is a test file import
	 * @param interactive True if this is an interactive import, false for scheduled/automated import
	 * @param batch_num The import batch number (only used for automated/batch imports)
	 * @param instr InputStream for the file being imported
	 * @param transCount The total number of transactions in the file (zero if unknown)
	 * @param blockSequence The sequence number when importing files in blocks; zero for first block (or entire file)
	 * @param fileComplete True if this is either the entire file or the last block
	 * 
	 * @return connective.teamup.download.FileImportStatus
	 */
	protected FileImportStatus importFile(DatabaseOperation op, ServerInfo serverInfo, String filename, String defaultAgent, long createdDate, boolean testFile, boolean interactive, int batch_num, InputStream instr, int transCount, int blockSequence, boolean fileComplete) throws Exception
	{
		ImportActionHelper importHelper = new ImportActionHelper(op, serverInfo, FileInfo.TYPE_ACORD, 
																 createdDate, batch_num, testFile);
		FileImportStatus status = new FileImportStatus();
		
		boolean agentLive = false;
		boolean agentActive = false;
		String agentid = defaultAgent;
		String participantCode = "";
		String agentName = "";

		String CRLF = "\r\n";
		
		// Strip path out of filename
		filename = importHelper.getFileName(filename);
		String origFilename = filename;
		
		
		FileInfo fileinfo = null;
		AcordDataGroup[] transactions = null;
		try
		{
			// parse AL3
			transactions = factory.parseFile(instr);
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			// Error occurred trying to parse the AL3 file
			String msg = "Error occurred parsing the AL3 file during import.";
			status.setStatus(FileImportStatus.FAILED_AL3_DATA_BAD);
			status.setText(msg);
			status.setException(e);
			
			importHelper.reportImportError(agentid, agentName, participantCode, msg, filename, 
							origFilename, true, status, e,0,DownloadStatus.CURRENT);
			
			return status;
		}
		
		try
		{
			if (transactions == null || transactions.length == 0)
			{
				// There were no transactions in the ACORD file
				String msg = "No transactions defined in the import file";
				status.setStatus(FileImportStatus.FAILED_AL3_NO_TRANSACTIONS);
				status.setText(msg);
				
				if (batch_num > 0)
				{
					importHelper.reportImportError(agentid, agentName, participantCode, msg, filename, 
									origFilename, interactive, status, null,0,DownloadStatus.CURRENT);
				}
			}
			else
			{
				// 	create the main file record
				AcordDataGroup msgHeader = (AcordDataGroup) transactions[0].getAttribute(AcordDataGroup.MESSAGE_HEADER);
				
				// try to parse the agent id from the AL3 file
				if (agentid == null || agentid.trim().length() == 0)
					agentid = msgHeader.getElementValue("1MHG03").trim().toUpperCase();
				else if (!agentid.trim().equals(msgHeader.getElementValue("1MHG03").trim()))
				{
					String newAgent = agentid;
					while (newAgent.length() < 10)
						newAgent += " ";
					msgHeader.setElementValue("1MHG03", newAgent);
				}
	
				AgentInfo agentInfo = importHelper.getAgentInfo(filename, agentid);
				if (agentInfo != null) {
					try
					{
						participantCode = agentid;
						agentid = agentInfo.getAgentId();
						agentName = agentInfo.getName();
						agentLive = agentInfo.isLive();
						agentActive = agentInfo.isActive();
						
						// If a default filename is defined for the agent's vender
						// system, change the filename to match the default
						String defaultFilename = agentInfo.getDefaultFilename();
						if (defaultFilename == null || defaultFilename.equals(""))
							defaultFilename = agentInfo.getAms().getCompanyFilename();
						if (!testFile && defaultFilename != null && !defaultFilename.equals(""))
							filename = defaultFilename;
					}
					catch (Exception e) {
						LOGGER.error(e);
					}
				}
				
				if (agentInfo == null || !agentActive)
				{
					if (agentInfo == null)
					{
						status.setStatus(FileImportStatus.REJECTED_AGENT_UNKNOWN);
						status.setText("Unknown agent id");
					}
					else
					{
						status.setStatus(FileImportStatus.REJECTED_AGENT_INACTIVE);
						status.setText("Agent is currently disabled in TEAM-UP Download");
					}
					
					if (batch_num > 0)
					{
						// Only log an error if this is the first block for this file
						if (blockSequence == 0) {
							// Create a temporary file record
							fileinfo = importHelper.createFileInfo(filename, origFilename, agentid);
							fileinfo.setParticipantCode(participantCode);

							// Create the error message
							String msg;
							if (agentInfo == null && serverInfo.getCarrierInfo().getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME))
							{
								msg = "No trading partner was found for this ";
								if (serverInfo.getCarrierInfo().getImportFileCreator().equals("KEYLINK"))
									msg += "KeyLink file";
								else
									msg += "file name";
							}
							else if (agentid.equalsIgnoreCase("UNKNOWN"))
								msg = "Import process could not identify the trading partner for this file";
							else if (agentInfo == null)
								msg = "This trading partner has not yet been registered for TEAM-UP Download";
							else
								msg = "This trading partner is currently flagged as disabled, import not processed";
							
							FileImportStatus failStatus = new FileImportStatus(status.getStatus());
							failStatus.setText(msg);
							
							// Log the file as a failed import.  Only log if import is
							// being done in interactive mode, OR if the file is matched
							// to an agent in the database (disabled)
							if (interactive || agentInfo != null)
								op.logImport(fileinfo, failStatus, batch_num, agentName);
						}
					}
				}
				
				if (agentActive)
				{
					boolean goodFile = false;
					DownloadStatus dlStatus = null;
					int existingTransCount = 0;
					
					if (blockSequence == 0)
					{
						// Create a new file header record
						
						int msgSeq = 0;
						try
						{
							msgSeq = Integer.parseInt(msgHeader.getElementValue("1MHG07"));
						}
						catch (Exception e) {
							LOGGER.error(e);
						}
						
						String header = msgHeader.getRawData();
						if ((msgHeader.getLength() - header.length()) == 2)
							header += CRLF;
						else
						{
							while (header.length() < msgHeader.getLength())
								header += " ";
						}
		
						String footer = "";
						AcordDataGroup msgFooter = (AcordDataGroup) transactions[0].getAttribute(AcordDataGroup.MESSAGE_FOOTER);
						if (msgFooter != null)
						{
							footer = msgFooter.getRawData();
							if ((msgFooter.getLength() - footer.length()) == 2)
								footer += CRLF;
							else
							{
								while (footer.length() < msgFooter.getLength())
									footer += " ";
							}
						}
						
						// Set download flags -- if agent is not live, import as archive file only
						dlStatus = DownloadStatus.CURRENT;
						if (testFile) {
							dlStatus = DownloadStatus.TEST;
						}
						else if (!agentLive) {
							dlStatus = DownloadStatus.ARCHIVED;
						}
						
						// Create the file record
						fileinfo = importHelper.createFileInfo(filename, origFilename, agentid);
						fileinfo.setDownloadStatus(dlStatus);
						fileinfo.setMessageSequence(msgSeq);
						fileinfo.setHeader(header);
						fileinfo.setFooter(footer);
						fileinfo.setFileComplete(fileComplete);
						fileinfo.setParticipantCode(participantCode);
						
						// Check to see if this is a duplicate file in the database
						if (op.isDuplicateFile(agentid, origFilename, String.valueOf(createdDate)))
						{
							status.setStatus(FileImportStatus.REJECTED_DUPLICATE_FILE);
							status.setText("Duplicate file, import failed");
							op.logImport(fileinfo, status, batch_num, agentName);
						}
						else
						{
							fileinfo.save();
							goodFile = true;
						}
					}
					else
					{
						// Get the existing header record for this file
						
						fileinfo = op.getDownloadFile(agentid, origFilename, createdDate);
						if (fileinfo != null)
						{
							goodFile = true;
							dlStatus = fileinfo.getDownloadStatus();
							
							// Load the existing transactions
							fileinfo.loadTransFromDb();
							existingTransCount = fileinfo.getTransactionCount();
							
							// If message footer is added, update file record
							if (fileinfo.getFooter() == null || fileinfo.getFooter().equals(""))
							{
								AcordDataGroup msgFooter = (AcordDataGroup) transactions[0].getAttribute(AcordDataGroup.MESSAGE_FOOTER);
								if (msgFooter != null)
								{
									String footer = msgFooter.getRawData();
									if ((msgFooter.getLength() - footer.length()) == 2)
										footer += CRLF;
									else
									{
										while (footer.length() < msgFooter.getLength())
											footer += " ";
									}
									
									fileinfo.setFooter(footer);
									fileinfo.save();
								}
							}
						}
						else
						{
							status.setStatus(FileImportStatus.FAILED_AL3_DATA_BAD);
							status.setText("Import failed: partial file received (block " + 
											blockSequence + "), missing data");
							op.logImport(fileinfo, status, batch_num, agentName);
							
						}
					}
					
					if (goodFile)
					{
						// create the transaction records
						for (int i=0; i < transactions.length; i++)
						{
							// transaction type
							String transType = transactions[i].getElementValue("2TRG08");
							if (transType.equals("FMG") || transType.equals("PMG"))
								transType = transactions[i].getElementValue("2TRG25");
							String description = (String) serverInfo.getTransTypeHashtable().get(transType);
							
							// transaction sequence number
							int transSeq = 0;
							try
							{
								transSeq = Integer.parseInt(transactions[i].getElementValue("2TRG19"));
							}
							catch (Exception e) {
								LOGGER.error(e);
							}
		
							// transaction eff date
							String transEffDate = transactions[i].getElementValue("2TRG33").trim();
							if (transEffDate.equals("") || isQuestionMarks(transEffDate))
								transEffDate = transactions[i].getElementValue("2TRG23").trim();
							if (isQuestionMarks(transEffDate))
								transEffDate = "";
							if (transEffDate.length() == 8)
								transEffDate = transEffDate.substring(4, 6) + "/" + transEffDate.substring(6) + "/" + transEffDate.substring(0, 4);
							else if(transEffDate.length() == 6)
								transEffDate = transEffDate.substring(2, 4) + "/" + transEffDate.substring(4) + "/" + transEffDate.substring(0, 2);
		
							// file type (AL1 or AL3)								
							String fileType = transactions[i].getElementValue("2TRG04");
							if (!fileType.equals(TransactionInfo.TYPE_AL3) && !fileType.equals(TransactionInfo.TYPE_AL1)) {
								fileType = TransactionInfo.TYPE_AL3;
							}

							// named insured
							String namedInsured = "";
							String customerId = "";
							AcordDataGroup basicInsured = transactions[i].findGroupInTree("5BIS", "B1", 1);
							if (basicInsured != null)
							{
								namedInsured = getName(basicInsured.getElementValue("5BIS01"));
								customerId = stripQuestionMarks(basicInsured.getElementValue("5BIS03")).trim();
							}
						  
						 	// basic policy info
						 	String lob = "";
						 	String policyEffDate = "";
						 	String policyNumber = "";
						 	String premNetChg = "";
							double transPremium = 0.0;
							AcordDataGroup policyInfo = transactions[i].findGroupInTree("5BPI", "F1", 1);
							if (policyInfo != null)
							{
								lob = policyInfo.getElementValue("5BPI04").trim();

								policyEffDate = policyInfo.getElementValue("5BPI37").trim();
								if (policyEffDate.equals(""))
									policyEffDate = policyInfo.getElementValue("5BPI06").trim();
								if (policyEffDate.length() == 8)
									policyEffDate = policyEffDate.substring(4, 6) + "/" + policyEffDate.substring(6) + "/" + policyEffDate.substring(0, 4);
								else if(policyEffDate.length() == 6)
									policyEffDate = policyEffDate.substring(2, 4) + "/" + policyEffDate.substring(4) + "/" + policyEffDate.substring(0, 2);
									
								policyNumber = stripApostrophes(policyInfo.getElementValue("5BPI01").trim());
								
								premNetChg = policyInfo.getElementValue("5BPI12").trim();
								if (premNetChg.equals(""))
									premNetChg = policyInfo.getElementValue("5BPI11").trim();
								if (!premNetChg.equals(""))
								{
									int len = premNetChg.length();
									char sign = premNetChg.charAt(len-1);
									if (sign == '-' || sign == '+')
										premNetChg = premNetChg.substring(0, len-1);
									try
									{
										transPremium = Double.parseDouble(premNetChg) / 100.0;
										if (sign == '-')
											transPremium *= -1;
									}
									catch (Exception e) {
										LOGGER.error(e);
									}
								}
							}
							else
							{
								// If no 5BPI record, use the policy type for the line of business code
								lob = transactions[i].getElementValue("2TRG07").trim();
							}
							
							// transaction description
							if (transType.equals("MEM"))		// MEMO transaction
							{
								Vector memoList = transactions[i].findGroupInTree("6MEM");
								if (memoList != null && memoList.size() > 0)
								{
									AcordDataGroup memo = (AcordDataGroup) memoList.elementAt(0);
									String temp = memo.getElementValue("6MEM04").trim();
									if (!temp.equals(""))
										description = temp;
								}
							}
							else if (transType.equals("DBR") || transType.equals("DBS"))
							{
								// Direct Bill transaction
								if (!dlStatus.equals(DownloadStatus.DB_CURRENT) && !dlStatus.equals(DownloadStatus.DB_ARCHIVED))
								{
									if (dlStatus.equals(DownloadStatus.CURRENT))
										dlStatus = DownloadStatus.DB_CURRENT;
									else
										dlStatus = DownloadStatus.DB_ARCHIVED;
									fileinfo.setDownloadStatus(dlStatus);
									fileinfo.save();
								}
							}
							else
							{
								AcordDataGroup transInfo = transactions[i].findGroupInTree("5ACT", "F2", 1);
								if (transInfo != null)
								{
									String transDesc = transInfo.getElementValue("5ACT05").trim();
									if (!transDesc.equals(""))
										description += ": " + stripApostrophes(transDesc);
								}
							}
							
							// Non-policy transactions
							if (policyNumber.equals("") && namedInsured.equals(""))
							{
								policyNumber = "(N/A)";
								namedInsured = "(" + description;
								String busPurpCd = transactions[i].getElementValue("2TRG25");
								if (busPurpCd != null && !busPurpCd.equals(transType))
								{
									String desc2 = (String) serverInfo.getTransTypeHashtable().get(busPurpCd);
									if (desc2 != null && !desc2.equals(description))
										namedInsured += " / " + desc2;
								}
								namedInsured = namedInsured.trim() + ")";
							}
							if (namedInsured != null && namedInsured.length() > 60)
								namedInsured = namedInsured.substring(0, 60);
							if (customerId != null && customerId.length() > 30)
								customerId = customerId.substring(0, 30);
							if (description != null && description.length() > 160)
								description = description.substring(0, 160);
							
							TransactionInfo transinfo = fileinfo.createTransaction();
							transinfo.setSequence(i + 1 + existingTransCount);
							transinfo.setDownloadStatus(dlStatus);
							transinfo.setLastDownloadDate(0);
							transinfo.setNamedInsured(namedInsured);
							transinfo.setCustomerId(customerId);
							transinfo.setLob(lob);
							transinfo.setPolicyEffDate(policyEffDate);
							transinfo.setPolicyNumber(policyNumber);
							transinfo.setTransType(transType);
							transinfo.setTransSequence(transSeq);
							transinfo.setFileType(fileType);
							transinfo.setTransEffDate(transEffDate);
							transinfo.setTransPremium(transPremium);
							transinfo.setDescription(description);
							transinfo.save();

							// write the transaction data
							ByteArrayOutputStream os = new ByteArrayOutputStream();
							PrintWriter out = new PrintWriter(os);
							out.print(transactions[i].getRawData());
							for (int j=0; j < transactions[i].getSubgroupCount(); j++)
							{
								String record = transactions[i].getSubgroup(j).getRawData();
								int recordLen = transactions[i].getSubgroup(j).getLength();
								if ((recordLen - record.length()) == 2)
									record += CRLF;
								else
								{
									while (record.length() < recordLen)
										record += " ";
								}
								
								out.print(record);
							}
							out.flush();
							
							ByteArrayInputStream bis = new ByteArrayInputStream(os.toByteArray());
							
							// write the blob
							transinfo.writeTransactionData(bis, os.toByteArray().length);
						}
						
						// update the table with the total number of transactions in the file
						fileinfo.updateTransactionCount();
						
						if (fileComplete)
						{
							if (!fileinfo.isFileComplete())
							{
								// update the header record
								fileinfo.setFileComplete(true);
								fileinfo.updateFileComplete();
							}
							
						}
						
						// Set the import status to successful
						if (fileComplete && (agentLive || 
											 serverInfo.getCarrierInfo().isDeleteAllImportedFiles()))
							status.setStatus(FileImportStatus.IMPORTED_DELETE_FILE);
						else
							status.setStatus(FileImportStatus.IMPORTED_LEAVE_FILE);
						
						if (fileComplete)
						{
							// add to the log
							String desc;
							if (testFile)
								desc = "Test file import";
							else
							{
								desc = "Import successful";
								if (fileinfo.getDownloadStatus().equals(DownloadStatus.ARCHIVED) ||
											fileinfo.getDownloadStatus().equals(DownloadStatus.DB_ARCHIVED))
									desc += ", file archived";
								if (serverInfo.getCarrierInfo().isDeleteAllImportedFiles())
									desc += "; original file deleted from source directory";
								else if (agentLive)
									desc += "; original file deleted from source directory for live agent";
							}
							FileImportStatus importStatus = new FileImportStatus(status.getStatus());
							importStatus.setText(desc);
							op.logImport(fileinfo, importStatus, batch_num, agentName);						
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			String msg = "";
			String msgText = e.getMessage();
			String body = "";
			
			// Save the exception to the import status object
			status.setException(e);
			
			if (fileinfo == null)
			{
				// Error occurred trying to parse the AL3 file
				msg = "Error occurred parsing the AL3 file during import.";
				status.setStatus(FileImportStatus.FAILED_AL3_DATA_BAD);
				status.setText(msg);
				
				// Create a temporary file record
				if (agentid == null || agentid.equals(""))
				{
					AgentInfo agentInfo = importHelper.getAgentInfo(filename, agentid);
					if (agentInfo != null) {
						agentid = agentInfo.getAgentId();
						agentName = agentInfo.getName();
					}
					else
						agentid = "UNKNOWN";
				}
				
				fileinfo = importHelper.createFileInfo(filename, origFilename, agentid);
				fileinfo.setDownloadStatus(DownloadStatus.CURRENT);
				fileinfo.setParticipantCode(participantCode);
				
				// Log the file as a failed import.  Only log if import is
				// being done in interactive mode, OR if the file is matched
				// to an agent in the database (disabled)
				body = msg + "\n\n  Application:  ";
				if (serverInfo == null)
					body += "Import service";
				else
					body += serverInfo.getAppName();
				body += "\n  Filename:  " + filename +
						"\n  Agent ID:  " + agentid +
						"\n\n" + msgText;
			}
			else
			{
				// Error most likely occurred trying to save the file 
				// to the database table
				status.setStatus(FileImportStatus.FAILED_OTHER);
				msg = "Import error: " + e.getMessage();
				status.setText(msg);
				
				// Attempt to delete the (current) offending or partial file
				int badFileCount = 0;
				try
				{
					fileinfo.delete();
					if (fileinfo.getFilename() != null)
						msgText += "\nDue to this error, this file was deleted from the TEAM-UP database:  " + fileinfo.getFilename();
					badFileCount++;
				}
				catch (Exception e2) {
					LOGGER.error(e2);
				}
				
				// Find and delete any other partially imported files in this batch
				FileInfo[] badFiles = op.getIncompleteFilesForBatch(batch_num);
				for (int i=0; i < badFiles.length; i++)
				{
					badFileCount++;
					if (fileinfo.getFilename()!= null && fileinfo.getFilename().equals(badFiles[i].getFilename()))
						msgText += "\nDue to this error, this file was deleted from the TEAM-UP database:  " + fileinfo.getFilename();
					badFiles[i].delete();
				}
				if (badFileCount > 1)
					msgText += "\n\nA total of " + badFileCount + " incomplete files were deleted from the database.";
					
				body = "Application:  ";
				if (serverInfo == null)
					body += "Import service";
				else
					body += serverInfo.getAppName();
				body += "\n\n" + msgText;
			}
			
			// Get the exception stack trace to store with the log entry
			String stackTrace = importHelper.getExceptionStackTrace(e);
			
			// Add the stack trace to the email body
			if (body != null && !body.equals("") && stackTrace != null && !stackTrace.equals(""))
				body += "\n\n---------------\n\nSTACK TRACE:\n\n" + stackTrace;
			
			// Send a tech support email
			CarrierInfo carrier = serverInfo.getCarrierInfo();
			if (carrier.isNotifyOnImportError())
				EmailService.getInstance().sendEMail(carrier.getErrorsEmail(), msg, body);
 			
			// Log the error
			op.logImport(fileinfo, status, batch_num, agentName, stackTrace);
		}

		return status;
	}

	private String stripApostrophes(String data)
	{
		if (data == null || data.length() == 0)
			return data;
		
		String text = "";
		for (int i=0; i < data.length(); i++)
		{
			char c = data.charAt(i);
			if (c == '\'')
				text += "`";
			else
				text += c;
		}
		
		return text;
	}
	
	private String stripQuestionMarks(String data)
	{
		String text = "";
		
		if (data != null && data.length() > 0)
		{
			for (int i=0; i < data.length(); i++)
			{
				char c = data.charAt(i);
				if (c == '?')
					text += " ";
				else
					text += c;
			}
		}
		
		return text;
	}
	
	private String getName(String al3Name)
	{
		String name = "";

		if (al3Name == null || al3Name.length() == 0)
			return "";
		
		char nameFormat = al3Name.charAt(0);
		
		if (al3Name.length() < 60)
		{
			name = al3Name.trim();
		}
		else if (nameFormat == 'C')	// Commercial name format
		{
			name = al3Name.substring(1, 52).trim();
		}
		else if (nameFormat == 'P')	// Personal name format
		{
			String prefix = al3Name.substring(1, 9).trim();
			String first = al3Name.substring(9, 36).trim();
			String last = al3Name.substring(36, 56).trim();
			String suffix = al3Name.substring(56).trim();
			
			name = prefix + " " + first + " " + last;
			name = name.trim();
			if (suffix.length() > 0)
				name += ", " + suffix;
		}
		else if (nameFormat == 'F')	// Family name format
		{
			String prefix = al3Name.substring(1, 9).trim();
			String first = al3Name.substring(9, 25).trim();
			String middle = al3Name.substring(25, 36).trim();
			String last = al3Name.substring(36, 56).trim();
			String suffix = al3Name.substring(56).trim();
			
			name = prefix + " " + first + " " + middle;
			name = name.trim() + " " + last;
			if (suffix.length() > 0)
				name += ", " + suffix;
		}
		else
		{
			name = al3Name.substring(1).trim();
		}
		
		return stripApostrophes(name);
	}

	protected boolean isQuestionMarks(String str)
	{
		if (str == null || str.trim().equals(""))
			return false;
		
		boolean questionMarks = true;
		String data = str.trim();
		for (int i=0; i < data.length(); i++)
		{
			if (data.charAt(i) != '?')
			{
				questionMarks = false;
				break;
			}
		}
		return questionMarks;
	}
}
