package connective.teamup.download.services;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanToCsv;
import com.opencsv.bean.ColumnPositionMappingStrategy;

import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.LogTransInfoBO;

public class ExportReportService {
	
	public static Logger log =Logger.getLogger(ExportReportService.class);
	public static DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	public static boolean  isClaimAccessAllowed = true;
	public static String[] headersWithClaim = new String[]{"Agent Id", "Policy Number", "Claim Number", "LOB","Insured Name","Transaction Type","Activity Date","Activity Type"  };
	public static String[] headers = new String[]{"Agent Id", "Policy Number",  "LOB","Insured Name","Transaction Type","Activity Date","Activity Type"  };
	
	
	public static void createCSVFile(ByteArrayOutputStream outputStream,DatabaseOperation op,Long fromActMillSeconds,
			Long toActMillSeconds,String dlActivityType,String dlFileType) throws Exception{
		CSVWriter csvWriter = null;
		BeanToCsv<LogTransInfoBO> bc = new BeanToCsv<LogTransInfoBO>();
		List<LogTransInfoBO> lstLog = new ArrayList<LogTransInfoBO>();
		String[] columnMapping = null;
		

		ColumnPositionMappingStrategy<LogTransInfoBO> mappingStrategy  = new ColumnPositionMappingStrategy<LogTransInfoBO>();
		if(isClaimAccessAllowed)
			columnMapping = new String[] {"agentID", "policyNumber","claimNumber","lob","insuredName","txnType","eventDate","event_type"};
		else
			columnMapping = new String[] {"agentID", "policyNumber","lob","insuredName","txnType","eventDate","event_type"};
		
		mappingStrategy .setType(LogTransInfoBO.class);
		mappingStrategy .setColumnMapping(columnMapping);
		OutputStreamWriter out = new OutputStreamWriter(outputStream);
	    try {

	    	csvWriter = new CSVWriter(out);
	        lstLog = op.getTransactionDetailsForDailyReport(fromActMillSeconds,toActMillSeconds,dlActivityType,dlFileType);
	        bc.write(mappingStrategy,csvWriter,lstLog);
	       
	    }catch(Exception e){
	    	log.error("createCSVFile:Daily Report Email Job" +e.getMessage());
	    	throw e;
	    }finally{
	    		csvWriter.close();
	    		out.close();
	    }
        
	}
	
	
	
	public static void createWorkBook(ByteArrayOutputStream outputStream,DatabaseOperation op,Long fromActMillSeconds,
			Long toActMillSeconds,String dlActivityType,String dlFileType) throws Exception{
		Workbook workbook = new HSSFWorkbook();
		List<LogTransInfoBO> lstLog = new ArrayList<LogTransInfoBO>();
		Sheet sheet = workbook.createSheet("Transaction Report");
		
		Row row = null;
	    Cell cell = null;
	    int r = 0;
	    int c = 0;
	    String fromDate ="";
		String toDate ="";
		Date dt = null;
		String [] hdr = null;
		//Style for header cell
	    try {
	    	CellStyle style = workbook.createCellStyle();
	        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
	        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	        style.setAlignment(CellStyle.ALIGN_CENTER);
	        
	        dt = new Date(fromActMillSeconds);
	    	 fromDate = formatter.format(dt);
	    	 
	    	 dt = new Date(toActMillSeconds);
	    	 toDate = formatter.format(dt);
	    	 String text ="From Date :		" +fromDate + "		To Date:		"+toDate;
	    	 
	        Header title = sheet.getHeader();
	        title.setCenter(HSSFHeader.font("Arial", "Bold") +
	        		
	        HSSFHeader.fontSize((short) 10) + "Transaction Report");
	        title.setRight(text);
	        
	        //Create Title cells
	        row = sheet.createRow(r++);
	        
	        //Create header cells
	        row = sheet.createRow(r++);
	        
	      //Set Column widths
	         if(isClaimAccessAllowed){
	        	 hdr = headersWithClaim;
	         }else {
	        	 hdr =headers;
	         }
	        
	        //Create header cells
	         for (String header : hdr) {
	        	
		        	 cell = row.createCell(c++);
			 	        cell.setCellStyle(style);
			 	        cell.setCellValue(header);
	        		 
	            }
	 
	        
	        lstLog = op.getTransactionDetailsForDailyReport(fromActMillSeconds,toActMillSeconds,dlActivityType,dlFileType);
	        for(LogTransInfoBO log:lstLog){
	            row = sheet.createRow(r++);
	            c = 0;
	            row.createCell(c++).setCellValue(log.getAgentID());
	            row.createCell(c++).setCellValue(log.getPolicyNumber());
	            if(isClaimAccessAllowed){
	            	row.createCell(c++).setCellValue(log.getClaimNumber());	 
	        	 }
	            
	            row.createCell(c++).setCellValue(log.getLob());
	            row.createCell(c++).setCellValue(log.getInsuredName());
	            row.createCell(c++).setCellValue(log.getTxnType());
	            row.createCell(c++).setCellValue(log.getEventDate());
	            row.createCell(c++).setCellValue(log.getEvent_type());
	 
	        }
	        for(int i = 0 ; i < hdr.length; i++)
	            sheet.autoSizeColumn(i, true);
	        workbook.write(outputStream);
	    }catch(Exception e){
	    	log.error("createWorkBook:Daily Report Email Job" +e.getMessage());
	    	throw e;
	    }finally{
	    	  workbook.close();
	    }
        
      
	}

	public static void createPdf(ByteArrayOutputStream outputStream,DatabaseOperation op,Long fromActMillSeconds,
			Long toActMillSeconds,String dlActivityType,String dlFileType) throws Exception{
		
		List<LogTransInfoBO> lstLog = new ArrayList<LogTransInfoBO>();
		Document document = new Document();
		PdfWriter writer = null;
		PdfPCell cell = null;
		Font bold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		Font headerFont = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
		Font normal = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
		
		String fromDate ="";
		String toDate ="";
		Date dt = null;
		PdfPTable table = null;
		String [] hdr = null;
		//Style for header cell
	    try {
	    	 writer = PdfWriter.getInstance(document, outputStream);
	    	 document.open();
	    	 
	    	 Paragraph preface = new Paragraph("Daily Transaction Report",headerFont); 
	    	 preface.setAlignment(Element.ALIGN_CENTER);
	    	 document.add(preface);
	    	 
	    	  dt = new Date(fromActMillSeconds);
	    	 fromDate = formatter.format(dt);
	    	 
	    	 dt = new Date(toActMillSeconds);
	    	 toDate = formatter.format(dt);
	    	 String text ="From Date :		" +fromDate + "		To Date:		"+toDate;
	    	 Paragraph dateP = new Paragraph(text,bold);
	    	 dateP.setAlignment(Element.ALIGN_CENTER);
	    	 document.add(dateP);
	    	 if(isClaimAccessAllowed)
	    	  table = new PdfPTable(8); // 8 columns.
	    	 else 
	    		 table = new PdfPTable(7); // 7 columns.
	    	 
	         table.setWidthPercentage(100); //Width 100%
	         table.setSpacingBefore(1f); //Space before table
	         table.setSpacingAfter(1f); //Space after table
	    	 
	       //Set Column widths
	         if(isClaimAccessAllowed){
	        	 float[] columnWidths = {1f, 2f, 2f,1f,2f,1f,2f,1f};
	        	 table.setWidths(columnWidths);
	        	 hdr = headersWithClaim;
	         }else {
	        	 float[] columnWidths = {1f, 2f, 1f,2f,1f,2f,1f};
	        	 table.setWidths(columnWidths);
	        	 hdr =headers;
	         }
	         
	         
	    	
	 
	        //Create header cells
	         for (String header : hdr) {
	       		 cell = new PdfPCell();
		         cell.setBorderColor(BaseColor.BLUE);
		         cell.setPaddingLeft(10);
		   	     cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		   	     cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		   	     cell.setPhrase(new Phrase(header, bold));
		   	     table.addCell(cell); 
	            }
	         
	         table.completeRow();
	         
	        lstLog = op.getTransactionDetailsForDailyReport(fromActMillSeconds,toActMillSeconds,dlActivityType,dlFileType);
	        for(LogTransInfoBO log:lstLog){
	        	
	        		cell = new PdfPCell();
                    cell.setPhrase(new Phrase(log.getAgentID(), normal));
                    table.addCell(cell);
                    
                    cell = new PdfPCell();
                    cell.setPhrase(new Phrase(log.getPolicyNumber(), normal));
                    table.addCell(cell);
                    if(isClaimAccessAllowed){
                    	cell = new PdfPCell();
                    	cell.setPhrase(new Phrase(log.getClaimNumber(), normal));
                    	table.addCell(cell);
                    }
                    cell = new PdfPCell();
                    cell.setPhrase(new Phrase(log.getLob(), normal));
                    table.addCell(cell);
                    
                    cell = new PdfPCell();
                    cell.setPhrase(new Phrase(log.getInsuredName(), normal));
                    table.addCell(cell);
                    
                    cell = new PdfPCell();
                    cell.setPhrase(new Phrase(log.getTxnType(), normal));
                    table.addCell(cell);
                    
                    cell = new PdfPCell();
                    cell.setPhrase(new Phrase(log.getEventDate(), normal));
                    table.addCell(cell);
                    
                    cell = new PdfPCell();
                    cell.setPhrase(new Phrase(log.getEvent_type(), normal));
                    table.addCell(cell);
	        	
	            
	 
	        }
	        document.addTitle("Daily Transaction Report");
            document.add(table);
	        
	    }catch(Exception e){
	    	log.error("createPdf:Daily Report Email Job" +e.getMessage());
	    	throw e;
	    }finally{
	    	document.close();
	    	writer.close();
	    }
        
      
	}

public static byte[]  exportReportToCsv(DatabaseOperation op,Long fromActMillSeconds,Long toActMillSeconds) throws Exception{
		
		ByteArrayOutputStream outputStream = null;
		byte[] bytes = null;
		try{
			outputStream = new ByteArrayOutputStream();
			ExportReportService.createCSVFile(outputStream,op,fromActMillSeconds,toActMillSeconds, null, null);
			 bytes = outputStream.toByteArray();
			
			
		}catch(Exception e){
			log.error("exportReportToCsv:Daily Report excel creation " +e.getMessage());
		}finally{
			if(null != outputStream) {
                try { outputStream.close(); outputStream = null; }
                catch(Exception ex) { }
            }
		}
		return  bytes;
	}	
	
public static byte[]  exportReportToExcel(DatabaseOperation op,Long fromActMillSeconds,Long toActMillSeconds) throws Exception{
		
		ByteArrayOutputStream outputStream = null;
		byte[] bytes = null;
		try{
			outputStream = new ByteArrayOutputStream();
			ExportReportService.createWorkBook(outputStream,op,fromActMillSeconds,toActMillSeconds, null, null);
			 bytes = outputStream.toByteArray();
			
			
		}catch(Exception e){
			log.error("exportReportToExcel:Daily Report excel creation " +e.getMessage());
		}finally{
			if(null != outputStream) {
                try { outputStream.close(); outputStream = null; }
                catch(Exception ex) { }
            }
		}
		return  bytes;
	}
public static byte[] exportReportToPdf(DatabaseOperation op,Long fromActMillSeconds,Long toActMillSeconds) throws Exception{
	
	ByteArrayOutputStream outputStream = null;
	byte[] bytes = null;
	try{
		outputStream = new ByteArrayOutputStream();
		ExportReportService.createPdf(outputStream,op,fromActMillSeconds,toActMillSeconds, null, null);
		bytes = outputStream.toByteArray();
		
	}catch(Exception e){
		log.error("exportReportToPdf:Daily Report pdf creation" +e.getMessage());
	}finally{
		if(null != outputStream) {
            try { outputStream.close(); outputStream = null; }
            catch(Exception ex) { }
        }
	}
	return  bytes;
}
public static void exportReportToExcelAndSendEmail(DatabaseOperation op,Long fromActMillSeconds,Long toActMillSeconds,String emailId,String dlActivityType,String dlFileType) throws Exception{
		
		ByteArrayOutputStream outputStream = null;
		try{
			outputStream = new ByteArrayOutputStream();
			createWorkBook(outputStream,op,fromActMillSeconds,toActMillSeconds, dlActivityType, dlFileType);
			byte[] bytes = outputStream.toByteArray();
			String attachmentType ="application/vnd.ms-excel";
			String attachmentName ="dailyDetailedTransReport.xls";
			
			Date dt = null;
			String subject ="Daily File Transaction Activity report ";
			String textMsg ="<B>Please find attached Daily File Transaction Activity Report</B> </BR>";
			
			 dt = new Date(fromActMillSeconds);
			 String fromDate = formatter.format(dt);
	   	 
		   	 dt = new Date(toActMillSeconds);
		   	 String toDate = formatter.format(dt);
		   	 String text ="From: "+fromDate +" To: "+toDate;
	   	 
		   	 dt = new Date(System.currentTimeMillis());
		   	 String reportRunDate = formatter.format(dt);
	   	 
			subject = subject + text;
			textMsg =textMsg+ "<B>From Date :		</B> " +fromDate +"<BR>";
			textMsg =textMsg+ "<B> To Date :		</B> " +fromDate +"<BR>";	
			textMsg =textMsg+ "<B>Report Run Date :	<B> " +reportRunDate +"<BR>";
			
			EmailService.getInstance().sendDailyReportEMail(emailId, subject, null, textMsg, outputStream, attachmentName, attachmentType);
			
		}catch(Exception e){
			log.error("exportReportToExcelAndSendEmail:Daily Report Email Job" +e.getMessage());
		}finally{
			if(null != outputStream) {
                try { outputStream.close(); outputStream = null; }
                catch(Exception ex) { }
            }
		}
      
	}
public static void exportReportToPdfAndSendEmail(DatabaseOperation op,Long fromActMillSeconds,Long toActMillSeconds,String emailId,String dlActivityType,String dlFileType) throws Exception{
	
	ByteArrayOutputStream outputStream = null;
	try{
		outputStream = new ByteArrayOutputStream();
		createPdf(outputStream,op,fromActMillSeconds,toActMillSeconds, dlActivityType, dlFileType);
		byte[] bytes = outputStream.toByteArray();
		String attachmentType ="application/pdf";
		String attachmentName ="dailyDetailedTransReport.pdf";
		
		Date dt = null;
		String subject ="Daily File Transaction Activity report ";
		String textMsg ="<B>Please find attached Daily File Transaction Activity Report</B> </BR>";
		
		 dt = new Date(fromActMillSeconds);
		 String fromDate = formatter.format(dt);
  	 
	   	 dt = new Date(toActMillSeconds);
	   	 String toDate = formatter.format(dt);
	   	 String text ="From: "+fromDate +" To: "+toDate;
  	 
	   	 dt = new Date(System.currentTimeMillis());
	   	 String reportRunDate = formatter.format(dt);
  	 
		subject = subject + text;
		textMsg =textMsg+ "<B>From Date :</B> " +fromDate +"<BR>";
		textMsg =textMsg+ "<B> To Date :</B> " +fromDate +"<BR>";	
		textMsg =textMsg+ "<B>Report Run Date :	<B> " +reportRunDate +"<BR>";
		EmailService.getInstance().sendDailyReportEMail(emailId, subject, null, textMsg, outputStream, attachmentName, attachmentType);
		
	}catch(Exception e){
		log.error("exportReportToPdfAndSendEmail:Daily Report Email Job" +e.getMessage());
	}finally{
		if(null != outputStream) {
            try { outputStream.close(); outputStream = null; }
            catch(Exception ex) { }
        }
	}
  
}
public static void exportReportToCSVAndSendEmail(DatabaseOperation op,Long fromActMillSeconds,Long toActMillSeconds,String emailId,String dlActivityType,String dlFileType) throws Exception{
	
	ByteArrayOutputStream outputStream = null;
	try{
		outputStream = new ByteArrayOutputStream();
		createCSVFile(outputStream,op,fromActMillSeconds,toActMillSeconds, dlActivityType, dlFileType);
		byte[] bytes = outputStream.toByteArray();
		String attachmentType ="text/csv";
		String attachmentName ="dailyDetailedTransReport.csv";
		Date dt = null;
		String subject ="Daily File Transaction Activity report ";
		String textMsg ="<B>Please find attached Daily File Transaction Activity Report</B> </BR>";
		
		 dt = new Date(fromActMillSeconds);
		 String fromDate = formatter.format(dt);
 	 
	   	 dt = new Date(toActMillSeconds);
	   	 String toDate = formatter.format(dt);
	   	String text ="From: "+fromDate +" To: "+toDate;
 	 
	   	 dt = new Date(System.currentTimeMillis());
	   	 String reportRunDate = formatter.format(dt);
 	 
		subject = subject + text;
		textMsg =textMsg+ "<B>From Date :		</B> " +fromDate +"<BR>";
		textMsg =textMsg+ "<B> To Date :		</B> " +fromDate +"<BR>";	
		textMsg =textMsg+ "<B>Report Run Date :	<B> " +reportRunDate +"<BR>";
		
		EmailService.getInstance().sendDailyReportEMail(emailId, subject, null, textMsg, outputStream, attachmentName, attachmentType);
		
	}catch(Exception e){
		log.error("exportReportToCSVAndSendEmail:Daily Report Email Job" +e.getMessage());
	}finally{
		if(null != outputStream) {
            try { outputStream.close(); outputStream = null; }
            catch(Exception ex) { }
        }
	}
  
}
}
