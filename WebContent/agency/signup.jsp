<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AgencyInfoDisplayBean" />
<TITLE><%= DisplayBean.getCarrierName() %> - TEAM-UP Download Agency Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
<%
  if (DisplayBean.getAmsId().length() == 0)
  {
%>
	document.tudlform.system.value = "";
<%
  }
  if (DisplayBean.isPasswordBlank() && DisplayBean.getCarrierInfo().isAgentPasswordChangeAllowed())
  {
%>
	document.tudlform.change_pword.checked = true;
	document.tudlform.change_pword.disabled = true;
	do_change_password();
<%
  }
  else if (DisplayBean.getCarrierInfo().isAgentPasswordChangeAllowed() && !DisplayBean.isAgentRegistered())
  {
%>
	do_change_password();
<%
  }
  if (DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed())
  {
%>
	document.tudlform.agencyname.focus();
<%
  }
  else
  {
%>
	document.tudlform.system.focus();
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
		document.tudlform.dldir.value = "";
	
	if (document.tudlform.system.value == "<%= DisplayBean.getAmsId() %>")
		document.tudlform.vendor_changed.value = "N";
	else
		document.tudlform.vendor_changed.value = "Y";
}

function do_save()
{ <%
if (DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed())
{
%>
	if (!document.tudlform.agencyname.value)
	{
		alert("Please enter the agency name.");
		document.tudlform.agencyname.focus();
	}
	else if (!document.tudlform.system.value)
	{
		alert("Please select your agency management system.");
		document.tudlform.system.focus();
	}
	else if (!document.tudlform.interactive.value)
	{
		alert("Please select a valid download session mode.");
		document.tudlform.interactive.focus();
	}
	else if (!document.tudlform.contact.value)
	{
		alert("Please enter your name as the primary contact.");
		document.tudlform.contact.focus();
	}
	else if (!document.tudlform.email.value)
	{
		alert("Please enter an email address for the primary contact.");
		document.tudlform.email.focus();
	}
	else if (!document.tudlform.phone_area.value ||
	         !document.tudlform.phone_prefix.value ||
	         !document.tudlform.phone_suffix.value)
	{
		alert("Please enter a valid phone number for the primary contact.");
		document.tudlform.phone_area.focus();
	}
	else if (!document.tudlform.locstate.value)
	{
		alert("Please select the state in which your agency is located.");
		document.tudlform.locstate.focus();
	}
<%
}
else
{
%>
	if (!document.tudlform.system.value)
	{
		alert("Please select your agency management system.");
		document.tudlform.system.focus();
	}
	else if (!document.tudlform.interactive.value)
	{
		alert("Please select a valid download session mode.");
		document.tudlform.interactive.focus();
	}
<%
}
if (DisplayBean.getCarrierInfo().isAgentPasswordChangeAllowed())
{
%>
	else if (document.tudlform.change_pword.checked == true &&
	         !document.tudlform.pword.value)
	{
		alert("Please enter a valid password.");
		document.tudlform.pword.focus();
	}
	else if (document.tudlform.change_pword.checked == true &&
	         !document.tudlform.confirm.value)
	{
		alert("Please confirm your new password.");
		document.tudlform.confirm.focus();
	}
	else if (document.tudlform.change_pword.checked == true &&
	         document.tudlform.pword.value != document.tudlform.confirm.value)
	{
		alert("The passwords you entered do not match.  Type the new password for this account in both text boxes.");
		document.tudlform.pword.focus();
	}
<%
}
%>
	else
	{
		do_page_action("signup_save");
	}
}

<%
if (DisplayBean.getCarrierInfo().isAgentPasswordChangeAllowed())
{
%>
function do_change_password()
{
	throw_change_flag();
	
	if (document.tudlform.change_pword.checked == true)
	{
		document.tudlform.pword.disabled = false;
		document.tudlform.confirm.disabled = false;
	}
	else
	{
		document.tudlform.pword.value = "";
		document.tudlform.pword.disabled = true;
		document.tudlform.confirm.value = "";
		document.tudlform.confirm.disabled = true;
	}
}
<%
}
%>
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
          <TD class="actionbar" nowrap="nowrap">
<%
if (DisplayBean.isAgentRegistered())
{
%><jsp:include page="actionbar.jsp" flush="true" /><%
}
else
{
%><jsp:include page="actionbar_nohome.jsp" flush="true" /><%
}
%></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap"><%
if (DisplayBean.isAgentRegistered())
{
%><jsp:include page="menu.jsp" flush="true" /><%
}
else
{
%><a href="javascript:void(0);" onClick="javascript:open_prn_win('agency/print_signup.jsp')" class="menu">Print Instructions</a><%
}
%></TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">
    <INPUT type="hidden" name="data_changed" value="N">
    <INPUT type="hidden" name="dldir" value="<%= DisplayBean.getRemoteDirectory() %>">
    <INPUT type="hidden" name="vendor_changed" value="N">
<%
if (!DisplayBean.isAgentRegistered())
{
%>    <INPUT type="hidden" name="interactive" value="Y">
<%
}
%>

<!-- BEGIN BODY -->

<%
if (DisplayBean.isAgentRegistered())
{
%>
      <H1>TEAM-UP Download Configuration<BR>&nbsp;</H1>
<%
}
else
{
%>
      <H1>TEAM-UP Download Registration <IMG border="0" src="<c:url value="/images/qBubble.gif"/>" alt="Confirm data below, change if necessary.  The system defaults to change your password while registering." width="20" height="22"><BR>&nbsp;</H1>
<%
}
%>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="40%">Agency ID:</TD>
            <TD class="qcell_text" width="60%"><%= DisplayBean.getAgentId() %></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="40%">Agency:</TD>
            <TD class="qcell_text" width="60%"><INPUT type="text" name="agencyname" value="<%= DisplayBean.getAgentName() %>" size="40" maxlength="50" <% if (DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>onchange="throw_change_flag()"<% } else { %>disabled="disabled"<% } %>></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Agency Management System:</TD>
            <TD class="qcell_text"><SELECT name="system" onchange="do_select_ams()">
<%
for (int i=0; i < DisplayBean.getAmsCount(); i++)
{
%>              <OPTION value="<%= DisplayBean.getAmsId(i) %>" <% if (DisplayBean.getAmsId().equals(DisplayBean.getAmsId(i))) { %>selected<% } %>><%= DisplayBean.getAmsName(i) %></OPTION>
<%
}
%>              </SELECT>
            </TD>
          </TR>
<%
// Remote Directory selection has been moved to page 2 of the 
// registration process (agencyams.jsp). -- 10/04/2003, kwm
//          <TR class="qcell_row" valign="top">
//            <TD class="qcell_question" align="right">Download to Directory:</TD>
//            <TD class="qcell_text"><INPUT type="text" name="dldir" size="25" maxlength="255" value="<= DisplayBean.getRemoteDirectory() >" onchange="throw_change_flag()"> &nbsp;<INPUT type="button" value="Use Default" onclick="do_select_ams()"></TD>
//          </TR>

if (DisplayBean.isAgentRegistered())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Download Session Mode:</TD>
            <TD class="qcell_text"><SELECT name="interactive" onchange="throw_change_flag()">
              <OPTION value="Y" <% if (DisplayBean.getInteractiveFlag().equals("Y")) { %>selected<% } %>>Interactive</OPTION>
              <OPTION value="N" <% if (DisplayBean.getInteractiveFlag().equals("N")) { %>selected<% } %>>Scheduled / Non-interactive</OPTION>
            </SELECT></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Contact Name:</TD>
            <TD class="qcell_text"><INPUT type="text" name="contact" size="30" maxlength="30" value="<%= DisplayBean.getContactName() %>" <% if (DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>onchange="throw_change_flag()"<% } else { %>disabled="disabled"<% } %>></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Contact Email:</TD>
            <TD class="qcell_text"><INPUT type="text" name="email" size="30" maxlength="50" value="<%= DisplayBean.getContactEmail() %>" <% if (DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>onchange="throw_change_flag()"<% } else { %>disabled="disabled"<% } %>></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Contact Phone:</TD>
            <TD class="qcell_text" valign="middle"><FONT size="+1">(</FONT><INPUT size="3" type="text" name="phone_area" maxlength="3" value="<%= DisplayBean.getContactPhoneArea() %>" <% if (DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>onchange="throw_change_flag()"<% } else { %>disabled="disabled"<% } %>><FONT size="+1">)</FONT> &nbsp;
              <INPUT size="3" type="text" maxlength="3" name="phone_prefix" value="<%= DisplayBean.getContactPhonePrefix() %>" <% if (DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>onchange="throw_change_flag()"<% } else { %>disabled="disabled"<% } %>><FONT size="+1">-</FONT>
              <INPUT size="4" type="text" maxlength="4" name="phone_suffix" value="<%= DisplayBean.getContactPhoneSuffix() %>" <% if (DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>onchange="throw_change_flag()"<% } else { %>disabled="disabled"<% } %>> &nbsp;ext. 
              <INPUT size="4" type="text" maxlength="4" name="phone_ext" value="<%= DisplayBean.getContactPhoneExt() %>" <% if (DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>onchange="throw_change_flag()"<% } else { %>disabled="disabled"<% } %>>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Agency Location:</TD>
            <TD class="qcell_text">
<%
if (!DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed())
{
%>
              <SELECT name="locstate" disabled="disabled">
                <OPTION value="<%= DisplayBean.getLocationState() %>" selected><%= DisplayBean.getLocationStateName() %></OPTION>
              </SELECT>
<%
}
else
{
%>
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
<%
}
%>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
<%
if (DisplayBean.getCarrierInfo().isAgentPasswordChangeAllowed())
{
  if (!DisplayBean.isAgentRegistered())
  {
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="center" colspan="2"><B><FONT color="red">We suggest that you replace the system-generated password at this time.</FONT></B></TD>
          </TR>
<%
  }
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right"><B><% if (DisplayBean.isPasswordBlank()) { %>Set<% } else { %>Change<% } %> Password:</B></TD>
            <TD class="qcell_text"><INPUT type="checkbox" name="change_pword" value="Y" onclick="do_change_password()"<% if (DisplayBean.isPasswordBlank() || !DisplayBean.isAgentRegistered()) { %> checked<% } %>></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">New Password:</TD>
            <TD class="qcell_text"><INPUT type="password" name="pword" disabled maxlength="10"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Confirm Password:</TD>
            <TD class="qcell_text"><INPUT type="password" name="confirm" disabled maxlength="10"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
<%
}
if (DisplayBean.isAgentRegistered())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2">To start receiving download <em>exclusively</em> through
            the TEAM-UP Download process, check the Go Live box below.  Be sure to save and test your
            download settings before activating this process.
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right"><B>Go Live:</B></TD>
            <TD class="qcell_text"><INPUT type="checkbox" <% if (DisplayBean.isAgentLive()) { %>checked<% } %> name="golive" value="Y" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
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
<%
if (DisplayBean.isAgentRegistered())
{
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('menu_settings')">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Save Changes</A></TD>
<%
}
else
{
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Continue</A></TD>
<%
}
%>          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
          <TD class="pagelogo"><jsp:include page="pagebar_logo.jsp" flush="true" /></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR>
</TABLE>
</BODY>
</HTML>
