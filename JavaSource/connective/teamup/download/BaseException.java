/*
 * 05/27/2005 - Created
 */
package connective.teamup.download;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Base class for enhancing the various custom exceptions used in the TEAM-UP Download app.
 * 
 * @author Kyle McCreary
 */
class BaseException extends Exception
{
	protected Exception e = null;


	public BaseException()
	{
		super();
	}

	public BaseException(String msg)
	{
		super(msg);
	}

	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() 
	{
		String ret = super.getMessage();
		if (ret == null)
			ret = "";
		if (ret.indexOf("Embedded Exception") < 0 && e != null)
		{
			String temp = e.getMessage();
			if (temp != null)
			{
				int n = temp.indexOf("Embedded Exception");
				if (n >= 0)
					ret = temp;
				else
					ret += "\nEmbedded Exception: " + e.getClass().getName() + " -- " + temp;
			}
		}

		return ret;
	}
	
	/**
	 * @see java.lang.Throwable#printStackTrace()
	 */
	public void printStackTrace() 
	{
		if (e == null)
			super.printStackTrace();
		else
			e.printStackTrace();
	}

	/**
	 * @see java.lang.Throwable#printStackTrace(PrintStream)
	 */
	public void printStackTrace(PrintStream s) 
	{
		if (e == null)
			super.printStackTrace(s);
		else
		{
//			if (getMessage() != null)
//				s.println(getClass().getName() + ":  " + getMessage());
			e.printStackTrace(s);
		}
	}

	/**
	 * @see java.lang.Throwable#printStackTrace(PrintWriter)
	 */
	public void printStackTrace(PrintWriter s) 
	{
		if (e == null)
			super.printStackTrace(s);
		else
		{
//			if (getMessage() != null)
//				s.println(getClass().getName() + ":  " + getMessage());
			e.printStackTrace(s);
		}
	}

	/**
	 * Returns the stack trace as a string.
	 */
	public String getStackTraceAsString()
	{
		String str = getMessage();
		try
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			printStackTrace(ps);
			str = os.toString();
			ps.close();
			os.close();
		}
		catch (IOException ioe)
		{
			// ignore
		}
				
		return str;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	public String toString()
	{
		return getMessage();
	}


}
