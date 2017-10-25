<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.DistFileAgentsDisplayBean" />
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

function do_submit()
{
	var itemsChecked = false;
  <%
  if (DisplayBean.getAgentCount() == 1)
  {
  %>
	if (document.tudlform.include[0].checked == true)
	{
		itemsChecked = true;
	}
  <%
  }
  else if (DisplayBean.getAgentCount() > 1)
  {
  %>
	for (count = 0; count < <%= DisplayBean.getAgentCount() %>; count++)
	{
		if (document.tudlform.include[count].checked == true)
		{
			itemsChecked = true;
			count = <%= DisplayBean.getAgentCount() %>;
		}
	}
  <%
  }
  %>
	if (itemsChecked)
		do_page_action("dist_email_save_agents");
	else
		alert("You must select at least one agent to receive the email.");
}

function do_back()
{
	do_page_action("dist_email_params");
}

function select_all()
{
  <%
  if (DisplayBean.getAgentCount() == 1)
  {
  %>
	document.tudlform.include[0].checked == true;
  <%
  }
  else if (DisplayBean.getAgentCount() > 1)
  {
  %>
	for (count = 0; count < <%= DisplayBean.getAgentCount() %>; count++)
	{
		document.tudlform.include[count].checked = true;
	}
  <%
  }
  %>
}

function deselect_all()
{
  <%
  if (DisplayBean.getAgentCount() == 1)
  {
  %>
	document.tudlform.include[0].checked == false;
  <%
  }
  else if (DisplayBean.getAgentCount() > 1)
  {
  %>
	for (count = 0; count < <%= DisplayBean.getAgentCount() %>; count++)
	{
		document.tudlform.include[count].checked = false;
	}
  <%
  }
  %>
}

function check_save()
{
	if (document.tudlform.save_query_as.checked == true)
	{
		document.tudlform.save_query_name.disabled = false;
		document.tudlform.save_query_name.focus();
	}
	else
	{
		document.tudlform.save_query_name.disabled = true;
	}
}

function do_cancel()
{
	do_page_action("dist_cancel");
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
    <INPUT type="hidden" name="agyGroup" value="<%= DisplayBean.getAgyGroup() %>">
    <INPUT type="hidden" name="amsId" value="<%= DisplayBean.getAmsId() %>">
    <INPUT type="hidden" name="amsVersion" value="<%= DisplayBean.getAmsVersion() %>">
    <INPUT type="hidden" name="distBatchNumber" value="<%= DisplayBean.getDistBatchNumber() %>">

<!-- BEGIN BODY -->

      <H1>Select Email Recipients</H1>
      <P><B>Current query:</B>&nbsp; <%= DisplayBean.getQueryText() %></P>
      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" align="center">&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center"><FONT size="-2"><B>ID</B></FONT></TD>
            <TD class="qcell_question" align="center">&nbsp;</TD>
            <TD class="qcell_question" align="center"><FONT size="-2"><B>Agent Name</B></FONT></TD>
            <TD class="qcell_question" align="center">&nbsp;</TD>
            <TD class="qcell_question" align="center"><FONT size="-2"><B>Vendor/<BR>Version</B></FONT></TD>
            <TD class="qcell_question" align="center">&nbsp;</TD>
            <TD class="qcell_question" align="center"><FONT size="-2"><B>Include</B></FONT></TD>
          </TR>
          <TR>
            <TD colspan="8"><HR></TD>
          </TR>
<%
for (int i=0; i < DisplayBean.getAgentCount(); i++)
{
%>          <TR class="qcell_row" valign="top">
            <TD class="qcell_question"></TD>
            <TD class="qcell_question"><%= DisplayBean.getAgentInfo(i).getAgentId() %></TD>
            <TD class="qcell_question">-</TD>
            <TD class="qcell_question"><%= DisplayBean.getAgentInfo(i).getName() %></TD>
            <TD class="qcell_question"></TD>
            <TD class="qcell_question"><%= DisplayBean.getAgentInfo(i).getAms().getDisplayName() %> <%= DisplayBean.getAgentInfo(i).getAmsVer() %></TD>
            <TD class="qcell_question"></TD>
            <TD class="qcell_question" align="center"><input type="checkbox" name="include" value="<%= DisplayBean.getAgentInfo(i).getAgentId() %>" <% if (DisplayBean.isIncluded(i)) { %>checked<% } %>></TD>
          </TR>
<%
}
%>
          <TR>
            <TD colspan="8"><HR></TD>
          </TR>
        </TBODY>
      </TABLE>
      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question">
              <input type="button" onclick="javascript:select_all();" value="Select all"> &nbsp; &nbsp;
              <input type="button" onclick="javascript:deselect_all();" value="Deselect all">
            </TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
<!--
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question"><input type="checkbox" name="update_query">Update this saved group</TD>
          </TR>
-->
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question"><input type="checkbox" name="save_query_as" onclick="javascript:check_save()" value="Y">Save selected agents as a new group: <input type="text" name="save_query_name" size="30" maxlength="50" disabled></TD>
          </TR>
        </TBODY>
      </TABLE>
<%
if (DisplayBean.getAgentCount() == 1)
{
%>
	<input type="hidden" name="include" value="">
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_cancel()">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_back()">Back</A></TD>
<%
if (DisplayBean.getAgentCount() > 0)
{
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_submit()">Continue</A></TD>
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
