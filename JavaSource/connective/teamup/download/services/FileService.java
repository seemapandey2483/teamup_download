package connective.teamup.download.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DirectBillTrans;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileTrans;

public class FileService {

	private static final Logger LOGGER = Logger.getLogger(FileService.class);
	
	public static String getPolicyTransactionList(String id,String fileName,long createdDate,DatabaseOperation op) {

		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		String jsonArray ="";
		Gson gson = new Gson();
		
		List<FileTrans> listOfTransaction = new ArrayList<FileTrans>();
		
		try{
			listOfTransaction = op.getPolicyFileTransaction(id, fileName, createdDate);
			int count = op.getPolicyFileTransCount(id, fileName, createdDate);
			// Return in the format required by jTable plugin
			JSONROOT.put("Result", "OK");
			JSONROOT.put("Records", listOfTransaction);
			JSONROOT.put("TotalRecordCount", count);
			// Convert Java Object to Json
			 jsonArray = gson.toJson(JSONROOT);
			
		}catch(Exception ex) {
			LOGGER.error(ex);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
	
	public static String getClaimTransactionList(String id,String fileName,long createdDate,DatabaseOperation op) {
		String jsonArray ="";
		Gson gson = new Gson();
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();
		List<FileTrans> listOfTransaction = new ArrayList<FileTrans>();
		
		try{
			listOfTransaction = op.getClaimFileTransaction(id, fileName, createdDate);
			int count = op.getClaimFileTransCount(id, fileName, createdDate);
			// Return in the format required by jTable plugin
			JSONROOT.put("Result", "OK");
			JSONROOT.put("Records", listOfTransaction);
			JSONROOT.put("TotalRecordCount", count);
			// Convert Java Object to Json
			 jsonArray = gson.toJson(JSONROOT);
			
		}catch(Exception ex) {
			LOGGER.error(ex);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
	
	public static String getDirectBillTransactionList(String id,String fileName,long createdDate,int startPageIndex,int recordsPerPage,DatabaseOperation op) {

		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		String jsonArray ="";
		Gson gson = new Gson();
		List<DirectBillTrans> listOfTransaction = new ArrayList<DirectBillTrans>();
		try{
			
			int start = startPageIndex + 1;
			int end = recordsPerPage + startPageIndex;
			listOfTransaction = op.loadDirectBills(id, fileName, createdDate,start, end);
			int count = op.getDirectBillFileTransCount(id, fileName, createdDate);
			// Return in the format required by jTable plugin
			JSONROOT.put("Result", "OK");
			JSONROOT.put("Records", listOfTransaction);
			JSONROOT.put("TotalRecordCount", count);
			// Convert Java Object to Json
			 jsonArray = gson.toJson(JSONROOT);
			
		}catch(Exception ex) {
			LOGGER.error(ex);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
	
	public static String deletePolicy(String ids,DatabaseOperation op) {
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		List<String> agentId = null;
		List<String> origFileName = null;
		List<Long> createdDate = null;
		
		String[][] secondSplit;
		String[] firstSplit;
		int k = 0;
		String jsonArray ="";
		Gson gson = new Gson();
		try{
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
			 jsonArray = gson.toJson(JSONROOT);

		}catch(Exception ex){
			LOGGER.error(ex);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
	
	public static String updatePolicy(String ids,String transIds,String valueAction,DatabaseOperation op) {
		
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		String key ="";
		String jsonArray ="";
		Gson gson = new Gson();
		HashMap<String,String[]> childsAL3Map = new HashMap<String,String[]>();
		HashMap<String,String[]> childsXMLMap = new HashMap<String,String[]>();
		HashMap<String,String[]> parentAL3Map = new HashMap<String,String[]>();
		HashMap<String,String[]> parentXMLMap = new HashMap<String,String[]>();
		HashMap<String,String[]> finalAL3Map = new HashMap<String,String[]>();
		HashMap<String,String[]> finalXMLMap = new HashMap<String,String[]>();
		try{
			if(ids!= null && !"".equals(ids)) {
				String [] fir = ids.split(",");
				if(fir!= null) {
					for(String str:fir){
						String sir[] = str.split(":");
						key = sir[0] + sir[1] + sir[2] +sir[3];
						
						if(DownloadStatus.POLICYXML_CURRENT.getCode().equals(sir[3]) || 
								DownloadStatus.POLICYXML_ARCHIVED.getCode().equals(sir[3])  ||
								DownloadStatus.POLICYXML_RESEND.getCode().equals(sir[3])||
								DownloadStatus.POLICYXML_DOWNLOAD.getCode().equals(sir[3])){
							parentXMLMap.put(key, sir);	
						}else{
							parentAL3Map.put(key, sir);	
						}

						
					}
				}
			
		}
		
		if(transIds!= null && !"".equals(transIds)) {
				String [] fir = transIds.split(",");
				if(fir!= null) {
					for(String str:fir){
						String sir[] = str.split("::");
						key = sir[0] + sir[1] + sir[2] + sir[3] +sir[4];
						if(DownloadStatus.POLICYXML_CURRENT.getCode().equals(sir[4]) || 
								DownloadStatus.POLICYXML_ARCHIVED.getCode().equals(sir[4])  ||
								DownloadStatus.POLICYXML_RESEND.getCode().equals(sir[4])||
								DownloadStatus.POLICYXML_DOWNLOAD.getCode().equals(sir[4])){
							
							if(childsXMLMap.get(key) != null) {
								String []ex =  childsXMLMap.get(key);
								String sequence = ex[3] +","+sir[3];
								ex[3] = sequence;
								childsXMLMap.put(key, ex);

							}else {
								childsXMLMap.put(key, sir);

							}
						}else {
							if(childsAL3Map.get(key) != null) {
								String []ex =  childsAL3Map.get(key);
								String sequence = ex[3] +","+sir[3];
								ex[3] = sequence;
								childsAL3Map.put(key, ex);

							}else {
								childsAL3Map.put(key, sir);

							}
							
						}
					
					}
				}
		
		}
		if(parentAL3Map != null && parentAL3Map.keySet().size()>0) {
			if(childsAL3Map!= null && childsAL3Map.size()>0) {
				for(String key1 :parentAL3Map.keySet()) {
					if(childsAL3Map.get(key1) == null) {
						finalAL3Map.put(key1, parentAL3Map.get(key1));
					}
					
				}	
				
			}else {
				finalAL3Map = parentAL3Map;
			}
		}
		
		if(parentXMLMap != null && parentXMLMap.keySet().size()>0) {
			if(childsXMLMap!= null && childsXMLMap.size()>0) {
				for(String key1 :parentXMLMap.keySet()) {
					if(childsXMLMap.get(key1) == null) {
						finalXMLMap.put(key1, parentXMLMap.get(key1));
					}
					
				}	
				
			}else {
				finalXMLMap = parentXMLMap;
			}
		}
		if(finalAL3Map.size()>0 || childsAL3Map.size()>0){
			if(DownloadStatus.CURRENT.getCode().equals(valueAction)){
				valueAction = DownloadStatus.CURRENT.getCode();
			}else if(DownloadStatus.ARCHIVED.getCode().equals(valueAction)){
				valueAction = DownloadStatus.ARCHIVED.getCode();
			}else if(DownloadStatus.RESEND.getCode().equals(valueAction)){
				valueAction = DownloadStatus.RESEND.getCode();
			}else if(DownloadStatus.POLICY_DOWNLOAD.getCode().equals(valueAction)){
				valueAction = DownloadStatus.POLICY_DOWNLOAD.getCode();
			}
				op.updatePolicyFiles(op,finalAL3Map, childsAL3Map,  valueAction);
		}

		
		if(finalXMLMap.size()>0 || childsXMLMap.size()>0) {
			if(DownloadStatus.CURRENT.getCode().equals(valueAction)){
				valueAction = DownloadStatus.POLICYXML_CURRENT.getCode();
			}else if(DownloadStatus.ARCHIVED.getCode().equals(valueAction)){
				valueAction = DownloadStatus.POLICYXML_ARCHIVED.getCode();
			}else if(DownloadStatus.POLICY_DOWNLOAD.getCode().equals(valueAction)){
				valueAction = DownloadStatus.POLICYXML_DOWNLOAD.getCode();
			}else if(DownloadStatus.RESEND.getCode().equals(valueAction)){
				valueAction = DownloadStatus.POLICYXML_RESEND.getCode();
			}
			
				op.updatePolicyFiles(op,finalXMLMap, childsXMLMap,  valueAction);
			
		}
		
		JSONROOT.put("Result", "Ok");
		 jsonArray = gson.toJson(JSONROOT);
		}catch(Exception ex) {
			LOGGER.error(ex);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
	
	public static String deleteDirectBill(String ids,DatabaseOperation op) {
		
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		List<String> agentId = null;
		List<String> origFileName = null;
		List<Long> createdDate = null;
		
		String[][] secondSplit;
		String[] firstSplit;
		int k = 0;
		String jsonArray ="";
		Gson gson = new Gson();
		try{
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
			 jsonArray = gson.toJson(JSONROOT);

		}catch(Exception ex){
			LOGGER.error(ex);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
	public static String deleteClaim(String ids,DatabaseOperation op) {
		List<String> agentId = null;
		List<String> origFileName = null;
		List<Long> createdDate = null;
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();
		
		String[][] secondSplit;
		String[] firstSplit;
		int k = 0;
		String jsonArray ="";
		Gson gson = new Gson();
		try{
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
			op.deleteClaimFiles(agentId, origFileName, createdDate);
			JSONROOT.put("Result", "Ok");
			 jsonArray = gson.toJson(JSONROOT);

		}catch(Exception ex){
			LOGGER.error(ex);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
}
