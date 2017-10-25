package connective.teamup.download.ws.commands;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.ArchiveService;
import connective.teamup.download.ws.objects.DownloadFileArrayOutput;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

public class GetArchiveFilesSelected implements ICommand{
	
	//@Override
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception {
		// TODO Auto-generated method stub
				DownloadFileArrayOutput output = null;
				
				DatabaseOperation op = null;
				DownloadFileInfo[] files = null;
				try
				{
					ArchiveService archive = new ArchiveService();
					op = DatabaseFactory.getInstance().startOperation();
					files = archive.getArchiveFilesSelected(op, secInfo.getAgentId());
				}
				finally
				{
					if (op != null)
						op.close();
				}

				output = new DownloadFileArrayOutput();
				output.setFiles(files);		
				return output;
	}

}
