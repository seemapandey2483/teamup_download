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
      <h1><font color="#FFFFFF"><a name="General_Info">Agency Rollout Procedures</a></font></h1>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="610">&nbsp;</td>
    <td> 
      <p>TEAM-UP Download's Automated Registration performs the vast majority 
        of the tasks required to set up agencies, but, in some cases, additional 
        procedures are required for completing the setup.</p>
      <table width="60%" border="1">
        <tr>
          <td bgcolor="#FFFF99" bordercolor="#000000">
            <div align="center"> For additional instructions per agency system, 
              <a href="#PerSystem">click here</a> ] </div>
          </td>
        </tr>
      </table>
      <p><b>General Registration Notes:</b></p>
      <p>It is recommended that Agents be notified that they will need the following 
        information at the time of registration:</p>
      <p><b>Scheduler and Desktop Icons:</b></p>
      <ul>
        <li>The agent will need to specify if the download will run on a &quot;scheduled&quot; 
          or &quot;interactive&quot; basis. 
          <ul>
            <li>Interactive downloads can be run at any time by clicking on a 
              desktop icon.</li>
            <li>Scheduled downloads can be defined to run unattended any time day 
              or night.</li>
            <li>For security purposes, setting up the scheduled task requires 
              that the user enter their network password to authenticate the 
              task when it is created. (NOTE: The password does not have to be 
              entered at runtime.)</li>
          </ul>
        </li>
        <li>If they agency runs on a scheduled basis, the download should run 
          at a time that does not interfere with any server backups or nightly 
          maintenance schedules.</li>
        <li>Scheduler Notes: &nbsp;See the flowchart below for additional notes regarding 
          setting up the scheduler:</li>
      </ul>
      <p><img src="<c:url value="/images/Registration.jpg"/>" width="665" height="851"></p>
      <p>&nbsp;</p>
      <p><b>Other notes regarding the scheduler: </b></p>
      <ul>
        <li>The machine that runs the download will have to be turned on and logged 
          on to the network in order for the download scheduler to perform correctly. 
          In the case of newer operation systems, the machine can be &quot;locked&quot;</li>
        <li>Scheduled tasks run for the currently logged in user, therefore, the 
          scheduled task automatically created during the TEAM-UP Download registration 
          process will run ONLY when the currently logged in user has a defined 
          TEAM-UP Download task. If multiple users share the download machine, 
          it is recommended that each user create a Download task. (This can be 
          completed via the &quot;Add/Modify Scheduled Download&quot; option from 
          the <b>Preferences</b> menu.)</li>
      </ul>
    </td>
  </tr>
  <tr> 
    <td width="3%"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#000000"> 
      <h2><b><font color="#FFFFFF"><a name="PerSystem"></a>Additional Setup Procedures 
        per Agency System </font></b></h2>
    </td>
  </tr>
  <tr>
    <td width="3%">&nbsp;</td>
    <td>
      <p>Listed below are additional recommended steps per agency system that 
        may have to be incorporated into the registration process.</p>
      <ul>
        <li><a href="#AMS_AFW_4x">AMS - AfW (4.x or below)</a></li>
        <li><a href="#AMS_AFW_v5">AMS - AfW (v5 or above)</a></li>
        <li><a href="#AMS_AFW_Online">AMS - AfW Online</a></li>
        <li><a href="#AMS_Prime">AMS - Agency One / Prime</a></li>
        <li><a href="#AMS_Prime_2000">AMS - Prime 2000</a></li>
        <li><a href="#AMS_Sagitta">AMS - Sagitta</a></li>
        <li><a href="#Applied_TAM">Applied - TAM</a></li>
        <li><a href="#DORIS_32">DORIS - The Agents Choice (DORIS 32)</a></li>
        <li><a href="#DORIS_DOS">DORIS - The Agents Choice (DOS)</a></li>
        <li><a href="#Redshaw_Elite">Redshaw - Elite</a></li>
        <li><a href="#SIS_v5x">SIS SEMCI Partner (5.x or below)</a></li>
        <li><a href="#SIS_v6x">SIS SEMCI Partner (6.x)</a></li>
        <li><a href="#Other">Other</a></li>
      </ul>
    </td>
  </tr>
  <tr> 
    <td width="3%"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><a name="AMS_AFW_4x"><font color="#FFFFFF">AMS - AfW (4.x or below)</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td height="12"> 
      <p><b>Setup Notes:</b></p>
      <ul>
        <li>There are no additional setup procedures for this agency system.</li>
      </ul>
      <p><b>Runtime notes:</b></p>
      <ul>
        <li>Agents will have to select &quot;Process Company Direct&quot; from 
          the &quot;File&quot; menu in order to complete the processing of the 
          download files</li>
      </ul>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><a name="AMS_AFW_v5"><font color="#FFFFFF">AMS - AfW (v5 or above)</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="20">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b></p>
      <ul>
        <li>There are no additional setup procedures for this agency system.</li>
      </ul>
      <p><b>Runtime notes:</b></p>
      <ul>
        <li>Agents will have to select &quot;Process Company Direct&quot; from 
          the &quot;File&quot; menu in order to complete the processing of the 
          download files</li>
      </ul>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><font color="#FFFFFF">AMS - AfW Online<a name="AMS_AFW_Online"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b></p>
      <ul>
        <li>Agents will have to open a support &quot;ticket&quot; with AMS in 
          order to activate &quot;company direct&quot; downloads. This should 
          be done prior to setting up TEAM-UP Download for the agent..</li>
      </ul>
      <p><b>Runtime notes:</b></p>
      <ul>
        <li>Agents will have to select &quot;Process Company Direct&quot; from 
          the &quot;File&quot; menu in order to completed the processing of the 
          download files.</li>
      </ul>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><font color="#FFFFFF">AMS - Agency One / Prime<a name="AMS_Prime"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td height="12">&nbsp;</td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><font color="#FFFFFF">AMS - Prime 2000<a name="AMS_Prime_2000"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b></p>
      <p>The agent will need to define a &quot;Non-IVANS Download Path&quot; with 
        in the Prime2000 system. To do this, have the agent perform the following 
        steps:</p>
      <ul>
        <li>From the &quot;Tools&quot; menu, select &quot;Company Interface...&quot;</li>
        <li>From the &quot;Setup&quot; menu, select &quot;File Locations...&quot;</li>
        <li>Type C:\Teamup in the Non-IVANS Download path as shown below. 
          <ul>
            <li><i>NOTE:</i> This folder will automatically be created during 
              registation process.<i> </i></li>
          </ul>
        </li>
      </ul>
      <p><img src="<c:url value="/images/Prime2kDLSetup.jpg"/>" width="514" height="350"></p>
      <p><b>Runtime notes:</b></p>
      <ul>
        <li><b>NONE</b>: TEAM-UP download files will process in the agent's normal 
          processing cycle. There are no additional procedures for the agent.</li>
      </ul>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><font color="#FFFFFF">AMS - Sagitta<a name="AMS_Sagitta"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><font color="#FFFFFF">Applied - TAM<a name="Applied_TAM"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b></p>
      <ul>
        <li>There are no additional setup procedures for this agency system.</li>
      </ul>
      <p><b>Runtime notes:</b></p>
      <ul>
        <li>NONE: TEAM-UP download files should process in the agent's normal 
          processing cycle.</li>
      </ul>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><font color="#FFFFFF">DORIS - The Agents Choice (DORIS 32)<a name="DORIS_32"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><font color="#FFFFFF">DORIS - The Agents Choice (DOS)<a name="DORIS_DOS"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><font color="#FFFFFF">Redshaw - Elite<a name="Redshaw_Elite"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><font color="#FFFFFF">SIS SEMCI Partner (5.x or below)<a name="SIS_v5x"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><font color="#FFFFFF">SIS SEMCI Partner (6.x)<a name="SIS_v6x"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b></p>
      <ul>
        <li>Agents will have to enable FTP download in the SEMCI Partner system via the &quot;Company Administration&quot; 
          option on the File menu. This process will create a directory for the 
          &quot;Company Direct&quot; downloads. This should be done prior to setting 
          up TEAM-UP Download for the agent.</li>
      </ul>
      <p><b>Runtime notes:</b></p>
      <ul>
        <li>If the agent enabled FTP download for the carrier, there are no extra steps 
          required at runtime for this system. Download files should process in the 
          agent's normal processing cycle.</li>
      </ul>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td bgcolor="#FF0000"> 
      <h2><font color="#FFFFFF"><b>Other<a name="Other"></a></b></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b></p>
      <ul>
        <li>When adding new agency systems to TEAM-UP Download, please contact 
          Ebix,Inc for assistance. In many cases, adding systems 
          does not require a new version or patch to an existing version. In these 
          cases, new systems can be added in a matter of minutes. If a new version 
          is required to support a new agency system, an update will be scheduled 
          at the time of the request.</li>
      </ul>
    </td>
  </tr>
</table>
<p>&nbsp;</p></body>
</html>
