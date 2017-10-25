package connective.teamup.download;

import java.io.Serializable;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MenuItemDef implements Serializable
{
	private String text = null;
	private String action = null;
	
	/**
	 * Constructor for MenuItemDef.
	 */
	public MenuItemDef() 
	{
		super();
	}
	/**
	 * Returns the action.
	 * @return String
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Returns the text.
	 * @return String
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the action.
	 * @param action The action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Sets the text.
	 * @param text The text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
