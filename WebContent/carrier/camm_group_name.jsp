<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.CammGroupNameDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	document.tudlform.groupName.focus();
	return true;
}

function allow_action()
{
	return true;
}

function do_submit()
{
	if (!document.tudlform.groupName.value)
	{
		alert("Please enter the agency migration group name.");
		document.tudlform.groupName.focus();
	}
	else if (!document.tudlform.groupDesc.value)
	{
		alert("Please enter a description for the agency migration group.");
		document.tudlform.groupDesc.focus();
	}
	else if (document.tudlform.multiGroups.checked == true && (!document.tudlform.maxAgts.value || !valid_integer(document.tudlform.maxAgts.value) || parseInt(document.tudlform.maxAgts.value) == 0))
	{
		alert("Please enter a valid maximum number of agents per group.");
		document.tudlform.maxAgts.focus();
	}
	else
	{
		var anySelected = false;
<%
if (DisplayBean.getAgentCount() == 1)
{
%>
		anySelected = document.tudlform.agent.checked;
<%
}
else
{
%>
		for (optionCount = 0; optionCount < document.tudlform.agent.length; optionCount++)
		{
			if (document.tudlform.agent[optionCount].checked == true)
				anySelected = true;
		}
<%
}
%>
		if (anySelected == true)
		{
			document.tudlform.action.value = "camm_create_group";
			document.tudlform.submit();
		}
		else
		{
			alert("You must select at least one agent to create this migration group.");
		}
	}
}

function select_multi()
{
	if (document.tudlform.multiGroups.checked == true)
		document.tudlform.maxAgts.disabled = false;
	else
		document.tudlform.maxAgts.disabled = true;
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
    <TD class="menubar" nowrap="nowrap">&nbsp;</TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
      <INPUT type="hidden" name="action" value="">

<!-- BEGIN BODY -->

      <H1>CAMM: Connective's Agent Migration Manager<BR>&nbsp;<BR>&nbsp;
      </H1>
      
      <table>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="right">Migration Group Name:&nbsp;</td>
			<td><INPUT type="text" name="groupName" size="25" maxlength="25" value="<%= DisplayBean.getGroupName() %>"></td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="right">Group Description:&nbsp;</td>
			<td><INPUT type="text" name="groupDesc" size="50" maxlength="230" value="<%= DisplayBean.getGroupDesc() %>"></td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="right">Create Multiple Groups:&nbsp;</td>
			<td><INPUT type="checkbox" name="multiGroups" value="Y" onclick="select_multi()"></td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="right">Max. Agents per Group:&nbsp;</td>
			<td><INPUT type="text" name="maxAgts" size="5" maxlength="3" value="25" disabled="disabled"></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
<%
if (DisplayBean.getAgentCount() > 0)
{
%>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="center" colspan="2"><INPUT type="button" value="Continue" onclick="do_submit()"></td>
		</tr>
<%
}
%>
      </table>
      
      <hr>
      
<%
if (DisplayBean.getTotalAgents() == 0)
{
%>
      <font color="red">&nbsp;<BR>&nbsp;<BR>&nbsp;<BR>
      <b><i>There are no live agents currently needing to be migrated.</i></b></font>
<%
}
else if (DisplayBean.getAgentCount() == 0)
{
%>
      <font color="red">&nbsp;<BR>&nbsp;<BR>&nbsp;<BR>
      <b><i>All live agents needing to be migrated are already members of a migration group.</i></b></font>
<%
}
else
{
%>
      <table cellpadding="0" cellspacing="0" border="0">
        <TR>
          <TD>&nbsp;&nbsp;</TD>
          <TD nowrap="nowrap">Total agents to be migrated:&nbsp;</TD>
          <TD align="right"><%= DisplayBean.getTotalAgents() %></TD>
        </TR>
<%
  if (DisplayBean.getGroupMembers() > 0)
  {
%>
        <TR>
          <TD>&nbsp;&nbsp;</TD>
          <TD nowrap="nowrap">Already members of migration group:&nbsp;</TD>
          <TD align="right"><%= DisplayBean.getGroupMembers() %></TD>
        </TR>
        <TR>
          <TD>&nbsp;&nbsp;</TD>
          <TD nowrap="nowrap"><b>Total agents listed:</b>&nbsp;</TD>
          <TD align="right"><b><%= DisplayBean.getListedAgents() %></b></TD>
        </TR>
<%
  }
%>
      </table>

      <table cellpadding="0" cellspacing="0" border="0">
        <TR>
          <td colspan="9">
            &nbsp;&nbsp;<INPUT type="button" value="Select All" onclick="javascript:select_all()">
            &nbsp;&nbsp;<INPUT type="button" value="Clear All" onclick="javascript:clear_all()"><BR>&nbsp;
          </td>
        </TR>
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
  for (int i=0; i < DisplayBean.getAgentCount(); i++)
  {
     if (background.equals("white"))
        background = "#e8e8e8";
     else
        background = "white";
%>
		<tr valign="middle" bgcolor="<%= background %>">
			<td align="center" nowrap="nowrap">&nbsp;<INPUT type="checkbox" name="agent" checked value="<%= DisplayBean.getAgent(i).getAgentId() %>">&nbsp;</td>
			<td nowrap="nowrap"><%= DisplayBean.getAgent(i).getAgentId() %></td>
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
      </table>
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
