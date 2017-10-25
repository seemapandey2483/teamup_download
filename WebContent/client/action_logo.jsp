<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<jsp:useBean id="MenuBean" scope="request" type="connective.teamup.download.beans.MenuBean" />
<img src="<c:url value="${requestScope.MenuBean.bannerGraphic}" />"><img src="<c:url value="/images/small_grad.jpg"/>" width="100" height="<%= MenuBean.getCarrierInfo().getBannerGraphicHeight() %>">