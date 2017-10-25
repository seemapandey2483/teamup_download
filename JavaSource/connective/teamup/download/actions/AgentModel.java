/*
 * 05/18/2005 - Created
 */
package connective.teamup.download.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;

//import org.quartz.SchedulerException;
//import connective.teamup.download.client.agent.job.JobQuery;
//import connective.teamup.download.client.agent.job.JobManager;
//import connective.teamup.download.client.agent.job.JobFrequencyEnumType;
//import connective.teamup.download.client.agent.job.Job;
//import connective.teamup.download.client.agent.AgentApp;


/**
 * Manages the agent info common to all carriers. <p>
 * SINGLETON
 * 
 * @author mccrearyk
 */
public class AgentModel
{
	private static final Logger LOGGER = Logger.getLogger(AgentModel.class);
	
	private static AgentModel _instance = null;
	private static final String configFilenameVer1 = "C:\\teamup\\download\\config.cti";
	private static final String configFilenameVer2 = "C:\\teamup\\download\\config2.cti";
	private static final String schedConfigFilename = "C:\\teamup\\download\\config.tus";
	private static final String schedTaskFilename = "C:\\teamup\\download\\tasks.tus";
	private static final String schedAppFilename = "C:\\teamup\\download\\tudlsched.exe";
	private static final String migrationFilename = "c:\\teamup\\download\\migration.mig";
	private static final String schedValidFilename = "c:\\teamup\\download\\svalid.txt";
	private static final String jobDirectory="C:\\teamup\\download";
	public static final String JOB_NAME="TEAMUP-";
	protected String agencyName = null;
	protected String locationState = null;
	protected String contactName = null;
	protected String contactEmail = null;
	protected String phoneArea = null;
	protected String phonePrefix = null;
	protected String phoneSuffix = null;
	protected String phoneExt = null;
	
	protected int migrationHour = -1;
	protected int migrationMinute = -1;
	protected String migrationCarrier = null;
	protected String migrationAgent = null;
	
	protected String amsId = null;
	protected String amsName = null;
	protected String amsVersion = null;
	protected String downloadDirectory = null;
	protected String pathSearchRoutine = null;
	protected String filenameIncrMethod = null;
	protected boolean downloadDirVariable = false;
	protected boolean filenameChangeable = false;
	protected boolean appendFile = false;
	protected boolean batchfile = false;
	protected boolean customSystem = false;
	
	protected boolean configured = false;

	public static AgentModel getInstance()
	{
		if (_instance == null)
		{
			// Create the singleton instance of the agent object
			_instance = new AgentModel();
			
			// Load previously saved agent, vendor and carrier configuration settings
			/*try
			{
				_instance.refresh();
			}
			catch (Exception e)
			{
				// TODO - Need to handle exception during loading of config settings
			}*/
			
//			// *** TEMP CODE: load temp config settings
/*			if (_instance.getAgencyName() == null || _instance.getAgencyName().equals(""))
				_instance.TEMP_loadTestAgent();
			if (CompanyModel.getInstance().getCompanyCount() == 0)
				CompanyModel.getInstance().TEMP_loadTestCompanies();
*/
			// *** END TEMP CODE ***
		}
		
		return _instance;		
	}

	protected AgentModel() 
	{
		super();
	}

	// check to see if a migration is in place, and if so load the migration info
	public boolean isMigration()
	{
		boolean ret = false;
		
		File migFile = new File(migrationFilename);
		if (migFile.exists())
		{
			try {
				FileReader reader = new FileReader(migFile);
				char[] buf = new char[255];
				int bytesRead = reader.read(buf);
				reader.close();
				String migData = new String(buf, 0, bytesRead);
				int iSep = migData.indexOf(';');
				migrationAgent = migData.substring(0, iSep);
				migData = migData.substring(iSep + 1);
				iSep = migData.indexOf(';');
				migrationCarrier = migData.substring(0, iSep);
				migData = migData.substring(iSep + 1);
				iSep = migData.indexOf(':');
				migrationHour = Integer.parseInt(migData.substring(0, iSep));
				migrationMinute = Integer.parseInt(migData.substring(iSep + 1));
			}
			catch (Exception e) {
				LOGGER.error(e.getMessage());
				System.out.println(e.getMessage());
				return false;
			}

			ret = true;
		}
		
		return ret;
	}
	
	public void completeMigration()
	{
				
		// delete the migration file
		File migFile = new File(migrationFilename);
		migFile.delete();

		migrationHour = -1;
		migrationMinute = -1;
		migrationCarrier = null;
		migrationAgent = null;
	}
	
	protected void TEMP_loadTestAgent()
	{
		agencyName = "Independent Insurance Agency";
		locationState = "TX";
		contactName = "Brendan Hilton";
		contactEmail = "bhilton@connective-edi.com";
		phoneArea = "713";
		phonePrefix = "690";
		phoneSuffix = "6789";
		phoneExt = "135";
		amsId = "APPLIED";
		amsName = "Applied - TAM";
		amsVersion = "6.3.0";
		downloadDirectory = "C:\\APPS\\";
		pathSearchRoutine = "wildcard";
		filenameIncrMethod = "X";
		downloadDirVariable = false;
		filenameChangeable = false;
		appendFile = false;
		batchfile = false;
		customSystem = false;
	}

	/**
	 * Saves the current agency contact, vendor and registered carrier settings to the config file.
	 */
	public void save() throws Exception
	{
		// Save the agent data to an XML file
		File configFile = new File(configFilenameVer2);
		if (!configFile.exists())
		{
			// Attempt to create the path/directory structure
			String path = configFile.getCanonicalPath();
			int n = path.lastIndexOf('\\');
			File filePath = new File(path.substring(0, n));
			filePath.mkdirs();
		}
		
		FileOutputStream os = new FileOutputStream(configFile);
		//ClientInfoHandler handler = new ClientInfoHandler(this);
		//os.write(handler.writeToXML().getBytes());
		os.flush();
		os.close();
	}
	
	public void validateSchedulerConfig() throws Exception
	{
		// check the config file
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(schedConfigFilename));
			boolean badPropsFound = false;
			Iterator it = props.keySet().iterator();
			while (it.hasNext()) {
				String propValue = props.getProperty((String) it.next());
				if (propValue != null) {
					int iSep = propValue.indexOf("http://");
					if (iSep >=0) {
						if (propValue.indexOf('\t', iSep) >= 0 || 
							propValue.indexOf("teamupregclient", iSep) >=0) {
							badPropsFound = true;
							break;
						}
					}
				}
			}
	
			if (!badPropsFound)
			{
				// check the tasks file
				props = new Properties();
				props.load(new FileInputStream(schedTaskFilename));
				it = props.keySet().iterator();
				while (it.hasNext()) {
					String propValue = props.getProperty((String) it.next());
					if (propValue != null) {
						int iSep = propValue.indexOf("http://");
						if (iSep >=0) {
							if (propValue.indexOf('\t', iSep) >= 0 || 
								propValue.indexOf("teamupregclient", iSep) >=0) {
								badPropsFound = true;
								break;
							}
						}
					}
				}
			}
			
			if (badPropsFound) {
				//writeSchedulerFiles();
				JOptionPane.showMessageDialog(null, "TEAM-UP Download has updated your scheduler configuration.  Please restart your computer to complete this update.", "TEAM-UP Download", JOptionPane.WARNING_MESSAGE);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage());
			// no scheduler files - proceed with writing the validated file
		}
		
		// create the "validated" file
		FileOutputStream os = new FileOutputStream(schedValidFilename);
		os.write(0);
		os.close();
	}
	
	/*public void saveSchedulerSettings() throws Exception
	{	
		// check if scheduler is enabled
		CompanyModel cm = CompanyModel.getInstance();
		boolean schedEnabled = false;
		for (int i=0; i < cm.getCompanyCount(); i++)
		{
			if (cm.getCompany(i).isScheduled())
			{
				schedEnabled = true;
				break;
			}
		}
		
		if (schedEnabled)
		{
			// write the scheduler configuration files
			//writeSchedulerFiles();
			//writeandScheduleJobs();
			writeWindowdScheduleJobs();
			// download the scheduler app if not present
			File schedApp = new File(schedAppFilename);
			if (!schedApp.exists())
			{
				// download the file
				URL schedAppUrl = new URL(AgentApp.teamupRegistrationUrl + "/sched/tudlsched.exe");
				InputStream is = schedAppUrl.openStream();
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(schedAppFilename));
				int read; 
				int blockSize = 32768;
				byte[] buf = new byte[blockSize];
				while ((read = is.read(buf, 0, blockSize)) != -1)
					bos.write(buf, 0, read);
				bos.flush();
				bos.close();			
			}
			
			// run the file
			Runtime rt = Runtime.getRuntime();
			String cmdline = schedAppFilename + " install";
			rt.exec(cmdline);				
		}
		else
		{
			// delete the scheduler configuration files
			File schedConfigFile = new File(schedConfigFilename);
			if (schedConfigFile.exists())
				schedConfigFile.delete();
				
			File schedTaskFile = new File(schedTaskFilename);
			if (schedTaskFile.exists())
				schedTaskFile.delete();
				
			// uninstall the scheduler app				
			//File schedApp = new File(schedAppFilename);
			//if (schedApp.exists())
			//{
			//	Runtime rt = Runtime.getRuntime();
			//	String cmdline = schedAppFilename + " uninstall";
			//	rt.exec(cmdline);
			//}				
		}
	}*/

	/*protected void writeSchedulerFiles() throws IOException
	{
		// get the JWS and JNLP paths
		String jwsPath = System.getProperty("java.home") + "\\bin\\javaws";
		String jnlpPath = AgentApp.teamupRegistrationUrl + "/client/tudlagent.jnlp";
		
		// write the scheduler config
		FileOutputStream os = new FileOutputStream(schedConfigFilename);
		PrintWriter out = new PrintWriter(os);
		out.println("Open TEAM-UP Agent App...=\"" + jwsPath + "\" " + jnlpPath);
		out.println("SEPARATOR=SEPARATOR");
		out.println("Download Current Files Now=\"" + jwsPath + "\" -open #ALL# " + jnlpPath);
		out.println("Scheduler Settings...=\"" + jwsPath + "\" -open #CONFIG# " + jnlpPath);
		out.flush();
		out.close();
		
		// write the scheduler task file
		CompanyModel cm = CompanyModel.getInstance();				
		os = new FileOutputStream(schedTaskFilename);
		out = new PrintWriter(os);
		for (int i=0; i < cm.getCompanyCount(); i++)
		{
			CompanyInfo company = cm.getCompany(i);
			if (company.isScheduled())
			{
				int schedHour = Integer.parseInt(company.getSchedHour());
				if (company.getSchedPeriod().equalsIgnoreCase("pm"))
					schedHour += 12;
				if (schedHour > 23)
					schedHour = 0;
				int schedMinute = Integer.parseInt(company.getSchedMinute());
				
				// randomly add 0-14 minutes to the time so that scheduled downloads
				// are spread out within a 15 minute period
				Random random = new Random();
				schedMinute += random.nextInt(15); 
				int schedTime = (schedHour * 60) + schedMinute;
				
				out.println(String.valueOf(schedTime) + ";\"" + jwsPath + "\" -open " + company.getCompanyId() + ":" + company.getAgentId() + " " + jnlpPath);
			}
		}
		
		out.flush();
		out.close();
	}*/
	
	/*protected void writeandScheduleJobs() throws IOException
	{
		
	
		// write the scheduler task file
		CompanyModel cm = CompanyModel.getInstance();				
	
		for (int i=0; i < cm.getCompanyCount(); i++)
		{
			CompanyInfo company = cm.getCompany(i);
			//DownloadJobScheduler downloadJobScheduler = DownloadJobScheduler.getInstance();
			if (company.isScheduled())
			{
				int schedHour = Integer.parseInt(company.getSchedHour());
				if (company.getSchedPeriod().equalsIgnoreCase("pm"))
					schedHour += 12;
				if (schedHour > 23)
					schedHour = 0;
				int schedMinute = Integer.parseInt(company.getSchedMinute());
				
				// randomly add 0-14 minutes to the time so that scheduled downloads
				// are spread out within a 15 minute period
				Random random = new Random();
				schedMinute += random.nextInt(5); 
				int schedTime = (schedHour * 60) + schedMinute;
				//downloadJobScheduler.deleteJob(company.getAgentId(),company.getCompanyId());
				//downloadJobScheduler.scheduleJob( company.getAgentId(),schedHour,schedMinute, company.getCompanyId());
				
				
			}else{
				//downloadJobScheduler.deleteJob(company.getAgentId(),company.getCompanyId());

			}
		}
		//DownloadJobScheduler.printJobList();
	}*/
	
	/*protected void writeWindowdScheduleJobs() throws IOException
	{
		
	
		String jwsPath = System.getProperty("java.home") + "\\bin\\javaws";
		String jnlpPath = AgentApp.teamupRegistrationUrl + "/client/tudlagent.jnlp";
		String command ="";
		
		
		// write the scheduler task file
		CompanyModel cm = CompanyModel.getInstance();				
	
		for (int i=0; i < cm.getCompanyCount(); i++)
		{
			CompanyInfo company = cm.getCompany(i);
			//DownloadJobScheduler downloadJobScheduler = DownloadJobScheduler.getInstance();
			String jobTime = "";
			String schedMinuteStr ="";
			if (company.isScheduled())
			{
				int schedHour = Integer.parseInt(company.getSchedHour());
				if (company.getSchedPeriod().equalsIgnoreCase("pm"))
					schedHour += 12;
				if (schedHour > 23)
					schedHour = 0;
				int schedMinute = Integer.parseInt(company.getSchedMinute());
				
				
				if(schedMinute<10){
					schedMinuteStr ="0"+schedMinute;					
				}else{
					schedMinuteStr = String.valueOf(schedMinute);
				}
				// randomly add 0-14 minutes to the time so that scheduled downloads
				// are spread out within a 15 minute period
				Random random = new Random();
				schedMinute += random.nextInt(10);
				if(schedMinute<10){
					schedMinuteStr ="0"+schedMinute;					
				}else{
					schedMinuteStr = String.valueOf(schedMinute);
				}

				
				jobTime = schedHour +":"+schedMinuteStr +":00";
				
				//downloadJobScheduler.deleteJob(company.getAgentId(),company.getCompanyId());
				//downloadJobScheduler.scheduleJob( company.getAgentId(),schedHour,schedMinute, company.getCompanyId());
				Job job = new Job();
				job.setAgentId(company.getAgentId());
				job.setCompanyId(company.getCompanyId());
				
				String jobName = JOB_NAME + job.getAgentId() +"("+ job.getCompanyId()+")";
				jobName ="\""+jobName+"\"";
				job.setJobName(jobName);
				
				
				job.setFrquency(JobFrequencyEnumType.DAILY);
				//command = "\"\\" +"\""+ jwsPath + "\\"+"\"" +"-open " + company.getCompanyId() + ":" + company.getAgentId() + " " + jnlpPath+"\"";
				command ="\"" + jwsPath + "\" -open " + company.getCompanyId() + ":" + company.getAgentId() + " " + jnlpPath;
				job.setTimeToRun(jobTime);
				job.setCommand(command);
				JobManager jManger = new JobManager();
				JobQuery query = jManger.queryJob(job);
				if(query == null){
					jManger.createDailyJob(job);					
				}else{
					if(jManger.checkforJobChange(job, query, company)){
						jManger.deleteDailyJob(job);
						jManger.createDailyJob(job);
					}
				}

				
			}else {
				JobManager jManger = new JobManager();
				Job job = new Job();
				job.setAgentId(company.getAgentId());
				job.setCompanyId(company.getCompanyId());
				String jobName = AgentModel.JOB_NAME + job.getAgentId() +"("+ job.getCompanyId()+")";
				jobName ="\""+jobName+"\"";
				job.setJobName(jobName);
				JobQuery query = jManger.queryJob(job);
				if(query != null){
					jManger.deleteDailyJob(job);					
				}
				
			}
		}
	
	}*/
	/**
	 * Loads the previously saved agency contact, vendor and registered carrier settings from
	 * the config file.
	 */
	/*public void refresh() throws Exception
	{
		File configFile = new File(configFilenameVer2);
		if (!configFile.exists())
		{
			// use the old file version
			configFile = new File(configFilenameVer1);

			// Load the previously saved configuration settings
			ClientInfoHandler handler = new ClientInfoHandler(this);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			parser.parse(configFile, handler);
			
			save();
			
			// delete the old file
			configFile.delete();

			// update the scheduler files
			saveSchedulerSettings();
			
			setConfigured(true);
		}
		else
		{
			// Load the previously saved configuration settings
			ClientInfoHandler handler = new ClientInfoHandler(this);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			parser.parse(configFile, handler);
			
			setConfigured(true);
		}
	}*/

	/*public void setVendorSystem(AMSInfo system)
	{
		if (system == null)
		{
			amsId = null;
			amsName = null;
			downloadDirectory = null;
			pathSearchRoutine = null;
			filenameIncrMethod = null;
			downloadDirVariable = false;
			filenameChangeable = false;
			appendFile = false;
			batchfile = false;
			customSystem = false;
		}
		else
		{
			amsId = system.getId();
			amsName = system.getName();
			downloadDirectory = system.getDefaultDirectory();
			pathSearchRoutine = system.getDirectoryDetection();
			filenameIncrMethod = system.getFilenameIncrementMethod();
			downloadDirVariable = system.isDirectoryVariable();
			filenameChangeable = system.isFilenameChangeAllowed();
			appendFile = system.isFileAppended();
			batchfile = system.isBatchfileNeeded();
			customSystem = system.isCustomSystem();
		}
	}*/

	/**
	 * @return
	 */
	public String getAgencyName()
	{
		return agencyName;
	}

	/**
	 * @return
	 */
	public String getAmsId()
	{
		return amsId;
	}

	public String getAmsName()
	{
		return amsName;
	}

	/**
	 * @return
	 */
	public String getAmsVersion()
	{
		return amsVersion;
	}

	/**
	 * @return
	 */
	public String getContactEmail()
	{
		return contactEmail;
	}

	/**
	 * @return
	 */
	public String getContactName()
	{
		return contactName;
	}

	/**
	 * @return
	 */
	public String getDownloadDirectory()
	{
		return downloadDirectory;
	}

	/**
	 * @return
	 */
	public String getPhoneArea()
	{
		return phoneArea;
	}

	/**
	 * @return
	 */
	public String getPhoneExt()
	{
		return phoneExt;
	}

	/**
	 * @return
	 */
	public String getPhonePrefix()
	{
		return phonePrefix;
	}

	/**
	 * @return
	 */
	public String getPhoneSuffix()
	{
		return phoneSuffix;
	}

	public String getPhoneFormatted()
	{
		if (phoneArea == null || phoneArea.trim().equals(""))
			return "";
		
		String ret = "(" + phoneArea + ") " + phonePrefix + "-" + phoneSuffix;
		if (phoneExt != null && !phoneExt.trim().equals(""))
			ret += " x." + phoneExt;
		
		return ret;
	}

	public String getPhoneCombined()
	{
		String phone = padd(phoneArea, 3) + padd(phonePrefix, 3) + padd(phoneSuffix, 4);
		if (phone.trim().equals(""))
			phone = null;
		else if (phoneExt != null)
			phone += phoneExt.trim();
		return phone;
	}

	private String padd(String str, int len)
	{
		String ret = str;
		if (ret == null)
			ret = "";
		while (ret.length() < len)
			ret += " ";
		return ret;
	}

	private String maxLength(String str, int len)
	{
		String ret = str;
		if (str == null)
			ret = "";
		else if (str.length() > len)
			ret = str.substring(0, len);
		return ret;
	}

	/**
	 * @param string
	 */
	public void setAgencyName(String string)
	{
		agencyName = string;
	}

	/**
	 * @param string
	 */
	public void setAmsId(String string)
	{
		amsId = string;
	}

	public void setAmsName(String string)
	{
		amsName = string;
	}

	/**
	 * @param string
	 */
	public void setAmsVersion(String string)
	{
		amsVersion = maxLength(string, 10);
	}

	/**
	 * @param string
	 */
	public void setContactEmail(String string)
	{
		contactEmail = maxLength(string, 50);
	}

	/**
	 * @param string
	 */
	public void setContactName(String string)
	{
		contactName = maxLength(string, 30);
	}

	/**
	 * @param string
	 */
	public void setDownloadDirectory(String string)
	{
		downloadDirectory = string;
	}

	/**
	 * @param string
	 */
	public void setPhoneArea(String string)
	{
		phoneArea = maxLength(string, 3);
	}

	/**
	 * @param string
	 */
	public void setPhoneExt(String string)
	{
		phoneExt = maxLength(string, 4);
	}

	/**
	 * @param string
	 */
	public void setPhonePrefix(String string)
	{
		phonePrefix = maxLength(string, 3);
	}

	/**
	 * @param string
	 */
	public void setPhoneSuffix(String string)
	{
		phoneSuffix = maxLength(string, 4);
	}

	/**
	 * @return
	 */
	public boolean isDownloadDirVariable()
	{
		return downloadDirVariable;
	}

	/**
	 * @param b
	 */
	public void setDownloadDirVariable(boolean b)
	{
		downloadDirVariable = b;
	}

	/**
	 * @return
	 */
	public String getPathSearchRoutine()
	{
		return pathSearchRoutine;
	}

	/**
	 * @param string
	 */
	public void setPathSearchRoutine(String string)
	{
		pathSearchRoutine = string;
	}

	/**
	 * @return
	 */
	public boolean isAppendFile()
	{
		return appendFile;
	}

	/**
	 * @return
	 */
	public boolean isBatchfile()
	{
		return batchfile;
	}

	/**
	 * @return
	 */
	public boolean isCustomSystem()
	{
		return customSystem;
	}

	/**
	 * @return
	 */
	public boolean isFilenameChangeable()
	{
		return filenameChangeable;
	}

	/**
	 * @return
	 */
	public String getFilenameIncrMethod()
	{
		return filenameIncrMethod;
	}

	/**
	 * @param b
	 */
	public void setAppendFile(boolean b)
	{
		appendFile = b;
	}

	/**
	 * @param b
	 */
	public void setBatchfile(boolean b)
	{
		batchfile = b;
	}

	/**
	 * @param b
	 */
	public void setCustomSystem(boolean b)
	{
		customSystem = b;
	}

	/**
	 * @param b
	 */
	public void setFilenameChangeable(boolean b)
	{
		filenameChangeable = b;
	}

	/**
	 * @param string
	 */
	public void setFilenameIncrMethod(String string)
	{
		filenameIncrMethod = string;
	}

	/**
	 * @return
	 */
	public boolean isConfigured()
	{
		return configured;
	}

	/**
	 * @param b
	 */
	public void setConfigured(boolean b)
	{
		configured = b;
	}

	/**
	 * @return
	 */
	public String getLocationState()
	{
		return locationState;
	}

	/**
	 * @param string
	 */
	public void setLocationState(String string)
	{
		locationState = string;
	}

	/**
	 * @return
	 */
	public String getMigrationAgent() {
		return migrationAgent;
	}

	/**
	 * @return
	 */
	public String getMigrationCarrier() {
		return migrationCarrier;
	}

	/**
	 * @return
	 */
	public int getMigrationHour() {
		return migrationHour;
	}

	/**
	 * @return
	 */
	public int getMigrationMinute() {
		return migrationMinute;
	}

}
