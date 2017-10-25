package connective.teamup.download;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import connective.teamup.download.actions.DownloadManager;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DirectBillTrans;
import connective.teamup.download.db.FileHDR;
import connective.teamup.download.db.FileTrans;

import com.google.gson.Gson;

public class FileController extends HttpServlet{
	
	private static final Logger LOGGER = Logger.getLogger(FileController.class);
	
	private static final long serialVersionUID = 3024703756055172264L;
	
	List<String> agentId = null;
	List<String> origFileName = null;
	List<Long> createdDate = null;
	
	String[][] secondSplit;
	String[] firstSplit;
	String valueAction = null;
	int k = 0;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		Gson gson = new Gson();
		resp.setContentType("application/json");
		DatabaseOperation op = null;
		String action = req.getParameter("action");
		String id = (String) req.getSession().getAttribute("agentId");
		InputStream in = null;
		ServletOutputStream out = null;
		String searchPolicyNumber = "";
		String searchInsuredName = "";
		String searchTxnType = "";
		String searchFromDate = "";
		String searchToDate = "";
		Long fromMillSeconds = null;
		Long toMillSeconds = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		
		try {
			op = DatabaseFactory.getInstance().startOperation();
			if (action != null) {
				try {
					if (action.equals("plist")) {
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
						List<FileHDR> files = op.getPolicyFiles(id, start, end,sortBy,searchPolicyNumber,searchInsuredName,searchTxnType,fromMillSeconds,toMillSeconds);
						int count = op.getPolicyFilesCount(id,searchPolicyNumber,searchInsuredName,searchTxnType,fromMillSeconds,toMillSeconds);
						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");
						JSONROOT.put("Records", files);
						JSONROOT.put("TotalRecordCount", count);
						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						resp.getWriter().print(jsonArray);
					}
					if (action.equals("dlist")) {
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
						List<FileHDR> files = op.getDirectBillFiles(id, start, end,sortBy,searchPolicyNumber,searchInsuredName,searchTxnType,searchflag,fromMillSeconds,toMillSeconds);
						int count = op.getDirectBillFilesCount(id,searchPolicyNumber,searchInsuredName,searchTxnType,searchflag,fromMillSeconds,toMillSeconds);
						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");
						JSONROOT.put("Records", files);
						JSONROOT.put("TotalRecordCount", count);
						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						resp.getWriter().print(jsonArray);
					}
					if (action.equals("pftlist")) {
						List<FileTrans> listOfTransaction = new ArrayList<FileTrans>();
						String fileName = req.getParameter("fileName");
						long createdDate = Long.valueOf(req.getParameter("creationDate"));
						listOfTransaction = op.getPolicyFileTransaction(id, fileName, createdDate);
						int count = op.getPolicyFileTransCount(id, fileName, createdDate);
						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");
						JSONROOT.put("Records", listOfTransaction);
						JSONROOT.put("TotalRecordCount", count);
						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						resp.getWriter().print(jsonArray);
					}
					if (action.equals("dbftlist")) {
						List<DirectBillTrans> listOfTransaction = new ArrayList<DirectBillTrans>();
						

						String fileName = req.getParameter("fileName");
						int startPageIndex = 1;
						int recordsPerPage = 0;
						startPageIndex = Integer.parseInt(req.getParameter("jtStartIndex"));
						recordsPerPage = Integer.parseInt(req.getParameter("jtPageSize"));
						int start = startPageIndex + 1;
						int end = recordsPerPage + startPageIndex;
						long createdDate = Long.parseLong(req.getParameter("creationDate"));
						listOfTransaction = op.loadDirectBills(id, fileName, createdDate,start, end);
						int count = op.getDirectBillFileTransCount(id, fileName, createdDate);
						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");
						JSONROOT.put("Records", listOfTransaction);
						JSONROOT.put("TotalRecordCount", count);
						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						resp.getWriter().print(jsonArray);
					}
					if (action.equals("deletePolicy")) {
						String ids = req.getParameter("ids");
						firstSplit = ids.split(",");
						secondSplit = new String[firstSplit.length][];
						agentId = new ArrayList<String>();
						origFileName = new ArrayList<String>();
						createdDate = new ArrayList<Long>();
						for (int i = 0; i < firstSplit.length; i++) {
							secondSplit[i] = firstSplit[i].split(":");
						}
						k = 0;
						for (int j = 0; j < secondSplit.length; j++) {
							String aid = String.valueOf(secondSplit[k][0]);
							k++;
							agentId.add(aid);
						}
						k = 0;
						for (int j = 0; j < secondSplit.length; j++) {
							String fileName = String.valueOf(secondSplit[k][1]);
							k++;
							origFileName.add(fileName);
						}
						k = 0;
						for (int j = 0; j < secondSplit.length; j++) {
							long date = Long.valueOf(secondSplit[k][2]);
							k++;
							createdDate.add(date);
						}
						op.deletePolicyFiles(agentId, origFileName, createdDate);
						JSONROOT.put("Result", "Ok");
						String jsonArray = gson.toJson(JSONROOT);
						resp.getWriter().print(jsonArray);
					}
					if (action.equals("deleteDirectBill")) {
						String ids = req.getParameter("ids");
						firstSplit = ids.split(",");
						secondSplit = new String[firstSplit.length][];
						agentId = new ArrayList<String>();
						origFileName = new ArrayList<String>();
						createdDate = new ArrayList<Long>();
						for (int i = 0; i < firstSplit.length; i++) {
							secondSplit[i] = firstSplit[i].split(":");
						}
						k = 0;
						for (int j = 0; j < secondSplit.length; j++) {
							String aid = String.valueOf(secondSplit[k][0]);
							k++;
							agentId.add(aid);
						}
						k = 0;
						for (int j = 0; j < secondSplit.length; j++) {
							String fileName = String.valueOf(secondSplit[k][1]);
							k++;
							origFileName.add(fileName);
						}
						k = 0;
						for (int j = 0; j < secondSplit.length; j++) {
							long date = Long.valueOf(secondSplit[k][2]);
							k++;
							createdDate.add(date);
						}
						op.deleteDirectBillFiles(agentId, origFileName, createdDate);
						
						JSONROOT.put("Result", "Ok");
						String jsonArray = gson.toJson(JSONROOT);
						resp.getWriter().print(jsonArray);
					}
					if (action.equals("update")) {
						String ids = req.getParameter("ids");
						String transIds = req.getParameter("transactionIds");
						String key ="";
						HashMap<String,String[]> childsMap = new HashMap<String,String[]>();
						HashMap<String,String[]> parentMap = new HashMap<String,String[]>();
						HashMap<String,String[]> finalMap = new HashMap<String,String[]>();
						
						if(ids!= null && !"".equals(ids)) {
								String [] fir = ids.split(",");
								if(fir!= null) {
									for(String str:fir){
										String sir[] = str.split(":");
										key = sir[0] + sir[1] + sir[2];
										parentMap.put(key, sir);
									}
								}
							
						}
						
						if(transIds!= null && !"".equals(transIds)) {
								String [] fir = transIds.split(",");
								if(fir!= null) {
									for(String str:fir){
										String sir[] = str.split("::");
										key = sir[0] + sir[1] + sir[2];
										
										if(childsMap.get(key) != null) {
											String []ex =  childsMap.get(key);
											String sequence = ex[3] +","+sir[3];
											ex[3] = sequence;
											childsMap.put(key, ex);

										}else {
											childsMap.put(key, sir);

										}
									}
								}
						
						}
						if(parentMap != null && parentMap.keySet().size()>0) {
							if(childsMap!= null && childsMap.size()>0) {
								for(String key1 :parentMap.keySet()) {
									if(childsMap.get(key1) == null) {
										finalMap.put(key1, parentMap.get(key1));
									}
									
								}	
								
							}else {
								finalMap = parentMap;
							}
						}
						valueAction = req.getParameter("actionValue");
						op.updatePolicyFiles(op,finalMap, childsMap,  valueAction);
						
						JSONROOT.put("Result", "Ok");
						String jsonArray = gson.toJson(JSONROOT);
						resp.getWriter().print(jsonArray);
					}
					if (action.equals("dwnlFiles")){
						
						String fileName = req.getParameter("fileName");
						String trasnIds = null;
						String [] transSeq = null;
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
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage());
					JSONROOT.put("Result", "ERROR");
					JSONROOT.put("Message", ex.getMessage());
					String error = gson.toJson(JSONROOT);
					resp.getWriter().print(error);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (op != null)
				op.close();
			if(in != null)
				in.close();
			if(out != null){
				out.flush();
				out.close();
			}
		}
	}

}
