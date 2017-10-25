/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.commands;

import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.DownloadService;
import connective.teamup.download.ws.objects.DownloadFileArrayOutput;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.download.ws.objects.GetDownloadFilesInput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetDownloadFiles implements ICommand {

	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		GetDownloadFilesInput input = (GetDownloadFilesInput) inputData;
		DownloadFileArrayOutput output = null;
		
		DownloadFileInfo[] files = null;
		DatabaseOperation op = null;
		try
		{
			DownloadService service = new DownloadService();
			op = DatabaseFactory.getInstance().startOperation();
			
			// retrieve the agent and verify it is not disabled
			AgentInfo agent = op.getAgentInfo(secInfo.getAgentId());
			if (agent == null) {
				throw new Exception("Invalid agent ID.");
			}
			else if (!agent.isActive()) {
				throw new Exception("This agent ID is not currently active.  Please contact your carrier representative to continue registration for TEAM-UP Download.");
			}
			
			// update the last login date for the agent
			agent.setLastLoginDate(System.currentTimeMillis());
			agent.save();
			
//			files = service.getDownloadFiles(op, input.getAgentId(), input.getDlStatus(), false);
			files = service.getDownloadFiles(op, secInfo.getAgentId(), input.getDlStatus(), true);
			if (files != null)
			{
				for (int i=0; i < files.length; i++)
					files[i].setFileContentsEncoded("#OFFLINE#");
			}
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
