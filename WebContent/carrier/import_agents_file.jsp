<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.ImportAgentInitDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
<%
if (DisplayBean.isCarProcess())
{
%>	document.fileform.datafile.focus();
<%
}
else
{
%>	document.fileform.carProcess[0].focus();
<%
}
%>
	return true;
}

function allow_action()
{
	return true;
}

function do_submit()
{
<%
if (!DisplayBean.isCarProcess())
{
%>
	if (document.fileform.carProcess[0].checked == false && document.fileform.carProcess[1].checked == false)
	{
		alert("Please answer the roll-out process question.");
		document.fileform.carProcess[0].focus();
		return;
	}
<%
}
%>
	if (!document.fileform.datafile.value)
	{
		alert("Please select a comma-delimited data file to import.");
		document.fileform.datafile.focus();
	}
	else if (document.fileform.agents.checked == false && document.fileform.participants.checked == false)
	{
		alert("Please select which type of data is included in the file: agent info, participants, or both.");
		document.fileform.agents.focus();
	}
	else
	{
		document.fileform.action.value = "import_agents_file";
		document.fileform.submit();
	}
}

function do_cancel()
{
	do_page_action("home");
}
// -->
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="return do_load();">
<TABLE width="100%" align="center" border="0" cellpadding="0" cellspacing="0" mm:layoutgroup="true" bgcolor="#FFFFFF">
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
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false" enctype="application/x-www-form-urlencoded">
      <INPUT type="hidden" name="action" value="">

<!-- BEGIN BODY -->

    </FORM>
    <FORM name="fileform" method="POST" action="<c:url value="/company"/>" onsubmit="return false" enctype="multipart/form-data">
      <INPUT type="hidden" name="action" value="">
      <INPUT type="hidden" name="batchnum">
<%
if (DisplayBean.isCarProcess())
{
%>      <INPUT type="hidden" name="carProcess" value="Y">
<%
}
%>

      <H1>Import Agent Info Wizard</H1>
      <H2>Import Data<BR>&nbsp;</H2>
      <CENTER>
      <table>
<%
if (!DisplayBean.isCarProcess())
{
%>
		<tr valign="middle">
			<td colspan="2">Would you like to assign <B>new</B> agents to a group for managing the roll-out process?</td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td>
				<INPUT type="radio" name="carProcess" value="Y">Yes &nbsp;
				<INPUT type="radio" name="carProcess" value="N">No
			</td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
<%
}
%>
		<tr valign="middle">
			<td colspan="2">Import file:&nbsp; <input type="file" name="datafile" size="50"></td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr valign="middle">
			<td colspan="2">File contains:</td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td><input type="checkbox" name="agents" value="Y"> Agents</td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td><input type="checkbox" name="participants" value="Y"> Participants / sub-agent codes</td>
		</tr>
		<tr valign="middle">
			<td colspan="2">&nbsp;<BR><input type="checkbox" name="overwrite" value="Y"> Overwrite existing agent data</td>
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
