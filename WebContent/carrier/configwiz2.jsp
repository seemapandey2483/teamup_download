<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.CompanyConfigDisplayBean" />
<TITLE>TEAM-UP Download Configuration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--

<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{ <%
if (DisplayBean.isInvalidGraphicFile())
{
%>
	alert("The graphic file you specified either does not exist or is not accessible by the application server.  Please verify that the path and filename are correct.");
	//document.tudlform.graphicfile.focus();
<%
}
else if (DisplayBean.getCarrierId() == null || DisplayBean.getCarrierId().equals(""))
{
%>
	document.tudlform.carrier_id.focus();
<%
}
else
{
%>
	document.tudlform.carriername.focus();
<%
}
%>
	return true;
}

function allow_action()
{
	if (document.tudlform.data_changed.value == "Y")
	{
		msg = "You are about to lose any changes you have made on this page.  To save your changes, click 'Cancel' and then use the 'Save' button at the bottom of the screen.";
		if (!confirm(msg))
			return false;
	}
	
	return true;
}

function do_back()
{
	if (allow_action() == true)
	{
		do_page_action("config2_back");
	}
}

function do_save()
{
	if (!document.tudlform.carrier_id.value)
	{
		alert("Please enter the carrier's registration ID.  Check with Ebix,Inc support for the correct ID.");
		document.tudlform.carrier_id.focus();
	}
	else if (!document.tudlform.carriername.value)
	{
		alert("Please enter the carrier's name.");
		document.tudlform.carriername.focus();
	}
	else if (!document.tudlform.shortname.value)
	{
		alert("Please enter a 'short name' for the carrier.");
		document.tudlform.shortname.focus();
	}
	else if (document.tudlform.clientAppFlag[0].checked == false && document.tudlform.clientAppFlag[1].checked == false)
	{
		alert("Please select whether or not to default agents to use the new client application (instead of the original browser-based agent app).");
		document.tudlform.clientAppFlag[0].focus();
	}
	else if (document.tudlform.displayMigFlag[0].checked == false && document.tudlform.displayMigFlag[1].checked == false)
	{
		alert("Please select whether or not to display the migration banner graphic on the home page of the agent browser app.");
		document.tudlform.displayMigFlag[0].focus();
	}
	else if (document.tudlform.clientAppFlag[1].checked == true && document.tudlform.displayMigFlag[0].checked == true)
	{
		alert("The migration banner graphic can only be displayed if carrier is defaulting agents to use the new client application.");
		document.tudlform.displayMigFlag[0].focus();
	}
	else if (document.tudlform.bannerGraphicHeight.value && (!valid_integer(document.tudlform.bannerGraphicHeight.value) || parseInt(document.tudlform.bannerGraphicHeight.value) <= 0))
	{
		alert("Please enter a valid integer (greater than zero) for the banner image height, or leave it blank to use the default.");
		document.tudlform.bannerGraphicHeight.focus();
	}
	else if (!document.tudlform.fileformat.value)
	{
		alert("Please select the file format to be downloaded.");
		document.tudlform.fileformat.focus();
	}
	else if (document.tudlform.fileformat.value == "XML")
	{
		alert("The ACORD XML format is not supported in this version.");
		document.tudlform.fileformat.focus();
	}
	else if (!document.tudlform.fileprocess.value)
	{
		alert("Please select the process used to create the ACORD download files.");
		document.tudlform.fileprocess.focus();
	}
	else if (!document.tudlform.link2agt.value)
	{
		alert("Please select the method used to link imported files to the appropriate trading partner.");
		document.tudlform.link2agt.focus();
	}
	else if (!document.tudlform.appserver.value)
	{
		alert("Please select your application server software.");
		document.tudlform.appserver.focus();
	}
	else if (document.tudlform.importsize.value && !valid_integer(document.tudlform.importsize.value))
	{
		alert("Please enter a valid number of bytes for the maximum import size, or leave blank.");
		document.tudlform.importsize.focus();
	}
	else if (document.tudlform.schedport.value && !valid_integer(document.tudlform.schedport.value))
	{
		alert("Please enter a valid number for the scheduled download port, or leave blank to use the default port.");
		document.tudlform.schedport.focus();
	}
<%
//**** Do not use the Security Provider config fields for now -- 06/03/2003, kwm
//	else if (!document.tudlform.secprovider.value)
//	{
//		alert("Please enter a valid java class for the security provider, or accept the default class name.");
//		document.tudlform.secprovider.value = "< %= DisplayBean.escapeForHtml(DisplayBean.getSecurityProvider()) % >";
//		document.tudlform.secprovider.focus();
//	}
//	else if (!document.tudlform.secuser.value)
//	{
//		alert("Please enter the user parameter name to be used for the security provider, or accept the default.");
//		document.tudlform.secuser.value = "< %= DisplayBean.escapeForHtml(DisplayBean.getSecurityUser()) % >";
//		document.tudlform.secuser.focus();
//	}
//	else if (!document.tudlform.secpass.value)
//	{
//		alert("Please enter the password parameter name to be used for the security provider, or accept the default.");
//		document.tudlform.secpass.value = "< %= DisplayBean.escapeForHtml(DisplayBean.getSecurityPassword()) % >";
//		document.tudlform.secpass.focus();
//	}
//**** (end of Security Provider config)
%>
	else
	{
<%
if (!DisplayBean.getUseNewDLControlFlag().equals("Y"))
{
%>		if (document.tudlform.useNewDLFlag[0].checked == true)
		{
			var msg = "You have elected to have agents use the new download control.\n\n";
			msg += "If you have already rolled TEAM-UP Download out to your agents,\n";
			msg += "please discuss with Ebix,Inc support before selecting\n";
			msg += "this option, as it will affect the download of existing agents.\n\n";
			msg += "Do you want to keep this selection and continue with the configuration process?";
			if (!confirm(msg))
			{
				document.tudlform.useNewDLFlag[0].focus();
				return;
			}
		}
<%
}
%>
		if (document.tudlform.id_changed.value == "Y")
		{
			var msg = "You have entered a new company registration ID.  Please verify\n";
			msg += "this company ID with Ebix,Inc support before continuing.\n\n";
			msg += "To edit the company ID, press 'Cancel'; to continue press 'OK'.";
			if (!confirm(msg))
			{
				document.tudlform.carrier_id.focus();
				return;
			}
		}
		
		do_page_action("config2_next");
	}
}

function throw_change_flag()
{
	document.tudlform.data_changed.value = "Y";
}

function throw_change_carrier_id()
{
	throw_change_flag();
	document.tudlform.id_changed.value = "Y";
}

function change_file_process()
{
	throw_change_flag();
	
	if (document.tudlform.fileprocess.value == "KEYLINK")
	{
		document.tudlform.link2agt.value = "filename";
	}
}

function upload_banner_file()
{
	document.tudlform.bannerGraphicHeight.value = "";
	document.tudlform.bannerGraphicHeight.disabled = true;

	var w = screen.width / 2;
	var h = screen.height / 4;
	var importWindow = window.open('<%= DisplayBean.getBannerImportUrl() %>','import_window', 'width=' + w + ',height=' + h + ',left=50,top=50,resizable=yes,scrollbars=yes');
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
          <TD class="actionbar" nowrap="nowrap">&nbsp;</TD>
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
    <INPUT type="hidden" name="id_changed" value="N">
    <INPUT type="hidden" name="config_wizard" value="<%= DisplayBean.getConfigWizardFlag() %>">

<!-- BEGIN BODY -->

      <H1>TEAM-UP Download Configuration Wizard &nbsp;(Page 2 of 5)<BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="3"><B>Carrier Customization</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question"><A title="The carrier's unique registration ID, assigned by Ebix,Inc.">Carrier ID:</A></TD>
            <TD class="qcell_question"><INPUT type="text" name="carrier_id" value="<%= DisplayBean.getCarrierId() %>" size="12" maxlength="10" onchange="throw_change_carrier_id()"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="The carrier's full name, to be displayed throughout the application and in system-generated emails and reports.">Carrier Name:</A></TD>
            <TD class="qcell_question"><INPUT type="text" name="carriername" value="<%= DisplayBean.getCarrierName() %>" size="30" maxlength="255" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="A 'short name' to be displayed in certain menus or page locations (e.g., 'ABC Insurance Company' could use a short name of 'ABC').">Carrier Short Name:</A></TD>
            <TD class="qcell_question"><INPUT type="text" name="shortname" value="<%= DisplayBean.getCarrierShortName() %>" size="30" maxlength="255" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="3"><B>Agency Application Settings</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">Default agents to use the non-browser client application?</TD>
            <TD class="qcell_question">
              <input type="radio" name="clientAppFlag" <% if (DisplayBean.getClientAppFlag().equals("Y")) { %>checked<% } %> value="Y"> Yes
              <input type="radio" name="clientAppFlag" <% if (DisplayBean.getClientAppFlag().equals("N")) { %>checked<% } %> value="N"> No
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">Display migration banner on agent browser home page?</TD>
            <TD class="qcell_question">
              <input type="radio" name="displayMigFlag" <% if (DisplayBean.getDisplayMigrationBannerFlag().equals("Y")) { %>checked<% } %> value="Y"> Yes
              <input type="radio" name="displayMigFlag" <% if (DisplayBean.getDisplayMigrationBannerFlag().equals("N")) { %>checked<% } %> value="N"> No
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">Disable the agency login page?</TD>
            <TD class="qcell_question">
              <input type="radio" name="agentLoginDisabledFlag" <% if (DisplayBean.getAgentLoginDisabledFlag().equals("Y")) { %>checked<% } %> value="Y"> Yes
              <input type="radio" name="agentLoginDisabledFlag" <% if (DisplayBean.getAgentLoginDisabledFlag().equals("N")) { %>checked<% } %> value="N"> No
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="Leave this field blank to use the default TEAM-UP Download log-off page.">Agency app log-off URL:</A></TD>
            <TD class="qcell_question"><input type="text" name="agentLogoutUrl" value="<%= DisplayBean.getAgentLogoutUrl() %>" size="35" maxlength="255"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="Leave this field blank to use the default graphic image height.">Banner image height (in pixels):</A></TD>
            <TD class="qcell_question"><input type="text" name="bannerGraphicHeight" value="<%= DisplayBean.getBannerGraphicHeight() %>" size="5" maxlength="3"> &nbsp; <input type="button" value="Import File" title="Upload a new image file to be used in the banner for the agency app." onclick="upload_banner_file()"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="Check with Ebix,Inc support before selecting 'Yes' as it may impact previously registered agents.">Use the new download control?</A></TD>
            <TD class="qcell_question">
              <input type="radio" name="useNewDLFlag" <% if (DisplayBean.getUseNewDLControlFlag().equals("Y")) { %>checked<% } %> value="Y"> Yes
              <input type="radio" name="useNewDLFlag" <% if (DisplayBean.getUseNewDLControlFlag().equals("N")) { %>checked<% } %> value="N"> No
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="3"><B>Download File Specs</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="Select the ACORD file format used for your download files.">File Format:</A></TD>
            <TD class="qcell_question">
              <SELECT name="fileformat" onchange="throw_change_flag()">
                <OPTION value="AL3"<% if (DisplayBean.getImportFileFormat().equals("AL3")) { %> selected<% } %>>ACORD AL3</OPTION>
<!--            <OPTION value="XML"<% if (DisplayBean.getImportFileFormat().equals("XML")) { %> selected<% } %>>ACORD XML</OPTION> -->
              </SELECT>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="Select the application or process used to create the download files prior to importing into TEAM-UP Download.">File Creation Process:</A></TD>
            <TD class="qcell_question">
              <SELECT name="fileprocess" onchange="change_file_process()">
                <OPTION value="CTI"<% if (DisplayBean.getImportFileCreator().equals("CTI")) { %> selected<% } %>>Connective Solutions</OPTION>
                <OPTION value="KEYLINK"<% if (DisplayBean.getImportFileCreator().equals("KEYLINK")) { %> selected<% } %>>BWC KeyLink</OPTION>
                <OPTION value="CUSTOM"<% if (DisplayBean.getImportFileCreator().equals("CUSTOM")) { %> selected<% } %>>Custom export from legacy system</OPTION>
                <OPTION value="OTHER"<% if (DisplayBean.getImportFileCreator().equals("OTHER")) { %> selected<% } %>>Other</OPTION>
              </SELECT>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="Select the method by which imported files are matched to a registered download trading partner.">Link files to Trading Partners via:</A></TD>
            <TD class="qcell_question" valign="bottom">
              <SELECT name="link2agt" onchange="throw_change_flag()">
                <OPTION value="agentID"<% if (DisplayBean.getImportFileIdMode().equals("agentID")) { %> selected<% } %>>Agent ID (1MHG03)</OPTION>
                <OPTION value="filename"<% if (DisplayBean.getImportFileIdMode().equals("filename")) { %> selected<% } %>>Unique filename</OPTION>
              </SELECT>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="3"><B>Web Server Specs</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="Select the web application server software on which you are running TEAM-UP Download.">Application Server Software:</A></TD>
            <TD class="qcell_question">
              <SELECT name="appserver" onchange="throw_change_flag()">
                <OPTION value="WebSphere"<% if (DisplayBean.getAppServer().equals("WebSphere")) { %> selected<% } %>>WebSphere</OPTION>
                <OPTION value="JBoss"<% if (DisplayBean.getAppServer().equals("JBoss")) { %> selected<% } %>>JBoss</OPTION>
                <OPTION value="Tomcat"<% if (DisplayBean.getAppServer().equals("Tomcat")) { %> selected<% } %>>Tomcat</OPTION>
                <OPTION value="OTHER"<% if (DisplayBean.getAppServer().equals("Other")) { %> selected<% } %>>Other</OPTION>
              </SELECT>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="If your server configuration imposes a maximum size for the HTTP request, enter that size here in bytes.  If there is no maximum limit, enter zero (0) or leave the field blank.">Maximum HTTP Request Size:</A></TD>
            <TD class="qcell_question"><INPUT type="text" name="importsize" value="<%= DisplayBean.getImportBlocksize() %>" size="10" maxlength="12" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="If your server configuration for the Scheduled Download app is using a port other than the default (80 or 443), specify that port here.  To use the default port, leave the field blank.">Scheduled Download Port:</A></TD>
            <TD class="qcell_question"><INPUT type="text" name="schedport" value="<%= DisplayBean.getScheduledDLPort() %>" size="10" maxlength="5" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
<%
//**** Do not use the Security Provider config fields for now -- 06/03/2003, kwm
//          <TR class="qcell_row" valign="top">
//            <TD class="qcell_question" colspan="3"><B>Security Provider Configuration</B></TD>
//          </TR>
//          <TR class="qcell_row" valign="top">
//            <TD class="qcell_question">&nbsp;</TD>
//            <TD class="qcell_question" colspan="2">NOTE: Do not change the default settings unless otherwise instructed.</TD>
//          </TR>
//          <TR class="qcell_row" valign="top">
//            <TD class="qcell_question">&nbsp;</TD>
//            <TD class="qcell_question"><A title="">Security Provider Class:</A></TD>
//            <TD class="qcell_question"><INPUT type="text" name="secprovider" value="< %= DisplayBean.getSecurityProvider() % >" size="60" maxlength="255" onchange="throw_change_flag()"></TD>
//          </TR>
//          <TR class="qcell_row" valign="top">
//            <TD class="qcell_question">&nbsp;</TD>
//            <TD class="qcell_question"><A title="">User Parameter:</A></TD>
//            <TD class="qcell_question"><INPUT type="text" name="secuser" value="< %= DisplayBean.getSecurityUser() % >" size="10" maxlength="255" onchange="throw_change_flag()"></TD>
//          </TR>
//          <TR class="qcell_row" valign="top">
//            <TD class="qcell_question">&nbsp;</TD>
//            <TD class="qcell_question"><A title="">Password Parameter:</A></TD>
//            <TD class="qcell_question"><INPUT type="text" name="secpass" value="< %= DisplayBean.getSecurityPassword() % >" size="10" maxlength="255" onchange="throw_change_flag()"></TD>
//          </TR>
//          <TR class="qcell_row" valign="top">
//            <TD class="qcell_question">&nbsp;</TD>
//            <TD class="qcell_question">&nbsp;</TD>
//            <TD class="qcell_question">&nbsp;</TD>
//          </TR>
//**** (end of Security Provider section)
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
<!--          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_back()">Back</A></TD> -->
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Continue</A></TD>
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
