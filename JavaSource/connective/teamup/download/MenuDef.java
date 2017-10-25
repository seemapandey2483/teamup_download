package connective.teamup.download;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MenuDef implements Serializable 
{
	private String text = null;
	private Vector items = new Vector();
	
	/**
	 * Constructor for MenuDef.
	 */
	public MenuDef() 
	{
		super();
	}


	/**
	 * Returns the text.
	 * @return String
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * @param text The text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the items.
	 * @return Vector
	 */
	public Vector getItems() {
		return items;
	}

	/**
	 * Sets the items.
	 * @param items The items to set
	 */
	public void setItems(Vector items) {
		this.items = items;
	}

}
