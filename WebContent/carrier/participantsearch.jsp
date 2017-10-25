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
	document.tudlform.partcode.focus();
	return true;
}

function allow_action()
{
	return true;
}

function search_by_partcode()
{
	submit_search();
}

function submit_search()
{
	if (!document.tudlform.partcode.value)
	{
		alert("Please enter a participant code to search for.");
		document.tudlform.partcode.focus();
		return false;
	}
	
	document.tudlform.search_type.value = "partcode";
	do_page_action("search_partcode");
	
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
		do_page_action("search_partcode");
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
    <INPUT type="hidden" name="search_type" value="">

<!-- BEGIN BODY -->

      <H1>Participant Code Search</H1>
      <TABLE class="qcell_table" valign="top" cellpadding="0" cellspacing="8" border="0" width="100%">
        <TBODY>
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
<%
if (DisplayBean.getErrorMessage() != null)
{
%>
          <TR class="qcell_row">
            <TD class="qcell_question" width="10%" align="left">&nbsp;</TD>
            <TD class="qcell_question" colspan="3" align="left"><FONT color="red"><I><%= DisplayBean.getErrorMessage() %></I></FONT><BR>&nbsp;</TD>
          </TR>
<%
}
%>
          <TR class="qcell_row">
            <TD class="qcell_question" colspan="2" align="right">Participant code:&nbsp;</TD>
            <TD class="qcell_question" width="10%" align="left"><INPUT type="text" name="partcode" size="10" maxlength="10"></TD>
            <TD class="qcell_question" width="55%" align="left">&nbsp;<INPUT type="button" name="partCodeButton" value="Search" onclick="search_by_partcode()"></TD>
          </TR>
<%
if (DisplayBean.isSearchByFilename())
{
%>
          <TR class="qcell_row">
            <TD class="qcell_question" width="10%" align="left">&nbsp;</TD>
            <TD class="qcell_question" width="25%" align="right">&nbsp;</TD>
            <TD class="qcell_question" width="10%" align="left">&nbsp;<BR>&nbsp; &nbsp; &nbsp;<B><I>OR</I></B><BR>
				&nbsp;</TD>
            <TD class="qcell_question" width="55%" align="left">&nbsp;</TD>
          </TR>
          <TR class="qcell_row">
            <TD class="qcell_question" colspan="2" align="right">Import filename:&nbsp;</TD>
            <TD class="qcell_question" width="10%" align="left"><INPUT type="text" name="filename" size="14" maxlength="20"></TD>
            <TD class="qcell_question" width="55%" align="left">&nbsp;<INPUT type="button" name="filenameButton" value="Search" onclick="search_by_filename()"></TD>
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
