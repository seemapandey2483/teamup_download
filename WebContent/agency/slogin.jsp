<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.SimpleDisplayBean" />
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
<% if (!DisplayBean.getCarrierInfo().isAgentLoginDisabled())
   {
%>   
<SCRIPT language="JavaScript" type="text/javascript">
<!--

<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	document.tudlform.userid.focus();
	return true;
}

function allow_action()
{
	return true;
}

function do_bookmark()
{
	bookmarkurl= "<c:url value="/agency"/>";
	bookmarktitle="<%= DisplayBean.getCarrierName() %> Download";
	if (document.all)
		window.external.AddFavorite(bookmarkurl,bookmarktitle);
}

function do_login()
{
	if (!document.tudlform.userid.value)
	{
		alert("Access denied without valid userid and password");
		document.tudlform.userid.focus();
		return false;
	}
	else if (!document.tudlform.password.value)
	{
		alert("Access denied without valid userid and password");
		document.tudlform.password.focus();
		return false;
	}
	else
	{
		//document.tudlform.submit();
	}
	
	return true;
}
// -->
</SCRIPT>
<%
}
%>
</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="return do_load();">
<% if (!DisplayBean.getCarrierInfo().isAgentLoginDisabled())
   {
%>   
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
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return do_login();">
    <INPUT type="hidden" name="action" value="logon">

<!-- BEGIN BODY -->

      <CENTER><FONT size="+3"><B><%= DisplayBean.getCarrierName() %></B></FONT></CENTER>
      <CENTER><FONT size="+2">TEAM-UP Download Agency Administration</FONT></CENTER>
      <P>&nbsp; <BR>&nbsp; <BR><B>Welcome!</B></P>
      <CENTER>
      <TABLE cellpadding="4">
        <TBODY>
          <TR>
            <TD align="right">Agent Number:</TD>
            <TD><INPUT size="15" type="text" name="userid" maxlength="10"></TD>
          </TR>
          <TR>
            <TD align="right">Password:</TD>
            <TD><INPUT size="15" type="password" name="password" maxlength="10"></TD>
          </TR>
          <TR>
            <TD align="center" colspan="2">&nbsp;<BR>
            <INPUT type="submit" name="login" value="Log In"></TD>
          </TR>
          <TR>
            <TD align="center" colspan="2">&nbsp;</TD>
          </TR>
          <TR>
            <TD align="center" colspan="2"><A class="navlink" href="javascript:do_bookmark()">Add this page to your Favorites</A></TD>
          </TR>
          <TR>
            <TD align="center" colspan="2">&nbsp;<BR>
            <script>
				with (document)
				{
				  if (navigator.appName == "Microsoft Internet Explorer")
					write("<style>P.IE{visibility:hidden;}</style>");
				  else
					write("<style>P.IE{visibility:visible;}</style>");
				}
			</script>
			<P class="IE"><font size="2" color="red"><b><i>Please note that TEAM-UP is designed to run under Internet Explorer. It may not work correctly with your browser.</i></b></font></P>
			<script>
				version=0
				if (navigator.appVersion.indexOf("MSIE")!=-1)
				{
					temp=navigator.appVersion.split("MSIE");
					version=parseFloat(temp[1]);
				}
				with (document)
				{
					if (version >= 5.0)
						write("<style>P.version{visibility:hidden;}</style>");
					else
						write("<style>P.version{visibility:visible;}</style>");
				}
			</script>
			<P class="version"><font size="2" color="red"><b><i>Please note that TEAM-UP is designed to run under Internet Explorer 5.5 and higher. It may not work correctly with your browser.</i></b></font></P>
            </TD>
          </TR>
        </TBODY>
      </TABLE>
      </CENTER>

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
<%
}
%>
</BODY>
</HTML>
