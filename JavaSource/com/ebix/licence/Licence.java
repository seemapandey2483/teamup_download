package com.ebix.licence;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import connective.teamup.download.db.LZString;


public class Licence implements Serializable{

	private static final long serialVersionUID = 1L;
    private String carrierId;
	private Date expirationDate;
	private List<String> activefileTypeSupported;
	private static final String DELIM = "\t";
	private static final String ERROR_MESSAGE = "Invalid Activation Code.";
	public static final SimpleDateFormat MM_DD_YYY = new SimpleDateFormat("MM/dd/yyyy");
	public static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");


	public static Licence getLicence(String actCode, String carrierId) throws Exception{
		String deactivationCode = LZString.decompressFromBase64(actCode);//unobsfucate(actCode,carrierId	);
		Licence lic = new Licence();

		String [] code = deactivationCode.split(DELIM);
		lic.carrierId = code[0];
		lic.expirationDate = parseDate(code[1], MM_DD_YYY);
		List<String> fileType = new ArrayList<String>();
		for (int i=2;i<code.length;i++){
			fileType.add(code[i]);
		}
		lic.activefileTypeSupported = fileType;
		return lic;
		}
	
	public static String generateActivationCode(String carrierId, Date expirationDate,
			 Collection<String> fileTypeSupported) {
				Licence l = new Licence();
					l.carrierId =carrierId;
					l.expirationDate = expirationDate;
					l.activefileTypeSupported = new ArrayList<String>();
					for (String line : fileTypeSupported) {
						l.activefileTypeSupported.add( line );
					}
					return l.getActivationCode();
}
	
	public static void main(String[] args) throws Exception{
		Collection<String> locs = new ArrayList<String>();
		for (int i = 2; i < args.length; i++) {
			locs.add(args[i]);
		}
		
		String activationCode = generateActivationCode(	args[0],parseDate(args[1],MM_DD_YYY),	locs	);
		System.out.println( "Activation Code: "				+ 	activationCode	);
		String deactivationCode = LZString.decompressFromBase64(activationCode);
		System.out.println( "DeActivation Code: "				+ 	deactivationCode);

		String [] code = deactivationCode.split(DELIM);
		Licence l = new Licence();
		l.carrierId = code[0];
		l.expirationDate = parseDate(code[1], MM_DD_YYY);
		System.out.println("l.carrierId " +l.carrierId );
		System.out.println("l.expirationDate " +l.expirationDate );
		List<String> fileType = new ArrayList<String>();
		for (int i=2;i<code.length;i++){
			fileType.add(code[i]);
		}
		l.activefileTypeSupported = fileType;
		System.out.println("l.fileTypeSupported " +l.activefileTypeSupported );
		System.out.println(l.getDaysUntilExpiration());
		System.out.println(l.isExpired());
	}
	
	
	
	/*private static String obsfucate(String readable, String clientName) {
		try {
		    return crypto.encrypt( readable, clientName );
	    } catch (InvalidKeyException e) {
	    	System.out.println(e);
	        return null;
	    } catch (BadPaddingException e) {
	    	System.out.println(e);
	        return null;
	    } catch (IllegalBlockSizeException e) {
	    	System.out.println(e);
	        return null;
	    }
	}
	
	private static String unobsfucate(String unreadable, String clientName ) {
		try {
            return crypto.decrypt( unreadable, clientName );
        } catch (InvalidKeyException e) {
        	System.out.println(e);
            return null;
        } catch (BadPaddingException e) {
        	System.out.println(e);
            return null;
        } catch (IllegalBlockSizeException e) {
        	System.out.println(e);
            return null;
        }
	}*/
	
	public String getActivationCode() {
		if (carrierId == null) {
			throw new IllegalStateException("Can't generate an activation code -- this licence has no 'Carrier Id'.");
		}
		if (expirationDate == null) {
			throw new IllegalStateException("Can't generate an activation code -- this licence has no 'expiration date'.");
		}
		
		if (activefileTypeSupported == null || activefileTypeSupported.size() == 0) {
			throw new IllegalStateException("Can't generate an activation code -- this licence has no 'active lines of coverage'.");
		}

		StringBuffer sb = new StringBuffer();
		sb.append(carrierId);
		sb.append(DELIM);
		sb.append(MM_DD_YYY.format(expirationDate));
		if (activefileTypeSupported != null) {
			for (Iterator<String> iter = activefileTypeSupported.iterator(); iter.hasNext(); ) {
				Object next = iter.next();
				if (next instanceof String) {
					sb.append(DELIM);
					sb.append(((String)next));
				}
			}
		}
		System.out.println("Generating activation code from licence: " + sb.toString());
		//log.debug("Generating activation code from licence: " + sb.toString());
		return LZString.compressToBase64(sb.toString());
		//return obsfucate(sb.toString(), this.getCarrierId() );
	}

	public String getCarrierId() {
		return carrierId;
	}

	
	public Date getExpirationDate() {
		return expirationDate;
	}

	
	public List<String> getActivefileTypeSupported() {
		return activefileTypeSupported;
	}

	public static Date formatAndParseBack(Date date, SimpleDateFormat s) {
		Date d = null;
		try {
			d = s.parse(s.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
			/* do nothing */
		}
		return d;
	}
	public boolean isExpired() {
		if (expirationDate == null) {
			return true;
		}
		
		Date expDate = formatAndParseBack(expirationDate, YYYYMMDD);
		Date today = formatAndParseBack(new Date(), YYYYMMDD);
		
		return expDate.before(today);
	}
	
	/**
	 * Calculates the number of days remaining before the activation code
	 * expires.  If the expiration date has been reached, then it returns a
	 * negative number.
	 * 
	 * @return the number of days remaining before the activation code expires. 
	 */
	public Long getDaysUntilExpiration() {
		if (isExpired()) {
			return -1L;
		}
		
		Date expDate = formatAndParseBack(expirationDate, YYYYMMDD);
		Date today = formatAndParseBack(new Date(), YYYYMMDD);
		
		Long days =  expDate.getTime() - today.getTime();
		
		if (days > 0 ) {
			return days / (1000 * 60 * 60 * 24);
		} else if (days == 0 ) {
			return 0L;
		} else {
			return -1L;
		}
	}
	public Date getExpDateWithNoTime() {
		if (expirationDate != null) {
			return formatAndParseBack(expirationDate, YYYYMMDD);
		} else {
			return expirationDate;
		}
	}
	
	public static Date parseDate(String str, SimpleDateFormat sdf) {
		Date result = null;
		if (str != null && str.length() > 0) {
			try {
				if(sdf == null)
					result = MM_DD_YYY.parse(str);
				else
					result = sdf.parse(str);
			} catch (ParseException e) {
				System.out.println("An exception occurred when parsing the date.  Using SimpleDateFormat("
						+ str.toString()
						+ ").  Returned NULL Date object and it will create problems!"+ e);
				
			}
		}
		return result;
	}
}
