package connective.teamup.al3;

import java.util.Hashtable;
import java.util.Vector;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.*;

/**
 * Insert the type's description here.
 * Creation date: (4/10/2002 1:55:33 PM)
 * @author: Mike Haney
 */
public class AcordFactory 
{
	// map of GroupDef objects keyed by group name
	Hashtable groupDefs = new Hashtable();

	/**
	 * AcordFactory constructor comment.
	 */
	public AcordFactory()
	{
		super();
	}
	
	/**
	 * Loads group definitions from a file.
	 * Creation date: (4/10/2002 4:20:35 PM)
	 */
	public void loadGroupDefinitions(InputStream instr) throws ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException
	{
		// parse the file definition xml
		DefFileHandler handler = new DefFileHandler(groupDefs);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(instr, handler);
	}
	
	/**
	 * create a new group from the named group template
	 */
	public AcordDataGroup createGroup(String name)
	{
		AcordDataGroup ret = null;
		GroupDef def = (GroupDef) groupDefs.get(name);
		if (def != null)
			ret = new AcordDataGroup(def);
		return ret;
	}
	
	public AcordDataGroup createGroup(String name, String level, int sequence)
	{
		AcordDataGroup ret = createGroup(name);
		ret.setLevel(level);
		ret.setSequence(sequence);
		
		return ret;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (4/10/2002 2:00:44 PM)
	 */
	public AcordDataGroup[] parseFile(InputStream instr) throws java.io.FileNotFoundException, java.io.IOException
	{
		// open the file
		InputStreamReader reader = new InputStreamReader(instr);
		
		// parse the file
		Vector transactions = new Vector();
		AcordDataGroup currentTrans = null;		// current transaction group
		AcordDataGroup currentMsgHeader = null;	// current message header group
		AcordDataGroup currentMsgFooter = null;
		
		String block = readGroupFromFile(reader);
		while (block != null)
		{
			// parse the group header
			GroupInfo gi = new GroupInfo(block);
	
			// build the group element
			AcordDataGroup group = null;
			GroupDef def = (GroupDef) groupDefs.get(gi.getName());
			if (def != null)
			{
				String groupName = def.getName();
				if (groupName.equalsIgnoreCase("6SOI") ||
					groupName.equalsIgnoreCase("6COL") ||
					groupName.equalsIgnoreCase("5DCV") ||
					groupName.equalsIgnoreCase("5CLD"))
				{
					group = new AcordCoverageGroup(gi, def, block);
				}
				else
				{
					group = new AcordDataGroup(gi, def, block);
				}
	
				if (group.getName().equalsIgnoreCase("1MHG"))
				{
					// check for message header group
		
					currentMsgHeader = group;
				}
				else if (group.getName().equalsIgnoreCase("3MTG"))
				{
					// check for message footer group
		
					currentMsgFooter = group;
				}
				else if (group.getName().equalsIgnoreCase("2TRG"))
				{
					// check for new transaction
	
					// add to the transaction list
					transactions.addElement(group);
	
					// set the current transaction
					currentTrans = group;
	
					// add the message header to the transaction attributes
					group.setAttribute(AcordDataGroup.MESSAGE_HEADER, currentMsgHeader);
				}
				else
				{
					// find the parent group
					AcordDataGroup parent = null;
//					AcordDataGroup parent = currentTrans.findGroupInTree(gi.getParentGroup(), gi.getParentLevel(),
//						gi.getParentSequence());
	
					// if a parent group is not found, treat the transaction as the implicit parent
//					if (parent == null)
						parent = currentTrans;
	
					// add the group to its parent
					parent.addSubGroup(group);
				}
			}
			else
			{
				// no group definition found - store the raw data in a generic group
				group = new AcordDataGroup(gi, block);

				// find the parent group
				AcordDataGroup parent = null;
//					AcordDataGroup parent = currentTrans.findGroupInTree(gi.getParentGroup(), gi.getParentLevel(),
//						gi.getParentSequence());

				// if a parent group is not found, treat the transaction as the implicit parent
//					if (parent == null)
					parent = currentTrans;

				// add the group to its parent
				parent.addSubGroup(group);
			}
			
			// read the next group
			block = readGroupFromFile(reader);
		}	
	
		reader.close();
		
		// create an array of transactions	
		AcordDataGroup[] ret = null;
//		if (transactions.size() > 0 && currentMsgFooter != null)
		if (transactions.size() > 0)
		{
			ret = new AcordDataGroup[transactions.size()];
			transactions.toArray(ret);

			if (currentMsgFooter != null)
			{
				// set the footer on each transaction
				for (int i=0; i < ret.length; i++)
				{
					ret[i].setAttribute(AcordDataGroup.MESSAGE_FOOTER, currentMsgFooter);
				}
			}
		}
					
		return ret;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/27/2001 1:45:24 PM)
	 */
	private String readGroupFromFile(Reader reader) throws java.io.IOException
	{
		String ret = null;
	
		// need to get rid of CR LF characters
		int firstChar = reader.read();
		while (firstChar == 10 || firstChar == 13)
			firstChar = reader.read();	
	
		if (firstChar == -1)
			return null;
				
		// skip the group name
		char[] groupName = new char[3];
		int numRead = reader.read(groupName);
		if (numRead < 3)
			return null;
		
		// read the block length
		char[] blockSize = new char[3];
		numRead = reader.read(blockSize);
		if (numRead < 3)
			return null;
		
		// convert block size from string
		int size = Integer.parseInt(String.valueOf(blockSize)) - 7;
		
		// read the block
		char[] block = new char[size];
		numRead = reader.read(block);
		if (numRead < size)
			return null;
			
		// build the string
		ret = String.valueOf((char)firstChar) + String.valueOf(groupName) + 
			String.valueOf(blockSize) + String.valueOf(block);
			
		return ret;
	}
}
