<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.LogInfoDisplayBean" />
<TITLE><%= DisplayBean.getCarrierName() %> - TEAM-UP Download Agency Report</TITLE>
</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
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

<!-- BEGIN REPORT BODY -->

      <H1>Download Log</H1>

      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question"><b>Carrier:</b>&nbsp;</TD>
            <TD class="qcell_question"><%= DisplayBean.getCarrierName() %></TD>
          </TR>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question"><b>Agent ID:</b>&nbsp;</TD>
            <TD class="qcell_question"><%= DisplayBean.getStrAgentID() %></TD>
          </TR>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question"><B>Date Range:</B>&nbsp;</TD>
            <TD class="qcell_question"><%= DisplayBean.getStartDateFormatted() %> - <%= DisplayBean.getEndDateFormatted() %></TD>
          </TR>
        </TBODY>
      </TABLE>
      <p>&nbsp;</p>
<%
if (DisplayBean.getAppCount() == 0)
{
%>
      <I>No records found for the specified date range</I>
<%
}
else
{
%>
      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Activity Date</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Filename</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Created Date</B></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><B>Status</B></TD>
          </TR>
          <TR>
            <TD colspan="8"><HR></TD>
          </TR>
<%
	for (int i=0; i < DisplayBean.getAppCount(); i++)
	{
		if (DisplayBean.getEvent_type(i).compareToIgnoreCase("I") != 0)
		{
%>          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><a title="<%= DisplayBean.getLog_date(i) %>"><%= DisplayBean.getLog_dateDisplay(i) %></a></td>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><%= DisplayBean.getFileName(i) %></td>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><a title="<%= DisplayBean.getCreated_date(i) %>"><%= DisplayBean.getCreated_dateDisplay(i) %></a></td>
            <TD class="qcell_question">&nbsp;</TD>
            <td class="qcell_question" align="center"><%= DisplayBean.getStatus(i) %></td>
          </TR>
<%
			String desc = DisplayBean.getDescription(i);
			if (DisplayBean.isFailed(i) && desc != null && !desc.equals(""))
			{
%>          <TR class="qcell_row" valign="bottom">
            <TD></TD>
            <TD></td>
            <TD class="qcell_question" colspan="6"><font size="-1" color="red"><%= desc %></font></TD>
          </TR>
<%
			}
		}
	}
%>
          <TR>
            <TD colspan="8"><HR></TD>
          </TR>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" colspan="8">Total files shown:  <%= DisplayBean.getAppCount() %></TD>
          </TR>
          <TR>
            <TD colspan="8">&nbsp;</TD>
          </TR>
        </TBODY>
      </TABLE>
<%
}
%>

<!-- END REPORT BODY -->

    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap" width="100%"></TD>
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