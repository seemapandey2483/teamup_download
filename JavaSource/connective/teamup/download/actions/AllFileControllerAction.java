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
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.FileHDR;
import connective.teamup.download.services.FileService;

public class AllFileControllerAction implements Action{

	private static final Logger LOGGER = Logger.getLogger(AllFileControllerAction.class);
	
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException		{

		
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		String method = req.getParameter("method");
		String jsonArray ="";
		Gson gson = new Gson();
		
		InputStream in = null;
		ServletOutputStream out = null;
		String searchPolicyNumber = "";
		String searchInsuredName = "";
		String searchTxnType = "";
		String searchFromDate = "";
		String searchToDate = "";
		String searchAgentId = ""; 
		Long fromMillSeconds = null;
		Long toMillSeconds = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		
		
		try{
			
			if ("plist".equals(method)) {
				int startPageIndex = 1;
				int recordsPerPage = 0;
				startPageIndex = Integer.parseInt(req.getParameter("jtStartIndex"));
				recordsPerPage = Integer.parseInt(req.getParameter("jtPageSize"));
				int start = startPageIndex + 1;
				int end = recordsPerPage + startPageIndex;
				String sortBy = req.getParameter("jtSorting");
				
				if(sortBy== null || "".equals(sortBy)) {
					sortBy = "created_dt DSC";
					
				} else{
					sortBy = FileHDR.getColumName(sortBy);
				}
				searchAgentId = req.getParameter("srcPolicyAgentId");
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
				int count = op.getPolicyFilesCount(searchAgentId,searchPolicyNumber,searchInsuredName,searchTxnType,fromMillSeconds,toMillSeconds);
				if(count > 0)
					files = op.getPolicyFiles( searchAgentId,start, end,sortBy,searchPolicyNumber,searchInsuredName,searchTxnType,fromMillSeconds,toMillSeconds);

				// Return in the format required by jTable plugin
				JSONROOT.put("Result", "OK");
				JSONROOT.put("Records", files);
				JSONROOT.put("TotalRecordCount", count);
				// Convert Java Object to Json
				 jsonArray = gson.toJson(JSONROOT);
			}
			if ("dlist".equals(method)) {
				int startPageIndex = 1;
				int recordsPerPage = 0;
				startPageIndex = Integer.parseInt(req.getParameter("jtStartIndex"));
				recordsPerPage = Integer.parseInt(req.getParameter("jtPageSize"));
				int start = startPageIndex + 1;
				int end = recordsPerPage + startPageIndex;
				String sortBy = req.getParameter("jtSorting");
				if(sortBy== null || "".equals(sortBy)) {
					sortBy = "created_dt DSC";
					
				} else{
					sortBy = FileHDR.getColumName(sortBy);
				}
				searchAgentId = req.getParameter("srcDBRAgentId");
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
				int count = op.getDirectBillFilesCount(searchAgentId,searchPolicyNumber,searchInsuredName,searchTxnType,searchflag,fromMillSeconds,toMillSeconds);
				if(count > 0)
					files = op.getDirectBillFiles(searchAgentId,start, end,sortBy,searchPolicyNumber,searchInsuredName,searchTxnType,searchflag,fromMillSeconds,toMillSeconds);
				// Return in the format required by jTable plugin
				JSONROOT.put("Result", "OK");
				JSONROOT.put("Records", files);
				JSONROOT.put("TotalRecordCount", count);
				// Convert Java Object to Json
				 jsonArray = gson.toJson(JSONROOT);
			}
			if ("clist".equals(method)) {
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
			
				
				if(sortBy== null || "".equals(sortBy)) {
					sortBy = "created_dt DESC";
					
				} else{
					sortBy = FileHDR.getColumName(sortBy);
				}
				searchAgentId = req.getParameter("srcClaimAgentId");
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
				int count = op.getClaimFilesCount(searchAgentId,srchClaimNumber,srchClmPolicyNumber,srchclaimStatus,srchclaimBPTCode,fromMillSeconds,toMillSeconds);
				if(count > 0)
					files = op.getClaimFiles(searchAgentId, start, end,sortBy,srchClaimNumber,srchClmPolicyNumber,srchclaimStatus,srchclaimBPTCode,fromMillSeconds,toMillSeconds);
				// Return in the format required by jTable plugin
				JSONROOT.put("Result", "OK");
				JSONROOT.put("Records", files);
				JSONROOT.put("TotalRecordCount", count);
				// Convert Java Object to Json
				 jsonArray = gson.toJson(JSONROOT);
				 return jsonArray;
			}
			if ("pftlist".equals(method)) {
				String fileName = req.getParameter("fileName");
				long createdDate = Long.valueOf(req.getParameter("creationDate"));
				String agentId = req.getParameter("agentId");

				jsonArray = FileService.getPolicyTransactionList(agentId, fileName, createdDate, op);
			}
			if ("dbftlist".equals(method)) {
				String fileName = req.getParameter("fileName");
				String agentId = req.getParameter("agentId");
				long createdDate = Long.parseLong(req.getParameter("creationDate"));
				int startPageIndex = 1;
				int recordsPerPage = 0;
				startPageIndex = Integer.parseInt(req.getParameter("jtStartIndex"));
				recordsPerPage = Integer.parseInt(req.getParameter("jtPageSize"));
				jsonArray = FileService.getDirectBillTransactionList(agentId, fileName, createdDate, startPageIndex, recordsPerPage, op);
			}
			if ("cftlist".equals(method)) {

				String fileName = req.getParameter("fileName");
				long createdDate = Long.valueOf(req.getParameter("creationDate"));
				String agentId = req.getParameter("agentId");

				jsonArray = FileService.getClaimTransactionList(agentId, fileName, createdDate, op);
				return jsonArray;
			}	
			if ("deletePolicy".equals(method)) {
				String ids = req.getParameter("ids");
				jsonArray = FileService.deletePolicy(ids, op);
			}
			if ("deleteDirectBill".equals(method)) {
				String ids = req.getParameter("ids");
				jsonArray = FileService.deleteDirectBill(ids, op);
			}
			if ("deleteClaim".equals(method)) {
				String ids = req.getParameter("ids");
				jsonArray = FileService.deleteClaim(ids, op);
				return jsonArray;
			}	
			if ("update".equals(method)) {
				String ids = req.getParameter("ids");
				String transIds = req.getParameter("transactionIds");
				String valueAction = req.getParameter("actionValue");
				
				 jsonArray = FileService.updatePolicy(ids, transIds,valueAction, op);
			}
			if ("dwnlFiles".equals(method)){
				String fileName = req.getParameter("fileName");
				String trasnIds = null;
				String [] transSeq = null;
				String id =  req.getParameter("agentId");
				if(fileName!= null && id != null && req.getParameter("creationDate") != null ) {
					String fileDisplay = fileName +".AL3";
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
					byte[] fileBytes =dm.fileDownload(op,id, fileName, createdDate,transSeq);
					in =  new ByteArrayInputStream(fileBytes);
					byte[] buf = new byte[32000];
					int read; 
					while ((read = in.read(buf, 0, 32000)) != -1)
					{
						out.write(buf, 0, read);
					}
				
				}
			}
			if ("dwnlClaimFiles".equals(method)) {
				String fileName = req.getParameter("fileName");
				String trasnIds = null;
				String [] transSeq = null;
				String id =  req.getParameter("agentId");

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
			
		}catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
}
