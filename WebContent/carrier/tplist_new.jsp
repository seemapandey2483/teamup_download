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

<link href="css/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />

<!-- Include jTable script file. -->
<script src="js/jquery-1.8.2.js" type="text/javascript"></script>
<script src="js/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>
<script src="js/jquery.jtable.js" type="text/javascript"></script>

<script src="js/jquery.maskedinput.js" type="text/javascript"></script>

<script type="text/javascript" src="js/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/validationEngine/jquery.validationEngine-en.js"></script>
<script src="js/carrier/tplist.js" type="text/javascript"></script>

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

 div.jtable-column-header-container {height: auto !important;}
 
 #jtable-create-form {
    display: block;
    width: 600px;
    -moz-column-gap:40px;
    -webkit-column-gap:40px;
    column-gap:40px;
    -moz-column-count:2;
    -webkit-column-count:2;
    column-count:2;
}
#jtable-edit-form {
    display: block;
    width: 600px;
    -moz-column-gap:40px;
    -webkit-column-gap:40px;
    column-gap:40px;
    -moz-column-count:2;
    -webkit-column-count:2;
    column-count:2;
}
</style>

<script type="text/javascript">


function ResetAgentSearch() {
	 
	
	document.getElementById('searchAgentId').value ="";
	
	document.getElementById('searchAgentName').value =""
	 document.tudlform.action.value = 'tplist_default_new';
	<%
	 session.removeAttribute("searchAgentId");
	 %> 
	document.tudlform.submit();
	 
}

function searchAgentKeyPress(e)
{
    // look for window.event in case event isn't passed in
    e = e || window.event;
    if (e.keyCode == 13)
    {
    	searchAgentRecord();
    	$( "#srchAgentdialog" ).dialog( "close" );
        return false;
    }
    return true;
}

function searchAgentRecord() {
	$('#AgentContainer').jtable('load', {
		 searchAgentId: $('#searchAgentId').val(),
		 searchAgentName: $('#searchAgentName').val()
		 //searchPartCode: $('#searchPartCode').val()
     });
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
	 
	$( "#srchAgentdialog" ).dialog({
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
 
    $( "#opener" ).click(function() {
      $( "#srchAgentdialog" ).dialog( "open" );
    });
  });



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
<script type="text/javascript">
	$(function(){
		$('.btnClick').click(function(){
			var $selectedRows = $('#PolicyFileInfo').jtable('selectedRows');
			if ($selectedRows.length > 0) {
            	var ids = [];
            	var agentId ;
		      	var fileName ;
		      	var createdDate;
			      	$selectedRows.each(function (index,Element) {
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
              			  $('#PolicyFileInfo').jtable('load');
              		  }
              		});
            		$('#PolicyFileInfo').jtable('load');
            	} else{
            		$.ajax({
              		  url: "Controller?action=update&actionValue="+$('.fileAction').val()+"&ids="+idsjoined,
              		  cache: false,
              		  success: function(result){
              			  $('#PolicyFileInfo').jtable('load');
              		  }
              		});
            		$('#PolicyFileInfo').jtable('load');
            	}
            }
		})
	})
	
	$('a.new_window').live('click', function(event) {
 
       /*  var url = $(this).attr("href");
        var windowName = $(this).attr("name");
        var h = 750;
        var w = 1000;
        var left = (window.innerWidth) ? (window.innerWidth - w) / 2 : 0;
        var top = (window.innerHeight) ? (window.innerHeight - h) / 2 : 0;
        var windowSize = 'height=' + h + ',width=' + w + ',top=' + top + ',left=' + left + ',scrollbars=yes,resizable=yes,location=no';
 
        //make sure the windowName does not have spaces! IE does not like it.
        var newwindow = window.open(url, windowName, windowSize);
        if (window.focus) {
              //Note IE6 hack! Must check to see if newwindow has been populated.
              if (newwindow) {
                    newwindow.focus();
              }
        }
        //make sure the current page does not also go to the url.
        //NOTE IE 6 hack here to check for preventDefault. Otherwise return false.
        if (event.preventDefault) {
            event.preventDefault();
        } else {
            event.returnValue = false;
        } */
		//window.open("tianjia.jsp","","height=250,width=400,status=no,location=no,toolbar=no,directories=no,menubar=no");
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
    <input type ="hidden" name ="keyLinkFileId" id ="keyLinkFileId" value ="<%= request.getAttribute("keyLinkFile")%>">
    <input type ="hidden" name ="srcAgentName" id ="srcAgentName">
    <input type ="hidden" name ="srcPartCode" id ="srcPartCode" >
	<INPUT type="hidden" name="transactionIds" id ="transactionIds">
	
	
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
  </TABLE>
  <H3 align="center">TEAM-UP Download Agent Listing</H3>
    <div class="wrapper">
		<div class="container">
<br>		
		<div id="AgentContainer"></div>
		
		</div>
	</div>
	<div>



<div id="srchAgentdialog" title="Search Trading Partner">
 <TABLE border='0'>
			<TR>
				<TD><strong><label class='jtable-input-label'>Agent Id:</label></strong></TD>
				<TD><input type='text' id='searchAgentId' name='searchAgentId'  value ="<%=(session.getAttribute("searchAgentId")!=null && !"null".equals(session.getAttribute("searchAgentId")))?session.getAttribute("searchAgentId"):"" %>" onkeypress='searchAgentKeyPress(event);' /></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Agent Name:</label></strong></TD>
				<TD><input type='text' id='searchAgentName' name='searchAgentName' onkeypress='searchAgentKeyPress(event);'/></TD>
			</TR>
		</TABLE> 
</div>

<div id="dialog-confirm" title="Delete Participants?">
  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Particpants will be deleted.Are you sure?</p>
</div>
	<table width="100%" align="center">
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>


          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
</TABLE>		
	</div>
<%@ include file="footer.jsp" %>
</body>
</HTML>
