/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.commands;

import java.util.Hashtable;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.ws.objects.OptionBean;
import connective.teamup.download.ws.objects.SetConfigPropertiesInput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SetConfigProperties implements ICommand {

	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		SetConfigPropertiesInput input = (SetConfigPropertiesInput) inputData;
		OptionBean[] properties = input.getProperties();
		
		DatabaseOperation op = null;
		try
		{
			op = DatabaseFactory.getInstance().startOperation();
			Hashtable props = new Hashtable();
			for (int i=0; i < properties.length; i++)
				props.put(properties[i].getLabel(), properties[i].getValue());
			op.setProperties(props);
		}
		finally
		{
			if (op != null)
				op.close();
		}
		return null;
	}

}
