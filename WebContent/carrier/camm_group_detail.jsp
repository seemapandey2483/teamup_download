<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.CammGroupDetailDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	document.tudlform.nextAction.value = "<%= DisplayBean.getNextEvent() %>";
	return true;
}

function allow_action()
{
	return true;
}

function do_next_action()
{
	var emailCount = parseInt(document.tudlform.email_count.value);
	if (emailCount == 0)
	{
		alert("There are no agent recipients available for this action.");
		return;
	}
	
	if (!document.tudlform.nextAction.value)
	{
		alert("Please select the email to send.");
		document.tudlform.nextAction.focus();
		return;
	}
	
	var anySelected = false;
	for (optionCount = 0; optionCount < document.tudlform.agent.length - 1; optionCount++)
	{
		if (document.tudlform.agent[optionCount].checked == true)
			anySelected = true;
	}
	if (anySelected == true)
	{
		do_page_action("camm_group_email");
	}
	else
	{
		alert("You must select at least one agent before sending the email.");
	}
}

function do_edit(agentnum)
{
	document.tudlform.agentID.value = agentnum;
	do_page_action("tplist_edit");
}

function select_all()
{
	for (optionCount = 0; optionCount < document.tudlform.agent.length; optionCount++)
	{
		document.tudlform.agent[optionCount].checked = true;
	}
}

function clear_all()
{
	for (optionCount = 0; optionCount < document.tudlform.agent.length; optionCount++)
	{
		document.tudlform.agent[optionCount].checked = false;
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
      <INPUT type="hidden" name="agentID" value="">
      <INPUT type="hidden" name="groupName" value="<%= DisplayBean.getGroupName() %>">

<!-- BEGIN BODY -->

      <H1>CAMM: Connective's Agent Migration Manager<BR>
          Migration Group Detail
      </H1>
      <H2><%= DisplayBean.getGroupName() %><BR>
          <font size="-1"><%= DisplayBean.getGroupDesc() %></font>
      </H2>

      <TABLE>
        <TR valign="top">
          <TD>
            <table>
              <TR>
                <TD colspan="3"><B>Statistics:</B></TD>
              </TR>
              <TR>
                <TD>&nbsp;&nbsp;</TD>
                <TD nowrap="nowrap">Total Number of Agents:&nbsp;</TD>
                <TD align="right"><%= DisplayBean.getTotalAgents() %></TD>
              </TR>
              <TR>
                <TD>&nbsp;&nbsp;</TD>
                <TD nowrap="nowrap">Migrated Agents:&nbsp;</TD>
                <TD align="right"><%= DisplayBean.getMigratedAgents() %></TD>
              </TR>
              <TR>
                <TD>&nbsp;&nbsp;</TD>
                <TD nowrap="nowrap">Live Agents still using browser app:&nbsp;</TD>
                <TD align="right"><%= DisplayBean.getRemainingAgents() %></TD>
              </TR>
              <TR>
                <TD>&nbsp;&nbsp;</TD>
                <TD nowrap="nowrap"><B>Percent Complete:</B>&nbsp;</TD>
                <TD align="right"><B><%= DisplayBean.getPercentComplete() %></B></TD>
              </TR>
            </table>
          </TD>
          <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
          <TD>
            <table>
              <TR>
                <TD colspan="3"><B>History:</B></TD>
              </TR>
<%
for (int i=0; i < DisplayBean.getEventCount(); i++)
{
%>
              <TR valign="top">
                <TD>&nbsp;&nbsp;<B>&middot;</B></TD>
                <TD nowrap="nowrap"><%= DisplayBean.getEventName(i) %>&nbsp;</TD>
                <TD><%= DisplayBean.getEventDate(i) %></TD>
              </TR>
<%
}
%>
            </table>
          </TD>
        </TR>
        <TR>
          <TD colspan="3">&nbsp;<BR>&nbsp;</TD>
        </TR>
      </TABLE>
      
      <table cellpadding="0" cellspacing="0">
        <tr>
        	<td colspan="9">(<IMG border="0" src="<c:url value="/images/red_check.gif"/>" height="18"> designates agent has completed the migration process)<BR>&nbsp;</td>
        </tr>
		<tr valign="bottom">
			<td>&nbsp;</td>
			<td colspan="3" align="center"><font size="-2"><B>Agent</B></font></td>
			<td>&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>Status</B></font></td>
			<td>&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>Vendor System</B></font></td>
			<td>&nbsp;&nbsp;</td>
		</tr>
<%
String background = "white";
int checkboxes = 0;
for (int i=0; i < DisplayBean.getAgentCount(); i++)
{
   if (background.equals("white"))
      background = "#e8e8e8";
   else
      background = "white";
%>
		<tr valign="middle" bgcolor="<%= background %>">
<%
   if (!DisplayBean.getAgent(i).isActive())
   {
%>			<td align="center" nowrap="nowrap">&nbsp;<INPUT type="checkbox" name="inactive" title="Agent is currently disabled" disabled>&nbsp;</td>
<%
   }
   else if (DisplayBean.getAgent(i).isClientAppRegistered())
   {
%>			<td align="center" nowrap="nowrap"><IMG title="Agent has completed the migration process" border="0" src="<c:url value="/images/red_check.gif"/>" height="18"></td>
<%
   }
   else if (DisplayBean.isAgentEmailOkay(i))
   {
      checkboxes++;
%>			<td align="center" nowrap="nowrap">&nbsp;<INPUT type="checkbox" name="agent" <%= DisplayBean.getChecked(i) %> value="<%= DisplayBean.getAgent(i).getAgentId() %>">&nbsp;</td>
<%
   }
   else
   {
%>			<td align="center" nowrap="nowrap"><IMG title="Missing or invalid email address" border="0" src="<c:url value="/images/no_email.gif"/>" width="29" height="18"></td>
<%
   }
%>			<td nowrap="nowrap"><%= DisplayBean.getAgent(i).getAgentId() %></td>
			<td nowrap="nowrap">&nbsp;-&nbsp;</td>
			<td nowrap="nowrap"><A href="#" onclick="javascript:do_edit('<%= DisplayBean.getAgent(i).getAgentId() %>')"><%= DisplayBean.getAgent(i).getName() %></A>&nbsp;&nbsp;</td>
			<td>&nbsp;&nbsp;</td>
			<td nowrap="nowrap"><%= DisplayBean.getAgent(i).getStatusDisplay() %>&nbsp;&nbsp;</td>
			<td>&nbsp;&nbsp;</td>
			<td nowrap="nowrap"><%= DisplayBean.getAgentVendorSystem(i) %></td>
			<td>&nbsp;&nbsp;</td>
		</tr>
<%
}
%>
        <TR>
          <td colspan="9">&nbsp;<BR>
            &nbsp;&nbsp;<INPUT type="button" value="Select All" onclick="javascript:select_all()">
            &nbsp;&nbsp;<INPUT type="button" value="Clear All" onclick="javascript:clear_all()">
          </td>
        </TR>
        <TR>
          <td colspan="9">&nbsp;<BR>&nbsp;</td>
        </TR>
        <TR>
          <td colspan="9">Email to send to selected agents:
            <SELECT name="nextAction">
<%
for (int i=0; i < DisplayBean.getNextActionCount(); i++)
{
%>              <OPTION value="<%= DisplayBean.getNextAction(i) %>"><%= DisplayBean.getNextAction(i) %></OPTION>
<%
}
%>            </SELECT>&nbsp;
            <INPUT type="button" value="Send..." onclick="javascript:do_next_action()">
          </td>
        </TR>
      </table>

      <INPUT type="hidden" name="agent" value="">
      <INPUT type="hidden" name="email_count" value="<%= checkboxes %>">

<!-- END BODY -->
    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_page_action('menu_rollout_groups')">Cancel</A></TD>
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
