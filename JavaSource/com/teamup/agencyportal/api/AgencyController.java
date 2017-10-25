 package com.teamup.agencyportal.api;

import java.util.List;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ebix.api.model.AgentConfigInfo;
import com.ebix.api.model.MonthlyBill;

@RestController
public class AgencyController {

	AgencyService agencyService = new AgencyService();

	@RequestMapping(value = "/agents/{agentId}/{password}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Boolean> isValidAgent(@PathVariable String agentId,
			@PathVariable String password) {
		try {
			boolean isValidAgent = agencyService.validate(agentId, password);
			return new ResponseEntity<Boolean>(isValidAgent, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Boolean>(false,
					HttpStatus.EXPECTATION_FAILED);
		}
		

	}

	@RequestMapping(value = "/agentsDB/{agentId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<AgentConfigInfo> getAgentByIdDB(
			@PathVariable String agentId) {
		try{
			AgentConfigInfo agent = agencyService.getAgentByAgentId(agentId);
			if (agent.getAgentId() == null) {
				return new ResponseEntity<AgentConfigInfo>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<AgentConfigInfo>(agent, HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<AgentConfigInfo>(HttpStatus.EXPECTATION_FAILED);
		}
	
	
	}

	@RequestMapping(value = "/monthlyReport/{date}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<MonthlyBill>> monthlyMonthlyBill(
			@PathVariable long date) {
		
		try{
			List<MonthlyBill> monthlyBills = agencyService
					.listMonthlyBill(date);
			if (monthlyBills.isEmpty()) {
				return new ResponseEntity<List<MonthlyBill>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<MonthlyBill>>(monthlyBills,
					HttpStatus.OK);
	
		}catch(Exception e){
			return new ResponseEntity<List<MonthlyBill>>(HttpStatus.EXPECTATION_FAILED);
		}
		
	}

	@RequestMapping(value = "/ListTransLog/{date}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<MonthlyBill>> getListTransLog(
			@PathVariable long date) {
		
		try{
			List<MonthlyBill> monthlyBills = agencyService
					.getListTransLog(date);
			if (monthlyBills.isEmpty()) {
				return new ResponseEntity<List<MonthlyBill>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<MonthlyBill>>(monthlyBills,
					HttpStatus.OK);
	
		}catch(Exception e){
			return new ResponseEntity<List<MonthlyBill>>(HttpStatus.EXPECTATION_FAILED);
		}
		
	}
	
	@RequestMapping(value = "/TransLog/{date}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<MonthlyBill> getTransLog(
			@PathVariable long date) {
		
		try{
			MonthlyBill monthlyBills = agencyService
					.getTransLog(date);
			if (monthlyBills == null) {
				return new ResponseEntity<MonthlyBill>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<MonthlyBill>(monthlyBills,
					HttpStatus.OK);
	
		}catch(Exception e){
			return new ResponseEntity<MonthlyBill>(HttpStatus.EXPECTATION_FAILED);
		}
		
	}
	
	@RequestMapping(value = "/disableAgent/{agentId}/{flag}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Boolean> disabeAgent(@PathVariable String agentId,
			@PathVariable String flag) {
		try{
			boolean disable = AgencyService.disableAgent(agentId, flag);// new
			return new ResponseEntity<Boolean>(disable, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<Boolean>(false,HttpStatus.EXPECTATION_FAILED);
		}
		
	}
	
	@Autowired
	ServletContext context;
	@RequestMapping(value = "/createAgent", method = RequestMethod.POST)
    public ResponseEntity<Boolean> createAgent(@RequestBody AgentConfigInfo agent, UriComponentsBuilder ucBuilder) {
		boolean disable = false;   
		try{
        		 disable = AgencyService.createAgent(agent,context);// new
        }catch(Exception e){return new ResponseEntity<Boolean>(false,HttpStatus.EXPECTATION_FAILED);}
        return new ResponseEntity<Boolean>(disable,HttpStatus.OK);
    }
}