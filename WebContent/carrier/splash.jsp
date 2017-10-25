<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">

<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AdminMenuDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--

<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
//	return true;
}

function allow_action()
{
	return true;
}
// -->
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<TABLE width="100%" align="center" border="0" cellpadding="0" cellspacing="0" mm:layoutgroup="true" bgcolor="#FFFFFF">
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
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">

<!-- BEGIN BODY -->

<!--     <CENTER>&nbsp;<BR><img src="<c:url value="/images/TEAMUP_Download.jpg"/>"></CENTER>
    <CENTER>&nbsp;<BR>&nbsp;<BR><img src="<c:url value="images/TEAMUP_NN_AC_Download.jpg"/>"></CENTER>
-->
	<center>&nbsp;<BR>&nbsp;<font color="RED">
		<c:if test="${not empty errorMessage}">
			*<c:out value="${errorMessage}"/>
		</c:if></font>
		<c:remove var="errorMessage" scope="session" />
		<c:remove var="agentId" scope="session" />
		<c:remove var="agentinfo" scope="session" />
	</center>
    <CENTER>&nbsp;<BR>&nbsp;<BR>&nbsp;<BR><FONT size="+3"><b>TEAM-UP Administration</b></FONT><BR>
      <b>Manage your agents and download process !!!!</b></CENTER>
    <CENTER>&nbsp;<BR>&nbsp;<BR><img src="<c:url value="images/NN_TEAM-UP_Download_184x187_72res.jpg"/>"></CENTER>
    <%-- <OBJECT id="ucContinue" style="LEFT: 0px; TOP: 0px" codeBase="controls/<%= DisplayBean.getBaseControlCodebase() %>" classid="<%= DisplayBean.getBaseControlClassid() %>" VIEWASTEXT>
      <PARAM NAME="_ExtentX" VALUE="2355">
      <PARAM NAME="_ExtentY" VALUE="661">
      <PARAM NAME="URL_LOC" VALUE="">
    </OBJECT> --%>

<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  >
        <TR>
          <TD class="pagebar" nowrap="nowrap"><% if (!DisplayBean.getStatusMessage().equals("")) {%> <img src="<c:url value="images/minilogo.gif"/>"> <%}%><%= DisplayBean.getStatusMessage() %></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
</TABLE>
<%@ include file="footer.jsp" %>
</BODY>
</HTML>
