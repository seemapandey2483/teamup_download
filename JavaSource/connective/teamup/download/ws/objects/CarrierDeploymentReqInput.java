package connective.teamup.download.ws.objects;

public class CarrierDeploymentReqInput {

	private String carrierDeploymentId = null;


	/**
	 * Returns the unique carrier deployment ID.
	 */
	public String getCarrierDeploymentId()
	{
		return carrierDeploymentId;
	}

	/**
	 * Sets the unique ID for the carrier deployment info to be retrieved.
	 */
	public void setCarrierDeploymentId(String carrierDeploymentId)
	{
		this.carrierDeploymentId = carrierDeploymentId;
	}
}
