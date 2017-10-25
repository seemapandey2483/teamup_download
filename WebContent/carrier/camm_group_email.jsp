<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.CammEmailDisplayBean" />
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
      <INPUT type="hidden" name="groupName" value="<%= DisplayBean.getGroupName() %>">
      <INPUT type="hidden" name="nextAction" value="<%= DisplayBean.getNextAction() %>">
      <INPUT type="hidden" name="subject" value="<%= DisplayBean.getSubject() %>">
      <INPUT type="hidden" name="groupType" value="M">

<!-- BEGIN BODY -->

      <H1>CAMM: Connective's Agent Migration Manager<BR>
          Email Confirmation<BR>&nbsp;
      </H1>
      
      <H2>Sample Email:</H2>
      <TABLE width="60%" cellpadding="1" cellspacing="0" border="1" rules="none" bgcolor="#e0e0e0">
        <TBODY>
          <TR>
            <TD class="small_text" width="2%">&nbsp;&nbsp;&nbsp;</TD>
            <TD class="small_text" width="6%">&nbsp;</TD>
            <TD class="small_text" width="90%">&nbsp;</TD>
            <TD class="small_text" width="2%">&nbsp;&nbsp;&nbsp;</TD>
          </TR>
          <TR>
            <TD class="small_text">&nbsp;</TD>
            <TD class="small_text"><B>From:</B>&nbsp;</TD>
            <TD class="small_text" width="90%"><%= DisplayBean.getCarrierInfo().getName() %></TD>
            <TD class="small_text">&nbsp;</TD>
          </TR>
          <TR>
            <TD class="small_text">&nbsp;</TD>
            <TD class="small_text"><B>Send:</B>&nbsp;</TD>
            <TD class="small_text" width="90%"><%= DisplayBean.getSentDate() %></TD>
            <TD class="small_text">&nbsp;</TD>
          </TR>
          <TR>
            <TD class="small_text">&nbsp;</TD>
            <TD class="small_text"><B>To:</B>&nbsp;</TD>
            <TD class="small_text" width="90%"><%= DisplayBean.getAgentName() %> [<%= DisplayBean.getAgentEmail() %>]</TD>
            <TD class="small_text">&nbsp;</TD>
          </TR>
          <TR>
            <TD class="small_text">&nbsp;</TD>
            <TD class="small_text"><B>Subject:</B>&nbsp;</TD>
            <TD class="small_text" width="90%"><%= DisplayBean.getSubject() %></TD>
            <TD class="small_text">&nbsp;</TD>
          </TR>
          <TR>
            <TD class="small_text">&nbsp;&nbsp;&nbsp;</TD>
            <TD class="small_text">&nbsp;<BR>&nbsp;</TD>
            <TD class="small_text">&nbsp;</TD>
            <TD class="small_text">&nbsp;&nbsp;&nbsp;</TD>
          </TR>
          <TR>
            <TD class="small_text">&nbsp;</TD>
            <TD class="small_text" colspan="2"><%= DisplayBean.getBody() %></TD>
            <TD class="small_text">&nbsp;</TD>
          </TR>
          <TR>
            <TD class="small_text">&nbsp;</TD>
            <TD class="small_text">&nbsp;</TD>
            <TD class="small_text">&nbsp;</TD>
            <TD class="small_text">&nbsp;</TD>
          </TR>
        </TBODY>
      </TABLE>
      
      <H2>&nbsp;<BR>Recipient List:</H2>
      <table border="1" rules="rows">
		<tr valign="top">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="center"><FONT size="-1"><B>Agency Name</B></FONT></td>
			<td>&nbsp;&nbsp;</td>
			<td align="center"><FONT size="-1"><B>Contact Name</B></FONT></td>
			<td>&nbsp;&nbsp;</td>
			<td align="center"><FONT size="-1"><B>Email Address</B></FONT></td>
			<td>&nbsp;&nbsp;&nbsp;</td>
		</tr>
<%
for (int i=0; i < DisplayBean.getAgentCount(); i++)
{
%>
		<tr valign="top">
			<td></td><td><%= DisplayBean.getAgent(i).getName() %></td>
			<td></td><td nowrap="nowrap"><%= DisplayBean.getAgent(i).getContactName() %></td>
			<td></td><td nowrap="nowrap"><%= DisplayBean.getAgent(i).getContactEmail() %></td>
			<td></td>
		</tr>
<%
}
%>
      </table>

<!-- END BODY -->
    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_page_action('menu_rollout_groups')">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_page_action('camm_group_detail')">Back</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_page_action('car_send_email')">Send</A></TD>
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
