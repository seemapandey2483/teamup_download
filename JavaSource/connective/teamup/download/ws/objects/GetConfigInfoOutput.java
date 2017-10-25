/*
 * Created on Aug 30, 2006
 */
package connective.teamup.download.ws.objects;

/**
 * Web service output object for the GetConfigInfo command.
 * 
 * @author mccrearyk
 */
public class GetConfigInfoOutput
{
	protected OptionBean[] companyInfo = null;
	protected OptionBean[] agentInfo = null;


	/**
	 * @return Returns the list of agent's registration information.
	 */
	public OptionBean[] getAgentInfo()
	{
		return agentInfo;
	}
	/**
	 * @param agentInfo - The list of agent information to set.
	 */
	public void setAgentInfo(OptionBean[] agentInfo)
	{
		this.agentInfo = agentInfo;
	}
	/**
	 * @return Returns the list of company information.
	 */
	public OptionBean[] getCompanyInfo()
	{
		return companyInfo;
	}
	/**
	 * @param companyInfo - The list of company information to set.
	 */
	public void setCompanyInfo(OptionBean[] companyInfo)
	{
		this.companyInfo = companyInfo;
	}
}
