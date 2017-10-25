/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.commands;

import java.util.ArrayList;
import java.util.Hashtable;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.ws.objects.GetConfigPropertiesInput;
import connective.teamup.download.ws.objects.GetConfigPropertiesOutput;
import connective.teamup.download.ws.objects.OptionBean;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetConfigProperties implements ICommand {

	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		GetConfigPropertiesInput input = (GetConfigPropertiesInput) inputData;
		GetConfigPropertiesOutput output = null;
		String[] propertyNames = input.getPropertyNames();
		
		ArrayList propList = new ArrayList();
		DatabaseOperation op = null;
		try
		{
			op = DatabaseFactory.getInstance().startOperation();
			Hashtable props = op.getProperties();
			for (int i=0; i < propertyNames.length; i++)
			{
				String value = (String) props.get(propertyNames[i]);
				if (value == null)
					value = "";
				propList.add(new OptionBean(propertyNames[i], value));
			}
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		OptionBean[] ret = new OptionBean[propList.size()];
		propList.toArray(ret);
		output = new GetConfigPropertiesOutput();
		output.setValues(ret);
		
		return output;
	}

}
