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

// keep track of image to load (for animated wait bar)
var next_image = 2;
var Wait1 = new Image(82,10);
var Wait2 = new Image(82,10);
var Wait3 = new Image(82,10);
var Wait4 = new Image(82,10);
var Wait5 = new Image(82,10);
var Wait6 = new Image(82,10);
var increment = 1;

function do_load()
{
// load the image objects
	Wait1.src = "<c:url value="/images/wait1.gif"/>";
	Wait2.src = "<c:url value="/images/wait2.gif"/>";
	Wait3.src = "<c:url value="/images/wait3.gif"/>";
	Wait4.src = "<c:url value="/images/wait4.gif"/>";
	Wait5.src = "<c:url value="/images/wait5.gif"/>";
	Wait6.src = "<c:url value="/images/wait6.gif"/>";

// set the timer
	setTimeout("change_image('wait_image')", 750);
	setTimeout("do_remove()", 1400);
}

function allow_action()
{
	return true;
}

function do_remove()
{
	document.tudlform.schedulerConfig.TaskName = "<%= DisplayBean.getTaskName() %>";
	document.tudlform.schedulerConfig.retrieveScheduledTask();
	if (document.tudlform.schedulerConfig.Status == 0)
	{
		document.tudlform.schedulerConfig.deleteScheduledTask();
		if (document.tudlform.schedulerConfig.Status == 0)
		{
			do_page_action("scheduler_delete");
			return;
		}
		else
		{
			alert("An error occurred while trying to delete the scheduled task.");
		}
	}
	else
	{
		alert("The scheduled download task does not exist on this machine.  To remove the scheduled download, please perform this action on the PC on which this action was first installed.");
	}
	
	do_page_action("scheduler_cancel");
}

function change_image(pic)
{
	document.images[pic].src = eval("Wait" + next_image + ".src");
	next_image = next_image + increment;
	if (next_image > 6 || next_image < 2)
	{
		increment = 0 - increment;
		next_image = next_image + (increment * 2);
	}

	setTimeout("change_image('wait_image')", 750);

	return true;			
}

<jsp:include page="javascript.jsp" flush="true" />
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
    <TD class="menubar" nowrap="nowrap">&nbsp;</TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">
	  <OBJECT id="schedulerConfig" codeBase="controls/<%= DisplayBean.getCodebase() %>" classid="<%= DisplayBean.getClassid() %>">
	  </OBJECT>
	

<!-- BEGIN BODY -->

    <H1>Removing Automatic Download Task...<BR>&nbsp;<BR>&nbsp;<BR>&nbsp;<BR>&nbsp;<BR>&nbsp;</H1>
    <CENTER><IMG name="wait_image" src="<c:url value="/images/wait1.gif"/>"><BR>&nbsp;<BR>&nbsp;
      <FONT face="Arial" size="2"><B>Please wait while we update your download settings.</B></FONT>
    </CENTER>

<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
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
