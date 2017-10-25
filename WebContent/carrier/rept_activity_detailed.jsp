<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.ActivityReportDisplayBean" />
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

function do_sort(sortName)
{
	document.tudlform.orderby.value = sortName;
	do_page_action("report_activity_det");
}

function do_refresh()
{
	if (!document.tudlform.startdt.value)
	{
		alert("Please select start date");
		document.tudlform.startdt.focus();
		return;
	}
	else if (!document.tudlform.enddt.value)
	{
		alert("Please select end date");
		document.tudlform.enddt.focus();
		return;
	}	
	else
	{
		do_page_action("report_activity_det");
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
    <INPUT type="hidden" name="orderby" value="">
    <INPUT type="hidden" name="startdt" value="<%= DisplayBean.getStrStartDT() %>">
    <INPUT type="hidden" name="enddt" value="<%= DisplayBean.getStrEndDT() %>">
    <INPUT type="hidden" name="activity" value="<%= DisplayBean.getEventType() %>">


<!-- BEGIN BODY -->

      <H1>Consolidated Activity Report - Detailed</H1>
      <P><B><%= DisplayBean.getStartDateFormatted() %></B> - <B><%= DisplayBean.getEndDateFormatted() %></B><BR>&nbsp;<BR>&nbsp;</P>
<%
if (DisplayBean.getAppCount() == 0)
{
%>
      <I>No records found</I>
<%
}
else
{
%>
      <TABLE class="qcell_table" cellpadding="0" cellspacing="0" border="0">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question">&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("BATCHNUM")) { %><B>Import<BR>Batch<BR>&nbsp;&nbsp;&nbsp;Number&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('BATCHNUM')">Import<BR>Batch<BR>&nbsp;&nbsp;&nbsp;Number&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Batch Number"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center" colspan="3"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("AGENTID")) { %><B>Agent&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('AGENTID')">Agent&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Agent ID"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("LOG_DT")) { %><B>Activity Date&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('LOG_DT')">Activity Date&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Activity Date"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("ORIG_FILENAME")) { %><B>Filename&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('ORIG_FILENAME')">Filename&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Filename"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("EVENT_TYPE")) { %><B>Activity<BR>&nbsp;&nbsp;&nbsp;Type&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('EVENT_TYPE')">Activity<BR>&nbsp;&nbsp;&nbsp;Type&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Activity Type"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("RESULT_STATUS")) { %><B>Status&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('RESULT_STATUS')">Status&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Status"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center">Status Description</TD>
          </TR>
          <TR>
            <TD colspan="16"><HR></TD>
          </TR>
<%
	for (int i=0; i < DisplayBean.getAppCount(); i++)
	{
%>          <tr valign="top" <% if (i % 2 > 0) { %>bgcolor="#eeeeee"<% } else { %>bgcolor="#ffffff"<% } %>>
            <TD>&nbsp;</TD>
            <td align="center"><%= DisplayBean.getBatchnum(i) %></td>
            <TD>&nbsp;</TD>
            <td><%= DisplayBean.getAgentID(i) %></td>
            <TD>-</TD>
            <td><%= DisplayBean.getAgentName(i) %></td>
            <TD>&nbsp;</TD>
            <td nowrap="nowrap"><%= DisplayBean.getLog_date(i) %></td>
            <TD>&nbsp;</TD>
            <td><%= DisplayBean.getOrigFileName(i) %></td>
            <TD>&nbsp;</TD>
            <td><%= DisplayBean.getEvent_type(i) %></td>
            <TD>&nbsp;</TD>
            <td><%= DisplayBean.getStatus(i) %></td>
            <TD>&nbsp;</TD>
            <td><%= DisplayBean.getDescription(i) %></td>
          </tr>
<%			
	}
%>
          <TR>
            <TD colspan="16">&nbsp;</TD>
          </TR>
          <TR>
            <TD colspan="16"><HR></TD>
          </TR>
          <tr>
            <TD colspan="16">&nbsp;<BR>Total files shown: &nbsp;<%= DisplayBean.getAppCount() %></TD>
          </tr>
        </TBODY>
      </TABLE>
<%
}
%>
<!-- <p>Activity Type:
   <br>&nbsp;&nbsp;&nbsp;I = Imported
   <br>&nbsp;&nbsp;&nbsp;D = Downloaded
</p>
<p>Status:
   <br>&nbsp;&nbsp;&nbsp;S = Success
   <br>&nbsp;&nbsp;&nbsp;F = Failed
</p> -->

<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('report_activity_param_det')">New Report</A></TD>
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
