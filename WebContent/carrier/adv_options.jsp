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
{
<%
if (DisplayBean.getAgencyRegistrationFlag().equals("N"))
{
%>	document.tudlform.flag_agtreg[1].focus();
<%
}
else
{
%>	document.tudlform.flag_agtreg[0].focus();
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
		do_page_action("config4_back");
	}
}
<%
}
%>

function do_save()
{
	if (document.tudlform.flag_agtreg[0].checked == false && document.tudlform.flag_agtreg[1].checked == false)
	{
		alert("Do you wish to receive email notification when an agent completes the registration process?");
		document.tudlform.flag_agtreg[0].focus();
	}
//	else if (document.tudlform.flag_agtmig[0].checked == false && document.tudlform.flag_agtmig[1].checked == false)
//	{
//		alert("Do you wish to receive email notification when an agent completes the migration process?");
//		document.tudlform.flag_agtmig[0].focus();
//	}
	else if (document.tudlform.flag_statuschg[0].checked == false && document.tudlform.flag_statuschg[1].checked == false)
	{
		alert("Do you wish to receive email notification when an agent changes its download status?");
		document.tudlform.flag_statuschg[0].focus();
	}
	else if (document.tudlform.flag_importerr[0].checked == false && document.tudlform.flag_importerr[1].checked == false)
	{
		alert("Do you wish to receive email notification an error occurs during the file import process?");
		document.tudlform.flag_importerr[0].focus();
	}
	else
	{
		do_page_action("advopt_save");
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

function upload_template_file()
{
	var w = screen.width / 2;
	var h = screen.height / 4;
	var importWindow = window.open('<%= DisplayBean.getCustomFileImportUrl() %>','import_window', 'width=' + w + ',height=' + h + ',left=50,top=50,resizable=yes,scrollbars=yes');
	importWindow.focus();
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
    <INPUT type="hidden" name="flag_agtmig" value="Y">
    <INPUT type="hidden" name="flag_loginShortcutCtrl" value="<%= DisplayBean.getDisplayLoginShortcutCtrl() %>">

<!-- BEGIN BODY -->

      <H1>TEAM-UP Download Configuration<% if (DisplayBean.isConfigWizard()) { %> Wizard &nbsp;(Page 4 of 5)<% } %><BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="3"><B>Email Notification Settings</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" colspan="2">
              <TABLE class="qcell_table">
                <TBODY>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Receive email notification of agency registration?</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" name="flag_agtreg" value="Y" <% if (DisplayBean.getAgencyRegistrationFlag().equals("Y")) { %>checked <% } %>onclick="throw_change_flag()"> Yes &nbsp; &nbsp;<INPUT type="radio" name="flag_agtreg" value="N" <% if (DisplayBean.getAgencyRegistrationFlag().equals("N")) { %>checked <% } %>onclick="throw_change_flag()"> No</TD>
                  </TR>
<!--
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Receive email notification of agency migration?</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" name="flag_agtmig" value="Y" <% if (DisplayBean.getAgencyMigrationFlag().equals("Y")) { %>checked <% } %>onclick="throw_change_flag()"> Yes &nbsp; &nbsp;<INPUT type="radio" name="flag_agtmig" value="N" <% if (DisplayBean.getAgencyMigrationFlag().equals("N")) { %>checked <% } %>onclick="throw_change_flag()"> No</TD>
                  </TR>
-->
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Receive email notification of agency status change?</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" name="flag_statuschg" value="Y" <% if (DisplayBean.getAgencyStatusChangeFlag().equals("Y")) { %>checked <% } %>onclick="throw_change_flag()"> Yes &nbsp; &nbsp;<INPUT type="radio" name="flag_statuschg" value="N" <% if (DisplayBean.getAgencyStatusChangeFlag().equals("N")) { %>checked <% } %>onclick="throw_change_flag()"> No</TD>
                  </TR>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Receive email notification of agency vendor system/version change?</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" name="flag_agtvendorchg" value="Y" <% if (DisplayBean.getAgencyVendorChangeFlag().equals("Y")) { %>checked <% } %>onclick="throw_change_flag()"> Yes &nbsp; &nbsp;<INPUT type="radio" name="flag_agtvendorchg" value="N" <% if (DisplayBean.getAgencyVendorChangeFlag().equals("N")) { %>checked <% } %>onclick="throw_change_flag()"> No</TD>
                  </TR>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Receive email notification of agency download errors?</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" name="flag_downloaderr" value="Y" <% if (DisplayBean.getDownloadErrorsFlag().equals("Y")) { %>checked <% } %>onclick="throw_change_flag()"> Yes &nbsp; &nbsp;<INPUT type="radio" name="flag_downloaderr" value="N" <% if (DisplayBean.getDownloadErrorsFlag().equals("N")) { %>checked <% } %>onclick="throw_change_flag()"> No</TD>
                  </TR>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Receive email notification of carrier import errors?</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" name="flag_importerr" value="Y" <% if (DisplayBean.getImportErrorsFlag().equals("Y")) { %>checked <% } %>onclick="throw_change_flag()"> Yes &nbsp; &nbsp;<INPUT type="radio" name="flag_importerr" value="N" <% if (DisplayBean.getImportErrorsFlag().equals("N")) { %>checked <% } %>onclick="throw_change_flag()"> No</TD>
                  </TR>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Send automatic emails as HTML where applicable?</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" name="flag_html_email" value="Y" <% if (DisplayBean.getEmailAsHtml().equals("Y")) { %>checked <% } %>onclick="throw_change_flag()"> Yes &nbsp; &nbsp;<INPUT type="radio" name="flag_html_email" value="N" <% if (DisplayBean.getEmailAsHtml().equals("N")) { %>checked <% } %>onclick="throw_change_flag()"> No</TD>
                  </TR>
                </TBODY>
              </TABLE>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="3"><B>Trading Partner Configuration</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" colspan="2">
              <TABLE class="qcell_table">
                <TBODY>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="If you select 'No,' the trading partner users will not see the options for changing their own password.">Allow trading partners to change their own passwords?</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" name="flag_chgpwd" value="Y" <% if (DisplayBean.getPasswordChangeFlag().equals("Y")) { %>checked <% } %>onclick="throw_change_flag()"> Yes &nbsp; &nbsp;<INPUT type="radio" name="flag_chgpwd" value="N" <% if (DisplayBean.getPasswordChangeFlag().equals("N")) { %>checked <% } %>onclick="throw_change_flag()"> No</TD>
                  </TR>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="If you select 'No,' the trading partner users will not be allowed to change any contact info from within the TEAM-UP Download application.">Allow trading partners to change their own contact info?</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" name="flag_chgagtinfo" value="Y" <% if (DisplayBean.getAgentInfoChangeFlag().equals("Y")) { %>checked <% } %>onclick="throw_change_flag()"> Yes &nbsp; &nbsp;<INPUT type="radio" name="flag_chgagtinfo" value="N" <% if (DisplayBean.getAgentInfoChangeFlag().equals("N")) { %>checked <% } %>onclick="throw_change_flag()"> No</TD>
                  </TR>
<!-- 
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="If you select 'No,' the trading partner users will not have access to the ActiveX control for creating a desktop icon/shortcut to the TEAM-UP Download agency application login page..">Give trading partners automated option to create a desktop shortcut to the login page?</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" name="flag_loginShortcutCtrl" value="Y" <% if (DisplayBean.getDisplayLoginShortcutCtrl().equals("Y")) { %>checked <% } %>onclick="throw_change_flag()"> Yes &nbsp; &nbsp;<INPUT type="radio" name="flag_loginShortcutCtrl" value="N" <% if (DisplayBean.getDisplayLoginShortcutCtrl().equals("N")) { %>checked <% } %>onclick="throw_change_flag()"> No</TD>
                  </TR>
-->
                </TBODY>
              </TABLE>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
<!-- 
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="3"><B>Customizable Text Files</B></TD>
          </TR>
           <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><input type="button" value="Import File Template" title="Upload a new text file to be used as a file template for customizable pages or emails." onclick="upload_template_file()"></TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
-->
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
  <%-- <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR> --%>
</TABLE>
<%@ include file="footer.jsp" %>
</BODY>
</HTML>
