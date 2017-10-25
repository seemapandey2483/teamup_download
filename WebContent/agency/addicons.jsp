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
    <TD class="menubar" nowrap="nowrap">&nbsp;</TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">
    <INPUT type="hidden" name="scheduleTask" value="N">

<!-- BEGIN BODY -->
    <H1>Create Desktop Icons</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center">&nbsp;<BR>&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center"><font color="red"><b>Create desktop icons for:</b></FONT></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center"><INPUT type="CHECKBOX" name="desktopDownload" value="Y">
              Immediately getting your download
            </TD>
          </TR>
<%
if (DisplayBean.getCarrierInfo().isDisplayLoginShortcutControl())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center"><INPUT type="CHECKBOX" name="desktopConfig" value="Y">
              Accessing this application's login page
            </TD>
          </TR>
<%
}
%>
        </TBODY>
      </TABLE>

<%
if (!DisplayBean.getCarrierInfo().isDisplayLoginShortcutControl())
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('dltest_complete')">Continue</A></TD>
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
