package connective.teamup.al3;

/**
 * Insert the type's description here.
 * Creation date: (10/5/2001 9:49:52 AM)
 * @author: Mike Haney
 */
public class GroupInfo 
{
	private String name = null;
	private int numBytes = 0;
	private String level = "";
	private int sequence = 0;
	private String parentGroup = null;
	private String parentLevel = "";
	private int parentSequence = 0;
	private String data = null;
/**
 * GroupHeader constructor comment.
 */
public GroupInfo(String record)
{
	super();

	String tmp;
	
	// parse the incoming record
	name = record.substring(0, 4);
	numBytes = Integer.parseInt(record.substring(4, 7));
	data = record;
	
	// determine header type
	boolean shortHeader = false;
	tmp = name.substring(0, 1);
	if (Character.isDigit(tmp.charAt(0)))
	{
		int digit = Integer.parseInt(tmp);
		if (digit < 5)
			shortHeader = true;
	}
	
	if (!shortHeader)
	{
		tmp = record.substring(10, 12);
		if (!tmp.equalsIgnoreCase("  "))
			level = tmp;
			
		tmp	= record.substring(12, 16);
		if (!tmp.equalsIgnoreCase("    "))
		{
			try
			{
				sequence = Integer.parseInt(tmp);
			}
			catch (Exception e)
			{
				sequence = 0;
			}
		}
		
		tmp = record.substring(16, 20);
		if (!tmp.equalsIgnoreCase("    "))
			parentGroup = tmp;
			
		tmp = record.substring(20, 22);
		if (!tmp.equalsIgnoreCase("    "))
			parentLevel = tmp;
		
		tmp = record.substring(22, 26);
		if (!tmp.equalsIgnoreCase("    "))
		{
			try
			{
				parentSequence = Integer.parseInt(tmp);
			}
			catch (Exception e)
			{
				parentSequence = 0;
			}
		}
			
	}
}
/**
 * Insert the method's description here.
 * Creation date: (10/5/2001 9:53:42 AM)
 * @return java.lang.String
 */
public java.lang.String getData() {
	return data;
}
/**
 * Insert the method's description here.
 * Creation date: (10/5/2001 9:53:42 AM)
 * @return java.lang.String
 */
public java.lang.String getLevel() {
	return level;
}
/**
 * Insert the method's description here.
 * Creation date: (10/5/2001 9:53:42 AM)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * Insert the method's description here.
 * Creation date: (10/5/2001 9:53:42 AM)
 * @return int
 */
public int getNumBytes() {
	return numBytes;
}
/**
 * Insert the method's description here.
 * Creation date: (10/5/2001 9:53:42 AM)
 * @return java.lang.String
 */
public java.lang.String getParentGroup() {
	return parentGroup;
}
/**
 * Insert the method's description here.
 * Creation date: (10/5/2001 9:53:42 AM)
 * @return java.lang.String
 */
public java.lang.String getParentLevel() {
	return parentLevel;
}
/**
 * Insert the method's description here.
 * Creation date: (10/5/2001 9:53:42 AM)
 * @return int
 */
public int getParentSequence() {
	return parentSequence;
}
/**
 * Insert the method's description here.
 * Creation date: (10/5/2001 9:53:42 AM)
 * @return int
 */
public int getSequence() {
	return sequence;
}
}
