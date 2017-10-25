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
    <td width="3%">&nbsp;</td>
    <td> 
      <p>TEAM-UP Download's Automated Registration performs the vast majority 
        of the tasks required to set up agencies, but, in some cases, additional 
        procedures are required for completing the setup.</p>
      <p>A registration help page is available on the first page of the registration 
        process that is generic to all agency systems. Listed below are additional 
        recommended steps per agency system that may have to be incorporated into 
        the registration process.</p>
      <table width="80%" border="1" align="center">
        <tr bgcolor="#000000"> 
          <td><b><font color="#FFFFFF">Vendor</font></b></td>
          <td><b><font color="#FFFFFF">Agency System</font></b></td>
          <td><b><font color="#FFFFFF">Requires Setup in <br>agency system</font></b></td>
          <td><b><font color="#FFFFFF">Requires Manual<br>Import</font></b></td>
        </tr>
        <tr> 
          <td>AMS</td>
          <td><a href="#AMS_360">AMS - 360</a></td>
          <td>No</td>
          <td>Yes</td>
        </tr>
        <tr> 
          <td>AMS</td>
          <td><a href="#AMS_AFW_4x">AfW (4.x or below)</a></td>
          <td>No</td>
          <td>Yes</td>
        </tr>
        <tr> 
          <td>AMS</td>
          <td><a href="#AMS_AFW_v5">AfW (v5 or above)</a></td>
          <td>No</td>
          <td>Yes</td>
        </tr>
        <tr> 
          <td>AMS</td>
          <td><a href="#AMS_AFW_Online">AfW Online</a></td>
          <td>Yes</td>
          <td>Yes</td>
        </tr>
        <tr> 
          <td>AMS</td>
          <td><a href="#AMS_Prime">Agency One / Prime</a></td>
          <td>Yes</td>
          <td>No</td>
        </tr>
        <tr> 
          <td>AMS</td>
          <td><a href="#AMS_Prime_2000">Prime 2000</a></td>
          <td>Yes</td>
          <td>No</td>
        </tr>
        <tr> 
          <td>AMS</td>
          <td><a href="#AMS_Sagitta">Sagitta Browser</a></td>
          <td>Yes / No</td>
          <td>Yes / No</td>
        </tr>
        <tr> 
          <td>AMS</td>
          <td><a href="#AMS_Satitta_Online">Sagitta Online</a></td>
          <td>Yes</td>
          <td>Yes</td>
        </tr>
        <tr> 
          <td>Applied</td>
          <td><a href="#Applied_TAM"> TAM</a></td>
          <td>No</td>
          <td>No</td>
        </tr>
        <tr> 
          <td>Applied</td>
          <td><a href="#Applied_TAMCentral">TAMCentral</a></td>
          <td>No</td>
          <td>Yes</td>
        </tr>
        <tr> 
          <td>DORIS</td>
          <td><a href="#DORIS_32"> The Agents Choice (DORIS 32)</a></td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
        <tr> 
          <td>DORIS</td>
          <td><a href="#DORIS_DOS"> The Agents Choice (DOS)</a></td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
        <tr> 
          <td>EBIX</td>
          <td><a href="#Redshaw_Elite">Redshaw - Elite</a></td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
        <tr> 
          <td>SIS</td>
          <td><a href="#SIS_v5x">SEMCI Partner (5.x or below)</a></td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
        <tr> 
          <td>SIS</td>
          <td><a href="#SIS_v6x">SEMCI Partner (6.x)</a></td>
          <td>Yes</td>
          <td>No</td>
        </tr>
      </table><br>
      <b>Adding new Agency systems</b><br>
      When adding new agency systems to TEAM-UP Download, please contact Ebix, 
      Inc. for assistance. In many cases, adding systems does not require 
      a new version or patch to an existing version. In these cases, new systems 
      can be added in a matter of minutes. If a new version of TEAM-UP Download 
      is required to support a new agency system, an update will be scheduled 
      at the time of the request. 
      <p>&nbsp;</p>
    </td>
  </tr>
  <tr bgcolor="#000000"> 
    <td colspan="2"> 
      <h1><font color="#FFFFFF">Agency System Notes</font></h1>
    </td>
  </tr>
  <tr> 
    <td width="3%">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><a name="AMS_360"><font color="#FF0000">AMS - 360</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td bgcolor="#FFFFFF"> 
      <p><b>Setup Notes:</b> There are no additional setup procedures for this 
        agency system.</p>
      <b>Runtime notes:</b><br>
      <ul>
        <li>To process a company direct download file on AMS 360, Agents will 
          have to launch 'Toolbox', then 'Integration' &raquo 'Download' &raquo 
          'Move Files' to move the download files to the AMS Data Center and kick 
          off the download programs. 
          <ul>
            <li>Note: A process called 'Store Transactions' is queued as a result 
              of moving the files to the Data Center (this process run approximately 
              every five minutes). Store Transactions will read and process the 
              agents downlaod automatically.</li>
            <li>Note: Agents will need to run 'Assign Transactions' followed by 
              'Process Transactions' as is customary with any download file.</li>
          </ul>
        </li>
      </ul>
      <p><a href="#General_Info">back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><a name="AMS_AFW_4x"><font color="#FF0000">AMS - AfW (4.x or below)</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td height="12"> 
      <p><b>Setup Notes:</b> There are no additional setup procedures for this 
        agency system.</p>
      <p><b>Runtime notes:</b>&nbsp; After receiving the download file, select 
        'Process Company Direct' from the 'File' menu in order to complete the 
        agency system processing of the download files.</p>
      <p><a href="#General_Info">back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><a name="AMS_AFW_v5"><font color="#FF0000">AMS - AfW (v5 or above)</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="20">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b> There are no additional setup procedures for this 
        agency system.</p>
      <p><b>Runtime notes:</b>&nbsp; After receiving the download file, select 
        'Process Company Direct' from the 'File' menu in order to complete the 
        agency system processing of the download files.</p>
      <p><a href="#General_Info">Back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><font color="#FF0000">AMS - AfW Online</font><font color="#FFFFFF"><a name="AMS_AFW_Online"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b> Agents will have to open a support 'ticket' with 
        AMS in order to activate 'company direct' downloads. Ideally, this should 
        be done prior to setting up TEAM-UP Download for the agent.</p>
      <p><b>Runtime notes:</b>&nbsp; &nbsp; To process a direct download file 
        on AfW Online:</p>
      <ul>
        <li> Agents MUST log out of AfW Onine(File, Exit) and then log back in. 
          <ul>
            <li>Note: During the login process, a check is done on c:\expedite 
              to fine any unprocessed download files. </li>
          </ul>
        </li>
        <li>Once back in AfW Online, the agent can then select File &raquo Download 
          &raquo 'Process Company Direct' in order to process the download file(s).</li>
      </ul>
      <p><a href="#General_Info">Back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><font color="#FF0000">AMS - Agency One / Prime<a name="AMS_Prime"></a></font></h2>
      <p><a href="#General_Info">back to top</a></p>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td height="12">
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><font color="#FF0000">AMS - Prime 2000</font><font color="#FFFFFF"><a name="AMS_Prime_2000"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b></p>
      <p>A 'Non-IVANS Download Path' and 'Alternate Filename' will have to be 
        created in the Prime2000 system:</p>
      <ul>
        <li>From the 'Tools' menu, select 'Company Interface...'</li>
        <li>From the 'Setup' menu, select 'File Locations...'</li>
        <li>Type the Non-IVANS Download path into the text box. 
          <ul>
            <li>From the example below, replace 'COMPNAME' with filename listed 
              above.</li>
            <li><i>NOTE: The Prime system will automatically create the directory 
              if it does not exist on your system. Also, the path MUST have a 
              backslash ('\') at the end of the path.</i></li>
          </ul>
        </li>
      </ul>
      <p><img src="<c:url value="/images/Prime_DLSetup.jpg"/>" width="514" height="350"></p>
      <p><b>Runtime notes:</b>&nbsp; TEAM-UP download files will process in the 
        agent's normal processing cycle. There are no additional procedures for 
        the agent.</p>
      <p><a href="#General_Info">Back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="1003"> 
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><a name="AMS_Sagitta"><font color="#FF0000">AMS - Sagitta Browser</font></a></h2>
      <p>Two options have been identified for implementing TEAM-UP Download for 
        Sagitta Browser. The AMS recommended options requires some setup and runtime 
        activities. An alternative option uses the same processing steps as the 
        IVANS download and does not require any additional setup or runtime procedures. 
      </p>
      <p><b>NOTE:</b> The options selected by the carrier for its Sagitta Browser 
        agents should be configured in the 'Preferences' &raquo 'Agency Vendor 
        System' maintenance screen. The option selected will apply to ALL Sagitta 
        Browser agents!</p>
      <p><font color="#FF0000"><b>AMS Recommended Option: </b></font></p>
      <p><b>Setup Notes:</b> Add an entry to the 'Import &amp; Export Personalization' 
        screen using the following values:</p>
      <ul>
        <li>Program ID: SA.600</li>
        <li>Staff ID: leave blank</li>
        <li>Product: NONIVANS (Import Program will display (BROWSER ACORD IMPORT)</li>
        <li>Data Dir: c:\teamup\carrier_name [where 'carrier_name' is replaced 
          with directory used earlier in the registration process. This directory 
          is also displayed on the 'Preferences' &raquo 'View configuration settings' 
          page.</li>
        <li>File Mask: [See filename listed above]</li>
      </ul>
      <p><b>Runtime notes:</b> </p>
      <ul>
        <li>After receiving the download file, select the Import/Export icon. 
          The system will prompt you to import the file.</li>
        <li>The file must manually delete manually after it is imported.</li>
      </ul>
      <b>TEAM-UP Agency Vendor System - Setup Parameters:</b><br>
      Below are the TEAM-UP Download defaults for use with this option: 
      <ul>
        <li>Default Download Directory: [see 'Data Dir' listed above]</li>
        <li>Default Download Filename: [See filename listed above]</li>
      </ul>
      <p><font color="#FF0000"><b>Alternative Option:</b></font></p>
      <p><b>Setup Notes:</b> There are no additional setup procedures.</p>
      <p><b>Runtime notes:</b> There are no runtime procedures</p>
      <b>TEAM-UP Agency Vendor System - Setup Parameters:</b><br>
      Below are the TEAM-UP Download defaults for use with this option: 
      <ul>
        <li>Default Download Directory: [drive]:\IVANS (where 'drive' is either 
          'c' or a network drive).</li>
        <li>Default Download Filename: Sagitta will accept any filename. 'coname.AL3' 
          is recommended where 'coname' is an abbreviation of the company name.</li>
      </ul>
      <p><a href="#General_Info">Back to top</a></p>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td bgcolor="#FFFFFF">
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><a name="AMS_Satitta_Online"><font color="#FF0000">AMS - Sagitta Online</font></a></h2>
      <blockquote>
        <p> [see notes for Sagitta Browser]</p>
      </blockquote>
      <p><a href="#General_Info">back to top</a></p>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><a name="Applied_TAM"><font color="#FF0000">Applied - TAM</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b> There are no additional setup procedures for this agency system.</p>
      <p><b>Runtime notes:</b> After receiving the download file, TEAM-UP download 
        files should process in the agent's normal processing cycle. No additional 
        steps are required.</p>
      <p><a href="#General_Info">Back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><a name="Applied_TAMCentral"><font color="#FF0000">Applied - TAMCentral</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td bgcolor="#FFFFFF"> 
      <p><b>Setup Notes:</b> There are no additional setup procedures for this agency system.</p>
      <p><b>Runtime notes:</b> After receiving the download file, run the TAMCentral 
        'Upload Center' to transfer the file to TAMCentral. The directory for 
        the TEAM-UP Download files is listed above.</p>
      <p><a href="#General_Info">Back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><font color="#FF0000">DORIS - The Agents Choice (DORIS 32)</font><font color="#FFFFFF"><a name="DORIS_32"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b> There are no additional setup procedures for this 
        agency system.</p>
      <p><b>Runtime notes:</b> NONE - TEAM-UP download files should process in 
        the agent's normal processing cycle.</p>
      <p><a href="#General_Info">back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><font color="#FF0000">DORIS - The Agents Choice (DOS)</font><font color="#FFFFFF"><a name="DORIS_DOS"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b> There are no additional setup procedures for this 
        agency system.</p>
      <p><b>Runtime notes:</b> NONE - TEAM-UP download files should process in 
        the agent's normal processing cycle.</p>
      <p><a href="#General_Info">back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><font color="#FF0000">Redshaw - Elite</font><font color="#FFFFFF"><a name="Redshaw_Elite"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><a href="#General_Info">back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><font color="#FF0000">SIS SEMCI Partner (5.x or below)</font><font color="#FFFFFF"><a name="SIS_v5x"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><a href="#General_Info">back to top</a></p>
      <hr>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">
      <h2><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></h2>
    </td>
    <td bgcolor="#FFFFFF"> 
      <h2><font color="#FF0000">SIS SEMCI Partner (6.x)</font><font color="#FFFFFF"><a name="SIS_v6x"></a></font></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="12">&nbsp;</td>
    <td> 
      <p><b>Setup Notes:</b>&nbsp; Enable FTP download in the SEMCI Partner system 
        via the 'Company Administration' option on the 'File' menu. This process 
        will create a directory for the 'Company Direct' downloads. This should 
        be done prior to setting up TEAM-UP Download for the agent.</p>
      <p><b>Runtime notes:</b>&nbsp; Download files should process in the agent's 
        normal processing cycle.</p>
      <p><a href="#General_Info">Back to top</a></p>
    </td>
  </tr>
</table>
<p>&nbsp;</p></body>
</html>
