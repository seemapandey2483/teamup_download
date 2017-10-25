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
<%
// Set the number of participants to be shone
int NUM_PARTICIPANTS = DisplayBean.getParticipantCount() + 3;
if (NUM_PARTICIPANTS < 12)
	NUM_PARTICIPANTS = 12;
%>
function do_load()
{
	document.tudlform.partcode<%= DisplayBean.getParticipantCount() + 1 %>.focus();
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

function do_cancel()
{
	do_page_action("partcode_cancel");
}

function do_save()
{
	if (!document.tudlform.filename0.value)
	{
		alert("Please enter a unique filename for the primary Agent ID.");
		document.tudlform.filename0.focus();
		return;
	}
<%
 for (int i=1; i <= NUM_PARTICIPANTS; i++)
 {
%>
	if (!document.tudlform.partcode<%= i %>.value && document.tudlform.filename<%= i %>.value)
	{
		alert("You must enter a valid participant code for each filename.");
		document.tudlform.partcode<%= i %>.focus();
		return;
	}
	if (document.tudlform.partcode<%= i %>.value && !document.tudlform.filename<%= i %>.value)
	{
		alert("You must enter a unique filename for each participant code.");
		document.tudlform.filename<%= i %>.focus();
		return;
	}
	if (document.tudlform.partcode<%= i %>.value)
	{
		if (document.tudlform.partcode<%= i %>.value.toUpperCase() == document.tudlform.partcode0.value.toUpperCase()<%
  for (int j=1; j < i; j++) { %> || document.tudlform.partcode<%= i %>.value.toUpperCase() == document.tudlform.partcode<%= j %>.value.toUpperCase()<% } %>)
		{
			alert("You have entered a duplicate participant code.  Each participant code must be unique.");
			document.tudlform.partcode<%= i %>.focus();
			return;
		}
		if (document.tudlform.filename<%= i %>.value.toUpperCase() == document.tudlform.filename0.value.toUpperCase()<%
  for (int j=1; j < i; j++) { %> || document.tudlform.filename<%= i %>.value.toUpperCase() == document.tudlform.filename<%= j %>.value.toUpperCase()<% } %>)
		{
			alert("You have entered a duplicate filename.  Each filename must be unique.");
			document.tudlform.filename<%= i %>.focus();
			return;
		}
	}
<%
 }
%>
	do_page_action("partcode_save");
}

function do_delete(num)
{ <%
  for (int i=1; i < NUM_PARTICIPANTS; i++)
  {
%>
	if (num <= <%= i %>)
	{
		document.tudlform.partcode<%= i %>.value = document.tudlform.partcode<%= i+1 %>.value;
		document.tudlform.filename<%= i %>.value = document.tudlform.filename<%= i+1 %>.value;
	}
<%
  }
%>
	document.tudlform.partcode<%= NUM_PARTICIPANTS %>.value = "";
	document.tudlform.filename<%= NUM_PARTICIPANTS %>.value = "";
	
	throw_change_flag();
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
    <INPUT type="hidden" name="current_sort" value="<%= DisplayBean.getTpSortOrder() %>">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">
    <INPUT type="hidden" name="partcode0" value="<%= DisplayBean.getAgentId() %>">
    <INPUT type="hidden" name="num_participants" value="<%= NUM_PARTICIPANTS %>">

<!-- BEGIN BODY -->

      <H1>Trading Partner Maintenance</H1>
      <H2><%= DisplayBean.getAgentId() %> - <%= DisplayBean.getAgentName() %><BR>&nbsp;</H2>
<%
if (DisplayBean.getDuplicateMessage() != null && !DisplayBean.getDuplicateMessage().equals(""))
{
%>
      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" ><FONT color="red"><%= DisplayBean.getDuplicateMessage() %></FONT></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
        </TBODY>
      </TABLE>
<%
}
%>
      
      <TABLE class="qcell_table" valign="top" cellpadding="0" cellspacing="6" border="0" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" width="30%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" width="5%" align="center"><B>Participant<BR>Code</B></TD>
            <TD class="qcell_question" width="1%">&nbsp;</TD>
            <TD class="qcell_question" width="5%" align="center"><B>&nbsp;<BR>File Name</B></TD>
            <TD class="qcell_question" width="59%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" width="30%" align="right">Agent ID:</TD>
            <TD class="qcell_question" width="5%" align="center"><B><%= DisplayBean.getAgentId() %></B></TD>
            <TD class="qcell_question" width="1%">&nbsp;</TD>
            <TD class="qcell_question" width="5%" align="center"><INPUT type="text" name="filename0" size="14" maxlength="20" value="<%= DisplayBean.getImportFile() %>" onchange="throw_change_flag()"></TD>
            <TD class="qcell_question" width="59%">&nbsp;</TD>
          </TR>
<%
for (int i=1; i <= NUM_PARTICIPANTS; i++)
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" width="30%" align="right"><%= i %>.&nbsp;</TD>
            <TD class="qcell_question" width="5%" align="center"><INPUT type="text" name="partcode<%= i %>" size="10" maxlength="10" value="<%= DisplayBean.getParticipantCode(i-1) %>" onchange="throw_change_flag()"></TD>
            <TD class="qcell_question" width="1%">&nbsp;</TD>
            <TD class="qcell_question" width="5%" align="center"><INPUT type="text" name="filename<%= i %>" size="14" maxlength="20" value="<%= DisplayBean.getParticipantFilename(i-1) %>" onchange="throw_change_flag()"></TD>
            <TD class="qcell_question" width="59%">&nbsp;<A class="brlink" href="javascript:do_delete(<%= i %>)">&laquo; Delete</A></TD>
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_cancel()">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Save</A></TD>
          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
 <%--  <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR> --%>
</TABLE>
<%@ include file="footer.jsp" %>
</BODY>
</HTML>
