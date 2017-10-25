/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.commands;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.AceDownloadService;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.download.ws.objects.GetAppliedEditFileInput;
import connective.teamup.download.ws.objects.GetAppliedEditFileOutput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetAppliedEditFile implements ICommand 
{
	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		GetAppliedEditFileInput input = (GetAppliedEditFileInput) inputData;
		GetAppliedEditFileOutput output = null;
		
		DatabaseOperation op = null;
		DownloadFileInfo file = null;
		try
		{
			AceDownloadService service = new AceDownloadService();
			op = DatabaseFactory.getInstance().startOperation();
			file = service.getAppliedEditFile(op, secInfo.getAgentId(), input.getBatchNumber(),
				input.getOriginalFilename(), input.getCreatedDate());
			output = new GetAppliedEditFileOutput();
			output.setFile(file);
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		return output;
	}
}
