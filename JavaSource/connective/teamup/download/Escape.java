/*
 * Created on Dec 8, 2003 - mccrearyk
 */
package connective.teamup.download;

/**
 * @author Kyle McCreary
 *
 * Escape special characters as required for different target data types.
 */
public class Escape
{
	
	/**
	 * Escapes reserved and tag symbols as needed for displaying text in HTML.
	 * @param data The data to check
	 * @return String
	 */
	public static String forHtml(String data)
	{
		StringBuffer str = new StringBuffer("");
		for (int i=0; i < data.length(); i++)
		{
			char c = data.charAt(i);
			
			if (c == '<')
				str.append("&lt;");
			else if (c == '>')
				str.append("&gt;");
			else if (c == '&')
				str.append("&amp;");
			else
				str.append(c);
		}
		
		return str.toString();
	}
	
	/**
	 * Adds escape sequencing as needed for single quotes for use with SQL statements.
	 * @param data The data to check
	 * @return String
	 */
	public static String forSQL(String data)
	{
		if (data == null || data.equals(""))
			return "";
		if (data.indexOf("'") < 0)
			return data;
		
		StringBuffer text = new StringBuffer("");
			
		for (int i=0; i < data.length(); i++)
		{
			char c = data.charAt(i);
			if (c == '\'')
				text.append("''");
			else
				text.append(c);
		}
		
		return text.toString();
	}

	/**
	 * Turns funky characters into HTTP URL entity equivalents.<p>
	 * e.g. <tt>"bread" & "butter"</tt> => <tt>%22bread%22%20%26%20%22butter%22</tt>.
	 * 
	 * @param string String to convert
	 * 
	 * @return java.lang.String
	 **/
	public static String forUrl(String s1)
	{
		StringBuffer buf = new StringBuffer();
		int i;
		for (i = 0; i < s1.length(); ++i)
			{
			char ch = s1.charAt(i);
			if ((int) ch < 48)
				buf.append("%" + Integer.toHexString((int) ch));
			else
				buf.append(ch);
		}
		return buf.toString();
	}
				
	/**
	* Escape the "special" characters as required for inclusion in XML elements
	* Replaces all incidences of 
	*	& with &amp; 
	*   < with &lt;
	*   > with &gt; 
	*   " with &quo;
	*   ' with &apos;
	* @param s The string to scan
	* @return String
	*/
	public static String forXML(String s)
	{
		if (s == null)
			return "";
		
		char[] array = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++)
		{
			switch (array[i])
			{
				case '&' :
					sb.append("&amp;");
					break;
				case '<' :
					sb.append("&lt;");
					break;
				case '>' :
					sb.append("&gt;");
					break;
				case '"' :
					sb.append("&quot;");
					 break;
				case '\'':
					 sb.append("&apos;");
					 break;
				 default:
					 sb.append(array[i]);
			}      
		}
		
		return sb.toString();
	}
}
