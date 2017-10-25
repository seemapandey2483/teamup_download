package connective.teamup.download.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.services.DownloadService;
import connective.teamup.download.ws.objects.DownloadFileInfo;

@SuppressWarnings("restriction")
public class DownloadManager {
	
	private static final Logger LOGGER = Logger.getLogger(DownloadManager.class);
	
	BASE64Decoder decoder = null;
	DownloadFileInfo[] dlFiles = null;
	
	public DownloadManager() {
		super();
		decoder = new BASE64Decoder();
	}
	
	
	
	@SuppressWarnings("unused")
	public byte[] claimfileDownload(DatabaseOperation op,String id, String fileName, long createdDate, String[] transSeq) throws Exception {
		byte[] fileBytes = null;
		try{

			FileInfo files = op.getAgentClaimFileForDownload(id, fileName, createdDate);
			fileBytes = files.getClaimFileContentsForServerDownloads(transSeq);
		}catch(Exception ex){
			LOGGER.error(ex);
			throw ex;
		}finally{
		}
		return fileBytes;

	}
	
	@SuppressWarnings("unused")
	public byte[] fileDownload(DatabaseOperation op,String id, String fileName, long createdDate, String[] transSeq) throws Exception {
		byte[] fileBytes = null;
		try{

			FileInfo files = op.getAgentFileForDownload(id, fileName, createdDate);
			fileBytes = files.getFileContentsForServerDownloads(transSeq);
		}catch(Exception ex){
			LOGGER.error(ex);
			throw ex;
		}finally{
		}
		return fileBytes;

	}
	
	@SuppressWarnings("unused")
	public void performDownload(String id, String fileName, long createdDate) {
		try{
			dlFiles = downloadFiles(id, fileName, createdDate);
			DownloadFileInfo[] savedFiles = saveDownloadFiles(id, dlFiles);
		}catch(Exception ex){
			LOGGER.error(ex);
			ex.printStackTrace();
		}
	}
	public DownloadFileInfo[] downloadFiles(String id, String fileName, long createdDate) throws Exception {
		DownloadFileInfo[] files = null;
		DatabaseOperation op = null;
		try{
			DownloadService service = new DownloadService();
			op = DatabaseFactory.getInstance().startOperation();
			/*AgentInfo agent = op.getAgentInfo(id);
			if (agent == null) {
				throw new Exception("Invalid agent ID.");
			} else if (!agent.isActive()) {
				throw new Exception("This agent ID is not currently active.  Please contact your carrier representative to continue registration for TEAM-UP Download.");
			}
			
			// update the last login date for the agent
			agent.setLastLoginDate(System.currentTimeMillis());
			agent.save();*/
			files = service.getDownloadFiles(op, id, fileName, createdDate, true);
			if (files != null) {
				for (int i=0; i < files.length; i++)
					files[i].setFileContentsEncoded("#OFFLINE#");
			}
		} finally {
			if (op != null)
				op.close();
		}
		return files;
	}
	
	public DownloadFileInfo[] saveDownloadFiles(String agentId, DownloadFileInfo[] dlFiles) {
		ArrayList<DownloadFileInfo> savedFiles = new ArrayList<DownloadFileInfo>();
		for (int i = 0; i < dlFiles.length; i++) {
			DownloadFileInfo fileInfo = dlFiles[i];
			String error = saveFile(agentId, dlFiles[i], false);
			String msg = "  File #" + String.valueOf(i+1) + " ";
			if (error == null) {
				msg += "successful";
				System.out.println(msg);
			} else {
				fileInfo.setError(true);
				fileInfo.setErrorMsg(error);
				msg += "failed:  " + error;
				System.out.println(msg);
			}
			// Remove the file contents from the info object
			fileInfo.setFileContentsEncoded(null);
			savedFiles.add(fileInfo);
			if (fileInfo.isError())
				break;
		}
		DownloadFileInfo[] ret = new DownloadFileInfo[savedFiles.size()];
		savedFiles.toArray(ret);
		return ret;
	}
	
	protected String saveFile(String agentId, DownloadFileInfo file, boolean overwriteExisting) {
		return saveFile(agentId, file, null, overwriteExisting);
	}

	protected String saveFile(String agentId, DownloadFileInfo fileInfo, String path, boolean overwriteExisting) {
		String errorMsg = null;
		try{
			// Get the file contents.  If this field reads #OFFLINE#, then the file contents must
			// be downloaded from the server separately.  This is to accomodate large download
			// files like initial loads, and is the default implementation for versions later than v3r4p2.
			// But, the file contents may still be stored in this field in older versions, so we
			// have to handle both situations for backward compatibility.
			InputStream fileContents = null;
			String encContents = fileInfo.getFileContentsEncoded();
			if (encContents != null && encContents.equals("#OFFLINE#")) {
				// download the file contents
				byte[] byteArray = decoder.decodeBuffer(getDownloadFileContents(agentId, fileInfo));
				if (byteArray.length > 9) {
					String check4xml = new String(byteArray, 0, 300);
					int n = check4xml.indexOf(',');
					if (check4xml.startsWith("ACORDXML:") && n > 0) {
						if (n > 9)
							path = check4xml.substring(9, n);
						fileContents = new ByteArrayInputStream(byteArray, n+1, byteArray.length - n - 1);
					} else {
						fileContents = new ByteArrayInputStream(byteArray);
					}
				} else {
					fileContents = new ByteArrayInputStream(byteArray);
				}
			} else {
				fileContents = new ByteArrayInputStream(decoder.decodeBuffer(encContents));
			}
			
			int contentSize = 0;
			AgentModel agent = AgentModel.getInstance();
			if (agent.getFilenameIncrMethod() == null)
				agent.setFilenameIncrMethod("X");
			path = "C:\\TEAMUP"	;
			// Get the filename to be used
			String filename = null;
			if (overwriteExisting)
				filename = getFilename(path, fileInfo);
			else
				filename = getFilenameIncrReplace('#', path, fileInfo);
			
			// Write the file
			FileOutputStream os = null;
			if (agent.isAppendFile() && !overwriteExisting) {
				os = new FileOutputStream(filename, true);
			} else {
				File file = new File(filename);
				os = new FileOutputStream(file);
			}
			
			byte[] buf = new byte[32000];
			int read; 
			while ((read = fileContents.read(buf, 0, 32000)) != -1) {
				os.write(buf, 0, read);
				contentSize += read;
			}
			os.flush();
			os.close();
			
			// Save the actual filename (as saved to agent's hard drive) back to the file info object
			fileInfo.setFilename(filename);
			// Verify that the entire file was received
			if (fileInfo.getFileSize() != contentSize) {
				errorMsg = "file size invalid, file contents cannot be verified";
			}
		} catch (Exception e) {
			LOGGER.error(e);
			errorMsg = e.getMessage();
			if (errorMsg == null)
				errorMsg = "error occurred during file save";
			fileInfo.setError(true);
			fileInfo.setErrorMsg(errorMsg);
		}
		return errorMsg;
	}
	
	public String getDownloadFileContents(String agentId, DownloadFileInfo fileInfo) {
		DatabaseOperation op = null;
		DownloadFileInfo file = null;
		DownloadService service = new DownloadService();
		try {
			op = DatabaseFactory.getInstance().startOperation();
			file = service.getDownloadFile(op, agentId, fileInfo.getOriginalFilename(), fileInfo.getCreatedDate(), false);
		} catch (Exception e) {
			LOGGER.error(e);
			e.printStackTrace();
		} finally {
			if (op != null)
				op.close();
		}
		return file.getFileContentsEncoded();
	}
	
	/**
	 * Returns the default filename (with full path).
	 */
	protected String getFilename(String path, DownloadFileInfo fileInfo) {
		return getFilenameIncrExtension(path, fileInfo, true);
	}

	/**
	 * Returns the filename to be used.  If default filename exists, increments
	 * the filename extension until a unique filename is found.
	 */
	protected String getFilenameIncrExtension(String path, DownloadFileInfo fileInfo, boolean overwriteExisting) {
		String filename = path;
		if (filename == null || filename.equals("")) {
			filename = "";
		} else if (filename.charAt(filename.length() - 1) != '\\') {
			filename += "\\";
		} else {
			filename += fileInfo.getFilename();
		}
		if (overwriteExisting)
			return filename;
		String baseFilename = filename;
		int extCount = 0;
		try {
			int n = filename.lastIndexOf('.');
			if (n < 0) {
				baseFilename += ".";
			} else {
				baseFilename = filename.substring(0, n+1);
				extCount = Integer.parseInt(filename.substring(n+1));
			}
		} catch (Exception e) {
			LOGGER.error(e);
			e.printStackTrace();
		}
		
		// See if file already exists; if so, increment file extension
		int extLen = 3;
		String maxExt = "999";
		File file = new File(filename);
		while (file.exists() && extLen < 6) {
			extCount++;
			String ext = String.valueOf(extCount);
			while (ext.length() < extLen)
				ext = "0" + ext;
			
			// Check for max number of files for specified extension length
			if (ext.equals(maxExt)) {
				extLen++;
				maxExt += "9";
			}
			filename = baseFilename + ext;
			file = new File(filename);
		}
		return filename;
	}

	/**
	 * Returns the filename to be used.  The '#' signs within the filename
	 * designate the location and padded length of the file counter, which
	 * is incremented (starting with 1) until a unique filename is found.
	 */
	protected String getFilenameIncrReplace(char replace, String path, DownloadFileInfo fileInfo) {
		String fileStart = path;
		if (fileStart == null || fileStart.equals("")) {
			fileStart = "";
		} else if (fileStart.charAt(fileStart.length() - 1) != '\\') {
			fileStart += "\\";
		}
		
		int incrLen = 0;
		String fileEnd = "";
		String temp = fileInfo.getFilename();
		for (int i=0; i < temp.length(); i++) {
			char c = temp.charAt(i);
			if (c == replace && fileEnd.equals("")) {
				incrLen++;
			} else if (incrLen == 0) {
				fileStart += c;
			} else {
				fileEnd += c;
			}
		}
		
		String filename = null;
		File file = null;
		int counter = 0;
		do {
			counter ++;
			String incr = String.valueOf(counter);
			while (incr.length() < incrLen)
				incr = "0" + incr;
			filename = fileStart + incr + fileEnd;
			file = new File(filename);
		} while (file.exists());
		
		return filename;
		
	}
	

}
