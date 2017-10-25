package connective.teamup.al3;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;


/**
 * Insert the type's description here.
 * Creation date: (9/27/2001 1:27:08 PM)
 * @author: Mike Haney
 */
public class AcordDataElement implements java.io.Serializable
{
	private String name = null;
	private String description = null;
	private String value = null;
	private int    length = 0;
	private AcordDataGroup group = null;
	private Hashtable attributes = new Hashtable();
/**
 * AcordDataElement constructor comment.
 */
public AcordDataElement(String name, String description, String value, int length, AcordDataGroup group) 
{
	super();


	this.name = name;
	this.description = description;
	this.value = value;
	this.group = group;
	this.length = length;
}


	/**
	 * Output the XML
	 */
	public void writeXMLToStream(OutputStream outstr) throws IOException
	{
		PrintWriter out = new PrintWriter(outstr, true);
		out.println("<element>");
		out.println("\t<name)" + name + "</name>");
		out.println("\t<value)" + value + "</value>");
		out.println("</element>");
	}


	/**
	 * Retrieves a named attribute about the data element.  This is just a generic method
	 * for passing additional meta data that the driver may provide.
	 *
	 * Creation date: (9/27/2001 1:27:08 PM)
	 */
	public Object getAttribute(String attrName) 
	{
		return attributes.get(attrName);
	}
	/**
	 * Returns the description of this data item.
	 * 
	 * Creation date: (9/27/2001 1:27:08 PM)
	 */
	public String getDescription()
	{
	    return description;
	}
	/**
	 * Retrieves a named attribute about the data element.  This is just a generic method
	 * for passing additional meta data that the driver may provide.
	 *
	 * Creation date: (9/27/2001 1:27:08 PM)
	 */
	public AcordDataGroup getGroup() 
	{
		return group;
	}
	/**
	 * Gets the length
	 * @return Returns a int
	 */
	public int getLength() {
		return length;
	}
	/**
	 * Returns the name of this data element.
	 *
	 * Creation date: (9/27/2001 1:27:08 PM)
	 */
	public String getName() 
	{
		return name;
	}
	/**
	 * Returns the value of this data element.
	 *
	 * Creation date: (9/27/2001 1:27:08 PM)
	 */
	public String getValue()
	{
		return value;
	}
	/**
	 * Returns the value of this data element.
	 *
	 * Creation date: (9/27/2001 1:27:08 PM)
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (4/10/2002 2:20:21 PM)
	 */
	public String toString()
	{
		String ret = name + "(" + description + ") = " + value;
		return ret;
	}
}
