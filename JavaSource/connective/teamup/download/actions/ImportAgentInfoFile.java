/*
 * 04/08/2005 - Created
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
 * Action bean to save uploaded comma-delimited file for importing new or updated 
 * agency info.
 * 
 * @author Kyle McCreary
 */
public class ImportAgentInfoFile implements Action
{
	private static final Logger LOGGER = Logger.getLogger(ImportAgentInfoFile.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws ActionException
	{
		String nextPage = "import.agents.map.fields";

		try
		{
			long importDate = System.currentTimeMillis();
			
			// Create the new batch import info object
			BatchInfo batchInfo = op.createBatchInfo();
			batchInfo.setDescription("Import agent info");
			batchInfo.setImportDate(importDate);
			batchInfo.save();
			
			if (items != null)
			{
				for (int i=0; i < items.length; i++)
				{
					if (items[i].isFormField())
					{
						// Parse the form fields from the list of items
						String name = items[i].getFieldName();
						if (name != null && name.equalsIgnoreCase("batchnum"))
						{
							// Rename the field, in order to pass the batch number
							// along to the next page
							String newName = "batchnum_" + String.valueOf(batchInfo.getBatchNumber());
							items[i].setFieldName(newName);
						}
					}
					else
					{
						// Import the file
						DistributedFileInfo fileInfo = op.createDistributedFile(batchInfo);
						String filename = items[i].getName();
						int n = filename.lastIndexOf("\\");
						if (n < 0)
							n = filename.lastIndexOf("/");
						if (n >= 0)
							filename = filename.substring(n+1);
						fileInfo.setFilename(filename);
						fileInfo.setCreatedDate(importDate);
						fileInfo.setImportedDate(importDate);
						fileInfo.setFileType("L");		// Load agent info
				
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
			throw new ActionException("Error importing agent info file", e);
		}
		
		return nextPage;
	}

}
