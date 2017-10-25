<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html>
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<meta charset="UTF-8">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.TradingPartnerListDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<link rel="stylesheet" href="css/jquery-ui.css">
<link rel="stylesheet" href="css/style.css">
<!-- Include one of jTable styles. -->
<link href="css/lightcolor/blue/jtable.css" rel="stylesheet" type="text/css" />
<link href="css/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" />


<!-- Include jTable script file. -->
<script src="js/jquery-1.8.2.js" type="text/javascript"></script>
<script src="js/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>
<script src="js/jquery.jtable.js" type="text/javascript"></script>
<script src="js/chart/Chart.js" type="text/javascript"></script>
<style type="text/css">

#mycanvasrepot {
  height: 450px;
  width: 600px;
}
	table.table-style-three {
		font-family: verdana, arial, sans-serif;
		font-size: 11px;
		color: #333333;
		border-width: 1px;
		border-color: #3A3A3A;
		border-collapse: collapse;
	}
	table.table-style-three th {
		border-width: 1px;
		padding: 8px;
		border-style: solid;
		border-color: #FFA6A6;
		background-color: #D56A6A;
		color: #ffffff;
	}
	table.table-style-three tr:hover td {
		cursor: pointer;
	}
	table.table-style-three tr:nth-child(even) td{
		background-color: #F7CFCF;
	}
	table.table-style-three td {
		border-width: 1px;
		padding: 8px;
		border-style: solid;
		border-color: #FFA6A6;
		background-color: #ffffff;
	}
</style>

<script>
var transChart=null;
var lobBarChart=null;
var lobPieChart=null;
var fileChart=null;
var eventChart=null;

 function getURL(method){
	 var url ="company?action=chartList&actionType=JSON&method="+method+"&reportType=" +document.getElementById("srchReportStatus").value;
	
	 if(document.getElementById("frmReportDate").value!= "" && document.getElementById("toReportDate").value!= "" ) {
				url= url.concat( "&frmReportDate="+document.getElementById("frmReportDate").value);
			 url=url.concat( "&toReportDate="+document.getElementById("toReportDate").value);
	}
	if(document.getElementById("cAgentId").value!= "" ) {
		url= url.concat( "&cAgentId="+document.getElementById("cAgentId").value);
	}
	if(document.getElementById("eventType").value!= "" ) {
		url= url.concat( "&eventType="+document.getElementById("eventType").value);
	}
	 return url;
 }
function showTransactionChart() {
	
	$.ajax({
        url: getURL("LoBDistributionReport"),
		method: "GET",
		async: false, 
		success: function(data) {
			console.log(data);
			var  lob = [];
			var  txn = [];
			var datalist = [];
			var count ;
			var datasetValue =[];
		
			for(var i in data) {
			
				if(data[i].lob && data[i].lob !=null){
					lob.push(data[i].lob);
				}
				if(data[i].txn && data[i].txn !=null){
						txn.push(data[i].txn);
				}
				if(data[i].dataset && data[i].dataset !=null){
					dataset=  data[i].dataset;
				}
				if(data[i].datalist && data[i].datalist !=null){
					datalist.push( data[i].datalist);
				}
				if(data[i].count && data[i].count !=null){
			
					count= data[i].count;
				}
			};
			
			for(var i=0;i<count;i++){
		
				var data = datalist[i].split(",");
		
				datasetValue[i]={
						label : txn[i],
						fillColor : 'rgba(' + randomColorFactor() + ',' + randomColorFactor() + ',' + randomColorFactor() + ',.7)',
						backgroundColor : 'rgba(' + randomColorFactor() + ',' + randomColorFactor() + ',' + randomColorFactor() + ',.7)',
						strokeColor :'rgba(' + randomColorFactor() + ',' + randomColorFactor() + ',' + randomColorFactor() + ',.7)',
						title :'2013',
						data : data
				}
			};
		
		
		
			var chartdata = {
				labels: lob,
				datasets : datasetValue
			};

		
			createBarChart(chartdata);
			
		},
		error: function(data) {
			console.log(data);
		}
	}); 
	
	}

function showFileChart() {
	$.ajax({
        url: getURL("FileReportCount"),
		method: "GET",
		async: false, 
		success: function(data) {
			console.log(data);
			var type = [];
			var count = [];
			var color =[];
			for(var i in data) {
				type.push(data[i].type);
				count.push(data[i].count);
				color.push(data[i].color);
			}

			var chartdata = {
				labels: type,
				datasets : [
					{
						label: 'File Transactions count',
						backgroundColor: color,
						borderColor: 'rgba(200, 200, 200, 0.75)',
						hoverBackgroundColor: 'rgba(200, 200, 200, 1)',
						hoverBorderColor: 'rgba(200, 200, 200, 1)',
						data: count,
						borderWidth: 1
					}
				]
			};

			
	
			
			if(window.fileChart!=null){
					window.fileChart.destroy();

		    }
			var FileReportCount = document.getElementById("FileReportCount");
			var FileReportCountContext = FileReportCount.getContext("2d");
			window.fileChart = new Chart(FileReportCountContext, {
				type: 'doughnut',
				animation:{
			        animateScale:true
			    },
				data: chartdata,
				 options: {
				        
			title: {
	            display: true,
	            text: 'File Transaction Counts'
	        },
	        legend: {
	            display: true,
	            labels: {
	                fontColor: 'rgb(255, 99, 132)'
	            }
	        }
				    }
			});
		},
		error: function(data) {
			console.log(data);
		}
	});
	
	
	
	}
	

/* function showLobPieChart() {
	 $.ajax({
        url: getURL("LoBReprtCount"),
		method: "GET",
		async: false, 
		success: function(data) {
			console.log(data);
			var lob = [];
			var count = [];
			var color =[];
			for(var i in data) {
				lob.push(data[i].lob);
				count.push(data[i].count);
				color.push(data[i].color);
			}

			var chartdata = {
				labels: lob,
				datasets : [
					{
						label: 'Line of Business Count',
						backgroundColor: color,
						borderColor: 'rgba(200, 200, 200, 0.75)',
						hoverBackgroundColor: 'rgba(200, 200, 200, 1)',
						hoverBorderColor: 'rgba(200, 200, 200, 1)',
						data: count,
						borderWidth: 1
					}
				]
			};

		
			
			if(window.lobPieChart!=null){
				window.lobPieChart.destroy();
			  }
			var mycanvas4 = document.getElementById("mycanvas4");
			var mycontext4 = mycanvas4.getContext("2d");
			window.lobPieChart = new Chart(mycontext4, {
				type: 'pie',
				data: chartdata
				 
			});
		},
		error: function(data) {
			console.log(data);
		}
	});
	
	
	
	
	} */
	

function showLobChart() {
	
	$.ajax({
        url: getURL("LoBReprtCount"),
		method: "GET",
		async: false, 
		success: function(data) {
			console.log(data);
			var lob = [];
			var count = [];
			var color =[];
			for(var i in data) {
				lob.push(data[i].lob);
				count.push(data[i].count);
				color.push(data[i].color);
			}

			var chartdata = {
				labels: lob,
				datasets : [
					{
						label: '',
						backgroundColor: color,
						borderColor: 'rgba(200, 200, 200, 0.75)',
						hoverBackgroundColor: 'rgba(200, 200, 200, 1)',
						hoverBorderColor: 'rgba(200, 200, 200, 1)',
						data: count,
						borderWidth: 1
					}
				]
			};
			
			if(window.lobBarChart!=null){
					window.lobBarChart.destroy();

			  }
			var LoBReprtCount = document.getElementById("LoBReprtCount");
			var mycontext = LoBReprtCount.getContext("2d");
			window.lobBarChart = new Chart(mycontext, {
				type: 'bar',
				data: chartdata,
				 options: {
	                    // Elements options apply to all of the options unless overridden in a dataset
	                    // In this case, we are setting the border of each bar to be 2px wide and green
	                    scaleBeginAtZero : true,
	                    scaleShowLabels : false,
	                    scaleShowLabelBackdrop : false,
	                    elements: {
	                        rectangle: {
	                            borderWidth: 2,
	                            borderColor: 'rgb(0, 255, 0)',
	                            borderSkipped: 'bottom'
	                        }
	                    },
	                    responsive: true,
	                    legend: {
	                        position: 'top',
	                    },
	                    title: {
	                        display: true,
	                        text: 'Line of Business Transaction counts'
	                    },
	                    hover: {
	                        mode: 'dataset'
	                    }
	                }
			});
		},
		error: function(data) {
			console.log(data);
		}
	});
	
	
	
	}

function addTable(barChartData) {
	 $('#LoBReprtCountDiv').append(  '<table>' );
	var content = "<table allign ='center' height='450' width='600' class ='table-style-three'>";
	 var lables =barChartData.labels;
	 content += '<tr><th>Transaction Type</th>' ;

	 for(var i =0;i<lables.length;i++){
		var lob = lables[i];
		content += '<th>'+lob+'</th>' ;
	} 
	 content += '</tr>' ;

	var datasets = barChartData.datasets;

	 for(var i =0;i<datasets.length;i++){
		var data = datasets[i].data
		var txn  = datasets[i].label;
		var color =datasets[i].backgroundColor;
		var fillColor =datasets[i].fillColor;
		 content += '</tr>' ;
		 content += '<td> '+txn+'</td>' ;
		for(var j in data) {
			 content += '<td>'+ data[j]+'</td>' ;
		}
		
		 content += '</tr>' ;
	} 
	 content += '</table>' ;
	$('#LoBReprtCountDiv').append(  content);
}
function createBarChart(barChartData) {
    
	
	if(window.transChart!=null){
		window.transChart.destroy();
	  }
	var LoBDistributionReport = document.getElementById("LoBDistributionReport");
	var LoBDistContext = LoBDistributionReport.getContext("2d");
	//addTable(barChartData);
	window.transChart = new Chart(LoBDistContext, {
        type: 'bar',
        data: barChartData,
        options: {
            title:{
                display:true,
                text:"Line of Buinsess distribution"
            },
            tooltips: {
                mode: 'label'
            },
            responsive: true,
            scales: {
                xAxes: [{
                    stacked: false,
                }],
                yAxes: [{
                    stacked: false
                }]
            }
        }
    });

}
function randomScalingFactor() {
	return (Math.random() > 0.5 ? 1.0 : 2.0) * Math.round(Math.random() * 1000);
}
function randomColorFactor() {
	return Math.round(Math.random() * 255);
}

function showEventChart() {
	$.ajax({
        url: getURL("EventeReportCount"),
		method: "GET",
		async: false, 
		success: function(data) {
			console.log(data);
			var type = [];
			var count = [];
			var color =[];
			for(var i in data) {
				type.push(data[i].type);
				count.push(data[i].count);
				color.push(data[i].color);
			}

			var chartdata = {
				labels: type,
				datasets : [
					{
						label: 'Files Event/Activity count',
						backgroundColor: color,
						borderColor: 'rgba(200, 200, 200, 0.75)',
						hoverBackgroundColor: 'rgba(200, 200, 200, 1)',
						hoverBorderColor: 'rgba(200, 200, 200, 1)',
						data: count,
						borderWidth: 1
					}
				]
			};

			
	
			
			if(window.eventChart!=null){
					window.eventChart.destroy();

		    }
			var EventReprtCount = document.getElementById("EventReprtCount");
			var EventReprtCountContext = EventReprtCount.getContext("2d");
			window.eventChart = new Chart(EventReprtCountContext, {
				type: 'doughnut',
				animation:{
			        animateScale:true
			    },
				data: chartdata,
				 options: {
				        
			title: {
	            display: true,
	            text: 'File Event Counts'
	        },
	        legend: {
	            display: true,
	            labels: {
	                fontColor: 'rgb(255, 99, 132)'
	            }
	        }
				    }
			});
		},
		error: function(data) {
			console.log(data);
		}
	});
	
	
	
	}

function showAnotherChart() {
	showLobChart();
	//showLobPieChart();
	showEventChart();
	showFileChart();
	showTransactionChart();
	
}
	
$(function() {
    $( ".datepicker" ).datepicker();
  });
</script>

<style type="text/css">
			#chart-container {
				width: 740px;
				height: auto;
			}
</style>
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

</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="return showAnotherChart();">
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


<!-- BEGIN BODY -->


    </FORM>
    </TD>
  </TR>
  
  
 

</TABLE><BR><BR>

	
	<div id="srchClaimdialog" title="Search claim files">
 		<TABLE border='0'>
			<TR>
				<TD><strong><label class='jtable-input-label'>Agent Id:</label></strong></td>
				<td> <input type="text" size ="10" name ="cAgentId" id ="cAgentId"></TD>
				<TD><strong><label class='jtable-input-label'>Report Frequency:</label></strong></TD>
				<TD>
				<select name='srchReportStatus' id='srchReportStatus' onchange="showAnotherChart()">
  					<option value="">ALL</option>
  					<option value="D">Daily</option>
  					<option value="W">Weekly</option>
  					<option value="M">Monthly</option>
  					<option value="Y">Yearly</option>
  					
			</select></TD>
			</TR>
			
			<TR>
				<TD><strong><label class='jtable-input-label'>Event Type:</label></strong></TD>
				<TD>
				<select name='eventType' id='eventType' onchange="showAnotherChart()">
  					<option value="D">Download</option>
  					<option value="I">Import</option>
  					
			</select></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Event Date:</label></strong></td>
				<td> <input type="text" size ="10" class="datepicker" name ="frmReportDate" id ="frmReportDate"> 
				To <input type="text" size ="10"  class="datepicker" name ="toReportDate" id ="toReportDate" onchange="showAnotherChart()"> <button id="viewReport" onclick="showAnotherChart()">Go</button></TD>
			</TR>
			
		</TABLE> 
</div>

	<div class="container">
		<div id ="LoBReprtCountDiv" style="width: 40% ; float:right" >
        		<canvas id="LoBReprtCount" height="450" width="600"></canvas><!-- LoBReprtCount -->
   			 </div>
	</div>
    <div class="container">
    	<div id ="LoBDistributionReportDiv" style="width: 40%; float:left">
        	<canvas id="LoBDistributionReport" height="450" width="600"></canvas><!-- LoBDistributionReport -->
     	</div>	
    </div>
    <br>
    
    <div class="container">
		<div id ="FileReportCountDiv" style="width: 40% ; float:right" >
        		<canvas id="FileReportCount" height="450" width="600"></canvas><!-- FileReportCount -->
   		</div>
	</div>
    <div class="container">
    	<div id ="EventReprtCountDiv" style="width: 40%; float:left">
       		 <canvas id="EventReprtCount" height="450" width="600"></canvas><!-- EventReprtCount -->
    	</div>
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
