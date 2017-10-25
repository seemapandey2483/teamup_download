package connective.teamup.download.actions;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;

import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileImportStatus;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.db.TransactionInfo;

/**
 * Action to import an ACORD XML file and store it in the database for download by the agent.
 * 
 * @author kmccreary
 */
public class AcordXmlImportHelper
{
	private static final Logger LOGGER = Logger.getLogger(AcordXmlImportHelper.class);
	
	private DatabaseOperation op = null;
	private ServerInfo serverInfo = null;
	private InputStream instr = null;
	private FileInfo fileInfo = null;
	private FileImportStatus status = null;
	private String filename = null;
	private String origFilename = null;
	private String defaultAgent = null;
	private String agentid = null;
	private String participantCode = null;
	private String agentName = null;
	private int batchNum = 0;
	private int transCount = 0;
	private int seq = 0;
	private int blockSequence = 0;
	private long createdDate = 0;
	private long importedDate = 0;
	private boolean testFile = false;
	private boolean interactive = false;
	private boolean fileComplete = false;
	private boolean agentLive = false;
	private boolean agentActive = false;


	/**
	 * Constructor for AcordXmlImportHelper.
	 */
	public AcordXmlImportHelper()
	{
		super();
		
		this.participantCode = "";
		this.agentName = "";
		this.importedDate = System.currentTimeMillis();
		this.status = new FileImportStatus();
	}

	/**
	 * Import an ACORD-XML file.
	 *
	 * @param op The database operation
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
	protected FileImportStatus importFile(DatabaseOperation op, ServerInfo serverInfo, String filename, String defaultAgent, long createdDate, boolean testFile, boolean interactive, int batchNum, InputStream instr, int transCount, int blockSequence, boolean fileComplete) throws Exception
	{
		this.op = op;
		this.serverInfo = serverInfo;
		this.filename = filename;
		this.defaultAgent = defaultAgent;
		this.agentid = defaultAgent;
		this.createdDate = createdDate;
		this.testFile = testFile;
		this.interactive = interactive;
		this.batchNum = batchNum;
		this.instr = instr;
		this.transCount = transCount;
		this.blockSequence = blockSequence;
		this.fileComplete = fileComplete;
		
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
		this.origFilename = filename;
		
		
		
		// initialize the ACORD XML parser
		AcordXmlHandler handler = new AcordXmlHandler(this);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(instr, handler);
		
		
		if (seq > 0)
		{
			// update transaction count file header in db
			if (fileInfo != null)
				fileInfo.updateTransactionCount();
		}
		
		return status;
	}

	public void saveFileHeader(String header, String agentId, String msgSeq)
	{
		// TODO - save all XML up to first transaction
		
	}

	public void updateFileHeader(String footer) throws Exception
	{
		// update the table with the total number of transactions in the file
		fileInfo.updateTransactionCount();
		
		// update the file header record
		fileInfo.setFooter(footer);
		fileInfo.setFileComplete(true);
		fileInfo.save();
		
		// Set the import status to successful
		if (fileComplete && (agentLive || 
							 serverInfo.getCarrierInfo().isDeleteAllImportedFiles()))
			status.setStatus(FileImportStatus.IMPORTED_DELETE_FILE);
		else
			status.setStatus(FileImportStatus.IMPORTED_LEAVE_FILE);
		
		// add to the log
		String desc;
		if (testFile)
			desc = "Test file import";
		else
		{
			desc = "Import successful";
			if (fileInfo.getDownloadStatus().equals(DownloadStatus.ARCHIVED) ||
					fileInfo.getDownloadStatus().equals(DownloadStatus.DB_ARCHIVED))
				desc += ", file archived";
			if (serverInfo.getCarrierInfo().isDeleteAllImportedFiles())
				desc += "; original file deleted from source directory";
			else if (agentLive)
				desc += "; original file deleted from source directory for live agent";
		}
		FileImportStatus importStatus = new FileImportStatus(status.getStatus());
		importStatus.setText(desc);
		op.logImport(fileInfo, importStatus, batchNum, agentName);						
		
		// reset the file info and transaction sequence
		fileInfo = null;
		seq = 0;
	}

	public void saveTransaction(String namedInsured, String customerId, String lob, String policyEffDt, String policyNum, String transType, String transSeq, String transEffDt, String desc, String rawTrans, double premiumAmt) throws Exception
	{
		boolean goodFile = true;
		
		if (fileInfo == null)
		{
			if (blockSequence == 0)
			{
				// Set download flags -- if agent is not live, import as archive file only
				DownloadStatus dlStatus = DownloadStatus.CURRENT;
				if (testFile)
				{
					dlStatus = DownloadStatus.TEST;
				}
				else if (!agentLive)
				{
					dlStatus = DownloadStatus.ARCHIVED;
				}
				
				// Create the new file header record
				fileInfo = op.createFile();
				fileInfo.setAgentId(agentid);
				fileInfo.setFilename(filename);
				fileInfo.setOriginalFilename(origFilename);
				fileInfo.setCreatedDate(createdDate);
				fileInfo.setImportedDate(importedDate);
				fileInfo.setDownloadStatus(dlStatus);
				fileInfo.setLastDownloadDate(0);
				fileInfo.setMessageSequence(0);
				fileInfo.setHeader("");
				fileInfo.setFooter("");
				fileInfo.setTestFile(testFile);
				fileInfo.setBatchNumber(batchNum);
				fileInfo.setFileComplete(fileComplete);
				fileInfo.setParticipantCode(participantCode);
				
				seq = 0;
				
				// Check to see if this is a duplicate file in the database
				if (op.isDuplicateFile(agentid, origFilename, String.valueOf(createdDate)))
				{
					status.setStatus(FileImportStatus.REJECTED_DUPLICATE_FILE);
					status.setText("Duplicate file, import failed");
					op.logImport(fileInfo, status, batchNum, agentName);
				}
				else
				{
					fileInfo.save();
					goodFile = true;
				}
			}
			else
			{
				// retrieve the existing header record for this file
				fileInfo = op.getDownloadFile(agentid, origFilename, createdDate);
				if (fileInfo != null)
				{
					// Load the existing transactions
					fileInfo.loadTransFromDb();
					seq = fileInfo.getTransactionCount();
					goodFile = true;
				}
				else
				{
					status.setStatus(FileImportStatus.FAILED_XML_DATA_BAD);
					status.setText("Import failed: partial file received (block " + 
									blockSequence + "), missing data");
					op.logImport(fileInfo, status, batchNum, agentName);
					goodFile = false;
				}
			}
		}
		
		if (!goodFile)
		{
			// TODO - handle file problem here!!
		}
		
		// create new transaction record
		seq++;
		
		if (desc == null || desc.equals(""))
			desc = transType;
		if (transType.indexOf("claim") >= 0)
			transType = "";
		// TODO - do other transaction element translations here
		else if (transType.length() > 3)
			transType = transType.substring(0,3);
		
		int nTransSeq = 0;
		try {
			nTransSeq = Integer.parseInt(transSeq);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			nTransSeq = 0;
		}
		
		TransactionInfo transaction = fileInfo.createTransaction();
		transaction.setCustomerId(notNull(customerId));
		transaction.setData(rawTrans);
		transaction.setDescription(notNull(desc));
		transaction.setDownloadStatus(fileInfo.getDownloadStatus());
		transaction.setFileType(TransactionInfo.TYPE_XML);
		transaction.setLob(notNull(lob));
		transaction.setNamedInsured(notNull(namedInsured));
		transaction.setPolicyEffDate(notNull(policyEffDt));
		transaction.setPolicyNumber(notNull(policyNum));
		transaction.setSequence(seq);
		transaction.setTransEffDate(notNull(transEffDt));
		transaction.setTransPremium(premiumAmt);
		transaction.setTransSequence(nTransSeq);
		transaction.setTransType(transType);
		
		try
		{
			transaction.save();
			transaction.writeTransactionData();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			// TODO - NEED TO HANDLE EXCEPTION HERE!!!
			e.printStackTrace();
		}
	}

	private String notNull(String text)
	{
		if (text == null)
			return "";
		return text;
	}

}
