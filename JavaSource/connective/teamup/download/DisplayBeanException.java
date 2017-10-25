package connective.teamup.download;

/**
 * @author haneym
 */
public class DisplayBeanException extends BaseException
{
	/**
	 * Constructor for DisplayBeanException.
	 */
	public DisplayBeanException()
	{
		super();
	}

	/**
	 * Constructor for DisplayBeanException.
	 * @param msg - Error message
	 */
	public DisplayBeanException(String msg)
	{
		super(msg);
	}

	/**
	 * Constructor for DisplayBeanException.
	 * @param msg - Error message
	 * @param e - The exception
	 */
	public DisplayBeanException(String msg, Exception e)
	{
		super(msg);
		
		this.e = e;
	}

}
