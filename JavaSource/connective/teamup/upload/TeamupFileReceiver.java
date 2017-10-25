package connective.teamup.upload;

/**
 * Insert the type's description here.
 * Creation date: (7/1/2002 8:03:50 AM)
 * @author: Kyle McCreary
 */
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

public class TeamupFileReceiver
{
	private static final Logger LOGGER = Logger.getLogger(TeamupFileReceiver.class);
	
	private String filePath = null;
	private String fileName = null;
	
	
	/**
	 * TeamupFileReceiver constructor comment.
	 */
	public TeamupFileReceiver(String newFilePath)
	{
		super();
	
		fileName = "";
		filePath = "";
		
		if (newFilePath != null)
		{
			StringBuffer buff = new StringBuffer("");
			
			for (int i=0; i < newFilePath.length(); i++)
			{
				if (newFilePath.charAt(i) == '/')
					buff.append("\\");
				else
					buff.append(newFilePath.charAt(i));
			}
			filePath = buff.toString();
	
			if (filePath.length() > 0 && filePath.charAt(filePath.length() - 1) != '\\')
				filePath += "\\";
		}
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/1/2002 8:19:00 AM)
	 * @return java.lang.String
	 */
	public java.lang.String getFileName()
	{
	    return fileName;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/1/2002 8:09:58 AM)
	 * @return boolean
	 * @param text java.lang.String
	 */
	public boolean saveFile(String user, String text)
	{
		boolean fileSaved = false;
	
		fileName = "";
	
		if (text == null || text.length() == 0)
			return false;
	
		try
		{
			String fileExt;
			int numFiles;
	
			fileName = "vmtu_";
			if (user == null || user.trim().length() == 0)
				fileName += "al3.";
			else
			{
				for (int i=0; i < user.length(); i++)
				{
					if (user.charAt(i) != ' ')
						fileName += user.charAt(i);
				}
				fileName += ".";
			}
	
			File nof = new File(filePath);
			File totalFiles[] = nof.listFiles();
			numFiles = totalFiles.length;
	
			fileName = filePath + setFileName(fileName, totalFiles);
			
			// Strip any escape sequencing enforced by the upload control
			text = stripEscapeSequencing(text);
	
			// Create and write the file
			RandomAccessFile outFile = new RandomAccessFile(fileName, "rw");
			outFile.writeBytes(text);
			outFile.close();
	
	
			fileSaved = true;
		}
		catch (IOException e)
		{
			LOGGER.error(e);
			System.out.println(e.getMessage());
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			System.out.println(e.getMessage());
		}
	
		
		return fileSaved;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (7/1/2002 8:09:58 AM)
	 * @return boolean
	 * @param text java.lang.String
	 */
	private String setFileName(String file, File dir[])
	{
		String fileExt;
		int fnLen = file.length();
	
		String tempFiles = "";
		for (int i=0; i < dir.length; i++)
		{
			String name = dir[i].getName();
			if (name.length() > fnLen && name.substring(0, fnLen).equalsIgnoreCase(file))
				tempFiles += name + "~";
		}
	
		boolean done = false;
		int count = 0;
		while (!done)
		{
			fileExt = String.valueOf(count++).trim();
			while (fileExt.length() < 3)
				fileExt = "0" + fileExt;
	
			if (tempFiles.indexOf(file + fileExt + "~") < 0)
			{
				fileName = file + fileExt;
				done = true;
			}
		}
	
		return fileName;
	}
	
	/**
	 * Strips out all escape sequencing enforced by the TEAM-UP upload control.
	 * 
	 * @param uploadData The AL3 data uploaded from the activeX control
	 * 
	 * @return java.lang.String
	 */
	public String stripEscapeSequencing(String uploadData)
	{
		String data = "";
		
		if (uploadData != null)
			data = uploadData;

		// Convert any '|~`' back to ampersand
		int n = data.indexOf("|~`");
		while (n >= 0)
		{
			data = data.substring(0, n) + "&" + data.substring(n+3);
			n = data.indexOf("|~`");
		}
		
		return data;
	}

	/**
	 * Removes the null character (ASCII char zero) from text files.
	 * Added 12/17/2003, kwm.
	 * 
	 * @param uploadData The AL3 data uploaded from the activeX control
	 * 
	 * @return java.lang.String
	 */
	public String removeBinaries(String uploadData)
	{
		if (uploadData == null)
			return "";
		
		char space = ' ';
		StringBuffer data = new StringBuffer();
		for (int i=0; i < uploadData.length(); i++)
		{
			char ch = uploadData.charAt(i);
			
			if (ch == 0)
				data.append(space);
			else
				data.append(ch);
		}
		
		return data.toString();
	}

}
