/*
 * Created on Nov 22, 2005
 */
package connective.teamup.download.services;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * @author mccrearyk
 */
public class ReportsService
{
	public static final String CR_DOWNLOAD_LOG = "client_report_agytrans";
	public static final String CR_LAST_DL_DETAILS = "client_report_agylastdl";
	public static final String CR_DOWNLOAD_DETAILS = "client_report_agytranslog";
	public static final String CR_VENDOR_RUNTIME = "avs_runtime_notes";
	public static final String CR_VENDOR_SETUP = "avs_setup_notes";


	/**
	 * Constructor for ReportsService.
	 */
	public ReportsService()
	{
		super();
	}

	/**
	 * Returns the relative URL for the specified report.
	 */
	public String getReportUrl(String reportAction, String servletPath, String agentId, StringBuffer agentKey, Hashtable parameters)
	{
		if (reportAction == null || reportAction.equals(""))
			return "";
		
		String url = servletPath + "?action=client_report&reptName=" + reportAction;
		
		if (reportAction.equalsIgnoreCase(CR_DOWNLOAD_LOG))
		{
			// Log of download files for specified date range
			url += "&agyName=" + agentId + "&orderby=LOG_DT&startdt=_START_DATE_&enddt=_END_DATE_";
		}
		else if (reportAction.equalsIgnoreCase(CR_LAST_DL_DETAILS))
		{
			// File and transaction details for last file or batch of files downloaded by agent
			url += "&agentId=" + agentId;
		}
		else if (reportAction.equalsIgnoreCase(CR_DOWNLOAD_DETAILS))
		{
			// Download file and transaction details for specified date range
			url += "&agentId=" + agentId + "&startdt=_START_DATE_&enddt=_END_DATE_";
		}
		else if (reportAction.equalsIgnoreCase(CR_VENDOR_RUNTIME))
		{
			// Agent and vendor IDs for vendor system runtime notes
			url += "&agentId=" + agentId;
		}
		else if (reportAction.equalsIgnoreCase(CR_VENDOR_SETUP))
		{
			// Agent and vendor IDs for vendor system runtime notes
			url += "&agentId=" + agentId;
		}
		
		// Add any extra parameter name/value pairs passed by the calling function
		if (parameters != null)
		{
			Iterator it = parameters.keySet().iterator();
			while (it.hasNext())
			{
				String name = (String) it.next();
				String value = null;
				if (name != null)
					value = (String) parameters.get(name);
				if (name != null && value != null)
					url += "&" + name + "=" + value;
			}
		}
		
		
		// Security key is required for agent reports
		if (servletPath.equals("/agency"))
		{
/*
			if (agentKey == null)
			{
				DatabaseOperation op = null;
				try
				{
					op = DatabaseFactory.getInstance().startOperation();
					AgentInfo agentInfo = op.getAgentInfo(agentId);
					String keyName = "key";
					agentKey = new StringBuffer();
					agentKey.append(keyName);
					agentKey.append("=");
					if (agentInfo != null)
						agentKey.append(securityProvider.getSecurityKey(agentInfo.getAgentId(),agentInfo.getAgentId(), agentInfo.getPassword()));
				}
				catch (Exception e)
				{
//					e.printStackTrace(System.out);
					EmailService.getInstance().sendErrorNotification(e, "Agency Java Client App", agentId);
				}
				finally
				{
					if (op != null)
						op.close();
				}
			}
*/			
			if (agentKey != null)
				url += "&key=" + agentKey.toString();
		}
		
		return url;
	}

}
