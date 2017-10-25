/*
 * 05/24/2005 - Created
 */
package connective.teamup.download.services;

import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import connective.teamup.download.Escape;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.download.ws.objects.DownloadTransactionInfo;

/**
 * @author Kyle W. McCreary
 */
public class DlFileHandler extends DefaultHandler
{
	private static final Logger LOGGER = Logger.getLogger(DlFileHandler.class);
	
	private ArrayList fileList = new ArrayList();
	private ArrayList transList = new ArrayList();
	private DownloadFileInfo currentFile = null;
	private DownloadTransactionInfo currentTransaction = null;
	private String cdata = null;


	/**
	 * Default constructor for DlFileHandler.
	 */
	public DlFileHandler()
	{
		super();
		
		fileList = new ArrayList();
	}

	public void characters(char[] ch, int start, int length) throws SAXException
	{
		String tmp = new String(ch, start, length);
		if (tmp == null)
			tmp = "";
		cdata += tmp;		
	}	

	/**
	 * Returns the array of download file beans.
	 */
	public DownloadFileInfo[] getDownloadFiles()
	{
		DownloadFileInfo[] files = new DownloadFileInfo[fileList.size()];
		fileList.toArray(files);
		return files;
	}

	public String writeToXML(DownloadFileInfo[] files)
	{
		String xml = "";
		if (files != null)
		{
			StringWriter sw = new StringWriter();
			for (int i=0; i < files.length; i++)
			{
				sw.write("<file>");
				sw.write("<filename>" + Escape.forXML(files[i].getFilename()) + "</filename>");
				sw.write("<origname>" + Escape.forXML(files[i].getOriginalFilename()) + "</origname>");
				sw.write("<status>" + files[i].getStatus() + "</status>");
				sw.write("<partcode>" + files[i].getParticipantCode() + "</partcode>");
				sw.write("<created>" + String.valueOf(files[i].getCreatedDate()) + "</created>");
				sw.write("<imported>" + String.valueOf(files[i].getImportedDate()) + "</imported>");
				sw.write("<lastdl>" + String.valueOf(files[i].getLastDownloadedDate()) + "</lastdl>");
				sw.write("<batchnum>" + String.valueOf(files[i].getBatchNumber()) + "</batchnum>");
				sw.write("<msgseq>" + String.valueOf(files[i].getMsgSeq()) + "</msgseq>");
				sw.write("<transcount>" + String.valueOf(files[i].getTransactionCount()) + "</transcount>");
				sw.write("<filesize>" + String.valueOf(files[i].getFileSize()) + "</filesize>");
				
				DownloadTransactionInfo[] trans = files[i].getTransactions();
				if (trans != null)
				{
					for (int j=0; j < trans.length; j++)
					{
						sw.write("<transaction>");
						sw.write("<customerid>" + Escape.forXML(trans[j].getCustomerId()) + "</customerid>");
						sw.write("<desc>" + Escape.forXML(trans[j].getDescription()) + "</desc>");
						sw.write("<dataformat>" + trans[j].getDataFormat() + "</dataformat>");
						sw.write("<dlstatus>" + trans[j].getDlStatus() + "</dlstatus>");
						sw.write("<insuredname>" + Escape.forXML(trans[j].getInsuredName()) + "</insuredname>");
						sw.write("<lob>" + trans[j].getLob() + "</lob>");
						sw.write("<effdt>" + trans[j].getPolicyEffDate() + "</effdt>");
						sw.write("<polnum>" + Escape.forXML(trans[j].getPolicyNumber()) + "</polnum>");
						sw.write("<transdt>" + trans[j].getTransactionEffDate() + "</transdt>");
						sw.write("<transtype>" + trans[j].getTransactionType() + "</transtype>");
						sw.write("<seq>" + trans[j].getSequence() + "</seq>");
						sw.write("<transseq>" + String.valueOf(trans[j].getTransactionSeq()) + "</transseq>");
						sw.write("<lastdl>" + String.valueOf(trans[j].getLastDownloadDate()) + "</lastdl>");
						sw.write("<premium>" + String.valueOf(trans[j].getTransactionPremium()) + "</premium>");
						sw.write("</transaction>");
					}
				}
				sw.write("</file>");
			}
			xml = sw.toString();
		}
		
		return xml;
	}

	public void endElement(String namespace, String simpleName, String qualifiedName) throws org.xml.sax.SAXException
	{
		if (qualifiedName.equals("file"))
		{
			// Save the transaction list to the current file
			DownloadTransactionInfo[] tl = new DownloadTransactionInfo[transList.size()];
			transList.toArray(tl);
			currentFile.setTransactions(tl);
			
			// save the current file to the list
			fileList.add(currentFile);
			
			// reset all pointers
			transList.clear();
			currentFile = null;
			currentTransaction = null;
		}
		else if (qualifiedName.equals("transaction"))
		{
			// add the current transaction to the list
			transList.add(currentTransaction);
			currentTransaction = null;
		}
		else if (currentTransaction != null)
		{
			if (qualifiedName.equals("customerid"))
			{
				currentTransaction.setCustomerId(cdata);
			}
			else if (qualifiedName.equals("desc"))
			{
				currentTransaction.setDescription(cdata);
			}
			else if (qualifiedName.equals("dataformat"))
			{
				currentTransaction.setDataFormat(cdata);
			}
			else if (qualifiedName.equals("dlstatus"))
			{
				currentTransaction.setDlStatus(cdata);
			}
			else if (qualifiedName.equals("insuredname"))
			{
				currentTransaction.setInsuredName(cdata);
			}
			else if (qualifiedName.equals("lob"))
			{
				currentTransaction.setLob(cdata);
			}
			else if (qualifiedName.equals("effdt"))
			{
				currentTransaction.setPolicyEffDate(cdata);
			}
			else if (qualifiedName.equals("polnum"))
			{
				currentTransaction.setPolicyNumber(cdata);
			}
			else if (qualifiedName.equals("transdt"))
			{
				currentTransaction.setTransactionEffDate(cdata);
			}
			else if (qualifiedName.equals("transtype"))
			{
				currentTransaction.setTransactionType(cdata);
			}
			else if (qualifiedName.equals("seq"))
			{
				currentTransaction.setSequence(Integer.parseInt(cdata));
			}
			else if (qualifiedName.equals("transseq"))
			{
				currentTransaction.setTransactionSeq(Integer.parseInt(cdata));
			}
			else if (qualifiedName.equals("lastdl"))
			{
				currentTransaction.setLastDownloadDate(Long.parseLong(cdata));
			}
			else if (qualifiedName.equals("premium"))
			{
				currentTransaction.setTransactionPremium(Double.parseDouble(cdata));
			}
		}
		else
		{
			if (qualifiedName.equals("filename"))
			{
				currentFile.setFilename(cdata);
			}
			else if (qualifiedName.equals("origname"))
			{
				currentFile.setOriginalFilename(cdata);
			}
			else if (qualifiedName.equals("status"))
			{
				currentFile.setStatus(cdata);
			}
			else if (qualifiedName.equals("partcode"))
			{
				currentFile.setParticipantCode(cdata);
			}
			else if (qualifiedName.equals("created"))
			{
				currentFile.setCreatedDate(Long.parseLong(cdata));
			}
			else if (qualifiedName.equals("imported"))
			{
				currentFile.setImportedDate(Long.parseLong(cdata));
			}
			else if (qualifiedName.equals("lastdl"))
			{
				currentFile.setLastDownloadedDate(Long.parseLong(cdata));
			}
			else if (qualifiedName.equals("batchnum"))
			{
				currentFile.setBatchNumber(Integer.parseInt(cdata));
			}
			else if (qualifiedName.equals("msgseq"))
			{
				currentFile.setMsgSeq(Integer.parseInt(cdata));
			}
			else if (qualifiedName.equals("transcount"))
			{
				currentFile.setTransactionCount(Integer.parseInt(cdata));
			}
			else if (qualifiedName.equals("filesize"))
			{
				currentFile.setFileSize(Integer.parseInt(cdata));
			}
		}
	}

	public void startElement(String namespace, String simpleName, String qualifiedName, Attributes attr) throws org.xml.sax.SAXException
	{
		cdata = "";
		
		try
		{
			if (qualifiedName.equals("file"))
			{
				currentFile = new DownloadFileInfo();
			}
			else if (qualifiedName.equals("transaction"))
			{
				currentTransaction = new DownloadTransactionInfo();
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			System.out.println(e.getMessage());
		}
	}

}
