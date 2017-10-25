package connective.teamup.al3;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Insert the type's description here.
 * Creation date: (9/27/2001 1:32:06 PM)
 * @author: Mike Haney
 */
public class AcordDataGroup implements java.io.Serializable
{
	private static final Logger LOGGER = Logger.getLogger(AcordDataGroup.class);
	// attribute constants
	public final static String MESSAGE_HEADER = "MESSAGE_HEADER";
	public final static String MESSAGE_FOOTER = "MESSAGE_FOOTER";
	private final static String CRLF = "\r\n";
	
	private String rawdata = null;
	private String name = null;
	private String description = null;
	private String level = null;
	private String header = null;
	private int length = 0;
	private int sequence = 0;
	private AcordDataGroup parent = null;

	private Vector elements = new Vector();
	private Vector subgroups = new Vector();
	
	private Hashtable attributes = new Hashtable();

	/**
	 * AcordDataGroup constructor comment.
	 */
	AcordDataGroup(GroupInfo info, GroupDef def, String rawdata)
	{
		super();
	
		this.rawdata = rawdata;
		this.name = def.getName();
		this.description = def.getDescription();
		this.level = info.getLevel();
		this.sequence = info.getSequence();
		this.length = rawdata.length();
		
		try
		{
			this.header = "";
			if (rawdata.charAt(0) >= '0' && rawdata.charAt(0) < '5')
				this.header = rawdata.substring(0, 10);
			else
				this.header = rawdata.substring(0, 30);
			
			this.length = Integer.parseInt(this.header.substring(4, 7));
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			System.out.println(this.name + ": " + e.getMessage());
		}
		
		// create all the data elements
		for (int i=0; i < def.getFieldCount(); i++)
		{
			FieldDef fd = def.getField(i);
			int start = fd.getStart() - 1;
			int end = start + fd.getLength();
			if (end < info.getData().length())
			{
				String value = info.getData().substring(start, end);
				AcordDataElement elem = new AcordDataElement(fd.getName(), fd.getDescription(), value, fd.getLength(), this);
				elements.addElement(elem);
			}
		}
	}

	/**
	 * AcordDataGroup constructor comment.
	 */
	AcordDataGroup(GroupInfo info, String rawdata)
	{
		super();

		this.rawdata = rawdata;	
		this.name = info.getName();
		this.description = "Undefined group";
		this.level = info.getLevel();
		this.sequence = info.getSequence();
		this.length = rawdata.length();
		
		try
		{
			this.header = "";
			if (rawdata.charAt(0) >= '0' && rawdata.charAt(0) < '5')
				this.header = rawdata.substring(0, 10);
			else
				this.header = rawdata.substring(0, 30);
			
			this.length = Integer.parseInt(this.header.substring(4, 7));
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			System.out.println(this.name + ": " + e.getMessage());
		}
	}

	/**
	 * AcordDataGroup constructor comment.
	 */
	AcordDataGroup(GroupDef def)
	{
		super();
	
		this.name = def.getName();
		this.description = def.getDescription();
//		this.level = info.getLevel();
//		this.sequence = info.getSequence();
		this.header = "";
		
		// create all the data elements
		for (int i=0; i < def.getFieldCount(); i++)
		{
			FieldDef fd = def.getField(i);
			AcordDataElement elem = new AcordDataElement(fd.getName(), fd.getDescription(), "", fd.getLength(), this);
			elements.addElement(elem);
		}
	}

	/**
	 * Output the XML
	 */
	public void writeXMLToStream(OutputStream outstr) throws IOException
	{
		PrintWriter out = new PrintWriter(outstr, true);
		out.println("<group>");
		out.println("\t<name)" + name + "</name>");
		out.println("\t<level)" + level + "</level>");
		out.println("\t<sequence)" + sequence + "</sequence>");
		out.println("<elements>");
		for (int i=0; i < elements.size(); i++)
		{
			AcordDataElement element = (AcordDataElement) elements.elementAt(i);
			element.writeXMLToStream(outstr);
		}
		out.println("</elements>");
		out.println("<subgroups>");
		for (int i=0; i < subgroups.size(); i++)
		{
			AcordDataGroup group = (AcordDataGroup) subgroups.elementAt(i);
			group.writeXMLToStream(outstr);
		}
		out.println("</subgroups>");
		out.println("</group>");
	}
	
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public void addSubGroup(AcordDataGroup subgroup) 
	{
		subgroup.setParentGroup(this);
		subgroups.addElement(subgroup);
	}
	
	public boolean equals(Object obj)
	{
		boolean ret = false;
		AcordDataGroup objGroup = (AcordDataGroup) obj;
	
		if (name.equalsIgnoreCase(objGroup.getName()) && sequence == objGroup.getSequence())
		{
			// check the level argument
			if (level != null)
			{
				ret = level.equalsIgnoreCase(objGroup.getLevel());
			}
			else
			{
				ret = (objGroup.getLevel() == null);
			}
		}
				
		return ret;
	}
	/**
	 * Find the group in the tree - called RECURSIVELY
	 *
	 * Returns a Vector of AcordDataGroup objects containing all groups with the given name
	 *
	 * Creation date: (10/9/2001 9:30:15 AM)
	 */
	public Vector findGroupInTree(String name)
	{
		Vector results = new Vector();
	
		// if we have incomplete group identifiers, then return an empty list
		if (name == null)
			return results;
			
		// start by checking this group
		if (name.equalsIgnoreCase(this.name))
		{
			results.addElement(this);
		}
	
		// check each of the children
		for (int i=0; i < subgroups.size(); i++)
		{
			AcordDataGroup subgroup = (AcordDataGroup) subgroups.elementAt(i);
			Vector subresults = subgroup.findGroupInTree(name);
			results.addAll(subresults);
		}	
			
		return results;
	}
	/**
	 * Find the group in the tree - called RECURSIVELY
	 *
	 * Returns a Vector of AcordDataGroup objects containing all groups with the given name and level
	 *
	 * Creation date: (10/9/2001 9:30:15 AM)
	 */
	public Vector findGroupInTree(String name, String level)
	{
		Vector results = new Vector();
	
		// if we have incomplete group identifiers, then return null
		if (name == null || level == null)
			return results;
	
		// start by checking this group
		if (name.equalsIgnoreCase(this.name) && level.equalsIgnoreCase(this.level))
		{
			results.addElement(this);
		}
	
		// check each of the children
		for (int i=0; i < subgroups.size(); i++)
		{
			AcordDataGroup subgroup = (AcordDataGroup) subgroups.elementAt(i);
			Vector subresults = subgroup.findGroupInTree(name, level);
			results.addAll(subresults);
		}	
			
		return results;
	}
	/**
	 * Find the group in the tree - called RECURSIVELY
	 *
	 * Returns a single instance of a group that is uniquely identified by the name, level and sequence
	 *
	 * Creation date: (10/9/2001 9:30:15 AM)
	 */
	public AcordDataGroup findGroupInTree(String name, String level, int sequence)
	{
		// if we have incomplete group identifiers, then return null
		if (name == null || level == null)
			return null;
	
		// start by checking this group
		if (name.equalsIgnoreCase(this.name) && level.equalsIgnoreCase(this.level) && sequence == this.sequence)
			return this;
	
		// check each of the children
		for (int i=0; i < subgroups.size(); i++)
		{
			AcordDataGroup subgroup = (AcordDataGroup) subgroups.elementAt(i);
			AcordDataGroup subret = subgroup.findGroupInTree(name, level, sequence);
			if (subret != null)
				return subret;
		}	
			
		return null;
	}
	/**
	 * Retrieves a named attribute about the data element.  This is just a generic method
	 * for passing additional meta data that the driver may provide.
	 *
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public Object getAttribute(String attrName) 
	{
		return attributes.get(attrName);
	}
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public String getDescription() 
	{
		return description;
	}
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public AcordDataElement getElement(int index) 
	{
		return (AcordDataElement) elements.elementAt(index);
	}
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public int getElementCount() 
	{
		return elements.size();
	}
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public int getElementNumericValue(String name) 
	{
		int ret = 0;
	
		try
		{
			String strValue = getElementValue(name).trim();
			if (strValue != null && strValue.length() > 0)
				ret = Integer.parseInt(strValue);
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			ret = 0;
		}
			
		return ret;
	}
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public String getElementValue(String name) 
	{
		String ret = "";
	
		for (int i=0; i < elements.size(); i++)
		{
			AcordDataElement element = (AcordDataElement) elements.elementAt(i);
			if (element.getName().equalsIgnoreCase(name))
			{
				ret = element.getValue();
	
				if (ret == null)
					ret = "";
					
				break;
			}
		}
			
		return ret;
	}
	/**
	 * Returns the AL3 record header of this data item.
	 * @return String
	 */
	public String getHeader()
	{
		return header;
	}
	/**
	 * Returns the original record length of this data element (as taken from
	 * the AL3 record header).
	 * @return int
	 */
	public int getLength()
	{
		return length;
	}
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public String getLevel() 
	{
		return level;
	}
	/**
	 * Returns the name of this data element.
	 *
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * Returns the full record as a string.
	 * @return String
	 */
	public String getRawData()
	{
		return rawdata;
	}
	
	/**
	 * Regenerates the raw data for this record/element group.
	 */
	public void rebuildRawData()
	{
		StringBuffer data = new StringBuffer(header);
		AcordDataElement element = null;
		for (int i=0; i < elements.size(); i++)
		{
			element = (AcordDataElement) elements.elementAt(i);
			data.append(element.getValue());
		}
		
		rawdata = padd(data.toString(), length);
	}
	
	
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public AcordDataGroup getParentGroup() 
	{
		return parent;
	}
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public int getSequence() 
	{
		return sequence;
	}
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public AcordDataGroup getSubgroup(int index) 
	{
		return (AcordDataGroup) subgroups.elementAt(index);
	}
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public int getSubgroupCount() 
	{
		return subgroups.size();
	}
	/**
	 * Retrieves a named attribute about the data element.  This is just a generic method
	 * for passing additional meta data that the driver may provide.
	 *
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public void setAttribute(String attrName, Object value) 
	{
		attributes.put(attrName, value);	
	}
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	public void setElementValue(String name, String value) 
	{
		for (int i=0; i < elements.size(); i++)
		{
			AcordDataElement element = (AcordDataElement) elements.elementAt(i);
			if (element.getName().equalsIgnoreCase(name))
			{
				element.setValue(value);
				break;
			}
		}
	}
	
	public void setLevel(String newLevel)
	{
		if (newLevel != null)
			level = newLevel;
	}
	
	public void setSequence(int newSequence)
	{
		sequence = newSequence;
	}
	
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:32:06 PM)
	 */
	void setParentGroup(AcordDataGroup parent) 
	{
		this.parent = parent;
	}
	public String toString()
	{
		StringBuffer ret = new StringBuffer("Group ");
		ret.append(name);
		ret.append(" ");
		ret.append(level);
		ret.append(" ");
		ret.append(sequence);
		ret.append(" (");
		ret.append(description);
		ret.append(")\n");
		ret.append("Elements:\n---------\n");
		for (int i=0; i < elements.size(); i++)
		{
			ret.append(elements.elementAt(i).toString());
			ret.append("\n");
		}
		ret.append("Subgroups:\n----------\n");
		for (int i=0; i < subgroups.size(); i++)
		{
			ret.append(subgroups.elementAt(i).toString());
		}
		
		return ret.toString();
	}
	
	/**
	 * Write the complete AL3 transaction to a file / output stream
	 * 
	 * @param outstream Output stream to write the file into
	 * 
	 * Creation date: (9/19/2002 10:00 AM)
	 */
	public void writeFile(OutputStream outstr) throws IOException
	{
		// Write the message header (1MHG)
		AcordDataGroup mhg = (AcordDataGroup) getAttribute(MESSAGE_HEADER);
		if (mhg != null)
			mhg.writeGroup(outstr);
		
		// Write this transaction
		writeGroup(outstr);
		
		// If it exists, write the message trailer (3MTG)
		Vector v = findGroupInTree("3MTG");
		if (v.size() > 0)
		{
			AcordDataGroup mtg = (AcordDataGroup) v.get(0);
			mtg.writeGroup(outstr);
		}
	}
	
	/**
	 * Write this AcordDataGroup and all of its subgroups in ACORD AL3 flat text format
	 * 
	 * @param outstream Output stream to write the file into
	 * 
	 * Creation date: (9/19/2002 10:16 AM)
	 */
	protected void writeGroup(OutputStream outstr) throws IOException
	{
		// Build the AL3 data record from the list of elements
		StringBuffer record = new StringBuffer("");
		for (int i=0; i < getElementCount(); i++)
		{
			AcordDataElement field = getElement(i);
			record.append(padd(field.getValue(), field.getLength()));
		}
		record.append(CRLF);
		
		
		// Build the AL3 record header
		int length = record.length();
		boolean shortHeader = (Character.isDigit(getName().charAt(0)) && getName().charAt(0) < '5');
		if (shortHeader)
			length += 10;
		else
			length += 30;
		
		StringBuffer header = new StringBuffer(padd(getName(), 4));
		header.append(paddzero(length, 3));
		header.append("   ");
		if (!shortHeader)
		{
			if (sequence < 1)
				sequence = 1;
			
			header.append(padd(level, 2));
			header.append(paddzero(sequence, 4));
			
			AcordDataGroup parent = getParentGroup();
			if (parent != null)
			{
				header.append(padd(parent.getName(), 4));
				header.append(padd(parent.getLevel(), 2));
				header.append(paddzero(parent.getSequence(), 4));
			}
			
			while (header.length() < 30)
				header.append(' ');
		}

		
		// Write the AL3 data record to the output stream
		outstr.write(header.toString().getBytes());
		outstr.write(record.toString().getBytes());
		
		
		// Loop through the set of subgroups and write each to the output stream
		for (int i=0; i < getSubgroupCount(); i++)
		{
			AcordDataGroup child = getSubgroup(i);
			
			// Do not write the message trailer (3MTG) as a child. This group is
			// handled separately in order to assure that it is the last record
			// written to the file.
			if (!child.getName().equalsIgnoreCase("3MTG"))
				child.writeGroup(outstr);
		}
	}
	
	private String padd(String data, int length)
	{
		StringBuffer paddedStr = null;
		
		if (data == null)
			paddedStr = new StringBuffer("");
		else if (data.length() > length)
			paddedStr = new StringBuffer(data.substring(0, length));
		else
			paddedStr = new StringBuffer(data);
		
		while (paddedStr.length() < length)
			paddedStr.append(' ');
		
		return paddedStr.toString();
	}
	
	private String paddzero(String data, int length)
	{
		StringBuffer paddedStr = null;
		
		if (data == null)
			paddedStr = new StringBuffer("");
		else
			paddedStr = new StringBuffer(data.trim());
		
		if (paddedStr.length() > length)
			paddedStr = new StringBuffer(paddedStr.substring(0, length));
		
		while (paddedStr.length() < length)
			paddedStr.insert(0, '0');
		
		return paddedStr.toString();
	}
	
	private String paddzero(int data, int length)
	{
		return paddzero(String.valueOf(data), length);
	}
}
