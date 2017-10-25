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
import org.apache.poi.util.StringUtil;

import com.ebix.utility.TeamUPConstant;
import com.google.gson.Gson;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileHDR;
import connective.teamup.download.services.FileService;

public class PolicyFileControllerAction implements Action{
	
	private static final Logger LOGGER = Logger.getLogger(PolicyFileControllerAction.class);
	
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
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		
		
		try{
			
			if (TeamUPConstant.PLIST.equals(method) 
					|| TeamUPConstant.PLISTALL.equals(method)) {
				int startPageIndex = 1;
				int recordsPerPage = 0;
				startPageIndex = Integer.parseInt(req.getParameter("jtStartIndex"));
				recordsPerPage = Integer.parseInt(req.getParameter("jtPageSize"));
				int start = startPageIndex + 1;
				int end = recordsPerPage + startPageIndex;
				String sortBy = req.getParameter("jtSorting");
				String searchPolicyNumber = "";
				String searchInsuredName = "";
				String searchTxnType = "";
				String searchFromDate = "";
				String searchToDate = "";
				Long fromMillSeconds = null;
				Long toMillSeconds = null;
				if(sortBy== null || "".equals(sortBy)) {
					sortBy = "created_dt DSC";
					
				} else{
					sortBy = FileHDR.getColumName(sortBy);
				}
				if(TeamUPConstant.PLISTALL.equals(method))
					id = req.getParameter("srcPolicyAgentId");
				
				searchPolicyNumber = req.getParameter("srchPolicyNumber");
				searchInsuredName = req.getParameter("srchInsuredName");
				searchTxnType = req.getParameter("srchTxnType");
				searchFromDate = req.getParameter("frmPolicyDate");
				if(searchFromDate != null  && searchFromDate.length()>=10) {
					Date datefrom  = sdf.parse(searchFromDate +" 00:00:00");
					fromMillSeconds = datefrom.getTime();	
				}
				searchToDate = req.getParameter("toPolicyDate");

				if(searchToDate != null && searchToDate.length()>=10) {
					if(searchToDate.equals(searchFromDate)){
						searchToDate = searchToDate +" 23:59:00";
					}else{
						searchToDate = searchToDate +" 00:00:00";
					}
					Date dateTo = sdf.parse(searchToDate);
					toMillSeconds = dateTo.getTime();
				}
				List<FileHDR> files = null;
				int count = op.getPolicyFilesCount(id,searchPolicyNumber,searchInsuredName,searchTxnType,fromMillSeconds,toMillSeconds);
				if(count	>	0)
					files = op.getPolicyFiles(id, start, end,sortBy,searchPolicyNumber,searchInsuredName,searchTxnType,fromMillSeconds,toMillSeconds);
				// Return in the format required by jTable plugin
				JSONROOT.put("Result", "OK");
				JSONROOT.put("Records", files);
				JSONROOT.put("TotalRecordCount", count);
				// Convert Java Object to Json
				 jsonArray = gson.toJson(JSONROOT);
			}
			if (TeamUPConstant.DLIST.equals(method) 
					|| TeamUPConstant.DLISTALL.equals(method)) {
				int startPageIndex = 1;
				int recordsPerPage = 0;
				startPageIndex = Integer.parseInt(req.getParameter("jtStartIndex"));
				recordsPerPage = Integer.parseInt(req.getParameter("jtPageSize"));
				int start = startPageIndex + 1;
				int end = recordsPerPage + startPageIndex;
				String sortBy = req.getParameter("jtSorting");
				String searchPolicyNumber = "";
				String searchInsuredName = "";
				String searchTxnType = "";
				String searchFromDate = "";
				String searchToDate = "";
				Long fromMillSeconds = null;
				Long toMillSeconds = null;
				if(sortBy== null || "".equals(sortBy)) {
					sortBy = "created_dt DSC";
					
				} else{
					sortBy = FileHDR.getColumName(sortBy);
				}

				if(TeamUPConstant.DLISTALL.equals(method))
					id = req.getParameter("srcDBRAgentId");
				
			
				
				searchPolicyNumber = req.getParameter("srcDBRPolicyNumber");
				searchInsuredName = req.getParameter("srcDBRInsuredName");
				searchTxnType = req.getParameter("srcDBRCode");
				boolean searchflag = false;
				if("Y".equals(req.getParameter("searchFlag")))
					searchflag = true;
				
				searchFromDate = req.getParameter("frmPolicyDate");
				
				if(searchFromDate != null  && searchFromDate.length()>=10) {
					Date datefrom  = sdf.parse(searchFromDate +" 00:00:00");
					fromMillSeconds = datefrom.getTime();	
				}
				searchToDate = req.getParameter("toPolicyDate");

				if(searchToDate != null && searchToDate.length()>=10) {
					if(searchToDate.equals(searchFromDate)){
						searchToDate = searchToDate +" 23:59:00";
					}else{
						searchToDate = searchToDate +" 00:00:00";
					}
					Date dateTo = sdf.parse(searchToDate);
					toMillSeconds = dateTo.getTime();
				}
				List<FileHDR> files = null;
				int count = op.getDirectBillFilesCount(id,searchPolicyNumber,searchInsuredName,searchTxnType,searchflag,fromMillSeconds,toMillSeconds);
				if(count >0)
					files = op.getDirectBillFiles(id, start, end,sortBy,searchPolicyNumber,searchInsuredName,searchTxnType,searchflag,fromMillSeconds,toMillSeconds);

				// Return in the format required by jTable plugin
				JSONROOT.put("Result", "OK");
				JSONROOT.put("Records", files);
				JSONROOT.put("TotalRecordCount", count);
				// Convert Java Object to Json
				 jsonArray = gson.toJson(JSONROOT);
			}
		
			if (TeamUPConstant.PTRANSLIST.equals(method)) {

				String fileName = req.getParameter("fileName");
				long createdDate = Long.valueOf(req.getParameter("creationDate"));
				String agentId = req.getParameter("agentId");

				jsonArray = FileService.getPolicyTransactionList(agentId, fileName, createdDate, op);
				return jsonArray;
			}
			if (TeamUPConstant.DBTRANSLIST.equals(method)) {
					
				String fileName = req.getParameter("fileName");
				String agentId = req.getParameter("agentId");
				long createdDate = Long.parseLong(req.getParameter("creationDate"));
				int startPageIndex = 1;
				int recordsPerPage = 0;
				startPageIndex = Integer.parseInt(req.getParameter("jtStartIndex"));
				recordsPerPage = Integer.parseInt(req.getParameter("jtPageSize"));
				jsonArray = FileService.getDirectBillTransactionList(agentId, fileName, createdDate, startPageIndex, recordsPerPage, op);
				return jsonArray;
			}
			if (TeamUPConstant.DELETEPOLICY.equals(method)) {
				String ids = req.getParameter("ids");
				jsonArray = FileService.deletePolicy(ids, op);
				return jsonArray;
			}
		
			if (TeamUPConstant.DELETEDBILL.equals(method)) {
				String ids = req.getParameter("ids");
				jsonArray = FileService.deleteDirectBill(ids, op);
				return jsonArray;
			}
			if (TeamUPConstant.UPDATE.equals(method)) {
				String ids = req.getParameter("ids");
				String transIds = req.getParameter("transactionIds");
				String valueAction = req.getParameter("actionValue");
				
				 jsonArray = FileService.updatePolicy(ids, transIds,valueAction, op);
				 return jsonArray;
				 
			}
			if (TeamUPConstant.PFILEDOWNLOAD.equals(method)) {
				String fileName = req.getParameter("fileName");
				String fstatus = req.getParameter("fstatus");
				String trasnIds = null;
				String [] transSeq = null;
				String fileDisplay ="";
				id = req.getParameter("agentId");
				DownloadManager dm = new DownloadManager();
				if(fileName!= null && id != null && req.getParameter("creationDate") != null ) {
					if(DownloadStatus.CURRENT.getCode().equals(fstatus) 
							|| DownloadStatus.ARCHIVED.getCode().equals(fstatus) 
							|| DownloadStatus.POLICY_DOWNLOAD.getCode().equals(fstatus) 
							|| DownloadStatus.RESEND.getCode().equals(fstatus)
							|| DownloadStatus.DB_CURRENT.getCode().equals(fstatus)
							|| DownloadStatus.DB_ARCHIVED.getCode().equals(fstatus)) {
						fileDisplay = fileName +".AL3";
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
						
						byte[] fileBytes =dm.fileDownload(op,id, fileName, createdDate,transSeq);
						in =  new ByteArrayInputStream(fileBytes);
						byte[] buf = new byte[32000];
						int read; 
						while ((read = in.read(buf, 0, 32000)) != -1)
						{
							out.write(buf, 0, read);
						}
					}else if(DownloadStatus.POLICYXML_CURRENT.getCode().equals(fstatus) 
							|| DownloadStatus.POLICYXML_ARCHIVED.getCode().equals(fstatus) 
							||DownloadStatus.POLICYXML_DOWNLOAD.getCode().equals(fstatus) 
							||DownloadStatus.POLICYXML_RESEND.getCode().equals(fstatus)) {
						fileDisplay = fileName +".xml";
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
