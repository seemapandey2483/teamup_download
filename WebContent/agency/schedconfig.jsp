<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.SchedulerConfigDisplayBean" />
<TITLE><%= DisplayBean.getCarrierName() %> - TEAM-UP Download Agency Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--

<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	var remove_button_anchor;
	for (count = 0; count < document.anchors.length; count++)
	{
		if (document.anchors[count].name == "removeButton")
		{
			remove_button_anchor = document.anchors[count];
		}
	}

	document.tudlform.schedulerConfig.TaskName = "<%= DisplayBean.getTaskName() %>";
	document.tudlform.schedulerConfig.retrieveScheduledTask();

	if (document.tudlform.schedulerConfig.Status == 0)
	{
		var hour = 0;
		hour = parseInt(document.tudlform.schedulerConfig.StartHour);
		if (hour >= 12)
		{
			document.tudlform.dlhour.value = hour - 12;
			document.tudlform.dlampm.value = 12;
		}
		else
		{
			document.tudlform.dlhour.value = hour;
			document.tudlform.dlampm.value = 0;
		}
		
		document.tudlform.dlminute.value = document.tudlform.schedulerConfig.StartMinute;
		document.tudlform.timeout.value = document.tudlform.schedulerConfig.TimeoutMinutes;
		document.tudlform.password.value = "";
		
		// Enable the "Remove Scheduled Action" button
		remove_button_anchor.setAttribute("innerText", "Remove Scheduled Download");
		remove_button_anchor.setAttribute("href", "javascript:do_remove()");
	}
	
	// get the logged in user and hardcode that value
	document.tudlform.schedulerConfig.getCurrentUser();
	document.tudlform.username.value = document.tudlform.schedulerConfig.Username;
	document.tudlform.username.disabled = true;
	
	document.tudlform.dlhour.focus();
	return true;
}

function allow_action()
{
	if (document.tudlform.data_changed.value == "Y")
	{
		msg = "You are about to lose any changes you have made on this page.  To save your changes, click 'Cancel' and then use the 'Save' button at the bottom of the screen.";
		if (!confirm(msg))
			return false;
	}
	
	return true;
}

function throw_change_flag()
{
	document.tudlform.data_changed.value = "Y";
}

function do_save()
{
	if (!document.tudlform.dlhour.value || !document.tudlform.dlminute.value || !document.tudlform.dlampm)
	{
		alert("Please enter the download time.");
		document.tudlform.dlhour.focus();
	}
	else if (!document.tudlform.timeout.value)
	{
		alert("Please select your Internet connection speed.");
		document.tudlform.timeout.focus();
	}
	else if (document.tudlform.username.value && !document.tudlform.password.value)
	{
		alert("You must enter your windows password.");
		document.tudlform.password.focus();
	}
	else
	{
		// calculate the download hour
		var hour = 0;
		hour = parseInt(document.tudlform.dlhour.value) + parseInt(document.tudlform.dlampm.value);
		document.tudlform.schedulerConfig.TaskName = "<%= DisplayBean.getTaskName() %>";
		document.tudlform.schedulerConfig.DownloadURL = "<%= DisplayBean.getDownloadURL() %>";
		document.tudlform.schedulerConfig.StartHour = hour;
		document.tudlform.schedulerConfig.StartMinute = document.tudlform.dlminute.value;
		document.tudlform.schedulerConfig.TimeoutMinutes = document.tudlform.timeout.value;
		document.tudlform.schedulerConfig.Username = document.tudlform.username.value;
		document.tudlform.schedulerConfig.Password = document.tudlform.password.value;
		document.tudlform.schedulerConfig.createScheduledTask();
		if (document.tudlform.schedulerConfig.Status == 0)
		{
			do_page_action("scheduler_save");
		}
		else if (document.tudlform.schedulerConfig.Status == 2)
		{
			alert("Could not create the scheduled task.  Make sure the Windows Scheduler service is running.");
		}
		else if (document.tudlform.schedulerConfig.Status == 3)
		{
			alert("The password you entered is invalid.  Please enter the correct password.");
			document.tudlform.password.focus();
		}
		else if (document.tudlform.schedulerConfig.Status == 5)
		{
			alert(document.tudlform.schedulerConfig.Error);
		}
		else
		{
			alert("An error occurred while trying to create the scheduled task.");		
		}
	}
}

function do_remove()
{
	if (!confirm("Do you wish to remove the automated download from your Windows Scheduler?"))
		return;

	document.tudlform.schedulerConfig.TaskName = "<%= DisplayBean.getTaskName() %>";
	document.tudlform.schedulerConfig.deleteScheduledTask();
	if (document.tudlform.schedulerConfig.Status == 0)
		do_page_action("scheduler_delete");
	else
		alert("An error occurred while trying to delete the scheduled task.");
}
// -->
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="return do_load();">
<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" mm:layoutgroup="true" bgcolor="#FFFFFF">
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="actionlogo"><jsp:include page="action_logo.jsp" flush="true" /></TD>
          <TD class="actionbar" nowrap="nowrap"><jsp:include page="actionbar_nohome.jsp" flush="true" /></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap">
    	<a href="javascript:void(0);" onClick="javascript:open_prn_win('agency/print_schedconfig.jsp')" class="menu">Print Instructions</a>
	</TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">
    <INPUT type="hidden" name="newRegistration" value="<%= DisplayBean.getNewRegistration() %>">
	  <OBJECT id="schedulerConfig" codeBase="controls/<%= DisplayBean.getCodebase() %>" classid="<%= DisplayBean.getClassid() %>">
	  </OBJECT>
	

<!-- BEGIN BODY -->

      <H1>Schedule Automatic Download <IMG border="0" src="<c:url value="/images/qBubble.gif"/>" alt="Select time for scheduled download and provide the type of internet connection available.  A password is required by Windows 2000, NT and XP in order to create a scheduled task." width="20" height="22"><BR>&nbsp;<BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="40%">Download Time:</TD>
            <TD class="qcell_text" width="60%">
            	<select name="dlhour">
            		<option value="">&nbsp;</option>
            		<option value="1">1</option>
            		<option value="2">2</option>
            		<option value="3">3</option>
            		<option value="4">4</option>
            		<option value="5">5</option>
            		<option value="6">6</option>
            		<option value="7">7</option>
            		<option value="8">8</option>
            		<option value="9">9</option>
            		<option value="10">10</option>
            		<option value="11">11</option>
            		<option value="0">12</option>
            	</select>
            	<select name="dlminute">
            		<option value="">&nbsp;</option>
            		<option value="0">00</option>
            		<option value="15">15</option>
            		<option value="30">30</option>
            		<option value="45">45</option>
            	</select>
            	<select name="dlampm">
            		<option value="">&nbsp;</option>
            		<option value="0">AM</option>
            		<option value="12">PM</option>
            	</select>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="40%">Internet Connection Speed:</TD>
            <TD class="qcell_text" width="60%">
            	<select name="timeout">
            		<option value="">&nbsp;</option>
            		<option value="15">High Speed (DSL, Cable, etc.)</option>
            		<option value="60">Dial-Up</option>
            	</select>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2" align="center">Please enter your Windows password below (Windows 95/98/ME users can skip this step):</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Windows Username:</TD>
            <TD class="qcell_text"><INPUT type="text" name="username" size="30" maxlength="50"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Windows Password:</TD>
            <TD class="qcell_text"><INPUT type="password" name="password" size="30" maxlength="50"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2" align="center"><strong>Note for Windows NT/2000/XP Users:</strong></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2" align="center">You must be logged into your computer for Automatic Download to work.  If your download is scheduled for overnight, then for security purposes, we recommend that you lock your computer when you leave work.  To lock your computer, hold down the CTRL ALT and DELETE keys at the same time, then click "Lock Computer".</TD>
          </TR>

        </TBODY>
      </TABLE>

<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('scheduler_cancel')">Cancel</A></TD>
<%
if (DisplayBean.getNewRegistration().equalsIgnoreCase("Y"))
{
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Continue</A></TD>
<%
}
else
{
%>          
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Save Changes</A></TD>
<%
}
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" name="removeButton"></A></TD>
          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
          <TD class="pagelogo"><jsp:include page="pagebar_logo.jsp" flush="true" /></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR>
</TABLE>
</BODY>
</HTML>
