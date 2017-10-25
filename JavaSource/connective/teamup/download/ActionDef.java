package connective.teamup.download;


/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ActionDef 
{
	private String id = null;
	private String actionBean = null;
	private String alias = null;
	private Action instance = null;
	
	/**
	 * Constructor for ActionDef.
	 */
	public ActionDef() 
	{
		super();
	}

	public Action getActionInstance() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		if (instance == null)
		{
			instance = (Action) Class.forName(actionBean).newInstance();
		}
		
		return instance;
	}

	/**
	 * Returns the actionBean.
	 * @return String
	 */
	public String getActionBean() {
		return actionBean;
	}

	/**
	 * Returns the id.
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the actionBean.
	 * @param actionBean The actionBean to set
	 */
	public void setActionBean(String actionBean) {
		this.actionBean = actionBean;
	}

	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the alias.
	 * @return String
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Sets the alias.
	 * @param alias The alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

}
