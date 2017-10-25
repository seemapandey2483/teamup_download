package connective.teamup.download.beans;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.Escape;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 */
public class CarGroupListDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(CarGroupListDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private AgentGroupInfo[] groups = null;


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the carrier info
			this.carrierInfo = serverInfo.getCarrierInfo();
			
			// Load the rollout group list
			groups = op.getAllAgentGroups(AgentGroupInfo.TYPE_ROLLOUT, AgentGroupInfo.TYPE_MIGRATION);
			
			// Clear any stored agent lists
			if (req.getSession().getAttribute(ServerInfo.STORE_AGENT_LIST) != null)
				req.getSession().removeAttribute(ServerInfo.STORE_AGENT_LIST);
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred retrieving agency rollout groups", e);
			throw new DisplayBeanException("Error occurred retrieving agency rollout groups", e);
		}
	}

	/**
	 * @return
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	/**
	 * @return
	 */
	public AgentGroupInfo getGroup(int index)
	{
		if (groups == null || index >= groups.length)
			return null;
		return groups[index];
	}

	public int getGroupCount()
	{
		if (groups == null)
			return 0;
		return groups.length;
	}

	public String getGroupType(int index)
	{
		String type = getGroup(index).getType();
		if (type == null)
			type = "";
		else if (type.equals(AgentGroupInfo.TYPE_ROLLOUT))
			type = "Rollout";
		else if (type.equals(AgentGroupInfo.TYPE_MIGRATION))
			type = "Migration";
		
		return type;
	}

	public String escapeForHtml(String str)
	{
		return Escape.forHtml(str);
	}

	public String escapeForFunction(String str)
	{
		StringBuffer buf = new StringBuffer("");
		for (int i=0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			
			if (c == '\'')
				buf.append("\\'");
			else if (c == '"')
				buf.append("\\\"");
			else if (c == '\\')
				buf.append("\\\\");
			else
				buf.append(c);
		}
		
		return buf.toString();
	}

}
