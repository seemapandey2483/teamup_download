package connective.teamup.download.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.Escape;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * Display bean for the various Customizable Emails viewing and editing pages.
 * 
 * @author kmccreary
 */
public class CustomTextEditorDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(CustomTextEditorDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private CustomTextFactory textFactory = null;
	private String textId = null;
	private String fromAddress = null;
	private String sentDate = null;
	private String toAddress = null;
	private String subject = null;
	private boolean textCustom = false;
	private boolean htmlAvailable = false;
	private boolean htmlCustom = false;


	/**
	 * Default constructor for CustomTextEditorDisplayBean.
	 */
	public CustomTextEditorDisplayBean()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the company info
			carrierInfo = serverInfo.getCarrierInfo();
			
			// Parse the text file name from the page and initialize the custom text factory
			textId = req.getParameter("text_file");
			String action = req.getParameter("action");
			boolean loadCustomText = true;
			if (action != null && action.equals("custom_view_default"))
				loadCustomText = false;
			textFactory = new CustomTextFactory(textId, CustomTextFactory.TYPE_EMAIL, 
												serverInfo, null, op, loadCustomText);
			textFactory.useDummyAgent(op, serverInfo, req);
			
			textCustom = textFactory.isCustomText();
			htmlCustom = textFactory.isCustomHtml();
			htmlAvailable = textFactory.isHtmlAvailable();
			
			SimpleDateFormat df = (SimpleDateFormat)SimpleDateFormat.getDateTimeInstance();
			df.applyPattern("E, MMMM d, yyyy h:mm a");
			sentDate = df.format(new Date(System.currentTimeMillis()));
			fromAddress = carrierInfo.getName();
			if (textFactory.isEmailToAgent())
				toAddress = textFactory.getAgentInfo().getContactName() + " [" + textFactory.getAgentInfo().getContactEmail() + "]";
			else if (textFactory.isEmailToCarrier())
				toAddress = carrierInfo.getReportsEmail();
			else
				toAddress = "";
			subject = textFactory.getEmailSubject();
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred loading custom email/text file info", e);
			throw new DisplayBeanException("Error occurred loading custom email/text file info", e);
		}
	}

	/**
	 * Returns the carrier info bean.
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	/**
	 * Returns true if custom text exists for this file.
	 */
	public boolean isTextCustom()
	{
		return textCustom;
	}

	/**
	 * Returns the email/text ID.
	 */
	public String getTextId()
	{
		return textId;
	}

	/**
	 * Returns true if an html version of this file is available.
	 */
	public boolean isHtmlAvailable()
	{
		return htmlAvailable;
	}

	/**
	 * Returns true if custom html exists for this file.
	 */
	public boolean isHtmlCustom()
	{
		return htmlCustom;
	}

//	/**
//	 * Returns the custom text factory.
//	 */
//	public CustomTextFactory getTextFactory()
//	{
//		return textFactory;
//	}

	/**
	 * Returns the email sender's name.
	 */
	public String getFrom()
	{
		return fromAddress;
	}

	/**
	 * Returns the default email sent date (display only).
	 */
	public String getSentDate()
	{
		return sentDate;
	}

	/**
	 * Returns the "send to" address.
	 */
	public String getTo()
	{
		return toAddress;
	}

	/**
	 * Returns the email subject line text.
	 */
	public String getSubject()
	{
		return subject;
	}

	public String getSubjectDefault()
	{
		return textFactory.getDefaultSubject();
	}

	/**
	 * Returns the formatted text version of the email body.
	 */
	public String getTextBody()
	{
		String temp = textFactory.getText();
		StringBuffer buf = new StringBuffer();
		for (int i=0; i < temp.length(); i++)
		{
			char c = temp.charAt(i);
			if (c == '\n')
				buf.append("&nbsp;<BR>\n");
			else
				buf.append(c);
		}
		return buf.toString();
	}

	/**
	 * Returns the formatted HTML version of the email body.
	 */
	public String getHtmlBody()
	{
		return textFactory.getHtml();
		
//		String temp = textFactory.getHtml();
//		StringBuffer buf = new StringBuffer();
//		for (int i=0; i < temp.length(); i++)
//		{
//			char c = temp.charAt(i);
//			if (c == '\n')
//				buf.append("&nbsp;<BR>\n");
//			else
//				buf.append(c);
//		}
//		return buf.toString();
	}

	/**
	 * Returns the escaped text version of the email body for inclusion in the JSP textarea control.
	 */
	public String getEditableText()
	{
		String temp = textFactory.getRawText();
		if (temp == null)
			temp = "";
		else
			temp = Escape.forHtml(temp);
		return temp;
	}

	/**
	 * Returns the escaped HTML version of the email body for inclusion in the JSP textarea control.
	 */
	public String getEditableHtml()
	{
		String temp = textFactory.getRawHtml();
		if (temp == null)
			temp = "";
		else
			temp = Escape.forHtml(temp);
		return temp;
	}

	/**
	 * Returns the escaped text version of the email body for inclusion in the JSP textarea control.
	 */
	public String getEditableSubject()
	{
		String temp = textFactory.getRawEmailSubject();
		if (temp == null)
			temp = "";
		else
			temp = Escape.forHtml(temp);
		return temp;
	}

	/**
	 * Returns the escaped text version of the email body for inclusion in the JSP textarea control.
	 */
	public String getEditableSubjectDefault()
	{
		String temp = textFactory.getDefaultSubject();
		if (temp == null)
			temp = "";
		else
			temp = Escape.forHtml(temp);
		return temp;
	}

	public String getFileDescription()
	{
		return textFactory.getFileDescription();
	}

	public int getAgentTagCount()
	{
		return textFactory.getAgentTagList().size();
	}

	public String getAgentTagId(int index)
	{
		if (index >= textFactory.getAgentTagList().size())
			return "";
		return (String) textFactory.getAgentTagList().get(index);
	}

	public String getAgentTagDesc(int index)
	{
		String tag = getAgentTagId(index);
		return textFactory.getTagDescription(tag);
	}

	public String getAgentTagCode(int index)
	{
		String tag = getAgentTagId(index);
		return escapeForQuoteStr(CustomTextFactory.getTag(tag));
	}

	public String getAgentTagSample(int index)
	{
		String tag = getAgentTagId(index);
		return escapeForQuoteStr(textFactory.getTagValue(tag, null));
	}

	public int getCompanyTagCount()
	{
		return textFactory.getCompanyTagList().size();
	}

	public String getCompanyTagId(int index)
	{
		if (index >= textFactory.getCompanyTagList().size())
			return "";
		return (String) textFactory.getCompanyTagList().get(index);
	}

	public String getCompanyTagDesc(int index)
	{
		String tag = getCompanyTagId(index);
		return textFactory.getTagDescription(tag);
	}

	public String getCompanyTagCode(int index)
	{
		String tag = getCompanyTagId(index);
		return escapeForQuoteStr(CustomTextFactory.getTag(tag));
	}

	public String getCompanyTagSample(int index)
	{
		String tag = getCompanyTagId(index);
		return escapeForQuoteStr(textFactory.getTagValue(tag, null));
	}

	private String escapeForQuoteStr(String text)
	{
		StringBuffer buf = new StringBuffer();
		if (text != null)
		{
			for (int i=0; i < text.length(); i++)
			{
				char c = text.charAt(i);
				if (c == '\\')
					buf.append("\\\\");
				else
					buf.append(c);
			}
		}
		return buf.toString();
	}

}
