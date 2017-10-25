package connective.teamup.download;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.log4j.Logger;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ResourceServlet extends HttpServlet
{
	private static final Logger LOGGER = Logger.getLogger(ResourceServlet.class);
	
	protected byte[] bannerGraphic = null;
						
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		try
		{
			// load the banner graphic
			DatabaseOperation op = null;
			try
			{
				op = DatabaseFactory.getInstance().startOperation();
				bannerGraphic = op.getResourceBytes(DatabaseFactory.RES_CARRIER_LOGO_FILENAME);
				if (bannerGraphic == null)
					bannerGraphic = new byte[0];
			}
			finally
			{
				if (op != null)
					op.close();
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new ServletException(e);
		}
	}
		
	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		doPost(req, resp);
	}
	
	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		try
		{
			String id = req.getParameter("id");
			if (id != null)
			{
				if (id.equals("banner"))
				{
					writeBanner(req, resp);
				}
				else if (id.equals("banner_config"))
				{
					writeBannerConfigOutput(req, resp);	
				}
				else if (id.equals("template_config"))
				{
					writeEMailConfigOutput(req, resp);	
				}
			}
			else
			{
				if (FileUpload.isMultipartContent(req))
				{
					DiskFileUpload upload = new DiskFileUpload();
					upload.setSizeThreshold(500000);
					upload.setSizeMax(500000);
					List /* FileItem */ items = upload.parseRequest(req);
					Iterator iter = items.iterator();
					String action = null;
					String template = null;
					InputStream is = null;
					byte[] fileContents = null;
					long size = 0;
					while (iter.hasNext()) 
					{
						FileItem item = (FileItem) iter.next();
						if (item.isFormField())
						{
							if (item.getFieldName() != null)
							{
								if (item.getFieldName().equals("id"))
									action = item.getString();
								else if (item.getFieldName().equals("template"))
									template = item.getString();
							}
						}
						else
						{
							is = item.getInputStream();
							fileContents = item.get();
							size = item.getSize();
						}
					}
						
					if (action != null)
					{
						DatabaseOperation op = null;
						try
						{
							if (action.equals("banner_upload"))
							{
//								String imageHeight = null;
								op = DatabaseFactory.getInstance().startOperation();
/*								try
								{
									if (fileContents != null && fileContents.length > 0)
									{
										javax.swing.ImageIcon image = 
											new javax.swing.ImageIcon(fileContents);
										if (image.getIconHeight() > 0)
										{
											imageHeight = String.valueOf(image.getIconHeight());
											Hashtable props = new Hashtable();
											props.put(DatabaseFactory.PROP_BANNER_GRAPHIC_HEIGHT, imageHeight);
											op.setProperties(props);
											CarrierInfo.getInstance().setBannerGraphicHeight(image.getIconHeight());
										}
									}
								}
								catch (Exception e)
								{
									// ImageIcon class is not available for non-Windows
									// servers; ignore the error and continue
									
									//System.out.println(e.getMessage());
								}
*/
								op.saveResource(DatabaseFactory.RES_CARRIER_LOGO_FILENAME, is, (int) size); 
								bannerGraphic = op.getResourceBytes(DatabaseFactory.RES_CARRIER_LOGO_FILENAME);
								if (bannerGraphic == null)
									bannerGraphic = new byte[0];
//								if (imageHeight != null)
//									req.setAttribute("_banner_height", imageHeight);
								writeBanner(req, resp);
							}
							else if (action.equals("template_upload") && template != null)
							{
								op = DatabaseFactory.getInstance().startOperation();
								op.saveResource(template, is, (int) size);
								writeTemplateUploaded(req, resp);
							}
						}
						finally
						{
							if (op != null)
								op.close();
						}
					}
				}
			}
		}
		catch(Throwable theException)
		{
			LOGGER.error(theException.getMessage());
			// uncomment the following line when unexpected exceptions
			// are occuring to aid in debugging the problem.
			theException.printStackTrace();
		}
	}
	
	protected void writeBanner(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		resp.setContentLength(bannerGraphic.length);
		ByteArrayInputStream instr = new ByteArrayInputStream(bannerGraphic);
		ServletOutputStream outstr = resp.getOutputStream();
					
		int read = 0;
		byte[] buf = new byte[1024];
		while ((read = instr.read(buf)) != -1)
			outstr.write(buf, 0, read);
		outstr.flush();
		outstr.close();	
	}

	protected void writeBannerConfigOutput(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		PrintWriter out = resp.getWriter();
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>Import graphic image file</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");
//		out.println("<FORM ENCTYPE=\"multipart/form-data\" ACTION=\"" + req.getContextPath() + req.getServletPath() + "\" METHOD=\"POST\">");
		out.println("<FORM ENCTYPE=\"multipart/form-data\" ACTION=\"" + getRequestUrl(req, "/resource") + "\" METHOD=\"POST\">");
		out.println("Image file to upload: <input type=\"hidden\" name=\"id\" value=\"banner_upload\">");
		out.println("<input type=\"file\" name=\"filename\"><BR>&nbsp;<BR>");
		out.println("<input type=\"submit\" value=\"Import File\">");
		out.println("</FORM>");
		out.println("</BODY>");
		out.println("</HTML>");
	}

	protected void writeTemplateUploaded(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		PrintWriter out = resp.getWriter();
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>Template upload complete</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY><p>Template upload complete.</p>");
		out.println("<p>&nbsp;</p>");
		out.println("<p align='center'><input type='button' value='Close' onclick='window.close();'></p>");
		out.println("</BODY>");
		out.println("</HTML>");
		
	}

	protected void writeEMailConfigOutput(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		PrintWriter out = resp.getWriter();
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>Import custom text template file</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");
//		out.println("<FORM ENCTYPE=\"multipart/form-data\" ACTION=\"" + req.getContextPath() + req.getServletPath() + "\" METHOD=\"POST\">");
		out.println("<FORM ENCTYPE=\"multipart/form-data\" ACTION=\"" + getRequestUrl(req, "/resource") + "\" METHOD=\"POST\">");
		out.println("<input type=\"hidden\" name=\"id\" value=\"template_upload\">");
		out.println("Template to upload: <select name=\"template\">");
		out.println("<option value=\"dl_failed_email\">Download Alert! email sent to Scheduled Download agents</option>");
		out.println("<option value=\"dl_stale_email\">Download Alert! email sent to Non-Scheduled/Interactive Download agents</option>");
		out.println("<option value=\"agent_migration\">Email sent to existing agents for migration to new non-browser app</option>");
		out.println("<option value=\"applied_edits_email\">Email sent to agents notifying of Applied Edits to be downloaded</option>");
		out.println("<option value=\"url_changed_email\">Email sent to agents on URL or security change</option>");
		out.println("<option value=\"new_agent_email\">Email sent to new agents to register (using browser-based app)</option>");
		out.println("<option value=\"new_agt_email_client\">Email sent to new agents (using new non-browser app, PREFERRED)</option>");
		out.println("<option value=\"agent_migrated_email\">Email sent to carrier on agent migration completion</option>");
		out.println("<option value=\"agent_reg_email\">Email sent to carrier on agent registration completion</option>");
		out.println("<option value=\"status_changed_email\">Email sent to carrier on agent status change</option>");
		out.println("<option value=\"vendor_changed_email\">Email sent to carrier when agent changes vendor system info</option>");
		out.println("<option value=\"welcome_page_text\">HTML welcome page displayed to registering agents</option>");
		out.println("</select><BR>&nbsp;<BR>");
		out.println("Text file: <input type=\"file\" name=\"filename\"><BR>&nbsp;<BR>");
		out.println("<input type=\"submit\" value=\"Import File\">");
		out.println("</FORM>");
		out.println("</BODY>");
		out.println("</HTML>");
	}

	/**
	 * Returns the current base URL from the HTTP request, using the specified 
	 * servlet path.
	 * @param request
	 * @param servletPath
	 * @return
	 */
	public String getRequestUrl(HttpServletRequest request, String servletPath)
	{
		// Build the base URL for this app
		int port = request.getServerPort();
		String url = "";
//		String thisUrl = request.getRequestURL().toString();
//		if (thisUrl != null && thisUrl.length() > 5)
//		{
//			if (thisUrl.substring(0, 5).equalsIgnoreCase("https"))
//				url = "https://";
//			else
//				url = "http://";
//		}
//		else
//		{
//			if (port == 443)
//				url = "https://";
//			else
//				url = "http://";
//		}
//		url += request.getServerName();
//		if (port != 80 && port != 443)
//			url += ":" + String.valueOf(port);
		
		String contextPath = request.getContextPath();
		if (contextPath == null || contextPath.equalsIgnoreCase("") || contextPath.equals(servletPath))
			contextPath = "/teamupdl";
		url += contextPath + servletPath;
		
		return url;
	}

}
