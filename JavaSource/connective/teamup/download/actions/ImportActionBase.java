package connective.teamup.download.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import connective.teamup.al3.AcordDataGroup;
import connective.teamup.al3.AcordFactory;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DirectBillTransactionInfo;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.ExcludeLob;
import connective.teamup.download.db.FileImportStatus;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.db.LogTransInfo;
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
public class ImportActionBase
{
	private static final Logger LOGGER = Logger.getLogger(ImportActionBase.class);
	
	protected AcordFactory factory = null;
	public static Map<String,List<ExcludeLob>> lobList = new HashMap<String,List<ExcludeLob>>();

	/**
	 * Constructor for UploadFile.
	 */
	public ImportActionBase() 
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
		FileImportStatus status = new FileImportStatus();
		
		boolean agentLive = false;
		boolean agentActive = false;
		String agentid = defaultAgent;
		String participantCode = "";
		String agentName = "";
		long importedDate = System.currentTimeMillis();
		boolean isDirectBillfile = false;
		List<AcordDataGroup> invalidTransaction = null;
		List<AcordDataGroup> validTransaction = null;
		AcordDataGroup msgFooter = null;
		HashMap<String, String> invalidLobMap = new HashMap<String,String>();
		String CRLF = "\r\n";
		boolean checkForExclusion = false;
		int chunk =0;	
		List<LogTransInfo> logList = null;
		Vector directBillList  = null;
		
		// Strip path out of filename
		if (filename != null)
		{
			filename = filename.trim().toUpperCase();
			int n = filename.lastIndexOf("/");
			if (n >= 0 && n < filename.length())
				filename = filename.substring(n+1);
			n = filename.lastIndexOf("\\");
			if (n >= 0 && n < filename.length())
				filename = filename.substring(n+1);
		}
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
			
			// Create a temporary file record
			if (agentid == null || agentid.equals(""))
			{
				if (filename != null && !filename.trim().equals("") && 
					serverInfo.getCarrierInfo().getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME))
				{
					int n = filename.lastIndexOf('.');
					if (n > 0)
						filename = filename.substring(0, n);
					AgentInfo agent = op.getAgentInfoForFilename(filename);
					if (agent == null)
					{
						// check to see if a multi-header file index was added to the filename
						n = filename.indexOf("_(");
						if (n > 0)
							agent = op.getAgentInfoForFilename(filename.substring(0,n));
					}
					if (agent != null)
					{
						agentid = agent.getAgentId();
						agentName = agent.getName();
					}
					else
						agentid = "UNKNOWN";
				}
				else
					agentid = "UNKNOWN";
			}
			if (createdDate == 0)
				createdDate = importedDate;
			fileinfo = op.createFile();
			fileinfo.setAgentId(agentid);
			fileinfo.setFilename(filename);
			fileinfo.setOriginalFilename(origFilename);
			fileinfo.setCreatedDate(createdDate);
			fileinfo.setImportedDate(importedDate);
			fileinfo.setDownloadStatus(DownloadStatus.CURRENT);
			fileinfo.setLastDownloadDate(0);
			fileinfo.setMessageSequence(0);
			fileinfo.setBatchNumber(batch_num);
			fileinfo.setHeader("");
			fileinfo.setFooter("");
			fileinfo.setTestFile(testFile);
			fileinfo.setParticipantCode(participantCode);
			fileinfo.setFileType(FileInfo.TYPE_ACORD);
			
			// Log the file as a failed import.  Only log if import is
			// being done in interactive mode, OR if the file is matched
			// to an agent in the database (disabled)
			msg = "Error occurred parsing the AL3 file during import.";
			String body = msg + "\n\n  Application:  ";
			if (serverInfo == null)
				body += "Import service";
			else
				body += serverInfo.getAppName();
			body += "\n  Filename:  " + filename +
					"\n  Agent ID:  " + agentid +
					"\n\n" + e.getMessage();
			
			// Get the exception stack trace to store with the log entry
			String stackTrace = null;
			try
			{
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(os);
				e.printStackTrace(ps);
				stackTrace = os.toString();
				ps.close();
				os.close();
			} catch (IOException ioe) {
				LOGGER.error(ioe);
			}
 			
			// Log the error
			op.logImport(fileinfo, status, batch_num, agentName, stackTrace);
			
			// Send a tech support email
			CarrierInfo carrier = serverInfo.getCarrierInfo();
			if (carrier.isNotifyOnImportError())
				EmailService.getInstance().sendEMail(carrier.getErrorsEmail(), msg, body);
			
			return status;
		}
		
		try
		{
			if (transactions == null || transactions.length == 0)
			{
				// There were no transactions defined in the ACORD file
				String msg = "No transactions defined in the import file";
				status.setStatus(FileImportStatus.FAILED_AL3_NO_TRANSACTIONS);
				status.setText(msg);
				
				if (batch_num > 0)
				{
					if (agentid == null || agentid.equals(""))
						agentid = "UNKNOWN";
					if (createdDate == 0)
						createdDate = importedDate;
				
					// Create a temporary file record
					fileinfo = op.createFile();
					fileinfo.setAgentId(agentid);
					fileinfo.setFilename(filename);
					fileinfo.setOriginalFilename(origFilename);
					fileinfo.setCreatedDate(createdDate);
					fileinfo.setImportedDate(importedDate);
					fileinfo.setDownloadStatus(DownloadStatus.CURRENT);
					fileinfo.setTransactionCount(0);
					fileinfo.setLastDownloadDate(0);
					fileinfo.setMessageSequence(0);
					fileinfo.setHeader("");
					fileinfo.setFooter("");
					fileinfo.setTestFile(testFile);
					fileinfo.setBatchNumber(batch_num);
				
					boolean logit = interactive;
					if (!interactive)
					{
						// If import is in automated/non-interactive mode, only log
						// a bad file if it is for an agent in the database
						if (serverInfo.getCarrierInfo().getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME))
						{
							// lookup the agent id based on the filename
							String uniqueFile = filename.trim();
							int n = uniqueFile.lastIndexOf(".");
							if (n > 0)
								uniqueFile = uniqueFile.substring(0, n);
						
							if (op.getAgentInfoForFilename(uniqueFile) != null)
								logit = true;
						}
						else if (op.getAgentInfo(agentid) != null)
							logit = true;
					}
					
					// Log the file as a failed import
					if (logit)
					{
						op.logImport(fileinfo, status, batch_num, agentName);
					}
				}
			}
			else
			{
				// 	create the main file record
				AcordDataGroup msgHeader = (AcordDataGroup) transactions[0].getAttribute(AcordDataGroup.MESSAGE_HEADER);
				AcordDataGroup dbHeader = (AcordDataGroup) transactions[0].getAttribute("6DBD");

				if (agentid == null || agentid.trim().length() == 0)
				{
					CarrierInfo carrier = serverInfo.getCarrierInfo();
					if (carrier.getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME))
					{
						// lookup the agent id based on the filename
						String uniqueFile = filename;
						int n = uniqueFile.lastIndexOf(".");
						if (n > 0)
							uniqueFile = uniqueFile.substring(0, n);
						
						try
						{
							AgentInfo agentinfo = op.getAgentInfoForFilename(uniqueFile);
							if (agentinfo == null)
							{
								// check to see if a multi-header file index was added to the filename
								n = uniqueFile.indexOf("_(");
								if (n > 0)
									agentinfo = op.getAgentInfoForFilename(uniqueFile.substring(0,n));
							}
							if (agentinfo != null)
								agentid = agentinfo.getAgentId();
						}
						catch (Exception e) {
							LOGGER.error(e);
						}
					}
				}
				
				// if we don't find it, try to parse the agent id from the AL3 file
				if (agentid == null || agentid.trim().length() == 0)
					agentid = msgHeader.getElementValue("1MHG03").trim().toUpperCase();
				else if (!agentid.trim().equals(msgHeader.getElementValue("1MHG03").trim()))
				{
					String newAgent = agentid;
					while (newAgent.length() < 10)
						newAgent += " ";
					msgHeader.setElementValue("1MHG03", newAgent);
				}
	
				AgentInfo agentInfo = null;
				if (agentid != null && !agentid.equals(""))
				{
					try
					{
						participantCode = agentid;
						agentInfo = op.getAgentInfo(agentid);
						agentid = agentInfo.getAgentId();
						agentName = agentInfo.getName();
						agentLive = agentInfo.isLive();
						agentActive = agentInfo.isActive();
						//if (agentid.equals(participantCode))
						//	participantCode = "";
						
						// If a default filename is defined for the agent's vender
						// system, change the filename to match the default
						String defaultFilename = agentInfo.getDefaultFilename();
						if (defaultFilename == null || defaultFilename.equals(""))
							defaultFilename = agentInfo.getAms().getCompanyFilename();
						if (!testFile && defaultFilename != null && !defaultFilename.equals(""))
							filename = defaultFilename;
					}
					catch (Exception e) {}
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
						if (blockSequence == 0)
						{
							if (agentid == null || agentid.equals(""))
								agentid = "UNKNOWN";
							if (createdDate == 0)
								createdDate = importedDate;
							
							// Create a temporary file record
							fileinfo = op.createFile();
							fileinfo.setAgentId(agentid);
							fileinfo.setFilename(filename);
							fileinfo.setOriginalFilename(origFilename);
							fileinfo.setCreatedDate(createdDate);
							fileinfo.setImportedDate(importedDate);
							fileinfo.setDownloadStatus(DownloadStatus.CURRENT);
							if(transactions!= null && transactions.length>0)
								fileinfo.setTransactionCount(transactions.length);
							else
								fileinfo.setTransactionCount(0);
							
							fileinfo.setLastDownloadDate(0);
							fileinfo.setMessageSequence(0);
							fileinfo.setBatchNumber(batch_num);
							fileinfo.setHeader("");
							fileinfo.setFooter("");
							fileinfo.setTestFile(testFile);
							fileinfo.setParticipantCode(participantCode);
							fileinfo.setFileType(FileInfo.TYPE_ACORD);
							
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
					if (createdDate == 0)
						createdDate = importedDate;
					
					boolean goodFile = false;
					DownloadStatus dlStatus = null;
					int existingTransCount = 0;
					chunk = blockSequence+1;

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
						 msgFooter = (AcordDataGroup) transactions[0].getAttribute(AcordDataGroup.MESSAGE_FOOTER);
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
						if (testFile)
						{
							dlStatus = DownloadStatus.TEST;
						}
						else if (!agentLive)
						{
							dlStatus = DownloadStatus.ARCHIVED;
						}
						
						// Create the file record
						fileinfo = op.createFile();
						fileinfo.setAgentId(agentid);
						fileinfo.setFilename(filename);
						fileinfo.setOriginalFilename(origFilename);
						fileinfo.setCreatedDate(createdDate);
						fileinfo.setImportedDate(importedDate);
						fileinfo.setDownloadStatus(dlStatus);
						fileinfo.setLastDownloadDate(0);
						fileinfo.setMessageSequence(msgSeq);
						fileinfo.setHeader(header);
						fileinfo.setFooter(footer);
						fileinfo.setTestFile(testFile);
						fileinfo.setBatchNumber(batch_num);
						fileinfo.setFileComplete(fileComplete);
						fileinfo.setParticipantCode(participantCode);
						fileinfo.setFileType(FileInfo.TYPE_ACORD);
						
						
						
						// Check to see if this is a duplicate file in the database
						if (op.isDuplicateFile(agentid, origFilename, String.valueOf(createdDate)))
						{
							status.setStatus(FileImportStatus.REJECTED_DUPLICATE_FILE);
							status.setText("Duplicate file, import failed");
							if(transactions!= null && transactions.length>0)
								fileinfo.setTransactionCount(transactions.length);
							else
								fileinfo.setTransactionCount(0);
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
								 msgFooter = (AcordDataGroup) transactions[0].getAttribute(AcordDataGroup.MESSAGE_FOOTER);
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
						List<ExcludeLob> excludeList = null;
						String lobRoutingCode = null;
						boolean ignoreTransaction = false;
						invalidTransaction = new ArrayList<AcordDataGroup>();
						validTransaction = new ArrayList<AcordDataGroup>();
						logList = new ArrayList<LogTransInfo>();
						
						String exclusion = op.getPropertyValue(DatabaseFactory.PROP_EXCLUDE_LOB);
						if (exclusion != null && "Y".equals(exclusion))
						{
							checkForExclusion = true;
						}
						String transType1 = transactions[0].getElementValue("2TRG08");
						if (transType1.equals("DBR") || transType1.equals("DBS")){
							checkForExclusion = false;
						}
						if(checkForExclusion) {
						
							excludeList = op.getExcludeLobListOnly(agentid);
								
								for (int i=0; i < transactions.length; i++)
								{
									
									AcordDataGroup policyInfo = transactions[i].findGroupInTree("5BPI", "F1", 1);
									ignoreTransaction = false;
									lobRoutingCode = policyInfo.getElementValue("5BPI04")!=null?policyInfo.getElementValue("5BPI04").trim():"";
									
									for(ExcludeLob ex:excludeList) {
										if(lobRoutingCode.trim().equals(ex.getCode())){
											ignoreTransaction = true;
											invalidTransaction.add(transactions[i]);
											invalidLobMap.put(ex.getCode(), ex.getDescription());
											break;
										}
										
									}
									if(!ignoreTransaction){
										validTransaction.add(transactions[i]);
									}
								}
							}else {
								for (int i=0; i < transactions.length; i++)
								{
									validTransaction.add(transactions[i]);
								}
							}
						logList = new ArrayList<LogTransInfo>();
						LogTransInfo logInfo = null;
						// create the transaction records
						for (int i=0; i < validTransaction.size(); i++)
						{
							ignoreTransaction = false;

							// transaction type
							String transType = validTransaction.get(i).getElementValue("2TRG08");
					
								if (transType.equals("FMG") || transType.equals("PMG"))
									transType = validTransaction.get(i).getElementValue("2TRG25");
								String description = (String) serverInfo.getTransTypeHashtable().get(transType);
								
								// transaction sequence number
								int transSeq = 0;
								try
								{
									transSeq = Integer.parseInt(validTransaction.get(i).getElementValue("2TRG19"));
								}
								catch (Exception e) {
									LOGGER.error(e);
								}
			
								// transaction eff date
								String transEffDate = validTransaction.get(i).getElementValue("2TRG33").trim();
								if (transEffDate.equals("") || isQuestionMarks(transEffDate))
									transEffDate = validTransaction.get(i).getElementValue("2TRG23").trim();
								if (isQuestionMarks(transEffDate))
									transEffDate = "";
								if (transEffDate.length() == 8)
									transEffDate = transEffDate.substring(4, 6) + "/" + transEffDate.substring(6) + "/" + transEffDate.substring(0, 4);
								else if(transEffDate.length() == 6)
									transEffDate = transEffDate.substring(2, 4) + "/" + transEffDate.substring(4) + "/" + transEffDate.substring(0, 2);
			
								// file type (AL1 or AL3)								
								String fileType = validTransaction.get(i).getElementValue("2TRG04");
								
								//*** TEMP CODE ***
								if (!fileType.equals("1") && !fileType.equals("3"))
									fileType = "3";
								//*****************

								// named insured
								String namedInsured = "";
								String customerId = "";
								AcordDataGroup basicInsured = validTransaction.get(i).findGroupInTree("5BIS", "B1", 1);
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
								AcordDataGroup policyInfo = validTransaction.get(i).findGroupInTree("5BPI", "F1", 1);
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
										catch (Exception e) {}
									}
								}
								else
								{
									// If no 5BPI record, use the policy type for the line of business code
									lob = validTransaction.get(i).getElementValue("2TRG07").trim();
								}
								
								// transaction description
								if (transType.equals("MEM"))		// MEMO transaction
								{
									Vector memoList = validTransaction.get(i).findGroupInTree("6MEM");
									if (memoList != null && memoList.size() > 0)
									{
										AcordDataGroup memo = (AcordDataGroup) memoList.elementAt(0);
										String temp = memo.getElementValue("6MEM04").trim();
										if (!temp.equals(""))
											description = temp;
									}
									isDirectBillfile = false;
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
									isDirectBillfile = true;

								}
								else
								{
									AcordDataGroup transInfo = validTransaction.get(i).findGroupInTree("5ACT", "F2", 1);
									if (transInfo != null)
									{
										String transDesc = transInfo.getElementValue("5ACT05").trim();
										if (!transDesc.equals(""))
											description += ": " + stripApostrophes(transDesc);
									}
								}
								
								// Non-policy transactions
								if (isDirectBillfile)
								{
									DirectBillTransactionInfo info = null;								
									directBillList = validTransaction.get(i).findGroupInTree("6DBD");
									String dbPolicyEffectiveDate ="";
									String  dbPolicyExpiryDate = "";
									String dbTransactionDate ="";
									Double grossAmount = null;
									Double comissionRate = null;
									Double comissionAmount = null;
									Double comissionAdjustedAmount = null;
									
									String strGrossAmount = null;
									String strComissionAmount = null;
									String strComissionAdjustedAmount = null;
									
									int installMentNumber =0;
									int policyVersion =0;
									
									if(directBillList != null && directBillList.size()>0) {
										for(int k =0;k<directBillList.size();k++) {
											AcordDataGroup dbd = (AcordDataGroup) directBillList.elementAt(k);
											
											if(dbd.getElementValue("6DBD09") != null  || !"".equals(dbd.getElementValue("6DBD09"))) {
												strGrossAmount =  dbd.getElementValue("6DBD09");
												int len = strGrossAmount.length();
												char sign = strGrossAmount.charAt(len-1);
												grossAmount = Double.parseDouble(strGrossAmount.substring(0, len-1));
												if (sign == '-')
													grossAmount *= -1;
											}
											if(dbd.getElementValue("6DBD10") != null  || !"".equals(dbd.getElementValue("6DBD10"))) {
												comissionRate = Double.parseDouble(dbd.getElementValue("6DBD10").trim());
											}
											if(dbd.getElementValue("6DBD12") != null  || !"".equals(dbd.getElementValue("6DBD12"))) {
												strComissionAmount =  dbd.getElementValue("6DBD12");
												int len = strComissionAmount.length();
												char sign = strComissionAmount.charAt(len-1);
												comissionAmount = Double.parseDouble(strComissionAmount.substring(0, len-1));
												if (sign == '-')
													comissionAmount *= -1;
											}
											if(dbd.getElementValue("6DBD23") != null  || !"".equals(dbd.getElementValue("6DBD23"))) {
												strComissionAdjustedAmount =  dbd.getElementValue("6DBD12");
												int len = strComissionAdjustedAmount.length();
												char sign = strComissionAdjustedAmount.charAt(len-1);
												comissionAdjustedAmount = Double.parseDouble(strComissionAdjustedAmount.substring(0, len-1));
												if (sign == '-')
													comissionAdjustedAmount *= -1;

											}

											
											if(dbd.getElementValue("6DBD18") != null) {
												dbPolicyEffectiveDate = dbd.getElementValue("6DBD18").trim();
												if (dbPolicyEffectiveDate.length() == 8)
													dbPolicyEffectiveDate = dbPolicyEffectiveDate.substring(4, 6) + "/" + dbPolicyEffectiveDate.substring(6) + "/" + dbPolicyEffectiveDate.substring(0, 4);
												else if(dbPolicyEffectiveDate.length() == 6)
													dbPolicyEffectiveDate = dbPolicyEffectiveDate.substring(2, 4) + "/" + dbPolicyEffectiveDate.substring(4) + "/" + dbPolicyEffectiveDate.substring(0, 2);
											}
											if(dbd.getElementValue("6DBD19") != null) {
												dbPolicyExpiryDate = dbd.getElementValue("6DBD19").trim();
												if (dbPolicyExpiryDate.length() == 8)
													dbPolicyExpiryDate = dbPolicyExpiryDate.substring(4, 6) + "/" + dbPolicyExpiryDate.substring(6) + "/" + dbPolicyExpiryDate.substring(0, 4);
												else if(dbPolicyExpiryDate.length() == 6)
													dbPolicyExpiryDate = dbPolicyExpiryDate.substring(2, 4) + "/" + dbPolicyExpiryDate.substring(4) + "/" + dbPolicyExpiryDate.substring(0, 2);
											}


											if(dbd.getElementValue("6DBD07") != null) {
												dbTransactionDate = dbd.getElementValue("6DBD07").trim();
												if (dbTransactionDate.length() == 8)
													dbTransactionDate = dbTransactionDate.substring(4, 6) + "/" + dbTransactionDate.substring(6) + "/" + dbTransactionDate.substring(0, 4);
												else if(dbPolicyExpiryDate.length() == 6)
													dbTransactionDate = dbTransactionDate.substring(2, 4) + "/" + dbTransactionDate.substring(4) + "/" + dbTransactionDate.substring(0, 2);
											}
											
											if(dbd.getElementValue("6DBD21") != null || !"".equals(dbd.getElementValue("6DBD21"))) {
												String pversion = dbd.getElementValue("6DBD21");
												if(pversion!= null && pversion!="" && !pversion.contains("?")) {
													try{
														pversion = pversion.trim();
														if(!"".equals(pversion))
															policyVersion = Integer.parseInt(pversion);												
													}catch(NumberFormatException ex){
														LOGGER.error(ex);
														policyVersion = 0;
													}
												}

											}
											if(dbd.getElementValue("6DBD13") != null || !"".equals(dbd.getElementValue("6DBD13"))) {
												String install = dbd.getElementValue("6DBD13");
												if(install!= null && install!="" && !install.contains("?")) {
													try{
														install = install.trim();
														if(!"".equals(install))
														installMentNumber = Integer.parseInt(install);												
													}catch(NumberFormatException ex){
														LOGGER.error(ex);
														installMentNumber = 0;
													}
												}
											}
											info = new DirectBillTransactionInfo(op);
											if(dbd.getElementValue("6DBD01") != null && !dbd.getElementValue("6DBD01").contains("?"))
												info.setItemNumber(dbd.getElementValue("6DBD01"));
											if(dbd.getElementValue("6DBD02") != null && !dbd.getElementValue("6DBD02").contains("?"))
												info.setInsuredName(dbd.getElementValue("6DBD02"));
											if(agentid != null )
												info.setAgentId(agentid);
											if(dbd.getElementValue("6DBD04") != null && !dbd.getElementValue("6DBD04").contains("?"))
												info.setProducerSubCode(dbd.getElementValue("6DBD04"));
											if(dbd.getElementValue("6DBD05") != null && !dbd.getElementValue("6DBD05").contains("?"))
												info.setPolicyNumber(dbd.getElementValue("6DBD05"));
											if(dbd.getElementValue("6DBD06") != null && !dbd.getElementValue("6DBD06").contains("?"))
												info.setCompanyProducerCode(dbd.getElementValue("6DBD06"));
											if(dbd.getElementValue("6DBD07") != null && !dbd.getElementValue("6DBD07").contains("?"))
												info.setTransEffectiveDate(dbd.getElementValue("6DBD07"));
											if(dbd.getElementValue("6DBD08") != null && !dbd.getElementValue("6DBD08").contains("?"))
												info.setTransTypeCode(dbd.getElementValue("6DBD08"));
											if(dbd.getElementValue("6DBD09") != null && !dbd.getElementValue("6DBD09").contains("?"))
												info.setGrossAmount(grossAmount);
											if(dbd.getElementValue("6DBD13") != null && !dbd.getElementValue("6DBD13").contains("?"))
												info.setInstallMentNumber(installMentNumber);
											if(dbd.getElementValue("6DBD14") != null && !dbd.getElementValue("6DBD14").contains("?"))
												info.setPaymentPlanCode(dbd.getElementValue("6DBD14"));
											if(dbd.getElementValue("6DBD15") != null && !dbd.getElementValue("6DBD15").contains("?"))
												info.setLob(dbd.getElementValue("6DBD15"));
											if(dbd.getElementValue("6DBD16") != null && !dbd.getElementValue("6DBD16").contains("?"))
												info.setLobSubCode(dbd.getElementValue("6DBD16"));
											if(dbd.getElementValue("6DBD18") != null && !dbd.getElementValue("6DBD18").contains("?"))
												info.setPolicyEffectiveDate(dbPolicyEffectiveDate);
											if(dbd.getElementValue("6DBD19") != null && !dbd.getElementValue("6DBD19").contains("?"))
												info.setPolicyExpiryDate(dbPolicyExpiryDate);
											if(dbd.getElementValue("6DBD20") != null && !dbd.getElementValue("6DBD20").contains("?"))
												info.setBillingAccountNumber(dbd.getElementValue("6DBD20"));
											if(dbd.getElementValue("6DBD21") != null && !dbd.getElementValue("6DBD21").contains("?"))
												info.setPolicyVersion(policyVersion);
											if(dbd.getElementValue("6DBD23") != null && !dbd.getElementValue("6DBD23").contains("?"))
												info.setComissionAdjustedAmount(comissionAdjustedAmount);
											if(dbd.getElementValue("6DBD08") != null && !dbd.getElementValue("6DBD08").contains("?"))
												info.setFileType("DBS");
											if(dbd.getElementValue("6DBD10") != null && !dbd.getElementValue("6DBD10").contains("?"))
												info.setComissionRate(comissionRate);
											if(dbd.getElementValue("6DBD12") != null && !dbd.getElementValue("6DBD12").contains("?"))
												info.setComissionAmount(comissionAmount);

											info.setCreatedDate(fileinfo.getCreatedDate());
											info.setOriginalFilename(origFilename);
											info.save();
											
											 dbPolicyEffectiveDate ="";
											  dbPolicyExpiryDate = "";
											 dbTransactionDate ="";
											 grossAmount = null;
											 comissionRate = null;
											 comissionAmount = null;
											 comissionAdjustedAmount = null;
											
											 strGrossAmount = null;
											 strComissionAmount = null;
											 strComissionAdjustedAmount = null;
											
											 installMentNumber =0;
											 policyVersion =0;
											
										}
										//update transaction count in fileHdr
										op.updateDirectBillCounts(agentid, origFilename, createdDate, directBillList.size());
									}

									
									policyNumber = "(N/A)";
									namedInsured = "(" + description;
									String busPurpCd = validTransaction.get(i).getElementValue("2TRG25");
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
								out.print(validTransaction.get(i).getRawData());
								for (int j=0; j < validTransaction.get(i).getSubgroupCount(); j++)
								{
									String record = validTransaction.get(i).getSubgroup(j).getRawData();
									int recordLen = validTransaction.get(i).getSubgroup(j).getLength();
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
								
								logInfo = new LogTransInfo(transinfo,fileinfo);
								logInfo.setBatchnum(batch_num);
								logInfo.setEvent_type(DatabaseFactory.EVENT_IMPORT);
								logInfo.setFileName(fileinfo.getFilename());
								logInfo.setOrigFileName(fileinfo.getOriginalFilename());
								logInfo.setCreated_date(fileinfo.getCreatedDate());
								logList.add(logInfo);
	
						}
						
						// update the table with the total number of transactions in the file
						if (!isDirectBillfile)
						{
							fileinfo.updateTransactionCount();
						}
						
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
							if(checkForExclusion) {
								int dataCount = 0;
								String countUnitCode = msgHeader.getElementValue("1MHG09");
								if (countUnitCode != null && countUnitCode.equals("1") && validTransaction.size()>0)
								{
									// Count the number of element groups (ACORD records) in the file
									dataCount = 2;  // header and footer records
									dataCount += validTransaction.size() +existingTransCount;	// 2TRG records
									for (int i=0; i < validTransaction.size(); i++)
										dataCount += validTransaction.get(i).getSubgroupCount();
									String count = String.valueOf(dataCount).trim();
									while (count.length() < 8)
										count = "0" + count;
									
									// Update the footer record with the new "Total Data in
									// Message" counter
									//int previousCount = op.get3MTGCount(agentid, fileinfo.getOriginalFilename(), createdDate);
									msgFooter.setElementValue("3MTG01", count);
									msgFooter.rebuildRawData();
									
									String footer = msgFooter.getRawData();
									if ((msgFooter.getLength() - footer.length()) == 2)
										footer += CRLF;
									else
									{
										while (footer.length() < msgFooter.getLength())
											footer += " ";
									}
									fileinfo.setFooter(footer);
									fileinfo.updateFoter();
								}

							}
							// add to the log
							String desc;
							if (testFile)
								desc = "Test file import";
							else
							{
								if(invalidTransaction != null && invalidTransaction.size()== transactions.length && existingTransCount==0){
									if(blockSequence==0)
									desc = " File is not Imported as following line of business [" + invalidLobMap.toString()+"] has been excluded from list for agent: " +agentid;
									else 
										desc = " Chunk# "+chunk+" Status:: Total transactions in chunk: "+transactions.length+"+ Invalid Transaction in chunk :"+invalidTransaction.size()+" Transactions have not been Imported as following line of business [" + invalidLobMap.toString()+"] has been excluded from list for agent: " +agentid;
									
									status.setStatus(FileImportStatus.REJECTED_LOB_EXCLUSION);
									fileinfo.delete();
								}else if(invalidTransaction != null && invalidTransaction.size()>0){
									if(blockSequence==0)
									desc = " File is  Imported successful but out of total transactions "+transactions.length+", "+ invalidTransaction.size()+" transaction has been ignored as following line of business[" + invalidLobMap.toString()+"] has been excluded from import list for agent:" +agentid;
									else
										desc = " Chunk# "+chunk+" Status:: Total transactions in chunk: "+transactions.length+ " Invalid Transactions in chunk :"+invalidTransaction.size()+" Transaction have not been Imported as following line of business [" + invalidLobMap.toString()+"] has been excluded from list for agent: " +agentid;
									status.setStatus(FileImportStatus.PARTIAL_FILE_IMPORT);
								}else{
									if(blockSequence==0)
										desc = "Import successful, Total number of transactions: "+transactions.length;
									else
										desc = " Chunk# "+chunk+" Import successful, Total transactions in chunk: "+transactions.length;
								}

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
							if(isDirectBillfile){
								fileinfo.setTransactionCount(directBillList.size());
							}else{
								if(transactions!= null && transactions.length>0)
									fileinfo.setTransactionCount(transactions.length);
								else
									fileinfo.setTransactionCount(0);
							}
							
							
							op.logImportWithDetails(fileinfo, importStatus, batch_num, agentName,logList,DatabaseFactory.EVENT_IMPORT ,null);		
							return importStatus;
						} else {
							String desc;
							if (testFile)
								desc = "Test file import";
							else
							{
								
								if(invalidTransaction != null && invalidTransaction.size()== transactions.length && existingTransCount==0){
									desc = " Chunk# "+chunk+" Status:: Total transactions in chunk: "+transactions.length+" Invalid Transactions in chunk :"+invalidTransaction.size()+" Transaction have not been Imported as following line of business [" + invalidLobMap.toString()+"] has been excluded from list for agent: " +agentid;
									status.setStatus(FileImportStatus.REJECTED_LOB_EXCLUSION);
								}else if(invalidTransaction != null && invalidTransaction.size()>0){
									//desc = " File is  Imported successful but out of total transactions "+transactions.length+", "+ invalidTransaction.size()+" transaction has been ignored as following line of business[" + invalidLobMap.toString()+"] has been excluded from import list for agent:" +agentid;
									desc = " Chunk# "+chunk+" Status:: Total transactions in chunk: "+transactions.length+" Invalid Transactions in chunk :"+invalidTransaction.size()+" Transaction have not been Imported as following line of business [" + invalidLobMap.toString()+"] has been excluded from list for agent: " +agentid;
									status.setStatus(FileImportStatus.PARTIAL_FILE_IMPORT);
								}else{
									if(blockSequence==0 && !fileComplete )
										desc = " Chunk# "+blockSequence+1+" Import successful, Total transactions in chunk: "+transactions.length;	
									else
										desc = " Chunk# "+chunk+" Import successful, Total transactions in chunk: "+transactions.length;									
								}

						
							}
							FileImportStatus importStatus = new FileImportStatus(status.getStatus());
							importStatus.setText(desc);
							if(isDirectBillfile){
								fileinfo.setTransactionCount(directBillList.size());
							}else{
								if(transactions!= null && transactions.length>0)
									fileinfo.setTransactionCount(transactions.length);
								else
									fileinfo.setTransactionCount(0);
							}
							op.logImportWithDetails(fileinfo, importStatus, batch_num, agentName,logList,DatabaseFactory.EVENT_IMPORT,null );		
							return importStatus;
							
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
					if (filename != null && !filename.trim().equals("") && 
						serverInfo.getCarrierInfo().getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME))
					{
						int n = filename.lastIndexOf('.');
						if (n > 0)
							filename = filename.substring(0, n);
						AgentInfo agent = op.getAgentInfoForFilename(filename);
						if (agent == null)
						{
							// check to see if a multi-header file index was added to the filename
							n = filename.indexOf("_(");
							if (n > 0)
								agent = op.getAgentInfoForFilename(filename.substring(0,n));
						}
						if (agent != null)
						{
							agentid = agent.getAgentId();
							agentName = agent.getName();
						}
						else
							agentid = "UNKNOWN";
					}
					else
						agentid = "UNKNOWN";
				}
				if (createdDate == 0)
					createdDate = importedDate;
				fileinfo = op.createFile();
				fileinfo.setAgentId(agentid);
				fileinfo.setFilename(filename);
				fileinfo.setOriginalFilename(origFilename);
				fileinfo.setCreatedDate(createdDate);
				fileinfo.setImportedDate(importedDate);
				if(isDirectBillfile)
					fileinfo.setDownloadStatus(DownloadStatus.DB_CURRENT);
				else
					fileinfo.setDownloadStatus(DownloadStatus.CURRENT);
				
				fileinfo.setLastDownloadDate(0);
				fileinfo.setMessageSequence(0);
				fileinfo.setBatchNumber(batch_num);
				fileinfo.setHeader("");
				fileinfo.setFooter("");
				fileinfo.setTestFile(testFile);
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
			String stackTrace = null;
			try
			{
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(os);
				e.printStackTrace(ps);
				stackTrace = os.toString();
				ps.close();
				os.close();
				
				// Add the stack trace to the email body
				if (body != null && !body.equals("") && stackTrace != null && !stackTrace.equals(""))
					body += "\n\n---------------\n\nSTACK TRACE:\n\n" + stackTrace;
			} catch (IOException ioe) {
				LOGGER.error(ioe);
			}
			
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
