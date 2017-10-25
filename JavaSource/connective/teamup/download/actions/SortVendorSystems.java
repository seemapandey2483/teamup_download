package connective.teamup.download.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Action bean to sort vendor systems alphabetically by name, saving the updated
 * sort sequences to the database.
 */
public class SortVendorSystems implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SortVendorSystems.class);
	/**
	 * Constructor for SortVendorSystems.
	 */
	public SortVendorSystems()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		sortByName(serverInfo, op);
		
		return "menu.ams";
	}
	
	/**
	 * Sorts all existing vendor systems alphabetically by name.  Vendor
	 * systems with a sort sequence greater than or equal to '999' are
	 * included at the bottom of the list.
	 * @param serverInfo The server info bean
	 */
	public void sortByName(ServerInfo serverInfo, DatabaseOperation op) throws ActionException
	{
		try
		{
			// Get the list of vendor systems
			AmsInfo amsInfos[] = op.getAmsInfoList();
			List amsList = new ArrayList();
			for (int i=0; i < amsInfos.length; i++)
				amsList.add(amsInfos[i]);
			
			// Sort the list alphabetically by name
			Collections.sort(amsList, ALPHA_ORDER);
			
			// Update the sort sequence for any records that have moved
			for (int i=0; i < amsList.size(); i++)
			{
				AmsInfo ams = (AmsInfo) amsList.get(i);
				if (ams.getSortSequence() < 999 && ams.getSortSequence() != i)
				{
					ams.setSortSequence(i);
					ams.save();
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error sorting agency vendor systems", e);
			throw new ActionException("Error sorting agency vendor systems", e);
		}
	}


	/**
	 * Comparator used for sorting the list of agency vendor systems.
	 */
	static final Comparator ALPHA_ORDER = new Comparator()
	{
		public int compare(Object o1, Object o2)
		{
			AmsInfo ams1 = (AmsInfo) o1;
			AmsInfo ams2 = (AmsInfo) o2;
			
			// Check for sort sequence greater or equal to "999"
			if (ams1.getSortSequence() >= 999 || ams2.getSortSequence() >= 999)
			{
				Integer seq1 = new Integer(ams1.getSortSequence());
				Integer seq2 = new Integer(ams2.getSortSequence());
				
				int cmp = seq1.compareTo(seq2);
				if (cmp != 0)
					return cmp;
			}

			// Compare system names
			return ams1.getDisplayName().toUpperCase().compareTo(ams2.getDisplayName().toUpperCase());
		}
	};

}
