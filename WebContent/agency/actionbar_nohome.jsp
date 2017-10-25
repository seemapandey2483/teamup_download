<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<jsp:useBean id="MenuBean" scope="request" type="connective.teamup.download.beans.MenuBean" />
<table height="75" cellpadding="0" cellspacing="0" border="0">
<tr>
<%
if (MenuBean.getCarrierInfo().getContactEmail() != null)
{
%>	<td align="right" valign="top">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="mailto:<%= MenuBean.getCarrierInfo().getContactEmail() %>" class="nav">CONTACT US</a></td>
<%
}
%>	<td align="right" valign="top">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:do_action('log_off');" class="nav">LOG OFF</a></td>	
</tr>
</table>