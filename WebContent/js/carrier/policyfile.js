$(document).ready(function() {
		$('#PolicyFileInfo').jtable({
			title : 'Current & Archived Download Files',
            paging: true,
            sorting: true,
            defaultSorting: 'created_dt DESC',
	        selecting: true,
	        multiselect: true,
	        selectingCheckboxes: true,
	        selectOnRowClick: false,
	        toolbar: {
	            items: [{
	                icon: 'images/printer.png',
	                text: 'print',
	                click: function () {
	                    //perform your custom job...
	                	var divToPrint = document.getElementById('PolicyFileInfo');
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
                          var $selectedRowsP = $('#PolicyFileInfo').jtable('selectedRows');
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
	   	            		   url: "company?action=policyfileAction&actionType=JSON&method=deletePolicy&ids="+idsjoined,
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

  		   	      
  		   	            	
  		   	            	// $('#StudentTableContainer').jtable('reload');
  		   	            }else{
  		   	           		var $div = $("<div id='dialog' title='Message'>" + "<strong><label class='jtable-input-label'>     No Policy files marked for delete </label></strong> </div>");
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
	                	//var $div = $("<div id='srcdialog' title='Search Policy'><form name ='tudlform1'><TABLE border='0'><TR><TD>" + "<strong><label class='jtable-input-label'>Policy Number:</label></strong></TD><TD><input type='text' id='srchPolicyNumber' name='srchPolicyNumber'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Insured Name:</label></strong></TD><TD><input type='text' id='srchInsuredName' name='srchInsuredName'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Transaction Type </label></strong></TD><TD><input type='text' name='srchTxnType' id='srchTxnType'/></TD></TR></TABLE> </form></div>");
	                    $( "#srchPolicydialog" ).dialog({
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
	                                      searchPolicyRecord();
	                                    }
	                                  
	                                    // Uncommenting the following line would hide the text,
	                                    // resulting in the label being used as a tooltip
	                                    //showText: false
	                                  }
	                                ]
	                    });
	                    $( "#srchPolicydialog" ).dialog( "open" );
	                }
	            },{
	            	icon: 'images/refresh.png',
	                text: 'Reset Search',
	                click: function () {
	                    //perform your custom job...
	                	//var $div = $("<div id='dialog' title='Search Agent'>" + "<strong><label class='jtable-input-label'>Agent Id </label></strong><input type='text' name='srcAgent'/> <BR><strong><label class='jtable-input-label'>Agent Name </label><input type='text' name='srcAgent'/></strong></div>");
	                	ResetPolicySearch();
	                }
	            }]
	        },
	      
            actions: {
                listAction: 'company?action=policyfileAction&actionType=JSON&method=plist',
            },
			fields : {
				
				
				Transaction :{
					title :'',
					width :'2%',
					sorting : false,
					edit : false,
					create : false,
					selecting : true,
					multiselect : true,
					selectingCheckboxes : true,
					listClass : 'child-opener-image-column',
					display : function(filesData){
						//Create an image that will be used to open child table
						var $img = $('<img class="child-opener-image" src="images/16_blue_add.png" title="Show Transactions" />');
						/*$("img").live('click', function () {
							if ($(this).attr("src").toString().indexOf('16_blue_add.png') != -1) {
							      this.src = this.src.replace("16_blue_add.png", "minus_button.png");
							      } 
						      });*/
						 
						
						//Open child table when user clicks the image
						$img.click(function(){
							
							var check = $(this).attr('src');
							if (check.toString().indexOf('16_blue_add.png') != -1) {
								$(this).attr('src', 'images/minus_button.png');
								$(this).attr('title', 'Hide Transactions');
						
							if ($('#PolicyFileInfo').jtable('isChildRowOpen', $img.closest('tr'))) {
                                $('#PolicyFileInfo').jtable('closeChildRow', $img.closest('tr'));
                                $("img").live('click', function () {
        							if ($(this).attr("src").toString().indexOf('16_blue_add.png') == -1) {
        							      this.src = this.src.replace("minus_button.png", "16_blue_add.png");
        							      } 
        						      });
                                return;
                            }
							
							
							$('#PolicyFileInfo').jtable('openChildTable', $img.closest('tr'),{
								title : filesData.record.orig_filename + ' - File Name',
								selecting : true,
								multiselect : true,
								selectingCheckboxes : true,
								selectOnRowClick : false,
								actions : {
									listAction : 'company?action=policyfileAction&actionType=JSON&method=pftlist&agentId='
											+ filesData.record.agentId
											+ '&fileName='
											+ filesData.record.orig_filename
											+ '&creationDate='
											+ filesData.record.created_dt
								},
								fields : {
									agentId : {
										key: true,
					                    create: false,
					                    edit: false,
					                    list: false
									},
									customerId : {
										title : 'customer Id',
										width : '10%'
									},
									policy_num : {
										title : 'Policy No.',
										width : '15%',
										display: function (data) {
						                      return '<label for ="test"  title ='+data.record.trans_effdt+'-'+data.record.description+'>'+data.record.policy_num+'</label>';
						                    }
									},
									policyEffDate : {
										title : 'Effective Date',
										width : '15%'
									},
									trans_type : {
										title : 'Trans.',
										width : '15%',
										display: function (data) {
						                      return '<label for ="test"  title ='+data.record.trans_effdt+'-'+data.record.description+'>'+data.record.trans_type+'</label>';
						                    }
									},
									trans_seq : {
										title : 'Trans. Seq.',
										width : '15%'
									},
									named_insured : {
										title : 'Named Insured',
										width : '30%'
									},
									lob : {
										title : 'LOB',
										width : '10%'
									},
									premium : {
										title : 'Premium Amount',
										width : '10%',
										display: function (data) {
						                      return '$'+number_format(data.record.premium, 2, '.', ',');
						                    }
									},
									downloadStatus : {
										title : 'status',
										width : '10%'
									},
									orig_filename : {
										title : 'Orig fileName',
										width : '10%',
										list : false
									},
									lastDownload_Date : {
										title : 'download date',
										width : '10%'
									},
									createdDate : {
										title : 'createdDate',
										width : '10%',
										list : false
									},
								},
								 closeRequested: function(event, studentData)  {
	                                	$img.attr('src', 'images/16_blue_add.png');
	                                	$img.attr('title', 'Show Transactions');
	                                	$('#PolicyFileInfo').jtable('closeChildTable',$img.closest('tr'));
	                                    },
								//Register to selectionChanged event to hanlde events
                                selectionChanged: function () {
                                    //Get all selected rows
                                   // $('#SelectedRowList').empty();
                                   // var $selectedRows = $(".jtable-child-table-container").jtable('selectedRows');
                                    var $selectedRows = $('#PolicyFileInfo>.jtable-main-container>.jtable>tbody>.jtable-child-row .jtable-row-selected');
                                    
                                     if ($selectedRows.length > 0) {
                                        //Show selected rows
                                        var ids = [];
                                        var transSeq ;
              		   			      	var orig_filename;
              		   			   		var createdDate;
              		   			   		var agentId;
              		   			   		var transStatus;
              		   			   		document.tudlform.transactionIds.value ="";
                                        $selectedRows.each(function (index,Element) {
                                        	transSeq = $(this).data('record').trans_seq;
                                        	orig_filename = $(this).data('record').orig_filename;
                                        	createdDate = $(this).data('record').createdDate;
                                        	agentId = $(this).data('record').agentId;
                                        	transStatus = $(this).data('record').downloadStatus;
             		   		                ids.push(agentId+"::"+ orig_filename +"::"+createdDate+"::"+transSeq+"::"+transStatus);
             		   	                });
                                        var idsjoined = ids.join();
                                        var transactionIds = "";
                                         if(document.tudlform.transactionIds.value !=''){
                                        	transactionIds = document.tudlform.transactionIds.value;
                                        }
										transactionIds = transactionIds + idsjoined;
										document.tudlform.transactionIds.value = transactionIds;

                                    } else {
                                        //No rows selected
                                        $('#SelectedRowList').append('No row selected! Select rows to see here...');
                                    } 
                                }
								
							},
							function(data) { //opened handler
								data.childTable.jtable('load');
							}); }else {
	                        	$('#PolicyFileInfo').jtable('closeChildTable',$img.closest('tr'));
	                            $(this).attr('src', 'images/16_blue_add.png');
	                            $(this).attr('title', 'Show Transactions');
	                            }
							
						});
						//Return image to show on the person row
						return $img;
					}
				},
			/*	fileDType : {
                    list: true,
                    title: '',
                    width :'2%',
					sorting : false,
					edit : false,
					create : false,
					display : function(filesData){
						if(filesData.record.dl_status =='A' || filesData.record.dl_status =='C' ){
								 var $img = $('<img  src="images/AL3.PNG" title="Policy AL3" />');
								  
						}else {
							var  $img = $('<img  src="images/AL3.PNG" title="Policy XML" />');
						}
						return $img;
					}
				},*/
				agentId : {
					key: true,
                    create: false,
                    edit: false,
                    list: false
				},
				dl_status : {
					title : 'Download Type',
					width : '20%',
					edit : true,
					//options: { 'I': 'Current (XML)', 'J': 'Archived (XML)','K': 'Resend/Synch (XML)', 'L': 'Download (XML)','C': 'Current (AL3)', 'A': 'Archived (AL3)', 'R': 'Resend/Synch (AL3)', 'D': 'Download (AL3)'}
					display : function(filesData){
						var $disp;
						if(filesData.record.dl_status =='C' ){
							$disp = $('<font  color="black" >Current </font><font  color="blue" > <B>(AL3)</B></font>');
								  
						}else if (filesData.record.dl_status =='A' ){
							$disp = $('<font  color="black" >Archive  </font><font  color="blue" ><B>(AL3)</B></font>');
						}else if (filesData.record.dl_status =='I' ){
							$disp = $('<font  color="black" >Current  </font><font  color="green" ><B>(XML)</B></font>');
						}else if (filesData.record.dl_status =='J' ){
							$disp = $('<font  color="black" >Archive  </font><font  color="green" ><B>(XML)</B></font>');
						}else if (filesData.record.dl_status =='D' ){
							$disp = $('<font  color="black" >Download  </font><font  color="blue" ><B>(AL3)</B></font>');
						}else if (filesData.record.dl_status =='K' ){
							$disp = $('<font  color="black" >Download  </font><font  color="green" ><B>(XML)</B></font>');
						}else if (filesData.record.dl_status =='L' ){
							$disp = $('<font  color="black" >Resend  </font><font  color="green" ><B>(XML)</B></font>');
						}else if (filesData.record.dl_status =='R' ){
							 $disp = $('<font  color="black" >Resend  </font><font  color="blue" ><B>(AL3)</B></font>');
						}
						return $disp;
					}
				},
				createdDate : {
					title : 'Created',
					width : '5%'
				},
				created_dt : {
					title : 'Created',
					list: false
				},
				orig_filename : {
					title : 'File Name',
					width : '5%',
					edit : true
				},
				partcode : {
					title : 'Participant Code',
					width : '15%',
					edit : true
				},
				filename : {
					title : 'Download Filename',
					width : '15%',
					edit : true
				},
				msg_seq : {
					title : 'Msg Seq',
					width : '17%',
					edit : true
				},
				trans_count : {
					title : 'No of Trans.',
					width : '10%',
					edit : true
				},
				lastDwnlDate : {
					title : 'Last Downloaded',
					width : '8%',
					edit : true
				},
				/* Download: {
                    title: '',
                    width: '1%',
                    display: function (dwnlData) {
                        var $download =$('<a href ="Controller?action=dwnlFiles" target="_blank"><img class="child-opener-image" src="images/download.png" title="Download File" /></a>');
                        return $download;
                    }
                }, */
                Download :{
					title :'',
					width :'2%',
					display : function(filesData) {
						//Create an image that will be used to open child table
						var $img = $('<img class="child-opener-image" src="images/download.png" title="Download Policy File"/>');
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
				
			}
		});
		//Load file list from server
		$('#PolicyFileInfo').jtable('load');
	});