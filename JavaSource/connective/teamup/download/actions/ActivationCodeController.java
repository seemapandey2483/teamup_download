package connective.teamup.download.actions;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.registration.ws.objects.CarrierIdRequest;
import connective.teamup.registration.ws.objects.GetCarrierActCodeOutput;
import connective.teamup.ws.client.TeamupWSClient;

public class ActivationCodeController implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(ActivationCodeController.class);

	@Override
	public String perform(HttpServletRequest req, HttpServletResponse resp,
			ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException {
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();
		String method = req.getParameter("method");
		String action = req.getParameter("action");
		String jsonArray ="";
		Gson gson = new Gson();

		try {
			if ("getActCode".equals(method)) {
				String actCode = "";
				String carrierRegMaster = "";
				String endpoint = serverInfo.getRegistrationUrl() + "/ws";
				TeamupWSClient ws = new TeamupWSClient(endpoint, "TUDL");
				CarrierIdRequest carrierIdInput = new CarrierIdRequest();
				carrierIdInput.setCarrierId(serverInfo.getCarrierInfo().getCarrierId());
				GetCarrierActCodeOutput outObject = (GetCarrierActCodeOutput) ws.callService("GetActivationCode", carrierIdInput);
				
				if(outObject != null  && outObject.getOutput() != null) {
					if(serverInfo.getCarrierInfo().getCarrierId().equals(outObject.getOutput().getCarrierId())){
						actCode = outObject.getOutput().getActivationCode();
						carrierRegMaster =  outObject.getOutput().getDeploymentId();
					}
				}
				JSONROOT.put("GetActivatedCode", actCode);
				JSONROOT.put("carrierRegMaster", carrierRegMaster);
				jsonArray = gson.toJson(JSONROOT);
				return jsonArray;
			} else if ("activationCode".equals(action)) {
				String toUpdateCode = req.getParameter("getCode");
				String carrierRegMaster = req.getParameter("carrierRegMaster"); 
				op.updateProperty(DatabaseFactory.PROP_CARRIER_ACTIVATION,toUpdateCode);
				op.updateProperty(DatabaseFactory.PROP_CARRIER_DEPLOYMENTID,carrierRegMaster);
				return "menu.activation.code";
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		
		return jsonArray;
	}

}
