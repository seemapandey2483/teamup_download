package connective.teamup.registration.ws.objects;

import java.util.List;

public class AmsGroupSummaryOutput {

	private List<CarrierAmsGroupInput> carrierAmsGroupInput;
	private String carrierId;
	
	public List<CarrierAmsGroupInput> getCarrierAmsGroupInput() {
		return carrierAmsGroupInput;
	}

	public void setCarrierAmsGroupInput(
			List<CarrierAmsGroupInput> carrierAmsGroupInput) {
		this.carrierAmsGroupInput = carrierAmsGroupInput;
	}

	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	
}
