package connective.teamup.al3;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * Insert the type's description here.
 * Creation date: (9/28/2001 10:06:31 AM)
 * @author: Mike Haney
 */
public class DefFileHandler extends DefaultHandler 
{
	private static final Logger LOGGER = Logger.getLogger(DefFileHandler.class);
	
	private Hashtable groups = new Hashtable();
	private GroupDef currentGroup = null;
	private FieldDef currentField = null;
	private String cdata = null;

	/**
	 * ConfigFileHandler constructor comment.
	 */
	public DefFileHandler(Hashtable groups)
	{
		super();
	
		this.groups = groups;
	}

	public void characters(char[] ch, int start, int length) throws SAXException
	{
		String tmp = new String(ch, start, length);
		if (tmp == null)
			tmp = "";
		cdata += tmp;		
	}	

	/**
	 * Insert the method's description here.
	 * Creation date: (9/28/2001 10:29:49 AM)
	 */
	public void endElement(String namespace, String simpleName, String qualifiedName) throws org.xml.sax.SAXException
	{
		if (qualifiedName.equals("group"))
		{
			// save the current group to the hashtable
			groups.put(currentGroup.getName(), currentGroup);
			currentGroup = null;
		}
		
		else if (qualifiedName.equals("field"))
		{
			// add the current field to the group
			currentGroup.addField(currentField);
			currentField = null;
		}
		
		else if (qualifiedName.equals("name"))
		{
			currentField.setName(cdata);
		}
		else if (qualifiedName.equals("refname"))
		{
			currentField.setRefName(cdata);
		}
		else if (qualifiedName.equals("description"))
		{
			currentField.setDescription(cdata);
		}
		else if (qualifiedName.equals("longdesc"))
		{
			currentField.setLongDesc(cdata);
		}
		else if (qualifiedName.equals("start"))
		{
			currentField.setStart(cdata);
		}
		else if (qualifiedName.equals("length"))
		{
			currentField.setLength(cdata);
		}
		else if (qualifiedName.equals("class"))
		{
			currentField.setAL3Class(cdata);
		}
		else if (qualifiedName.equals("type"))
		{
			currentField.setType(cdata);
		}
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (9/28/2001 10:29:49 AM)
	 */
	public void startElement(String namespace, String simpleName, String qualifiedName, Attributes attr) throws org.xml.sax.SAXException
	{
		cdata = "";
		
		try
		{
			if (qualifiedName.equals("group"))
			{
				// get the group attributes
				String name = attr.getValue("name");
				String desc = attr.getValue("description");
				
				// create the group def object and save it to the current group
				currentGroup = new GroupDef(name, desc);
			}
			else if (qualifiedName.equals("field"))
			{
				// create a new field def object and save it to the current field
				currentField = new FieldDef();
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			System.out.println(e.getMessage());
		}
	}
}
