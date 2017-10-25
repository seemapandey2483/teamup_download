/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.objects;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetVendorSystemListOutput 
{
	protected AgencyVendorInfo[] vendorInfos = null;
	/**
	 * @return
	 */
	public AgencyVendorInfo[] getVendorInfos() {
		return vendorInfos;
	}

	/**
	 * @param infos
	 */
	public void setVendorInfos(AgencyVendorInfo[] infos) {
		vendorInfos = infos;
	}

}
