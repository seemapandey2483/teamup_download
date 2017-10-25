<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.MigrationReportDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
function do_load()
{
	return true;
}

function allow_action()
{
	return true;
}

<jsp:include page="javascript.jsp" flush="true" />

function edit_agent(agentnum)
{
	document.tudlform.agentID.value = agentnum;
	document.tudlform.searchAgentId.value = agentnum;
	do_page_action("tplist_default_new");
}

function view_group(grp_desig)
{
	if (grp_desig == "")
		return;
<%
  for (int i=0; i < DisplayBean.getJavaAppCount(); i++)
  {
    connective.teamup.download.db.AgentInfo agent = DisplayBean.getJavaAppAgent(i);
    String group = DisplayBean.getMigrationGroup(agent.getAgentId());
    if (group != null && !group.equals(""))
    {
%>	else if (grp_desig == "j<%= i %>")
		document.tudlform.groupName.value = "<%= group %>";
<%
    }
  }
  for (int i=0; i < DisplayBean.getBrowserCount(); i++)
  {
    connective.teamup.download.db.AgentInfo agent = DisplayBean.getBrowserAgent(i);
    String group = DisplayBean.getMigrationGroup(agent.getAgentId());
    if (group != null && !group.equals(""))
    {
%>	else if (grp_desig == "b<%= i %>")
		document.tudlform.groupName.value = "<%= group %>";
<%
    }
  }
%>
	do_page_action("camm_group_detail");
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
      <INPUT type="hidden" name="searchAgentId" id ="searchAgentId" value="">
      <INPUT type="hidden" name="groupName" value="">

<!-- BEGIN BODY -->

      <H1>Agency Migration Report</H1>

      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row">
            <TD colspan="11">&nbsp;<BR><B><FONT size="+2">Agents using the Java Client App</FONT></B></TD>
          </TR>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question">&nbsp;&nbsp;</TD>
            <TD class="qcell_question" colspan="3" align="center"><B>Trading Partner</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Status</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Agency Vendor System</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Migration Group</B></TD>
            <TD class="qcell_question">&nbsp;&nbsp;</TD>
          </TR>
          <TR>
            <TD colspan="11"><HR></TD>
          </TR>
<%
if (DisplayBean.getJavaAppCount() == 0)
{
%>
          <TR>
            <TD>&nbsp;</TD>
            <TD colspan="10"><i>No active registered agents are currently configured to use the java client app.</i></TD>
          </TR>
<%
}
else
{
	for (int i=0; i < DisplayBean.getJavaAppCount(); i++)
	{
		connective.teamup.download.db.AgentInfo agent = DisplayBean.getJavaAppAgent(i);
%>          <tr>
            <TD>&nbsp;</TD>
            <td><%= agent.getAgentId() %></td>
            <TD>-</TD>
			<td nowrap="nowrap"><A href="#" onclick="javascript:edit_agent('<%= agent.getAgentId() %>')"><%= agent.getName() %></A>&nbsp;&nbsp;</td>
            <TD>&nbsp;</TD>
            <td><%= agent.getStatusDisplay() %></td>
            <TD>&nbsp;</TD>
            <td><%= agent.getAms().getDisplayName() %></td>
            <TD>&nbsp;</TD>
            <td><%
              String group = DisplayBean.getMigrationGroup(agent.getAgentId());
              if (group != null && !group.equals(""))
              { 
                %><A href="#" onclick="javascript:view_group('j<%= i %>')"><%= group %></A>&nbsp;<% 
              }
            %></td>
          </tr>
<%
	}
}
if (DisplayBean.getJavaAppCount() > 0)
{
%>
          <TR>
            <TD colspan="11"><HR></TD>
          </TR>
          <TR>
            <TD>&nbsp;</TD>
            <TD colspan="10">Total # of agents: &nbsp; <%= DisplayBean.getJavaAppCount() %></TD>
          </TR>
<%
}
%>
          <TR>
            <TD colspan="11"><HR></TD>
          </TR>
        </TBODY>
      </TABLE>

      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row">
            <TD colspan="11">&nbsp;<BR><B><FONT size="+2">Agents using the Browser App</FONT></B></TD>
          </TR>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question">&nbsp;&nbsp;</TD>
            <TD class="qcell_question" colspan="3" align="center"><B>Trading Partner</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Status</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Agency Vendor System</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Migration Group</B></TD>
            <TD class="qcell_question">&nbsp;&nbsp;</TD>
          </TR>
          <TR>
            <TD colspan="11"><HR></TD>
          </TR>
<%
if (DisplayBean.getBrowserCount() == 0)
{
%>
          <TR>
            <TD>&nbsp;</TD>
            <TD colspan="10"><i>No active registered agents are currently configured to use the browser version of the download app.</i></TD>
          </TR>
<%
}
else
{
	for (int i=0; i < DisplayBean.getBrowserCount(); i++)
	{
		connective.teamup.download.db.AgentInfo agent = DisplayBean.getBrowserAgent(i);
%>          <tr>
            <TD>&nbsp;</TD>
            <td><%= agent.getAgentId() %></td>
            <TD>-</TD>
			<td nowrap="nowrap"><A href="#" onclick="javascript:edit_agent('<%= agent.getAgentId() %>')"><%= agent.getName() %></A>&nbsp;&nbsp;</td>
            <TD>&nbsp;</TD>
            <td><%= agent.getStatusDisplay() %></td>
            <TD>&nbsp;</TD>
            <td><%= agent.getAms().getDisplayName() %></td>
            <TD>&nbsp;</TD>
            <td><%
              String group = DisplayBean.getMigrationGroup(agent.getAgentId());
              if (group != null && !group.equals(""))
              { 
                %><A href="#" onclick="javascript:view_group('b<%= i %>')"><%= group %></A>&nbsp;<% 
              }
            %></td>
          </tr>
<%
	}
}
if (DisplayBean.getBrowserCount() > 0)
{
%>
          <TR>
            <TD colspan="11"><HR></TD>
          </TR>
          <TR>
            <TD>&nbsp;</TD>
            <TD colspan="10">Total # of agents: &nbsp; <%= DisplayBean.getBrowserCount() %></TD>
          </TR>
<%
}
%>
          <TR>
            <TD colspan="11"><HR></TD>
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
          <TD class="pagebar" nowrap="nowrap"></TD>
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
