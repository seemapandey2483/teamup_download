<%@page import="connective.teamup.download.beans.KeyValueBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.CompanyConfigDisplayBean" />
<TITLE>TEAM-UP Download <% if (DisplayBean.isConfigWizard()) { %>Configuration<% } else { %>Carrier Administration<% } %></TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--

<jsp:include page="javascript.jsp" flush="true" />

/*function do_load()
{
	document.tudlform.dlhour.value = 6;
	document.tudlform.dlminute.value = 0;
	document.tudlform.dlampm.value = 0;
	
	
	return true;
}*/

function set_fields_disabled(dsable)
{
	document.tudlform.flag_enable.disabled = dsable;
	document.tudlform.dlhour.disabled = dsable;
	document.tudlform.dlminute.disabled = dsable;
	document.tudlform.dlampm.disabled = dsable;
	
}

function do_enabled_yes_clicked()
{
	throw_change_flag();
	set_fields_disabled(false);
}

function do_enabled_no_clicked()
{
	throw_change_flag();
	set_fields_disabled(true);
}

function allow_action()
{ <%
String btnName = "Save";
if (DisplayBean.isConfigWizard())
	btnName = "Continue";
%>

	if (document.tudlform.data_changed.value == "Y")
	{
		msg = "You are about to lose any changes you have made on this page.  To save your changes, click 'Cancel' and then use the '<%= btnName %>' button at the bottom of the screen.";
		if (!confirm(msg))
			return false;
	}
	
	return true;
}

<%
if (DisplayBean.isConfigWizard())
{
%>function do_back()
{
	if (allow_action() == true)
	{
		do_page_action("config5_back");
	}
}
<%
}
%>

function do_save()
{
	if (document.tudlform.flag_enable.checked == true)
	{
		if (!document.tudlform.dlhour.value || !document.tudlform.dlminute.value || !document.tudlform.dlampm)
		{
			alert("Please enter the download time.");
			document.tudlform.dlhour.focus();
		}
	
		
		else
		{
			// create the scheduled task
			// calculate the download hour
			var hour = 0;
			hour = parseInt(document.tudlform.dlhour.value) + parseInt(document.tudlform.dlampm.value);
			//document.tudlform.schedulerConfig.TaskName = "Connective TEAM-UP Download Alert";
			<%--document.tudlform.schedulerConfig.DownloadURL = "<%= DisplayBean.getDlAlertLink() %>";--%>
			//document.tudlform.schedulerConfig.StartHour = hour;
			//document.tudlform.schedulerConfig.StartMinute = document.tudlform.dlminute.value;
			//document.tudlform.schedulerConfig.TimeoutMinutes = 60;
			//document.tudlform.schedulerConfig.Username = document.tudlform.username.value;
			//document.tudlform.schedulerConfig.Password = document.tudlform.password.value;
			//document.tudlform.schedulerConfig.createScheduledTask();
			
				do_page_action("dlreport_save");		
		
		}
	}
	else
	{
		// delete the scheduled task
		//document.tudlform.schedulerConfig.TaskName = "Connective TEAM-UP Download Alert";
		//document.tudlform.schedulerConfig.deleteScheduledTask();
	
		do_page_action("dlreport_save");
	}
}

function do_cancel()
{
	do_page_action("home");
}

function throw_change_flag()
{
	document.tudlform.data_changed.value = "Y";
}

// -->
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" mm:layoutgroup="true" bgcolor="#FFFFFF">
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="actionlogo"><jsp:include page="action_logo.jsp" flush="true" /></TD>
          <TD class="actionbar" nowrap="nowrap"><% if (!DisplayBean.isConfigWizard()) { %><jsp:include page="actionbar.jsp" flush="true" /><% } else { %>&nbsp;<% } %></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap"><jsp:include page="menu.jsp" flush="true" /></TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="${requestScope.DisplayBean.servletPath}" />" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="data_changed" value="N">
    <INPUT type="hidden" name="config_wizard" value="<%= DisplayBean.getConfigWizardFlag() %>">
    <input type="hidden" name ="downloadURL" value="<%= DisplayBean.getDlAlertLink() %>">

<!-- BEGIN BODY -->

      <H1>TEAM-UP Download Configuration<% if (DisplayBean.isConfigWizard()) { %> Wizard &nbsp;(Page 5 of 5)<% } %><BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="3"><B>Download Report! Settings</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_question" colspan="2">
              <TABLE class="qcell_table">
                <TBODY>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question" colspan="2">Download  Report! sends  import/download activity and sends email.</TD>
                  </TR>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question" colspan="2">&nbsp;</TD>
                  </TR>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Enable Download Report!</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" id="flag_enable" name="flag_enable" value="Y" <% if (DisplayBean.getDownloadReptCarrierFlag()) { %>checked <% } %>onclick="do_enabled_yes_clicked()"> Yes &nbsp; &nbsp;<INPUT type="radio" id="flag_enable" name="flag_enable" value="N" <% if (!DisplayBean.getDownloadReptCarrierFlag()) { %>checked <% } %>onclick="do_enabled_no_clicked()"> No</TD>
                  </TR>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Run Download Report! each day at:</A></TD>
                    <TD class="qcell_question" nowrap="nowrap">
		            	<select name="dlhour">
		            		<option value="">&nbsp;</option>
		            		<%
		            		String selected = "";
		            		for (int i=1;i<=12;i++) {
		            			selected = "";
		            			if( DisplayBean.getDownloadReptSchedHour()!= null &&  DisplayBean.getDownloadReptSchedHour().equals(String.valueOf(i))) {
		            				selected ="selected";
		            			}
		            		%>
		            		<option value="<%=i %>" <%=selected %>><%=i %></option>
		            		<%} %>
		            	</select>
		            	<select name="dlminute">
		            		<option value="">&nbsp;</option>
		            		<%
		            		selected = "";
		            		for (int i=0;i<=55;i+=5) {
		            			selected = "";
		            			String zeroValue ="00";

		            			if(i>0)
		            				zeroValue =String.valueOf(i);
		            			if(DisplayBean.getDownloadReptSchedMinute()!= null && DisplayBean.getDownloadReptSchedMinute().equals(String.valueOf(i))) {
		            				selected ="selected";
		            			}
		            		%>
		            		<option value="<%=i %>" <%=selected %>><%=zeroValue %></option>
		            		<%} %>
		            	</select>
		            	<select name="dlampm">
		            		<option value="">&nbsp;</option>
		            		<%
		             		if(DisplayBean.getDownloadReptSchedAmpM()!= null && DisplayBean.getDownloadReptSchedAmpM().equals("0")) {
	            				%>
	            			<option value="0" selected>AM</option>	
	            				<%
	            			} else{
		            		%>
		            		<option value="0" >AM</option>
		            		<%} %>
		            		<%
		            		if(DisplayBean.getDownloadReptSchedAmpM()!= null && DisplayBean.getDownloadReptSchedAmpM().equals("12")) {
	            				%>
	            			<option value="12" selected>PM</option>	
	            				<%
	            			} else{
		            		%>
		            		<option value="12" >PM</option>
		            		<%} %>
		            	</select>
                    </TD>
                  </TR>
 		<TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Include Activity Type:</A></TD>
                    <TD class="qcell_question" nowrap="nowrap">
		            	<select name="dlActivityType" id="dlActivityType">
		            		<%
		            		 selected = "";
		            		for (KeyValueBean val:DisplayBean.getActList()) {
		            			selected = "";
		            			if( val.getKey()!= null &&  val.getKey().equals(DisplayBean.getDownloadReptActType())) {
		            				selected ="selected";
		            			}
		            		%>
		            		<option value="<%=val.getKey() %>" <%=selected %>><%=val.getValue() %></option>
		            		<%} %>
		            	</select>
		            	
                    </TD>
                  </TR>
                  	<TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Include File Type:</A></TD>
                    <TD class="qcell_question" nowrap="nowrap">
		            	<select name="dlFileType" id="dlFileType">
		            		<%
		            		 selected = "";
		            		for (KeyValueBean val:DisplayBean.getFileList()) {
		            			selected = "";
		            			if( val.getKey()!= null &&  val.getKey().equals(DisplayBean.getDownloadReptFileType())) {
		            				selected ="selected";
		            			}
		            		%>
		            		<option value="<%=val.getKey() %>" <%=selected %>><%=val.getValue() %></option>
		            		<%} %>
		            	</select>
		            	
                    </TD>
                  </TR>
                  	<TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Send Attachment as:</A></TD>
                    <TD class="qcell_question" nowrap="nowrap">
		            	<select name="dlAttachType" id="dlAttachType">
		            		<%
		            		 selected = "";
		            		for (KeyValueBean val:DisplayBean.getFileTypeList()) {
		            			selected = "";
		            			if( val.getKey()!= null &&  val.getKey().equals(DisplayBean.getDownloadReptAttachType())) {
		            				selected ="selected";
		            			}
		            		%>
		            		<option value="<%=val.getKey() %>" <%=selected %>><%=val.getValue() %></option>
		            		<%} %>
		            	</select>
		            	
                    </TD>
                  </TR>
                  <TR class="qcell_row" valign="top">
                    <TD class="qcell_question"><A title="">Send Detailed Transaction Report!</A></TD>
                    <TD class="qcell_question" nowrap="nowrap"><INPUT type="radio" id="flag_sendDetail" name="flag_sendDetail" value="Y" <% if (DisplayBean.getSendDetailReptFlag()) { %>checked <% } %>onclick="do_enabled_yes_clicked()"> Yes &nbsp; &nbsp;<INPUT type="radio" id="flag_sendDetail" name="flag_sendDetail" value="N" <% if (!DisplayBean.getSendDetailReptFlag()) { %>checked <% } %>onclick="do_enabled_no_clicked()"> No</TD>
                  </TR>
                </TBODY>
              </TABLE>
            </TD>
          </TR>
      
       
        </TBODY>
      </TABLE>

<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
<%
if (DisplayBean.isConfigWizard())
{
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_back()">Back</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Continue</A></TD>
<%
}
else
{
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_cancel()">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Save</A></TD>
<%
}
%>
          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
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
