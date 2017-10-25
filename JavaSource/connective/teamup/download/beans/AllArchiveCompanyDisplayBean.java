package connective.teamup.download.beans;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

public class AllArchiveCompanyDisplayBean implements Serializable, DisplayBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CarrierInfo carrierInfo = null;
	@Override
	public void init(HttpServletRequest req, HttpServletResponse resp,
			ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws DisplayBeanException {
		// TODO Auto-generated method stub
		this.carrierInfo = serverInfo.getCarrierInfo();

	}

}
