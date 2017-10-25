<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.WelcomeDisplayBean" />
<TITLE><%= DisplayBean.getCarrierInfo().getName() %> - TEAM-UP Download Agency Administration</TITLE>
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
          <TD class="actionbar" nowrap="nowrap"><jsp:include page="actionbar_nohome.jsp" flush="true" /></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR class="menu">
    <TD class="menubar" valign="middle" nowrap><a href="javascript:void(0);" onClick="javascript:open_prn_win('agency/print_complete.jsp')" class="menu">Print Instructions</a></TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="welcomenext">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">

<!-- BEGIN BODY -->

      <CENTER><FONT size="+2"><B>TEAM-UP Download Registration</B></FONT><BR>
      <B>for</B><BR>
      <FONT size="+2"><B><%= DisplayBean.getCarrierInfo().getName() %></B></FONT></CENTER>
      <H4>Welcome, <%= DisplayBean.getAgentName() %>!</H4>
      
      <%= DisplayBean.getPageText() %>
      
      <CENTER>
      <table>
      <tr>
      <td align="center" valign="top"><input type="button" value="Print Instructions" onClick="javascript:open_prn_win('agency/print.jsp')"></td>
      <td align="center" valign="top">
        <OBJECT id="ucContinue" style="LEFT: 0px; TOP: 0px" codeBase="controls/<%= DisplayBean.getCodebase() %>" classid="<%= DisplayBean.getClassid() %>" VIEWASTEXT>
          <PARAM NAME="_ExtentX" VALUE="2355">
          <PARAM NAME="_ExtentY" VALUE="661">
          <PARAM NAME="URL_LOC" VALUE="<%= DisplayBean.getForwardUrl() %>">
        </OBJECT>
      </td>
      </tr>
      </table>  
      </CENTER>
      
<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
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
