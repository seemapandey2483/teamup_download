package connective.teamup.download.ws.commands;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.ws.objects.ApplicationVersionOutput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

public class GetApplicationVersion implements ICommand {

	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{

		ApplicationVersionOutput output = null;
		String applicationVersion = "";
		DatabaseOperation op = null;

		try
		{
			op = DatabaseFactory.getInstance().startOperation();
			applicationVersion = op.getPropertyValue(DatabaseFactory.PROP_VERSION);
		}
		finally
		{
			if (op != null)
				op.close();
		}

		output = new ApplicationVersionOutput();
		output.setAppVersion(applicationVersion);	
		return output;
	}
}
