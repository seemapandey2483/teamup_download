<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.ImportDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
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
          <TD class="actionbar" nowrap="nowrap"><% if (DisplayBean.getInteractiveFlag().equals("Y")) { %><jsp:include page="actionbar.jsp" flush="true" /><% } else { %>&nbsp;<% } %></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap"><% if (DisplayBean.getInteractiveFlag().equals("Y")) { %><jsp:include page="menu.jsp" flush="true" /><% } else { %>&nbsp;<% } %></TD>
  </TR>
  <TR>
    <TD class="blankbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">

<!-- BEGIN BODY -->

    <H2>Select Files for Import<BR>&nbsp;</H2>
    <CENTER>
		<OBJECT id="ctlTEAMUP" codeBase="<%= DisplayBean.getCodebase() %>" classid="<%= DisplayBean.getClassid() %>">
			<PARAM NAME="_ExtentX" VALUE="14446">
			<PARAM NAME="_ExtentY" VALUE="6800">
			<PARAM NAME="FILE_LOC" VALUE="<%= DisplayBean.getFileSource() %>">
			<PARAM NAME="URL_LOC" VALUE="<%= DisplayBean.getImportUrl() %>">
		    <PARAM NAME="interactive" VALUE="<%= DisplayBean.getInteractiveFlag() %>">
		    <PARAM NAME="redirect" VALUE="<%= DisplayBean.getForwardUrl() %>">
		</OBJECT>
    </CENTER>
    <CENTER>&nbsp;<BR>&nbsp;<BR><A href="<c:url value="/impsetup.jsp"/>" target="_blank">Click here</A> if you do not see the control</CENTER>

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
