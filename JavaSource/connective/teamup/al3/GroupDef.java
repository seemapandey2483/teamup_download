package connective.teamup.al3;

import java.util.List;
import java.util.Vector;

/**
 * Insert the type's description here.
 * Creation date: (9/27/2001 4:06:50 PM)
 * @author: Mike Haney
 */
public class GroupDef implements Comparable
{
	private String name = null;
	private String description = null;
	private Vector fields = new Vector();
	/**
	 * GroupDef constructor comment.
	 */
	public GroupDef(String name, String description) 
	{
		super();
	
		this.name = name;
		this.description = description;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/27/2001 4:08:32 PM)
	 * @return jedi.datadriver.acord.al3.FieldDef[]
	 */
	public void addField(FieldDef field) 
	{
		fields.addElement(field);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/27/2001 4:08:32 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getDescription() {
		return description;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/27/2001 4:08:32 PM)
	 * @return jedi.datadriver.acord.al3.FieldDef[]
	 */
	public FieldDef getField(int index)
	{
		FieldDef ret = (FieldDef) fields.elementAt(index);
		return ret;
	}
	public List getFieldList()
	{
		return (List) fields;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/27/2001 4:08:32 PM)
	 * @return jedi.datadriver.acord.al3.FieldDef[]
	 */
	public int getFieldCount()
	{
		return fields.size();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/27/2001 4:08:32 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Object o)
	{
		return getName().compareTo(((GroupDef)o).getName());
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		GroupDef groupClone = new GroupDef(getName(), getDescription());
		
		for (int i=0; i < getFieldCount(); i++)
		{
			FieldDef field = getField(i);
			groupClone.addField((FieldDef) field.clone());
		}
		
		return groupClone;
	}

	/**
	 * Sets the group description.
	 * @param description The description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Sets the group name.
	 * @param name The name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

}
