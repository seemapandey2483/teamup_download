<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.LogParamDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

var labelAnchor;

function do_load()
{
	document.tudlform.start_mon.value = "<%= DisplayBean.getStartMonth() %>";
	document.tudlform.start_day.value = "<%= DisplayBean.getStartDay() %>";
	document.tudlform.start_yr.value = "<%= DisplayBean.getStartYear() %>";
	
	document.tudlform.end_mon.value = "<%= DisplayBean.getCurrentMonth() %>";
	document.tudlform.end_day.value = "<%= DisplayBean.getCurrentDay() %>";
	document.tudlform.end_yr.value = "<%= DisplayBean.getCurrentYear() %>";
	
<%
if (DisplayBean.getAgencyCount() > 0)
{
%>	document.tudlform.agy_select.focus();
	on_select_tp();
<%
}
else
{
%>	document.tudlform.search_str.focus();
<%
}
%>
	for (var i = 0; i < document.anchors.length; i++)
	{
		if (document.anchors[i].name == "search_label")
			labelAnchor = document.anchors[i];
	}
	
	return true;
}

function allow_action()
{
	return true;
}

function do_report()
{
	var st_dt = 0;
	var end_dt = 0;
	
	if (!document.tudlform.start_yr.value)
	{
		alert("Please select start year");
		document.tudlform.start_yr.focus();
		return;
	}
	else if (!document.tudlform.start_mon.value)
	{
		alert("Please select start month");
		document.tudlform.start_mon.focus();
		return;
	}
	else if (!document.tudlform.start_day.value)
	{
		alert("Please select start day");
		document.tudlform.start_day.focus();
		return;
	}		
	else if (isNaN(document.tudlform.start_day.value))
	{
		alert("Please enter a valid start day");
		document.tudlform.start_day.value = "";
		document.tudlform.start_day.focus();
		return;
	}			
	else if (!document.tudlform.end_yr.value)
	{
		alert("Please select end year");
		document.tudlform.end_yr.focus();
		return;
	}
	else if (!document.tudlform.end_mon.value)
	{
		alert("Please select end month");
		document.tudlform.end_mon.focus();
		return;
	}
	else if (!document.tudlform.end_day.value)
	{
		alert("Please enter an end day");
		document.tudlform.end_day.focus();
		return;
	}
	else if (isNaN(document.tudlform.end_day.value))
	{
		alert("Please enter a valid end day");
		document.tudlform.end_day.value = "";
		document.tudlform.end_day.focus();
		return;
	}			
	else
	{
		st_dt = document.tudlform.start_yr.value + document.tudlform.start_mon.value + document.tudlform.start_day.value;
		end_dt = document.tudlform.end_yr.value + document.tudlform.end_mon.value + document.tudlform.end_day.value;
		
		if (st_dt > end_dt)
			alert("ERROR: End date cannot be earlier than start date!");
		else if ((document.tudlform.start_mon.value == "01" || 
		          document.tudlform.start_mon.value == "03" || 
		          document.tudlform.start_mon.value == "05" || 
		          document.tudlform.start_mon.value == "07" || 
		          document.tudlform.start_mon.value == "08" || 
		          document.tudlform.start_mon.value == "10" || 
		          document.tudlform.start_mon.value == "12") && 
		          document.tudlform.start_day.value > 31)
		{
			alert("Error: Start day invalid!");
			document.tudlform.start_day.value = "";
			document.tudlform.start_day.focus();
		}	
		else if ((document.tudlform.start_mon.value == "04" || 
		          document.tudlform.start_mon.value == "06" || 
		          document.tudlform.start_mon.value == "09" || 
		          document.tudlform.start_mon.value == "11") && 
		          document.tudlform.start_day.value > 30)
		{
			alert("Error: Start day invalid!");
			document.tudlform.start_day.value = "";
			document.tudlform.start_day.focus();
		}	
		else if (document.tudlform.start_mon.value == "02" && 
		         document.tudlform.start_day.value > 28)
		{
			alert("Error: Start day invalid!");
			document.tudlform.start_day.value = "";
			document.tudlform.start_day.focus();
		}	

		else if ((document.tudlform.end_mon.value == "01" || 
		          document.tudlform.end_mon.value == "03" || 
		          document.tudlform.end_mon.value == "05" || 
		          document.tudlform.end_mon.value == "07" || 
		          document.tudlform.end_mon.value == "08" || 
		          document.tudlform.end_mon.value == "10" || 
		          document.tudlform.end_mon.value == "12") && 
		          document.tudlform.end_day.value > 31)
		{
			alert("Error: End day invalid!");
			document.tudlform.end_day.value = "";
			document.tudlform.end_day.focus();
		}	
		else if ((document.tudlform.end_mon.value == "04" || 
		          document.tudlform.end_mon.value == "06" || 
		          document.tudlform.end_mon.value == "09" || 
		          document.tudlform.end_mon.value == "11") && 
		          document.tudlform.end_day.value > 31)
		{
			alert("Error: End day invalid!");
			document.tudlform.end_day.value = "";
			document.tudlform.end_day.focus();
		}	
		else if (document.tudlform.end_mon.value == "02" && 
		         document.tudlform.end_day.value > 28)
		{
			alert("Error: End day invalid!");
			document.tudlform.end_day.value = "";
			document.tudlform.end_day.focus();
		}	
		else
		{
			document.tudlform.startdt.value = document.tudlform.start_yr.value + "-" + document.tudlform.start_mon.value + "-" + document.tudlform.start_day.value;
			document.tudlform.enddt.value = document.tudlform.end_yr.value + "-" + document.tudlform.end_mon.value + "-" + document.tudlform.end_day.value;
			do_page_action("report_agytrans");
		}	
	}
}

function on_select_method(strLabel)
{
	labelAnchor.setAttribute("innerText", strLabel);
}

<%
if (DisplayBean.getAgencyCount() > 0)
{
%>
function on_select_tp()
{
	document.tudlform.agentID.value = document.tudlform.agy_select.value;
}
<%
}
%>
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
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="">
    <INPUT type="hidden" name="src_page" value="agytrans_param">
    <INPUT type="hidden" name="orderby" value="LOG_DT">
    <INPUT type="hidden" name="startdt" value="">
    <INPUT type="hidden" name="enddt" value="">
    <INPUT type="hidden" name="current_sort" value="<%= DisplayBean.getTpSortOrder() %>">

<!-- BEGIN BODY -->

      <H1>Transaction Log<BR>&nbsp;<BR>&nbsp;<BR>&nbsp;</H1>
      <CENTER>
	  <table>
<%
if (DisplayBean.getErrorMsg() != null)
{
%>		<tr valign="top">
			<td colspan="2" align="center"><font color="red"><i><%= DisplayBean.getErrorMsg() %></i></font></td>
		</tr>
<%
}
if (DisplayBean.getAgencyCount() > 0)
{
%>		<tr valign="top">
			<td align="right">Trading partner:&nbsp;</td>
			<td>
				<select name="agy_select" onchange="on_select_tp()">
					<option value=""> (select one)</option>
<%
	for (int i=0; i < DisplayBean.getAgencyCount(); i++)
	{
%>					<option value="<%= DisplayBean.getAgencyId(i) %>"<% if (i == DisplayBean.getListSelection()) { %> selected<% } %>><%= DisplayBean.getAgencyNameDisplay(i) %></option>
<%
	}
%>					
				</select>
			</td>
		</tr>
<%
}
%>
		<tr valign="top">
			<td align="right">Search by:&nbsp;</td>
			<td nowrap="nowrap">
				<INPUT type="radio" name="search_by" value="agt_id"<% if (!DisplayBean.getSearchMethod().equals("agt_name")) { %> checked<% } %> onclick="on_select_method('Agent ID or participant code')"> Agent ID &nbsp; &nbsp;
				<INPUT type="radio" name="search_by" value="agt_name"<% if (DisplayBean.getSearchMethod().equals("agt_name")) { %> checked<% } %> onclick="on_select_method('Agent name')"> Agent name
			</td>
		</tr>
		<tr valign="top">
			<td align="right" nowrap="nowrap"><A name="search_label">
				<% if (DisplayBean.getSearchMethod().equals("agt_name")) { %>Agent name<% } else { %>Agent ID or participant code<% } %></A>:&nbsp;
			</td>
			<td><INPUT type="text" name="search_str" value="<%= DisplayBean.getSearchStr() %>" size="12" maxlength="10"></td>
		</tr>
		<tr valign="top">
			<td nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td align="right">Start Date:&nbsp;</td>
			<td nowrap="nowrap"><select name="start_mon">
				<OPTION value=""></OPTION>
    			<OPTION value="01">Jan</OPTION>
				<OPTION value="02">Feb</OPTION>
				<OPTION value="03">Mar</OPTION>
				<OPTION value="04">Apr</OPTION>
				<OPTION value="05">May</OPTION>
				<OPTION value="06">Jun</OPTION>
				<OPTION value="07">Jul</OPTION>
				<OPTION value="08">Aug</OPTION>
				<OPTION value="09">Sep</OPTION>
				<OPTION value="10">Oct</OPTION>
				<OPTION value="11">Nov</OPTION>
				<OPTION value="12">Dec</OPTION>
			  </select>&nbsp;
			  <select name="start_day">
			  	<option value=""></option>
			  	<option value="01">01</option>
			  	<option value="02">02</option>
			  	<option value="03">03</option>
			  	<option value="04">04</option>
			  	<option value="05">05</option>
			  	<option value="06">06</option>
			  	<option value="07">07</option>
			  	<option value="08">08</option>
			  	<option value="09">09</option>
			  	<option value="10">10</option>
			  	<option value="11">11</option>
			  	<option value="12">12</option>
			  	<option value="13">13</option>
			  	<option value="14">14</option>
			  	<option value="15">15</option>
			  	<option value="16">16</option>
			  	<option value="17">17</option>
			  	<option value="18">18</option>
			  	<option value="19">19</option>
			  	<option value="20">20</option>
			  	<option value="21">21</option>
			  	<option value="22">22</option>
			  	<option value="23">23</option>
			  	<option value="24">24</option>
			  	<option value="25">25</option>
			  	<option value="26">26</option>
			  	<option value="27">27</option>
			  	<option value="28">28</option>
			  	<option value="29">29</option>
			  	<option value="30">30</option>
			  	<option value="31">31</option>
			  </select>
			  <select name="start_yr">
				<option value=""></option>
<%
for (int i = 0; i < DisplayBean.getYears().size(); i++)
{
%>				<option value="<%= (String) DisplayBean.getYears().get(i) %>"><%= (String) DisplayBean.getYears().get(i) %></option>
<%
}
%>
			  </select>
			</td>
		</tr>				
		<tr>
			<td align="right">End Date:&nbsp;</td>
			<td nowrap="nowrap">
				<select name="end_mon">
						<OPTION value=""></OPTION>
    					<OPTION value="01">Jan</OPTION>
					    <OPTION value="02">Feb</OPTION>
					    <OPTION value="03">Mar</OPTION>
					    <OPTION value="04">Apr</OPTION>
					    <OPTION value="05">May</OPTION>
					    <OPTION value="06">Jun</OPTION>
					    <OPTION value="07">Jul</OPTION>
					    <OPTION value="08">Aug</OPTION>
					    <OPTION value="09">Sep</OPTION>
					    <OPTION value="10">Oct</OPTION>
					    <OPTION value="11">Nov</OPTION>
					    <OPTION value="12">Dec</OPTION>
				 </select>&nbsp;
			  <select name="end_day">
			  	<option value=""></option>
			  	<option value="01">01</option>
			  	<option value="02">02</option>
			  	<option value="03">03</option>
			  	<option value="04">04</option>
			  	<option value="05">05</option>
			  	<option value="06">06</option>
			  	<option value="07">07</option>
			  	<option value="08">08</option>
			  	<option value="09">09</option>
			  	<option value="10">10</option>
			  	<option value="11">11</option>
			  	<option value="12">12</option>
			  	<option value="13">13</option>
			  	<option value="14">14</option>
			  	<option value="15">15</option>
			  	<option value="16">16</option>
			  	<option value="17">17</option>
			  	<option value="18">18</option>
			  	<option value="19">19</option>
			  	<option value="20">20</option>
			  	<option value="21">21</option>
			  	<option value="22">22</option>
			  	<option value="23">23</option>
			  	<option value="24">24</option>
			  	<option value="25">25</option>
			  	<option value="26">26</option>
			  	<option value="27">27</option>
			  	<option value="28">28</option>
			  	<option value="29">29</option>
			  	<option value="30">30</option>
			  	<option value="31">31</option>
			  </select>
				 <select name="end_yr">
						<option value=""></option>
<%
for (int i = 0; i < DisplayBean.getYears().size(); i++)
{
%>						<option value="<%= (String) DisplayBean.getYears().get(i) %>"><%= (String) DisplayBean.getYears().get(i) %></option>
<%
}
%>
					  </select>
			</td>
		  </tr>				
		</table>
      </CENTER>

<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_report()">View Report</A></TD>
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
