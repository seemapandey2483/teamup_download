<%@page import="java.util.PropertyResourceBundle"%>
<jsp:useBean id="MenuBean" scope="request" type="connective.teamup.download.beans.MenuBean" />
<table class="menuH">
<tr class="menuH">
<% for (int i=0; i < MenuBean.getMenuCount(); i++)
	{
%>	
	<td class="menu" name="td<%= MenuBean.getMenuText(i) %>" onmouseover="showmenu('<%= MenuBean.getMenuText(i) %>')" onmouseout="hidemenu('<%= MenuBean.getMenuText(i) %>')" nowrap><a class="menu" href="javascript:void(0);"><%= MenuBean.getMenuText(i) %></a>
<br>
    	<table class="submenu" id="<%= MenuBean.getMenuText(i) %>">
        <tr class="submenu">
			<td class="submenu" nowrap>
<%
			PropertyResourceBundle props = new PropertyResourceBundle(getClass().getResourceAsStream("/teamworkdl.properties"));
			String helpURL = props.getString("download.connective.support");
			for (int j=0; j < MenuBean.getMenuItemCount(i); j++)
			{
				String actionStr = MenuBean.getMenuItemAction(i, j);
				
				if (actionStr.length() > 7 && actionStr.substring(0, 7).equalsIgnoreCase("mailto:"))
				{
%>				<a class="submenu" href="<%= actionStr %>" nowrap><%= MenuBean.getMenuItemText(i, j) %></a>
				<br>
<%
				}
				else if (actionStr.length() > 5 && actionStr.equalsIgnoreCase("link:carrier"))
				{
					actionStr = helpURL + "/help?type=carrier"; 
%>				<a class="submenu" href="javascript:open_child('<%= actionStr %>');" nowrap><%= MenuBean.getMenuItemText(i, j) %></a>
				<br>
<%
				}
				else if (actionStr.length() > 5 && actionStr.equalsIgnoreCase("link:agency"))
				{
					actionStr = helpURL + "/help?type=agency"; 
%>				<a class="submenu" href="javascript:open_child('<%= actionStr %>');" nowrap><%= MenuBean.getMenuItemText(i, j) %></a>
				<br>
<%
				}
				else
				{
%>				<a class="submenu" href="javascript:do_menu('<%= MenuBean.getMenuItemAction(i, j) %>');" nowrap><%= MenuBean.getMenuItemText(i, j) %></a>
				<br>
<%
				}
			}
%>
	    	</td>
	    </tr>
		</table>
	</td>
<%
	}
%>	
</tr>
</table>