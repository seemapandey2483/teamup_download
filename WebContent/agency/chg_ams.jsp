<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AgencyRegisterDisplayBean" />
<TITLE><%= DisplayBean.getCarrierInfo().getName() %> - TEAM-UP Download Agency Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
function do_load()
{ <%
  if (DisplayBean.getAmsId().length() == 0)
  {
%>
	document.tudlform.system.value = "";
<%
  }
%>
	document.tudlform.system.focus();
	
	return true;
}

function allow_action()
{
	if (document.tudlform.data_changed.value == "Y")
	{
		msg = "You are about to lose any changes you have made on this page.  To save your changes, click 'Cancel' and then use the 'Continue' button at the bottom of the screen.";
		if (!confirm(msg))
			return false;
	}
	
	return true;
}

<jsp:include page="javascript.jsp" flush="true" />

function throw_change_flag()
{
	document.tudlform.data_changed.value = "Y";
}

function do_select_ams()
{
	throw_change_flag();
	
	if (document.tudlform.system.value == "<%= DisplayBean.getAmsId() %>")
		document.tudlform.vendor_changed.value = "N";
	else
		document.tudlform.vendor_changed.value = "Y";
	
	document.tudlform.dldir.value = "";
}

function do_save()
{
	if (!document.tudlform.system.value)
	{
		alert("Please select your agency management system.");
		document.tudlform.system.focus();
	}
	else
	{
		do_page_action("agt_chgams_save");
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
    <INPUT type="hidden" name="dldir" value="<%= DisplayBean.getAgentDirectory() %>">
    <INPUT type="hidden" name="vendor_changed" value="N">

<!-- BEGIN BODY -->

    <H1>Change Agency System Settings<BR>&nbsp;<BR>&nbsp;</H1>

    <TABLE class="qcell_table" width="100%">
      <TBODY>
        <TR class="qcell_row" valign="top">
          <TD class="qcell_question" colspan="2" align="center">
            <TABLE border="1" cellpadding="1" cellspacing="0"  bgcolor="white" rules="none">
              <TR class="qcell_row" valign="top">
                <TD class="qcell_question" colspan="3">&nbsp;<B>Current Settings:</B></TD>
              </TR>
              <TR class="qcell_row" valign="top">
                <TD class="qcell_number">&nbsp;&nbsp;&nbsp;</TD>
                <TD class="qcell_question" align="right">Agency Management System:&nbsp;</TD>
                <TD class="qcell_text"><%= DisplayBean.getAmsName() %> &nbsp;</TD>
              </TR>
              <TR class="qcell_row" valign="top">
                <TD class="qcell_number">&nbsp;</TD>
                <TD class="qcell_question" align="right">Version:&nbsp;</TD>
                <TD class="qcell_text"><%= DisplayBean.getAmsVersion() %></TD>
              </TR>
              <TR class="qcell_row" valign="top">
                <TD class="qcell_number">&nbsp;</TD>
                <TD class="qcell_question" align="right">Download Directory:&nbsp;</TD>
                <TD class="qcell_text"><%= DisplayBean.getAgentDirectory() %> &nbsp;</TD>
              </TR>
              <TR class="qcell_row" valign="top">
                <TD class="qcell_number">&nbsp;</TD>
                <TD class="qcell_question" align="right">Download Filename:&nbsp;</TD>
                <TD class="qcell_text"><%= DisplayBean.getAgentFilename() %> &nbsp;</TD>
              </TR>
            </TABLE>
          </TD>
        </TR>
        <TR class="qcell_row" valign="top">
          <TD class="qcell_question" align="right" width="40%">&nbsp;<BR>&nbsp;</TD>
          <TD class="qcell_text" width="60%">&nbsp;</TD>
        </TR>
        <TR class="qcell_row" valign="top">
          <TD class="qcell_question" align="right">Agency Management System:</TD>
          <TD class="qcell_text"><SELECT name="system" onchange="do_select_ams()">
<%
for (int i=0; i < DisplayBean.getAmsCount(); i++)
{
%>            <OPTION value="<%= DisplayBean.getAmsId(i) %>" <% if (DisplayBean.getAmsId().equals(DisplayBean.getAmsId(i))) { %>selected<% } %>><%= DisplayBean.getAmsName(i) %></OPTION>
<%
}
%>            </SELECT>
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('agt_chgams_cancel')">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Continue</A></TD>
          <TD class="pagebar" nowrap="nowrap" width="100%"></TD>
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
