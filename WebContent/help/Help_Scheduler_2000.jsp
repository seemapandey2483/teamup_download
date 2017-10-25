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
      <h1><b><font color="#FFFFFF">TEAM-UP Download Scheduling</font></b></h1>
    </td>
  </tr>
  <tr> 
    <td width="3%" height="19"><img src="<c:url value="/images/NN_bullet_small_2.jpg"/>" width="25" height="26"></td>
    <td colspan="2" height="19" bgcolor="#FF0000"> 
      <h2><a name="Carrier_Actions"><font color="#FFFFFF">Set Up a Scheduled Download</font></a></h2>
    </td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td colspan="2">
<h3>Windows 2000 Scheduler Setup</h3>
      <p><i><font color="#0000FF">Note: If Windows Scheduler is not installed 
        on your machine, go to Windows Help and type in &quot;Scheduled Tasks&quot; 
        in the search field and follow the instructions to install the Scheduler.</font></i></p>
      <p>From the Windows 2000 Control Panel, open the &quot;Scheduled Tasks&quot; 
        and then add a new task to the scheduler using the &quot;Add Scheduled 
        Task&quot; wizard. Previous versions of Windows follow very similar procedures 
        for adding scheduled tasks. </p>
      <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<c:url value="/images/sched1_icon1.jpg"/>" width="106" height="116">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
        <img src="<c:url value="/images/sched2_icon2.jpg"/>" width="139" height="111"></p>
      <p>&nbsp;<BR>&nbsp;</p>
      <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="<c:url value="/images/sched3_wiz1.jpg"/>" width="441" height="318"></p>
      <p>&nbsp;<BR>&nbsp;</p>
      <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="<c:url value="/images/sched4_wiz2.jpg"/>" width="441" height="318"></P>
      <p>&nbsp;<BR>&nbsp;</p>
      <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="<c:url value="/images/sched5_wiz3.jpg"/>" width="441" height="318"></P>
      <p>&nbsp;<BR>&nbsp;</p>
      <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="<c:url value="/images/sched6_wiz4.jpg"/>" width="441" height="318"></P>
      <p>&nbsp;<BR>&nbsp;<BR>&nbsp;</p>
      <p>Windows should default the user name with the current DOMAIN\username, 
      however, Windows will <B>not</B> default the password field (for security 
      purposes).  Please enter and confirm your password.<BR>
		<i>Note: This dialog box will not be displayed in Windows 98. Please
		continue with the next step.</i></p>
      <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="<c:url value="/images/sched7_wiz5.jpg"/>" width="441" height="318"></P>
      <p>&nbsp;<BR>&nbsp;<BR>&nbsp;</p>
      <P>Check the "Open advanced properties..." box and click "Finish."</p>
      <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="<c:url value="/images/sched8_wiz6.jpg"/>" width="441" height="318"></P>
      <p>&nbsp;<BR>&nbsp;<BR>&nbsp;</p>
      <p>On the "Configuration Settings" page of the TEAM-UP Download Agency
      Administration app, highlight the link that is displayed &quot;for use in 
      initiating a download.&quot; &nbsp;Right click on the selected text and 
      select &quot;Copy.&quot;</p>
      <p>In the &quot;Task&quot; section of the scheduled task wizard (see below), 
        go to the &quot;Run:&quot; prompt. At the end of the defaulted command 
        insert a blank space, then paste the copied link by pressing Ctrl-V (hold 
        down the &quot;Ctrl&quot; key and press the letter v). Click &quot;OK&quot; 
        to save.</p>
      <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="<c:url value="/images/sched9_wiz7.jpg"/>" width="406" height="448"></p>
      <p>&nbsp;<BR>&nbsp;<BR>&nbsp;</p>
      <P>The scheduled task has now been added... it will run at the schedule 
        time each day.</p>
      <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<c:url value="/images/sched10_icon3.jpg"/>" width="144" height="139"> 
      </P>
      <p><b>From within the Scheduled Tasks window, you can change the properties 
        of the task or manually initiate the task to run immediately:</b></P>
      <ol>
        <li>Double-click on the task icon to view/change the properties of the 
          task</li>
        <li>Right-click on the task icon and select &quot;Run&quot; from the drop-down 
          menu to run the task</li>
      </ol>
      <p><b>Disable the Scheduled Task</b></p>
      <p>NOTE: If for any reason you need to stop your downloads, you can &quot;disable&quot; 
        the task by double-clicking on the icon and deselect the &quot;Enabled&quot; 
        setting on the task. Click &quot;OK&quot; when done. </p>
      <p>You can enable this task when you are ready to start receiving downloads 
        again.</p>
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
