package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.Vector;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.MenuDef;
import connective.teamup.download.MenuItemDef;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MenuBean implements Serializable 
{
	private CarrierInfo carrierInfo = null;
	private Vector menubar = null;
	private Vector pagemenu = null;
	
	/**
	 * Constructor for MenuBean.
	 */
	public MenuBean(Vector menubar, Vector pagemenu, CarrierInfo carrierInfo) 
	{
		super();
		
		this.carrierInfo = carrierInfo;
		this.menubar = menubar;
		this.pagemenu = pagemenu;
	}
	
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	/**
	 * Returns the banner graphic filename.
	 * @return String
	 */
	public String getBannerGraphic()
	{
		return carrierInfo.getBannerGraphicFile();
	}

	public int getMenuCount()
	{
		return menubar.size();
	}
	
	public String getMenuText(int menu)
	{
		MenuDef def = (MenuDef) menubar.elementAt(menu);
		return def.getText();
	}
	
	public int getMenuItemCount(int menu)
	{
		MenuDef def = (MenuDef) menubar.elementAt(menu);
		return def.getItems().size();
	}
	
	public String getMenuItemText(int menu, int item)
	{
		MenuDef menudef = (MenuDef) menubar.elementAt(menu);
		MenuItemDef itemdef = (MenuItemDef) menudef.getItems().elementAt(item);
		return fillTags(itemdef.getText());		
	}
	
	public String getMenuItemAction(int menu, int item)
	{
		MenuDef menudef = (MenuDef) menubar.elementAt(menu);
		MenuItemDef itemdef = (MenuItemDef) menudef.getItems().elementAt(item);
		return fillTags(itemdef.getAction());
	}

	public int getPageMenuItemCount()
	{
		if (pagemenu == null)
			return 0;
			
		return pagemenu.size();			
	}
	
	public String getPageMenuItemText(int item)
	{
		MenuItemDef def = (MenuItemDef) pagemenu.elementAt(item);
		return def.getText();
	}
	
	public String getPageMenuItemAction(int item)
	{
		MenuItemDef def = (MenuItemDef) pagemenu.elementAt(item);
		return def.getAction();
	}
	
	private String fillTags(String text)
	{
		String str = "";
		if (text != null && text.length() > 0)
		{
			str = text;
			int n = str.indexOf("[TAG:");
			while (n >= 0 && str.length() > n+5)
			{
				String temp = str.substring(n+5);
				str = str.substring(0, n);
				int x = temp.indexOf("]");
				if (x < 0)
					str += temp;
				else
				{
					if (x > 0)
					{
						String tag = temp.substring(0, x);
						if (tag.equalsIgnoreCase("CarrierName"))
							str += carrierInfo.getName();
						else if (tag.equalsIgnoreCase("CarrierShortName"))
							str += carrierInfo.getShortName();
						else if (tag.equalsIgnoreCase("CarrierContactEmail"))
							str += carrierInfo.getContactEmail();
					}
					
					if (temp.length() > x+1)
						str += temp.substring(x+1);
				}
				
				n = str.indexOf("[TAG:");
			}
		}
		
		return str;
	}
}
