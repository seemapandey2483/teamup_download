<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.ImportAgentVendorMappingDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	document.tudlform.vendormap[0].focus();
	return true;
}

function allow_action()
{
	return true;
}

function do_submit()
{
	var isValid = true;
	var messages = "";
	var tempFld = null;
	
	for (i = 0; i < <%= DisplayBean.getMappingCount() %>; i++)
	{
		if (!document.tudlform.vendormap[i].value)
		{
			isValid = false;
			messages += "At least one vendor system ID has not been mapped.  Each imported ID\n";
			messages += "must be mapped to either a defined TEAM-UP Download system or 'Other.'\n";
			if (tempFld == null)
				tempFld = document.tudlform.vendormap[i];
			break;
		}
	}
	
	if (isValid == true)
	{
		do_page_action("import_agents_save_vendors");
	}
	else
	{
		if (tempFld == null)
			tempFld = document.tudlform.vendormap[0];
		tempFld.focus();
		alert(messages);
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
      <H2>Map Agency Vendor System IDs<BR>&nbsp;</H2>
      <TABLE class="qcell_table" align="center" frame="border" rules="rows">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" align="center"><B>Imported System ID</B></TD>
            <TD class="qcell_question" align="center">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Vendor System</B></TD>
          </TR>
<%
for (int i=0; i < DisplayBean.getMappingCount(); i++)
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question"><%= DisplayBean.getImportId(i) %></TD>
            <TD class="qcell_question"><input type="hidden" name="importedId" value="<%= DisplayBean.getImportId(i) %>"></TD>
            <TD class="qcell_question">
              <SELECT name="vendormap">
<%
  for (int j=0; j < DisplayBean.getVendorOptionsCount(); j++)
  {
%>              <OPTION value="<%= DisplayBean.getVendorOption(j) %>" <% if (DisplayBean.isVendorOptionSelected(i, j)) { %>selected<% } %>><%= DisplayBean.getVendorLabel(j) %></OPTION>
<%
  }
%>              </SELECT>
            </TD>
          </TR>
<%
}
%>
        </TBODY>
      </TABLE>

<%
if (DisplayBean.getMappingCount() == 1)
{
%>
	<input type="hidden" name="vendormap" value="">
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_submit()">Continue</A></TD>
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
