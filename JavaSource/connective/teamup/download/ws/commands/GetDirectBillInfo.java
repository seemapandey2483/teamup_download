package connective.teamup.download.ws.commands;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.ArchiveService;
import connective.teamup.download.ws.objects.DirectBillFileOutput;
import connective.teamup.download.ws.objects.GetDownloadFileInput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

public class GetDirectBillInfo  implements ICommand {

	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		GetDownloadFileInput input = (GetDownloadFileInput) inputData;
		
		DatabaseOperation op = null;
		DirectBillFileOutput file = null;
		try
		{
			ArchiveService archive = new ArchiveService();
			op = DatabaseFactory.getInstance().startOperation();
			file = archive.getDirectBillInfo(op, secInfo.getAgentId(), input.getOriginalFilename(),	input.getCreatedDate());
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		return file;
	}
}
