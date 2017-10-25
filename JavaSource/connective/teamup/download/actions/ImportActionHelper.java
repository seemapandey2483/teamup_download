package connective.teamup.download.actions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileEventStatus;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.services.EmailService;

/**
 * @author kmccreary
 * 
 * Helper class to consolidate the repeated functions needed during file import.
 */
public class ImportActionHelper {
	
	private static final Logger LOGGER = Logger.getLogger(ImportActionHelper.class);
	
	private DatabaseOperation op = null;
	private ServerInfo serverInfo = null;
	private AgentInfo agentInfo = null;
	private String fileType = "";
	private long fileCreatedDate = 0L;
	private long fileImportedDate = 0L;
	private int batchNumber = 0;
	private boolean testFile = false;

	/**
	 * Constructor for ImportActionHelper.
	 */
	public ImportActionHelper(DatabaseOperation op, ServerInfo serverInfo, String fileType, 
							  long fileCreatedDate, int batchNumber, boolean testFile) {
		super();
		
		this.op = op;
		this.serverInfo = serverInfo;
		this.fileType = fileType;
		this.batchNumber = batchNumber;
		this.testFile = testFile;
		this.fileImportedDate = System.currentTimeMillis();
		this.fileCreatedDate = fileCreatedDate;
		if (this.fileCreatedDate == 0)
			this.fileCreatedDate = fileImportedDate;
	}

	/**
	 * Strip the path out of the original filename.
	 * 
	 * @param filename - the original name (including path) of the file being imported
	 */
	public String getFileName(String filename) {
		String ret = "";
		if (filename != null)
		{
			ret = filename.trim().toUpperCase();
			int n = ret.lastIndexOf("/");
			if (n >= 0 && n < ret.length())
				ret = ret.substring(n+1);
			n = ret.lastIndexOf("\\");
			if (n >= 0 && n < ret.length())
				ret = ret.substring(n+1);
		}
		return ret;
	}

	/**
	 * Create a default file info object.
	 * @return
	 */
	public FileInfo createFileInfo(String filename, String origFilename, String agentId) {
		FileInfo fileinfo = op.createFile();
		fileinfo.setAgentId(agentId == null ? "" : agentId);
		fileinfo.setFilename(filename);
		fileinfo.setOriginalFilename(origFilename);
		fileinfo.setCreatedDate(fileCreatedDate);
		fileinfo.setImportedDate(fileImportedDate);
		fileinfo.setDownloadStatus(DownloadStatus.HOLD);
		fileinfo.setLastDownloadDate(0);
		fileinfo.setMessageSequence(0);
		fileinfo.setBatchNumber(batchNumber);
		fileinfo.setHeader("");
		fileinfo.setFooter("");
		fileinfo.setTestFile(testFile);
		fileinfo.setParticipantCode("");
		fileinfo.setFileType(fileType);
		
		return fileinfo;
	}

	/**
	 * Returns TRUE if agent lookup is done using a unique filename; FALSE if agent lookup is
	 * done using an agent id or participant code.
	 */
	public boolean isAgentLookupByFilename() {
		return serverInfo.getCarrierInfo().getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME);
	}

	/**
	 * Does a dataabase lookup of the agent information using the filename and/or agent id, 
	 * depending on the configuration.
	 * 
	 * @param filename
	 * @param agentId
	 */
	public AgentInfo getAgentInfo(String filename, String agentId) throws SQLException {
		
		if (agentInfo == null && filename != null && !filename.equals("") && isAgentLookupByFilename()) {
			// try to find the agent using the unique filename
			int n = filename.lastIndexOf('.');
			if (n > 0)
				filename = filename.substring(0, n);
			agentInfo = op.getAgentInfoForFilename(filename);
			
			if (agentInfo == null) {
				// check to see if a multi-header file index was added to the filename
				n = filename.indexOf("_(");
				if (n > 0)
					agentInfo = op.getAgentInfoForFilename(filename.substring(0,n));
			}
		}
		
		if (agentInfo == null && agentId != null && !agentId.equals("")) {
			// try to find the agent using the agent id or participant code
			agentInfo = op.getAgentInfo(agentId);
		}
		
		return agentInfo;
	}

	/**
	 * Returns the stack trace data from the exception as a string.
	 * @param e - the exception
	 */
	public String getExceptionStackTrace(Exception e) {
		String stackTrace = null;
		if (e != null) {
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
		}
		
		return stackTrace;
	}

	/**
	 * Logs the import error in the Transaction Log database and, if appropriate, sends a 
	 * tech support email notification.
	 * 
	 * @param agentId - the agent ID for the import file, if known
	 * @param agentName - the agent name for the import file, if known
	 * @param participantCode - the participant code for the import file, if known/needed
	 * @param errorMsg - the error message
	 * @param filename - the name to be used for reporting purposes for the import file
	 * @param origFilename - the original filename of the import file
	 * @param alwaysLog - 'true' to always log the import failure, 'false' to only log for agents in the database
	 * @param status - the import status
	 * @param exception - the exception that occurred during import, or 'null' if no exception
	 * @throws Exception
	 */
	public void reportImportError(String agentId, String agentName, String participantCode, 
								  String errorMsg, String filename, String origFilename, 
								  boolean alwaysLog, FileEventStatus status, Exception exception, int transCount, DownloadStatus dstatus)
						throws Exception {
		// look up the agent info
		/*AgentInfo agent = null;
		try {
			agent = getAgentInfo(filename, agentId);
		} catch (SQLException sqlEx) {
			agent = null;
		}*/
		
		reportImportError(agentId, participantCode, errorMsg, filename, origFilename, 
						  alwaysLog, status, exception,transCount,dstatus);
	}

	/**
	 * Logs the import error in the Transaction Log database and, if appropriate, sends a 
	 * tech support email notification.
	 * 
	 * @param agentInfo - the agent info bean associated with the import file, if known
	 * @param participantCode - the participant code for the import file, if known/needed
	 * @param errorMsg - the error message
	 * @param filename - the name to be used for reporting purposes for the import file
	 * @param origFilename - the original filename of the import file
	 * @param alwaysLog - 'true' to always log the import failure, 'false' to only log for agents in the database
	 * @param status - the import status
	 * @param exception - the exception that occurred during import, or 'null' if no exception
	 * @throws Exception
	 */
	public void reportImportError(String agentId , String participantCode, String errorMsg, 
								  String filename, String origFilename, boolean alwaysLog, 
								  FileEventStatus status, Exception exception, int transCount,DownloadStatus dstatus) 
						throws Exception {
		
		/*String agentId = "UNKNOWN";
		String agentName = "";
		if (agentInfo != null) {
			agentId = agentInfo.getAgentId();
			agentName = agentInfo.getName();
		}*/
		
		// Create a temporary file record
		FileInfo fileinfo = createFileInfo(filename, origFilename, agentId);
		fileinfo.setDownloadStatus(dstatus);
		fileinfo.setParticipantCode(participantCode);
		fileinfo.setTransactionCount(transCount);
		// Log the file as a failed import.  Only log if import is
		// being done in interactive mode, OR if the file is matched
		// to an agent in the database (disabled)
		if (alwaysLog || agentInfo != null) {
			op.logImport(fileinfo, status, batchNumber, agentId, getExceptionStackTrace(exception));
		}
		
		if (exception != null) {
			CarrierInfo carrier = serverInfo.getCarrierInfo();
			if (carrier.isNotifyOnImportError()) {
				// Send a tech support email
				String body = errorMsg + "\n\n  Application:  ";
				if (serverInfo == null)
					body += "Import service";
				else
					body += serverInfo.getAppName();
				body += "\n  Filename:  " + filename +
						"\n  Agent ID:  " + agentId;
				if (exception != null)
					body += "\n\n" + exception.getMessage();
				
				EmailService.getInstance().sendEMail(carrier.getErrorsEmail(), errorMsg, body);
			}
		}
	}

}
