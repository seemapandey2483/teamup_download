package connective.teamup.download;

import java.util.Vector;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PageDef 
{
	private static final Logger LOGGER = Logger.getLogger(PageDef.class);
	
	private String id = null;
	private String pageBean = "connective.teamup.download.GenericPage";
	private String displayBean = null;
	private String jsp = null;
	private Page instance = null;
	private Vector menuItems = new Vector();
		
	/**
	 * Constructor for PageDef.
	 */
	public PageDef() 
	{
		super();
	}

	public Page getPageInstance(ServletContext context) throws PageException
	{
		if (instance == null)
		{
			try
			{
				instance = (Page) Class.forName(pageBean).newInstance();
				instance.init(this, context);
			}
			catch (Exception e)
			{
				LOGGER.error(e.getMessage());
				throw new PageException("Error creating page instance for '" + pageBean + "'", e);
			}
		}
		
		return instance;
	}
	
	/**
	 * Returns the displayBean.
	 * @return String
	 */
	public String getDisplayBean() {
		return displayBean;
	}

	/**
	 * Returns the id.
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the jsp.
	 * @return String
	 */
	public String getJsp() {
		return jsp;
	}

	/**
	 * Returns the pageBean.
	 * @return String
	 */
	public String getPageBean() {
		return pageBean;
	}

	/**
	 * Sets the displayBean.
	 * @param displayBean The displayBean to set
	 */
	public void setDisplayBean(String displayBean) {
		this.displayBean = displayBean;
	}

	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the jsp.
	 * @param jsp The jsp to set
	 */
	public void setJsp(String jsp) {
		this.jsp = jsp;
	}

	/**
	 * Sets the pageBean.
	 * @param pageBean The pageBean to set
	 */
	public void setPageBean(String pageBean) {
		this.pageBean = pageBean;
	}
	/**
	 * Returns the menuItems.
	 * @return Vector
	 */
	public Vector getMenuItems() {
		return menuItems;
	}

	/**
	 * Sets the menuItems.
	 * @param menuItems The menuItems to set
	 */
	public void setMenuItems(Vector menuItems) {
		this.menuItems = menuItems;
	}

}
