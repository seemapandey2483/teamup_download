package connective.teamup.download.actions;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.XmlExport;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileImportStatus;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.db.LogTransInfo;
import connective.teamup.download.db.TransactionInfo;
import connective.teamup.download.services.EmailService;

public class ImportAcordPolicyXmlBase {
	private static final Logger LOGGER = Logger.getLogger(ImportAcordPolicyXmlBase.class);
	private static final String POLICY_XML ="P";
	private static final String TRANSCLASS ="TransactionInfo";
	/**
	 * Constructor for ImportAcordXmlBase.
	 */
	public ImportAcordPolicyXmlBase()
	{
		super();
	}

	/**
	 * Import an ACORD XML file.
	 * 
	 * @param serverInfo The server info bean
	 * @param filename The original name of the file being imported
	 * @param createdDate The original created date of the file being imported
	 * @param testFile True if this is a test file import
	 * @param interactive True if this is an interactive import, false for scheduled/automated import
	 * @param batch_num The import batch number (only used for automated/batch imports)
	 * @param xmlData The ACORD XML data to be imported
	 * @param blockSequence The sequence number when importing files in blocks; zero for first block (or entire file)
	 * 
	 * @return connective.teamup.download.FileImportStatus
	 */
	protected FileImportStatus importFile(DatabaseOperation op, ServerInfo serverInfo, String filename, 
											long createdDate, boolean testFile, boolean interactive, 
											int batch_num, String xmlData, int blockSequence,String xmlHeader) throws Exception
	{
		ImportActionHelper importHelper = new ImportActionHelper(op, serverInfo, FileInfo.TYPE_XML, 
																 createdDate, batch_num, testFile);
		FileImportStatus status = new FileImportStatus();
		List<LogTransInfo> logList = null;
		
		boolean agentLive = false;
		boolean agentActive = false;
		String agentid = "";
		String participantCode = "";
		String agentName = "";
		long importedDate = System.currentTimeMillis();
		Map<String,Element> parentMap = new HashMap<String,Element>();
		// Strip path out of filename
		filename = importHelper.getFileName(filename);
		String origFilename = filename;
		List<Element> transactions = null;
		
		FileInfo fileinfo = null;
		Document doc = null;
		try {
			XMLImportHelper.agentId = null;
			// parse the xml data
			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(new ByteArrayInputStream(xmlData.getBytes("UTF-8")));
		}
		catch (Exception e) {
			LOGGER.error("Policy XML: "+e);
			// Error occurred trying to parse the xml data
			String msg = "Error occurred parsing Policy XML file: "+filename+" during import :" +e.getMessage();
			status.setStatus(FileImportStatus.FAILED_XML_DATA_BAD);
			status.setText(msg);
			status.setException(e);
			
			importHelper.reportImportError(agentid, agentName, participantCode, msg, filename, 
							origFilename, true, status, e,0,DownloadStatus.POLICYXML_CURRENT);
			
			return status;
		}
		
		try {
			// check for ACORD XML Claims Service format
			Element root = doc.getRootElement();
			String policyXMLParentNode = op.getPropertyValue(DatabaseFactory.PROP_POLICYXML_PARENT_NODE);
			if(StringUtils.isBlank(policyXMLParentNode)){
				String msg = "There is no mapping defined for Parent Node in System.";
				status.setStatus(FileImportStatus.REJECTED_XML_NOT_SUPPORTED);
				status.setText(msg);
				if (batch_num > 0) {
					importHelper.reportImportError(agentid, agentName, participantCode, msg, 
							filename, origFilename, interactive, status, null,transactions.size(),DownloadStatus.CLAIM_CURRENT );
				}
				return status;
			}
			String policyXMLTransNode = op.getPropertyValue(DatabaseFactory.PROP_POLICYXML_TRANSACTION_NODE);
			
			if(StringUtils.isBlank(policyXMLTransNode)){
				String msg = "There is no mapping defined for Transaction node Claims System.";
				status.setStatus(FileImportStatus.REJECTED_XML_NOT_SUPPORTED);
				status.setText(msg);
				if (batch_num > 0) {
					importHelper.reportImportError(agentid, agentName, participantCode, msg, 
							filename, origFilename, interactive, status, null,transactions.size(),DownloadStatus.CLAIM_CURRENT );
				}
				return status;
			}
			
			Element eclaimSvcRs = root.getChild(policyXMLParentNode);
			parentMap.put(policyXMLParentNode, eclaimSvcRs);
			
			transactions =  eclaimSvcRs.getChildren(policyXMLTransNode);
			
			if (transactions == null || transactions.size() == 0) {
				// there were no transactions in the xml file
				String msg = "No Policy transactions defined in the XML file:" +filename;
				status.setStatus(FileImportStatus.FAILED_XML_NO_TRANSACTIONS);
				status.setText(msg);
				
				if (batch_num > 0) {
					importHelper.reportImportError(agentid, agentName, participantCode, msg, filename, 
									origFilename, interactive, status, null,0,DownloadStatus.POLICYXML_CURRENT);
				}
				return status;
			}
			//ClaimDAO.prepareList();
			List<XmlExport> policyMapList = op.getClaimMapping(POLICY_XML);
			if(policyMapList== null || policyMapList.size()==0) {
				String msg = "There is no mapping defined for Policy XML in System.";
				status.setStatus(FileImportStatus.REJECTED_XML_NOT_SUPPORTED);
				status.setText(msg);
				if (batch_num > 0) {
					importHelper.reportImportError(agentid, agentName, participantCode, msg, 
							filename, origFilename, interactive, status, null,transactions.size() ,DownloadStatus.POLICYXML_CURRENT);
				}
				return status;
			}
			// get the agent id, check to make sure all transactions are for the same agent id
			XmlExport agentMapNode = XMLImportHelper.AgentMappingNode(policyMapList);
			if(XMLImportHelper.checkForMultipleAgent(transactions,agentMapNode)){
				String msg = "Policy XML File#  " +filename+" contained transactions for multiple agents / contract numbers";
				status.setStatus(FileImportStatus.FAILED_MULTIPLE_AGENTS_IN_FILE);
				status.setText(msg);
				if (batch_num > 0) {
					importHelper.reportImportError(agentid, agentName, participantCode, msg, 
							filename, origFilename, interactive, status, null,transactions.size(),DownloadStatus.POLICYXML_CURRENT );
				}
				return status;
			}
			agentid = XMLImportHelper.agentId;

			AgentInfo agentInfo = null;
			if (agentid != null && !agentid.equals("")) {
				agentInfo = importHelper.getAgentInfo(filename, agentid);
				if (agentInfo != null) {
					try {
						participantCode = agentid;
						agentid = agentInfo.getAgentId();
						agentName = agentInfo.getName();
						agentLive = agentInfo.isLive();
						agentActive = agentInfo.isActive();
						
						if (agentInfo.getAms() == null || !agentInfo.getAms().isPolicyXMLSupported()) {
							String msg = "For AgentId:" + agentid + ",Vendor system: "+agentInfo.getAms().getVendor()+"-"+agentInfo.getAms().getName()+ " does not support Policy XML download";
							status.setStatus(FileImportStatus.REJECTED_XML_NOT_SUPPORTED);
							status.setText(msg);
							if (batch_num > 0) {
								importHelper.reportImportError(agentid, agentName, participantCode, msg, 
										filename, origFilename, interactive, status, null,transactions.size() ,DownloadStatus.POLICYXML_CURRENT);
							}
							return status;
						}
						
						// If a default filename is defined for the agent's vender
						// system, change the filename to match the default
						String defaultFilename = agentInfo.getDefaultPolicyFilename();
						if (defaultFilename == null || defaultFilename.equals(""))
							defaultFilename = agentInfo.getAms().getCompanyClaimFilename();
						if (!testFile && defaultFilename != null && !defaultFilename.equals(""))
							filename = defaultFilename;
					}
					catch (Exception e) {
						LOGGER.error("Policy XML: "+e);
					}
				}
			}
			
			if (agentInfo == null || !agentActive) {
				if (agentInfo == null) {
					status.setStatus(FileImportStatus.REJECTED_AGENT_UNKNOWN);
					status.setText("Unknown agent id");
				}
				else {
					status.setStatus(FileImportStatus.REJECTED_AGENT_INACTIVE);
					status.setText("AgentId=" + agentInfo.getAgentId() + ",Agent is currently disabled in TEAM-UP Download");
				}
				
				if (batch_num > 0) {
					// Create a temporary file record
					fileinfo = importHelper.createFileInfo(filename, origFilename, agentid);
					fileinfo.setParticipantCode(participantCode);
					fileinfo.setDownloadStatus(DownloadStatus.POLICYXML_CURRENT);
					// Create the error message
					String msg = "AgentId=" + agentid + ",";
					if (agentInfo == null && serverInfo.getCarrierInfo().getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME)) {
						msg = "No trading partner was found for this ";
						if (serverInfo.getCarrierInfo().getImportFileCreator().equals("KEYLINK"))
							msg += "KeyLink file";
						else
							msg += "file name";
					}
					else if (agentid.equalsIgnoreCase("UNKNOWN"))
						msg += "Policy XML Import process could not identify the trading partner for this file";
					else if (agentInfo == null)
						msg += "This trading partner has not yet been registered for TEAM-UP Download";
					else
						msg += "This trading partner is currently flagged as disabled; import was not processed";
					
					FileImportStatus failStatus = new FileImportStatus(status.getStatus());
					failStatus.setText(msg);
					
					// Log the file as a failed import.  Only log if import is
					// being done in interactive mode, OR if the file is matched
					// to an agent in the database (disabled)
					importHelper.reportImportError(agentid, agentName, participantCode, msg, 
							filename, origFilename, interactive, failStatus, null,transactions.size() ,DownloadStatus.POLICYXML_CURRENT);
				}
				
				return status;
			}
			
			boolean goodFile = false;
			DownloadStatus dlStatus = null;
			
			// Create a new file header record
			int msgSeq = 0;
			try {
				XmlExport msgSeqtMapNode = XMLImportHelper.MsgSequenceMappingNode(policyMapList);
				String strMsgSeq = (String)XMLImportHelper.getElementValue(eclaimSvcRs,msgSeqtMapNode.getNodeName(),msgSeqtMapNode.getDataType());
				msgSeq = Integer.parseInt(strMsgSeq);
			}
			catch (Exception e) {
				LOGGER.error(e);
			}
			
			// Set download flags -- if agent is not live, import as archive file only
			if (testFile)
				dlStatus = DownloadStatus.TEST;
			else if (!agentLive)
				dlStatus = DownloadStatus.POLICYXML_ARCHIVED;
			else
				dlStatus = DownloadStatus.POLICYXML_CURRENT;
			
			// Create the file record
			fileinfo = importHelper.createFileInfo(filename, origFilename, agentid);
			fileinfo.setDownloadStatus(dlStatus);
			fileinfo.setMessageSequence(msgSeq);
			fileinfo.setHeader("");
			fileinfo.setFooter("");
			fileinfo.setFileComplete(true);
			fileinfo.setParticipantCode(participantCode);
			
			// Check to see if this is a duplicate file in the database
			if (op.isDuplicateFile(agentid, origFilename, String.valueOf(createdDate)))
			{
				status.setStatus(FileImportStatus.REJECTED_DUPLICATE_FILE);
				status.setText("AgentId=" + agentInfo.getAgentId() + ", Duplicate file, Policy XML import failed");
				fileinfo.setTransactionCount(transactions.size());
				op.logImport(fileinfo, status, batch_num, agentName);
			}
			else
			{
				fileinfo.save();
				goodFile = true;
			}
				
			if (goodFile) {
				// save the xml header as a special transaction record
				String tag = "<" + ((Element)transactions.get(0)).getName() + ">";
				int n = xmlData.indexOf(tag);
				if (n > 0) {
					// create the xml header transaction record
					fileinfo.createClaimXmlHeader(xmlHeader,xmlData.substring(0,n));
					
					// trim the xml header off the remaining file data
					xmlData = xmlData.substring(n);
				}
				int counter =0;
				logList = new ArrayList<LogTransInfo>();
				LogTransInfo logInfo = null;
				for (Element element :transactions) {
					Object value = null;
					TransactionInfo transinfo = fileinfo.createPolicyXMLTransaction();
					for(XmlExport cEx:policyMapList) {
						if(TRANSCLASS.equals(cEx.getClasssName())){
							if(parentMap.containsKey(cEx.getParentNode())){
								value = XMLImportHelper.getElementValue(parentMap.get(cEx.getParentNode()),cEx.getNodeName(),cEx.getDataType());
							}else {
								value = XMLImportHelper.getElementValue(element,cEx.getNodeName(),cEx.getDataType());									
							}
							PropertyUtils.setProperty(transinfo, cEx.getElementName(), value);
						}
						
					}
					
					
					// create the transaction record
					transinfo.setSequence(counter + 1);
					transinfo.setDownloadStatus(dlStatus);
					//transinfo.setLastDownloadDate(0);
					transinfo.setNamedInsured(transinfo.getInsured().getInsuredName());
					//transinfo.setCustomerId(notNull(customerId));
					//transinfo.setLob(transinfo.getLob());
					//transinfo.setPolicyEffDate(notNull(transinfo.getPolicyEffDate()));
					//transinfo.setPolicyExpirationDate(notNull(transinfo.getPolicyExpirationDate()));
					//transinfo.setPolicyNumber(notNull(transinfo.getPolicyNumber()));
					//transinfo.setTransType(notNull(transType));
				//	transinfo.setTransSequence(transinfo.getTransSequence());
				//	transinfo.setTransType(transinfo.getClaimInfo().getClaimTypeCd());
				///	transinfo.setTransEffDate(notNull(transEffDate));
				//	transinfo.setTransPremium(transPremium);
					//transinfo.setDescription(notNull(description));
					transinfo.save();
					//transinfo.getClaimInfo().save();
					counter++;
					// write the transaction data to the database
					tag = "</" + element.getName() + ">";
					n = xmlData.indexOf(tag);
					if (n > 0) {
						n += tag.length();
						ByteArrayInputStream bis = null;
						if (n == xmlData.length()) {
							bis = new ByteArrayInputStream(xmlData.getBytes());
							xmlData = "";
						} else {
							bis = new ByteArrayInputStream(xmlData.substring(0,n).getBytes());
							xmlData = xmlData.substring(n);
						}
					transinfo.writeTransactionData(bis, n);
					}
					
					
					logInfo = new LogTransInfo(transinfo,fileinfo);
					logInfo.setBatchnum(batch_num);
					logInfo.setEvent_type(DatabaseFactory.EVENT_IMPORT);
					logInfo.setFileName(fileinfo.getFilename());
					logInfo.setOrigFileName(fileinfo.getOriginalFilename());
					logInfo.setCreated_date(fileinfo.getCreatedDate());
					logList.add(logInfo);
					
				}
				
				
				
	
				// save the xml footer as a special transaction record
				if (xmlData.length() > 0) {
					fileinfo.createClaimXmlFooter(xmlData);
				}
				// update the table with the total number of transactions in the file
				fileinfo.updateTransactionCount();
				if (!fileinfo.isFileComplete()) {
					// update the header record
					fileinfo.setFileComplete(true);
					//fileinfo.updateFileComplete();
				}
				
				// Set the import status to successful
				if (agentLive || serverInfo.getCarrierInfo().isDeleteAllImportedFiles())
					status.setStatus(FileImportStatus.IMPORTED_DELETE_FILE);
				else
					status.setStatus(FileImportStatus.IMPORTED_LEAVE_FILE);
				
				// add to the log
				String desc;
				if (testFile)
					desc = "Test file import";
				else
				{
					desc = "AgentId=" + agentInfo.getAgentId() + ",Import Policy XML file [ " +origFilename+ "] successful";
					if (fileinfo.getDownloadStatus().equals(DownloadStatus.POLICYXML_ARCHIVED) ||
								fileinfo.getDownloadStatus().equals(DownloadStatus.DB_ARCHIVED))
						desc += ", Policy XML file [ " +origFilename+ "] archived";
					if (serverInfo.getCarrierInfo().isDeleteAllImportedFiles())
						desc += "; original Policy XML file [ " +origFilename+ "] deleted from source directory";
					else if (agentLive)
						desc += "; original Policy XML file [ " +origFilename+ "] deleted from source directory for live agent";
				}
				FileImportStatus importStatus = new FileImportStatus(status.getStatus());
				importStatus.setText(desc);
				fileinfo.setTransactionCount(transactions.size());
				op.logImportWithDetails(fileinfo, importStatus, batch_num, agentName,logList,DatabaseFactory.EVENT_IMPORT,null);

			}
		}
		catch (Exception e)
		{
			LOGGER.error("Policy XML:" +e);
			String msg = "";
			String msgText = e.getMessage();
			String body = "";
			
			// Save the exception to the import status object
			status.setException(e);
			
			if (fileinfo == null)
			{
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
				fileinfo.setDownloadStatus(DownloadStatus.POLICYXML_CURRENT);
				fileinfo.setParticipantCode(participantCode);
				if (transactions == null || transactions.size() == 0) 
					fileinfo.setTransactionCount(0);
				else
					fileinfo.setTransactionCount(transactions.size());
				
				// Error occurred trying to parse the xml file
				msg = "Error occurred parsing the Policy XML data during import.";
				String resultMsg = "AgentId=" + agentid + "," + msg;
				status.setStatus(FileImportStatus.FAILED_XML_DATA_BAD);
				status.setText(resultMsg);
				
				// Log the file as a failed import.  Only log if import is
				// being done in interactive mode, OR if the file is matched
				// to an agent in the database (disabled)
				body = msg + "\n\n  Application:  ";
				if (serverInfo == null)
					body += "Import service";
				else
					body += serverInfo.getAppName();
				body += "\n  Policy XML Filename:  " + filename +
						"\n  Agent ID:  " + agentid +
						"\n\n" + msgText;
			}
			else
			{
				// Error most likely occurred trying to save the file 
				// to the database table
				status.setStatus(FileImportStatus.FAILED_OTHER);
				msg = "Import error: " + e.getMessage();
				String resultMsg = msg;
				if (agentid != null && !agentid.equals(""))
					resultMsg = "AgentId=" + agentid + "," + msg;
				status.setText(resultMsg);
				
				// Attempt to delete the (current) offending or partial file
				int badFileCount = 0;
				try
				{
					fileinfo.delete();
					if (fileinfo.getFilename() != null)
						msgText += "\nDue to this error, this Policy XML file was deleted from the TEAM-UP database:  " + fileinfo.getFilename();
					badFileCount++;
				}
				catch (Exception e2) {
					LOGGER.error("Policy XML: "+e2);
				}
				
				// Find and delete any other partially imported files in this batch
				FileInfo[] badFiles = op.getIncompleteFilesForBatch(batch_num);
				for (int i=0; i < badFiles.length; i++)
				{
					badFileCount++;
					if (fileinfo.getFilename()!= null && fileinfo.getFilename().equals(badFiles[i].getFilename()))
						msgText += "\nDue to this error, this Policy XML file was deleted from the TEAM-UP database:  " + fileinfo.getFilename();
					badFiles[i].delete();
				}
				if (badFileCount > 1)
					msgText += "\n\nA total of " + badFileCount + " incomplete Policy XML files were deleted from the database.";
					
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

	private String notNull(String text)
	{
		if (text == null)
			return "";
		return text;
	}

	private String parseName(Element nameInfo)
	{
		String ret = null;
		
		if (nameInfo != null) {
			Element elName = nameInfo.getChild("CommlName");
			if (elName != null) {
				ret = elName.getChildText("CommercialName");
			}
			
			if (ret == null || ret.equals("")) {
				elName = nameInfo.getChild("PersonName");
				if (elName == null)
					elName = nameInfo.getChild("FamilyName");
				
				if (elName != null) {
					String prefix = elName.getChildText("TitlePrefix");
					String first = elName.getChildText("GivenName");
					if ((first == null || first.equals("")) && elName.getName().equals("FamilyName"))
						first = elName.getChildText("FamilyNames");
					String last = elName.getChildText("Surname");
					String suffix = elName.getChildText("NameSuffix");
					
					ret = "";
					if (first != null && !first.equals(""))
						ret = first + " ";
					if (last != null)
						ret += last;
					if (!ret.equals("")) {
						if (prefix != null && !prefix.equals(""))
							ret = prefix + " " + ret;
						if (suffix != null && !suffix.equals(""))
							ret += ", " + suffix;
					}
				}
			}
		}
		
		return ret == null ? "" : ret;
	}

}
