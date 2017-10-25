/*
 * 04/10/2005 - Created
 */
package connective.teamup.download.agentimport;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Read comma-delimited text from a character-input stream, buffering 
 * characters so as to provide for the efficient reading of characters, 
 * arrays, and lines.
 * 
 * @author Kyle McCreary
 */
public class CommaDelimitedFileReader extends Reader
{
	BufferedReader reader = null;
	String lastLine = null;


	/**
	 * Constructor for CommaDelimitedFileReader.
	 */
	public CommaDelimitedFileReader(InputStream is)
	{
		super();
		
		reader = new BufferedReader(new InputStreamReader(is));
	}

	/**
	 * Constructor for CommaDelimitedFileReader.
	 */
	public CommaDelimitedFileReader(byte[] fileContents)
	{
		super();
		
		ByteArrayInputStream is = new ByteArrayInputStream(fileContents);
		reader = new BufferedReader(new InputStreamReader(is));
	}

	public String readLine() throws IOException
	{
		lastLine = reader.readLine();
		return lastLine;
	}

	public String[] getFieldData()
	{
		if (lastLine == null)
			return null;
		return parseCommaDelimited(lastLine);
	}

	/* (non-Javadoc)
	 * @see java.io.Reader#read(char[], int, int)
	 */
	public int read(char[] arg0, int arg1, int arg2) throws IOException
	{
		return reader.read(arg0, arg1, arg2);
	}

	public String[] parseCommaDelimited(String data)
	{
		if (data == null || data.trim().equals(""))
			return null;
		
		ArrayList fieldData = new ArrayList();
		boolean quoted = false;
		char lastChar = ' ';
		StringBuffer buf = new StringBuffer();
		for (int i=0; i < data.length(); i++)
		{
			char c = data.charAt(i);
			if (c == ',' && !quoted)
			{
				fieldData.add(buf.toString());
				buf = new StringBuffer();
			}
			else if (c == '"')
			{
				if (!quoted && lastChar == '"')
				{
					buf.append(c);
					c = ' ';
				}
				quoted = !quoted;
			}
			else
			{
				buf.append(c);
			}
			lastChar = c;
		}
		
		if (buf.length() > 0)
			fieldData.add(buf.toString());
		
		String[] ret = null;
		if (fieldData.size() > 0)
		{
			ret = new String[fieldData.size()];
			fieldData.toArray(ret);
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see java.io.Reader#close()
	 */
	public void close() throws IOException
	{
		reader.close();
	}

}
