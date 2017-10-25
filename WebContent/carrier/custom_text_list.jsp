<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.CustomTextListDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	document.tudlform.file_select[0].focus();
	return true;
}

function allow_action()
{
	return true;
}

<jsp:include page="javascript.jsp" flush="true" />

function select_file(filename, custom)
{
	document.tudlform.text_file.value = filename;
	document.tudlform.custom_flag.value = custom;
}

function edit_file(filename)
{
	document.tudlform.text_file.value = filename;
	do_page_action("custom_edit");
}

function do_edit()
{
	if (document.tudlform.text_file.value == "")
	{
		alert("Please select an email to edit.");
		document.tudlform.file_select[0].focus();
	}
	else
	{
		do_page_action("custom_edit");
	}
}

function do_import()
{
	if (document.tudlform.text_file.value == "")
	{
		alert("Please select an email to import.");
		document.tudlform.file_select[0].focus();
	}
	else
	{
		do_page_action("custom_import");
	}
}

function view_custom()
{
	if (document.tudlform.text_file.value == "")
	{
		alert("Please select an email to view.");
		document.tudlform.file_select[0].focus();
	}
	else if (document.tudlform.custom_flag.value != "Y")
	{
		alert("This email does not currently have any custom text to view.");
	}
	else
	{
		do_page_action("custom_view");
	}
}

function view_default()
{
	if (document.tudlform.text_file.value == "")
	{
		alert("Please select an email to view.");
		document.tudlform.file_select[0].focus();
	}
	else
	{
		do_page_action("custom_view_default");
	}
}

function set_default()
{
	if (document.tudlform.text_file.value == "")
	{
		alert("Please select an email to set.");
		document.tudlform.file_select[0].focus();
	}
	else if (document.tudlform.custom_flag.value != "Y")
	{
		alert("This email is already using the default text.");
	}
	else
	{
		do_page_action("custom_set_default");
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
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
      <INPUT type="hidden" name="action" value="">
      <INPUT type="hidden" name="text_file" value="">
      <INPUT type="hidden" name="custom_flag" value="">

<!-- BEGIN BODY -->

      <H1>Customizable Emails<BR>&nbsp;</H1>
      
      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD nowrap="nowrap">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</TD>
            <TD class="qcell_question" align="center" colspan="2"><B>Email Description</B></TD>
            <TD nowrap="nowrap">&nbsp; &nbsp; &nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Status</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="5"><H2>&nbsp;<br>Emails to Agent:</H2></TD>
          </TR>
<%
for (int i=0; i < DisplayBean.getAgentFileCount(); i++)
{
	String custom = "N";
	if (DisplayBean.getAgentFileStatus(i).equalsIgnoreCase("custom"))
		custom = "Y";
%>
          <TR class="qcell_row" valign="top">
            <TD></TD>
            <TD class="qcell_question"><INPUT type="radio" name="file_select" onclick="select_file('<%= DisplayBean.getAgentFileName(i) %>', '<%= custom %>')"></TD>
            <TD class="qcell_question" nowrap="nowrap"><A href="javascript:edit_file('<%= DisplayBean.getAgentFileName(i) %>')" title="edit this email">
                <%= DisplayBean.getAgentFileDesc(i) %></A></TD>
            <TD></TD>
            <TD class="qcell_question" align="center"><%= DisplayBean.getAgentFileStatus(i) %></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="5">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="5"><H2>Emails to Carrier:</H2></TD>
          </TR>
<%
for (int i=0; i < DisplayBean.getCarrierFileCount(); i++)
{
	String custom = "N";
	if (DisplayBean.getCarrierFileStatus(i).equalsIgnoreCase("custom"))
		custom = "Y";
%>
          <TR class="qcell_row" valign="top">
            <TD></TD>
            <TD class="qcell_question"><INPUT type="radio" name="file_select" onclick="select_file('<%= DisplayBean.getCarrierFileName(i) %>', '<%= custom %>')"></TD>
            <TD class="qcell_question" nowrap="nowrap"><A href="javascript:edit_file('<%= DisplayBean.getCarrierFileName(i) %>')" title="edit this email">
                <%= DisplayBean.getCarrierFileDesc(i) %></A></TD>
            <TD></TD>
            <TD class="qcell_question" align="center"><%= DisplayBean.getCarrierFileStatus(i) %></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="5">&nbsp;<BR>&nbsp;</TD>
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_edit()">Edit</A></TD>
<!--      <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_import()">Import</A></TD>
-->
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:view_custom()">View Custom Text</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:view_default()">View Default Text</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:set_default()">Use Default Text</A></TD>
          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
 <%--  <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR> --%>
</TABLE>
<%@ include file="footer.jsp" %>
</BODY>
</HTML>
