package connective.teamup.download.actions;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * SAX event handler for parsing an ACORD XML file and storing it the download database.
 * 
 * @author kmccreary
 */
public class AcordXmlHandler extends DefaultHandler
{
	private static final Logger LOGGER = Logger.getLogger(AcordXmlHandler.class);
	
	private AcordXmlImportHelper importAction = null;
	private StringBuffer curbuf = null;
	private String cdata = null;
	private String header = null;
	private String footer = null;
	private String curTrans = null;
	private String curAgg = null;
	private String agentId = null;
	private String msgSeq = null;
	private String transSeq = null;
	private String transEff = null;
	private String lob = null;
	private String policyEff = null;
	private String policyNum = null;
	private String customerId = null;
	private String insuredName = null;
	private String insFirstName = null;
	private String insLastName = null;
	private String transDesc = null;
	private double premiumAmount = 0.0;
	
	private boolean fileStarted = false;

	private String[] transTypeList = { "ClaimDownloadRs",
									   "ClaimsNotificationAddRs",
									   "ClaimStatusInqRs" };

	private String[] specialAggList = { "ProducerInfo",
										"Policy",
										"InsuredOrPrincipal",
										"ClaimsPayment" };
	private HashMap specialAggHash = null;


	/**
	 * Constructor for AcordXmlHandler.
	 */
	public AcordXmlHandler(AcordXmlImportHelper importAction)
	{
		super();
		
		this.importAction = importAction;
		this.curbuf = new StringBuffer();
		this.curAgg = "";
		
		specialAggHash = new HashMap();
		for (int i=0; i < specialAggList.length; i++)
			specialAggHash.put(specialAggList[i], specialAggList[i]);
	}

	public void characters(char[] ch, int start, int length) throws SAXException
	{
		String tmp = new String(ch, start, length);
		if (tmp == null)
			tmp = "";
		cdata += tmp;
		
		// add characters to the current text buffer
		curbuf.append(tmp);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		cdata = "";
		if (qName != null && !qName.equals(""))
		{
			if (curTrans == null)
			{
				for (int i=0; i < transTypeList.length; i++)
				{
					if (qName.equalsIgnoreCase(transTypeList[i]))
					{
						curTrans = qName;
						if (header == null)
						{
							header = curbuf.toString();
							curbuf = new StringBuffer();
						}
						break;
					}
				}
			}
			else if (curAgg.equals("") && specialAggHash.get(qName) != null)
				curAgg = qName;
			
			curbuf.append("<");
			curbuf.append(qName);
			curbuf.append(">");
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		try
		{
			if (qName != null && !qName.equals(""))
			{
				if (qName.equalsIgnoreCase("ACORD"))
				{
					if (curTrans != null)
					{
						// save transaction to the database
						importAction.saveTransaction(insFirstName, customerId, lob, policyEff, 
													 policyNum, curTrans, transSeq, transEff, 
													 transDesc, curbuf.toString(), premiumAmount);
						
						curTrans = null;
						curbuf = new StringBuffer();
					}
					
					curbuf.append("</");
					curbuf.append(qName);
					curbuf.append(">");
					footer = curbuf.toString();
					
					// save file footer info to database, set file complete to true
					importAction.updateFileHeader(footer);
					
					curbuf = new StringBuffer();
					fileStarted = false;
					header = null;
					footer = null;
					curTrans = null;
					curAgg = "";
					agentId = null;
					msgSeq = null;
					transSeq = null;
					transEff = null;
					lob = null;
					policyEff = null;
					policyNum = null;
					insuredName = null;
					insFirstName = null;
					insLastName = null;
					customerId = null;
					transDesc = null;
					premiumAmount = 0.0;
				}
				else
				{
					curbuf.append("</");
					curbuf.append(qName);
					curbuf.append(">");
					
					if (curTrans != null && qName.equals(curTrans))
					{
						if (!fileStarted)
						{
							// save file header info to the database
							importAction.saveFileHeader(header, agentId, msgSeq);
							
							fileStarted = true;
						}
						
						if (insuredName == null && insFirstName != null && insLastName != null)
							insuredName = insFirstName + " " + insLastName;
						
						// save transaction to the database
						importAction.saveTransaction(insFirstName, customerId, lob, policyEff, 
													 policyNum, curTrans, transSeq, transEff, 
													 transDesc, curbuf.toString(), premiumAmount);
						
						curTrans = null;
						transSeq = null;
						transEff = null;
						lob = null;
						policyEff = null;
						policyNum = null;
						insuredName = null;
						insFirstName = null;
						insLastName = null;
						customerId = null;
						transDesc = null;
						premiumAmount = 0.0;
						
						curbuf = new StringBuffer();
						curAgg = "";
					}
					else if (!curAgg.equals("") && qName.equals(curAgg))
						curAgg = "";
					else if (curAgg.equals("ProducerInfo") && qName.equalsIgnoreCase("ContractNumber") && agentId == null)
						agentId = cdata;
					else if (qName.equalsIgnoreCase("MsgSeqNumber") && msgSeq == null)
						msgSeq = cdata;
					else if (qName.equalsIgnoreCase("TransactionSeqNumber") && transSeq == null)
						transSeq = cdata;
					else if (qName.equalsIgnoreCase("TransactionResponseDt") && transEff == null)
						transEff = cdata;
					else if (curAgg.equals("Policy"))
					{
						if (qName.equalsIgnoreCase("PolicyNumber") && policyNum == null)
							policyNum = cdata;
						else if (qName.equalsIgnoreCase("LOBCd") && lob == null)
							lob = cdata;
						else if (qName.equalsIgnoreCase("EffectiveDt") && policyEff == null)
							policyEff = cdata;
					}
					else if (curAgg.equals("InsuredOrPrincipal"))
					{
						if (qName.equalsIgnoreCase("CommercialName") && insuredName == null)
							insuredName = cdata;
						else if (qName.equalsIgnoreCase("GivenName") && insFirstName == null)
							insFirstName = cdata;
						else if (qName.equalsIgnoreCase("Surname") && insLastName == null)
							insLastName = cdata;
					}
				}
			}
		} catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new SAXException(e);
		}
	}
}
