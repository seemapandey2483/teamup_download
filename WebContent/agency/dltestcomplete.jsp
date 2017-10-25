<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AgencyInfoDisplayBean" />
<TITLE><%= DisplayBean.getCarrierName() %> - TEAM-UP Download Agency Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	return true;
}

function allow_action()
{
	return true;
}

function check_file()
{
	window.open("", "_file_check", "resizable=yes,scrollbars=yes");
	return true;
}

function do_continue()
{ <%
if (DisplayBean.getInteractiveFlag().equals("N"))
{
%>
	if (document.tudlform.scheduleTask.checked == false)
	{
		if (confirm("Delete the scheduled download task?"))
			document.tudlform.removeSchedule.value = "Y";
		else
		{
			document.tudlform.scheduleTask.checked = true;
			return;
		}
	}
<%
}
%>
	do_page_action("dltest_complete");
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
<%
if (DisplayBean.isAgentRegistered())
{
%>          <TD class="actionbar" nowrap="nowrap"><jsp:include page="actionbar.jsp" flush="true" /></TD>
<%
}
else
{
%>          <TD class="actionbar" nowrap="nowrap"><jsp:include page="actionbar_nohome.jsp" flush="true" /></TD>
<%
}
%>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap"><%
if (DisplayBean.isAgentRegistered())
{
	// display nothing
}
else
{
%><a href="javascript:void(0);" onClick="javascript:open_prn_win('agency/print_dltestcomplete.jsp')" class="menu">Print Instructions</a><%
}
%></TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">
    <INPUT type="hidden" name="removeSchedule" value="">
	<INPUT type="hidden" name="newRegistration" value="<% if (!DisplayBean.isAgentRegistered()) { %>Y<% } %>">    

<!-- BEGIN BODY -->
<%
if (DisplayBean.isAgentRegistered())
{
%>
    <H1>Change Agency System Settings</H1>
<%
}
else
{
%>
    <H1>TEAM-UP Download Registration <IMG border="0" src="<c:url value="/images/qBubble.gif"/>" alt="Your machine must be on and logged in to perform scheduled downloads.  You can use both Scheduled downloads and create the desktop icons to make it convenient to access your archived files." width="20" height="22"></H1>
<%
}
%>
    <CENTER>&nbsp;<BR>&nbsp;</CENTER>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">
              <B>Congratulations!</B>&nbsp; TEAM-UP has successfully completed a test 
              download based on the configuration information that has been entered.<BR>&nbsp;
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">
              You are now ready to download files over the internet using TEAM-UP 
              Download.  TEAM-UP Download can run on a scheduled, unattended basis 
              each evening.  Setting up the scheduler requires you to enter your 
              scheduler preferences on the next page.  Whether you choose to use the 
              scheduler or not, the TEAM-UP application is also available at any 
              time of the day to get your download files or retrieve files from your 
              archive over the Internet.  You can add icons to your desktop that 
              will make using TEAM-UP Download even easier!
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;<BR>&nbsp;</TD>
          </TR>
<%
if (DisplayBean.isAgentRegistered())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center">To run your
					downloads automatically, select the "Schedule download" box below.</TD>
          </TR>
<%
}
else
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center"><FONT color="red"><B>To run your
					downloads automatically, select the "Schedule download" box below.</B></FONT></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center"><INPUT type="CHECKBOX" name="scheduleTask" value="Y" <% if (DisplayBean.getInteractiveFlag().equals("N")) { out.print("checked"); }%> >
              <B>Schedule download</B>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center">&nbsp;<BR>&nbsp;</TD>
          </TR>
<%
if (DisplayBean.isAgentRegistered())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center">Create desktop icons for:</TD>
          </TR>
<%
}
else
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center"><FONT color="red"><B>Create desktop icons for:</B></FONT></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center"><INPUT type="CHECKBOX" name="desktopDownload" value="Y">
              <B>Immediately getting your download</B>
            </TD>
          </TR>
<%
if (DisplayBean.getCarrierInfo().isDisplayLoginShortcutControl())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center"><INPUT type="CHECKBOX" name="desktopConfig" value="Y">
              <B>Accessing this application's login page</B>
            </TD>
          </TR>
<%
}
%>
        </TBODY>
      </TABLE>

<%
if (DisplayBean.getCarrierInfo().isDisplayLoginShortcutControl())
{
%>      <input type="hidden" name="desktopConfig" value="">
<%
}
%>
		
<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
<!--          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('dltest_back')">Back</A></TD> -->
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_continue()">Continue</A></TD>
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
