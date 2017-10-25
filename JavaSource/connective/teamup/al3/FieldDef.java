package connective.teamup.al3;

/**
 * Insert the type's description here.
 * Creation date: (9/27/2001 4:04:31 PM)
 * @author: Mike Haney
 */
public class FieldDef 
{
	private String name = "";
	private String refName = "";
	private String description = "";
	private String longDesc = "";
	private int start = 0;
	private int length = 0;
	private String AL3Class = "";
	private String type = "";
	
	
	/**
	 * FieldDef constructor comment.
	 */
	public FieldDef() 
	{
		super();
	}
	
	public FieldDef(String name, String description, int start, int length) 
	{
		super();
	
		this.name = name;
		this.description = description;
		this.start = start;
		this.length = length;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 * @return java.lang.String
	 */
	public String getAL3Class()
	{
		return AL3Class;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (9/27/2001 4:05:31 PM)
	 * @return java.lang.String
	 */
	public String getDescription()
	{
		return description;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (9/27/2001 4:05:31 PM)
	 * @return int
	 */
	public int getLength()
	{
		return length;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 * @return java.lang.String
	 */
	public String getLongDescription()
	{
		return longDesc;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (9/27/2001 4:05:31 PM)
	 * @return java.lang.String
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 * @return java.lang.String
	 */
	public String getRefName()
	{
		return refName;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (9/27/2001 4:05:31 PM)
	 * @return int
	 */
	public int getStart()
	{
		return start;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 * @return java.lang.String
	 */
	public String getType()
	{
		return type;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 */
	public void setName(String newName)
	{
		this.name = newName;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 */
	public void setRefName(String newRefName)
	{
		this.refName = newRefName;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 */
	public void setDescription(String newDescription)
	{
		this.description = newDescription;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 */
	public void setLongDesc(String newDescription)
	{
		this.longDesc = newDescription;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 */
	public void setStart(String newStart)
	{
		this.start = Integer.parseInt(newStart);
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 */
	public void setStart(int newStart)
	{
		this.start = newStart;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 */
	public void setLength(String newLength)
	{
		this.length = Integer.parseInt(newLength);
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 */
	public void setLength(int newLength)
	{
		this.length = newLength;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 */
	public void setAL3Class(String newClass)
	{
		this.AL3Class = newClass;
	}
	
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/31/2002 1:02:31 PM)
	 */
	public void setType(String newType)
	{
		this.type = newType;
	}
	
	
	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		FieldDef fieldClone = new FieldDef();
		fieldClone.setAL3Class(getAL3Class());
		fieldClone.setDescription(getDescription());
		fieldClone.setLength(getLength());
		fieldClone.setLongDesc(getLongDescription());
		fieldClone.setName(getName());
		fieldClone.setRefName(getRefName());
		fieldClone.setStart(getStart());
		fieldClone.setType(getType());
		
		return fieldClone;
	}
}
