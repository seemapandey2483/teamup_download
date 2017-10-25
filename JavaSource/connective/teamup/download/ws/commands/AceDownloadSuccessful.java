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
import connective.teamup.download.services.AceDownloadService;
import connective.teamup.download.ws.objects.AceDownloadSuccessfulInput;
import connective.teamup.download.ws.objects.AceDownloadSuccessfulOutput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AceDownloadSuccessful implements ICommand 
{
	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		AceDownloadSuccessfulInput input = (AceDownloadSuccessfulInput) inputData;
		AceDownloadSuccessfulOutput output = null;
		
		DatabaseOperation op = null;
		try
		{
			AceDownloadService service = new AceDownloadService();
			op = DatabaseFactory.getInstance().startOperation();
			AgentInfo agent = op.getAgentInfo(secInfo.getAgentId());
			int batchesRemaining = service.downloadSuccessful(op, agent, input.getBatchNumber(), input.getFiles());
			output = new AceDownloadSuccessfulOutput();
			output.setBatchesRemaining(batchesRemaining);
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		return output;
	}
}
