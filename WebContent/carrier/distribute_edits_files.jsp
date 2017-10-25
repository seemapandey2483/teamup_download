<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.DistFileListDisplayBean" />
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
	if (document.fileform.file0.value)
	{
		do_upload();
	}
	else
	{
		do_page_action("dist_edits_complete");
	}
}

function do_upload()
{
	if (document.fileform.file0.value)
	{
		document.fileform.action.value = "upload_edit_file";
		document.fileform.submit();
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
    <FORM name="fileform" method="POST" action="<c:url value="/company"/>" onsubmit="return false" enctype="multipart/form-data">
      <INPUT type="hidden" name="action" value="">
      <INPUT type="hidden" name="uploadType" value="applied.edits">
      <INPUT type="hidden" name="fileCount" value="1">
      <INPUT type="hidden" name="distBatchNumber" value="<%= DisplayBean.getDistBatchNumber() %>">
      <INPUT type="hidden" name="notify" value="<%= DisplayBean.getNotify() %>">

<!-- BEGIN BODY -->

      <H1>Select Applied Edit Files<BR>&nbsp;</H1>
      <CENTER>
        <table>
          <tr valign="top">
            <td colspan="2" align="left"><input type="file" name="file0" size="50"></td>
          </tr>
<!--
          <tr valign="top">
            <td colspan="2" align="left"><input type="file" name="file1" size="50"></td>
          </tr>
          <tr valign="top">
            <td colspan="2" align="left"><input type="file" name="file2" size="50"></td>
          </tr>
-->
          <tr valign="top">
            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td><input type="button" value="Add Attachment" onclick="javascript:do_upload();"></td>
          </tr>
<%
if (DisplayBean.getAttachmentCount() > 0)
{
%>
          <tr valign="top">
            <td colspan="2" align="left">&nbsp;</td>
          </tr>
          <tr valign="top">
            <td colspan="2" align="left"><B>Current Attachments:</B></td>
          </tr>
<%
  for (int i=0; i < DisplayBean.getAttachmentCount(); i++)
  {
%>
          <tr valign="top">
            <td></td>
            <td><%= DisplayBean.getAttachment(i) %></td>
          </tr>
<%
  }
}
%>
        </table>
      </CENTER>
    </FORM>

    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false" enctype="application/x-www-form-urlencoded">
      <INPUT type="hidden" name="action" value="">
      <INPUT type="hidden" name="distBatchNumber" value="<%= DisplayBean.getDistBatchNumber() %>">
      <INPUT type="hidden" name="notify" value="<%= DisplayBean.getNotify() %>">

<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_cancel()">Cancel</A></TD>
<%
if (DisplayBean.getAttachmentCount() > 0)
{
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_submit()">Send</A></TD>
<%
}
%>          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
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
