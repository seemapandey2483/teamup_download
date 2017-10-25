/*
 * Created on Apr 8, 2005
 *
 * Defines all the information needed to do an import (field mappings, etc.)
 */
package connective.teamup.download.agentimport;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ImportDefinition extends DefaultHandler
{
	protected String serverUrl = null;
	protected boolean fileContainsAgents = false;
	protected boolean fileContainsParticipants = false;
	protected boolean overwriteExisting = false;
	protected ArrayList fieldList = new ArrayList();
	protected HashMap amsMap = new HashMap();

	// parsing support
	private String cdata = null;
	
	/**
	 * Constructor
	 * 
	 * @author haneym
	 */
	public ImportDefinition()
	{
		super();	
	}
	
	public void saveToXML(PrintWriter out) throws IOException
	{
		out.println("<?xml version=\"1.0\" ?>");
		out.println("<tudlimp>");
		out.println("<server>" + serverUrl + "</server>");
		out.println("<agents>" + (fileContainsAgents ? "true" : "false") + "</agents>");
		out.println("<participants>" + (fileContainsParticipants ? "true" : "false") + "</participants>");
		out.println("<overwrite>" + (overwriteExisting ? "true" : "false") + "</overwrite>");
		out.println("<fieldmap>");
		for (int i=0; i < fieldList.size(); i++)
			out.println("\t<field>" + (String) fieldList.get(i) + "</field>"); 
		out.println("</fieldmap>");
		out.println("<amsmap>");
		Iterator it = amsMap.keySet().iterator();
		while (it.hasNext())
		{
			String key = (String) it.next();
			out.println("\t<amsmapitem name=\"" + key + "\" value=\"" + (String) amsMap.get(key) + "\" />");
		}
		out.println("</amsmap>");
		out.println("</tudlimp>");
	}
	
	public void loadFromXML(InputStream is) throws ParserConfigurationException, SAXException, IOException
	{
		// initilize the variables
		serverUrl = null;
		fileContainsAgents = false;
		fileContainsParticipants = false;
		overwriteExisting = false;
		fieldList = new ArrayList();
		amsMap = new HashMap();

		// parse the file definition xml
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(is, this);
	}
	
	private String getMappingPropertiesName()
	{
		String label = null;
		if (getFileContainsAgents() && getFileContainsParticipants())
			label = DatabaseFactory.PROP_IMPORT_MAP_AGENTS_PARTS;
		else if (getFileContainsParticipants())
			label = DatabaseFactory.PROP_IMPORT_MAP_PARTICIPANTS;
		else
			label = DatabaseFactory.PROP_IMPORT_MAP_AGENTS;
		
		return label;
	}
	
	/**
	 * Saves the field mapping definitions to the properties table under the 
	 * specified profile.  There are currently 3 profiles - agent only, 
	 * participant only, and agent+participant
	 * 
	 * @param op
	 */
	public void saveToProperties(DatabaseOperation op) throws SQLException
	{
		// Save the field mappings
		String label1 = getMappingPropertiesName();
		String label2 = label1 + "2";
		if (label2.length() > 20)
			label2 = label1.substring(0, 19) + "2";
		
		StringBuffer data = new StringBuffer();
		for (int i=0; i < fieldList.size(); i++)
		{
			if (data.length() > 0)
				data.append(',');
			data.append((String)fieldList.get(i));
		}
		
		Hashtable props = new Hashtable();
		if (data.length() > 255)
		{
			props.put(label1, data.substring(0, 255));
			props.put(label2, data.substring(255));
		}
		else
		{
			props.put(label1, data.toString());
			props.put(label2, "");
		}
		
		// Save the vendor system ID mappings
		if (amsMap.size() > 0)
		{
			Iterator it = amsMap.keySet().iterator();
			while (it.hasNext())
			{
				String id = (String) it.next();
				String key = DatabaseFactory.PROP_IMPORT_MAP_VENDOR_ID + id;
				if (key.length() > 20)
					key = key.substring(0, 20);
				props.put(key, amsMap.get(id));
			}
		}
		
		// TEMP (?) - Remove old mappings from the properties table -- 02/26/07, kwm
		op.removeProperties(props);
		
		// Save the mappings to the properties table
		op.setProperties(props);
	}

	/**
	 * Loads the field mapping definitions from the properties table using the 
	 * specified profile.  There are currently 3 profiles - agent only, 
	 * participant only, and agent+participant
	 * 
	 * @param op
	 */	
	public void loadFromProperties(DatabaseOperation op) throws SQLException
	{
		// Load the field mappings
		String label1 = getMappingPropertiesName();
		String label2 = label1 + "2";
		if (label2.length() > 20)
			label2 = label1.substring(0, 19) + "2";
		
		String data = op.getPropertyValue(label1);
		if (data != null)
		{
			String data2 = op.getPropertyValue(label2);
			if (data2 != null)
				data += data2;
			
			StringTokenizer st = new StringTokenizer(data, ",");
			while (st.hasMoreTokens())
				fieldList.add(st.nextToken());
		}
		
		// Load the vendor system ID mappings
		amsMap = op.getVendorIdMappings();
	}
	
	
	/**
	 * @return
	 */
	public HashMap getAmsMap() {
		return amsMap;
	}

	/**
	 * @return
	 */
	public ArrayList getFieldList() {
		return fieldList;
	}

	/**
	 * @return
	 */
	public boolean getFileContainsAgents() {
		return fileContainsAgents;
	}

	/**
	 * @return
	 */
	public boolean getFileContainsParticipants() {
		return fileContainsParticipants;
	}

	/**
	 * @return
	 */
	public boolean getOverwriteExisting() {
		return overwriteExisting;
	}

	/**
	 * @return
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * @param map
	 */
	public void setAmsMap(HashMap map) {
		amsMap = map;
	}

	/**
	 * @param list
	 */
	public void setFieldList(ArrayList list) {
		fieldList = list;
	}

	/**
	 * @param b
	 */
	public void setFileContainsAgents(boolean b) {
		fileContainsAgents = b;
	}

	/**
	 * @param b
	 */
	public void setFileContainsParticipants(boolean b) {
		fileContainsParticipants = b;
	}

	/**
	 * @param b
	 */
	public void setOverwriteExisting(boolean b) {
		overwriteExisting = b;
	}

	/**
	 * @param string
	 */
	public void setServerUrl(String string) {
		serverUrl = string;
	}

// parsing functions
   public void characters(char[] ch, int start, int length) throws SAXException
   {
	   String tmp = new String(ch, start, length);
	   if (tmp == null)
		   tmp = "";
	   cdata += tmp;		
   }	

   public void endElement(String namespace, String simpleName, String qualifiedName) throws org.xml.sax.SAXException
   {
   		if (qualifiedName.equals("server"))
   		{
   			serverUrl = cdata;
   		}
		else if (qualifiedName.equals("agents"))
		{
			if ("true".equals(cdata))
				fileContainsAgents = true;
			else
				fileContainsAgents = false;
		}
		else if (qualifiedName.equals("participants"))
		{
			if ("true".equals(cdata))
				fileContainsParticipants = true;
			else
			fileContainsParticipants = false;
		}
		else if (qualifiedName.equals("overwrite"))
		{
			if ("true".equals(cdata))
				overwriteExisting = true;
			else
			overwriteExisting = false;
		}
		else if (qualifiedName.equals("field"))
		{
			fieldList.add(cdata);
		}
   }

   public void startElement(String namespace, String simpleName, String qualifiedName, Attributes attr) throws org.xml.sax.SAXException
   {
	   cdata = "";
	   
	   if (qualifiedName.equals("amsmapitem"))
	   {
			String name = attr.getValue("name");
			String value = attr.getValue("value");
			amsMap.put(name, value);
	   }
   }
}
