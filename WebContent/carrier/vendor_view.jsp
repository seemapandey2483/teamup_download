<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AmsListDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
var name_anchor;
var id_anchor;
var sort_seq_anchor;
var default_dir_anchor;
var dir_label_anchor;
var dir_notes_anchor;
var default_filename_anchor;
var filename_label_anchor;
var filename_notes_anchor;
var append_flag_anchor;
var custom_flag_anchor;

function do_load()
{
	for (count = 0; count < document.anchors.length; count++)
	{
		if (document.anchors[count].name == "amsname")
			name_anchor = document.anchors[count];
		else if (document.anchors[count].name == "id")
			id_anchor = document.anchors[count];
		else if (document.anchors[count].name == "sort_seq")
			sort_seq_anchor = document.anchors[count];
		else if (document.anchors[count].name == "default_dir")
			default_dir_anchor = document.anchors[count];
		else if (document.anchors[count].name == "dir_label")
			dir_label_anchor = document.anchors[count];
		else if (document.anchors[count].name == "dir_notes")
			dir_notes_anchor = document.anchors[count];
		else if (document.anchors[count].name == "default_filename")
			default_filename_anchor = document.anchors[count];
		else if (document.anchors[count].name == "filename_label")
			filename_label_anchor = document.anchors[count];
		else if (document.anchors[count].name == "filename_notes")
			filename_notes_anchor = document.anchors[count];
		else if (document.anchors[count].name == "append_flag")
			append_flag_anchor = document.anchors[count];
		else if (document.anchors[count].name == "custom_flag")
			custom_flag_anchor = document.anchors[count];
	}
	
	document.tudlform.amsid.focus();

	return true;
}

function allow_action()
{
	return true;
}

function do_addnew()
{
	document.tudlform.amsid.value = "";
	do_page_action("ams_addnew");
}

function do_edit()
{
	if (!document.tudlform.amsid.value)
	{
		alert("Please select a vendor system to edit.");
		document.tudlform.amsid.focus();
	}
	else
	{
		do_page_action("ams_edit");
	}
}

function do_help()
{
	if (!document.tudlform.amsid.value)
	{
		alert("Please select a vendor system.");
		document.tudlform.amsid.focus();
	}
	else
	{
		do_page_action("ams_help");
	}
}

function do_sync()
{
	var msg = "Updating the vendor system list from Ebix,Inc may take up to\nseveral minutes to complete.\n\nDo you wish to perform this update at this time?";
	if (confirm(msg))
	{
		do_page_action("ams_sync");
	}
}

function do_select_ams()
{
	if (!document.tudlform.amsid.value)
	{
		name_anchor.setAttribute("innerText", "");
		id_anchor.setAttribute("innerText", "");
		sort_seq_anchor.setAttribute("innerText", "");
		default_dir_anchor.setAttribute("innerText", "");
		dir_label_anchor.setAttribute("innerText", "");
		dir_notes_anchor.setAttribute("innerText", "");
		default_filename_anchor.setAttribute("innerText", "");
		filename_label_anchor.setAttribute("innerText", "");
		filename_notes_anchor.setAttribute("innerText", "");
		append_flag_anchor.setAttribute("innerText", "");
		custom_flag_anchor.setAttribute("innerText", "");
	}
<%
for (int i=0; i < DisplayBean.getAmsCount(); i++)
{
%>	else if (document.tudlform.amsid.value == "<%= DisplayBean.getAms(i).getId() %>")
	{
		name_anchor.setAttribute("innerText", "<%= DisplayBean.escapeForHtml(DisplayBean.getAms(i).getDisplayName()) %>");
		id_anchor.setAttribute("innerText", "<%= DisplayBean.getAms(i).getId() %>");
		sort_seq_anchor.setAttribute("innerText", "<%= DisplayBean.getAms(i).getSortSequence() %>");
		default_dir_anchor.setAttribute("innerText", "<%= DisplayBean.escapeForHtml(DisplayBean.getAms(i).getDefaultDir()) %>");
		dir_label_anchor.setAttribute("innerText", "<% if (!DisplayBean.getAms(i).getDirectoryNotes().equals("")) { %>Directory Note:<% } %>");
		dir_notes_anchor.setAttribute("innerText", "<%= DisplayBean.escapeForHtml(DisplayBean.getAms(i).getDirectoryNotes()) %>");
		default_filename_anchor.setAttribute("innerText", "<%= DisplayBean.getAms(i).getDefaultFilename() %>");
		filename_label_anchor.setAttribute("innerText", "<% if (!DisplayBean.getAms(i).getFilenameNotes().equals("")) { %>File Note:<% } %>");
		filename_notes_anchor.setAttribute("innerText", "<%= DisplayBean.escapeForHtml(DisplayBean.getAms(i).getFilenameNotes()) %>");
		append_flag_anchor.setAttribute("innerText", "<% if (DisplayBean.getAms(i).isAppendFlag()) { %>Yes<% } else { %>No<% } %>");
		custom_flag_anchor.setAttribute("innerText", "<% if (DisplayBean.getAms(i).isCustomSystem()) { %>*<% } %>");
	}
<%
}
%>
do_edit();
}

<jsp:include page="javascript.jsp" flush="true" />
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

<!-- BEGIN BODY -->

    <H1>Agency Vendor System Maintenance<BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="30%"><B>Vendor System:</B></TD>
            <TD class="qcell_text" valign="top" width="70%">
              <SELECT name="amsid" onchange="do_select_ams()">
                <OPTION value=""> &lt;select a vendor system&gt; </OPTION>
<%
for (int i=0; i < DisplayBean.getAmsCount(); i++)
{
%>                <OPTION value="<%= DisplayBean.getAms(i).getId() %>"><%= DisplayBean.getAms(i).getDisplayName() %></OPTION>
<%
}
%>              </SELECT>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="30%">&nbsp;</TD>
            <TD class="qcell_text" valign="top" width="70%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="30%"><B>Name:</B></TD>
            <TD class="qcell_text" valign="top" width="70%"><B><A name="amsname"></A></B> &nbsp; <A name="custom_flag"></A></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="30%"><B>System ID:</B></TD>
            <TD class="qcell_text" valign="top" width="70%"><A name="id"></A></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="30%"><B>Sort Sequence:</B></TD>
            <TD class="qcell_text" valign="top" width="70%"><A name="sort_seq"></A></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="30%"><B>Default Download Directory:</B></TD>
            <TD class="qcell_text" valign="top" width="70%"><A name="default_dir"></A></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="30%"><B><A name="dir_label"></A></B></TD>
            <TD class="qcell_text" valign="top" width="70%"><A name="dir_notes"></A></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="30%"><B>Default Download Filename:</B></TD>
            <TD class="qcell_text" valign="top" width="70%"><A name="default_filename"></A></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="30%"><B><A name="filename_label"></A></B></TD>
            <TD class="qcell_text" valign="top" width="70%"><A name="filename_notes"></A></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" valign="top" width="30%"><B>Append to Existing File:</B></TD>
            <TD class="qcell_text" valign="top" width="70%"><A name="append_flag"></A></TD>
          </TR>
<%
if (DisplayBean.getAmsCount() <= 1)
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center" colspan="2">&nbsp;<BR>&nbsp;<BR>
              <font color="red"><B><i>Agency vendor system list has not yet been initialized.<BR>
              Please select 'Update System List' below to continue.</i></B></font>
            </TD>
          </TR>
<%
}
%>
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_addnew()">Add New</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_edit()">Edit</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_help()">View System Notes</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('ams_resort')">Sort Alphabetically</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_sync()">Update System List</A></TD>
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
