<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
<title>TEAM-UP Download</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body bgcolor="#FFFFFF" text="#000000">
<p><img src="<c:url value="/images/TEAMUP_Download.jpg"/>" width="450" height="73"></p>
<table width="100%" border="0" cellpadding="0" name="Help_Body">
  <tr> 
    <td colspan="2" bgcolor="#000000"> 
      <h1><font color="#FFFFFF"><a name="General_Info">Agency Rollout FAQ</a></font></h1>
    </td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td bgcolor="#FFFFFF"> <br>
      <ul>
        <li><a href="#Sched_not_run">Scheduled task does not run</a></li>
        <li><a href="#Continue_not_appear">Continue button does not appear on 
          first registration page</a></li>
        <li><a href="#Malicious_Script">Malicious Script Warning</a></li>
        <li><a href="#Create_Icon_task_error">Problems creating the scheduled 
          task and/or desktop icons</a></li>
        <li><a href="#Add_new_pc">Problems adding new PC for download</a> (for 
          an existing/registered agent)</li>
        <li><a href="#Download_alert">What is a Download Alert!</a></li>
      </ul>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%"> 
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><a name="Sched_not_run"><font color="#FF0000">Scheduled task does not 
        run</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" valign="top"> 
      <div align="right"></div>
    </td>
    <td bgcolor="#FFFFFF"> 
      <ul>
        <li>If the system is either Windows 2000 or XP, then the problem is probably 
          the password on the scheduled task. In order to correct this problem, 
          update the password either through the TEAM-UP screen or go to Control 
          Panel &raquo Scheduled Tasks, and right-click on the task and select 
          &quot;Properties&quot;. Click on the &quot;Set Password&quot; button 
          and enter the password. Click &quot;Apply&quot;, then &quot;OK&quot;. 
        </li>
      </ul>
      <ul>
        <li>The machine that runs the download will have to be turned on and logged 
          on to the network in order for the download scheduler to perform correctly. 
          In the case of newer operation systems, the machine can be &quot;locked&quot;</li>
      </ul>
      <ul>
        <li>Scheduled tasks run for the currently logged in user, therefore, the 
          scheduled task automatically created during the TEAM-UP Download registration 
          process will run ONLY when the currently logged in user has a defined 
          TEAM-UP Download task. If multiple users share the download machine, 
          it is recommended that each user create a Download task. (This can be 
          completed via the &quot;Add/Modify Scheduled Download&quot; option from 
          the <b>Preferences</b> menu.</li>
      </ul>
    </td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td bgcolor="#CCCCCC"><b>For ALL of the above fixes:</b><br>
      In order to test the scheduled task, right-click on the task and select 
      &quot;Run&quot;. Within a second or two, a browser should pop up and initiate 
      the download.</td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td bgcolor="#FFFFFF"> 
      <p>&nbsp;</p>
      <p><a href="#General_Info">back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%"> 
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><a name="Continue_not_appear"><font color="#FF0000">Continue button 
        does not appear on first registration page</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" valign="top"> 
      <div align="right">1.</div>
    </td>
    <td>This is a known issue with Windows NT. Supported operating systems are 
      95, 98, 2000 and XP.</td>
  </tr>
  <tr> 
    <td width="3%" valign="top"> 
      <div align="right">2.</div>
    </td>
    <td> 
      <p>Security settings on the machine may prevent downloading or running <b>signed 
        ActiveX controls. </b></p>
      <ul>
        <li>From the Tools menu, select Internet Options and click on the security 
          tab. </li>
        <li>Click on the &quot;Trusted Sites&quot; icon and then click on the 
          &quot;Sites&quot; button</li>
        <li>Add the exact URL for your TEAM-UP download site and click &quot;OK&quot;</li>
        <li>Set the &quot;Security level for this zone&quot; to <b>Medium </b>by 
          dragging the slidder, click &quot;Apply&quot;, the &quot;OK&quot;</li>
        <li>Close the browser; open a new browser session and try the registration 
          again. </li>
      </ul>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12" valign="top"> 
      <div align="right">3.</div>
    </td>
    <td height="12"> 
      <p>It is possible that the currently logged in user does not have rights 
        to load software on the machine. A security policy may have been applied 
        to the machine. Log in as the administrator for the machine and try the 
        registration process again (this may require assistance from a network 
        administrator). </p>
      <p>This problem is more likely to occur in larger agencies that have a full-time 
        network administrator that maintains the PC's and manages all of the software.</p>
      <p><a href="#General_Info">back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12"> 
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><a name="Malicious_Script"><font color="#FF0000">Malicious Script Warning</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td> 
      <p>These warning messages are is caused by anti-virus software. If a warning 
        message about a &quot;malicious&quot; script appears, select &quot;Authorize&quot; 
        or &quot;Always Run&quot; to allow the script to run.</p>
      <p><a href="#General_Info">back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12"> 
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><a name="Create_Icon_task_error"><font color="#FF0000">Problems creating 
        the scheduled task and/or desktop icons</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <ul>
        <li>The agent will need to specify if the download will run on a &quot;scheduled&quot; 
          or &quot;interactive&quot; basis. 
          <ul>
            <li>Interactive downloads can be run at any time by clicking on a 
              desktop icon.</li>
            <li>Scheduled downloads can be defined to run unattended any time 
              day or night.</li>
            <li>For security purposes, setting up the scheduled task requires 
              that the user enter their network password to authenticate the task 
              when it is created. (NOTE: The password does not have to be entered 
              at runtime.)</li>
          </ul>
        </li>
        <li>If they agency runs on a scheduled basis, the download should run 
          at a time that does not interfere with any server backups or nightly 
          maintenance schedules.</li>
        <li>Scheduler Notes: &nbsp;See the flowchart below for additional notes 
          regarding setting up the scheduler:</li>
      </ul>
      <p align="center"><img src="<c:url value="/images/Registration.jpg"/>" width="642" height="495"></p>
      <p><a href="#General_Info">back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td> 
      <h2><font color="#FF0000"><a name="Add_new_pc">Problems adding new PC for 
        download</a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td>
      <p>See: <a href="#Continue_not_appear">Continue button does not appear on 
        first registration page</a>. </p>
      <p><a href="#General_Info">back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr>
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td> 
      <h2><font color="#FF0000"><a name="Download_alert">What is a Scheduled 
        Download Alert!</a></font></h2>
    </td>
  </tr>
  <tr>
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p>The TEAM-UP Download application can be configured to monitor agents 
        download frequency. The Download Alert report can be configured to run 
        each day (at a specified time) and &quot;alert&quot; agents via email 
        if their download was not requested -- this could help catch an issue 
        with an agents system before it becomes a real problem.</p>
      <ul>
        <li>By default, agents that have chosen to download via a scheduled task 
          are expected to request a download file every week day. </li>
        <li>Agents that are not on a scheduled download are expected to request 
          their download at least every 5 days.</li>
      </ul>
      <p><a href="#General_Info">back to top</a></p>
    </td>
  </tr>
</table>
<p>&nbsp;</p></body>
</html>
