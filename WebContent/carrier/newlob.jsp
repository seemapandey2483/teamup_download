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

<script type="text/javascript" src="js/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/validationEngine/jquery.validationEngine-en.js"></script>

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

	    $(document).ready(function (data) {
	    	$('#lineOfBusiness').jtable({
	            title: 'LOB',
	            sorting: true,
	            selecting: true,
	    		multiselect: true,
	    		selectingCheckboxes: true,
	    		 toolbar: {
	    	            items: [{
	    	               // icon: 'images/printer.png',
	    	                text: 'Activate',
	    	                click: function (data) {
			                    //perform your custom job...
			                	var $selectedRows = $('#lineOfBusiness').jtable('selectedRows');
			                	 var ids = [];
			                	 if ($selectedRows.length > 0) {
			                           	$selectedRows.each(function (index,Element) {
			   				      		ids.push(($(this).data('record').id));
			   				      	});
			   	            	var idsjoined = ids.join();
			   	            	
			   	            	$( "#dialog-confirm-act" ).dialog( "open" );
			   	            	$( "#dialog-confirm-act" ).dialog({
			   	            		resizable: false,
			   	            		height:140,
			   	            		modal: true,
			   	            		buttons: {
			   	            			"Activate": function() {
			   	            				$( this ).dialog( "close" );
			   	            					$.ajax({
			   	            						url: "company?action=loblist&method=updateActive&actionType=JSON&active=Y&ids="+idsjoined,
			   	            						cache: false,
			   	            						success: function(result){
			   	            							$('.jtable-row-selected').removeClass('jtable-row-selected');
			   	            							$('#lineOfBusiness').jtable('reload');
			   	            							}
			   	            					});
			   	            			},
			   	            			Cancel: function() {
			   	            				$( this ).dialog( "close" );
			   	            				}
			   	            		}
			   	            	});

			   	            }else{
			   	           		var $div = $("<div id='dialog' title='Message'>" + "<strong><label class='jtable-input-label'> No Lineofbusiness marked for activation </label></strong> </div>");
			   	           		$div.dialog({
			   	           			modal : true
			   	           		});
			   	            }
			                
			              }
	    	            },{
	    	            	//icon: 'images/find.png',
	    	                text: 'Deactivate',
	    	                click: function (data) {
			                    //perform your custom job...
			                	var $selectedRows = $('#lineOfBusiness').jtable('selectedRows');
			                	 var ids = [];
			                	 if ($selectedRows.length > 0) {
			                	
			   				      	$selectedRows.each(function (index,Element) {
			   				      		ids.push($(this).data('record').id);
			   				      	});
			   	            	var idsjoined = ids.join();
			   	            	
			   	            	$( "#dialog-confirm-dact" ).dialog( "open" );
			   	            	$( "#dialog-confirm-dact" ).dialog({
			   	            		resizable: false,
			   	            		height:140,
			   	            		modal: true,
			   	            		buttons: {
			   	            			"Deactivate": function() {
			   	            				$( this ).dialog( "close" );
			   	            					$.ajax({
			   	            						url: "company?action=loblist&method=updateActive&actionType=JSON&active=N&ids="+idsjoined,
			   	            						cache: false,
			   	            						success: function(result){
			   	            							$('.jtable-row-selected').removeClass('jtable-row-selected');
			   	            							$('#lineOfBusiness').jtable('reload');
			   	            							}
			   	            					});
			   	            			},
			   	            			Cancel: function() {
			   	            				$( this ).dialog( "close" );
			   	            				}
			   	            		}
			   	            	});

			   	            }else{
			   	           		var $div = $("<div id='dialog' title='Message'>" + "<strong><label class='jtable-input-label'> No Lineofbusiness marked for deactivation </label></strong> </div>");
			   	           		$div.dialog({
			   	           			modal : true
			   	           		});
			   	            }
			                
			              }
	    	            }]
	    	        },
	            actions: {
	                listAction: 'company?action=loblist&method=lobList&actionType=JSON',
	                createAction:'company?action=loblist&method=lobCreate&actionType=JSON',
	                updateAction: 'company?action=loblist&method=lobUpdate&actionType=JSON',
	                deleteAction: 'company?action=loblist&method=lobDelete&actionType=JSON'
	            },
	            fields: {
	            	id: {
	            		key: true,
	                    list: false
	            	},
	              /*   name: {
	                    title: 'Name',
	                    width: '30%',
	                    edit:true,
	                    create:true,
	                    list: false,
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.name+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.name;
                    			
                    		}
         				}
	                }, */
	                code: {
	                    title: 'Code',
	                    width: '30%',
	                    edit:true,
	                    create : true,
	                    inputClass: 'validate[required]',
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.code+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.code;
                    			
                    		}
         				}
	                },
	               /*  type: {
	                    title: 'Type',
	                    width: '20%',
	                    edit: false,
	                    create: false,
	                    list: false,
	                    options: { 'P': 'Personal'},
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.type+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.type;
                    			
                    		}
         				}
	                }, */
	                description: {
	                    title: 'Description',
	                    width: '70%',
	                    edit: true,
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.description+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.description;
                    			
                    		}
         				}
	                }  ,
	                 active: {
	                    title: 'active',
	                    width: '20%',
	                    edit: true,
	                    type: 'checkbox',
	                    list: false,
	                    values: { 'false': 'Inactive', 'true': 'Active' },
	                    defaultValue: 'true',
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.active+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return '<B>'+data.record.active+'</B>';
                    			
                    		}
         				}
	                }  
	            },
	            recordDeleted: function (event, data) {
	            },
	            recordUpdated:function(event, data){
	            },
	            recordAdded: function (event, data) {
	            },
	            //Initialize validation logic when a form is created
	            formCreated: function (event, data) {
	                 data.form.validationEngine();
	                 if(data.formType=='create') {
	                	 data.form.find('[name=code]').addClass('validate[required,ajax[ajaxCheckExistingLOB]]');	
                 	}
	                 if(data.formType=='edit') {
                         $('#Edit-code').prop('readonly', true);
                         $('#Edit-code').addClass('jtable-input-readonly');
                     }
	            },
	             //Validate form when it is being submitted
	             formSubmitting: function (event, data) {
	            	 if (data.formType == 'create') {
	            		 var lobCode = data.form.find('[name=code]').val();
	            		 if (lobCode == '') {
	             			return data.form.validationEngine('validate');
	             		}
	            	 }
	            	 //return data.form.validationEngine('validate');
	             },
	             //Dispose validation logic when form is closed
	             formClosed: function (event, data) {
	                 data.form.validationEngine('hide');
	                 data.form.validationEngine('detach');
	             }
	        });
	        $('#lineOfBusiness').jtable('load');
	    });
	   /*  $(document).ready(function () {
	        $('#cLineOfBusiness').jtable({
	            title: 'LOB',
	            sorting: true,
	            selecting: true,
	    		multiselect: true,
	    		selectingCheckboxes: true,
	    		 toolbar: {
	    	            items: [{
	    	               // icon: 'images/printer.png',
	    	                text: 'Activate',
	    	                click: function (data) {
			                    //perform your custom job...
			                	var $selectedRowsP = $('#cLineOfBusiness').jtable('selectedRows');
			                	 var ids = [];
			                	 if ($selectedRowsP.length > 0) {
			                           	$selectedRowsP.each(function (index,Element) {
			   				      		ids.push(($(this).data('record').id));
			   				      	});
			   	            	var idsjoined = ids.join();
			   	            	
			   	            	$( "#dialog-confirm-act" ).dialog( "open" );
			   	            	$( "#dialog-confirm-act" ).dialog({
			   	            		resizable: false,
			   	            		height:140,
			   	            		modal: true,
			   	            		buttons: {
			   	            			"Activate": function() {
			   	            				$( this ).dialog( "close" );
			   	            					$.ajax({
			   	            						url: "LOBController?action=updateActive&active=Y&ids="+idsjoined,
			   	            						cache: false,
			   	            						success: function(result){
			   	            							$('.jtable-row-selected').removeClass('jtable-row-selected');
			   	            							$('#cLineOfBusiness').jtable('reload');
			   	            							}
			   	            					});
			   	            			},
			   	            			Cancel: function() {
			   	            				$( this ).dialog( "close" );
			   	            				}
			   	            		}
			   	            	});

			   	            }else{
			   	           		var $div = $("<div id='dialog' title='Message'>" + "<strong><label class='jtable-input-label'>      No Lineofbusiness marked for activation </label></strong> </div>");
			   	           		$div.dialog({
			   	           			modal : true
			   	           		});
			   	            }
			                
			              }
	    	            },{
	    	            	//icon: 'images/find.png',
	    	                text: 'Deactivate',
	    	                click: function (data) {
			                    //perform your custom job...
			                	var $selectedRowsP = $('#cLineOfBusiness').jtable('selectedRows');
			                	 var ids = [];
			                	 if ($selectedRowsP.length > 0) {
			                	
			   				      	$selectedRowsP.each(function (index,Element) {
			   				      		ids.push($(this).data('record').id);
			   				      	});
			   	            	var idsjoined = ids.join();
			   	            	
			   	            	$( "#dialog-confirm-dact" ).dialog( "open" );
			   	            	$( "#dialog-confirm-dact" ).dialog({
			   	            		resizable: false,
			   	            		height:140,
			   	            		modal: true,
			   	            		buttons: {
			   	            			"Deactivate": function() {
			   	            				$( this ).dialog( "close" );
			   	            					$.ajax({
			   	            						url: "LOBController?action=updateActive&active=N&ids="+idsjoined,
			   	            						cache: false,
			   	            						success: function(result){
			   	            							$('.jtable-row-selected').removeClass('jtable-row-selected');
			   	            							$('#cLineOfBusiness').jtable('reload');
			   	            							}
			   	            					});
			   	            			},
			   	            			Cancel: function() {
			   	            				$( this ).dialog( "close" );
			   	            				}
			   	            		}
			   	            	});

			   	            }else{
			   	           		var $div = $("<div id='dialog' title='Message'>" + "<strong><label class='jtable-input-label'>     No Lineofbusiness marked for deactivation </label></strong> </div>");
			   	           		$div.dialog({
			   	           			modal : true
			   	           		});
			   	            }
			                
			              }
	    	            }]
	    	        },
	            actions: {
	                listAction: 'LOBController?action=cLobList',
	                createAction:'LOBController?action=cLobCreate',
	                updateAction: 'LOBController?action=cLobUpdate',
	                deleteAction: 'LOBController?action=cLobDelete'
	            },
	            fields: {
	            	id: {
	            		key: true,
	                    list: false
	            	},
	                name: {
	                    title: 'Name',
	                    key: true,
	                    width: '30%',
	                    edit:true,
	                    create:true,
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.name+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.name;
                    			
                    		}
         				}
	                },
	                code: {
	                    title: 'Code',
	                    width: '30%',
	                    edit:true,
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.code+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.code;
                    			
                    		}
         				}
	                },
	                type: {
	                    title: 'Type',
	                    width: '20%',
	                    edit: true,
	                    options: { 'C': 'Commercial Lines'},
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.type+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.type;
                    			
                    		}
         				}
	                },
	                description: {
	                    title: 'Description',
	                    width: '20%',
	                    edit: true,
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.description+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.description;
                    			
                    		}
         				}
	                } ,
	                active: {
	                    title: 'active',
	                    width: '20%',
	                    edit: false,
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.active+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return '<B>'+data.record.active+'</B>';
                    			
                    		}
         				}
	                }
	            }
	        });
	        $('#cLineOfBusiness').jtable('load');
	    }); */
	   /*  $(document).ready(function () {
	        $('#oLineOfBusiness').jtable({
	            title: 'LOB',
	            sorting: true,
	            selecting: true,
	    		multiselect: true,
	    		selectingCheckboxes: true,
	    		 toolbar: {
	    	            items: [{
	    	               // icon: 'images/printer.png',
	    	                text: 'Activate',
	    	                click: function (data) {
			                    //perform your custom job...
			                	var $selectedRowsP = $('#oLineOfBusiness').jtable('selectedRows');
			                	 var ids = [];
			                	 if ($selectedRowsP.length > 0) {
			                           	$selectedRowsP.each(function (index,Element) {
			   				      		ids.push(($(this).data('record').id));
			   				      	});
			   	            	var idsjoined = ids.join();
			   	            	
			   	            	$( "#dialog-confirm-act" ).dialog( "open" );
			   	            	$( "#dialog-confirm-act" ).dialog({
			   	            		resizable: false,
			   	            		height:140,
			   	            		modal: true,
			   	            		buttons: {
			   	            			"Activate": function() {
			   	            				$( this ).dialog( "close" );
			   	            					$.ajax({
			   	            						url: "LOBController?action=updateActive&active=Y&ids="+idsjoined,
			   	            						cache: false,
			   	            						success: function(result){
			   	            							$('.jtable-row-selected').removeClass('jtable-row-selected');
			   	            							$('#oLineOfBusiness').jtable('reload');
			   	            							}
			   	            					});
			   	            			},
			   	            			Cancel: function() {
			   	            				$( this ).dialog( "close" );
			   	            				}
			   	            		}
			   	            	});

			   	            }else{
			   	           		var $div = $("<div id='dialog' title='Message'>" + "<strong><label class='jtable-input-label'>      No Lineofbusiness marked for activation </label></strong> </div>");
			   	           		$div.dialog({
			   	           			modal : true
			   	           		});
			   	            }
			                
			              }
	    	            },{
	    	            	//icon: 'images/find.png',
	    	                text: 'Deactivate',
	    	                click: function (data) {
			                    //perform your custom job...
			                	var $selectedRowsP = $('#oLineOfBusiness').jtable('selectedRows');
			                	 var ids = [];
			                	 if ($selectedRowsP.length > 0) {
			                	
			   				      	$selectedRowsP.each(function (index,Element) {
			   				      		ids.push($(this).data('record').id);
			   				      	});
			   	            	var idsjoined = ids.join();
			   	            	
			   	            	$( "#dialog-confirm-dact" ).dialog( "open" );
			   	            	$( "#dialog-confirm-dact" ).dialog({
			   	            		resizable: false,
			   	            		height:140,
			   	            		modal: true,
			   	            		buttons: {
			   	            			"Deactivate": function() {
			   	            				$( this ).dialog( "close" );
			   	            					$.ajax({
			   	            						url: "LOBController?action=updateActive&active=N&ids="+idsjoined,
			   	            						cache: false,
			   	            						success: function(result){
			   	            							$('.jtable-row-selected').removeClass('jtable-row-selected');
			   	            							$('#oLineOfBusiness').jtable('reload');
			   	            							}
			   	            					});
			   	            			},
			   	            			Cancel: function() {
			   	            				$( this ).dialog( "close" );
			   	            				}
			   	            		}
			   	            	});

			   	            }else{
			   	           		var $div = $("<div id='dialog' title='Message'>" + "<strong><label class='jtable-input-label'>     No Lineofbusiness marked for deactivation </label></strong> </div>");
			   	           		$div.dialog({
			   	           			modal : true
			   	           		});
			   	            }
			                
			              }
	    	            }]
	    	        },
	            actions: {
	                listAction: 'LOBController?action=oLobList',
	                createAction:'LOBController?action=oLobCreate',
	                updateAction: 'LOBController?action=oLobUpdate',
	                deleteAction: 'LOBController?action=oLobDelete'
	            },
	            fields: {
	            	id: {
	            		key: true,
	                    list: false
	            	},
	                name: {
	                    title: 'Name',
	                    width: '30%',
	                    edit:true,
	                    create:true,
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.name+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.name;
                    			
                    		}
         				}
	                },
	                code: {
	                    title: 'Code',
	                    width: '30%',
	                    edit:true,
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.code+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.code;
                    			
                    		}
         				}
	                },
	                type: {
	                    title: 'Type',
	                    width: '20%',
	                    edit: true,
	                    options: { 'O': 'Other'},
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.type+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.type;
                    			
                    		}
         				}
	                },
	                description: {
	                    title: 'Description',
	                    width: '20%',
	                    edit: true,
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.description+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return data.record.description;
                    			
                    		}
         				}
	                } ,
	                active: {
	                    title: 'active',
	                    width: '20%',
	                    edit: false,
	                    display : function(data) {
                    		if(data.record.active==false){
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                    			return '<B><font color="red">'+data.record.active+'</font></B>';
                    		}else{
                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                    			return '<B>'+data.record.active+'</B>';
                    			
                    		}
         				}
	                }
	            }
	        });
	        $('#oLineOfBusiness').jtable('load');
	    }); */
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

<center>&nbsp;<BR>&nbsp;<font color="RED">
		<c:if test="${not empty errorMessage}">
			*<c:out value="${errorMessage}"/>
		</c:if></font>
		<c:remove var="errorMessage" scope="session" />
		<c:remove var="agentId" scope="session" />
		<c:remove var="agentinfo" scope="session" />
	</center>
<div class="wrapper">
		<div class="container">
			<H3 align="center">Line Of Business Download Maintenance</H3>
			<div id="lineOfBusiness"></div>
			<!-- <div id="tabs">
				<H3 align="center">Line Of Business Download Maintenance</H3>
				<ul>
					<li><a href="#personalLines">Personal Lines</a></li>
					<li><a href="#commercialLines">Commercial Lines</a></li>
					<li><a href="#other">Other</a></li>
				</ul>
				<div id="personalLines" style="width: 96%;">
					<div id="LineOfBusiness"></div>
				</div>
				<div id="commercialLines" style="width: 96%;">
					<div id="cLineOfBusiness"></div>
				</div>
				<div id="other" style="width: 96%;">
					<div id="oLineOfBusiness"></div>
				</div>
			</div> -->
		</div>
		
	</div>
	
	<div id="dialog-confirm-act" title="Activate Lineofbusiness?">
  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Selected Lineofbusiness will be activated for all agents.Are you sure?</p>
</div>
<div id="dialog-confirm-dact" title="Deactivate Lineofbusiness?">
  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Selected Lineofbusiness will be deactivated for all agents.Are you sure?</p>
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
