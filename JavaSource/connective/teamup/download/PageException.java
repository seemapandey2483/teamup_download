package connective.teamup.download;

/**
 * @author haneym
 */
public class PageException extends BaseException
{
	/**
	 * Constructor for PageException.
	 */
	public PageException()
	{
		super();
	}

	/**
	 * Constructor for PageException.
	 * @param msg - Error message
	 */
	public PageException(String msg)
	{
		super(msg);
	}

	/**
	 * Constructor for PageException.
	 * @param msg - Error message
	 * @param e - The exception
	 */
	public PageException(String msg, Exception e)
	{
		super(msg);
		
		this.e = e;
	}

	public PageException(DisplayBeanException dbe)
	{
		super(dbe.getMessage());
		this.e = dbe.e;
	}
}
