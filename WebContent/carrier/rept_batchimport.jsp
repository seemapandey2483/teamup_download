<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.BatchLogDisplayBean" />
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

<!-- BEGIN BODY -->

      <H1>Import Log</H1>
      <P><B>Batch Number:</B> <%= DisplayBean.getBatchNumber() %><BR>
      <B>Import Date:</B> <%= DisplayBean.getImportDate() %><BR>
      &nbsp;<BR>&nbsp;</P>
<%
if (DisplayBean.getLogCount() == 0)
{
%>
      <I>No records found</I>
<%
}
else
{
%>
      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Filename</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Trading Partner</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Created Date</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Status</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Message</B></TD>
          </TR>
          <TR>
            <TD colspan="10"><HR></TD>
          </TR>
<%
	for (int i=0; i < DisplayBean.getLogCount(); i++)
	{
%>          <tr>
            <TD>&nbsp;</TD>
            <td><%= DisplayBean.getLog(i).getOrigFileName() %></td>
            <TD>&nbsp;</TD>
            <td align="center"><%= DisplayBean.getLog(i).getAgentID() %></td>
            <TD>&nbsp;</TD>
            <td align="center"><a title="<%= DisplayBean.getLog(i).getCreatedDateStrLong() %>"><%= DisplayBean.getLog(i).getCreatedDateStrShort() %></a></td>
            <TD>&nbsp;</TD>
            <td align="center"><%= DisplayBean.getLog(i).getEventStatus().getSimpleStatusDescription() %></td>
            <TD>&nbsp;</TD>
            <td><%= DisplayBean.getLog(i).getDescription() %></td>
          </tr>
<%			
	}
%>
          <TR>
            <TD colspan="10"><HR></TD>
          </TR>
          <tr>
            <TD colspan="10">&nbsp;<BR>Total files imported: &nbsp;<%= DisplayBean.getImportCount() %></TD>
          </tr>
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
          <TD class="pagebar" nowrap="nowrap"></TD>
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
