/*
 * 03/11/2005 - Created
 */
package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedEmailHelper;
import connective.teamup.download.db.DistributedFileInfo;

/**
 * @author Kyle McCreary
 *
 * Display bean used for displaying the list of files currently uploaded to be
 * included in a batch of distributed files (Applied edits or email).
 */
public class DistFileListDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(DistFileListDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private ArrayList attachments = new ArrayList();
	
	private String distBatchNumber = "";
	private String actionPrefix = "";
	private String notify = "Y";


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws DisplayBeanException
	{
		try
		{
			// Load the carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			
			// Parse the action prefix and distribution parameters from the request
			String action = null;
			if (items == null)
			{
				action = req.getParameter("action");
				distBatchNumber = req.getParameter("distBatchNumber");
				notify = req.getParameter("notify");
			}
			else
			{
				for (int i=0; i < items.length; i++)
				{
					if (items[i].isFormField())
					{
						if (items[i].getFieldName() == null)
						{
							// do nothing
						}
						else if (items[i].getFieldName().equals("action"))
							action = items[i].getString();
						else if (items[i].getFieldName().equals("distBatchNumber"))
							distBatchNumber = items[i].getString();
						else if (items[i].getFieldName().equals("notify"))
							notify = items[i].getString();
					}
				}
			}
			
			if (action != null && action.length() > 10)
				actionPrefix = action.substring(0, 10);

			if (distBatchNumber != null && !distBatchNumber.equals(""))
			{
				BatchInfo batchInfo = op.getBatchInfo(Integer.parseInt(distBatchNumber));
				DistributedFileInfo[] files = op.getDistributedFilesForBatch(batchInfo.getBatchNumber());
				if (files != null && files.length > 0)
				{
					for (int i=0; i < files.length; i++)
					{
						String filename = files[i].getFilename();
						if (i == 0 && filename.equals(DistributedEmailHelper.EMAIL_BODY))
						{
							// Ignore email body
						}
						else
							attachments.add(filename);
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			e.printStackTrace(System.out);
		}
	}

	/**
	 * @return
	 */
	public String getActionPrefix()
	{
		if (actionPrefix == null)
			return "";
		return actionPrefix;
	}

	public String getAttachment(int index)
	{
		if (attachments == null || index >= attachments.size())
			return "";
		return (String) attachments.get(index);
	}

	public int getAttachmentCount()
	{
		if (attachments == null)
			return 0;
		return attachments.size();
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
	public String getNotify()
	{
		if (notify == null)
			return "";
		return notify;
	}

}
