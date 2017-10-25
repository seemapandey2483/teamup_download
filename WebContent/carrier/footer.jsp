<%@page import="java.util.Calendar"%>
<div class="copyright">
	<div class="copyright-left">
		<P>© 2001 - <%= Calendar.getInstance().get(Calendar.YEAR) %> <a href="http://www.ebix.com/" target=_blank>Ebix, Inc.</a> | All Rights Reserved |</P>
		<%
		long nofoDays =0;	
		if(session.getAttribute("daysUntilExpiration")!= null){
		 	nofoDays = (Long)session.getAttribute("daysUntilExpiration");
			if(nofoDays<30 && nofoDays>0){
			%>
		<P><font  color="red">License for Claim will expire in  - <%= session.getAttribute("daysUntilExpiration") %> days, please contact Ebix</font></P>
		<%} else if(nofoDays<0){%> 
			<P><font  color="red">License for Claim already expired  , please contact Ebix</font></P>
			
		<%}
			}%>
	</div>
	<div class="copyright-right">
		<ul>
			<li><a href="https://twitter.com/ebixinc" class="twitter" target=_blank></a></li>
			<li><a href="https://www.facebook.com/Ebix1" class="twitter facebook" target=_blank></a></li>
			<li><a href="https://plus.google.com/104287178415099123564/posts" class="twitter chrome" target=_blank></a></li>
			<li><a href="https://www.linkedin.com/company/ebix" class="twitter linkedin" target=_blank></a></li>
		</ul>
	</div>
</div>
	
