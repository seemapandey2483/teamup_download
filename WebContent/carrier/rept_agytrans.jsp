<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.LogInfoDisplayBean" />
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
	do_page_action("report_agytrans");
}

function do_refresh()
{
	if (!document.tudlform.agyName.value)
	{
		alert("Please select an Agency");
		document.tudlform.agyName.focus();
		return;
	}
	else if (!document.tudlform.startdt.value)
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
		do_page_action("report_agytrans");
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
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getStrAgentID() %>">
    <INPUT type="hidden" name="agyName" value="<%= DisplayBean.getStrAgentID() %>">
    <INPUT type="hidden" name="startdt" value="<%= DisplayBean.getStrStartDT() %>">
    <INPUT type="hidden" name="enddt" value="<%= DisplayBean.getStrEndDT() %>">
    <INPUT type="hidden" name="current_sort" value="<%= DisplayBean.getTpSortOrder() %>">


<!-- BEGIN BODY -->

      <H1>Transaction Log</H1>
      <P><%= DisplayBean.getStrAgencyName() %>: <B><%= DisplayBean.getStartDateFormatted() %></B> - <B><%= DisplayBean.getEndDateFormatted() %></B><BR>&nbsp;<BR>&nbsp;</P>
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
<TABLE class="scroll">
<TR>
	<TD>
      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("BATCHNUM")) { %><B>Import<BR>Batch<BR>&nbsp;&nbsp;&nbsp;Number&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('BATCHNUM')">Import<BR>Batch<BR>&nbsp;&nbsp;&nbsp;Number&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Batch Number"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("LOG_DT")) { %><B>Activity Date&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('LOG_DT')">Activity Date&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Activity Date"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("FILENAME")) { %><B>Filename&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('FILENAME')">Filename&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Filename"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("CREATED_DT")) { %><B>Created Date&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('CREATED_DT')">Created Date&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Created Date"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("IMPORTED_DT")) { %><B>Imported Date&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('IMPORTED_DT')">Imported Date&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Imported Date"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("EVENT_TYPE")) { %><B>Activity<BR>&nbsp;&nbsp;&nbsp;Type&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('EVENT_TYPE')">Activity<BR>&nbsp;&nbsp;&nbsp;Type&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Activity Type"></A><% } %></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.getStrOrderBy().equalsIgnoreCase("RESULT_STATUS")) { %><B>Status&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('RESULT_STATUS')">Status&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Status"></A><% } %></TD>
          </TR>
          <TR>
            <TD colspan="14"><HR></TD>
          </TR>
<%
	for (int i=0; i < DisplayBean.getAppCount(); i++)
	{
%>          <tr>
            <TD>&nbsp;</TD>
            <td align="center"><%= DisplayBean.getBatchnum(i) %></td>
            <TD>&nbsp;</TD>
            <td><a title="<%= DisplayBean.getLog_date(i) %>"><%= DisplayBean.getLog_dateDisplay(i) %></a></td>
            <TD>&nbsp;</TD>
            <td><%= DisplayBean.getOrigFileName(i) %></td>
            <TD>&nbsp;</TD>
            <td><a title="<%= DisplayBean.getCreated_date(i) %>"><%= DisplayBean.getCreated_dateDisplay(i) %></a></td>
            <TD>&nbsp;</TD>
            <td><a title="<%= DisplayBean.getImported_date(i) %>"><%= DisplayBean.getImported_dateDisplay(i) %></a></td>
            <TD>&nbsp;</TD>
            <td align="center"><%= DisplayBean.getEvent_type(i) %></td>
            <TD>&nbsp;</TD>
            <td align="center"><a title="<%= DisplayBean.getDescription(i) %>"><%= DisplayBean.getStatus(i) %></a></td>
          </tr>
<%			
	}
%>
          <TR>
            <TD colspan="14">&nbsp;</TD>
          </TR>
<!--          <TR>
            <TD colspan="14"><HR></TD>
          </TR> -->
        </TBODY>
      </TABLE>
      </TD>
    </TR>
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('report_agytrans_param')">New Report</A></TD>
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
