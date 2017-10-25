package connective.teamup.download.actions;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.log4j.Logger;
import org.jdom2.Element;

import connective.teamup.download.db.XmlExport;
import connective.teamup.download.db.TransactionInfo;


public class XMLImportHelper {

	private static final Logger LOGGER = Logger.getLogger(XMLImportHelper.class);
	public static String agentId;
	
	public static String getNodeValue(Node node, String nodeName) {
		NodeList nList = node.getChildNodes();
		String val = null;
		if (node.getFirstChild() != null && nodeName.equals(node.getNodeName())) {
			return node.getFirstChild().getNodeValue();
		}
		for (int i = 0; i < nList.getLength(); i++) {
			Node n = nList.item(i);
			val = getNodeValue(n, nodeName);
			if (val != null) break;
		}
		return val;
	}
	public static XmlExport AgentMappingNode(List<XmlExport> claimMapList) {
		for(XmlExport expo: claimMapList) {
			if("agentId".equals(expo.getElementName())) {
				return expo;
			}
		}
		return null;
	}
	public static XmlExport MsgSequenceMappingNode(List<XmlExport> claimMapList) {
		for(XmlExport expo: claimMapList) {
			if("MsgSeqNumber".equals(expo.getElementName())) {
				return expo;
			}
		}
		return null;
	}
	public static boolean checkForMultipleAgent(List<Element> transactions,XmlExport agentMapping) throws Exception {
	String agentId ="";
	String prevAgentId ="";
	
	for(Element trans: transactions) {
		agentId = (String)getElementValue(trans,agentMapping.getNodeName(),agentMapping.getDataType());
		if("".equals(prevAgentId))
			prevAgentId = agentId;
		if(!prevAgentId.equals(agentId))
			return true;
		prevAgentId =  agentId;

	}
		XMLImportHelper.agentId = agentId;
		return false;
	}
	
	public static Object getElementValue(Element parent, String nodeName,String dataType) throws Exception {
		Object value = null; 

		if(nodeName != null && nodeName.contains(".")) {
			nodeName = nodeName.replace('.', '/');

			String dataNode = nodeName.substring(nodeName.lastIndexOf("/") +1);
			String nodeToTravese = nodeName.substring(0, nodeName.lastIndexOf("/"));
			String[] nodeSplit = nodeToTravese.split("/");

			Element tempParent = parent;
			Element e = null;
			for(String str:nodeSplit) {
				e = tempParent.getChild(str);
				if(e== null)
					return null;
				tempParent = e;
			}
			value = e.getChild(dataNode)!= null?e.getChild(dataNode).getText():null;			
		}else {
			value = parent.getChild(nodeName)!= null ?parent.getChild(nodeName).getText():null;
		}
		if(value != null)
			value = value.toString().trim();
		
		if("string".equals(dataType)) {
			return String.valueOf(value);
		}else if("int".equals(dataType)){
			try{
				return Integer.parseInt(String.valueOf(value));
			}catch(Exception e) {
				LOGGER.error("Invalid Value for node "+ nodeName +"::" + e.getMessage());
				throw  new Exception("Invalid Value for node "+ nodeName +"::" + e.getMessage());
			}
		}else{
			try{
				return Double.parseDouble(String.valueOf(value));
			}catch(Exception e) {
				LOGGER.error("Invalid Value for node "+ nodeName +"::" + e.getMessage());
				throw  new Exception("Invalid Value for node "+ nodeName +"::" + e.getMessage());
			}
		}
	}
	
	public static void printClaim(TransactionInfo claim) {

			System.out.print("  TransactionResponseDt:" + claim.getCreatedDate());
			System.out.print("  AgentId:" + claim.getAgentId());
			System.out.print("  Policy Number:" + claim.getPolicyNumber());
			System.out.print("  Claim Number:" + claim.getClaimInfo().getClaimNumber());
			System.out.print("  Policy Effective Date:" + claim.getPolicyEffDate());
			System.out.print("  Policy Expiration Date:" + claim.getPolicyExpirationDate());
			System.out.print("  lineofBusiness:" + claim.getLob());
			System.out.print("  lossDate:" + claim.getClaimInfo().getLossDate());
			System.out.print("  businessPurposeCode:" + claim.getClaimInfo().getBusinessPurposeCode());
			System.out.print("  transactionSeqNumber:" + claim.getTransSequence());
		//	System.out.print("  paymentTypeCd:" + claim.getPaymentTypeCd());
			//System.out.print("  totalPaymentAmt:" + claim.getTotalPaymentAmt());
			//System.out.print("  lossCauseCd:" + claim.getLossCauseCd());
			//System.out.print("  PaymentDt:" + claim.getPaymentDt());
			System.out.print("  commercialName:" + claim.getClaimInfo().getCommercialName());
			System.out.print("  PersonName:" + claim.getClaimInfo().getPersonalNameSurName() + "#" +claim.getClaimInfo().getPersonalNameGivenName());
			System.out.print("  Claim Status:" + claim.getClaimInfo().getStatus());
			System.out.print("  Claim Repored Date:" + claim.getClaimInfo().getReportedDate());
			System.out.print("  Claim Type Code:" + claim.getClaimInfo().getClaimTypeCd());
			System.out.println();

	}
}
