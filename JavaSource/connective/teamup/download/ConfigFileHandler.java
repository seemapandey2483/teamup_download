package connective.teamup.download;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * Insert the type's description here.
 * Creation date: (9/28/2001 10:06:31 AM)
 * @author: Mike Haney
 */
public class ConfigFileHandler extends DefaultHandler 
{
	private static final Logger LOGGER = Logger.getLogger(ConfigFileHandler.class);
	
	private Hashtable pages = new Hashtable();
	private Hashtable actions = new Hashtable();
	private Vector menubar = new Vector();
	private PageDef currentPage = null;
	private ActionDef currentAction = null;
	private MenuDef currentMenu = null;
	private MenuItemDef currentItem = null;
	private String cdata = null;

	private static final int NO_CONTEXT = 0;
	private static final int PAGE_CONTEXT = 1;
	private static final int ACTION_CONTEXT = 2;
	private static final int PAGEMENU_CONTEXT = 3;
	private static final int MENU_CONTEXT = 4;
	private static final int MENUITEM_CONTEXT = 5;
	
	private int context = NO_CONTEXT;
	
	/**
	 * ConfigFileHandler constructor comment.
	 */
	public ConfigFileHandler(Hashtable pages, Hashtable actions, Vector menubar)
	{
		super();
	
		this.pages = pages;
		this.actions = actions;
		this.menubar = menubar;
	}

	public void characters(char[] ch, int start, int length) throws SAXException
	{
		String tmp = new String(ch, start, length);
		if (tmp == null)
			tmp = "";
		cdata += tmp;		
	}	

	/**
	 * Insert the method's description here.
	 * Creation date: (9/28/2001 10:29:49 AM)
	 */
	public void endElement(String namespace, String simpleName, String qualifiedName) throws org.xml.sax.SAXException
	{
		if (qualifiedName.equals("page"))
		{
			// save the current page to the hashtable
			pages.put(currentPage.getId(), currentPage);
			currentPage = null;
			context = NO_CONTEXT;
		}
		else if (qualifiedName.equals("action"))
		{
			if (context == ACTION_CONTEXT)
			{
				// save the current action to the hashtable
				actions.put(currentAction.getId(), currentAction);
				currentAction = null;
				context = NO_CONTEXT;
			}
			else if (context == PAGEMENU_CONTEXT)
			{
				currentItem.setAction(cdata);
			}
			else if (context == MENUITEM_CONTEXT)
			{
				currentItem.setAction(cdata);
			}
		}
		else if (qualifiedName.equals("pagemenu"))
		{
			currentPage.getMenuItems().addElement(currentItem);
			currentItem = null;
			context = PAGE_CONTEXT;
		}
		else if (qualifiedName.equals("menu"))
		{
			menubar.addElement(currentMenu);
			currentMenu = null;
			context = NO_CONTEXT;
		}
		else if (qualifiedName.equals("item"))
		{
			currentMenu.getItems().addElement(currentItem);
			currentItem = null;
			context = MENU_CONTEXT;
		}
		else if (qualifiedName.equals("id"))
		{
			if (context == PAGE_CONTEXT)
				currentPage.setId(cdata);
			else if (context == ACTION_CONTEXT)
				currentAction.setId(cdata);
		}
		else if (qualifiedName.equals("pagebean"))
		{
			currentPage.setPageBean(cdata);
		}
		else if (qualifiedName.equals("displaybean"))
		{
			currentPage.setDisplayBean(cdata);
		}
		else if (qualifiedName.equals("jsp"))
		{
			currentPage.setJsp(cdata);
		}
		else if (qualifiedName.equals("actionbean"))
		{
			currentAction.setActionBean(cdata);
		}
		else if (qualifiedName.equals("alias"))
		{
			currentAction.setAlias(cdata);
		}
		else if (qualifiedName.equals("text"))
		{
			if (context == PAGEMENU_CONTEXT)
				currentItem.setText(cdata);
			else if (context == MENU_CONTEXT)
				currentMenu.setText(cdata);
			else if (context == MENUITEM_CONTEXT)
				currentItem.setText(cdata);
		}
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (9/28/2001 10:29:49 AM)
	 */
	public void startElement(String namespace, String simpleName, String qualifiedName, Attributes attr) throws org.xml.sax.SAXException
	{
		cdata = "";
		
		try
		{
			if (qualifiedName.equals("page"))
			{
				currentPage = new PageDef();
				context = PAGE_CONTEXT;
			}
			else if (qualifiedName.equals("action"))
			{
				if (context == NO_CONTEXT)
				{
					currentAction = new ActionDef();
					context = ACTION_CONTEXT;
				}
			}
			else if (qualifiedName.equals("pagemenu"))
			{
				currentItem = new MenuItemDef();
				context = PAGEMENU_CONTEXT;
			}
			else if (qualifiedName.equals("menu"))
			{
				currentMenu = new MenuDef();
				context = MENU_CONTEXT;
			}
			else if (qualifiedName.equals("item"))
			{
				currentItem = new MenuItemDef();
				context = MENUITEM_CONTEXT;
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			System.out.println(e.getMessage());
		}
	}
}
