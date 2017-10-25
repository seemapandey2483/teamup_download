package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.Participants;

public class AgentSeparateDisplayBean implements Serializable, DisplayBean{
	
	private static final Logger LOGGER = Logger.getLogger(AgentSeparateDisplayBean.class);
	
	private AgentInfo[] agents = null;
	private CarrierInfo carrierInfo = null;
	private String partcode = "";
	private String agentId = "";
	
	public String oldAgentId = null;
	public String oldAgentName = null;
	public String rollToAgentId = null;
	public String rollToAgentName = null;
	public Vector displayText = null;
	public boolean rollupOkay = true;
	
	private static final long serialVersionUID = 1L;

	public AgentSeparateDisplayBean() {
		super();
	}
	
	@Override
	public void init(HttpServletRequest req, HttpServletResponse resp,
			ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws DisplayBeanException {
		try{
			Participants participants = null;
			carrierInfo = serverInfo.getCarrierInfo();
			String partCode = req.getParameter("pCode");
			String moveAgent = req.getParameter("mAgent");
			String newTPAgent = req.getParameter("nTPAgent");
			String action = req.getParameter("action");
			req.getSession().setAttribute("pCode", partCode);
			
			try{
				if ("tp_Separation_Only".equals(action) || "tp_summary".equals(action)){
					if (partCode == null){
						return;
					}
					AgentInfo agentID = op.getAgentInfo(partCode);
					participants = op.loadPartcipants(partCode);
					displayText = new Vector();
					if (agentID == null) {
						displayText.add("Cannot find a agent of trading partner with the specified ID:  " + partCode + ".");
						displayText.add("Please re-enter the parameters and try again.");
						return;
					} else {
						displayText.add("Source Trading Partner was: " + agentID.getAgentId() + "-" + (agentID.getName() == null ? " " : agentID.getName()) + ".");
						displayText.add(partCode + "-" + (participants.getAgencyName() == null ? " " : participants.getAgencyName())  + " will be moved on its own as a Primary Agent");
						displayText.add("Note: all archives will be moved to the new trading partner location");
					}
				} else if ("tp_Move_Only".equals(action) || "tp_move_summary".equals(action)){
					AgentInfo moveAgentID = op.getAgentInfo(moveAgent);
					AgentInfo newTPAgentID = op.getAgentInfo(newTPAgent);
					displayText = new Vector();
					if (moveAgent == null || newTPAgent == null || moveAgentID == null || newTPAgentID == null){
						displayText.add("Cannot find a agent of trading partner with the specified ID:  " + moveAgent + " or " + newTPAgent + ".");
						displayText.add("Please re-enter the parameters and try again.");
						return;
					} else {
						displayText = new Vector();
						displayText.add("Source Trading Partner was: " + moveAgentID.getAgentId() + "-" + moveAgentID.getName() + ".");
						displayText.add("Roll Into: "+ newTPAgentID.getAgentId() + "-" + newTPAgentID.getName() + ".");
						displayText.add("Trading Partner " + moveAgentID.getAgentId() + "-" + moveAgentID.getName() + " will become new participant code under " + newTPAgentID.getAgentId() + "-" + newTPAgentID.getName() + ".");
						displayText.add("Note: all archives will be moved to the new trading partner archive location");
					}
					
				}
				
			} catch (Exception e){
				LOGGER.error("Error occurred building the trading partner as agent", e);
				e.printStackTrace(System.out);
				throw new DisplayBeanException("Error occurred building the trading partner as agent", e);
			}
			
		} catch(Exception e){
			LOGGER.error("Error occurred building the trading partner as Agent", e);
			throw new DisplayBeanException("Error occurred building the trading partner as Agent", e);
		}
	}
	
	/**
	 * Returns the display text.
	 * @param index The line number to return
	 * @return String
	 */
	public String getDisplayText(int index) {
		if (displayText == null || index >= displayText.size())
			return "";
		return (String) displayText.elementAt(index);
	}
	
	/**
	 * Returns the number of lines of display text.
	 * @return int
	 */
	public int getDisplayTextLineCount() {
		if (displayText == null)
			return 0;
		return displayText.size();
	}

	/**
	 * Returns the old agent ID.
	 * @return String
	 */
	public String getOldAgentId() {
		if (oldAgentId == null)
			return "";
		return oldAgentId;
	}
	
	/**
	 * Returns the roll-to agent ID.
	 * @return String
	 */
	public String getRollToAgentId() {
		return rollToAgentId;
	}
	
	/**
	 * Returns true if roll-up definition is good, otherwise false.
	 * @return boolean
	 */
	public boolean isRollupOkay() {
		return rollupOkay;
	}
	
	/**
	 * Returns the old agent name.
	 * @return String
	 */
	public String getOldAgentName() {
		return oldAgentName;
	}
	
	/**
	 * Returns the roll-to agent name.
	 * @return String
	 */
	public String getRollToAgentName() {
		return rollToAgentName;
	}
	
	/**
	 * Returns the carrier info.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo(){
		return carrierInfo;
	}
	
	public int getAgentCount(){
		if (agents == null)
			return 0;
		return agents.length;
	}

	public String getPartcode() {
		return partcode;
	}

	public void setPartcode(String partcode) {
		this.partcode = partcode;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}


}
