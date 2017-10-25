package connective.teamup.download.ws.commands;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.ArchiveService;
import connective.teamup.download.ws.objects.AgentIdInput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

public class GetCurrentSearchFileCount implements ICommand{
	
	@Override
	public Object perform(Object inputData, SecurityStatus secInfo)
			throws Exception {
		int fileCount = 0;
		DatabaseOperation op = null;
		AgentIdInput agentIdInput = (AgentIdInput)inputData;
		String searchString = null;
		String searchFor = null;
	
		String agentId =  agentIdInput.getAgentId();
		if(agentId!= null && !"".equals(agentId)) {
			if(agentId.contains("#")) {
				String [] agnt = agentId.split("#");
				searchString = String.valueOf(agnt[1]);
				searchFor = String.valueOf(agnt[2]);
			}
		}
		try{
			ArchiveService archiveService = new ArchiveService();
			op = DatabaseFactory.getInstance().startOperation();
			fileCount = archiveService.getCurrentsearchFileCount(op,secInfo.getAgentId(), searchString, searchFor);
		} finally{
			if(op!=null)
				op.close();
		}
		return fileCount;
	}

}
