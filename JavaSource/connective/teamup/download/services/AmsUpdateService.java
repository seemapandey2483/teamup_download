package connective.teamup.download.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import connective.teamup.download.db.CarrierAmsGroupInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.registration.ws.objects.AmsGroupSummaryOutput;
import connective.teamup.registration.ws.objects.CarrierAmsGroupInput;

public class AmsUpdateService {

	public static Logger log =Logger.getLogger(ExportReportService.class);
	
	public static AmsGroupSummaryOutput getAmsSumarry(DatabaseOperation op,String carrierId) {
		AmsGroupSummaryOutput output = new AmsGroupSummaryOutput();
		List<CarrierAmsGroupInput> input = null;
		List<CarrierAmsGroupInfo> carrierAmsGroupInfoBean =null;
		CarrierAmsGroupInput inputBean = null;
		try{
			
			carrierAmsGroupInfoBean = op.getCarrierAmsGroupInfo();
			input = new ArrayList<CarrierAmsGroupInput>();
			
			for(CarrierAmsGroupInfo bean:carrierAmsGroupInfoBean) {
				inputBean = new CarrierAmsGroupInput();
				inputBean.setAmsId(bean.getAmsId());
				inputBean.setAgencyCount(bean.getAgencyCount());
				inputBean.setPartcipantCount(bean.getPartcipantCount());
				input.add(inputBean);
			}
			output.setCarrierId(carrierId);
			output.setCarrierAmsGroupInput(input);
			
		}catch(SQLException e){
			log.error("Unable to update AMSINFO JBO to RegAdmin server:" +e.getMessage() );
			output = null;
		}
		
		return output;
	}
}
