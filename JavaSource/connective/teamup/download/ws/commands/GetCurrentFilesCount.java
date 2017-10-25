package connective.teamup.download.ws.commands;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.ArchiveService;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

public class GetCurrentFilesCount implements ICommand{
	
	//@Override
	public Object perform(Object inputData, SecurityStatus secInfo)
			throws Exception {
		int fileCount = 0;
		DatabaseOperation op = null;
		try{
			ArchiveService archiveService = new ArchiveService();
			op = DatabaseFactory.getInstance().startOperation();
			fileCount = archiveService.getCurrentFilesCount(op,secInfo.getAgentId());
		} finally{
			if(op!=null)
				op.close();
		}
		// TODO Auto-generated method stub
		return fileCount;
	}

}
