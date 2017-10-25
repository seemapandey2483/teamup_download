<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.DistFileEmailDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	document.tudlform.subject.focus();
	return true;
}

function allow_action()
{
	return true;
}

function do_submit()
{
	if (!document.tudlform.subject.value)
	{
		alert("Please enter the email subject line.");
		document.tudlform.subject.focus();
	}
	else if (!document.tudlform.body.value)
	{
		alert("Please enter the body of the email.");
		document.tudlform.body.focus();
	}
//	else if (!document.tudlform.sender.value)
//	{
//		alert("Please enter the sending email address.");
//		document.tudlform.sender.focus();
//	}
	else
	{
		do_page_action("save_email_params");
	}
}

function do_back()
{
	do_page_action("dist_email_select");
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
    <INPUT type="hidden" name="agyGroup" value="<%= DisplayBean.getAgyGroup() %>">
    <INPUT type="hidden" name="amsId" value="<%= DisplayBean.getAmsId() %>">
    <INPUT type="hidden" name="amsVersion" value="<%= DisplayBean.getAmsVersion() %>">
    <INPUT type="hidden" name="distBatchNumber" value="<%= DisplayBean.getDistBatchNumber() %>">

<!-- BEGIN BODY -->

      <H1>Enter E-Mail Parameters<BR>&nbsp;</H1>
      <CENTER>
		<table>
		<tr valign="top">
			<td align="right">Subject:&nbsp;<BR>&nbsp;</td>
			<td><input type="text" name="subject" size="50" value="<%= DisplayBean.getSubject() %>"></td>
		</tr>
		<tr valign="top">
			<td align="right">E-mail Text:&nbsp;</td>
			<td><TEXTAREA name="body" cols="50" rows="10"><%= DisplayBean.getBody() %></TEXTAREA></td>
		</tr>
		<tr valign="top">
			<td align="right">Add Attachments:&nbsp;</td>
			<td><input type="checkbox" name="addAttachments" value="Y"></td>
		</tr>
		<tr valign="top">
			<td align="right">&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
<!--
		<tr valign="top">
			<td align="right">Sender's e-mail address:&nbsp;</td>
			<td><input type="text" name="sender" size="30" value="<%= DisplayBean.getSender() %>"></td>
		</tr>
-->
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_back()">Back</A></TD>
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
