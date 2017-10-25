<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<LINK rel="stylesheet" href="<c:url value="/css/custom.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AgentSeparateDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<link rel="stylesheet" href="css/jquery-ui.css">
<script src="js/jquery-1.11.2.js"></script>
<script src="js/jquery-ui.js"></script>
<script>
	$(function() {
        /*
        *Ref: http://stackoverflow.com/questions/6373512/source-only-allowed-values-in-jquery-ui-autocomplete-plugin
        */
        $('#partToAgent').keypress(function() {
			$.ajax({
				url : 'company?action=beanlist&method=participantList&actionType=JSON',
				type : "post",
				data : '',
				success : function(data) {
					var previousValue = "";
					$("#partToAgent").autocomplete({
						autoFocus: true,
						source : data
					}).keyup(function() {
					    var isValid = false;
					    for (i in data) {
					        if (data[i].toLowerCase().match(this.value.toLowerCase())) {
					            isValid = true;
					        }
					    }
					    if (!isValid) {
					        this.value = previousValue
					        alert("Please enter valid participant")
					    } else {
					        previousValue = this.value;
					    }
					});

				}, // end success

			});

		}); // end keypress function

	});
	
	
</script>
<SCRIPT language="JavaScript" type="text/javascript">
<!--

<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{ <%
  if (DisplayBean.getAgentCount() == 1)
  {
%>
	document.tudlform.agent.checked = true;
	document.tudlform.agentID.value = "";
<%
  }
%>
	document.tudlform.agentID_top.focus();
	
	return true;
}

function allow_action()
{
	return true;
}

function do_addnew()
{
	document.tudlform.agentID.value = "";
	do_page_action("tplist_addnew");
}

function do_edit(agentnum)
{
	document.tudlform.agentID.value = agentnum;
	do_page_action("tplist_edit");
}

function do_sort(sortby)
{
	document.tudlform.sort_order.value = sortby;
	do_page_action("tplist_sort");
}

function do_archive()
{ <%
  if (DisplayBean.getAgentCount() == 1)
  {
%>
	do_page_action("tplist_archive");
<%
  }
  else
  {
%>
	if (!document.tudlform.agentID.value)
	{
		alert("Please select a trading partner to view.");
		document.tudlform.agent[0].focus();
	}
	else
	{
		do_page_action("tplist_archive");
	}
<%
  }
%>
}

function do_files(){
	<%
	if (DisplayBean.getAgentCount() == 1){
	%>
	do_page_action("tplist_files");
	<%
	}
	else
	{
	%>
	if (!document.tudlform.agentID.value){
		alert("Please select a trading partner to view.");
		document.tudlform.agent[0].focus();
		} else{
			do_page_action("tplist_files");
		}
	<%
	}
	%>
}

function do_delete()
{
	msg = "You are about to delete this trading partner (Agent ID: " + document.tudlform.agentID.value + ") and any associated download files from the database.  Are you sure?";
<%
  if (DisplayBean.getAgentCount() == 1)
  {
%>
	if (confirm(msg))
<%
  }
  else 
  {
%>
	if (!document.tudlform.agentID.value)
	{
		alert("Please select a trading partner to delete.");
		document.tudlform.agent[0].focus();
	}
	else if (confirm(msg))
<%
  }
%>	{
		do_page_action("tplist_delete");
	}
}

function on_select_agent(agentnum)
{
	document.tudlform.agentID.value = agentnum;
}

function jumpTo(letter)
{
	document.tudlform.alphaStart.value = letter;
	document.tudlform.startPos.value = 0;
	do_page_action("tplist_alpha");
}

function on_select_numrows(selbox)
{
	if (selbox == "top")
	{
		document.tudlform.maxRows.value = document.tudlform.numrows_top.value;
		document.tudlform.numrows_bottom.value = document.tudlform.numrows_top.value;
	}
	else
	{
		document.tudlform.maxRows.value = document.tudlform.numrows_bottom.value;
		document.tudlform.numrows_top.value = document.tudlform.numrows_bottom.value;
	}
}

function search_by_agentid(editbox)
{
	if (editbox == "top")
	{
		if (!document.tudlform.agentID_top.value)
		{
			alert("Please enter an agent ID or participant code to search for.");
			document.tudlform.agentID_top.focus();
			return false;
		}
		document.tudlform.agentID.value = document.tudlform.agentID_top.value;
	}
	else
	{
		if (!document.tudlform.agentID_bottom.value)
		{
			alert("Please enter an agent ID or participant code to search for.");
			document.tudlform.agentID_bottom.focus();
			return false;
		}
		document.tudlform.agentID.value = document.tudlform.agentID_bottom.value;
	}
	
	document.tudlform.agentSearch.value = document.tudlform.agentID.value;
	document.tudlform.search_type.value = "agentid";
	do_page_action("tplist_agentid_search");
	
	return true;
}

function do_continue(){
	 do_page_action("tp_Separation_Only");
}

function do_cancel(){
	 do_page_action("dist_cancel");
}

$(function(){
    $("#continue").click(function() {
    	var partCode = document.tudlform.tag1.value;
    	if (partCode == ''){
    		alert("Please enter participant code");
    		document.tudlform.tag1.focus();
    		return false;
    	}
    	$.ajax({
    		  url: "AgentSeparation?action=tp_Separation_Only&pCode="+partCode,
    		  cache: false,
    		success: function(result){
    			window.location.href = "AgentSeparation?action=tp_summary&pCode="+partCode;
  		  }
    		});
        /* if ($("input[name=tp]:checked").val() == 'tpSeparation'){
        	var partCode = document.tudlform.tag1.value;
        	if (partCode == ''){
        		alert("Please Enter Participant Code");
        		return false;
        	}
            $.ajax({
      		  url: "AgentSeparation?action=tp_Separation_Only&pCode="+partCode,
      		  cache: false,
      		success: function(result){
      			window.location.href = "AgentSeparation?action=tp_summary&pCode="+partCode;
    		  }
      		});
        }  if ($("input[name=tp]:checked").val() == 'tpMove'){
        	var moveAgent = document.tudlform.tag2.value;
        	var newTPAgent = document.tudlform.tag3.value;
        	if (moveAgent == '' && newTPAgent == ''){
        		alert("Please Enter Agent IDs");
        		return false;
        	}
        	if (moveAgent == ''){
        		alert("Please Enter Move Agent ID");
        		return false;
        	}
        	if (newTPAgent == ''){
        		alert("Please Enter New Trading Partner Agent ID");
        		return false;
        	}
        	  $.ajax({
        		  url: "AgentSeparation?action=tp_Move_Only&mAgent="+moveAgent+"&nTPAgent="+newTPAgent,
        		  cache: false,
        	success: function(result){
            	window.location.href = "AgentSeparation?action=tp_move_summary&mAgent="+moveAgent+"&nTPAgent="+newTPAgent;
       		  }
       		});
        } */
        
    });
});
// -->
</SCRIPT>

</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<TABLE width="100%" align="center" border="0" cellpadding="0" cellspacing="0" mm:layoutgroup="true" bgcolor="#FFFFFF">
  <TR>
    <TD>
      <TABLE width="100%" align="center" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
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
  <td>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center"> 
    	<tr>
    		<td class="docbody">
    			<FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
    			<INPUT type="hidden" name="action" value="">
    			<H3 align="center">Trading Partner Separation</H3>
    			<br><br><br><br>
		              <div id="wrapper">
		              <fieldset>
		              		<div>
                				<div class="small">Agency Id:</div>
                    				<input id="partToAgent" name="tag1" value="<%= DisplayBean.getPartcode()%>">
                			</div>
                	  </fieldset>
		              </div>
   				</FORM>
    		</td>
  		</tr>
    </table>
    </td>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_cancel()">Cancel</A></TD>
		  <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" id="continue">Continue</A></TD>
		  <!-- TD class="pagebar" nowrap="nowrap"><input type="submit" onclick="do_cancel();" value="Cancel"></TD>
		  <TD class="pagebar" nowrap="nowrap"><input type="submit" id="continue" value="Continue"></TD-->
          <TD class="pagebar" nowrap="nowrap" width="100%"></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
</TABLE>
<%@ include file="footer.jsp" %>
</BODY>
</HTML>
