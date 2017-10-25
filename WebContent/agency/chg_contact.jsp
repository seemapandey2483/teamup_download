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
{ <%
  if (DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed())
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

function do_save()
{
<%
  if (DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed())
  {
%>
	if (!document.tudlform.agencyname.value)
	{
		alert("Please enter the agency name.");
		document.tudlform.agencyname.focus();
	}
	else if (!document.tudlform.locstate.value)
	{
		alert("Please select the state in which your agency is located.");
		document.tudlform.locstate.focus();
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
	else
	{
		do_page_action("contact_save");
	}
<%
  }
  else
  {
%>	do_page_action("contact_save");

<%
  }
%>
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
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">
    <INPUT type="hidden" name="data_changed" value="N">
    <INPUT type="hidden" name="dldir" value="<%= DisplayBean.getRemoteDirectory() %>">
    <INPUT type="hidden" name="interactive" value="<%= DisplayBean.getInteractiveFlag() %>">
    <INPUT type="hidden" name="system" value="<%= DisplayBean.getAmsId() %>">

<!-- BEGIN BODY -->

      <H1>Change Agency Contact Information<BR>&nbsp;<BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="40%">Agency ID:</TD>
            <TD class="qcell_text" width="60%">&nbsp;<%= DisplayBean.getAgentId() %></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="40%">Agency Name:</TD>
            <TD class="qcell_text" width="60%"><INPUT type="text" name="agencyname" value="<%= DisplayBean.getAgentName() %>" size="40" maxlength="50" <% if (!DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>disabled="disabled"<% } else { %>onchange="throw_change_flag()"<% } %>></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="40%">Location:</TD>
            <TD class="qcell_text" width="60%">
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
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Contact Name:</TD>
            <TD class="qcell_text"><INPUT type="text" name="contact" size="30" maxlength="30" value="<%= DisplayBean.getContactName() %>" <% if (!DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>disabled="disabled"<% } else { %>onchange="throw_change_flag()"<% } %>></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Contact Email:</TD>
            <TD class="qcell_text"><INPUT type="text" name="email" size="30" maxlength="50" value="<%= DisplayBean.getContactEmail() %>" <% if (!DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>disabled="disabled"<% } else { %>onchange="throw_change_flag()"<% } %>></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">Contact Phone:</TD>
            <TD class="qcell_text" valign="middle"><FONT size="+1">(</FONT><INPUT size="3" type="text" name="phone_area" maxlength="3" value="<%= DisplayBean.getContactPhoneArea() %>" <% if (!DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>disabled="disabled"<% } else { %>onchange="throw_change_flag()"<% } %>><FONT size="+1">)</FONT> &nbsp;
              <INPUT size="3" type="text" maxlength="3" name="phone_prefix" value="<%= DisplayBean.getContactPhonePrefix() %>" <% if (!DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>disabled="disabled"<% } else { %>onchange="throw_change_flag()"<% } %>><FONT size="+1">-</FONT>
              <INPUT size="4" type="text" maxlength="4" name="phone_suffix" value="<%= DisplayBean.getContactPhoneSuffix() %>" <% if (!DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>disabled="disabled"<% } else { %>onchange="throw_change_flag()"<% } %>> &nbsp;ext. 
              <INPUT size="4" type="text" maxlength="4" name="phone_ext" value="<%= DisplayBean.getContactPhoneExt() %>" <% if (!DisplayBean.getCarrierInfo().isAgentInfoChangeAllowed()) { %>disabled="disabled"<% } else { %>onchange="throw_change_flag()"<% } %>>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('contact_cancel')">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Save Changes</A></TD>
          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
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
