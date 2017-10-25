/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.commands;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.DownloadService;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.download.ws.objects.GetDownloadFileInput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetDownloadFile implements ICommand 
{

	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		GetDownloadFileInput input = (GetDownloadFileInput) inputData;
		
		DatabaseOperation op = null;
		DownloadFileInfo file = null;
		try
		{
			DownloadService service = new DownloadService();
			op = DatabaseFactory.getInstance().startOperation();
			file = service.getDownloadFile(op, secInfo.getAgentId(), input.getOriginalFilename(),
				input.getCreatedDate(), true);
			file.setFileContentsEncoded("#OFFLINE#");
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		return file;
	}

}
