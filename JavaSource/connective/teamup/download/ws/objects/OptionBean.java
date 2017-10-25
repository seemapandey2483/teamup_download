/*
 * Created on May 13, 2004
 * 
 * Copied from the "Connective Utilities" project:
 * package connective.web.pagemanager;
 */
package connective.teamup.download.ws.objects;

import java.io.Serializable;

/**
 * Info bean used for storing label/value pairs for SELECT and RADIO control options.
 * 
 * @author Kyle McCreary
 */
public class OptionBean implements Serializable
{
	private String label;
	private String value;
	
	
	/**
	 * Constructor for OptionBean.
	 * @param label The text to be displayed
	 * @param value The value associated with the label
	 */
	public OptionBean(String label, String value)
	{
		super();
		
		this.label = label;
		this.value = value;
	}

	/**
	 * Returns the label text for this option.
	 * @return String
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * Returns the data value for this option.
	 * @return String
	 */
	public String getValue()
	{
		return value;
	}

}
