<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.TradingPartnerListDisplayBean" />
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
<script src="js/carrier/common.js" type="text/javascript"></script>
<script>
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

<script type="text/javascript">

function searchTransaction() {
	
	if($('#frmClaimFileDate').val()=='' && $('#toClaimFileDate').val()!=''){
		alert("Please enter File creation From date");
		$( "#srchReportdialog" ).dialog( "open" );
	}
	else if($('#toClaimFileDate').val()=='' && $('#frmClaimFileDate').val()!=''){
		alert("Please enter File creation To date");
		$( "#srchReportdialog" ).dialog( "open" );
	}else if($('#frmClaimEventDate').val()=='' && $('#toClaimEventDate').val()!=''){
		alert("Please enter Activity From date");
		$( "#srchReportdialog" ).dialog( "open" );
	}else if($('#toClaimEventDate').val()=='' && $('#frmClaimEventDate').val()!=''){
		alert("Please enter Activity To date");
		$( "#srchReportdialog" ).dialog( "open" );
	}else {
	$('#ReportContainer').jtable('load', {
		 srchClaimNumber: $('#srchClaimNumber').val(),
		 srchClmPolicyNumber: $('#srchClmPolicyNumber').val(),
		 srchclaimStatus: $('#srchclaimStatus').val(),
		 searchAgentId: $('#searchAgentId').val(),
		 frmClaimFileDate: $('#frmClaimFileDate').val(),
		 toClaimFileDate: $('#toClaimFileDate').val(),
		 frmClaimEventDate: $('#frmClaimEventDate').val(),
		 toClaimEventDate: $('#toClaimEventDate').val()
     });
	}
}

function ResetTransaction() {
	 
	if(document.getElementById('srchClaimNumber'))
		document.getElementById('srchClaimNumber').value ="";
	
	if(document.getElementById('srchClmPolicyNumber'))
		document.getElementById('srchClmPolicyNumber').value =""
	
		if(document.getElementById('srchclaimStatus'))
		document.getElementById('srchclaimStatus').value =""
		
		if(document.getElementById('searchAgentId'))
			document.getElementById('searchAgentId').value =""
				
		if(document.getElementById('frmClaimFileDate'))
			document.getElementById('frmClaimFileDate').value =""		
		
		if(document.getElementById('toClaimFileDate'))
			document.getElementById('toClaimFileDate').value =""
	
	if(document.getElementById('frmClaimEventDate'))
			document.getElementById('frmClaimEventDate').value =""			
	
	if(document.getElementById('toClaimEventDate'))
			document.getElementById('toClaimEventDate').value =""		

				$('#ReportContainer').jtable('load', {
					 srchClaimNumber: $('#srchClaimNumber').val(),
					 srchClmPolicyNumber: $('#srchClmPolicyNumber').val(),
					 srchclaimStatus: $('#srchclaimStatus').val(),
					 searchAgentId: $('#searchAgentId').val(),
					 frmClaimFileDate: $('#frmClaimFileDate').val(),
					 toClaimFileDate: $('#toClaimFileDate').val(),
					 frmClaimEventDate: $('#frmClaimEventDate').val(),
					 toClaimEventDate: $('#toClaimEventDate').val()
			     });
}


$(document).ready(function (data) {
	$("#srchReportdialog").hide();
	$("#exportReportdialog").hide();
	$('#ReportContainer').jtable({
	            title: 'Transaction activity log details',
	            sorting: true,
	            paging: true,
	            selecting: false,
	    		multiselect: true,
	    		 toolbar: {
	    	            items: [{
	    	                icon: 'images/printer.png',
	    	                text: 'print',
	    	                click: function () {
	    	                    //perform your custom job...
	    	                	var divToPrint = document.getElementById('ReportContainer');
	    			        	newWin = window.open("");
	    			        	newWin.document.write(divToPrint.outerHTML);
	    			        	newWin.print();
	    			        	newWin.close();
	    	                }
	    	            },{
	    	            	icon: 'images/find.png',
	    	                text: 'search',
	    	                click: function () {
	    	                	var viewportWidth = window.innerWidth-20;
	    	                	var viewportHeight = window.innerHeight-20;
	    	                	if (viewportWidth > 1000) viewportWidth = 400;
	    	                	if (viewportHeight > 500) viewportHeight = 250;
	    	                	
	    	                	var dialogContainer=document.body.appendChild(document.createElement("div"));
	    	                	dialogContainer.style.position="fixed";
	    	                	dialogContainer.style.top=dialogContainer.style.left="50%";
	    	                    //perform your custom job...
	    	                	//var $div = $("<div id='dialog' title='Search Agent'>" + "<strong><label class='jtable-input-label'>Agent Id </label></strong><input type='text' name='srcAgent'/> <BR><strong><label class='jtable-input-label'>Agent Name </label><input type='text' name='srcAgent'/></strong></div>");
	    	                //var $div = $("<div id='srcdialog' title='Search Agent'><form name ='tudlform1'><TABLE border='0'><TR><TD>" + "<strong><label class='jtable-input-label'>Agent Id:</label></strong></TD><TD><input type='text' id='searchAgentId' name='searchAgentId'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Agent Name:</label></strong></TD><TD><input type='text' id='searchAgentName' name='searchAgentName'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Participant Code </label></strong></TD><TD><input type='text' name='searchPartCode' id='searchPartCode'/></TD></TR></TABLE> </form></div>");
	    	                $( "#srchReportdialog" ).dialog({
	    	                	height: viewportHeight,
	    	            		width: viewportWidth,
	    	            		autoOpen: false,
	    	            		modal: false,
	    	            		resizable: true,
	    	            		 appendTo: dialogContainer,
	    	            		position: {
	    	                		/* my: "left top",
	    	                		at: "left top",
	    	                		of: window,
	    	                		collision: "none" */
	    	            			at: 'center center',
	    	            	        of: dialogContainer
	    	                	},
	    	                	create: function (event, ui) {
	    	            			$(event.target).parent().css('position', 'fixed');
	    	            		},
	    	                        buttons: [
	    	                                  {
	    	                                    text: "search",
	    	                                    id: "srchAgent",
	    	                                    icons: {
	    	                                      primary: "ui-icon-heart"
	    	                                    },
	    	                                    click: function() {
	    	                                      $( this ).dialog( "close" );
	    	                                      searchTransaction();
	    	                                    }
	    	                                  
	    	                                    // Uncommenting the following line would hide the text,
	    	                                    // resulting in the label being used as a tooltip
	    	                                    //showText: false
	    	                                  }
	    	                                ]
	    	                    }); 
	    	                	 $( "#srchReportdialog" ).dialog( "open" );
	    	                }
	    	            },{
	    	            	icon: 'images/export.png',
	    	                text: 'export',
	    	                click: function () {
	    	                	var viewportWidth = window.innerWidth-20;
	    	                	var viewportHeight = window.innerHeight-20;
	    	                	if (viewportWidth > 1000) viewportWidth = 400;
	    	                	if (viewportHeight > 500) viewportHeight = 200;
	    	                	
	    	                	var dialogContainer=document.body.appendChild(document.createElement("div"));
	    	                	dialogContainer.style.position="fixed";
	    	                	dialogContainer.style.top=dialogContainer.style.left="50%";
	    	                    //perform your custom job...
	    	                	//var $div = $("<div id='dialog' title='Search Agent'>" + "<strong><label class='jtable-input-label'>Agent Id </label></strong><input type='text' name='srcAgent'/> <BR><strong><label class='jtable-input-label'>Agent Name </label><input type='text' name='srcAgent'/></strong></div>");
	    	                //var $div = $("<div id='srcdialog' title='Search Agent'><form name ='tudlform1'><TABLE border='0'><TR><TD>" + "<strong><label class='jtable-input-label'>Agent Id:</label></strong></TD><TD><input type='text' id='searchAgentId' name='searchAgentId'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Agent Name:</label></strong></TD><TD><input type='text' id='searchAgentName' name='searchAgentName'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Participant Code </label></strong></TD><TD><input type='text' name='searchPartCode' id='searchPartCode'/></TD></TR></TABLE> </form></div>");
	    	                $( "#exportReportdialog" ).dialog({
	    	                	height: viewportHeight,
	    	            		width: viewportWidth,
	    	            		autoOpen: false,
	    	            		modal: false,
	    	            		resizable: true,
	    	            		 appendTo: dialogContainer,
	    	            		position: {
	    	                		/* my: "left top",
	    	                		at: "left top",
	    	                		of: window,
	    	                		collision: "none" */
	    	            			at: 'center center',
	    	            	        of: dialogContainer
	    	                	},
	    	                	create: function (event, ui) {
	    	            			$(event.target).parent().css('position', 'fixed');
	    	            		},
	    	                        buttons: [
	    	                                  {
	    	                                    text: "export",
	    	                                    id: "exportAgent",
	    	                                    icons: {
	    	                                      primary: "ui-icon-heart"
	    	                                    },
	    	                                    click: function() {
	    	                                      $( this ).dialog( "close" );
	    	                                      exportTransaction();
	    	                                    }
	    	                                  
	    	                                    // Uncommenting the following line would hide the text,
	    	                                    // resulting in the label being used as a tooltip
	    	                                    //showText: false
	    	                                  }
	    	                                ]
	    	                    }); 
	    	                	 $( "#exportReportdialog" ).dialog( "open" );
	    	                }
	    	            },{
	    		            	icon: 'images/refresh.png',
	    		                text: 'Reset Search',
	    		                click: function () {
	    		                    //perform your custom job...
	    		                	//var $div = $("<div id='dialog' title='Search Agent'>" + "<strong><label class='jtable-input-label'>Agent Id </label></strong><input type='text' name='srcAgent'/> <BR><strong><label class='jtable-input-label'>Agent Name </label><input type='text' name='srcAgent'/></strong></div>");
	    		                	ResetTransaction();
	    		                }
	    		            }]
	    	        },
	            actions: {
	                listAction: 'company?action=report_list&method=replist&actionType=JSON'

	            },
	            fields: {
	            	agentID: {
	            		title: 'Agent Id',
	            		 width: '10%',
	                    list: true
	            	},
	            
	                fileName: {
	                    title: 'File Name',
	                    width: '15%',
	                    list: true
	                 
	                },
	                eventDate: {
	                    title: 'Activity Date',
	                    width: '15%',
	                    list: true
	                 
	                },
	          
	                
	                
	                event_type: {
                 		title: 'Activity Type',
                 			width: '20%',
                 				display : function(data) {
                            		if(data.record.event_type!='D'){
                            			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                            			return '<B>Import</B>';
                            		}else{
                            			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                            			return '<B>Download</B>';
                            			
                            		}
                 				}
                     },
	                
	                policyNumber: {
	                    title: 'policyNumber',
	                    width: '15%',
	                    list: true
	                
	                }  ,
	                claimNumber : {
	    				title : 'Claim Number',
	    				width : '15%'
	    			},
	    			lob : {
	     				title : 'LOB',
	     				width : '5%'
	     			},
	     			insuredName : {
	     				title : 'Insured Name',
	     				width : '30%'
	     			},
	     			txnType : {
	     				title : 'Txn Type',
	     				width : '5%'
	     			},
	     			fileType : {
	     				title : 'File Type',
	     				width : '5%'
	     			}
	            }
	        });
	        $('#ReportContainer').jtable('load');
	        checkForClaimActivationOnReport();
	    }


	    );
	 
</script>
<script>
$(function() {
    
	 $( "#dialog-confirm-act" ).dialog({
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
  	 
  	 $( "#dialog-confirm-dact" ).dialog({
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


$(function() {
    $( "#srchReportdialog" ).dialog({
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
      $( "#srchReportdialog" ).dialog( "open" );
    });
  });
  

$(function() {
    $( "#exportReportdialog" ).dialog({
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
      $( "#exportReportdialog" ).dialog( "open" );
    });
  });
function searchTransaction() {
	
	if($('#frmClaimFileDate').val()=='' && $('#toClaimFileDate').val()!=''){
		alert("Please enter File creation From date");
		$( "#srchReportdialog" ).dialog( "open" );
	}
	else if($('#toClaimFileDate').val()=='' && $('#frmClaimFileDate').val()!=''){
		alert("Please enter File creation To date");
		$( "#srchReportdialog" ).dialog( "open" );
	}else if($('#frmClaimEventDate').val()=='' && $('#toClaimEventDate').val()!=''){
		alert("Please enter Activity From date");
		$( "#srchReportdialog" ).dialog( "open" );
	}else if($('#toClaimEventDate').val()=='' && $('#frmClaimEventDate').val()!=''){
		alert("Please enter Activity To date");
		$( "#srchReportdialog" ).dialog( "open" );
	}else {
	$('#ReportContainer').jtable('load', {
		 srchClaimNumber: $('#srchClaimNumber').val(),
		 srchClmPolicyNumber: $('#srchClmPolicyNumber').val(),
		 srchclaimStatus: $('#srchclaimStatus').val(),
		 searchAgentId: $('#searchAgentId').val(),
		 frmClaimFileDate: $('#frmClaimFileDate').val(),
		 toClaimFileDate: $('#toClaimFileDate').val(),
		 frmClaimEventDate: $('#frmClaimEventDate').val(),
		 toClaimEventDate: $('#toClaimEventDate').val()
     });
	}
}


function exportTransaction() {
	
	var url ="";
	var exportStatus = $('#exportStatus').val();
	if($('#exportfrmClaimFileDate').val()=='' && $('#exporttoClaimFileDate').val()!=''){
		alert("Please enter File creation From date");
		$( "#exportReportdialog" ).dialog( "open" );
	}
	else if($('#exporttoClaimFileDate').val()=='' && $('#exportfrmClaimFileDate').val()!=''){
		alert("Please enter File creation To date");
		$( "#exportReportdialog" ).dialog( "open" );
	}else {
		var exportfrmClaimFileDate = $('#exportfrmClaimFileDate').val();
		var exporttoClaimFileDate = $('#exporttoClaimFileDate').val();
		url ="company?action=report_explist&method=exportDailyReport&exportfrmClaimFileDate="+exportfrmClaimFileDate+"&exporttoClaimFileDate="+exporttoClaimFileDate+"&exportStatus="+exportStatus;
		
		
	}
	window.location.href =url;
}
function ResetTransaction() {
	 
	if(document.getElementById('srchClaimNumber'))
		document.getElementById('srchClaimNumber').value ="";
	
	if(document.getElementById('srchClmPolicyNumber'))
		document.getElementById('srchClmPolicyNumber').value =""
	
		if(document.getElementById('srchclaimStatus'))
		document.getElementById('srchclaimStatus').value =""
		
		if(document.getElementById('searchAgentId'))
			document.getElementById('searchAgentId').value =""
				
		if(document.getElementById('frmClaimFileDate'))
			document.getElementById('frmClaimFileDate').value =""		
		
		if(document.getElementById('toClaimFileDate'))
			document.getElementById('toClaimFileDate').value =""
	
	if(document.getElementById('frmClaimEventDate'))
			document.getElementById('frmClaimEventDate').value =""			
	
	if(document.getElementById('toClaimEventDate'))
			document.getElementById('toClaimEventDate').value =""		

				$('#ReportContainer').jtable('load', {
					 srchClaimNumber: $('#srchClaimNumber').val(),
					 srchClmPolicyNumber: $('#srchClmPolicyNumber').val(),
					 srchclaimStatus: $('#srchclaimStatus').val(),
					 searchAgentId: $('#searchAgentId').val(),
					 frmClaimFileDate: $('#frmClaimFileDate').val(),
					 toClaimFileDate: $('#toClaimFileDate').val(),
					 frmClaimEventDate: $('#frmClaimEventDate').val(),
					 toClaimEventDate: $('#toClaimEventDate').val()
			     });
}
  
</script>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
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
// -->
</SCRIPT>
<script>
	$(function() {
		$("#tabs").tabs();
	});
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
      <INPUT type="hidden" name="data_changed" value="N">
    <INPUT type="hidden" name="action_filetype" value="">
    <INPUT type="hidden" name="action_filename" value="">
    <INPUT type="hidden" name="action_filedate" value="">
    <INPUT type="hidden" name="pagever" value="C">
    <INPUT type="hidden" name="transactionIds" id ="transactionIds">
<!-- BEGIN BODY -->


     <!--  <B>Current &amp; Archived Download Files:</B> -->
<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  
  
 
  <%-- <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR> --%>
</TABLE><BR><BR>
<div class="wrapper">
		<div class="container">
			<H3 align="center">Transaction Activity Log Report</H3>
			<div id="ReportContainer"></div>
		</div>
		
	</div>
	
	
		
	<div id="exportReportdialog" title="Export Transaction logs">
 <TABLE border='0'>
			<TR>
				<TD><strong><label class='jtable-input-label'>Export Type:</label></strong></TD>
				<TD>
				<select name='exportStatus' id='exportStatus'>
  					<option value="X">Excel</option>
  					<option value="P">PDF</option>
  					<option value="C">CSV</option>
 					
			</select></TD>
			</TR>
		<TR>
				<TD><strong><label class='jtable-input-label'>File Creation Date:</label></strong></td>
				<td> <input type="text" size ="10" class="datepicker" name ="exportfrmClaimFileDate" id ="exportfrmClaimFileDate"> To 
				<input type="text" size ="10"  class="datepicker" name ="exporttoClaimFileDate" id ="exporttoClaimFileDate"></TD>
			</TR>
						
		</TABLE> 
</div>
	
	
	<div id="srchReportdialog" title="Search Transaction logs">
 <TABLE class ="searchClass" border='0'>
			<TR class ="claimC">
				<TD><strong><label class='jtable-input-label'>Claim Number:   </label></strong></TD>
				<TD><input type='text' id='srchClaimNumber' name='srchClaimNumber'/></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Policy Number:  </label></strong></TD>
				<TD><input type='text' id='srchClmPolicyNumber' name='srchClmPolicyNumber'/></TD>
			</TR>
		
			<TR>
				<TD><strong><label class='jtable-input-label'>File Creation Date:</label></strong></td>
				<td> <input type="text" size ="10" class="datepicker" name ="frmClaimFileDate" id ="frmClaimFileDate"> To 
				<input type="text" size ="10"  class="datepicker" name ="toClaimFileDate" id ="toClaimFileDate"></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Event Log Date:</label></strong></td>
				<td> <input type="text" size ="10" class="datepicker" name ="frmClaimEventDate" id ="frmClaimEventDate"> To 
				<input type="text" size ="10"  class="datepicker" name ="toClaimEventDate" id ="toClaimEventDate"></TD>
			</TR>
			
		</TABLE> 
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
</BODY>
</HTML>
