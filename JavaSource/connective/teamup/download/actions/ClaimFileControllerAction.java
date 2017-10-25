package connective.teamup.download.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ebix.licence.Licence;
import com.ebix.utility.TeamUPConstant;
import com.google.gson.Gson;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.FileHDR;
import connective.teamup.download.services.FileService;
import connective.teamup.download.services.LicenceService;

public class ClaimFileControllerAction implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(ClaimFileControllerAction.class);
	
	private static final long serialVersionUID = 3024703756055172264L;
	private HashMap<String, Object> JSONROOT = new HashMap<String, Object>();
	
	
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException		{
		String method = req.getParameter("method");
		String jsonArray ="";
		Gson gson = new Gson();
		String id = (String) req.getSession().getAttribute("agentId");
		InputStream in = null;
		ServletOutputStream out = null;
		boolean isClaimAccess = false;
		
		try{
			isClaimAccess = LicenceService.isClaimActivated(op);
		}catch(Exception e){
			//issue with License
			LOGGER.error(e);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		
		
		try{
			
			if(isClaimAccess) {
				if (TeamUPConstant.CLIST.equals(method) 
						|| TeamUPConstant.CLISTALL.equals(method)) {
					int startPageIndex = 1;
					int recordsPerPage = 0;
					startPageIndex = Integer.parseInt(req.getParameter("jtStartIndex"));
					recordsPerPage = Integer.parseInt(req.getParameter("jtPageSize"));
					int start = startPageIndex + 1;
					int end = recordsPerPage + startPageIndex;
					String sortBy = req.getParameter("jtSorting");
					String srchClaimNumber = "";
					String srchClmPolicyNumber = "";
					String srchclaimStatus = "";
					String srchclaimBPTCode = "";
					String frmClaimFileDate = "";
					String toClaimFileDate = "";
					String toClaimDate = "";
					String searchToDate = "";
					Long fromMillSeconds = null;
					Long toMillSeconds = null;
					
					if(sortBy== null || "".equals(sortBy)) {
						sortBy = "created_dt DESC";
						
					} else{
						sortBy = FileHDR.getColumName(sortBy);
					}
					
					if(TeamUPConstant.CLISTALL.equals(method))
						id = req.getParameter("srcClaimAgentId");
					
					srchClaimNumber = req.getParameter("srchClaimNumber");
					srchClmPolicyNumber = req.getParameter("srchClmPolicyNumber");
					srchclaimStatus = req.getParameter("srchclaimStatus");
					srchclaimBPTCode = req.getParameter("srchclaimBPTCode");
					
					frmClaimFileDate = req.getParameter("frmClaimFileDate");
					if(frmClaimFileDate != null  && frmClaimFileDate.length()>=10) {
						Date datefrom  = sdf.parse(frmClaimFileDate +" 00:00:00");
						fromMillSeconds = datefrom.getTime();	
					}
					toClaimFileDate = req.getParameter("toClaimFileDate");

					if(toClaimFileDate != null && toClaimFileDate.length()>=10) {
						if(toClaimFileDate.equals(toClaimFileDate)){
							toClaimFileDate = toClaimFileDate +" 23:59:00";
						}else{
							toClaimFileDate = toClaimFileDate +" 00:00:00";
						}
					Date dateTo = sdf.parse(toClaimFileDate);
					toMillSeconds = dateTo.getTime();
					}
					List<FileHDR> files = null;
					int count = op.getClaimFilesCount(id,srchClaimNumber,srchClmPolicyNumber,srchclaimStatus,srchclaimBPTCode,fromMillSeconds,toMillSeconds);
					if(count > 0)
						files = op.getClaimFiles(id, start, end,sortBy,srchClaimNumber,srchClmPolicyNumber,srchclaimStatus,srchclaimBPTCode,fromMillSeconds,toMillSeconds);

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", files);
					JSONROOT.put("TotalRecordCount", count);
					// Convert Java Object to Json
					 jsonArray = gson.toJson(JSONROOT);
					 return jsonArray;
				}
				
				if (TeamUPConstant.CTRANSLIST.equals(method)) {

					String fileName = req.getParameter("fileName");
					long createdDate = Long.valueOf(req.getParameter("creationDate"));
					String agentId = req.getParameter("agentId");

					jsonArray = FileService.getClaimTransactionList(agentId, fileName, createdDate, op);
					return jsonArray;
				}
				
				
				if (TeamUPConstant.DELETECLAIM.equals(method)) {
					String ids = req.getParameter("ids");
					jsonArray = FileService.deleteClaim(ids, op);
					return jsonArray;
				}
				
				if (TeamUPConstant.UPDATE.equals(method)) {
					String ids = req.getParameter("ids");
					String transIds = req.getParameter("transactionIds");
					String valueAction = req.getParameter("actionValue");
					
					 jsonArray = FileService.updatePolicy(ids, transIds,valueAction, op);
					 return jsonArray;
					 
				}
				
				if (TeamUPConstant.CFILEDOWNLOAD.equals(method)) {
					String fileName = req.getParameter("fileName");
					String trasnIds = null;
					String [] transSeq = null;
					id = req.getParameter("agentId");
					if(fileName!= null && id != null && req.getParameter("creationDate") != null ) {
						String fileDisplay = fileName +".xml";
						long createdDate = Long.parseLong(req.getParameter("creationDate"));
						resp.setContentType("application/download");
						resp.setHeader("Content-Disposition","attachment;filename="+fileDisplay);
						 out = resp.getOutputStream();
						if(req.getParameter("trasnIds") != null &&!req.getParameter("trasnIds").equals("") ) {
							trasnIds = req.getParameter("trasnIds");
							if(trasnIds.contains(",")) {
								transSeq = trasnIds.split(",");
							}else {
								transSeq = new String[1];
								transSeq[0] = trasnIds;
							}
						}
						DownloadManager dm = new DownloadManager();
						byte[] fileBytes =dm.claimfileDownload(op,id, fileName, createdDate,transSeq);
						in =  new ByteArrayInputStream(fileBytes);
						byte[] buf = new byte[32000];
						int read; 
						while ((read = in.read(buf, 0, 32000)) != -1)
						{
							out.write(buf, 0, read);
						}
					
					}
				}	
			}else{
				//access to claim is denied
			}
					
		}catch (Exception ex) {
			LOGGER.error(ex);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
}
