/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.commands;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.ArchiveService;
import connective.teamup.download.ws.objects.AgentIdInput;
import connective.teamup.download.ws.objects.DownloadFileArrayOutput;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetArchiveFiles implements ICommand 
{
	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		AgentIdInput input = (AgentIdInput) inputData;
		DownloadFileArrayOutput output = null;
		
		DatabaseOperation op = null;
		DownloadFileInfo[] files = null;
		try
		{
			ArchiveService archive = new ArchiveService();
			op = DatabaseFactory.getInstance().startOperation();
			files = archive.getArchiveFiles(op, secInfo.getAgentId(), true);
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
