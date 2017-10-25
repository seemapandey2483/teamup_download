/*
 * 03/02/2005 - Created
 */
package connective.teamup.download.actions;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedFileInfo;

/**
 * Action bean to save uploaded Applied edit files for distribution to agents.
 * 
 * @author Kyle McCreary
 */
public class UploadEditFile implements Action
{
	private static final Logger LOGGER = Logger.getLogger(UploadEditFile.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "distribute.edits.select.files";
		BatchInfo batch = null;

		try
		{
			if (items != null)
			{
				// Parse the form fields from the list of items
				String uploadType = null;
				for (int i=0; i < items.length; i++)
				{
					if (items[i].isFormField())
					{
						String name = items[i].getFieldName();
						if (name != null && name.equalsIgnoreCase("distBatchNumber"))
						{
							try
							{
								int batchnum = Integer.parseInt(items[i].getString());
								batch = op.getBatchInfo(batchnum);
							}
							catch (Exception e) {
								LOGGER.error(e);
							}
						}
						else if (name != null && name.equalsIgnoreCase("uploadType"))
						{
							uploadType = items[i].getString();
							if (uploadType == null)
								uploadType = "";
							else if (uploadType.equals("applied.edits"))
								nextPage = "distribute.edits.select.files";
							else if (uploadType.equals("email.distribution"))
								nextPage = "distribute.email.select.files";
						}
					}
				}
				
				// Process the uploaded files
				for (int i=0; i < items.length; i++)
				{
					if (!items[i].isFormField())
					{
						DistributedFileInfo fileInfo = op.createDistributedFile(batch);
						String filename = items[i].getName();
						int n = filename.lastIndexOf("\\");
						if (n < 0)
							n = filename.lastIndexOf("/");
						if (n >= 0)
							filename = filename.substring(n+1);
						fileInfo.setFilename(filename);
						if (uploadType == null)
							fileInfo.setFileType("");
						else if (uploadType.equalsIgnoreCase("applied.edits"))
							fileInfo.setFileType("A");
						else if (uploadType.equalsIgnoreCase("email.distribution"))
							fileInfo.setFileType("E");
						fileInfo.setImportedDate(System.currentTimeMillis());
						// TODO - Determine file creation date/time
						fileInfo.setCreatedDate(System.currentTimeMillis());
				
						// upload the file contents
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						byte[] buf = new byte[256];
						int read; 
						InputStream is = items[i].getInputStream();
						while ((read = is.read(buf, 0, 256)) != -1)
							bos.write(buf, 0, read);
						
						fileInfo.setFileContents(bos.toByteArray());
				
						fileInfo.save();
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			e.printStackTrace(System.out);
			throw new ActionException("Error uploading files for distribution", e);
		}
		
		return nextPage;
	}

}
