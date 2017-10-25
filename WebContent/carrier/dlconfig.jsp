<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.CompanyConfigDisplayBean" />
<TITLE>TEAM-UP Download <% if (DisplayBean.isConfigWizard()) { %>Configuration<% } else { %>Carrier Administration<% } %></TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--

<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{ <%
if (DisplayBean.isInvalidTestfile())
{
%>
	alert("The download test file you specified either does not exist or is not accessible by the application server.  Please verify that the path and filename are correct.");
	document.tudlform.testfile.focus();
<%
}
else
{
%>
	document.tudlform.autopurge.focus();
<%
}
%>
	return true;
}

function allow_action()
{ <%
String btnName = "Save";
if (DisplayBean.isConfigWizard())
	btnName = "Continue";
%>
	if (document.tudlform.data_changed.value == "Y")
	{
		msg = "You are about to lose any changes you have made on this page.  To save your changes, click 'Cancel' and then use the '<%= btnName %>' button at the bottom of the screen.";
		if (!confirm(msg))
			return false;
	}
	
	return true;
}

<%
if (DisplayBean.isConfigWizard())
{
%>function do_back()
{
	if (allow_action() == true)
	{
		do_page_action("config3_back");
	}
}
<%
}
%>

function do_save()
{
	if (document.tudlform.autopurge.checked == true &&
	    (!document.tudlform.archive_period.value || !valid_integer(document.tudlform.archive_period.value)))
	{
		alert("Please enter the number of days to keep downloaded files.");
		document.tudlform.archive_period.focus();
	}
	else if (document.tudlform.archive_period.value && !valid_integer(document.tudlform.archive_period.value))
	{
		alert("Please enter a valid number of days to keep downloaded files.");
		document.tudlform.archive_period.focus();
	}
	else if (!document.tudlform.source_path.value)
	{
		alert("Please enter the source path to import new download files from.");
		document.tudlform.source_path.focus();
	}
	else if (!document.tudlform.import_delete.value)
	{
		alert("Please select a delete option for imported files.");
		document.tudlform.import_delete.focus();
	}
	else if (!document.tudlform.reports_email.value)
	{
		alert("Please enter the email address to be used for all notifications and reports.");
		document.tudlform.reports_email.focus();
	}
	else
	{
		if (!document.tudlform.archive_period.value)
			document.tudlform.archive_period.value = "0";
		
		do_page_action("dlconfigsave");
	}
}

function do_cancel()
{
	do_page_action("home");
}

function throw_change_flag()
{
	document.tudlform.data_changed.value = "Y";
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
          <TD class="actionbar" nowrap="nowrap"><% if (!DisplayBean.isConfigWizard()) { %><jsp:include page="actionbar.jsp" flush="true" /><% } else { %>&nbsp;<% } %></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap"><jsp:include page="menu.jsp" flush="true" /></TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="${requestScope.DisplayBean.servletPath}" />" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="data_changed" value="N">
    <INPUT type="hidden" name="config_wizard" value="<%= DisplayBean.getConfigWizardFlag() %>">

<!-- BEGIN BODY -->

      <H1>TEAM-UP Download Configuration<% if (DisplayBean.isConfigWizard()) { %> Wizard &nbsp;(Page 3 of 5)<% } %><BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2"><B>Archiving Download Files</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" width="100%"><A title="">Automatically purge downloaded files:</A> &nbsp;
              <INPUT type="checkbox" name="autopurge" <% if (DisplayBean.isAutoPurge()) { %>checked<% } %> value="Y" onclick="throw_change_flag()">
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="Number of days' worth of files to keep in the archive any time a purge is performed.">Archive for:</A> <INPUT type="text" name="archive_period" value="<%= DisplayBean.getNumArchiveDays() %>" size="5" onchange="throw_change_flag()"> days</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2"><B>Importing Download Files</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="The default location of the AL3 download files. The Batch Import process will look in this directory for the AL3 formatted download files.">Import file source path:</A> <INPUT type="text" name="source_path" value="<%= DisplayBean.getSourcePath() %>" size="50" maxlength="255" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="">Delete imported source files from source path:</A>
              <SELECT name="import_delete" onchange="throw_change_flag()">
                <OPTION value="N"<% if (DisplayBean.getDeleteAllFilesFlag().equals("N")) { %> selected<% } %>>Only for 'live' trading partners</OPTION>
                <OPTION value="Y"<% if (DisplayBean.getDeleteAllFilesFlag().equals("Y")) { %> selected<% } %>>For all registered trading partners</OPTION>
              </SELECT>
            </TD>
          </TR>
           <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" width="100%"><A title="">Exclude Line of Business from Import for Agent:</A> &nbsp;
              <INPUT type="checkbox" name="excludelob" <% if (DisplayBean.isExcludeLob()) { %>checked<% } %> value="Y" onclick="throw_change_flag()">
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" width="100%"><A title="">Claim XML Allowed:</A> &nbsp;
              <INPUT type="checkbox" name="cliamXMLImportAllowed"   id="cliamXMLImportAllowed" <% if (DisplayBean.isCliamXMLImportAllowed()) { %>checked<% } %> value="Y" onclick="throw_change_flag()">
            </TD>
          </TR>
           <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" width="100%"><A title="">Policy XML Allowed:</A> &nbsp;
              <INPUT type="checkbox" name="policyXMLImportAllowed"   id="policyXMLImportAllowed" <% if (DisplayBean.isPolicyXMLImportAllowed()) { %>checked<% } %> value="Y" onclick="throw_change_flag()">
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2"><B>Trading Partner Lists</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="The default view for displaying the Trading Partner List page.">Default "Trading Partner List" view:</A>
              <SELECT name="tplist_view" onchange="throw_change_flag()">
                <OPTION value="<%= connective.teamup.download.ServerInfo.TPLIST_ALL %>" <% if (DisplayBean.getDefaultTPListView().equals(connective.teamup.download.ServerInfo.TPLIST_ALL)) { %> selected<% } %>>List all agents</OPTION>
                <OPTION value="<%= connective.teamup.download.ServerInfo.TPLIST_SEARCH %>" <% if (DisplayBean.getDefaultTPListView().equals(connective.teamup.download.ServerInfo.TPLIST_SEARCH)) { %> selected<% } %>>Search page</OPTION>
              </SELECT>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
<!--
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2"><B>Downloading Test Files</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
-->
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2"><B><%= DisplayBean.getCarrierShortName() %> Email Addresses</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD>
              <TABLE>
                <TBODY>

                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="Carrier email address used for the 'Contact Us' button at the top of each page.">Agency Contact:</A></TD>
                    <TD class="qcell_text"><INPUT type="text" name="contact_email" value="<%= DisplayBean.getContactEmail() %>" size="35" maxlength="255" onchange="throw_change_flag()"></TD>
                  </TR>

                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="Carrier email address to send confirmation/notification messages to.">Confirmation&nbsp;Reports:</A>&nbsp;</TD>
                    <TD class="qcell_text"><INPUT type="text" name="reports_email" value="<%= DisplayBean.getReportsEmail() %>" size="35" maxlength="255" onchange="throw_change_flag()"></TD>
                  </TR>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="Carrier email address to send system error reports to.">Error Reports:</A></TD>
                    <TD class="qcell_text"><INPUT type="text" name="errors_email" value="<%= DisplayBean.getErrorsEmail() %>" size="35" maxlength="255" onchange="throw_change_flag()"></TD>
                  </TR>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="Carrier email address to send Misc. reports such as Import Reports.">Misc./ Other Reports:</A></TD>
                    <TD class="qcell_text"><INPUT type="text" name="misc_reports_email" value="<%= DisplayBean.getMiscreportsEmail() %>" size="35" maxlength="255" onchange="throw_change_flag()"></TD>
                  </TR>
                  
                </TBODY>
              </TABLE>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
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
<%
if (DisplayBean.isConfigWizard())
{
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_back()">Back</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Continue</A></TD>
<%
}
else
{
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_cancel()">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Save</A></TD>
<%
}
%>
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
