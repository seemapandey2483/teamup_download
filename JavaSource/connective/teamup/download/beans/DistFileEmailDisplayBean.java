/*
 * 03/19/2005 - Created
 */
package connective.teamup.download.beans;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedEmailHelper;
import connective.teamup.download.db.DistributedFileInfo;

/**
 * @author Kyle McCreary
 *
 * Display bean used for displaying the email parameters for sending email
 * and file attachments to agent recipients.
 */
public class DistFileEmailDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(DistFileEmailDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	
	private String agyGroup = AgentGroupInfo.GROUP_ALL_LIVE;
	private String amsId = "";
	private String amsVersion = "";
	private String distBatchNumber = "";
	private String subject = "";
	private String body = "";
	private String sender = "";


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws DisplayBeanException
	{
		// Load the carrier info
		carrierInfo = serverInfo.getCarrierInfo();
		
		// Parse existing values from the request
		agyGroup = req.getParameter("agyGroup");
		if (agyGroup == null || agyGroup.equals(""))
			agyGroup = AgentGroupInfo.GROUP_ALL_LIVE;	// default to "All live agents"
		amsId = req.getParameter("amsId");
		amsVersion = req.getParameter("amsVersion");
		distBatchNumber = req.getParameter("distBatchNumber");
		if (distBatchNumber != null && !distBatchNumber.equals(""))
		{
			try
			{
				BatchInfo batchInfo = op.getBatchInfo(Integer.parseInt(distBatchNumber));
				DistributedFileInfo[] files = op.getDistributedFilesForBatch(batchInfo.getBatchNumber());
				if (files != null)
				{
					for (int i=0; i < files.length; i++)
					{
						if (files[i].getFilename().equals(DistributedEmailHelper.EMAIL_BODY))
						{
							// Parse the email subject, body, etc. from the file contents
							DistributedEmailHelper email = new DistributedEmailHelper(files[i]);
							subject = escapeForHtml(email.getSubject());
							body = escapeForHtml(email.getBody());
							sender = email.getSender();
							break;
						}
					}
				}
			}
			catch (Exception e) {
				LOGGER.error(e);
			}
		}
		
		if (sender == null || sender.equals(""))
			sender = carrierInfo.getContactEmail();
	}
	
	private String escapeForHtml(String data)
	{
		StringBuffer str = new StringBuffer("");
		for (int i=0; i < data.length(); i++)
		{
			if (data.charAt(i) == '\\')
				str.append("\\\\");
			else if (data.charAt(i) == '"')
				str.append("&quot;");
			else
				str.append(data.charAt(i));
		}
		
		return str.toString();
	}


	/**
	 * @return
	 */
	public String getAgyGroup()
	{
		if (agyGroup == null)
			return "";
		return agyGroup;
	}

	/**
	 * @return
	 */
	public String getAmsId()
	{
		if (amsId == null)
			return "";
		return amsId;
	}

	/**
	 * @return
	 */
	public String getAmsVersion()
	{
		if (amsVersion == null)
			return "";
		return amsVersion;
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
	public String getDistBatchNumber()
	{
		if (distBatchNumber == null)
			return "";
		return distBatchNumber;
	}

	/**
	 * @return
	 */
	public String getBody()
	{
		if (body == null)
			return "";
		return body;
	}

	/**
	 * @return
	 */
	public String getSender()
	{
		if (sender == null)
			return "";
		return sender;
	}

	/**
	 * @return
	 */
	public String getSubject()
	{
		if (subject == null)
			return "";
		return subject;
	}

}
