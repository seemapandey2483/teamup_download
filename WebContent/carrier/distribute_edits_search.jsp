<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.DistFileParamsDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	document.tudlform.batchDesc.focus();
	return true;
}

function allow_action()
{
	return true;
}

function do_submit()
{
	if (!document.tudlform.batchDesc.value)
	{
		alert("Please enter a brief description of the files to be distributed.");
		document.tudlform.batchDesc.focus();
	}
	else if (!document.tudlform.agyGroup.value)
	{
		alert("Please select the group of agents to include in the search.");
		document.tudlform.agyGroup.focus();
	}
//	else if (document.tudlform.preview[0].checked == false && document.tudlform.preview[1].checked == false)
//	{
//		alert("Please select whether or not to review the list of agent recipients.");
//		document.tudlform.preview[0].focus();
//	}
	else if (document.tudlform.notify[0].checked == false && document.tudlform.notify[1].checked == false)
	{
		alert("Please select whether or not to send a notification email to all agent recipients.");
		document.tudlform.notify[0].focus();
	}
	else
	{
		//if (document.tudlform.preview[0].checked == true)
			do_page_action("dist_edits_select");
		//else
		//	do_page_action("dist_edits_files");
	}
}

function do_cancel()
{
	do_page_action("dist_cancel");
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
    <INPUT type="hidden" name="amsId" value="APPLIED">
    <INPUT type="hidden" name="distBatchNumber" value="<%= DisplayBean.getDistBatchNumber() %>">
    <INPUT type="hidden" name="preview" value="Y">

<!-- BEGIN BODY -->

      <H1>Select Distribution Parameters<BR>&nbsp;<BR>&nbsp;</H1>
      <CENTER>
      <table>
		<tr valign="top">
			<td align="right">Description of files:&nbsp;</td>
			<td><input type="text" name="batchDesc" size="20" maxlength="20" value="<%= DisplayBean.getBatchDesc("Applied edits") %>"></td>
		</tr>
		<tr valign="top">
			<td align="right">&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr valign="top">
			<td align="right">Agency Vendor System:&nbsp;</td>
			<td><b>Applied - TAM</b></td>
		</tr>
		<tr valign="top">
			<td align="right">Version:&nbsp;<BR>&nbsp;</td>
			<td><input type="text" name="amsVersion" size="6" maxlength="10" value="<%= DisplayBean.getAmsVersion() %>"></td>
		</tr>
		<tr valign="top">
			<td align="right">Select a previously saved group:&nbsp;<BR>&nbsp;</td>
			<td>
				<select name="agyGroup">
<%
for (int i=0; i < DisplayBean.getGroupCount(); i++)
{
%>					<option value="<%= DisplayBean.getGroupValue(i) %>"<% if (DisplayBean.isGroupSelected(i)) { %> selected<% } %>><%= DisplayBean.getGroupLabel(i) %></option>
<%
}
%>
				</select>
			</td>
		</tr>
		<tr valign="top">
			<td align="right">&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
<!--
		<tr valign="top">
			<td align="right">Preview/edit list before sending:&nbsp;</td>
			<td nowrap="nowrap">
			  <input type="radio" name="preview" <% if (DisplayBean.getPreview().equals("Y")) { %>checked<% } %> value="Y">Yes &nbsp;
			  <input type="radio" name="preview" <% if (DisplayBean.getPreview().equals("N")) { %>checked<% } %> value="N">No
			</td>
		</tr>
-->
		<tr valign="top">
			<td align="right">Send notification email to all recipients:&nbsp;</td>
			<td nowrap="nowrap">
			  <input type="radio" name="notify" <% if (DisplayBean.getNotify().equals("Y")) { %>checked<% } %> value="Y">Yes &nbsp;
			  <input type="radio" name="notify" <% if (DisplayBean.getNotify().equals("N")) { %>checked<% } %> value="N">No
			</td>
		</tr>
      </table>
      </CENTER>

<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_cancel()">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_submit()">Submit</A></TD>
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
