<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.VendorSetupDisplayBean" />
<TITLE><%= DisplayBean.getCarrierName() %> - TEAM-UP Download Agency Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
function do_load()
{
	return true;
}

function allow_action()
{
	return true;
}

<jsp:include page="javascript.jsp" flush="true" />

function show_batchfile_msg()
{
	alert("Please contact your company representative or help desk for help with this feature.");
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

      <H1>Agency System Notes: &nbsp;<%= DisplayBean.getAmsName() %></H1>
      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" width="5%">Download&nbsp;Directory:&nbsp;</TD>
            <TD class="qcell_text" width="95%"><B><%= DisplayBean.getRemoteDirectory() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" width="5%">Download&nbsp;Filename:&nbsp;</TD>
            <TD class="qcell_text" width="95%"><B><%= DisplayBean.getImportFile() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2"><B>Setup Notes:</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2"><%= DisplayBean.getSetupNotes() %></TD>
          </TR>
<%
if (DisplayBean.getBatchFileNotes() != null && !DisplayBean.getBatchFileNotes().equals(""))
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2"><%= DisplayBean.getBatchFileNotes() %></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2"><B>Runtime Notes:</B></TD>
          </TR>
<%
if (DisplayBean.getInteractiveNotes() != null && !DisplayBean.getInteractiveNotes().equals(""))
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2"><%= DisplayBean.getInteractiveNotes() %></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2"><%= DisplayBean.getRuntimeNotes() %></TD>
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
