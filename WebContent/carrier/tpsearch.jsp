<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.ParticipantSearchDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	document.tudlform.agentID.focus();
	return true;
}

function allow_action()
{
	return true;
}

function search_by_agentid()
{
	submit_search();
}

function jumpTo(letter)
{
	document.tudlform.alphaStart.value = letter;
	do_page_action("tplist_alpha");
}

function submit_search()
{
	if (!document.tudlform.agentID.value)
	{
		alert("Please enter an agent ID or participant code to search for.");
		document.tudlform.agentID.focus();
		return false;
	}
	
	document.tudlform.agentSearch.value = document.tudlform.agentID.value;
	document.tudlform.search_type.value = "agentid";
	document.tudlform.maxRows.value = document.tudlform.numrows.value;
	do_page_action("tplist_agentid_search");
	
	return true;
}

<%
if (DisplayBean.isSearchByFilename())
{
%>
function search_by_filename()
{
	if (!document.tudlform.filename.value)
	{
		alert("Please enter an import filename to search for.");
		document.tudlform.filename.focus();
	}
	else
	{
		document.tudlform.search_type.value = "filename";
		do_page_action("tplist_agentid_search");
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
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" <% if (DisplayBean.isSearchByFilename()) { %>onsubmit="return false"<% } else { %>onsubmit="return submit_search()"<% } %>>
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentSearch" value="">
    <INPUT type="hidden" name="search_type" value="">
    <INPUT type="hidden" name="alphaStart" value="">
    <INPUT type="hidden" name="startPos" value="0">
    <INPUT type="hidden" name="maxRows" value="-1">

<!-- BEGIN BODY -->

      <H1>Trading Partner Search</H1>
      <TABLE class="qcell_table" cellpadding="0" cellspacing="8" border="0" width="100%">
        <TBODY>
<!-- 
          <TR class="qcell_row">
            <TD class="qcell_question" width="10%" align="left">&nbsp;</TD>
            <TD class="qcell_question" width="25%" align="right">&nbsp;</TD>
            <TD class="qcell_question" width="10%" align="left">&nbsp;</TD>
            <TD class="qcell_question" width="55%" align="left">&nbsp;</TD>
          </TR>
          <TR class="qcell_row">
            <TD class="qcell_question" width="10%" align="left">&nbsp;</TD>
            <TD class="qcell_question" width="25%" align="right">&nbsp;</TD>
            <TD class="qcell_question" width="10%" align="left">&nbsp;</TD>
            <TD class="qcell_question" width="55%" align="left">&nbsp;</TD>
          </TR>
-->
          <TR class="qcell_row">
            <TD class="qcell_question" colspan="2" align="right" valign="middle">Search by agent ID or participant code:</TD>
            <TD class="qcell_question" width="10%" align="left" nowrap="nowrap" valign="bottom">&nbsp;
              <INPUT type="text" name="agentID" size="10" maxlength="10"> 
              <SELECT name="numrows">
                <OPTION value="10">10 rows</OPTION>
                <OPTION value="25">25 rows</OPTION>
                <OPTION value="50">50 rows</OPTION>
                <OPTION value="100">100 rows</OPTION>
              </SELECT>
            </TD>
            <TD class="qcell_question" width="55%" align="left" valign="bottom">&nbsp;<INPUT type="button" name="agentidButton" value="Search" onclick="search_by_agentid()"></TD>
          </TR>
<%
if (DisplayBean.isSearchByFilename())
{
	if (DisplayBean.getErrorMessage() != null)
	{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" width="10%" align="left">&nbsp;</TD>
            <TD class="qcell_question" colspan="3" align="left"><FONT color="red"><I><%= DisplayBean.getErrorMessage() %></I></FONT><BR>&nbsp;</TD>
          </TR>
<%
	}
%>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" colspan="2" align="right">Search by import filename:</TD>
            <TD class="qcell_question" width="10%" align="left">&nbsp;<INPUT type="text" name="filename" size="14" maxlength="20"></TD>
            <TD class="qcell_question" width="55%" align="left">&nbsp;<INPUT type="button" name="filenameButton" value="Search" onclick="search_by_filename()"></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" colspan="2" align="right">View by agent name:</TD>
            <TD class="qcell_question" colspan="2" align="left" nowrap="nowrap">&nbsp;
              <A href="javascript:jumpTo('0')">#</A> | <%
for (char c='A'; c <= 'Z'; c++)
{
	%><A href="javascript:jumpTo('<%= String.valueOf(c) %>')"><%= String.valueOf(c) %></A> | <%
}   %><A href="javascript:do_page_action('tplist_all')">ALL</A></TD>
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
