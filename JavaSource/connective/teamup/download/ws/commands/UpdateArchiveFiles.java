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
import connective.teamup.download.ws.objects.UpdateArchiveFilesInput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UpdateArchiveFiles implements ICommand {

	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		UpdateArchiveFilesInput input = (UpdateArchiveFilesInput) inputData;
		DatabaseOperation op = null;
		
		try
		{
			ArchiveService archive = new ArchiveService();
			op = DatabaseFactory.getInstance().startOperation();
			archive.updateFiles(op, secInfo.getAgentId(), input.getFiles());
		}
		finally
		{
			if (op != null)
				op.close();
		}
		return null;
	}

}
