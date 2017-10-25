<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.CarGroupListDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{ <%
  if (DisplayBean.getGroupCount() > 0)
  {
%>
	document.tudlform.group[0].focus();
<%
    if (DisplayBean.getGroupCount() == 1)
    {
%>	document.tudlform.group[0].checked = true;
	document.tudlform.groupName.value = "<%= DisplayBean.getGroup(0).getName() %>";
<%
    }
  }
%>
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
//	do_page_action("car_group_detail");
}

function select_group(grp_name)
{
	document.tudlform.groupName.value = grp_name;
}

function do_migration()
{
	document.tudlform.groupName.value = "";
	do_page_action('camm_define_group');
}

function add_new_group()
{
	document.tudlform.groupName.value = "";
	do_page_action('car_define_group');
}
<%
if (DisplayBean.getGroupCount() > 0)
{
%>
function delete_group()
{
	if (!document.tudlform.groupName.value)
	{
		alert("Please select the rollout group to delete.");
		document.tudlform.group[0].focus();
	}
	else if (confirm("Delete the selected rollout group?"))
	{
		do_page_action("car_delete_group");
	}
}

function send_email()
{
	if (!document.tudlform.groupName.value)
	{
		alert("Please select the rollout group to manage.");
		document.tudlform.group[0].focus();
	}
	else
	{
		do_page_action("car_group_detail_select_unreg");
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

      <H1>CAR: Connective's Accelerated Rollout Process<BR>
          Rollout Group Management<BR>&nbsp;
      </H1>

      
      <table cellpadding="0" cellspacing="0" border="0">
		<tr valign="bottom">
			<td>&nbsp;</td>
			<td align="center"><font size="-2"><B>Group</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>Type</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>Last Action</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><font size="-2"><B>Date</B></font></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
		</tr>
<%
if (DisplayBean.getGroupCount() == 0)
{
%>
		<tr valign="middle">
			<td>&nbsp;</td>
			<td colspan="8"><em>There are no agent rollout groups defined at this time.</em></td>
		</tr>
<%
}
else
{
	String actionName = "car_group_detail";
	String background = "white";
	for (int i=0; i < DisplayBean.getGroupCount(); i++)
	{
		if (background.equals("white"))
			background = "#dddddd";
		else
			background = "white";
		if (DisplayBean.getGroup(i).getType().equals("M"))
			actionName = "camm_group_detail";
		else
			actionName = "car_group_detail";
%>
		<tr valign="middle" bgcolor="<%= background %>">
			<td nowrap="nowrap">&nbsp;&nbsp;<INPUT type="radio" name="group" value="<%= DisplayBean.getGroup(i).getName() %>" onclick="javascript:select_group('<%= DisplayBean.escapeForFunction(DisplayBean.getGroup(i).getName()) %>')"></td>
			<td nowrap="nowrap"><A href="#" title="View group detail" onclick="javascript:view_group('<%= DisplayBean.escapeForFunction(DisplayBean.getGroup(i).getName()) %>', '<%= actionName %>')"><%= DisplayBean.escapeForHtml(DisplayBean.getGroup(i).getName()) %></A></td>
			<td>&nbsp;&nbsp;</td>
			<td nowrap="nowrap"><%= DisplayBean.getGroupType(i) %></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td nowrap="nowrap"><%= DisplayBean.getGroup(i).getLastAction() %></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td nowrap="nowrap"><%= DisplayBean.getGroup(i).getLastActionDateStrShort() %></td>
			<td>&nbsp;&nbsp;</td>
		</tr>
		<tr bgcolor="<%= background %>">
			<td>&nbsp;</td>
			<td colspan="7"><%= DisplayBean.getGroup(i).getDescription() %></td>
			<td>&nbsp;</td>
		</tr>
<%
	}
}
%>
      </table>

<%
if (DisplayBean.getGroupCount() == 1)
{
%>      <input type="hidden" name="group">
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_page_action('car_import_agents_file')">Import Agents from File</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:add_new_group()">Add Rollout Group</A></TD>
<!--      <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_migration()">Add Migration Group</A></TD>  -->
<%
if (DisplayBean.getGroupCount() > 0)
{
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:delete_group()">Delete</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:send_email()">Send Email</A></TD>
<%
}
else
{
%>
          <TD class="pagebar" nowrap="nowrap"><font color="gray">Delete</font></TD>
          <TD class="pagebar" nowrap="nowrap"><font color="gray">Send Email</font></TD>
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
