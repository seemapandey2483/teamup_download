<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.CarStatsDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	return true;
}

function allow_action()
{
	return true;
}

function view_group(grp_name, action)
{
	document.tudlform.groupName.value = grp_name;
	do_page_action(action);
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
      <INPUT type="hidden" name="groupName" value="">

<!-- BEGIN BODY -->

      <H1>CAR: Connective's Accelerated Rollout Process</H1>

      
<%
if (DisplayBean.getTotalGroups() == 0)
{
%>
      <em>There are no agent rollout groups defined at this time.</em>
<%
}
else
{
  if (DisplayBean.getRolloutGroupCount() > 0)
  {
%>
      <H2>&nbsp;<BR>Rollout Group Statistics</H2>
      <table cellpadding="0" cellspacing="0" border="0">
		<tr valign="bottom">
			<td>&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>Group</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B># Agents</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B># Registered</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B># Remaining</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>% Complete</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>Last Action</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>Date</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr>
			<TD colspan="15"><hr></TD>
		</tr>
<%
	String background = "white";
	for (int i=0; i < DisplayBean.getRolloutGroupCount(); i++)
	{
		if (background.equals("white"))
			background = "#dddddd";
		else
			background = "white";
%>
		<tr valign="top" bgcolor="<%= background %>">
			<td></td>
			<td nowrap="nowrap"><A href="#" title="View group detail" onclick="javascript:view_group('<%= DisplayBean.escapeForFunction(DisplayBean.getRolloutGroupName(i)) %>', 'car_group_detail')"><%= DisplayBean.getRolloutGroupName(i) %></A></td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getRolloutGroupAgentCount(i) %></td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getRolloutGroupRegisteredCount(i) %></td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getRolloutGroupUnregisteredCount(i) %></td>
			<td></td>
			<td align="center" nowrap="nowrap"><B><%= DisplayBean.getRolloutGroupPercentComplete(i) %></B></td>
			<td></td>
			<td nowrap="nowrap"><%= DisplayBean.getRolloutGroupLastAction(i) %></td>
			<td></td>
			<td nowrap="nowrap"><%= DisplayBean.getRolloutGroupLastActionDate(i) %></td>
			<td></td>
		</tr>
		<tr bgcolor="<%= background %>">
			<td>&nbsp;</td>
			<td colspan="13"><%= DisplayBean.getRolloutGroupDesc(i) %></td>
			<td>&nbsp;</td>
		</tr>
<%
	}
%>
		<tr>
			<TD colspan="15"><hr></TD>
		</tr>
<%
	if (DisplayBean.getRolloutGroupCount() > 1)
	{
%>
		<tr valign="top">
			<td></td>
			<td nowrap="nowrap"><B>Totals:</B>&nbsp; <%= DisplayBean.getRolloutGroupCount() %> rollout groups</td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getTotalRolloutAgents() %></td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getTotalRegistered() %></td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getTotalUnregistered() %></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<TD colspan="15"><hr></TD>
		</tr>
<%
	}
%>
      </table>
<%
  }
  
  if (DisplayBean.getMigrationGroupCount() > 0)
  {
%>
      <H2>&nbsp;<BR>Migration Group Statistics</H2>
      <table cellpadding="0" cellspacing="0" border="0">
		<tr valign="bottom">
			<td>&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>Group</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B># Agents</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B># Migrated</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B># Remaining</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>% Complete</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>Last Action</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>Date</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr>
			<TD colspan="15"><hr></TD>
		</tr>
<%
	String background = "white";
	for (int i=0; i < DisplayBean.getMigrationGroupCount(); i++)
	{
		if (background.equals("white"))
			background = "#dddddd";
		else
			background = "white";
%>
		<tr valign="top" bgcolor="<%= background %>">
			<td></td>
			<td nowrap="nowrap"><A href="#" title="View group detail" onclick="javascript:view_group('<%= DisplayBean.escapeForFunction(DisplayBean.getMigrationGroupName(i)) %>', 'camm_group_detail')"><%= DisplayBean.getMigrationGroupName(i) %></A></td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getMigrationGroupAgentCount(i) %></td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getMigrationGroupMigratedCount(i) %></td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getMigrationGroupUnmigratedCount(i) %></td>
			<td></td>
			<td align="center" nowrap="nowrap"><B><%= DisplayBean.getMigrationGroupPercentComplete(i) %></B></td>
			<td></td>
			<td nowrap="nowrap"><%= DisplayBean.getMigrationGroupLastAction(i) %></td>
			<td></td>
			<td nowrap="nowrap"><%= DisplayBean.getMigrationGroupLastActionDate(i) %></td>
			<td></td>
		</tr>
		<tr bgcolor="<%= background %>">
			<td>&nbsp;</td>
			<td colspan="13"><%= DisplayBean.getMigrationGroupDesc(i) %></td>
			<td>&nbsp;</td>
		</tr>
<%
	}
%>
		<tr>
			<TD colspan="15"><hr></TD>
		</tr>
<%
	if (DisplayBean.getMigrationGroupCount() > 1)
	{
%>
		<tr valign="top">
			<td></td>
			<td nowrap="nowrap"><B>Totals:</B>&nbsp; <%= DisplayBean.getMigrationGroupCount() %> migration groups</td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getTotalMigrationAgents() %></td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getTotalMigrated() %></td>
			<td></td>
			<td align="center" nowrap="nowrap"><%= DisplayBean.getTotalUnmigrated() %></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<TD colspan="15"><hr></TD>
		</tr>
<%
	}
%>
      </table>
<%
  }

  if (DisplayBean.getTotalGroups() == 1)
  {
%>      <input type="hidden" name="group">
<%
  }
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
