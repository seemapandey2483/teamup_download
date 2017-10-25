<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.TradingPartnerListDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--

<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{ <%
  if (DisplayBean.getAgentCount() == 1)
  {
%>
	document.tudlform.agent.checked = true;
	document.tudlform.agentID.value = "<%= DisplayBean.getAgent(0).getAgentId() %>";
<%
  }
%>
	document.tudlform.agentID_top.focus();
	
	return true;
}

function allow_action()
{
	return true;
}

function do_addnew()
{
	document.tudlform.agentID.value = "";
	do_page_action("tplist_addnew");
}

function do_edit(agentnum)
{
	document.tudlform.agentID.value = agentnum;
	do_page_action("tplist_edit");
}

function do_sort(sortby)
{
	document.tudlform.sort_order.value = sortby;
	do_page_action("tplist_sort");
}

function do_archive()
{ <%
  if (DisplayBean.getAgentCount() == 1)
  {
%>
	do_page_action("tplist_archive");
<%
  }
  else
  {
%>
	if (!document.tudlform.agentID.value)
	{
		alert("Please select a trading partner to view.");
		document.tudlform.agent[0].focus();
	}
	else
	{
		do_page_action("tplist_archive");
	}
<%
  }
%>
}

function do_files(){
	<%
	if (DisplayBean.getAgentCount() == 1){
	%>
	do_page_action("tplist_files");
	<%
	}
	else
	{
	%>
	if (!document.tudlform.agentID.value){
		alert("Please select a trading partner to view.");
		document.tudlform.agent[0].focus();
		} else{
			do_page_action("tplist_files");
		}
	<%
	}
	%>
}

function do_delete()
{
	msg = "You are about to delete this trading partner (Agent ID: " + document.tudlform.agentID.value + ") and any associated download files from the database.  Are you sure?";
<%
  if (DisplayBean.getAgentCount() == 1)
  {
%>
	if (confirm(msg))
<%
  }
  else 
  {
%>
	if (!document.tudlform.agentID.value)
	{
		alert("Please select a trading partner to delete.");
		document.tudlform.agent[0].focus();
	}
	else if (confirm(msg))
<%
  }
%>	{
		do_page_action("tplist_delete");
	}
}

function on_select_agent(agentnum)
{
	document.tudlform.agentID.value = agentnum;
}

function jumpTo(letter)
{
	document.tudlform.alphaStart.value = letter;
	document.tudlform.startPos.value = 0;
	do_page_action("tplist_alpha");
}

function on_select_numrows(selbox)
{
	if (selbox == "top")
	{
		document.tudlform.maxRows.value = document.tudlform.numrows_top.value;
		document.tudlform.numrows_bottom.value = document.tudlform.numrows_top.value;
	}
	else
	{
		document.tudlform.maxRows.value = document.tudlform.numrows_bottom.value;
		document.tudlform.numrows_top.value = document.tudlform.numrows_bottom.value;
	}
}

function search_by_agentid(editbox)
{
	if (editbox == "top")
	{
		if (!document.tudlform.agentID_top.value)
		{
			alert("Please enter an agent ID or participant code to search for.");
			document.tudlform.agentID_top.focus();
			return false;
		}
		document.tudlform.agentID.value = document.tudlform.agentID_top.value;
	}
	else
	{
		if (!document.tudlform.agentID_bottom.value)
		{
			alert("Please enter an agent ID or participant code to search for.");
			document.tudlform.agentID_bottom.focus();
			return false;
		}
		document.tudlform.agentID.value = document.tudlform.agentID_bottom.value;
	}
	
	document.tudlform.agentSearch.value = document.tudlform.agentID.value;
	document.tudlform.search_type.value = "agentid";
	do_page_action("tplist_agentid_search");
	
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
    <TD class="menubar" nowrap="nowrap"><jsp:include page="menu.jsp" flush="true" /></TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="">
    <INPUT type="hidden" name="sort_order" value="">
    <INPUT type="hidden" name="current_sort" value="<%= DisplayBean.getSortOrder() %>">
    <INPUT type="hidden" name="agentSearch" value="">
    <INPUT type="hidden" name="search_type" value="agentid">
    <INPUT type="hidden" name="alphaStart" value="">
    <INPUT type="hidden" name="startPos" value="0">
    <INPUT type="hidden" name="maxRows" value="10">

<!-- BEGIN BODY -->

      <H1>Trading Partner Maintenance</H1>
      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row">
            <TD class="qcell_question" colspan="3">&nbsp;</TD>
            <TD class="qcell_question" colspan="9" align="left" nowrap="nowrap" valign="middle">
              Search by agent ID or participant code:&nbsp;
              <INPUT type="text" name="agentID_top" size="10" maxlength="10"> 
              <SELECT name="numrows_top" onchange="on_select_numrows('top')">
                <OPTION value="10">10 rows</OPTION>
                <OPTION value="25">25 rows</OPTION>
                <OPTION value="50">50 rows</OPTION>
                <OPTION value="100">100 rows</OPTION>
              </SELECT>
              <INPUT type="button" name="agentidButton_top" value="Search" onclick="search_by_agentid('top')">
            </TD>
          </TR>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" colspan="3">&nbsp;</TD>
            <TD class="qcell_question" colspan="9" align="left" nowrap="nowrap">
              View by agent name:&nbsp;
              <A href="javascript:jumpTo('0')">#</A> | <%
for (char c='A'; c <= 'Z'; c++)
{
	%><A href="javascript:jumpTo('<%= String.valueOf(c) %>')"><%= String.valueOf(c) %></A> | <%
}   %><B>[ALL]</B></TD>
          </TR>
          <TR>
            <TD colspan="12"><HR></TD>
          </TR>
<%
if (DisplayBean.getAgentCount() == 0)
{
%>
          <TR>
            <TD nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD colspan="11" align="center"><i>No trading partners have been created at this time</i></TD>
          </TR>
<%
}
else
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.isCurrentSort("id")) { %><B>ID&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('<%= DisplayBean.getColumnSort("id") %>')">ID&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Agent ID"></A><% } %></TD>
            <TD></TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.isCurrentSort("name")) { %><B>Trading Partner Name&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('<%= DisplayBean.getColumnSort("name") %>')">Trading Partner Name&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Name"></A><% } %></TD>
            <TD class="qcell_question" align="center">&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.isCurrentSort("status")) { %><B>Status&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('<%= DisplayBean.getColumnSort("status") %>')">Status&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Status"></A><% } %></TD>
            <TD class="qcell_question" align="center">&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.isCurrentSort("dldate")) { %><B>Last Download Date&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('<%= DisplayBean.getColumnSort("dldate") %>')">Last Download Date&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Last Download Date"></A><% } %></TD>
            <TD class="qcell_question" align="center">&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.isCurrentSort("vendor")) { %><B>Agency Vendor System&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('<%= DisplayBean.getColumnSort("vendor") %>')">Agency Vendor System&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Vendor System and Version"></A><% } %></TD>
            <TD class="qcell_question" align="center">&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" align="center"><% if (DisplayBean.isCurrentSort("dlapp")) { %><B>Download App&nbsp;<img src="<c:url value="/images/sorted_arrow.gif"/>" width="11" height="14" border="0" alt="Current sort order"></B><% } else { %><A class="listlink" href="javascript:do_sort('<%= DisplayBean.getColumnSort("dlapp") %>')">Download App&nbsp;<img src="<c:url value="/images/sort_arrow.gif"/>" width="11" height="14" border="0" alt="Sort by Download App, Agent ID"></A><% } %></TD>
          </TR>
          <TR>
            <TD colspan="12"><HR></TD>
          </TR>
<%
  for (int i=0; i < DisplayBean.getAgentCount(); i++)
  {
    connective.teamup.download.db.AgentInfo agent = DisplayBean.getAgent(i);
%>          <TR class="qcell_row" valign="top">
            <TD class="qcell_question"><INPUT type="radio" name="agent" value="<%= agent.getAgentId() %>" onclick="on_select_agent('<%= agent.getAgentId() %>')"></TD>
            <TD class="qcell_question"><A href="javascript:do_edit('<%= agent.getAgentId() %>')"><%= agent.getAgentId() %></A></TD>
            <TD>-</TD>
            <TD class="qcell_question"><A href="javascript:do_edit('<%= agent.getAgentId() %>')"><%= agent.getName() %></A></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><%= DisplayBean.getAgentStatus(i) %></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><%= agent.getLastDownloadDateStrShort() %></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><%= DisplayBean.getAgentVendorSystem(i) %></TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><%= agent.getDownloadApp() %></TD>
          </TR>
<%
  }
%>
          <TR>
            <TD colspan="12"><HR></TD>
          </TR>
          <TR class="qcell_row">
            <TD class="qcell_question" colspan="3">&nbsp;</TD>
            <TD class="qcell_question" colspan="9" align="left" nowrap="nowrap" valign="middle">
              Search by agent ID or participant code:&nbsp;
              <INPUT type="text" name="agentID_bottom" size="10" maxlength="10"> 
              <SELECT name="numrows_bottom" onchange="on_select_numrows('bottom')">
                <OPTION value="10">10 rows</OPTION>
                <OPTION value="25">25 rows</OPTION>
                <OPTION value="50">50 rows</OPTION>
                <OPTION value="100">100 rows</OPTION>
              </SELECT>
              <INPUT type="button" name="agentidButton_bottom" value="Search" onclick="search_by_agentid('bottom')">
            </TD>
          </TR>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" colspan="3">&nbsp;</TD>
            <TD class="qcell_question" colspan="9" align="left" nowrap="nowrap">
              View by agent name:&nbsp;
              <A href="javascript:jumpTo('0')">#</A> | <%
  for (char c='A'; c <= 'Z'; c++)
  {
	%><A href="javascript:jumpTo('<%= String.valueOf(c) %>')"><%= String.valueOf(c) %></A> | <%
  }   %><B>[ALL]</B></TD>
          </TR>
<%
}
%>
          <TR>
            <TD colspan="12">&nbsp;<BR>&nbsp;</TD>
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_addnew()"><img alt="Add New Agent" border="0" src="<c:url value="images/add.png"/>"></A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_archive()"><img alt="View Files" border="0" src="<c:url value="images/view.png"/>"></A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_files()"><img alt="View Files" border="0" src="<c:url value="images/view.png"/>"></A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_delete()"><img alt="Delete Agent" border="0" src="<c:url value="images/delete.png"/>"></A></TD>
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
