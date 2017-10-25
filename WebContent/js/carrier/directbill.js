$(document).ready(function() {
		$('#DirectBillFileInfo').jtable({
			title : 'Direct Bill Download Files',
            paging: true,
	        selecting: true,
	        pageSize : 25,
	        multiselect: true,
	        selectingCheckboxes: true,
	        sorting: true,
            defaultSorting: 'created_dt DESC',
	        selectOnRowClick: false,
	        toolbar: {
	            items: [{
	                icon: 'images/printer.png',
	                text: 'print',
	                click: function () {
	                    //perform your custom job...
	                	var divToPrint = document.getElementById('DirectBillFileInfo');
			        	newWin = window.open("");
			        	newWin.document.write(divToPrint.outerHTML);
			        	newWin.print();
			        	newWin.close();
	                }
	            },{
	                icon: 'images/DeleteRed.png',
	                text: 'delete files',
		                click: function (data) {
		                    //perform your custom job...
		                	var $selectedRowsP = $('#DirectBillFileInfo').jtable('selectedRows');
		                	 if ($selectedRowsP.length > 0) {
		                		 var ids = [];
		                		 var agentId ;
		   			      		var fileName ;
		   			      		var keyLink;
		   				      	$selectedRowsP.each(function (index,Element) {
		   				      		agentId = $(this).data('record').agentId;
		   				      		fileName = $(this).data('record').orig_filename;
		   				      		createdDate = $(this).data('record').created_dt;
		   				      		ids.push(($(this).data('record').agentId)+":" +($(this).data('record').orig_filename)+":"+$(this).data('record').created_dt);
		   				      	});
		   	            	var idsjoined = ids.join();
		   	            	$( "#dialog-confirm" ).dialog( "open" );
		   	            	$( "#dialog-confirm" ).dialog({
		   	            		resizable: false,
		   	            		height:140,
		   	            		modal: true,
		   	            		buttons: {
		   	            			"Delete": function() {
		   	            				$( this ).dialog( "close" );
		   	            					$.ajax({
		   	            						url: "company?action=policyfileAction&actionType=JSON&method=deleteDirectBill&ids="+idsjoined,
		   	            						cache: false,
		   	            						success: function(result){
		   	            							$('.jtable-row-selected').removeClass('jtable-row-selected');
		   	            							$('#DirectBillFileInfo').jtable('reload');
		   	            							}
		   	            					});
		   	            			},
		   	            			Cancel: function() {
		   	            				$( this ).dialog( "close" );
		   	            				}
		   	            		}
		   	            	});

		   	            }else{
		   	           		var $div = $("<div id='dialog' title='Message'>" + "<strong><label class='jtable-input-label'>     No Direct bill files marked for delete </label></strong> </div>");
		   	           		$div.dialog({
		   	           			modal : true
		   	           		});
		   	            }
		                
		              }
		            },
	            {
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
	                	//var $div = $("<div id='srcdialog' title='Search Direct Bill'><form name ='tudlform2'><TABLE border='0'><TR><TD>" + "<strong><label class='jtable-input-label'>Policy Number:</label></strong></TD><TD><input type='text' id='srcDBRPolicyNumber' name='srcDBRPolicyNumber'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Insured Name:</label></strong></TD><TD><input type='text' id='srcDBRInsuredName' name='srcDBRInsuredName'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Transaction Type </label></strong></TD><TD><input type='text' name='srcDBRCode' id='srcDBRCode'/></TD></TR></TABLE> </form></div>");
	                    $( "#srchDirectBilldialog" ).dialog({
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
	                                    icons: {
	                                      primary: "ui-icon-heart"
	                                    },
	                                    click: function() {
	                                      $( this ).dialog( "close" );
	                                      searchDirectBillRecord();
	                                    }
	                                  
	                                    // Uncommenting the following line would hide the text,
	                                    // resulting in the label being used as a tooltip
	                                    //showText: false
	                                  }
	                                ]
	                    });
	                    $( "#srchDirectBilldialog" ).dialog( "open" );
	                }
	            },{
	            	icon: 'images/refresh.png',
	                text: 'Reset Search',
	                click: function () {
	                    //perform your custom job...
	                	//var $div = $("<div id='dialog' title='Search Agent'>" + "<strong><label class='jtable-input-label'>Agent Id </label></strong><input type='text' name='srcAgent'/> <BR><strong><label class='jtable-input-label'>Agent Name </label><input type='text' name='srcAgent'/></strong></div>");
	                	ResetDirectBillSearch();
	                }
	            }]
	        },
            actions: {
                listAction: 'company?action=policyfileAction&actionType=JSON&method=dlist',
            },
			fields : {
				Transaction :{
					title :'',
					width :'2%',
					pageSize : 25,
					selecting : true,
					multiselect : true,
					cache: false,
					selectingCheckboxes : true,
					listClass : 'child-opener-image-column',
					display : function(filesData){
						//Create an image that will be used to open child table
						var $img = $('<img class="child-opener-image" src="images/16_blue_add.png" title="Show Transactions" />');
						//Open child table when user clicks the image
						$img.click(function(){
							
							var check = $(this).attr('src');
							if (check.toString().indexOf('16_blue_add.png') != -1) {
								$(this).attr('src', 'images/minus_button.png');
								$(this).attr('title', 'Hide Transactions');
							
							if ($('#DirectBillFileInfo').jtable('isChildRowOpen', $img.closest('tr'))) {
                                $('#DirectBillFileInfo').jtable('closeChildRow', $img.closest('tr'));
                                return;
                            }
							$('#DirectBillFileInfo').jtable('openChildTable', $img.closest('tr'),{
								title : filesData.record.orig_filename + ' - File Name',
								paging: true,
								pageSize : 25,
								selecting : false,
								multiselect : false,
								selectingCheckboxes : false,
								selectOnRowClick : false,
								messages: {
							        noDataAvailable: '(Direct Bill Reconciliation/ Direct Bill Commission Detail)'
							    },
								actions : {
									listAction : 'company?action=policyfileAction&actionType=JSON&method=dbftlist&agentId='
										+ filesData.record.agentId
										+ '&fileName='
										+ filesData.record.orig_filename
										+ '&creationDate='
										+ filesData.record.created_dt
								},
								fields : {
									agentId : {
										type : 'hidden',
										defaultValue : filesData.record.agentId
									},
									insuredName : {
										title : 'Insured',
										width : '15%'
									},
									policyNumber : {
										title : 'Policy No.',
										width : '15%',
									},
									transEffectiveDate : {
										title : 'Eff Dt.',
										width : '15%'
									},
									transTypeCode : {
										title : 'Trans Code',
										width : '15%'
									},
									grossAmount : {
										title : 'Gross Amt',
										width : '10%',
										display: function (data) {
						                      return '$'+number_format(data.record.grossAmount, 2, '.', ',');
						                    }
									},
									comissionRate : {
										title : 'Comm. Rate',
										width : '10%',
										display: function (data) {
						                      return '$'+number_format(data.record.comissionRate, 2, '.', ',');
						                    }
									},
									comissionAmount : {
										title : 'Comm. Amt',
										width : '10%',
										display: function (data) {
						                      return '$'+number_format(data.record.comissionAmount, 2, '.', ',');
						                    }
									},
									lob : {
										title : 'LOB',
										width : '10%'
									}
									 
								},
								closeRequested: function(event, studentData)  {
                                	$img.attr('src', 'images/16_blue_add.png');
                                	$img.attr('title', 'Show Transactions');
                                	$('#DirectBillFileInfo').jtable('closeChildTable',$img.closest('tr'));
                                    }
							},
							function(data) { //opened handler
								data.childTable.jtable('load');
							});}else {
	                        	$('#DirectBillFileInfo').jtable('closeChildTable',$img.closest('tr'));
	                            $(this).attr('src', 'images/16_blue_add.png');
	                            $(this).attr('title', 'Show Transactions');
	                            }
						});
						//Return image to show on the person row
						return $img;
					}
				},
				agentId : {
					key: true,
                    create: false,
                    edit: false,
                    list: false
				},
				dl_status : {
					title : 'Download Type',
					width : '10%',
					edit : true,
					options: { 'Y': 'Archived (DirBill)', 'X': 'Current (DirBill)'}
				},
				createdDate : {
					title : 'Created',
					width : '5%'
				},
				created_dt : {
					title : 'Created',
					width : '5%',
					list: false
				},
				orig_filename : {
					title : 'File',
					width : '8%',
					edit : true
				},
				partcode : {
					title : 'Participant Code',
					width : '10%',
					edit : true
				},
				filename : {
					title : 'Download Filename',
					width : '12%',
					edit : true
				},
				msg_seq : {
					title : 'Message Sequence',
					width : '15%',
					edit : true
				},
				trans_count : {
					title : 'No of Trans.',
					width : '15%',
					edit : true
				},
				lastDwnlDate : {
					title : 'Last Downloaded',
					width : '8%',
					edit : true
				},
				 Download :{
						title :'',
						width :'2%',
						display : function(filesData) {
							//Create an image that will be used to open child table
							var $img = $('<img class="child-opener-image" src="images/download.png" title="Download Direct bill File"/>');
							var idsjoined;
							var $url ;
							//Open child table when user clicks the image
							$img.click(function(){
							     var $selectedRows = $(".jtable-child-table-container").jtable('selectedRows');
	                             // $('#SelectedRowList').empty();
	                              if ($selectedRows.length > 0) {
	                                  //Show selected rows
	                                  var ids = [];
	                                  var transSeq ;
	        		   			      	
	                                  $selectedRows.each(function (index,Element) {
	                                  	transSeq = $(this).data('record').trans_seq;
	       		   		                 ids.push(transSeq);
	       		   	                });
	                                  idsjoined = ids.join();
	                                  $url ="company?action=policyfileAction&method=dwnlFiles&agentId="+filesData.record.agentId+"&fileName="+filesData.record.orig_filename+"&creationDate="+filesData.record.created_dt+"&trasnIds="+idsjoined+"&fstatus="+filesData.record.dl_status;
	                              }else {
	                            	  $url ="company?action=policyfileAction&method=dwnlFiles&agentId="+filesData.record.agentId+"&fileName="+filesData.record.orig_filename+"&creationDate="+filesData.record.created_dt+"&fstatus="+filesData.record.dl_status;
	                              }
	                              
	                             downloadFile($url);
	                          //    window.open("Controller?action=dwnlFiles&agentId="+filesData.record.agentId+"&fileName="+filesData.record.orig_filename+"&creationDate="+filesData.record.created_dt+");

							}); 
							//Return image to show on the person row
							return $img;
							//var $download =$("<a href ='Controller?action=dwnlFiles&agentId="+filesData.record.agentId+"&fileName="+filesData.record.orig_filename+"&creationDate="+filesData.record.created_dt+"'><img class='child-opener-image' src='images/download.png' title='Download File' /></a>");
							//return $download;
						}
					} 
				/* Download: {
                    title: '',
                    width: '2%',
                    display: function (data) {
                       /* var $img = $('<img class="child-opener-image" src="images/download.png" title="Download File" />');
                        $img.click(function () {
							alert("image is clicked");
                        	//$('#PersonTableContainer').jtable('load');

							$.ajax({
			            		  url: "DownloadDemo?action=download",
			            		});
                        });
                        return $img;*/
                        //var $download =$('<a href ="DownloadDemo?action=download"><img class="child-opener-image" src="images/download.png" title="Download File" /></a>');
                        //return $download;
                  //  }
              //  }, */
				/* Download1: {
                    title: '',
                    width: '2%',
                    display: function (data) {
                       /* var $img = $('<img class="child-opener-image" src="images/download.png" title="Download File" />');
                        $img.click(function () {
							alert("image is clicked");
                        	//$('#PersonTableContainer').jtable('load');

							$.ajax({
			            		  url: "DownloadDemo?action=download",
			            		});
                        });
                        return $img;*/
                       // var $download =$('<a href ="DownloadDemo?action=download"><img class="child-opener-image" src="images/download.png" title="Download File" /></a>');
                       // return $download;
                  //  }
               // }  */
			}
		});
		$('#DirectBillFileInfo').jtable('load');
	});