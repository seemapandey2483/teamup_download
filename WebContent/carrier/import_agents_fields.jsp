<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.ImportAgentFieldMappingDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	document.tudlform.fieldmap[0].focus();
	return true;
}

function allow_action()
{
	return true;
}

function do_cancel()
{
	do_page_action("import_agents_cancel");
}

function do_submit()
{
	var allMapped = true;
	var agentIdMapped = 0;
	var partCodeMapped = false;
	var tempFld = null;
	
	for (i = 0; i < <%= DisplayBean.getFieldCount() %>; i++)
	{
		if (!document.tudlform.fieldmap[i].value)
		{
			allMapped = false;
			if (tempFld == null)
				tempFld = document.tudlform.fieldmap[i];
		}
		else if (document.tudlform.fieldmap[i].value == "AGENTID")
		{
			agentIdMapped++;
		}
		else if (document.tudlform.fieldmap[i].value == "PARTCODE")
		{
			partCodeMapped = true;
		}
	}
	
	var isValid = true;
	var messages = "";
	if (agentIdMapped == 0)
	{
		isValid = false;
		messages += "You must map an import field to the Agent ID.\n";
	}
	else if (agentIdMapped > 1)
	{
		isValid = false;
		messages += "Agent ID can only be mapped to one import field.\n";
	}
	
	if (document.tudlform.participants.value == "Y" && partCodeMapped == false)
	{
		isValid = false;
		messages += "To import participants/agent sub-codes, you must map an import field to the Agent Sub-Code.\n";
	}
	
	if (allMapped == false)
	{
		isValid = false;
		messages += "At least one import field has not been mapped.  Each field must\n";
		messages += "either be mapped to a table field or designated as 'no mapping.'\n";
	}
	
	
	if (isValid == true)
	{
		do_page_action("import_agents_save_fields");
	}
	else
	{
		if (tempFld == null)
			tempFld = document.tudlform.fieldmap[0];
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
      <H2>Map Data to Agent Table Fields<BR>&nbsp;</H2>
      <TABLE class="qcell_table" align="center" frame="border" rules="rows">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" align="center"><B>Sample File Data</B></TD>
            <TD class="qcell_question" align="center">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Agent Table Field</B></TD>
          </TR>
<%
for (int i=0; i < DisplayBean.getFieldCount(); i++)
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question"><%= DisplayBean.getFieldData(i) %></TD>
            <TD class="qcell_question"></TD>
            <TD class="qcell_question">
              <SELECT name="fieldmap">
<%
  for (int j=0; j < DisplayBean.getFieldNameOptionsCount(); j++)
  {
%>              <OPTION value="<%= DisplayBean.getFieldNameOption(j) %>" <% if (DisplayBean.isFieldNameOptionSelected(i, j)) { %>selected<% } %>><%= DisplayBean.getFieldNameLabel(j) %></OPTION>
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
if (DisplayBean.getFieldCount() == 1)
{
%>
	<input type="hidden" name="fieldmap" value="">
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_cancel()">Cancel</A></TD>
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
