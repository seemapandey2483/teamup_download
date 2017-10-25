package connective.teamup.download.beans;

import java.util.ArrayList;
import java.util.Hashtable;


public class States
{
	private Hashtable stateNames = null;
	private Hashtable stateAbbreviations = null;
	private ArrayList stateList = null;
	
	private static States _the_instance = null;
	
	
	/**
	 * Returns the singleton instance of the class.
	 */
	public static States instance()
	{
		if (_the_instance == null)
			_the_instance = new States();
		return _the_instance;
	}
	
	/**
	 * Protected default constructor for AL3Mapping.
	 */
	protected States()
	{
		// Load the state conversion hashtables
		stateNames = new Hashtable();
		stateAbbreviations = new Hashtable();
		stateList = new ArrayList();
		loadStateHashtables("AL", "Alabama");
		loadStateHashtables("AK", "Alaska");
		loadStateHashtables("AZ", "Arizona");
		loadStateHashtables("AR", "Arkansas");
		loadStateHashtables("CA", "California");
		loadStateHashtables("CO", "Colorado");
		loadStateHashtables("CT", "Connecticut");
		loadStateHashtables("DE", "Delaware");
		loadStateHashtables("FL", "Florida");
		loadStateHashtables("GA", "Georgia");
		loadStateHashtables("HI", "Hawaii");
		loadStateHashtables("ID", "Idaho");
		loadStateHashtables("IL", "Illinois");
		loadStateHashtables("IN", "Indiana");
		loadStateHashtables("IA", "Iowa");
		loadStateHashtables("KS", "Kansas");
		loadStateHashtables("KY", "Kentucky");
		loadStateHashtables("LA", "Louisiana");
		loadStateHashtables("ME", "Maine");
		loadStateHashtables("MD", "Maryland");
		loadStateHashtables("MA", "Massachusetts");
		loadStateHashtables("MI", "Michigan");
		loadStateHashtables("MN", "Minnesota");
		loadStateHashtables("MS", "Mississippi");
		loadStateHashtables("MO", "Missouri");
		loadStateHashtables("MT", "Montana");
		loadStateHashtables("NE", "Nebraska");
		loadStateHashtables("NV", "Nevada");
		loadStateHashtables("NH", "New Hampshire");
		loadStateHashtables("NJ", "New Jersey");
		loadStateHashtables("NM", "New Mexico");
		loadStateHashtables("NY", "New York");
		loadStateHashtables("NC", "North Carolina");
		loadStateHashtables("ND", "North Dakota");
		loadStateHashtables("OH", "Ohio");
		loadStateHashtables("OK", "Oklahoma");
		loadStateHashtables("OR", "Oregon");
		loadStateHashtables("PA", "Pennsylvania");
		loadStateHashtables("RI", "Rhode Island");
		loadStateHashtables("SC", "South Carolina");
		loadStateHashtables("SD", "South Dakota");
		loadStateHashtables("TN", "Tennessee");
		loadStateHashtables("TX", "Texas");
		loadStateHashtables("UT", "Utah");
		loadStateHashtables("VT", "Vermont");
		loadStateHashtables("VA", "Virginia");
		loadStateHashtables("WA", "Washington");
		loadStateHashtables("WV", "West Virginia");
		loadStateHashtables("WI", "Wisconsin");
		loadStateHashtables("WY", "Wyoming");
		loadStateHashtables("DC", "Washington D.C.");
	}
	
	private void loadStateHashtables(String abbrev, String name)
	{
		stateNames.put(abbrev, name);
		stateAbbreviations.put(name.toUpperCase(), abbrev);
		stateList.add(name);
	}


	/**
	 * Returns the state name for the specified state abbreviation.
	 * @param stateAbbrev
	 * @return
	 */
	public static String getStateName(String stateAbbrev)
	{
		if (stateAbbrev == null)
			return "";
		
		States mapper = States.instance();
		String state = (String) mapper.stateNames.get(stateAbbrev);
		if (state == null)
			state = null;
		return state;
	}

	/**
	 * Returns the state name for the specified state abbreviation.
	 * @param stateAbbrev
	 * @return
	 */
	public static String getStateAbbreviation(String stateName)
	{
		if (stateName == null)
			return "";
		
		States mapper = States.instance();
		String state = (String) mapper.stateAbbreviations.get(stateName.toUpperCase());
		if (state == null)
			state = null;
		return state;
	}

	public static ArrayList getStateList()
	{
		return States.instance().stateList;
	}

}
