/*
 * Created on Oct 25, 2007
 * 
 * Method 'decompress()' was originally part of the TeamupFileReceiver class as method 'unsqueeze()'.
 */
package connective.teamup.upload;

/**
 * Decompression filter and utilities for standard ACORD file compression.
 *  
 * @author Kyle McCreary
 */
public class AcordCompressionFilter
{
	private char[] compressionChar = {250, 179};


	/**
	 * Checks to see if the specified text data contains any instances of the compression character(s).
	 * 
	 * @param text - the text data to check
	 * 
	 * @return true if data contains compression sequence(s), otherwise false
	 */
	public boolean isCompressed(String text)
	{
		boolean compressed = false;
		
		if (text != null && !text.equals(""))
		{
			for (int i=0; i < compressionChar.length; i++)
			{
				if (text.indexOf(compressionChar[i]) >= 0)
				{
					compressed = true;
					break;
				}
			}
		}
		
		return compressed;
	}

	/**
	 * Decompresses AL3 data that has been compressed with the "AL3 squeeze" or other standard
	 * ACORD compression algorithm.
	 * 
	 * @param uploadData - the AL3 data
	 * 
	 * @return String containing the decompressed text data
	 */
	public String decompress(String text)
	{
		if (text == null)
			return "";
					
		StringBuffer data = new StringBuffer();
		for (int i=0; i < text.length(); i++)
		{
			char ch = text.charAt(i);
			boolean match = false;
			for (int j=0; j < compressionChar.length; j++)
			{
				if (ch == compressionChar[j])
				{
					match = true;
					break;
				}
			}

			if (match)
			{
				int count = Integer.parseInt(text.substring(i+ 1, i + 3), 16);
				char character = text.charAt(i+3);
				for (int k=0; k<count; k++)
					data.append(character);
				i += 3;				
			}
			else
			{
				data.append(ch);
			}
		}

		return data.toString();
	}

}
