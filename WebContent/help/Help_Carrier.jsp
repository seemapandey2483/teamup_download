<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
<title>TEAM-UP Download</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body bgcolor="#FFFFFF" text="#000000">
<table width="100%" border="0" name="Help_Body">
  <tr bgcolor="#000000"> 
    <td colspan="3"> 
      <h1><b><font color="#FFFFFF"><a name="top">Carrier Administration</a></font></b></h1>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="19">&nbsp;</td>
    <td colspan="2" height="19" bgcolor="#FFFFFF"> 
      <p><a href="#Carrier_Actions">Actions</a></p>
      <ul>
        <li><a href="#Import">Import...</a></li>
        <li><a href="#Purge">Purge</a></li>
      </ul>
      <p><a href="#Carrier_Preferences">Preferences</a></p>
      <ul>
        <li><a href="#Config_Settings">Configuration Settings</a></li>
        <li><a href="#Advanced_Options">Advanced Options</a></li>
        <li><a href="#AVS_Maint">Agency Vendor System Maintenance (View)</a></li>
        <li><a href="#AVS_Edit">Agency Vendor System Maintenance (Edit)</a></li>
      </ul>
      <p><a href="#Carrier_TradPart">Trading Partners</a></p>
      <ul>
        <li><a href="#View_List">View List</a></li>
        <li><a href="#Summary">Summary</a> </li>
        <li><a href="#Add_New">Add New</a></li>
        <li><a href="#Edit_Existing">Edit Existing</a></li>
        <li><a href="#Manage_Part_Codes">Manage Participant Codes</a></li>
      </ul>
      <p><a href="#Carrier_Reports">Reports</a></p>
      <ul>
        <li><a href="#TransLog">Transaction Log per Trading Partner</a></li>
        <li><a href="#Consolidated_Report">Consolidated Activity Report for All 
          Agents</a></li>
      </ul>
      <p>&nbsp;</p>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="19"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td colspan="2" height="19" bgcolor="#FF0000"> 
      <h2><a name="Carrier_Actions"><font color="#FFFFFF">Actions</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td colspan="2"> 
      <h3>Import...<a name="Import"></a></h3>
      <p>The &quot;import&quot; process pulls files from a carriers LAN or mainframe 
        and imports inserts them into TEAM-UP Download's database where they are 
        made available to agents for download</p>
      <p>The Import process is designed to fit into a carrier's existing download 
        workflow. A separate non-interactive version of the import process is 
        available that can work within a nightly process. The import can also 
        be run interactively to select individual files from the download directory.</p>
      <p>Import Process:</p>
      <ul>
        <li>Each AL3 file in the download directory is evaluated to determine 
          if it is associated with a defined agent</li>
        <li>The import process will act on the file based on the status of the 
          agent that is associated with the file</li>
        <li>Processing is based on Agent Status</li>
      </ul>
      <table width="80%" border="1" align="center">
        <tr bordercolor="#999999" bgcolor="#000000"> 
          <td width="22%"><font color="#FFFFFF"><b>Agent Status</b></font></td>
          <td width="78%"><font color="#FFFFFF"><b>Description</b></font></td>
        </tr>
        <tr> 
          <td width="22%"> 
            <div align="left"><B>Inactive</B></div>
          </td>
          <td width="78%"> 
            <p>A new agent will remain in &quot;Inactive&quot; status until they 
              have completed the agent registration wizard.</p>
            <p>The AL3 download file will be pulled into the agent <b>archive</b>, 
              but the file will <b>NOT</b> be deleted from the download directory. 
              (If a carrier has a process in place to transmit files to IVANS, 
              the agent's AL3 file will be sent to the agent's IVANS mailbox).</p>
          </td>
        </tr>
        <tr> 
          <td width="22%" height="49"> 
            <div align="left"><B>Active</B></div>
          </td>
          <td width="78%" height="49"> 
            <p>An agent will be changed to &quot;Active&quot; when they have completed 
              the agent registration wizard. </p>
            <p>Note: <i>All processing for an agent will remain the same as an 
              Inactive [unregistered] agent.</i></p>
          </td>
        </tr>
        <tr> 
          <td width="22%"><B>Live</B></td>
          <td width="78%"> 
            <p>An agent has the option to &quot;go live&quot; after the registration 
              process. The AL3 download file will be pulled into the agent's <b><font color="#FF0000">current 
              </font></b>download file and <b>WILL</b> be deleted from the download 
              directory (it will NOT go to the agent's IVANS mailbox).</p>
            <p>Note: An Agent has the ability to toggle between &quot;Live&quot; 
              and &quot;Active&quot; status.</p>
          </td>
        </tr>
        <tr> 
          <td width="22%"><B>Disabled</B></td>
          <td width="78%"> 
            <p>An agent may be marked as &quot;<b>Disabled</b>&quot; by the carrier. 
              Disabling an agent stops all imports for that agent, and prevents 
              the agent from accessing the system for downloads.</p>
          </td>
        </tr>
      </table>
      <p><a href="#top">back to top</a></p>
      <hr>
      <h3><a name="Purge"></a>Purge</h3>
      <ul>
        <li>The Purge process will delete Archive files based on settings defined 
          in the carrier configuration. The Purge will not delete any &quot;current&quot; 
          files. 
          <ul>
            <li>Download File Types 
              <ul>
                <li>Current File: files that have been imported for Live agents, 
                  but have not been downloaded by the agent</li>
                <li>Archive File: files that have been imported for Inactive or 
                  Active agents, OR files that have been downloaded by Live agents</li>
              </ul>
            </li>
          </ul>
        </li>
        <li>The purge process will typically be set to run automatically, but 
          can also be initiated via the interactive menu.</li>
      </ul>
      <p><a href="#top">back to top</a></p>
      </td>
  </tr>
  <tr> 
    <td width="3%" height="16"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td colspan="2" bgcolor="#FF0000" height="16"> 
      <h2><a name="Carrier_Preferences"><font color="#FFFFFF">Preferences</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="471">&nbsp;</td>
    <td colspan="2" height="471"> 
      <h3>Configuration Settings<a name="Config_Settings"></a>...</h3>
      <ul>
        <li>Archiving Download Files 
          <ul>
            <li>Automatically purge download files: If checked, the archive will 
              be purged according to the configuration settings each time an import 
              process is run. (Typically, the import process will be run each 
              weekday per the carrier's current download procedures.)</li>
            <li>Archive for ___ days: It is recommended that carriers keep at 
              least 30 days of archive files.</li>
          </ul>
        </li>
        <li>Importing Download Files 
          <ul>
            <li>Import file source path: This is the location of the AL3 download 
              files. The Import process will look to this directory in for the 
              AL3 formatted download files.</li>
            <li>Delete imported source files from source path: The default is 
              to delete imported files from the source path only for trading partners 
              who have set their status to &quot;go live.&quot;&nbsp; The second 
              option will delete the file from the source path for all trading 
              partners (no matter what their status is) upon a successful import 
              into the system.</li>
          </ul>
        </li>
        <li>Downloading Test Files 
          <ul>
            <li>Test file: The test file is used during the Agency Registration 
              Process. A download test is performed as part of the registration 
              process in order to ensure the communications and configuration 
              settings are set up properly.</li>
          </ul>
        </li>
        <li>Email Addresses: TEAM-UP Download allows the carrier to define different 
          email addresses based on the purpose of the incoming email. It is recommended 
          that a distribution list is used as the email address whenever possible. 
          The distribution list can then be maintained via the carrier's mail 
          system. 
          <ul>
            <li>Agency Contact: emails initiated via the &quot;Contact Us&quot; 
              link on the application are sent to this address</li>
            <li>Confirmation Reports: System-generated confirmation reports are 
              sent to this address</li>
            <li>Error Reports: System-generated error reports are sent to this 
              address</li>
          </ul>
        </li>
      </ul>
      <ul>
        <li> <b><font color="#FF0000">Screen Actions</font></b> 
          <ul>
            <li><b>Cancel</b></li>
            <li><b>Save</b></li>
          </ul>
        </li>
      </ul>
      <p><a href="#top">back to top</a></p>
      <hr>
      <h3>Advanced Options<a name="Advanced_Options"></a>...</h3>
      <ul>
        <li> Email Notification Settings 
          <ul>
            <li>Receive email notification of agency registration: If &quot;Yes&quot; 
              is selected, the system will send an email notification to the administrator 
              when an agent completes the Registration process.</li>
            <li>Receive email notification of agency status change: If &quot;Yes&quot; 
              is selected, the system will send an email notification to the administrator 
              when an agent changes configuration settings to either start or 
              stop receiving &quot;live&quot; downloads via TEAM-UP.</li>
            <li>Receive email notification of agency download errors: If &quot;Yes&quot; 
              is selected, the system will send an email notification to the administrator 
              when the download process encounters an error.</li>
            <li>Receive email notification of carrier import errors: If &quot;Yes&quot; 
              is selected, the system will send an email notification to the administrator 
              when the import process encounters an error.</li>
          </ul>
        </li>
        <li>Trading Partner Configuration 
          <ul>
            <li>Allow trading partners to change their own passwords: If yes is 
              selected, agents will have the ability to change their TEAM-UP Download 
              password. This option may be turned off if the TEAM-UP Download 
              password is 'tied' to some other carrier password or system.</li>
          </ul>
        </li>
        <li>Customizable Text Files: TEAM-UP Download provides default wording 
          for several email messages and screens that are presented to the agents. 
          Companies have the ability to edit the text files that contain the &quot;body&quot; 
          of the emails and screens. The path and filenames of these text files 
          are presented on the &quot;Advanced options&quot; screen. 
          <ul>
            <li><i>NOTE: Within each of these files are various special tags, 
              in the format of &quot;&lt;TEAMUP:tagname&gt;&quot;. Any time the 
              system encounters one of these tags, it replaces it with the appropriate 
              data. For example, &quot;Thank you for placing your business with 
              &lt;TEAMUP:CarrierName&gt;!&quot; will actually appear to the agent 
              as &quot;Thank you for placing your business with ABC Insurance 
              Company!&quot;.</i> </li>
          </ul>
        </li>
        <blockquote> 
          <p>&nbsp;</p>
        </blockquote>
        <li><b><font color="#FF0000">Screen Actions</font></b> 
          <ul>
            <li><b>Cancel</b></li>
            <li><b>Save</b></li>
          </ul>
        </li>
      </ul>
      <p><a href="#top">back to top</a></p>
      <hr>
      <h3>Agency Vendor Systems...</h3>
      <h3>Agency Vendor System Maintenance<a name="AVS_Maint"></a> (view)</h3>
      <ul>
        <li>TEAM-UP has predefined most of the configuration settings for the 
          most common agency management systems. Basic information for each system, 
          including download directory preferences, filename conventions and rules 
          are presented to the administrator (this information is also presented 
          to the agent during the registration process).</li>
        <li>If an agency system is not listed, a new system may be added by selecting 
          &quot;Add New&quot; (see below).</li>
      </ul>
      <ul>
        <li><b><font color="#FF0000">Screen Actions</font></b> 
          <ul>
            <li><b>Add New: </b>Agency systems may be added to the &quot;Vendor 
              System&quot; list</li>
            <li><b>Edit: </b>Defaults for existing agency systems may be edited 
              / changed</li>
            <li><b>Sort Alphabetically: </b>The drop-down list can be resorted</li>
          </ul>
        </li>
      </ul>
      <p><a href="#top">back to top</a></p>
      <hr>
      <h3>Agency Vendor System Maintenance<a name="AVS_Edit"></a> (edit/change)</h3>
      <p>(The vendor system maintenance screen can be reached from the &quot;Add 
        New&quot; or &quot;Edit&quot; screen options (see above).</p>
      <ul>
        <li>Required Fields: 
          <ul>
            <li>System ID: this is a unique id for the vendor system, used internally</li>
            <LI>Name: the displayed name for the vendor system</LI>
            <LI>Registration Control Type: It is recommended that you contact 
              Connective for assistance selecting a &quot;Registration Control&quot; 
              when adding a new agency system.</LI>
            <LI>Sort Sequence: the sort order used when building the vendor system 
              drop box. <I>Note: You can use a sort sequence of 999 or greater 
              to place an entry at the bottom of the list; this will prevent it 
              from being resorted when an alphabetical sort is performed.</I></LI>
          </ul>
        </li>
        <li>Optional Fields: 
          <UL>
            <LI>Default Download Directory: enter only if the vendor system has 
              a specific directory where downloaded files should be placed</LI>
            <LI>Directory Notes: if a default download directory is entered, enter 
              any related notes or instructions to be displayed to the agent</LI>
            <LI>Default Download Filename: enter only if the vendor system has 
              a specific filename that is required for the download file; if a 
              default filename is specified, TEAM-UP will rename the file during 
              the download process, otherwise the file will retain its original 
              name</LI>
            <LI>Filename Notes: any relevant notes pertaining to the default filename</LI>
            <LI>Append to Existing File: <I>(not used at this time)</I></LI>
          </UL>
        </li>
      </ul>
      <ul>
        <li><b><font color="#FF0000">Screen Actions</font></b> 
          <ul>
            <li><b>Cancel</b></li>
            <li><b>Save</b></li>
          </ul>
        </li>
      </ul>
      <p><a href="#top">back to top</a></p>
      </td>
  </tr>
  <tr bgcolor="#CCCCCC"> 
    <td width="3%" height="19" bgcolor="#FFFFFF"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td colspan="2" height="19" bgcolor="#FF0000"> 
      <h2><a name="Carrier_TradPart"><font color="#FFFFFF">Trading Partners</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="690">&nbsp;</td>
    <td colspan="2" height="690"> 
      <h3>View List<a name="View_List"></a></h3>
      <ul>
        <li> 
          <p>The &quot;Trading Partner Maintenance&quot; screen displays a list 
            of the defined trading partners (agents) and provides a brief summary 
            of each trading partners profile. </p>
          <ul>
            <li>A link is available on the &quot;Agency ID&quot; and the &quot;Agency 
              Name&quot;. These links drill down to the detail configuration screen 
              for the agent. </li>
            <li>Agent Status: (see the &quot;Agent Status&quot; table for descriptions 
              of each agent status) 
              <ul>
                <li>Inactive</li>
                <li>Active</li>
                <li>Live</li>
                <li>Disabled</li>
              </ul>
            </li>
            <li>Last Download Date: This column allows carriers to quickly sort 
              agents by their last downloaded date in order to see if any agents 
              are not retrieving their download files.</li>
            <li>Agency Vendor System</li>
          </ul>
          <p>&nbsp;</p>
        </li>
        <li><b><font color="#FF0000">Screen Actions</font></b> 
          <ul>
            <li><b>Add New</b></li>
            <li><b>View Files</b>: Displays current and archive files available 
              for the selected agent</li>
            <li><b>Delete</b></li>
          </ul>
        </li>
      </ul>
      <p><a href="#top">back to top</a></p>
      <hr>
      <h3>Summary<a name="Summary"></a></h3>
      <ul>
        <li> The screen displays a quick overview of the number of agents for 
          each download status. Future enhancements to this screen will allow 
          drill downs and expanded reporting features to get details for each 
          agent. </li>
      </ul>
      <p><a href="#top">back to top</a></p>
      <hr>
      <h3>Add New<a name="Add_New"></a>...</h3>
      <ul>
        <li>A carrier can add agents to TEAM-UP Download without knowing all of 
          the information required to fully define an agent.</li>
        <li>Required Fields: 
          <ul>
            <li>Agency ID: this is the carrier's ID for the agency</li>
            <li>Agency Name</li>
            <li>Import File: If a unique filename is associated with each agency, 
              this field will be display. If the carrier uses the agency code 
              that is part of the AL3 data file to identify the agent, then this 
              field will not be present. (Note: The agency identification method 
              is set during the initial installation of TEAM-UP Download.)</li>
            <li>Participant Codes: Check this box to indicate that this agent 
              has multiple agency codes that get downloaded for this agency. If 
              this option is selected, a subsequent screen will be displayed that 
              will capture the participant codes for the agent.</li>
            <li>Initial Password: agent is required to use this password when 
              they enter the system to register</li>
          </ul>
        </li>
        <li>Optional Fields: 
          <ul>
            <li>All other fields are optional during the initial setup of the 
              agency. All optional fields will be completed during the agency 
              registration process</li>
          </ul>
        </li>
        <li>Send Registration Email: 
          <ul>
            <li>If checked, an invitation will be sent to the new agent with a 
              welcome that contains a link back into the system where the agent 
              can complete the registration process.</li>
            <li>If checked, the agent's contact email address will be required</li>
          </ul>
        </li>
        <li>All fields except Agency ID can be edited by the agent during the 
          agent registration process</li>
      </ul>
      <ul>
        <li><b><font color="#FF0000">Screen Actions</font></b> 
          <ul>
            <li><b>Cancel</b></li>
            <li><b>Save</b></li>
          </ul>
          <p>&nbsp;</p>
        </li>
      </ul>
      <p>Note: If the &quot;Participant Codes&quot; field was checked, the &quot;Manage 
        Participant Codes&quot; screen will be presented to complete the New Agent 
        definition. </p>
      <p><a href="#top">back to top</a></p>
      <hr>
      <h3>Edit Existing<a name="Edit_Existing"></a>... </h3>
      <ul>
        <li> 
          <p>A list of agents is provided. A link is available on the &quot;Agency 
            ID&quot; and the &quot;Agency Name&quot;. These links drill down to 
            the detail configuration screen for the selected agent.</p>
        </li>
      </ul>
      <ul>
        <li><b><font color="#FF0000">Screen Actions</font></b> 
          <ul>
            <li><b>Cancel</b></li>
            <li><b>Save</b></li>
            <li><b>Manage Participant Codes</b></li>
          </ul>
        </li>
      </ul>
      <p><a href="#top">back to top</a></p>
      <hr>
      <h3><b>Manage Participant Codes<a name="Manage_Part_Codes"></a></b></h3>
      <p>Participant Codes (sometimes referred to as an agency &quot;sub-code&quot;) 
        can be defined to associate multiple agency codes with one Agency. Defining 
        the &quot;Parent-Child&quot; or &quot;One-to-Many&quot; relationship will 
        allow the agent to perform one download session instead of initiating 
        a download session for each agency code.</p>
      <p>Depending on the agency identification method (unique filename or agency 
        code within the AL3), the &quot;Manage Participant Code&quot; screen will 
        have either one or two fields per Participant Code</p>
      <ul>
        <li>If a unique filename is used to identify the each agency, then the 
          agent code as well as the filename must be defined for each &quot;child&quot; 
          agency.</li>
        <li>If the agent code in the AL3 is used to identify the agent, then only 
          the agent code is required to associate an &quot;child&quot; agent. 
          <p><i>(Note: The agency identification method is set during the installation 
            of TEAM-UP Download. This is something that will not likely change.)</i></p>
        </li>
      </ul>
      <p><a href="#top">back to top</a></p>
      </td>
  </tr>
  <tr> 
    <td width="3%"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td colspan="2" bgcolor="#FF0000"> 
      <h2><a name="Carrier_Reports"><font color="#FFFFFF">Reports</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td colspan="2"> 
      <h3>Transaction Log per Trading Partner<a name="TransLog"></a>...</h3>
      <ul>
        <li>Provides a detail activity report for an individual agent. Both Import 
          and Download activities are listed on the carrier version of this report. 
        </li>
      </ul>
      <p><a href="#top">back to top</a> </p>
      <hr>
      <h3>Consolidated Activity Report for All Agents<a name="Consolidated_Report"></a>... 
      </h3>
      <ul>
        <li>Provides a summary activity report for all agents. The report can 
          display Imports, Downloads or both types of activities on the report 
          for a specified time period</li>
      </ul>
      <p><a href="#top">back to top</a></p>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td width="67%" height="12">&nbsp;</td>
    <td height="12" width="30%">&nbsp;</td>
  </tr>
</table>
<p>&nbsp;</p></body>
</html>
