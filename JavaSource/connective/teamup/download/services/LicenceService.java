package connective.teamup.download.services;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ebix.licence.Licence;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

public class LicenceService {
	
	private static final Logger LOGGER = Logger.getLogger(LicenceService.class);

	public static final String CLAIM="CLAIM";
	public static final String POLICY="POLICY";
	
	public static boolean isClaimActivated(DatabaseOperation op) throws SQLException, Exception{
		boolean isClaimActivated = false;
		try{
			String actCode = op.getPropertyValue(DatabaseFactory.PROP_CARRIER_ACTIVATION);
			String carrierId = op.getPropertyValue(DatabaseFactory.PROP_CARRIER_DEPLOYMENTID);
			if(carrierId!= null && actCode !=null)
				isClaimActivated = isClaimActivated( actCode,  carrierId);
		}catch(SQLException se) {
			LOGGER.error(se);
			throw se;
		}catch(Exception e){
			LOGGER.error(e);
			throw e;
		}

		return isClaimActivated;
	}
	
	public static boolean isPolicyActivated(DatabaseOperation op) throws SQLException, Exception{
		boolean isPolicyActivated = false;
		try{
			String actCode = op.getPropertyValue(DatabaseFactory.PROP_CARRIER_ACTIVATION);
			String carrierId = op.getPropertyValue(DatabaseFactory.PROP_CARRIER_DEPLOYMENTID);
			isPolicyActivated = isPolicyActivated( actCode,  carrierId);
		}catch(SQLException se) {
			LOGGER.error(se);
			throw se;
		}catch(Exception e){
			LOGGER.error(e);
			throw e;
		}

		return isPolicyActivated;
	}
	
	public static boolean isClaimActivated(String actCode, String carrierId) throws SQLException, Exception{
		boolean isClaimActivated = false;
		try{
		
			Licence lic =  Licence.getLicence(actCode, carrierId);
			isClaimActivated = isClaimActivated( lic);
			if(lic.isExpired()){
				isClaimActivated = false;
			}
				
		}catch(Exception e){
			LOGGER.error(e);
			throw e;
		}

		return isClaimActivated;
	}
	public static boolean isPolicyActivated(String actCode, String carrierId) throws SQLException, Exception{
		boolean isPolicyActivated = false;
		try{
		
			Licence lic =  Licence.getLicence(actCode, carrierId);
			isPolicyActivated = isPolicyActivated( lic);
			if(lic.isExpired()){
				isPolicyActivated = false;
			}
				
		}catch(Exception e){
			LOGGER.error(e);
			throw e;
		}

		return isPolicyActivated;
	}
	public static boolean isClaimActivated(Licence lic){
		boolean activate = false;
		if(lic.isExpired()){
			return activate;
		}
		for(String act : lic.getActivefileTypeSupported()) {
			if(CLAIM.equals(act)) {
				activate = true; 
			}
		}
		return activate;
	}
	
	public static boolean isPolicyActivated(Licence lic){
		boolean activate = false;
		if(lic.isExpired()){
			return activate;
		}
		for(String act : lic.getActivefileTypeSupported()) {
			if(POLICY.equals(act)) {
				activate = true; 
			}
		}
		return activate;
	}
	public static Licence getCurrentLicence(String actCode, String carrierId) throws SQLException, Exception{
		Licence lic = null;
		try{
		
			 lic =  Licence.getLicence(actCode, carrierId);
							
		}catch(Exception e){
			LOGGER.error(e);
			throw e;
		}

		return lic;
	}
}
