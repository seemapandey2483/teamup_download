<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.LoginErrorDisplayBean" />
<TITLE><%= DisplayBean.getCarrierName() %> - TEAM-UP Download Agency Administration</TITLE>
<STYLE type="text/css">
<!--
H1{
  text-align : left;
}
P{
  text-align : center;
  font-size : 12pt;
}
H2{
  color : #f77c1a;
  position : absolute;
  top : 480px;
  left : 360px;
}
-->
</STYLE>
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
          <TD class="actionbar" nowrap="nowrap">&nbsp;</TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap">&nbsp;</TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">

<!-- BEGIN BODY -->

      <H1>TEAM-UP Download Agency Administration</H1>
      <P>&nbsp; <BR>&nbsp; <BR>&nbsp; <BR>&nbsp; <BR>&nbsp; <BR>&nbsp; <BR>Authorization failed accessing the TEAM-UP Download system. Please 
         <A class="navlink" href="mailto:<%= DisplayBean.getCarrierEmail() %>">contact your 
         underwriter</A> for more information, or to sign up for this feature.</P>

<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('startup')">Try Again</A></TD>
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
