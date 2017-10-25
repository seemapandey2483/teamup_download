/*
 * Created on Apr 12, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.agentimport;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ImportResult 
{
	protected boolean error = false;
	protected String msg = null;
	protected String data = null;
	
	public ImportResult(String data)
	{
		super();
		
		this.data = data;
	}
	
	public String getData()
	{
		return data;
	}
	
	public boolean isError()
	{
		return error;
	}
	
	public String getMessage()
	{
		return msg;
	}
	/**
	 * @param b
	 */
	public void setError(boolean b) {
		error = b;
	}

	/**
	 * @param string
	 */
	public void setMessage(String string) {
		msg = string;
	}

}
