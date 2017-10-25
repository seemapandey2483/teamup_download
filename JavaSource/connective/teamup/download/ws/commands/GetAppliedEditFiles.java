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
import connective.teamup.download.ws.objects.GetAppliedEditFilesInput;
import connective.teamup.download.ws.objects.DownloadFileArrayOutput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetAppliedEditFiles implements ICommand 
{
	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		GetAppliedEditFilesInput input = (GetAppliedEditFilesInput) inputData;
		DownloadFileArrayOutput output = null;
		
		DownloadFileInfo[] files  = null;
		DatabaseOperation op = null;
		try
		{
			AceDownloadService service = new AceDownloadService();
			op = DatabaseFactory.getInstance().startOperation();
			files = service.getAppliedEditFiles(op, secInfo.getAgentId(), input.getDlStatus());
			output = new DownloadFileArrayOutput();
			output.setFiles(files);			
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		return output;
	}

}
