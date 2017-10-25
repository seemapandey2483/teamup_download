package connective.teamup.registration.ws.objects;

public class CarrierAmsGroupInput {
	private String amsId;
	private int agencyCount;
	private int partcipantCount;
	

	public String getAmsId() {
		return amsId;
	}
	public void setAmsId(String amsId) {
		this.amsId = amsId;
	}
	public int getAgencyCount() {
		return agencyCount;
	}
	public void setAgencyCount(int agencyCount) {
		this.agencyCount = agencyCount;
	}
	public int getPartcipantCount() {
		return partcipantCount;
	}
	public void setPartcipantCount(int partcipantCount) {
		this.partcipantCount = partcipantCount;
	}
}
