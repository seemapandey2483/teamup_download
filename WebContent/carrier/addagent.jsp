<%@page import="connective.teamup.download.db.DatabaseFactory"%>
<%@page import="connective.teamup.download.db.DatabaseOperation"%>
<%@page import="connective.teamup.download.db.AmsOptionBean"%>
<%@page import="connective.teamup.download.dao.StatesDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="connective.teamup.download.db.StateOptionBean"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<LINK rel="stylesheet" href="<c:url value="/css/custom.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AddAgentDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<%

List<StateOptionBean> lstCustomer = new ArrayList<StateOptionBean>();
lstCustomer = StatesDao.getStateList();
request.setAttribute("stateList", lstCustomer);

List<AmsOptionBean> lstCustomer1=new ArrayList<AmsOptionBean>();
DatabaseOperation op = null;
op = DatabaseFactory.getInstance().startOperation();
lstCustomer1=op.geAllAmsBeanList();
request.setAttribute("amsList", lstCustomer1);

%>


<SCRIPT language="JavaScript" type="text/javascript">

function do_load()
{
	document.tudlform.rollfrom.focus();
	return true;
}

function allow_action()
{
	return true;
}

<jsp:include page="javascript.jsp" flush="true" />

function do_cancel()
{
	do_page_action("dist_cancel");
}

function do_continue()
{
	if (!document.tudlform.rollfrom.value)
	{
		alert("Please enter the trading partner ID you wish to move.");
		document.tudlform.rollfrom.focus();
	}
	else if (!document.tudlform.rollinto.value)
	{
		alert("Please enter the primary trading partner ID into which this ID will be rolled.");
		document.tudlform.rollinto.focus();
	}
	else
	{
		do_page_action("tp_rollup_summary");
	}
}

</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="return do_load();">
<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" mm:layoutgroup="true" bgcolor="#FFFFFF">
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
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/company?action=tpmaint_save&newAgent=Y&actionType=newJSON"/>">
    <INPUT type="hidden" name="action" value="">
   <div id="wrapper">
         
            <fieldset>
                <legend>New Agent</legend>
                <div>
                	<div class="small">Agency Id</div>
                    <input type="text" name="agentId" placeholder="Agency Id"/>
                </div>
                <div>
                <div class="small">Agency Name</div>
                    <input type="text" name="name" placeholder="Agency Name"/>
                </div>
                <!-- <div>
                      <select>
        <option value="">Agency Location</option>
        <option value="">Palastine</option>
        <option value="">Syria</option>
        <option value="">Italy</option>
      </select>


                </div> -->
                <div>
                    <!-- <select>
       					<option value="">Agency State</option>
        				<option value="">Palastine</option>
        				<option value="">Syria</option>
        				<option value="">Italy</option>
      				</select> -->
      				<div class="small">Agency Location</div>
      				<select id="agencyLocation" name="stateList">
      				<%
      					for(int i = 0; i < lstCustomer.size(); i++){
      						StateOptionBean s = (StateOptionBean)lstCustomer.get(i);
      				%>
      				<option value="<%=s.getValue()%>"><%=s.getDisplayText()%></option>
<%}%> 
      				
                <%-- <c:forEach items="${stateList}" var="stateList">
                    <option value="${stateList.key}">${stateList.value}</option>
                </c:forEach> --%>
            </select>
                </div>
                <div>
                <div class="small">Contact Name</div>
                    <input type="text" name="contactName" placeholder="Contact Name"/>
                </div>
                <div>
                	<div class="small">Contact Email</div>
                    <input type="text" name="contactEmail" placeholder="Contact Email"/>
                </div>
                <div>
                	<div class="small">Contact Phone</div>
                    <input type="text" name="contactPhone" placeholder="Contact Phone"/>
                </div>
                <div>
                	<div class="small">Agent City</div>
                    <input type="text" name="city" placeholder="Agent City"/>
                </div>
                <div>
                    <!-- <select>
       					<option value="">Agency State</option>
        				<option value="">Palastine</option>
        				<option value="">Syria</option>
        				<option value="">Italy</option>
      				</select> -->
      				<div class="small">Agent State</div>
      				<select id="agentState" name="stateList">
      				<%
      					for(int i = 0; i < lstCustomer.size(); i++){
      						StateOptionBean s = (StateOptionBean)lstCustomer.get(i);
      				%>
      				<option value="<%=s.getValue()%>"><%=s.getDisplayText()%></option>
<%}%> 
      				
                <%-- <c:forEach items="${stateList}" var="stateList">
                    <option value="${stateList.key}">${stateList.value}</option>
                </c:forEach> --%>
            </select>
                </div>
                <div>
                	<div class="small">Postal Code</div>
                    <input type="text" name="zip" placeholder="Postal Code"/>
                </div>
                <div>
                	<div class="small">Agency Management System</div>
                    <!-- <select>
        <option value="">Agency Management System</option>
        <option value="">Palastine</option>
        <option value="">Syria</option>
        <option value="">Italy</option> -->
        <select id="amsId" name="stateList">
      				<%
      					for(int i = 0; i < lstCustomer1.size(); i++){
      						AmsOptionBean ams = (AmsOptionBean)lstCustomer1.get(i);
      				%>
      				<option value="<%=ams.getValue()%>"><%=ams.getDisplayText()%></option>
<%}%>
        
      </select>
                </div>
                <div>
                	<div class="small">Agent Destination Address</div>
                    <input type="text" name="destAddress" placeholder="Agent Destination Address"/>
                </div>
                <div>
                	<div class="small">Download to Directory</div>
                    <input type="text" name="email" placeholder="Download to Directory"/>
                </div>
                <div align="left">
                	<div class="small">Use Default</div>
                    <input type="checkbox" name="chkDefault"/>
                </div>
                <div align="left">
                	<div class="small">Active</div>
                    <input type="checkbox" name="chkDefault"/>Active
                </div>
                <div align="left">
                	<div class="small">Test Agent</div>
                    <input type="checkbox" name="testAget"/>No
                </div>
               
                <div>
                	<div class="small">Initial Password</div>
                    <input type="text" name="password" placeholder="Initial Password"/>
                </div>
                <div align="left">
                	<div class="small">Send registration email to new trading partner</div>
                    <input type="checkbox" name="resendRegEmail"/>
                </div>
                <!-- div>
                    <div class="small">this textarea is just for test so you can see the placeholder working on textarea as well</div>
                    <textarea name="message" placeholder="Message"></textarea>
                </div-->    
                <input type="submit" name="submit" value="Submit"/>
            </fieldset>  
        
    </div>
    </form>
    </TD>
  </TR>
  <TR>
    <TD>
      <!-- TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_cancel()">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_continue()">Continue</A></TD>
          <TD class="pagebar" nowrap="nowrap" width="100%"></TD>
        </TR>
      </TABLE-->
    </TD>
  </TR>
  <%-- <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR> --%>
</TABLE>
<%@ include file="footer.jsp" %>
</BODY>
</HTML>
