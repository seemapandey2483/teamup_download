package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ebix.licence.Licence;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.LicenceService;

public class ActivationDisplayBean implements Serializable, DisplayBean {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ActivationDisplayBean.class);

	CarrierInfo carrierInfo = null;
	String currentActivationCode = "";
	Date expirationDate = null;

	public ActivationDisplayBean() {
		super();
	}

	@Override
	public void init(HttpServletRequest req, HttpServletResponse resp,
			ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws DisplayBeanException {
		this.carrierInfo = serverInfo.getCarrierInfo();
		Licence lic = null;
		try {
			currentActivationCode = op.getPropertyValue(DatabaseFactory.PROP_CARRIER_ACTIVATION);
			String carrierDeploymentId = op.getPropertyValue(DatabaseFactory.PROP_CARRIER_DEPLOYMENTID);
			if(StringUtils.isNotBlank(currentActivationCode) && StringUtils.isNotBlank(carrierDeploymentId))
				lic = LicenceService.getCurrentLicence(currentActivationCode, carrierDeploymentId);
			
			if(lic!= null)
				this.expirationDate = lic.getExpirationDate();
			else
				LOGGER.error("Licence object not found for CarrierId:" + carrierInfo.getCarrierId());
		} catch (Exception e) {
			LOGGER.error("GetActivationCode ::" + e.getMessage());
		}

	}

	public String getCurrentActivationCode() {
		String currActCode = "";
		currActCode = currentActivationCode;
		return currActCode;
	}

	public Date getExpiredDate() {
		return expirationDate;
	}

}
