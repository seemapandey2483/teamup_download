<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AgentRollupDisplayBean" />
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

function do_cancel()
{
	do_page_action("tplist_default");
}

function do_continue()
{
	do_page_action("tp_rollup");
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
    <INPUT type="hidden" name="rollfrom" value="<%= DisplayBean.getOldAgentId() %>">
    <INPUT type="hidden" name="rollinto" value="<%= DisplayBean.getRollToAgentId() %>">

<!-- BEGIN BODY -->

      <H1>Trading Partner Roll-Up</H1>
      <TABLE class="qcell_table" valign="top" cellpadding="0" cellspacing="8" border="0" width="100%">
        <TBODY>
          <TR class="qcell_row">
            <TD class="qcell_question" width="15%" align="right">&nbsp;</TD>
            <TD class="qcell_question" width="85%" align="left">&nbsp;</TD>
          </TR>
<%
if (DisplayBean.getOldAgentName() != null)
{
%>          <TR class="qcell_row">
            <TD class="qcell_question" width="15%" align="right">Source&nbsp;Trading&nbsp;Partner:&nbsp;</TD>
            <TD class="qcell_question" width="85%" align="left"><B><%= DisplayBean.getOldAgentId() %> - <%= DisplayBean.getOldAgentName() %></B></TD>
          </TR>
<%
}
if (DisplayBean.getRollToAgentName() != null)
{
%>          <TR class="qcell_row">
            <TD class="qcell_question" width="15%" align="right">Roll&nbsp;Into:&nbsp;</TD>
            <TD class="qcell_question" width="85%" align="left"><B><%= DisplayBean.getRollToAgentId() %> - <%= DisplayBean.getRollToAgentName() %></B></TD>
          </TR>
<%
}
%>          <TR class="qcell_row">
            <TD class="qcell_question" width="15%" align="right">&nbsp;</TD>
            <TD class="qcell_question" width="85%" align="left">&nbsp;</TD>
          </TR>
<%
for (int i=0; i < DisplayBean.getDisplayTextLineCount(); i++)
{
%>          <TR class="qcell_row">
            <TD class="qcell_question" colspan="2">&nbsp;<BR><%= DisplayBean.getDisplayText(i) %></TD>
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
<%
if (DisplayBean.isRollupOkay())
{
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_continue()">Continue</A></TD>
<%
}
%>          <TD class="pagebar" nowrap="nowrap" width="100%"></TD>
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
