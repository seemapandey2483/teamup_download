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
{ <%
if (DisplayBean.isKeyUpdated())
{
  String msg = "Data changed on the previous screen has resulted in a change to the link used for downloads to your agency.  Please copy the new link below and update ";
  if (DisplayBean.getInteractiveFlag().equals("N"))
    msg += "the link used by your system's scheduled download.";
  else
    msg += "any stored links that you use to access the download.";
%>
	alert("<%= msg %>");
<%
}
%>
	return true;
}

function allow_action()
{
	return true;
}

function get_help()
{
	window.open("", "_help_blank", "resizable=yes,scrollbars=yes");
	return true;
}

function go_live()
{ <%
if (DisplayBean.getInteractiveFlag().equals("Y"))
{
%>
//	alert("To use the scheduled (non-interactive) mode, click the 'Change Settings' button.");
<%
}
%>
	do_page_action("agtinfo_golive");
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

<!-- BEGIN BODY -->

      <H1>TEAM-UP Download Agency Administration<% if (!DisplayBean.isAgentLive()) { %> <IMG border="0" src="<c:url value="/images/qBubble.gif"/>" alt="The Registration is complete.  By clicking 'Go Live' your next download file will be delivered via TEAM-UP Download." width="20" height="22"><% } %><BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="40%">Agency:</TD>
            <TD class="qcell_text" valign="top" width="60%"><B><%= DisplayBean.getAgentName() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="40%">Agency ID:</TD>
            <TD class="qcell_text" valign="top" width="60%"><B><%= DisplayBean.getAgentId() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top">Agency Management System:</TD>
            <TD class="qcell_text" valign="top"><B><%= DisplayBean.getAmsName() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top">Software Version:</TD>
            <TD class="qcell_text" valign="top"><B><%= DisplayBean.getAmsVersion() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top">Download to Directory:</TD>
            <TD class="qcell_text" valign="top"><B><%= DisplayBean.getRemoteDirectory() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Download Session Mode:</TD>
            <TD class="qcell_text"><B><%= DisplayBean.getInteractiveDesc() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Go Live:</TD>
            <TD class="qcell_text"><B><% if (DisplayBean.isAgentLive()) { %>Yes<% } else { %>No<% } %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Contact Name:</TD>
            <TD class="qcell_text"><B><%= DisplayBean.getContactName() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Email:</TD>
            <TD class="qcell_text"><B><%= DisplayBean.getContactEmail() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Phone:</TD>
            <TD class="qcell_text"><B><%= DisplayBean.getContactPhone() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Agency Location:</TD>
            <TD class="qcell_text"><B><%= DisplayBean.getLocationStateName() %></B></TD>
          </TR>
<%
if (DisplayBean.getParticipantCount() > 0)
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right"><span style="cursor: help"><IMG border="0" src="<c:url value="/images/qCircle.gif"/>" alt="Participant codes, sometimes referred to as agency sub-codes, allow you to associate multiple agency codes with one agency master code." width="14" height="14"></SPAN>&nbsp; Participant Codes:</TD>
            <TD class="qcell_text"><B><%= DisplayBean.getParticipantCodeList() %></B></TD>
          </TR>
<%
}
%>
<!--          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center" colspan="2">&nbsp;<BR><A href="<c:url value="/help/Help_Scheduler.jsp"/>" target="_help_blank" onclick="get_help()">View instructions for setting up scheduled downloads.</A></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2">&nbsp; &nbsp;<B>Copy this link for use in
						initiating a download from your system:</B></TD>
          </TR>
          <TR>
            <TD align="center" colspan="2"><FONT size="-2"><%= DisplayBean.getDownloadLink() %></FONT></TD>
          </TR>
-->          
          <TR class="qcell_row" valign="top">
            <TD colspan="2" class="qcell_question" align="left">
<%
if (DisplayBean.getNewRegistration().equalsIgnoreCase("Y"))
{
%>            
            	<p>&nbsp;</p><p><FONT color="red"><B>Your TEAM-UP Download registration is complete. Please click the "<b>Go Live</b>" button below to begin receiving download files through TEAM-UP.</B></FONT><br>&nbsp;</p>
<%
}
else if (DisplayBean.isAgentRegistered() && !DisplayBean.isAgentLive())
{
%>            
            	<p>&nbsp;</p><p>Your TEAM-UP Download registration is complete. Please click the "<b>Go Live</b>" button below to begin receiving download files through TEAM-UP.<br>&nbsp;</p>
<%
}
else
{
%>
				<p>&nbsp;</p><p>&nbsp;</p>
<%
}
%>
            </TD>
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
<%
if (DisplayBean.isAgentRegistered() && !DisplayBean.isAgentLive())
{
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:go_live()">Go Live</A></TD>
<%
}
%>
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
