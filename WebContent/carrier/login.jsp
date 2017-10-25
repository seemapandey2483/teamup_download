<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<LINK rel="stylesheet" href="<c:url value="/css/common.css"/>" type="text/css">
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
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
  position : absolute;
  top : 450px;
}
-->
</STYLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
function do_load()
{
	document.tudlform.j_username.focus();
	return true;
}

function do_bookmark()
{
	bookmarkurl= location.protocol + "//" + location.hostname + ":" + location.port + "<c:url value="/company"/>";
	bookmarktitle="TEAM-UP Download Administration";
	if (document.all)
		window.external.AddFavorite(bookmarkurl,bookmarktitle);
}

function allow_action()
{
	return true;
}
// -->
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="return do_load();">
 <FORM name="tudlform" method="POST" action="j_security_check">
    <!-- <H1>TEAM-UP Download Company Administration</H1> -->
    <div class="actionlogo">
			<jsp:include page="action_logo.jsp" flush="true" />
		</div>
	<BR><BR><BR><BR><BR><BR><BR>
		<div class="main">
		<div class="login-form">
			<h1>TEAM-UP Login</h1>
					<div class="head">
						<img src="images/user.png" alt=""/>
					</div>
						<input type="text" class="text" name="j_username" placeholder="User ID" maxlength="15" />
						<input type="password" name="j_password" placeholder="Password" maxlength="15"/>
						<div class="submit">
							<input type="submit" value="LOGIN" >
					</div>	
			</div>
			<!--//End-login-form-->
		</div>
		</FORM>
<!-- <TABLE width="100%" border="0" cellpadding="0" cellspacing="0" mm:layoutgroup="true" bgcolor="#FFFFFF">
  <TR>
    <TD>
      TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="actionlogo"><img src="<c:url value="/images/team_up_logo_resized.png"/>"></TD>
          <TD class="actionbar" nowrap="nowrap">&nbsp;</TD>
        </TR>
      </TABLE
    </TD>
  </TR>
  
  <TR>
    <TD>
    <FORM name="tudlform" method="POST" action="j_security_check">
    <H1>TEAM-UP Download Company Administration</H1>
	<BR><BR><BR>	
		<div class="main">
		<div class="login-form">
			<h1>TEAM-UP Login</h1>
					<div class="head">
						<img src="images/user.png" alt=""/>
					</div>
						<input type="text" class="text" name="j_username" placeholder="User ID" maxlength="15" />
						<input type="password" name="j_password" placeholder="Password" maxlength="15"/>
						<div class="submit">
							<input type="submit" value="LOGIN" >
					</div>	
			</div>
			//End-login-form
		</div>
		
BEGIN BODY

      <H1>TEAM-UP Download Company Administration</H1>
      <P>&nbsp; <BR>&nbsp; <BR>&nbsp; <BR>&nbsp; <BR><B>Welcome!</B></P>
      <CENTER>
      <TABLE cellpadding="4">
        <TBODY>
          <TR>
            <TD>User ID:</TD>
            <TD><INPUT size="15" type="text" name="j_username" maxlength="15"></TD>
          </TR>
          <TR>
            <TD>Password:</TD>
            <TD><INPUT size="15" type="password" name="j_password" maxlength="15" onpaste="return false;"></TD>
          </TR>
          <TR>
            <TD></TD>
            <TD>&nbsp;<BR>
            <INPUT type="submit" name="login" value="Log In"></TD>
          </TR>
          <TR>
            <TD align="center" colspan="2">&nbsp;</TD>
          </TR>
          <TR>
            <TD align="center" colspan="2"><A class="navlink" href="javascript:do_bookmark()">Add this page to your Favorites</A></TD>
          </TR>
         </TBODY>
      </TABLE>
      </CENTER>
      <CENTER>&nbsp;<BR>
		<BR><I><B></B></I>
      </CENTER>

END BODY

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>&nbsp;</TD>
  </TR>
</TABLE> -->
<%@ include file="footer.jsp" %>
</BODY>
</HTML>
