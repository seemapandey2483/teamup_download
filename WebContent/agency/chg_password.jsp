<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.SimpleAgencyDisplayBean" />
<TITLE><%= DisplayBean.getCarrierInfo().getName() %> - TEAM-UP Download Agency Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
function do_load()
{
<%
  if (DisplayBean.isPasswordChangeAllowed())
  {
%>	document.tudlform.pword.focus();
<%
  }
%>	
	return true;
}

function allow_action()
{
	if (document.tudlform.data_changed.value == "Y" || document.tudlform.pword.value)
	{
		msg = "You are about to lose any changes you have made on this page.  To save your new password, click 'Cancel' and then use the 'Save' button at the bottom of the screen.";
		if (!confirm(msg))
		{
			document.tudlform.pword.focus();
			return false;
		}
	}
	
	return true;
}

<jsp:include page="javascript.jsp" flush="true" />

function throw_change_flag()
{
	document.tudlform.data_changed.value = "Y";
}

function do_save()
{
	if (!document.tudlform.pword.value)
	{
		alert("Please enter a valid password.");
		document.tudlform.pword.focus();
	}
	else if (!document.tudlform.confirm.value)
	{
		alert("Please confirm your new password.");
		document.tudlform.confirm.focus();
	}
	else if (document.tudlform.pword.value != document.tudlform.confirm.value)
	{
		alert("The passwords you entered do not match.  Type the new password for this account in both text boxes.");
		document.tudlform.pword.focus();
	}
	else
	{
<%
if (DisplayBean.isAgentScheduled())
{
%>		alert("This action will change your automated download link.  If you have set up the scheduled download OR desktop icons on a PC other than this one, you will need to log into TEAM-UP Download on that PC and select 'Create Desktop Icons' and 'Add/Modify Scheduled Download' from the menu.");
<%
}
else
{
%>		alert("If you have set up desktop icons on a PC other than this one, you will need to log into TEAM-UP Download on that PC and select 'Create Desktop Icons' from the menu.");
<%
}
%>
		do_page_action("password_save");
	}
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
          <TD class="actionbar" nowrap="nowrap"><jsp:include page="actionbar.jsp" flush="true" /></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap"><jsp:include page="menu.jsp" flush="true" /></TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">
    <INPUT type="hidden" name="data_changed" value="N">
    <INPUT type="hidden" name="scheduleTask" value="<% if (DisplayBean.isAgentScheduled()) { %>Y<% } %>">
<%
if (!DisplayBean.isPasswordChangeAllowed())
{
%>    <INPUT type="hidden" name="pword" value="">
    <INPUT type="hidden" name="confirm" value="">
<%
}
%>

<!-- BEGIN BODY -->

      <H1>Change User Password<BR>&nbsp;<BR>&nbsp;<BR>&nbsp;</H1>
      
      <TABLE class="qcell_table" width="100%">
        <TBODY>
<%
if (DisplayBean.isPasswordChangeAllowed())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="35%">New Password:</TD>
            <TD class="qcell_text" width="65%"><INPUT type="password" name="pword" maxlength="10" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="35%">Confirm Password:</TD>
            <TD class="qcell_text" width="65%"><INPUT type="password" name="confirm" maxlength="10" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="35%">&nbsp;<BR>&nbsp;</TD>
            <TD class="qcell_text" width="65%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="35%">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Create/update desktop icons for:</b></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="35%">&nbsp;</TD>
            <TD class="qcell_question"><INPUT type="CHECKBOX" name="desktopDownload" value="Y">
              Immediately getting your download
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="35%">&nbsp;</TD>
            <TD class="qcell_question"><INPUT type="CHECKBOX" name="desktopConfig" value="Y">
              Accessing this application's login page
            </TD>
          </TR>
<%
}
else
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2" align="center"><I>
              You do not have authority to perform this action.  Please <BR>
              contact your carrier for help with changing your password.</I>
            </TD>
          </TR>
<%
}
%>
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
<%
if (DisplayBean.isPasswordChangeAllowed())
{
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('password_cancel')">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Save Changes</A></TD>
<%
}
%>          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
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
