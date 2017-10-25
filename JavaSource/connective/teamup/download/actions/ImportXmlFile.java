/*
 * Created on Aug 17, 2010
 */
package connective.teamup.download.actions;

import java.io.IOException;
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

/**
 * Import a block of ACORD XML data or a single ACORD XML file.
 * 
 * @author kmccreary
 */
public class ImportXmlFile extends ImportAcordXmlBase implements Action {

	private static final Logger LOGGER = Logger.getLogger(ImportXmlFile.class);
	/**
	 * Default constructor for ImportXmlFile.
	 */
	public ImportXmlFile() {
		super();
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp,
			ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException {
		
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
		String xmlHeader = null;
		
		try
		{
			try
			{
				filename = req.getParameter("f_name");
				filesize = req.getParameter("f_size");
				filedata = req.getParameter("f_contents");
				String compressed =  req.getParameter("f_compressed");
				xmlHeader  =  req.getParameter("f_header");
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
				fileinfo.setDownloadStatus(DownloadStatus.CLAIM_CURRENT);
				fileinfo.setLastDownloadDate(0);
				fileinfo.setMessageSequence(0);
				fileinfo.setBatchNumber(batchNum);
				fileinfo.setHeader("");
				fileinfo.setFooter("");
				fileinfo.setTestFile(false);
				fileinfo.setFileComplete(false);
				fileinfo.setTransactionCount(0);
				// Create the error message and log the file as a failed import
				FileImportStatus status = new FileImportStatus(FileImportStatus.FAILED_OTHER);
				status.setText("Claim File Import error: " + e.getMessage());
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
						/*System.out.println("Data received (" + datasize + 
							" bytes) is not the same length as the original file size (" +
							fsize + " bytes)");*/
						
						LOGGER.error("Claim XML file Data received (" + datasize + 
								" bytes) is not the same length as the original file size (" +
								fsize + " bytes)");
					}
				}
			}
			catch (Exception e) {
				LOGGER.error("Claim XML:" +e);
			}
			
			String deleteFlag = "Y";
			String statusCode = "I";
			String msgText = null;
			
			// perform the xml import
			FileImportStatus status = importFile(op, serverInfo, filename, createdDate, false, true, batchNum, filedata, chunkNum, xmlHeader);
			
			if (!status.isStatusDeleteFile())
				deleteFlag = "N";
			if (status.isFailed())
			{
				statusCode = "F";
				msgText = status.getText();
			}
			else if (status.isRejected())
			{
				statusCode = "R";
				if (msgText == null || msgText.equals(""))
					msgText = status.getText();
				else
					msgText += "; " + status.getText();
			}else if(status.isImported()){
				statusCode ="I";
				msgText = status.getText();
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
			
			throw new ActionException("Error uploading Claim XML file", e);
		}
		
		return null;
	}

}
