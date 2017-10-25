<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.ArchiveCompanyDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<%
int totalCount = 0;
%>
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	return true;
}

function allow_action()
{
	if (document.tudlform.data_changed.value == "Y")
	{
		msg = "You are about to lose any changes you have made on this page.  To save your changes, click 'Cancel' and then use the 'Save Changes' button at the bottom of the screen.";
		if (!confirm(msg))
			return false;
	}
	
	return true;
}

function do_page_action_verified(page_action)
{
<%
  for (int i=0; i < DisplayBean.getArchiveFilesCount(); i++)
  {
%>	<% if (i>0) { %>else <% } %>if (document.tudlform.file<%= i %>flag.checked == true && (!document.tudlform.file<%= i %>type.value || document.tudlform.file<%= i %>type.value == "<%= DisplayBean.getStatusArchive() %>" || document.tudlform.file<%= i %>type.value == "<%= DisplayBean.getStatusDBArchive() %>"))
	{
		alert("You must select a download type for all archived files flagged for download.");
		document.tudlform.file<%= i %>type.focus();
		return;
	}
<%
  }
%>
	do_page_action(page_action);
}

function change_settings(file_index)
{
	document.tudlform.data_changed.value = "Y";
<%
  for (int i=0; i < DisplayBean.getArchiveFilesCount(); i++)
  {
%>
	<% if (i > 0) { %>else <% } %>if (file_index == "<%= i %>")
	{
		document.tudlform.file<%= i %>changed.value = "Y";
		if (document.tudlform.file<%= i %>flag.checked == true)
		{
			document.tudlform.file<%= i %>type.disabled = false;
			if (document.tudlform.file<%= i %>dirbill.value == "Y")
				document.tudlform.file<%= i %>type.value = "<%= DisplayBean.getStatusDBCurrent() %>";
			else if (!document.tudlform.file<%= i %>type.value || document.tudlform.file<%= i %>type.value == "<%= DisplayBean.getStatusArchive() %>")
				document.tudlform.file<%= i %>type.value = "<%= DisplayBean.getStatusDownload() %>";
		}
		else
			document.tudlform.file<%= i %>type.disabled = true;
<%
    if (DisplayBean.isTransShown())
    {
      connective.teamup.download.db.FileInfo file = DisplayBean.getArchiveFile(i);
      totalCount = file.getTransactionCount();	
		if (file.isDirectBill()) {
			totalCount = 1;
		} 
      for (int t=0; t < totalCount; t++)
      {
        connective.teamup.download.db.TransactionInfo trans = file.getTransaction(t);
%>		document.tudlform.file<%= i %>trans<%= t %>.checked = document.tudlform.file<%= i %>flag.checked;
<%
      }
    }
%>	}
<%
  }
%>
}

function change_file(file_index)
{
	document.tudlform.data_changed.value = "Y";
<%
  for (int i=0; i < DisplayBean.getArchiveFilesCount(); i++)
  {
%>
	<% if (i > 0) { %>else <% } %>if (file_index == "<%= i %>")
	{
		document.tudlform.file<%= i %>changed.value = "Y";
<%
    if (DisplayBean.isTransShown())
    {
%>
		if (<%
    connective.teamup.download.db.FileInfo file = DisplayBean.getArchiveFile(i);
		 totalCount = file.getTransactionCount();	
			if (file.isDirectBill()) {
				totalCount = 1;
			} 
	      for (int t=0; t < totalCount; t++)    {
      if (t > 0) { %> || <% } %>document.tudlform.file<%= i %>trans<%= t %>.checked<%
	} %>)
		{
			if (document.tudlform.file<%= i %>flag.checked == false)
			{
				document.tudlform.file<%= i %>flag.checked = true;
				document.tudlform.file<%= i %>type.disabled = false;
				if (document.tudlform.file<%= i %>dirbill.value == "Y")
				{
					document.tudlform.file<%= i %>type.focus();
					document.tudlform.file<%= i %>type.value = "<%= DisplayBean.getStatusDBCurrent() %>";
				}
				else if (!document.tudlform.file<%= i %>type.value || document.tudlform.file<%= i %>type.value == "<%= DisplayBean.getStatusArchive() %>")
				{
					document.tudlform.file<%= i %>type.focus();
					document.tudlform.file<%= i %>type.value = "<%= DisplayBean.getStatusDownload() %>";
				}
			}
		}
		else
		{
			if (document.tudlform.file<%= i %>flag.checked == true)
			{
				document.tudlform.file<%= i %>flag.checked = false;
				change_settings(file_index);
			}
		}
<%
    }
%>
	}
<%
  }
%>
}

function delete_file(filetype, filename, filedate)
{
	var msg = "Deleting this file will permanently remove it from the download database.\n\nDo you wish to delete the specified file?";
	if (confirm(msg))
	{
		document.tudlform.action_filetype.value = filetype;
		document.tudlform.action_filename.value = filename;
		document.tudlform.action_filedate.value = filedate;
		do_page_action_verified("archive_delete");
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
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">
    <INPUT type="hidden" name="filecount" value="<%= DisplayBean.getArchiveFilesCount() %>">
    <INPUT type="hidden" name="data_changed" value="N">
    <INPUT type="hidden" name="origPage" value="<%= DisplayBean.getOriginatingPage() %>">
    <INPUT type="hidden" name="trans_shown" value="<% if (DisplayBean.isTransShown()) { %>Y<% } else { %>N<% } %>">
    <INPUT type="hidden" name="current_sort" value="<%= DisplayBean.getTpSortOrder() %>">
    <INPUT type="hidden" name="action_filetype" value="">
    <INPUT type="hidden" name="action_filename" value="">
    <INPUT type="hidden" name="action_filedate" value="">
    <INPUT type="hidden" name="pagever" value="C">

<!-- BEGIN BODY -->

<%
int NUM_COLUMNS = 11;
if (DisplayBean.isParticipantUsed())
	NUM_COLUMNS += 2;
%>
      <H1>TEAM-UP Download Agency Archive</H1>
      <H2><%= DisplayBean.getAgentId() %> - <%= DisplayBean.getAgentName() %> <% if (DisplayBean.getAgentName().equals("")) { %><EM>(Not a registered agent ID)</EM><% } %><BR>&nbsp;</H2>
      <TABLE class="qcell_table" cellpadding="0" cellspacing="0" border="0" width="100%">
        <TBODY>
          <TR>
            <TD colspan="<%= NUM_COLUMNS %>"><FONT size="+1"><B>Current &amp; Archived Download Files:</B></FONT></TD>
          </TR>
<%
if (DisplayBean.getArchiveFilesCount() == 0)
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="<%= NUM_COLUMNS %>">&nbsp;<BR><EM>There are no files currently in the archive for this agent.</EM><BR>&nbsp;<BR>&nbsp;</TD>
          </TR>
<%
}
else
{
%>          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" colspan="2" align="center"><B>Download</B></TD>
            <TD class="qcell_question" align="center"><B>Download<BR>Type</B></TD>
            <TD class="qcell_question" align="center">&nbsp;&nbsp;&nbsp;<B>Created</B>&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>File</B></TD>
            <TD class="qcell_question">&nbsp;&nbsp;</TD>
<%
  if (DisplayBean.isParticipantUsed())
  {
%>            <TD class="qcell_question" align="center"><B>Participant<BR>Code</B></TD>
            <TD class="qcell_question">&nbsp;&nbsp;</TD>
<%
  }
%>
            <TD class="qcell_question" align="center"><B>Download<BR>&nbsp;&nbsp;Filename&nbsp;&nbsp;</B></TD>
            <TD class="qcell_question" align="center"><B>Message<BR>&nbsp;&nbsp;&nbsp;Sequence&nbsp;&nbsp;&nbsp;</B></TD>
            <TD class="qcell_question" align="center"><B>No.&nbsp;of<BR>Trans</B></TD>
            <TD class="qcell_question" align="center"><B>Last<BR>&nbsp;&nbsp;&nbsp;Downloaded&nbsp;&nbsp;&nbsp;</B></TD>
            <TD class="qcell_question" width="40%">&nbsp;</TD>
          </TR>
          <TR>
            <TD colspan="<%= NUM_COLUMNS %>">
              <TABLE cellpadding="0" cellspacing="0" border="1" frame="above" width="100%"><TBODY><TR><TD></TD></TR></TBODY></TABLE>
            </TD>
          </TR>
<%
  for (int i=0; i < DisplayBean.getArchiveFilesCount(); i++)
  {
    connective.teamup.download.db.FileInfo file = DisplayBean.getArchiveFile(i);
%>          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2" align="center"><INPUT type="checkbox" name="file<%= i %>flag" value="Y" onclick="change_settings('<%= i %>')" <% if (DisplayBean.isDownloadable(file.getDownloadStatus())) { %>checked<% } %>><INPUT type="hidden" name="file<%= i %>" value="<%= file.getOriginalFilename() %>_<%= String.valueOf(file.getCreatedDate()) %>"><INPUT type="hidden" name="file<%= i %>changed" value="N"><INPUT type="hidden" name="file<%= i %>transcount" value="<%= file.getTransactionCount() %>"></TD>
            <TD class="qcell_text" align="center">
              <SELECT name="file<%= i %>type"<% if (!DisplayBean.isDownloadable(file.getDownloadStatus())) { %> disabled<% } %> onchange="change_file('<%= i %>')">
<%
    if (file.isDirectBill())
    {
%>
                <OPTION value="<%= DisplayBean.getStatusDBArchive() %>"<% if (file.getDownloadStatus().getCode().equals(DisplayBean.getStatusDBArchive())) { %> selected<% } %>>Archived (DirBill)</OPTION>
                <OPTION value="<%= DisplayBean.getStatusDBCurrent() %>"<% if (file.getDownloadStatus().getCode().equals(DisplayBean.getStatusDBCurrent())) { %> selected<% } %>>Current (DirBill)</OPTION>
<%
    }
    else
    {
%>
                <OPTION value="<%= DisplayBean.getStatusArchive() %>"<% if (file.getDownloadStatus().getCode().equals(DisplayBean.getStatusArchive())) { %> selected<% } %>>Archived</OPTION>
                <OPTION value="<%= DisplayBean.getStatusCurrent() %>"<% if (file.getDownloadStatus().getCode().equals(DisplayBean.getStatusCurrent())) { %> selected<% } %>>Current</OPTION>
                <OPTION value="<%= DisplayBean.getStatusDownload() %>"<% if (file.getDownloadStatus().getCode().equals(DisplayBean.getStatusDownload())) { %> selected<% } %>>Download</OPTION>
                <OPTION value="<%= DisplayBean.getStatusResend() %>"<% if (file.getDownloadStatus().getCode().equals(DisplayBean.getStatusResend())) { %> selected<% } %>>Resend/Synch</OPTION>
<%
    }
%>
              </SELECT>&nbsp;
              <INPUT type="hidden" name="file<%= i %>dirbill" value="<% if (file.isDirectBill()) { %>Y<% } else { %>N<% } %>">
            </TD>
            <TD class="qcell_text" align="center"><a title="<%= file.getCreatedDateStrLong() %>"><%= file.getCreatedDateStrShort() %></a></TD>
            <TD class="qcell_text" align="center" nowrap="nowrap"><%= file.getOriginalFilename() %></TD>
            <TD class="qcell_question">&nbsp;&nbsp;<a href="#" class="ButtonImgLink" onclick="javascript:delete_file('D', '<%= file.getOriginalFilename() %>', '<%= String.valueOf(file.getCreatedDate()) %>')"><IMG SRC="<c:url value="/images/delBtn.gif"/>" ALT="Delete this file" border="0"></a>&nbsp;&nbsp;</TD>
<%
    if (DisplayBean.isParticipantUsed())
    {
%>            <TD class="qcell_question" align="center"><%= file.getParticipantCode() %> <% if (file.getParticipantCode().equals("")) { %> <%= DisplayBean.getAgentId() %> <% } %></TD>
            <TD class="qcell_question"></TD>
<%
    }
%>
            <TD class="qcell_text" align="center" nowrap="nowrap"><%= file.getFilenameForDisplay() %></TD>
            <TD class="qcell_text" align="center"><%= file.getMessageSequence() %></TD>
            <TD class="qcell_text" align="center"><%= file.getTransactionCount() %></TD>
            <TD class="qcell_text" align="center"><a title="<%= file.getLastDownloadDateStrLong() %>"><%= file.getLastDownloadDateStrShort() %></a></TD>
            <TD class="qcell_text" width="40%">&nbsp;</TD>
          </TR>
<%
    if (DisplayBean.isTransShown() && file.getTransactionCount() > 0)
    {
%>          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_text" colspan="<%= NUM_COLUMNS - 1 %>">
              <TABLE class="qcell_table" cellpadding="0" cellspacing="0" border="1" frame="hsides" rules="rows" width="100%">
                <TBODY>
                  <TR class="qcell_row" valign="bottom" bgcolor="#c0c0c0">
                    <TD align="center">&nbsp;&nbsp;&nbsp;&nbsp;</TD>
                    <TD align="center"><FONT size="-2"><B>Cust. ID</B></FONT>&nbsp;</TD>
                    <TD align="center"><FONT size="-2"><B>Policy No.</B></FONT></TD>
                    <TD align="center"><FONT size="-2"><B>Eff. Dt.</B></FONT></TD>
                    <TD align="center"><FONT size="-2"><B>Trans.</B></FONT></TD>
                    <TD align="center"><FONT size="-2"><B>Seq.</B></FONT></TD>
                    <TD align="center"><FONT size="-2"><B>Named Insured</B></FONT></TD>
                  </TR>
<%
			totalCount = file.getTransactionCount();	
			if (file.isDirectBill()) {
					totalCount = 1;
			} 
		for (int t=0; t < totalCount; t++)
      {
        connective.teamup.download.db.TransactionInfo trans = file.getTransaction(t);
%>                  <TR class="qcell_row" valign="top" bgcolor="#c0c0c0">
                    <TD class="qcell_text" align="center"><INPUT type="checkbox" name="file<%= i %>trans<%= t %>" value="<%= trans.getSequence() %>"<% if (DisplayBean.isDownloadable(trans.getDownloadStatus())) { %> checked<% } %> onclick="change_file('<%= i %>')"></TD>
                    <TD class="qcell_text" align="center"><%= trans.getCustomerId() %>&nbsp;</TD>
                    <TD class="qcell_text"><A title="<%= trans.getTransEffDate() %> - <%= trans.getDescription() %>" class="listlink"><%= trans.getPolicyNumber() %></A>&nbsp;</TD>
                    <TD class="qcell_text" align="center">&nbsp;<%= trans.getPolicyEffDate() %>&nbsp;</TD>
                    <TD class="qcell_text" align="center"><A class="listlink" title="<%= trans.getTransEffDate() %> - <%= trans.getDescription() %>"><%= trans.getTransType() %></A></TD>
                    <TD class="qcell_text" align="center">&nbsp;<%= trans.getTransSequence() %> &nbsp;</TD>
                    <TD class="qcell_text"><%= trans.getNamedInsured() %> </TD>
                  </TR>
<%
      }
%>                </TBODY>
              </TABLE>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="<%= NUM_COLUMNS %>" align="center">&nbsp;</TD>
          </TR>
<%
    }
  }
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
        <TR><%
String actionName = "archive_showtrans";
String buttonName = "Show Transactions";
if (DisplayBean.isTransShown())
{
	actionName = "archive_hidetrans";
	buttonName = "Hide Transactions";
}
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action_verified('<%= actionName %>')"><%= buttonName %></A></TD> <%
if (DisplayBean.getArchiveFilesCount() > 0)
{
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action_verified('archive_updt')">Save Changes</A></TD> <%
}
%>
          <TD class="pagebar" nowrap="nowrap" width="100%"></TD>
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
