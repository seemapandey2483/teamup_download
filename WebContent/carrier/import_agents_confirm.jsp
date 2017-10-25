<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.ImportAgentConfirmDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
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

function do_submit()
{
	do_page_action("import_agents_complete");
}

function viewAsXml()
{
	var xmlWindow = window.open('<%= DisplayBean.getSaveXmlUrl() %>','xml_window');
	xmlWindow.focus();
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
    <TD class="menubar" nowrap="nowrap">&nbsp;</TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
      <INPUT type="hidden" name="action" value="">
      <INPUT type="hidden" name="agents" value="<%= DisplayBean.getAgentsFlag() %>">
      <INPUT type="hidden" name="batchnum" value="<%= DisplayBean.getBatchNumber() %>">
      <INPUT type="hidden" name="overwrite" value="<%= DisplayBean.getOverwriteFlag() %>">
      <INPUT type="hidden" name="participants" value="<%= DisplayBean.getParticipantsFlag() %>">
      <INPUT type="hidden" name="carProcess" value="<%= DisplayBean.getCarFlag() %>">

<!-- BEGIN BODY -->

      <H1>Import Agent Info Wizard</H1>
      <H2>Summary of Mappings - Confirm Agent Import<BR>&nbsp;</H2>
      <A href="javascript:viewAsXml()">View import parameters as XML</A><BR>&nbsp;<BR>
      <FONT size="+1"><B>Field Mappings</B></FONT>
      <TABLE class="qcell_table" frame="border" rules="rows">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" align="center" nowrap="nowrap"><B>Sample File Data</B></TD>
            <TD class="qcell_question" align="center" nowrap="nowrap"><B>&nbsp;&nbsp;&raquo;&nbsp;&nbsp;</B></TD>
            <TD class="qcell_question" align="center" nowrap="nowrap"><B>Agent Table Field</B></TD>
          </TR>
<%
for (int i=0; i < DisplayBean.getFieldCount(); i++)
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question"><%= DisplayBean.getFieldData(i) %></TD>
            <TD class="qcell_question" align="center">&raquo;</TD>
            <TD class="qcell_question" nowrap="nowrap"><%= DisplayBean.getFieldName(i) %></TD>
          </TR>
<%
}
%>
        </TBODY>
      </TABLE>

<%
if (DisplayBean.isVendorMappingIncluded())
{
%>
      &nbsp;<BR>
      <FONT size="+1"><B>Agency Vendor System Mappings</B></FONT>
      <TABLE class="qcell_table" frame="border" rules="rows">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" align="center" nowrap="nowrap"><B>Imported System ID</B></TD>
            <TD class="qcell_question" align="center" nowrap="nowrap"><B>&nbsp;&raquo;&nbsp;</B></TD>
            <TD class="qcell_question" align="center" nowrap="nowrap"><B>Agency Vendor System</B></TD>
          </TR>
<%
  for (int i=0; i < DisplayBean.getVendorCount(); i++)
  {
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question"><%= DisplayBean.getImportedId(i) %></TD>
            <TD class="qcell_question" align="center">&raquo;</TD>
            <TD class="qcell_question"><%= DisplayBean.getVendorName(i) %></TD>
          </TR>
<%
  }
%>
        </TBODY>
      </TABLE>
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_page_action('import_agents_cancel')">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_page_action('import_agents_map_fields')">Back</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_submit()">Import</A></TD>
          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <%-- <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR> --%>
</TABLE>
<%@ include file="footer.jsp" %>
</BODY>
</HTML>
