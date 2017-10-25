/*
 * Created on May 11, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.objects;

import org.apache.commons.codec.binary.Base64;


/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DownloadFileInfoInternal extends DownloadFileInfo 
{
	public DownloadFileInfoInternal() 
	{
		super();
	}

	/**
	 * Returns the file contents as a byte array.
	 * @return byte[]
	 */
	public byte[] getFileContents()
	{
		byte[] ret = null;
		if (fileContents != null)
		{
			ret = Base64.decodeBase64(fileContents.getBytes());
		}
		return ret;
	}

	/**
	 * Sets the file contents.
	 */
	public void setFileContents(byte[] data)
	{
		if (data == null)
			fileSize = -1;
		else
			fileSize = data.length;
		byte[] encContents = Base64.encodeBase64(data);
		fileContents = new String(encContents);
	}
}
