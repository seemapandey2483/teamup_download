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
      <h1><b><font color="#FFFFFF"><a name="top">Agent Administration</a></font></b></h1>
    </td>
  </tr>
  <tr> 
    <td width="3%"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td colspan="2" bgcolor="#FF0000"> 
      <h2><a name="Agent_Registration"><font color="#FFFFFF">Agency Registration 
        Wizard...</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td colspan="2"> For help with the agency registration, please click <a href="Help_Registration.jsp">here</a>. 
      <p><font color="#0000FF">NOTE: Any configuration settings can be updated 
        from the &quot;Preferences&quot; menu.</font></p>
      <p>&nbsp;</p>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td colspan="2" height="12" bgcolor="#FF0000"> 
      <h2><a name="Agent_Actions"><font color="#FFFFFF">Actions</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td colspan="2" valign="top"> 
      <h3>Download Current...</h3>
      <ul>
        <li>This Action will download all &quot;Current Download Files&quot; for 
          the agent and any Archived files/policies that have been flagged for 
          download.</li>
      </ul>
      <hr>
      <h3>Work with Archive...</h3>
      <p>Every Agent has the ability to administer their own archive. The Archive 
        screen provides the agent with the following functionality:</p>
      <ul>
        <li> View all archive files (default view for this screen)</li>
        <li>View individual transactions within each archive file (see Screen 
          Actions: <i>Show Transactions</i>)</li>
        <li>Select entire archive files</li>
        <li>Select individual transaction from within an archive file (<i>Show 
          Transactions</i> must have been selected)</li>
        <li>Select the &quot;Type&quot; of download to perform from the archive 
          <ul>
            <li>Download</li>
            <li>Resend/Synch</li>
          </ul>
        </li>
      </ul>
      <ul>
        <li><b><font color="#FF0000">Screen Actions</font></b> 
          <ul>
            <li><b>Show Transactions: </b>expands the default view to display 
              each individual transaction (policy) within each of the files in 
              the archive</li>
            <li><b>Save Changes</b></li>
            <li><b>Download Selected Files: </b>Runs the download process for 
              all selected files.</li>
          </ul>
        </li>
      </ul>
      <blockquote> 
        <p>&nbsp;</p>
      </blockquote>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="15"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td colspan="2" height="15" bgcolor="#FF0000"> 
      <h2><a name="Agent_Preferences"><font color="#FFFFFF">Preferences</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td colspan="2" valign="top"> 
      <h3>View Configuration Settings</h3>
      <ul>
        <li>This screen presents the data that was entered during the registration 
          process. Basic information such as agency name, contact information 
          and basic system settings are presented. In order to change any of the 
          system settings, select the menu item from the <b>Preference</b> menu 
          that correspond to the system setting that you wish to change. </li>
      </ul>
      <hr>
      <h3>Change Contact Information...</h3>
      <p>The screen allows agents to update the contact information (name, email 
        address, phone number) used by the download system.</p>
      <hr>
      <h3>Change Password...</h3>
      <p>Agents can change their password at any time. The system will ask you 
        to enter the password twice in order to verify the password.</p>
      <p><font color="#0000FF"><i>Note: some carriers have turned this feature 
        &quot;off&quot; in order to synchronize TEAM-UP passwords with other passwords 
        within their web site.</i></font> </p>
      <hr>
      <h3>Change Agency System Settings...</h3>
      <p>This menu item will walk you through a series of screens to gather and 
        verify the new system settings. Agents should use this menu item when 
        they have upgraded to a new version of their agency management system 
        or have changed to a new agency system.</p>
      The screens will ask many of the same questions that are asked during the 
      initial registration process (see that section for more information). See 
      the <a href="Help_Registration.jsp">Agency Registration Wizard...</a> section 
      of this help file for more details. 
      <hr>
      <h3>Add/Modify Scheduled Download</h3>
      <ul>
        <li>You will be prompted for the time that you wish the download to run. 
          When TEAM-UP Download is running on newer operating systems, this screen 
          will also request your network password. This is required by your operating 
          system in order to run the scheduled download (TEAM-UP does NOT read 
          or store you network password).</li>
        <li>Schedule Download Instructions: a link is provided that will walk 
          an agent through setting up a scheduled process within their Windows 
          scheduler. This will allow an agent to run TEAM-UP Download in an unattended 
          mode 
          <ul>
            <li><i>NOTE: Download Session Mode: must be set to &quot;Scheduled 
              / Non-interactive&quot;.</i>.</li>
          </ul>
        </li>
      </ul>
      <hr>
      <h3>Create Desktop Icons...</h3>
      <p>TEAM-UP Download also allows you to retrieve your download files &quot;Interactively&quot;. 
        This setting will allow you to pick-up your download files by double-clicking 
        on an icon on your computer's desktop. Two types of icons are available:</p>
      <ul>
        <li>Immediately getting your download: This option will provide a desktop 
          that will automatically log in to retrieve any download files and then 
          automatically logs off of the download system. This is a very convenient 
          option to pickup your daily download files.</li>
        <li>Accessing your archived files and configuration settings: This options 
          brings you to the log-on screen of the download system. Once logged 
          on to the system, you can access the entire application including: 
          <ul>
            <li>Current download files</li>
            <li>Archived files</li>
            <li>Configuration settings</li>
            <li>Online reports</li>
          </ul>
        </li>
      </ul>
      <p><b><i><font color="#0000FF">NOTE: you can run downloads via the icons 
        even if you have setup TEAM-UP download to run on a scheduled basis.</font></i></b></p>
      <hr>
      <h3>Add New PC for Download...</h3>
      <p>This process guides the agent through the registration process. This 
        process should be completed when the agent has replaced their download 
        server or has added a new PC that will perform download transactions.</p>
      <blockquote> 
        <p>&nbsp;</p>
      </blockquote>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="15"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td colspan="2" height="15" bgcolor="#FF0000"> 
      <h2><a name="Agent_Reports"><font color="#FFFFFF">Reports</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td colspan="2" height="12"> 
      <h3>Transaction Log...</h3>
      <h3></h3>
      <ul>
        <li>Provides a detail activity report for the agent currently logged in 
          to the system. Download and Resend activities are listed on the agency 
          version of this report. </li>
        <li>Note: for the exact time for any activity, hover over the date and 
          the time will be displayed.</li>
        <li>This report can be sorted by any column heading by clicking on the 
          arrow beside the column name. The 'active' sort is displayed in <font color="#FF0000">RED<font color="#000000">.</font></font></li>
      </ul>
      <p>&nbsp;</p>
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
