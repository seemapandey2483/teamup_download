<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AppInfoDisplayBean" />
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

<!-- BEGIN BODY -->

    <H1>&nbsp;<BR>TEAM-UP Download</H1>
    Implemented for <B><%= DisplayBean.getCarrierName() %></B> in <%= DisplayBean.getCarrierInfo().getImplementationYear() %>.<BR>
    &nbsp;<BR>
    Ebix, Inc.<BR>
    <a href="http://www.ebix.com/">http://www.ebix.com</a><BR>
    <a href="mailto:<%= DisplayBean.getTechSupportEmail() %>?subject=<%= DisplayBean.getCarrierShortName() %> Download"><%= DisplayBean.getTechSupportEmail() %></a><BR>
    &nbsp;<BR>&nbsp;<BR>
    <TABLE>
      <TR>
        <TD>Application version:&nbsp;</TD>
        <TD><%= DisplayBean.getAppVersion() %></TD>
      </TR>
      <TR>
        <TD>Database version:&nbsp;</TD>
        <TD><%= DisplayBean.getDbVersion() %></TD>
      </TR>
      <TR>
        <TD>&nbsp;</TD>
        <TD>&nbsp;</TD>
      </TR>
      <TR>
        <TD>Version deployed:&nbsp;</TD>
        <TD><%= DisplayBean.getAppDeployDate() %></TD>
      </TR>
      <TR>
        <TD>Application started:&nbsp;</TD>
        <TD><%= DisplayBean.getAppStartDate() %></TD>
      </TR>
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
