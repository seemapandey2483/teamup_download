package connective.teamup.download.ws.commands;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.ArchiveService;
import connective.teamup.download.ws.objects.AgentIdInput;
import connective.teamup.download.ws.objects.DownloadFileArrayOutput;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

public class GetArchiveSearchPaginatedFiles implements ICommand {
	
	@Override
	public Object perform(Object inputData, SecurityStatus secInfo)
			throws Exception {
		AgentIdInput agentIdInput = (AgentIdInput)inputData;
		int pageNo = 1;
		int rowsPerSelected = 0;
		String searchString = null;
		String searchFor = null;
		String agentId =  agentIdInput.getAgentId();
		if(agentId!= null && !"".equals(agentId)) {
			if(agentId.contains("#")) {
				String [] agnt = agentId.split("#");
				pageNo = Integer.valueOf(agnt[1]);
				rowsPerSelected = Integer.valueOf(agnt[2]);
				searchString = String.valueOf(agnt[3]);
				searchFor = String.valueOf(agnt[4]);
			}
		}
		
		DownloadFileArrayOutput downloadFileArrayOutput = null; 
		
		DatabaseOperation databaseOperation = null;
		DownloadFileInfo[] downloadFileInfos = null;
		
		try{
			ArchiveService archiveService = new ArchiveService();
			databaseOperation = DatabaseFactory.getInstance().startOperation();
			downloadFileInfos = archiveService.getArchiveSearchFiles(databaseOperation, secInfo.getAgentId(), pageNo, rowsPerSelected, true, searchString,searchFor);
		} finally {
			if (databaseOperation != null)
				databaseOperation.close();
		}
		
		downloadFileArrayOutput = new DownloadFileArrayOutput();
		downloadFileArrayOutput.setFiles(downloadFileInfos);
		return downloadFileArrayOutput;
	}

}
