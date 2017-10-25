<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<HTML>
<HEAD>
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.ClientReportDisplayBean" />
<TITLE>TEAM-UP Download Reports</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
function do_load()
{
	document.tudlform.submit();
	return true;
}
// -->
</SCRIPT>
</HEAD>
<BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="return do_load();">
<FORM name="tudlform" method="POST" action="<c:url value="/agency"/>">
  <INPUT type="hidden" name="action" value="<%= DisplayBean.getAction() %>">
<%
for (int i=0; i < DisplayBean.getParamCount(); i++)
{
%>  <INPUT type="hidden" name="<%= DisplayBean.getParamName(i) %>" value="<%= DisplayBean.getParamValue(i) %>">
<%
}
%>
</FORM>
</BODY>
</HTML>