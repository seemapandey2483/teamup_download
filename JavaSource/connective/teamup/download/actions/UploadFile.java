package connective.teamup.download.actions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileImportStatus;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.db.LZString;
import connective.teamup.upload.AcordCompressionFilter;
import connective.teamup.upload.TeamupFileReceiver;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class UploadFile extends ImportActionBase implements Action 
{
	private static final Logger LOGGER = Logger.getLogger(UploadFile.class);
	/**
	 * Constructor for UploadFile.
	 */
	public UploadFile() 
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		String filename = "";
		String filesize = "";
		String filedata = "";
		String agentName = "";
		long createdDate = 0;
		boolean interactive = false;
		boolean fileComplete = false;
		boolean reportDetail = false;
		int chunkNum = 0;
		int batchNum = 0;
		int datasize = 0;
		int transCount = 0;
		
		
		try
		{
			try
			{
				filename = req.getParameter("f_name");
				filesize = req.getParameter("f_size");
				filedata = req.getParameter("f_contents");
				String compressed =  req.getParameter("f_compressed");
				
				//	if file is compressed decompressed it
				if("Y".equals(compressed)){
					filedata = LZString.decompressFromBase64(filedata);					
				}


				// parse the creation date
				String createdDateStr = req.getParameter("f_createdt");
				GregorianCalendar cal = new GregorianCalendar(
					Integer.parseInt(createdDateStr.substring(0, 4)),
					(Integer.parseInt(createdDateStr.substring(5, 7)) - 1),
					Integer.parseInt(createdDateStr.substring(8, 10)),
					Integer.parseInt(createdDateStr.substring(11, 13)),
					Integer.parseInt(createdDateStr.substring(14, 16)),
					Integer.parseInt(createdDateStr.substring(17, 19)));
				createdDate = cal.getTime().getTime();
				
				String interactiveFlag = req.getParameter("interactive");
				interactive = (interactiveFlag != null && interactiveFlag.equals("Y"));
				String completeFlag = req.getParameter("f_complete");
				fileComplete = (completeFlag != null && completeFlag.equals("Y"));
				String detailFlag = req.getParameter("f_result_detail");
				reportDetail = (detailFlag != null && detailFlag.equals("Y"));
				
				// Strip any apostrophes or quote marks from the file name -- 09/09/2003, kwm
				if (filename != null)
				{
					filename = filename.trim();
					StringBuffer fn = new StringBuffer("");
					for (int n=0; n < filename.length(); n++)
					{
						char c = filename.charAt(n);
						if (c == ' ')
							fn.append("_");
						else if (c != '\'' && c != '"')
							fn.append(c);
					}
					filename = fn.toString();
				}
				
				// Parse the import batch number from the request
				try
				{
					String bn = req.getParameter("batch_num");
					batchNum = Integer.parseInt(bn);
				}
				catch (Exception e) {
					LOGGER.error(e);
				}
				
				// Parse the block sequence from the request
				try
				{
					String cn = req.getParameter("chunk_num");
					chunkNum = Integer.parseInt(cn);
				}
				catch (Exception e) {
					LOGGER.error(e);
				}
				
				// Parse the total number of transactions from the request
				try
				{
					String tc = req.getParameter("trans_count");
					transCount = Integer.parseInt(tc);
				}
				catch (Exception e) {
					LOGGER.error(e);
				}
				
				// Check for upload source -- if empty, assume ActiveX control (or old import app)
				TeamupFileReceiver teamup = new TeamupFileReceiver(null);
				String importSource = req.getParameter("src");
				if (importSource == null || !importSource.equalsIgnoreCase("cl_import"))
				{
					// Strip any escape sequencing enforced by the upload control
					filedata = teamup.stripEscapeSequencing(filedata);
				}
				
				// Check for ACORD data compression
				datasize = filedata.length();	// Get original data size (before de-compressing/unsqueezing)
				AcordCompressionFilter acFilter = new AcordCompressionFilter();
				if (acFilter.isCompressed(filedata))
					filedata = acFilter.decompress(filedata);
				
				// Remove any instances of the null character from ACORD AL* files -- 12/17/2003, kwm
				if (filedata.substring(0, 4).equals("1MHG"))
					filedata = teamup.removeBinaries(filedata);
			}
			catch (Exception e)
			{
				LOGGER.error(e);
				// Set default values for reporting the import error
				String agentid = "UNKNOWN";
				if (filename == null || filename.equals(""))
					filename = "UNKNOWN";
				
				// Create a temporary file record
				FileInfo fileinfo = op.createFile();
				fileinfo.setAgentId(agentid);
				fileinfo.setFilename(filename);
				fileinfo.setOriginalFilename(filename);
				fileinfo.setCreatedDate(createdDate);
				fileinfo.setImportedDate(System.currentTimeMillis());
				fileinfo.setDownloadStatus(DownloadStatus.CURRENT);
				fileinfo.setLastDownloadDate(0);
				fileinfo.setMessageSequence(0);
				fileinfo.setBatchNumber(batchNum);
				fileinfo.setHeader("");
				fileinfo.setFooter("");
				fileinfo.setTestFile(false);
				fileinfo.setFileComplete(false);
				
				// Create the error message and log the file as a failed import
				FileImportStatus status = new FileImportStatus(FileImportStatus.FAILED_OTHER);
				status.setText("Import error: " + e.getMessage());
				ActionException ax = new ActionException(null, e);
				op.logImport(fileinfo, status, batchNum, agentName, ax.getStackTraceAsString());
				
				// Send response to the control to NOT delete the file
				if (reportDetail)
					resp.getOutputStream().print("N,F," + status.getText());
				else
					resp.getOutputStream().print("N");
				
				throw e;
			}

			
			// Check file size to verify the entire file was received
			try
			{
				if (fileComplete && chunkNum == 0)
				{
					int fsize = Integer.parseInt(filesize);
					if (datasize != fsize && datasize != (fsize-2))
					{
						System.out.println("Data received (" + datasize + 
							" bytes) is not the same length as the original file size (" +
							fsize + " bytes)");
					}
				}
			}
			catch (Exception e) {
				LOGGER.error(e);
			}
			
			// Check for multiple 1MHG blocks within the file; if found, split and treat as
			// separate files for import and storage -- 06/30/2005, kwm
			ArrayList fileStrings = new ArrayList();
			if (chunkNum > 0 || !fileComplete)
			{
				// Do not split multiple 1MHG blocks if import is being chunked -- 07/01/2005, kwm
				fileStrings.add(filedata);
				filedata = null;
			}
			else if (filedata != null && filedata.lastIndexOf("1MHG") <= 0)
			{
				// There is only one 1MHG message group in the file, go with it! -- 11/12/2007, kwm
				fileStrings.add(filedata);
			}
			else
			{
				// Loop through the ACORD records, break into multiple message groups at 
				// every new 1MHG record header -- 11/12/2007, kwm
				StringBuffer buf = new StringBuffer(filedata);
				String al3group = null;
				String al3len = null;
				int len = buf.length();
				int n = 0;
				while (buf != null && n+7 < len)
				{
					// parse the AL3 group name
					al3group = buf.substring(n, n+4);
					if (al3group.equals("1MHG") && n > 0)
					{
						fileStrings.add(buf.substring(0, n));
						buf.delete(0, n);
						if (buf.lastIndexOf("1MHG") <= 0)
							break;
						n = 0;
						len = buf.length();
					}
					
					// parse the record length, increase the counter accordingly
					al3len = buf.substring(n+4, n+7);
					try {
						int nLen = Integer.parseInt(al3len);
						n += nLen;
						while (n < len && buf.charAt(n) < '0')
							n++;
					}
					catch (Exception e)
					{
						LOGGER.error(e);
						int nMhg = buf.indexOf("1MHG", n+4);
						if (nMhg > 0)
						{
							fileStrings.add(buf.substring(0, nMhg));
							buf.delete(0, nMhg);
						}
						else
						{
							fileStrings.add(buf.toString());
							buf = null;
						}
					}
				}
				if (buf != null && buf.length() > 7)
					fileStrings.add(buf.toString());
			}
			String deleteFlag = "Y";
			String statusCode = "I";
			String msgText = null;
			for (int i=0; i < fileStrings.size(); i++)
			{
				// Create the input stream
				String blockdata = (String) fileStrings.get(i);
				InputStream instr = new ByteArrayInputStream(blockdata.getBytes());				
				
				// If multiple 1MHG blocks, update filename to force unique keys
				String tmpFilename = filename;
				if (fileStrings.size() > 1)
					tmpFilename += " (" + String.valueOf(i+1).trim() + ")";
				
				// The import function returns TRUE if file should be deleted, otherwise FALSE
				FileImportStatus status = importFile(op, serverInfo, tmpFilename, null, createdDate, false, interactive, batchNum, instr, transCount, chunkNum, fileComplete);
				instr.close();
				
				if (!status.isStatusDeleteFile())
					deleteFlag = "N";
				if (status.isFailed())
				{
					statusCode = "F";
					msgText = status.getText();
					break;
				}else if (status.getStatus()==FileImportStatus.PARTIAL_FILE_IMPORT)
				{
					statusCode = "P";
					if (msgText == null || msgText.equals(""))
						msgText = status.getText();
					else
						msgText += "; " + status.getText();
				}
				else if (status.isRejected())
				{
					statusCode = "R";
					if (msgText == null || msgText.equals(""))
						msgText = status.getText();
					else
						msgText += "; " + status.getText();
				} else if(status.isSuccessful()) {
					msgText = status.getText();
				}
			}
			
			if (msgText == null || msgText.equals(""))
				msgText = "Import successful";
			
			if (reportDetail)
				resp.getOutputStream().print(deleteFlag + "," + statusCode + "," + msgText);
			else
				resp.getOutputStream().print(deleteFlag);
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			try
			{
				if (reportDetail)
					resp.getOutputStream().print("N,F," + e.getMessage());
				else
					resp.getOutputStream().print("N");
			}
			catch (IOException iox) {
				LOGGER.error(iox);
			}
			
			throw new ActionException("Error uploading file", e);
		}
		
		return null;
	}
}
