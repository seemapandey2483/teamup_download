/*
 * Created on Aug 17, 2010
 */
package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.LicenceService;

/**
 * Checks to see if ACORD XML import is allowed for this carrier.
 * 
 * @author kmccreary
 */
public class ImportXmlCheck implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(ImportXmlCheck.class);

	private static final String YES = "Y";
	private static final String NO = "N";

	/**
	 * Default constructor for ImportXmlCheck.
	 */
	public ImportXmlCheck() {
		super();
		
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp,
			ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException {
		
		String responseStr = NO;
	
		try {
			
			boolean isClaimActivated = LicenceService.isClaimActivated(op);
			if (isClaimActivated){
				String ClaimXmlImportAllowed = op.getPropertyValue(DatabaseFactory.PROP_CLAIM_XML_ALLOWED);
				if ("Y".equals(ClaimXmlImportAllowed))
					responseStr = YES;
			}
			resp.getWriter().print(responseStr);
			resp.getWriter().flush();
		}
		catch (Exception e) {
			LOGGER.error("Error checking for XML import", e);
			throw new ActionException("Error checking for XML import", e);
		}
		
		return null;
	}

}
