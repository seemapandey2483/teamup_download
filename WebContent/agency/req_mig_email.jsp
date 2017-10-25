<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.SimpleAgencyDisplayBean" />
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
          <TD class="actionbar" nowrap="nowrap"><jsp:include page="actionbar.jsp" flush="true" /></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap"><jsp:include page="menu.jsp" flush="true" /></TD>
  </TR>
  <TR>
    <TD class="blankbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">

<!-- BEGIN BODY -->

    <H1>&nbsp;<BR>&nbsp;</H1>
<%
if (DisplayBean.getCarrierInfo().isClientAppUsed())
{
%>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
function req_mig_email()
{
	alert("An email with instructions for upgrading will be sent to: <%= DisplayBean.getAgentEmail() %>");
	do_page_action('request_migration_email');
}
// -->
</SCRIPT>

    <CENTER><a href="#" onclick="javascript:req_mig_email()"><img src="<c:url value="/images/Upgrade2v3.gif"/>"></a></CENTER>
<%
}
else
{
%>
    &nbsp;<BR>&nbsp;<BR>
    <i><%= DisplayBean.getCarrierInfo().getName() %> is not currently rolling out the newest version of TEAM-UP Download.<BR>
       Please check back in a few weeks.</i>
<%
}
%>
    <OBJECT id="ucContinue" style="LEFT: 0px; TOP: 0px" codeBase="controls/<%= DisplayBean.getBaseControlCodebase() %>" classid="<%= DisplayBean.getBaseControlClassid() %>" VIEWASTEXT>
      <PARAM NAME="_ExtentX" VALUE="2355">
      <PARAM NAME="_ExtentY" VALUE="661">
      <PARAM NAME="URL_LOC" VALUE="">
    </OBJECT>

<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"></TD>
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
