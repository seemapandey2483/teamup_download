package connective.teamup.al3;

/**
 * A special instance of the AcordDataGroup to handle varying ACORD coverage records.
 * Creation date: (4/18/2002 3:15:35 PM)
 * @author: Kyle McCreary
 */
public class AcordCoverageGroup extends AcordDataGroup
{
	/**
	 * AcordCoverageGroup constructor comment.
	 */
	public AcordCoverageGroup(GroupInfo info, GroupDef def, String rawdata)
	{
	    super(info, def, rawdata);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (4/18/2002 3:20:46 PM)
	 * @return int
	 */
	public int getDeductible()
	{
		int limit = 0;
		
		if (getName().equalsIgnoreCase("5DCV"))
			limit = getElementNumericValue("5DCV04");
		else if (getName().equalsIgnoreCase("5CLD"))
			limit = getElementNumericValue("5CLD05");
		else if (getName().equalsIgnoreCase("6COL"))
			limit = getElementNumericValue("6COL11");
		
		return limit;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (4/18/2002 3:20:46 PM)
	 * @return int
	 */
	public int getLimit1()
	{
		int limit = 0;
		
		if (getName().equalsIgnoreCase("6SOI"))
			limit = getElementNumericValue("6SOI02");
		else if (getName().equalsIgnoreCase("5DCV"))
			limit = getElementNumericValue("5DCV02");
		else if (getName().equalsIgnoreCase("5CLD"))
			limit = getElementNumericValue("5CLD03");
		else if (getName().equalsIgnoreCase("6COL"))
			limit = getElementNumericValue("6COL24");
		
		return limit;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (4/18/2002 3:20:46 PM)
	 * @return int
	 */
	public int getLimit2()
	{
		int limit = 0;
		
		if (getName().equalsIgnoreCase("5DCV"))
			limit = getElementNumericValue("5DCV03");
		else if (getName().equalsIgnoreCase("5CLD"))
			limit = getElementNumericValue("5CLD04");
		else if (getName().equalsIgnoreCase("6COL"))
			limit = getElementNumericValue("6COL26");
		
		return limit;
	}
}
