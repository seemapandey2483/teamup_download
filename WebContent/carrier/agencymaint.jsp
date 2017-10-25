<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AgencyInfoDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{ <%
  if (DisplayBean.getAmsId().length() == 0)
  {
%>
	document.tudlform.system.value = ""; <%
  }
  
  if (DisplayBean.isDuplicateAgent())
  {
%>
	alert("The agent ID you entered already exists.  Please enter a new agent ID, or return to the Trading Partner list to edit the existing agent.");
<%
  }
  if (DisplayBean.isIdByFilename() && DisplayBean.isFilenameInvalid())
  {
%>
	document.tudlform.filename.focus();
<%
  }
  else if (DisplayBean.isNewAgent())
  {
%>
	document.tudlform.agentID.focus();
<%
  }
  else
  {
%>
	document.tudlform.agencyname.focus();
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

function throw_change_flag()
{
	document.tudlform.data_changed.value = "Y";
}

function do_select_ams()
{
	throw_change_flag();
	
	// Set the default file directory, depending on the agency
	// management system selected
	if (!document.tudlform.system.value)
		return;
	
	<%
//  for (int i=0; i < DisplayBean.getAmsCount(); i++)
//  {
//    if (i > 0) { % >else < % }
//	% >if (document.tudlform.system.value == "< %= DisplayBean.getAmsId(i) % >")
//		document.tudlform.dldir.value = "< %= DisplayBean.getAmsDefaultDir(i) % >";
//	< %
//  }
  %>
//	else
//		document.tudlform.dldir.value = "";
}

function do_default_dir()
{
	throw_change_flag();
	
	// Set the default file directory, depending on the agency
	// management system selected
	if (!document.tudlform.system.value)
		return;
	
	<%
for (int i=0; i < DisplayBean.getAmsCount(); i++)
{
  if (i > 0) { %>else <% }
  %>if (document.tudlform.system.value == "<%= DisplayBean.getAmsId(i) %>")
		document.tudlform.dldir.value = "<%= DisplayBean.getAmsDefaultDir(i) %>";
	<%
}
%>
	else
		document.tudlform.dldir.value = "";
}

function verify_page()
{ <%
if (DisplayBean.isNewAgent())
{
%>
	if (!document.tudlform.agentID.value)
	{
		alert("Please enter a valid agency ID.");
		document.tudlform.agentID.focus();
		return false;
	}
<%
}
%>
	if (!document.tudlform.agencyname.value)
	{
		alert("Please enter the name of this agency.");
		document.tudlform.agencyname.focus();
	}
	else if (!document.tudlform.email.value && document.tudlform.send_email.checked == true)
	{
		alert("Please enter an email address for the agency contact.");
		document.tudlform.email.focus();
	}
<%
if (DisplayBean.isIdByFilename())
{
%>	else if (!document.tudlform.filename.value)
	{
		alert("Please enter the <%= DisplayBean.getImportFilePrompt() %> used for this agency.");
		document.tudlform.filename.focus();
	}
<%
}
if (DisplayBean.isPasswordBlank())
{
%>	else if (!document.tudlform.pword.value)
	{
		alert("Please enter an initial password for this agency.");
		document.tudlform.pword.focus();
	}
<%
}
else
{
%>	else if (document.tudlform.chg_pword.checked == true && !document.tudlform.pword.value)
	{
		alert("Please enter the new password for this agency.");
		document.tudlform.pword.focus();
	}
<%
}
%>
	else
	{
		return true;
	}
	
	return false;
}

function do_save()
{
	if (verify_page())
	{
		do_page_action("tpmaint_save");
	}
}

function do_participants()
{
	if (verify_page())
	{
		document.tudlform.partcodes.value = "Y";
		do_page_action("tpmaint_save");
	}
}

function do_cancel()
{
	do_page_action("tplist_default");
}

function do_archive()
{
	do_page_action("tplist_archive");
}

function do_disable()
{
	if (document.tudlform.disabled.checked == false)
	{
		document.tudlform.send_email.disabled = false;
	}
	else
	{
		document.tudlform.send_email.checked = false;
		document.tudlform.send_email.disabled = true;
	}

	throw_change_flag();
}
<%
if (!DisplayBean.isPasswordBlank())
{
%>
function change_password()
{
	if (document.tudlform.chg_pword.checked == true)
	{
		document.tudlform.pword.disabled = false;
	}
	else
	{
		document.tudlform.pword.value = "";
		document.tudlform.pword.disabled = true;
	}
	
	throw_change_flag();
}
<%
}
if (DisplayBean.getLoginLink() != null && !DisplayBean.getLoginLink().equals(""))
{
%>
function agent_login()
{
	var agtWindow = window.open('<%= DisplayBean.getLoginLink() %>','agt_window');
	agtWindow.focus();
}
<%
}
%>
function view_group(grp_name, grp_action)
{
	document.tudlform.groupName.value = grp_name;
	do_page_action(grp_action);
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
    <INPUT type="hidden" name="data_changed" value="N">
    <INPUT type="hidden" name="origPage" value="<%= DisplayBean.getOriginatingPage() %>">
    <INPUT type="hidden" name="newAgent" value="<%= DisplayBean.getNewAgentFlag() %>">
    <INPUT type="hidden" name="current_sort" value="<%= DisplayBean.getTpSortOrder() %>">
    <INPUT type="hidden" name="groupName" value="">

<!-- BEGIN BODY -->

      <H1>Trading Partner Maintenance<BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
<%
if (DisplayBean.isNewAgent())
{
%>          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="40%"><font color="red"><B>*</B></font> Agency ID:</TD>
            <TD class="qcell_text" width="60%"><INPUT type="text" name="agentID" value="<%= DisplayBean.getAgentId() %>" size="12" maxlength="10" onchange="throw_change_flag()"></TD>
          </TR>
<%
}
else
{
%>          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="40%">Agency ID:</TD>
            <TD class="qcell_text" width="60%"><B><%= DisplayBean.getAgentId() %></B><INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>"></TD>
          </TR>
<%
}
%>          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="40%"><font color="red"><B>*</B></font> Agency Name:</TD>
            <TD class="qcell_text" width="60%"><INPUT type="text" name="agencyname" value="<%= DisplayBean.getAgentName() %>" size="40" maxlength="50" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right"><font color="red"><B>*</B></font> Contact Name:</TD>
            <TD class="qcell_text"><INPUT type="text" name="contact" size="30" maxlength="30" value="<%= DisplayBean.getContactName() %>" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right"><font color="red"><B>*</B></font> Contact Email:</TD>
            <TD class="qcell_text"><INPUT type="text" name="email" size="30" maxlength="50" value="<%= DisplayBean.getContactEmail() %>" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">Contact Phone:</TD>
            <TD class="qcell_text"><FONT size="+1">(</FONT><INPUT size="3" type="text" name="phone_area" maxlength="3" value="<%= DisplayBean.getContactPhoneArea() %>" onchange="throw_change_flag()"><FONT size="+1">)</FONT> &nbsp;<INPUT size="3" type="text" maxlength="3" name="phone_prefix" value="<%= DisplayBean.getContactPhonePrefix() %>" onchange="throw_change_flag()"><FONT size="+1">-</FONT><INPUT size="4" type="text" maxlength="4" name="phone_suffix" value="<%= DisplayBean.getContactPhoneSuffix() %>" onchange="throw_change_flag()"> &nbsp;ext. <INPUT size="4" type="text" maxlength="4" name="phone_ext" value="<%= DisplayBean.getContactPhoneExt() %>" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">Agency Location:</TD>
            <TD class="qcell_text">
              <SELECT name="locstate" onchange="throw_change_flag()">
                <OPTION value=""></OPTION>
<%
   for (int i=0; i < DisplayBean.getStateCount(); i++)
   {
%>                <OPTION value="<%= DisplayBean.getStateAbbreviation(i) %>"<%= DisplayBean.getStateSelected(i) %>><%= DisplayBean.getStateName(i) %></OPTION>
<%
   }
%>
              </SELECT>
            </TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">Agency Management System:</TD>
            <TD class="qcell_text"><SELECT name="system"<% if (DisplayBean.getAmsId().equals("")) { %> onchange="do_select_ams()"<% } else { %> onchange="throw_change_flag()"<% } %>>
              <OPTION value=""> (system unknown)</OPTION>
<%
for (int i=0; i < DisplayBean.getAmsCount(); i++)
{
%>              <OPTION value="<%= DisplayBean.getAmsId(i) %>" <% if (DisplayBean.getAmsId().equals(DisplayBean.getAmsId(i))) { %>selected<% } %>><%= DisplayBean.getAmsName(i) %></OPTION>
<%
}
%>              </SELECT>
<%
if (!DisplayBean.isNewAgent())
{
%>              &nbsp; &nbsp;
                <INPUT type="checkbox" name="updtAmsSettings" value="Y" onchange="throw_change_flag()"> Update system settings
<%
}
%>            </TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">Current Software Version:</TD>
            <TD class="qcell_text"><INPUT type="text" name="version" size="8" maxlength="10" value="<%= DisplayBean.getAmsVersion() %>" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">Download to Directory:</TD>
            <TD class="qcell_text"><INPUT type="text" name="dldir" size="25" maxlength="255" value="<%= DisplayBean.getRemoteDirectory() %>" onchange="throw_change_flag()"<% if (DisplayBean.isNewAgent()) { %> disabled<% } %>> &nbsp;<INPUT type="button" value="Use Default" onclick="do_default_dir()"<% if (DisplayBean.isNewAgent()) { %> disabled<% } %>></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">Scheduled Downloads:</TD>
            <TD class="qcell_text"><% if (DisplayBean.getInteractiveFlag().equals("N")) { %>Yes<% } else { %>No<% } %>
              <INPUT type="hidden" name="interactive" value="<%= DisplayBean.getInteractiveFlag() %>">
          </TR>
<%
if (DisplayBean.isPasswordBlank())
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right"><% if (DisplayBean.isNewAgent()) { %>Initial <% } %>Password:</TD>
            <TD class="qcell_text"><INPUT type="text" name="pword" size="15" maxlength="10" onchange="throw_change_flag()"<% if (DisplayBean.isNewAgent()) { %> value="<%= DisplayBean.getRandomPassword() %>"<% } %>></TD>
          </TR>
<%
}
if (DisplayBean.isIdByFilename())
{
  if (DisplayBean.isFilenameInvalid())
  {
    if (DisplayBean.getDuplicateMessage() != null)
    {
%>          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text"><FONT color="red"><%= DisplayBean.getDuplicateMessage() %></FONT></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right"><FONT color="red"><B><%= DisplayBean.getImportFilePrompt() %>:</B></FONT></TD>
            <TD class="qcell_text"><INPUT type="text" name="filename" size="10" maxlength="20" value="<%= DisplayBean.getImportFile() %>" onchange="throw_change_flag()"></TD>
          </TR>
<%
    }
    else
    {
%>          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right"><FONT color="red"><B><%= DisplayBean.getImportFilePrompt() %>:</B></FONT></TD>
            <TD class="qcell_text"><INPUT type="text" name="filename" size="10" maxlength="20" value="<%= DisplayBean.getImportFile() %>" onchange="throw_change_flag()">&nbsp; &nbsp;<FONT color="red">(This filename is already in use.)</FONT></TD>
          </TR>
<%
    }
  }
  else
  {
%>          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right"><%= DisplayBean.getImportFilePrompt() %>:</TD>
            <TD class="qcell_text"><INPUT type="text" name="filename" size="10" maxlength="20" value="<%= DisplayBean.getImportFile() %>" onchange="throw_change_flag()"></TD>
          </TR>
<%
  }
}
// Added support for participant codes -- 01/31/2004, kwm
if (DisplayBean.getParticipantCount() == 0)
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">Add Participant / Agency Sub Codes:</TD>
            <TD class="qcell_text"><INPUT type="checkbox" name="partcodes" value="Y"></TD>
          </TR>
<%
}
else
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">Participant / Agency Sub Codes:</TD>
            <TD class="qcell_text"><INPUT type="checkbox" name="partcodes_checked" value="Y" checked disabled><INPUT type="hidden" name="partcodes" value=""></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right"><A title="Check this box if you are adding an in-house agent for testing purposes">Test Agent:</A></TD>
            <TD class="qcell_text"><INPUT type="checkbox" name="testAgent" value="Y"<% if (DisplayBean.isTestAgent()) { %> checked<% } %>></TD>
          </TR>
<%
if (DisplayBean.isFilenameInvalid())
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right"><A title="Disable this agent's access to TEAM-UP Download"><FONT color="red"><B>Disabled:</B></FONT></A></TD>
            <TD class="qcell_text"><INPUT type="checkbox" name="disabled" value="Y"<% if (DisplayBean.isAgentDisabled()) { %> checked<% } %> onclick="do_disabled()"></TD>
          </TR>
<%
}
else
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right"><A title="Disable this agent's access to TEAM-UP Download">Disabled:</A></TD>
            <TD class="qcell_text"><INPUT type="checkbox" name="disabled" value="Y"<% if (DisplayBean.isAgentDisabled()) { %> checked<% } %> onclick="do_disabled()"></TD>
          </TR>
<%
}

String status = "";
if (DisplayBean.isNewAgent())
	status = "New trading partner";
else if (DisplayBean.isAgentDisabled())
	status = "Disabled (agent does not have access to the download system)";
else if (!DisplayBean.isAgentRegistered() || !DisplayBean.isAgentLive())
	status = DisplayBean.getStatusDesc();
else
	status = "Live";
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Status:</TD>
            <TD class="qcell_text"><%= status %></TD>
          </TR>
<%
if (DisplayBean.getMigrationGroup() != null && !DisplayBean.getMigrationGroup().equals(""))
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Migration Group:</TD>
            <TD class="qcell_text"><A href="#" onclick="javascript:view_group('<%= DisplayBean.getMigrationGroup() %>', 'camm_group_detail')"><%= DisplayBean.getMigrationGroup() %></A></TD>
          </TR>
<%
}
if (DisplayBean.getRolloutGroup() != null && !DisplayBean.getRolloutGroup().equals(""))
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Agency Rollout Group:</TD>
            <TD class="qcell_text"><A href="#" onclick="javascript:view_group('<%= DisplayBean.getRolloutGroup() %>', 'car_group_detail')"><%= DisplayBean.getRolloutGroup() %></A></TD>
          </TR>
<%
}
if (!DisplayBean.isPasswordBlank())
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">Reset Password:</TD>
            <TD class="qcell_text"><INPUT type="checkbox" name="chg_pword" onclick="change_password()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">New Password:</TD>
            <TD class="qcell_text"><INPUT type="text" name="pword" size="15" maxlength="10" disabled></TD>
          </TR>
<%
}
if (!DisplayBean.isNewAgent())
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" colspan="2" align="center"><B>Reset trading partner, require re-registration:</B> &nbsp;<INPUT type="checkbox" name="reset_agent_to_nonreg" value="Y" onclick="throw_change_flag()"></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" colspan="2" align="center"><B>Send registration email to new trading partner:</B> &nbsp;<INPUT type="checkbox" name="send_email" value="Y"<% if (DisplayBean.isNewAgent()) { %> checked<% } %> onclick="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
        </TBODY>
      </TABLE>

<%
if (DisplayBean.getGroupCount() > 0)
{
%>
      <TABLE class="qcell_table" align="center" border="1" cellpadding="2" rules="none">
        <TBODY>
          <TR class="qcell_row" valign="middle">
            <TD colspan="3" align="left">&nbsp;<B>Agent Groups:</B></TD>
          </TR>
<%
  for (int i=0; i < DisplayBean.getGroupCount(); i++)
  {
%>
          <TR class="qcell_row" valign="middle">
            <TD>&nbsp;&nbsp;</TD>
            <TD><input type="checkbox" name="groupMember" <% if (DisplayBean.isGroupMember(i)) { %>checked<% } %> value="<%= DisplayBean.getGroupName(i) %>"></TD>
            <TD><A title="<%= DisplayBean.getGroupDescription(i) %>"><%= DisplayBean.getGroupName(i) %></A>&nbsp;</TD>
          </TR>
<%
  }
%>
        </TBODY>
      </TABLE>
<%
}
%>
<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_cancel()">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Save</A></TD>
<%
if (DisplayBean.getParticipantCount() > 0)
{
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_participants()">Manage Participant/Agency Sub Codes</A></TD>
<%
}
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_archive()">View File Archive</A></TD>
<%
if (DisplayBean.getLoginLink() != null && !DisplayBean.getLoginLink().equals(""))
{
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:agent_login()">Login as Agent</A></TD>
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
