package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AmsClaimInfo;
import connective.teamup.download.db.DatabaseOperation;

public class AmsClaimListDisplayBean implements Serializable, DisplayBean {
	
	private static final Logger LOGGER = Logger.getLogger(AmsClaimListDisplayBean.class);
	
	Vector systems = new Vector();
	
	CarrierInfo carrierInfo = null;
	

	/**
	 * Constructor for AmsListDisplayBean.
	 */
	public AmsClaimListDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		this.carrierInfo = serverInfo.getCarrierInfo();
		
		try
		{
			// Build the list of agency vendor systems
			AmsClaimInfo[] amsInfos = op.getAmsClaimInfoList();
			for (int i=0; i < amsInfos.length; i++)
				systems.add(amsInfos[i]);
			
			if (!carrierInfo.isVendorListInitialized() && amsInfos.length > 1)
				carrierInfo.setVendorListInitialized(true);
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred loading agency vendor system table", e);
			throw new DisplayBeanException("Error occurred loading agency vendor system table", e);
		}
	}
	
	/**
	 * Returns the specified agency vendor system info bean.
	 * @param index The index of the system to get
	 * @return AmsInfo
	 */
	public AmsClaimInfo getAms(int index)
	{
		if (index >= systems.size())
			return null;
		else
			return (AmsClaimInfo) systems.elementAt(index);
	}
	
	/**
	 * Returns the number of available agency vendor systems.
	 * @return int
	 */
	public int getAmsCount()
	{
		return systems.size();
	}

	/**
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}
	
	/**
	 * Returns the specified string escaped for HTML.
	 * @param text The text string to convert
	 * @return String
	 */
	public String escapeForHtml(String text)
	{
		StringBuffer str = new StringBuffer("");
		
		if (text != null)
		{
			for (int i=0; i < text.length(); i++)
			{
				char c = text.charAt(i);
				if (c == '\\')
					str.append("\\\\");
				else if (c == '"')
					str.append("&quot;");
				else
					str.append(c);
			}
		}
		return str.toString();
	}

}
