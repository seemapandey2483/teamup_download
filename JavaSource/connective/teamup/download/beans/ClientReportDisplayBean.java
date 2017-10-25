/*
 * Created on Sep 15, 2005
 */
package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * Display bean used for the generic client report forwarder.  Retrieves all known request parameters
 * and dynamically builds the report forwarder page.
 * 
 * @author Kyle McCreary
 */
public class ClientReportDisplayBean implements Serializable, DisplayBean
{
	String action = null;
	ArrayList paramName = null;
	ArrayList paramValue = null;


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		// Parse the report action from the request
		action = req.getParameter("reptName");
		
		// Check all known report parameters for inclusion in the parameter list
		checkParameter(req, "agentId");
		checkParameter(req, "agyName");
		checkParameter(req, "amsid");
		checkParameter(req, "enddt");
		checkParameter(req, "orderby");
		checkParameter(req, "startdt");
	}

	private void checkParameter(HttpServletRequest req, String name)
	{
		// Check to see if the parameter exists; if so, add it to the list
		String value = req.getParameter(name);
		if (value != null)
		{
			if (paramName == null)
			{
				paramName = new ArrayList();
				paramValue = new ArrayList();
			}
			
			paramName.add(name);
			paramValue.add(value);
		}
	}

	public String getAction()
	{
		return action;
	}

	/**
	 * Returns the number of parameters in the report.
	 * @return int
	 */
	public int getParamCount()
	{
		if (paramName == null)
			return 0;
		return paramName.size();
	}

	/**
	 * Returns the specified parameter name.
	 * @return String
	 */
	public String getParamName(int index)
	{
		if (index >= getParamCount())
			return "";
		return (String) paramName.get(index);
	}

	/**
	 * Returns the specified parameter value.
	 * @return String
	 */
	public String getParamValue(int index)
	{
		if (paramValue == null || index >= paramValue.size())
			return "";
		return (String) paramValue.get(index);
	}

}
