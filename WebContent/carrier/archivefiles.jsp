<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.ArchiveCompanyDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<link rel="stylesheet" href="css/jquery-ui.css">
<link rel="stylesheet" href="css/style.css">
<!-- Include one of jTable styles. -->
<link href="css/lightcolor/blue/jtable.css" rel="stylesheet" type="text/css" />
<link href="css/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" />

<!-- <script src="js/jquery-1.11.1.js"></script> -->
<!-- <script src="js/jquery-ui.js"></script> -->

<!-- Include jTable script file. -->
<script src="js/jquery-1.8.2.js" type="text/javascript"></script>
<script src="js/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>
<script src="js/jquery.jtable.js" type="text/javascript"></script>
<script src="js/number_format.js" type="text/javascript"></script>
<script src="js/carrier/policyfile.js" type="text/javascript"></script>
<script src="js/carrier/directbill.js" type="text/javascript"></script>
<script src="js/carrier/claimfile.js" type="text/javascript"></script>
<script src="js/carrier/common.js" type="text/javascript"></script>
<script>

function changeDropdown(val) {
	if(val=='D' ) {
		//$("#archivefileAction option[value='R']").attr("disabled", true);	
		//$("#archivefileAction option[value='D']").attr("disabled", true);	
		$("#archivefileAction option[value='R']").remove();
		$("#archivefileAction option[value='D']").remove();
	}
	else if(val=='C' ) {
		//$("#archivefileAction option[value='R']").attr("disabled", true);	
		//$("#archivefileAction option[value='D']").attr("disabled", true);	
		$("#archivefileAction option[value='R']").remove();
		$("#archivefileAction option[value='D']").remove();
	}else if(val=='P' ) {
		//$("#archivefileAction option[value='R']").attr("disabled", false);	
		//$("#archivefileAction option[value='D']").attr("disabled", false);	
		$("<option value='R'>Set to Resend/Sync</option>").appendTo("#archivefileAction");
		$("<option value='D'>Set to Download</option>").appendTo("#archivefileAction");

		
	}
	
}
  $(function() {
    $( ".datepicker" ).datepicker();
  });
  
  
 
  </script>
<style>
.child-opener-image {
	cursor: pointer;
}

.child-opener-image-column {
	text-align: center;
}

.jtable-dialog-form {
	min-width: 220px;
}

.jtable-dialog-form input[type="text"] {
	min-width: 200px;
}

table.jtable{
overflow-y: scroll;
display:block;
overflow-x: hidden;
}

 div.jtable-column-header-container {height: auto !important;}
 tr.jtable-child-row div.jtable-main-container > div.jtable-title {
background-color: red;
}
</style>
<script>

$(function() {
    
	 $( "#dialog-confirm" ).dialog({
	      autoOpen: false,
	      show: {
	        effect: "blind",
	        duration: 500
	      },
	      hide: {
	        effect: "clip",
	        duration: 500
	      }
	    });
 
 });
function downloadFile(url) {
	/* newwindow=window.open(url,'name','height=200,width=150');
	if (window.focus) {newwindow.focus()}
	return false; */
	window.location.href =url;
}





function ResetPolicySearch() {
	 
	if(document.getElementById('srchPolicyNumber'))
		document.getElementById('srchPolicyNumber').value ="";
	
	if(document.getElementById('srchInsuredName'))
		document.getElementById('srchInsuredName').value =""
	
		if(document.getElementById('srchTxnType'))
		document.getElementById('srchTxnType').value =""
		
		if(document.getElementById('frmPolicyDate'))
			document.getElementById('frmPolicyDate').value =""
				
		if(document.getElementById('toPolicyDate'))
			document.getElementById('toPolicyDate').value =""				

	 $('#PolicyFileInfo').jtable('load', {
		 srchPolicyNumber: $('#srchPolicyNumber').val(),
		 srchInsuredName: $('#srchInsuredName').val(),
		 srchTxnType: $('#srchTxnType').val(),
		 frmPolicyDate: $('#frmPolicyDate').val(),
		 toPolicyDate: $('#toPolicyDate').val()
     });
}

function searchPolicyRecord() {
	
	if($('#frmPolicyDate').val()=='' && $('#toPolicyDate').val()!=''){
		alert("Please enter Policy creation From date");
		$( "#srchPolicydialog" ).dialog( "open" );
	}
	else if($('#toPolicyDate').val()=='' && $('#frmPolicyDate').val()!=''){
		alert("Please enter Policy creation To date");
		$( "#srchPolicydialog" ).dialog( "open" );
	}else {
	$('#PolicyFileInfo').jtable('load', {
		 srchPolicyNumber: $('#srchPolicyNumber').val(),
		 srchInsuredName: $('#srchInsuredName').val(),
		 srchTxnType: $('#srchTxnType').val(),
		 frmPolicyDate: $('#frmPolicyDate').val(),
		 toPolicyDate: $('#toPolicyDate').val()
     });
	}
}
function searchDirectBillRecord() {
	if($('#frmDBRDate').val()=='' && $('#toDBRDate').val()!=''){
		alert("Please enter direct bill creation From date");
		$( "#srchDirectBilldialog" ).dialog( "open" );
	}
	else if($('#toDBRDate').val()=='' && $('#frmDBRDate').val()!=''){
		alert("Please enter direct bill creation To date");
		$( "#srchDirectBilldialog" ).dialog( "open" );
	}else { 
	$('#DirectBillFileInfo').jtable('load', {
		 srcDBRPolicyNumber: $('#srcDBRPolicyNumber').val(),
		 srcDBRInsuredName: $('#srcDBRInsuredName').val(),
		 srcDBRCode: $('#srcDBRCode').val(),
		 searchFlag:'Y',
		 frmPolicyDate: $('#frmDBRDate').val(),
		 toPolicyDate: $('#toDBRDate').val()
    });
	}
}

function ResetDirectBillSearch() {
	 
	if(document.getElementById('srcDBRPolicyNumber'))
		document.getElementById('srcDBRPolicyNumber').value ="";
	
	if(document.getElementById('srcDBRInsuredName'))
		document.getElementById('srcDBRInsuredName').value =""
	
		if(document.getElementById('srcDBRCode'))
		document.getElementById('srcDBRCode').value =""
		
		if(document.getElementById('frmDBRDate'))
			document.getElementById('frmDBRDate').value =""
					
		if(document.getElementById('toDBRDate'))
				document.getElementById('toDBRDate').value =""				

	 $('#DirectBillFileInfo').jtable('load', {
		 srcDBRPolicyNumber: $('#srcDBRPolicyNumber').val(),
		 srcDBRInsuredName: $('#srcDBRInsuredName').val(),
		 srcDBRCode: $('#srcDBRCode').val(),
		 frmPolicyDate: $('#frmDBRDate').val(),
		 toPolicyDate: $('#toDBRDate').val()
     });
}


function ResetClaimSearch() {
	 
	if(document.getElementById('srchClaimNumber'))
		document.getElementById('srchClaimNumber').value ="";
	
	if(document.getElementById('srchClmPolicyNumber'))
		document.getElementById('srchClmPolicyNumber').value =""
	
	if(document.getElementById('srchclaimStatus'))
		document.getElementById('srchclaimStatus').value =""
		
	if(document.getElementById('srchclaimBPTCode'))
		document.getElementById('srchclaimBPTCode').value =""		
		
	if(document.getElementById('frmClaimFileDate'))
		document.getElementById('frmClaimFileDate').value =""
				
	if(document.getElementById('toClaimFileDate'))
		document.getElementById('toClaimFileDate').value =""				
	
					

	 $('#claimFileInfo').jtable('load', {
		 srchPolicyNumber: $('#srchClaimNumber').val(),
		 srchInsuredName: $('#srchClmPolicyNumber').val(),
		 srchTxnType: $('#srchclaimStatus').val(),
		 srchTxnType: $('#srchclaimBPTCode').val(),
		 frmPolicyDate: $('#frmClaimFileDate').val(),
		 frmPolicyDate: $('#toClaimFileDate').val()
     });
}

function searchClaimRecord() {
	
	if($('#frmClaimFileDate').val()=='' && $('#toClaimFileDate').val()!=''){
		alert("Please enter Claim creation file From date");
		$( "#srchClaimdialog" ).dialog( "open" );
	}
	else if($('#toClaimFileDate').val()=='' && $('#frmClaimFileDate').val()!=''){
		alert("Please enter Claim creation file To date");
		$( "#srchClaimdialog" ).dialog( "open" );
	}else {
		$('#claimFileInfo').jtable('load', {
			srchClaimNumber: $('#srchClaimNumber').val(),
			srchClmPolicyNumber: $('#srchClmPolicyNumber').val(),
			srchclaimStatus: $('#srchclaimStatus').val(),
			srchclaimBPTCode: $('#srchclaimBPTCode').val(),
			frmClaimFileDate: $('#frmClaimFileDate').val(),
			toClaimFileDate: $('#toClaimFileDate').val()
	     });
	}
}

$(function() {
    $( "#srchDirectBilldialog" ).dialog({
      autoOpen: false,
      width : 450,
      show: {
        effect: "blind",
        duration: 200
      },
      hide: {
        effect: "clip",
        duration: 700
      }
    });
 
    $( "#opener" ).click(function() {
      $( "#srchDirectBilldialog" ).dialog( "open" );
    });
  });


$(function() {
    $( "#srchPolicydialog" ).dialog({
      autoOpen: false,
      width : 450,
      show: {
        effect: "blind",
        duration: 200
      },
      hide: {
        effect: "clip",
        duration: 700
      }
    });
 
    $( "#opener" ).click(function() {
      $( "#srchPolicydialog" ).dialog( "open" );
    });
  });

$(function() {
    $( "#srchClaimdialog" ).dialog({
      autoOpen: false,
      width : 450,
      show: {
        effect: "blind",
        duration: 200
      },
      hide: {
        effect: "clip",
        duration: 700
      }
    });
 
    $( "#opener" ).click(function() {
      $( "#srchClaimdialog" ).dialog( "open" );
    });
  });
  
  
  function searchPolicyKeyPress(e)
{
    // look for window.event in case event isn't passed in
    e = e || window.event;
    if (e.keyCode == 13)
    {
    	searchPolicyRecord();
    	$( "#srchPolicydialog" ).dialog( "close" );
        return false;
    }
    return true;
}

function searchDirectBillKeyPress(e)
{
    // look for window.event in case event isn't passed in
    e = e || window.event;
    if (e.keyCode == 13)
    {
    	searchDirectBillRecord();
    	$( "#srchDirectBilldialog" ).dialog( "close" );
        return false;
    }
    return true;
}
function searchClaimKeyPress(e)
{
    // look for window.event in case event isn't passed in
    e = e || window.event;
    if (e.keyCode == 13)
    {
    	searchClaimRecord();
    	$( "#srchClaimdialog" ).dialog( "close" );
        return false;
    }
    return true;
}
</script>

<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	checkForClaimActivation();
	return true;
}

function allow_action()
{
	if (document.tudlform.data_changed.value == "Y")
	{
		msg = "You are about to lose any changes you have made on this page.  To save your changes, click 'Cancel' and then use the 'Save Changes' button at the bottom of the screen.";
		if (!confirm(msg))
			return false;
	}
	
	return true;
}

function do_page_action_verified(page_action)
{
<%
  for (int i=0; i < DisplayBean.getArchiveFilesCount(); i++)
  {
%>	<% if (i>0) { %>else <% } %>if (document.tudlform.file<%= i %>flag.checked == true && (!document.tudlform.file<%= i %>type.value || document.tudlform.file<%= i %>type.value == "<%= DisplayBean.getStatusArchive() %>" || document.tudlform.file<%= i %>type.value == "<%= DisplayBean.getStatusDBArchive() %>"))
	{
		alert("You must select a download type for all archived files flagged for download.");
		document.tudlform.file<%= i %>type.focus();
		return;
	}
<%
  }
%>
	do_page_action(page_action);
}

function change_settings(file_index)
{
	document.tudlform.data_changed.value = "Y";
<%
  for (int i=0; i < DisplayBean.getArchiveFilesCount(); i++)
  {
%>
	<% if (i > 0) { %>else <% } %>if (file_index == "<%= i %>")
	{
		document.tudlform.file<%= i %>changed.value = "Y";
		if (document.tudlform.file<%= i %>flag.checked == true)
		{
			document.tudlform.file<%= i %>type.disabled = false;
			if (document.tudlform.file<%= i %>dirbill.value == "Y")
				document.tudlform.file<%= i %>type.value = "<%= DisplayBean.getStatusDBCurrent() %>";
			else if (!document.tudlform.file<%= i %>type.value || document.tudlform.file<%= i %>type.value == "<%= DisplayBean.getStatusArchive() %>")
				document.tudlform.file<%= i %>type.value = "<%= DisplayBean.getStatusDownload() %>";
		}
		else
			document.tudlform.file<%= i %>type.disabled = true;
<%
    if (DisplayBean.isTransShown())
    {
      connective.teamup.download.db.FileInfo file = DisplayBean.getArchiveFile(i);
      for (int t=0; t < file.getTransactionCount(); t++)
      {
        connective.teamup.download.db.TransactionInfo trans = file.getTransaction(t);
%>		document.tudlform.file<%= i %>trans<%= t %>.checked = document.tudlform.file<%= i %>flag.checked;
<%
      }
    }
%>	}
<%
  }
%>
}

function change_file(file_index)
{
	document.tudlform.data_changed.value = "Y";
<%
  for (int i=0; i < DisplayBean.getArchiveFilesCount(); i++)
  {
%>
	<% if (i > 0) { %>else <% } %>if (file_index == "<%= i %>")
	{
		document.tudlform.file<%= i %>changed.value = "Y";
<%
    if (DisplayBean.isTransShown())
    {
%>
		if (<%
    connective.teamup.download.db.FileInfo file = DisplayBean.getArchiveFile(i);
    for (int t=0; t < file.getTransactionCount(); t++)
    {
      if (t > 0) { %> || <% } %>document.tudlform.file<%= i %>trans<%= t %>.checked<%
	} %>)
		{
			if (document.tudlform.file<%= i %>flag.checked == false)
			{
				document.tudlform.file<%= i %>flag.checked = true;
				document.tudlform.file<%= i %>type.disabled = false;
				if (document.tudlform.file<%= i %>dirbill.value == "Y")
				{
					document.tudlform.file<%= i %>type.focus();
					document.tudlform.file<%= i %>type.value = "<%= DisplayBean.getStatusDBCurrent() %>";
				}
				else if (!document.tudlform.file<%= i %>type.value || document.tudlform.file<%= i %>type.value == "<%= DisplayBean.getStatusArchive() %>")
				{
					document.tudlform.file<%= i %>type.focus();
					document.tudlform.file<%= i %>type.value = "<%= DisplayBean.getStatusDownload() %>";
				}
			}
		}
		else
		{
			if (document.tudlform.file<%= i %>flag.checked == true)
			{
				document.tudlform.file<%= i %>flag.checked = false;
				change_settings(file_index);
			}
		}
<%
    }
%>
	}
<%
  }
%>
}

function delete_file(filetype, filename, filedate)
{
	var msg = "Deleting this file will permanently remove it from the download database.\n\nDo you wish to delete the specified file?";
	if (confirm(msg))
	{
		document.tudlform.action_filetype.value = filetype;
		document.tudlform.action_filename.value = filename;
		document.tudlform.action_filedate.value = filedate;
		do_page_action_verified("archive_delete");
	}
}
 -->
</SCRIPT>
<script>
	$(function() {
		$("#tabs").tabs();
	});
	function goBack() {
	    do_action('tplist_default_new');
	}
</script>
<script type="text/javascript">
	$(function(){
		$('.btnClick').click(function(){
			var $selectedRowsP = $('#PolicyFileInfo').jtable('selectedRows');
			var $selectedRowsD = $('#DirectBillFileInfo').jtable('selectedRows');
			var $selectedRowsC = $('#claimFileInfo').jtable('selectedRows');
	      	var transactionIds ;
	      	var claimtransactionIds;
	      	
	      	if(document.tudlform.transactionIds){
	      		transactionIds = document.tudlform.transactionIds.value;
	      		document.tudlform.transactionIds.value = "";
	      	}
	      	if(document.tudlform.claimtransactionIds){
	      		claimtransactionIds = document.tudlform.claimtransactionIds.value;
	      		document.tudlform.claimtransactionIds.value = "";
	      	}
			if ($selectedRowsP.length > 0 || transactionIds!="") {
            	var ids = [];
            	var agentId ;
		      	var fileName ;
		      	var createdDate;
		      	var status;
		      	$selectedRowsP.each(function (index,Element) {
			      		agentId = $(this).data('record').agentId;
			      		fileName = $(this).data('record').orig_filename;
			      		createdDate = $(this).data('record').created_dt;
			      		status = $(this).data('record').dl_status;
			      		ids.push(agentId+":"+ fileName +":"+createdDate+":"+status);
                });
            	var idsjoined = ids.join();
            	if ($('.fileAction').val() == 'Delete'){
            		
            		$( "#dialog-confirm" ).dialog( "open" );
            		$( "#dialog-confirm" ).dialog({
		   	         		 resizable: false,
		   	          		height:140,
		   	          		modal: true,
		   	          		buttons: {
		   	            	"Delete": function() {
		   	            	 $( this ).dialog( "close" );
		   	            		$.ajax({
		   	            		 url: "Controller?action=delete&ids="+idsjoined,
  	            		  cache: false,
  	            		  success: function(result){
  	            			$('.jtable-row-selected').removeClass('jtable-row-selected');
  	            			  $('#PolicyFileInfo').jtable('reload');
  	            		  }
  	            		});
		   	            },
		   	            Cancel: function() {
		   	              $( this ).dialog( "close" );
		   	            }
		   	          }});
            		
            		
            		
            		
            		
            		/* $.ajax({
              		  url: "Controller?action=delete&ids="+idsjoined,
              		  cache: false,
              		  success: function(result){
              			  $('#PolicyFileInfo').jtable('load');
              		  }
              		});
            		$('#PolicyFileInfo').jtable('load'); */
            	} else{
            		$.ajax({
              		  url: "company?action=policyfileAction&method=update&actionType=JSON&actionValue="+$('.fileAction').val()+"&ids="+idsjoined +"&transactionIds="+transactionIds,
              		  cache: false,
              		  success: function(result){
              			$('.jtable-row-selected').removeClass('jtable-row-selected');
              			 $('#PolicyFileInfo').jtable('reload');
              		  }
              		});
            		//$('#PolicyFileInfo').jtable('load');
            	}
            }
			// Direct Bill status change
			if ($selectedRowsD.length > 0) {
            	var ids = [];
            	var agentId ;
		      	var fileName ;
		      	var createdDate;
			      	$selectedRowsD.each(function (index,Element) {
			      		agentId = $(this).data('record').agentId;
			      		fileName = $(this).data('record').orig_filename;
			      		createdDate = $(this).data('record').created_dt;
			      		ids.push(($(this).data('record').agentId)+":" +($(this).data('record').orig_filename)+":"+$(this).data('record').created_dt);
                });
            	var idsjoined = ids.join();
            	if ($('.fileAction').val() == 'Delete'){
            		$.ajax({
              		  url: "Controller?action=delete&ids="+idsjoined,
              		  cache: false,
              		  success: function(result){
              			  $('#DirectBillFileInfo').jtable('load');
              		  }
              		});
            		$('#DirectBillFileInfo').jtable('load');
            	} else{
            		$valAction = $('.fileAction').val();
            		if($valAction =='C'){
            			$valAction ='X';
            		}
            		if($valAction =='A'){
            			$valAction ='Y';
            		}
            		$.ajax({
              		  url: "company?action=policyfileAction&method=update&actionType=JSON&actionValue="+$valAction+"&ids="+idsjoined,
              		  cache: false,
              		  success: function(result){
                			$('.jtable-row-selected').removeClass('jtable-row-selected');
              			  $('#DirectBillFileInfo').jtable('reload');
              		  }
              		});
            		//$('#DirectBillFileInfo').jtable('load');
            	}
            }
			
			if ($selectedRowsC.length > 0 || claimtransactionIds!="") {
            	var ids = [];
            	var agentId ;
		      	var fileName ;
		      	var createdDate;
	
			      	$selectedRowsC.each(function (index,Element) {
			      		agentId = $(this).data('record').agentId;
			      		fileName = $(this).data('record').orig_filename;
			      		createdDate = $(this).data('record').created_dt;
			      		ids.push(($(this).data('record').agentId)+":" +($(this).data('record').orig_filename)+":"+$(this).data('record').created_dt);
                });
            	var idsjoined = ids.join();
            	if ($('.fileAction').val() == 'Delete'){
            		
            		$( "#dialog-confirm" ).dialog( "open" );
            		$( "#dialog-confirm" ).dialog({
		   	         		 resizable: false,
		   	          		height:140,
		   	          		modal: true,
		   	          		buttons: {
		   	            	"Delete": function() {
		   	            	 $( this ).dialog( "close" );
		   	            		$.ajax({
		   	            		 url: "Controller?action=delete&ids="+idsjoined,
  	            		  cache: false,
  	            		  success: function(result){
  	            			$('.jtable-row-selected').removeClass('jtable-row-selected');
  	            			  $('#claimFileInfo').jtable('reload');
  	            		  }
  	            		});
		   	            },
		   	            Cancel: function() {
		   	              $( this ).dialog( "close" );
		   	            }
		   	          }});
            		
            		
            		
            		
            		
            		/* $.ajax({
              		  url: "Controller?action=delete&ids="+idsjoined,
              		  cache: false,
              		  success: function(result){
              			  $('#PolicyFileInfo').jtable('load');
              		  }
              		});
            		$('#PolicyFileInfo').jtable('load'); */
            	} else{
            		$valAction = $('.fileAction').val();
            		if($valAction =='C'){
            			$valAction ='P';
            		}
            		if($valAction =='A'){
            			$valAction ='Q';
            		}
            		$.ajax({
              		  url: "company?action=claimfileAction&method=update&actionType=JSON&actionValue="+$valAction+"&ids="+idsjoined +"&transactionIds="+claimtransactionIds,
              		  cache: false,
              		  success: function(result){
              			$('.jtable-row-selected').removeClass('jtable-row-selected');
              			 $('#claimFileInfo').jtable('reload');
              		  }
              		});
            		//$('#PolicyFileInfo').jtable('load');
            	}
            }

		})
	})
</script>

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
    <TD>
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">
    <INPUT type="hidden" name="filecount" value="<%= DisplayBean.getArchiveFilesCount() %>">
    <INPUT type="hidden" name="data_changed" value="N">
    <INPUT type="hidden" name="origPage" value="<%= DisplayBean.getOriginatingPage() %>">
    <INPUT type="hidden" name="trans_shown" value="<% if (DisplayBean.isTransShown()) { %>Y<% } else { %>N<% } %>">
    <INPUT type="hidden" name="current_sort" value="<%= DisplayBean.getTpSortOrder() %>">
    <INPUT type="hidden" name="action_filetype" value="">
    <INPUT type="hidden" name="action_filename" value="">
    <INPUT type="hidden" name="action_filedate" value="">
    <INPUT type="hidden" name="pagever" value="C">
    <INPUT type="hidden" name="transactionIds" id ="transactionIds">
    <INPUT type="hidden" name="claimtransactionIds" id ="claimtransactionIds">

<!-- BEGIN BODY -->

<%
int NUM_COLUMNS = 1;
if (DisplayBean.isParticipantUsed())
	NUM_COLUMNS += 2;
%>
     <!--  <B>Current &amp; Archived Download Files:</B> -->

<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  
  
 
  <%-- <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR> --%>
</TABLE>
      <H3 align="center">TEAM-UP Download Agency Archive For : <%= DisplayBean.getAgentId() %> - <%= DisplayBean.getAgentName() %> <% if ("".equals(DisplayBean.getAgentName())) { %><EM>(Not a registered agent ID)</EM><% } %>&nbsp;</H3>
	<div class="wrapper">
		<div class="container">
			<p><p>
			<div id="tabs">
				<div class="option">
				<select class="fileAction" id ="archivefileAction">
					<option>Archive Action</option>
					<option value="R">Set to Resend/Sync</option>
					<option value="A">Set to Archive</option>
					<option value="C">Set to Current</option>
					<option value="D">Set to Download</option>
				</select>
				<input type="button" class="btnClick Button" value="Save" />
				<button onclick="goBack()">Back</button>
			</div>
				<ul id ="files">
					<li><a href="#policyFiles" onclick="changeDropdown('P');">Policy Files(AL3/XML)</a></li>
					<li><a href="#directBillFiles" onclick="changeDropdown('D');" >Direct Bill Files</a></li>
					<li><a href="#claimFiles" onclick="changeDropdown('C');" >Claim Files</a></li>
				</ul>
				
				<div id="policyFiles">
					<div id="PolicyFileInfo"></div>
				</div>
				
				
				<div id="directBillFiles">
					<div id="DirectBillFileInfo"></div>
				</div>

				<div id="claimFiles">
					<div id="claimFileInfo"></div>
				</div>

			</div>
		</div>
	</div>
	
<div id="srchDirectBilldialog" title="Search Direct Bill files">
 <TABLE border='0'>
			<TR>
				<TD><strong><label class='jtable-input-label'>Policy Number:   </label></strong></TD>
				<TD><input type='text' id='srcDBRPolicyNumber' name='srcDBRPolicyNumber' onkeypress='return searchDirectBillKeyPress(event);' /></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Insured Name:  </label></strong></TD>
				<TD><input type='text' id='srcDBRInsuredName' name='srcDBRInsuredName' onkeypress='return searchDirectBillKeyPress(event);' /></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Transaction Type:</label></strong></TD>
				<TD>
				<select name='srcDBRCode' id='srcDBRCode' onkeypress='return searchDirectBillKeyPress(event);'>
  					<option value="">ALL</option>
  					<option value="REV">Assignment Reversal</option>
  					<option value="XLC">Cancellation Confirmation</option>
  					<option value="XLN">Cancellation Request</option>
  					<option value="NBS">New Business</option>
  					<option value="NBQ">New Business Quote</option>
  					<option value="NRA">Non-renewal notification to agency</option>
  					<option value="RWX">Non-Renewal</option>
  					<option value="PCH">Policy Change</option>
  					<option value="PCQ">Policy Change Quote</option>
  					<option value="PNQ">Policy Inquiry</option>
  					<option value="RWL">Renewal Image</option>
  					<option value="REI">Reinstatement</option>
  					<option value="REW">Rewrite</option>
  					<option value="RIX">Re-Issue</option>
  					<option value="RNR">Reversal of Non-Renewal</option>
  					<option value="RRQ">Renewal Re-Quote</option>
  					<option value="RWQ">Renewal Quote</option>
  					<option value="RWR">Renewal Request</option>
  					
			</select></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>File Creation Date:</label></strong></td>
				<td> <input type="text" size ="10" class="datepicker" id ="frmDBRDate" name ="frmDBRDate" onkeypress='return searchDirectBillKeyPress(event);'> To <input type="text" size ="10"  class="datepicker" id ="toDBRDate" name ="toDBRDate" onkeypress='return searchDirectBillKeyPress(event);'></TD>
			</TR>
		</TABLE>
</div>
<div id="srchPolicydialog" title="Search policy files">
 <TABLE border='0'>
			<TR>
				<TD><strong><label class='jtable-input-label'>Policy Number:   </label></strong></TD>
				<TD><input type='text' id='srchPolicyNumber' name='srchPolicyNumber' onkeypress='return searchPolicyKeyPress(event);' /></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Insured Name:  </label></strong></TD>
				<TD><input type='text' id='srchInsuredName' name='srchInsuredName' onkeypress='return searchPolicyKeyPress(event);'/></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Transaction Type:</label></strong></TD>
				<TD>
				<select name='srchTxnType' id='srchTxnType' onkeypress='return searchPolicyKeyPress(event);'>
  					<option value="">ALL</option>
  					<option value="REV">Assignment Reversal</option>
  					<option value="XLC">Cancellation Confirmation</option>
  					<option value="XLN">Cancellation Request</option>
  					<option value="NBS">New Business</option>
  					<option value="NBQ">New Business Quote</option>
  					<option value="NRA">Non-renewal notification to agency</option>
  					<option value="RWX">Non-Renewal</option>
  					<option value="PCH">Policy Change</option>
  					<option value="PCQ">Policy Change Quote</option>
  					<option value="PNQ">Policy Inquiry</option>
  					<option value="RWL">Renewal Image</option>
  					<option value="REI">Reinstatement</option>
  					<option value="REW">Rewrite</option>
  					<option value="RIX">Re-Issue</option>
  					<option value="RNR">Reversal of Non-Renewal</option>
  					<option value="RRQ">Renewal Re-Quote</option>
  					<option value="RWQ">Renewal Quote</option>
  					<option value="RWR">Renewal Request</option>
  					
			</select></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>File Creation Date:</label></strong></td>
				<td> <input type="text" size ="10" class="datepicker" name ="frmPolicyDate" id ="frmPolicyDate" onkeypress='return searchPolicyKeyPress(event);'> To <input type="text" size ="10"  class="datepicker" name ="toPolicyDate" id ="toPolicyDate" onkeypress='return searchPolicyKeyPress(event);'></TD>
			</TR>
		</TABLE> 
</div>

<div id="srchClaimdialog" title="Search claim files">
 <TABLE border='0'>
			<TR>
				<TD><strong><label class='jtable-input-label'>Claim Number:   </label></strong></TD>
				<TD><input type='text' id='srchClaimNumber' name='srchClaimNumber' onkeypress='return searchClaimKeyPress(event);'/></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Policy Number:  </label></strong></TD>
				<TD><input type='text' id='srchClmPolicyNumber' name='srchClmPolicyNumber' onkeypress='return searchClaimKeyPress(event);'/></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Claim Status:</label></strong></TD>
				<TD>
				<select name='srchclaimStatus' id='srchclaimStatus' onkeypress='return searchClaimKeyPress(event);'>
  					<option value="">ALL</option>
  					<option value="C">Close</option>
  					<option value="O">Open</option>
  					<option value="R">Re-opened</option>
  					<option value="OT">OT</option>
  					<option value="D">D</option>
  					<option value="S">S</option>
  					
			</select></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Business Purpose Type Code:</label></strong></TD>
				<TD>
				<select name='srchclaimBPTCode' id='srchclaimBPTCode' onkeypress='return searchClaimKeyPress(event);'>
  					<option value="">ALL</option>
  					<option value="CLI">CLI</option>
  					<option value="CLN">CLN</option>
  					<option value="CLS">CLS</option>
  					<option value="ADJ">ADJ</option>
  					<option value="PMT">PMT</option>
  					<option value="RES">RES</option>
  					
			</select></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>File Creation Date:</label></strong></td>
				<td> <input type="text" size ="10" class="datepicker" name ="frmClaimFileDate" id ="frmClaimFileDate" onkeypress='return searchClaimKeyPress(event);'> To <input type="text" size ="10"  class="datepicker" name ="toClaimFileDate" id ="toClaimFileDate" onkeypress='return searchClaimKeyPress(event);'></TD>
			</TR>
			
		</TABLE> 
</div>

<div id="dialog-confirm" title="Delete Files?">
  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Selected files will be deleted.Are you sure?</p>
</div>
<table width="100%" >
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>


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
				
</body>
</HTML>	
	
</BODY>
</HTML>
