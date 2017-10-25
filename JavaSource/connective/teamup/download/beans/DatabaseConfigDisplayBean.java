package connective.teamup.download.beans;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mike Haney
 *
 * Display bean for the Database config page
 */
public class DatabaseConfigDisplayBean implements Serializable
{
	private String servletPath = null;
	private boolean invalid = false;
	
	// type info
	private String[] typeIds = {"db2", "mssql"};
	private String[] typeDescriptions = {"IBM DB2", "Microsoft SQL Server"};
		
	/**
	 * Constructor for CompanyConfigDisplayBean.
	 */
	public DatabaseConfigDisplayBean(HttpServletRequest req, boolean invalid) throws IOException, FileNotFoundException
	{
		super();

		this.invalid = invalid;
		
		// Get the current servlet path from the request
		servletPath = req.getServletPath();
	}

	public boolean isInvalid()
	{
		return invalid;
	}
	
	public int getTypeCount()
	{
		return typeIds.length;
	}
	
	public String getTypeId(int index)
	{
		return typeIds[index];
	}
	
	public String getTypeDescription(int index)
	{
		return typeDescriptions[index];
	}

	/**
	 * Returns the servletPath.
	 * @return String
	 */
	public String getServletPath() {
		return servletPath;
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
