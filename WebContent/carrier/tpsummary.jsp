<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.TradingPartnerSummaryDisplayBean" />
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
    <INPUT type="hidden" name="current_sort" value="<%= DisplayBean.getTpSortOrder() %>">

<!-- BEGIN BODY -->

      <H1>Trading Partner Summary<BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" width="5%">&nbsp;</TD>
            <TD class="qcell_question" width="40%" align="center">
              <TABLE class="qcell_table" cellpadding="1" cellspacing="0" border="1" rules="rows">
                <TBODY>
                  <TR class="qcell_row">
                    <TD class="qcell_question">&nbsp;</TD>
                    <TD class="qcell_question" align="right"><FONT size="-2">Primary&nbsp;&middot;</FONT></TD>
                    <TD class="qcell_question" align="right"><FONT size="-2">Participants&nbsp;&middot;</FONT></TD>
                    <TD class="qcell_question" align="right"><FONT size="-2"><B>Total</B></FONT>&nbsp;</TD>
                  </TR>
                  <TR class="qcell_row">
                    <TD class="qcell_question" nowrap="nowrap">&nbsp; Live Trading Partners:&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getLivePartners() %>&nbsp;&nbsp;&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getLiveParticipants() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getLivePartners() + DisplayBean.getLiveParticipants() %>&nbsp;</TD>
                  </TR>
                  <TR class="qcell_row"><% String regMsg = "Agents who have registered, but are not flagged as receiving 'live' downloads."; %>
                    <TD class="qcell_question" nowrap="nowrap">&nbsp; <a title="<%= regMsg %>">Registered Trading Partners:</a>&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getRegisteredPartners() %>&nbsp;&nbsp;&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getRegisteredParticipants() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getRegisteredPartners() + DisplayBean.getRegisteredParticipants() %>&nbsp;</TD>
                  </TR>
                  <TR class="qcell_row"><% String unrMsg = "Agents who have been entered into the TEAM-UP Download system, but have not yet completed the registration process."; %>
                    <TD class="qcell_question" nowrap="nowrap">&nbsp; <a title="<%= unrMsg %>">Unregistered Trading Partners:</a>&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getUnregisteredPartners() %>&nbsp;&nbsp;&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getUnregisteredParticipants() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getUnregisteredPartners() + DisplayBean.getUnregisteredParticipants() %>&nbsp;</TD>
                  </TR>
                  <TR class="qcell_row">
                    <TD class="qcell_question" nowrap="nowrap">&nbsp; Disabled Trading Partners:&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getDisabledPartners() %>&nbsp;&nbsp;&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getDisabledParticipants() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getDisabledPartners() + DisplayBean.getDisabledParticipants() %>&nbsp;</TD>
                  </TR>
                  <TR class="qcell_row">
                    <TD class="qcell_question"></TD>
                    <TD class="qcell_question" align="right"></TD>
                    <TD class="qcell_question" align="right"></TD>
                    <TD class="qcell_question" align="right"></TD>
                  </TR>
                  <TR class="qcell_row"><% String totMsg = "Totals do not include any trading partners defined as 'test' agents."; %>
                    <TD class="qcell_question" nowrap="nowrap">&nbsp; <B><a title="<%= totMsg %>">Total Trading Partners:</a></B>&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getTotalPartners() %>&nbsp;&nbsp;&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getTotalParticipants() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
                    <TD class="qcell_question" align="right"><B><%= DisplayBean.getTotalIDs() %></B>&nbsp;</TD>
                  </TR>
                </TBODY>
              </TABLE>
            </TD>
            <TD class="qcell_question" width="55%">&nbsp;</TD>
          </TR>
<%
if (DisplayBean.getVendorCount() > 0)
{
%>
          <TR>
            <TD colspan="3">&nbsp;<BR>&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question"></TD>
            <TD class="qcell_question" align="center">
              <TABLE class="qcell_table" cellpadding="1" cellspacing="0" border="1" rules="rows">
                <TBODY>
                  <TR class="qcell_row">
                    <TD class="qcell_question">&nbsp; &nbsp; &nbsp;<font size="-2"><b>Agency Vendor System</b></font></TD>
                    <TD class="qcell_question" align="right"><FONT size="-2">Primary&nbsp;&middot;</FONT></TD>
<%
  if (DisplayBean.isBrowserUsers() && DisplayBean.isClientUsers())
  {
%>                    <TD class="qcell_question" align="right"><FONT size="-2">Browser&nbsp;/</FONT></TD>
                    <TD class="qcell_question" align="left"><FONT size="-2">Client&nbsp;App&nbsp;&middot;</FONT></TD>
<%
  }
%>
                    <TD class="qcell_question" align="right"><FONT size="-2">Participants&nbsp;&middot;</FONT></TD>
                    <TD class="qcell_question" align="right"><FONT size="-2"><b>Total</b></FONT>&nbsp;</TD>
                  </TR>
                  <TR class="qcell_row">
                    <TD class="qcell_question"></TD>
                    <TD class="qcell_question" align="right"></TD>
                    <TD class="qcell_question" align="right"></TD>
                    <TD class="qcell_question" align="right"></TD>
<%
  if (DisplayBean.isBrowserUsers() && DisplayBean.isClientUsers())
  {
%>                    <TD class="qcell_question" align="right"></TD>
                    <TD class="qcell_question" align="right"></TD>
<%
  }
%>
                  </TR>
<%
  for (int i=0; i < DisplayBean.getVendorCount(); i++)
  {
%>
                  <TR class="qcell_row">
                    <TD class="qcell_question" nowrap="nowrap">&nbsp; <%= DisplayBean.getVendorName(i) %>&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getVendorAgentCount(i) %>&nbsp;&nbsp;&nbsp;</TD>
<%
    if (DisplayBean.isBrowserUsers() && DisplayBean.isClientUsers())
    {
%>                    <TD class="qcell_question" align="right"><%= DisplayBean.getVendorBrowserAppCount(i) %>&nbsp;/</TD>
                    <TD class="qcell_question" align="left"><%= DisplayBean.getVendorClientAppCount(i) %></TD>
<%
    }
%>                    <TD class="qcell_question" align="right"><%= DisplayBean.getVendorParticipantCount(i) %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
                    <TD class="qcell_question" align="right"><%= DisplayBean.getVendorTotalCount(i) %>&nbsp;&nbsp;</TD>
                  </TR>
<%
  }
%>
                </TBODY>
              </TABLE>
            </TD>
            <TD class="qcell_question">&nbsp;</TD>
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
<%--   <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR> --%>
</TABLE>
<%@ include file="footer.jsp" %>
</BODY>
</HTML>
