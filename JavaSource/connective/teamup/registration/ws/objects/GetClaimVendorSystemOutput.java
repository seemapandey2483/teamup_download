package connective.teamup.registration.ws.objects;

public class GetClaimVendorSystemOutput {
	VendorClaimSystemOutput[] vendorSystems = null;
	
	/**
	 * @return
	 */
	public VendorClaimSystemOutput[] getVendorSystems() {
		return vendorSystems;
	}

	/**
	 * @param outputs
	 */
	public void setVendorSystems(VendorClaimSystemOutput[] outputs) {
		vendorSystems = outputs;
	}

}
