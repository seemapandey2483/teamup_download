package connective.teamup.download;

/**
 * @author haneym
 */
public class ActionException extends BaseException
{
	/**
	 * Constructor for ActionException.
	 */
	public ActionException()
	{
		super();
	}

	/**
	 * Constructor for ActionException.
	 * @param arg0
	 */
	public ActionException(String arg0)
	{
		super(arg0);
	}

	/**
	 * Constructor for ActionException.
	 * @param msg - Error message
	 * @param e - The exception
	 */
	public ActionException(String msg, Exception e)
	{
		super(msg);
		
		this.e = e;
	}

}
