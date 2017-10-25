$(document).ready(function() {
	 $("srchAgentdialog").hide();
	$('#AgentContainer').jtable({
		title: 'Trading Partner Maintenance',
		paging: true,
		sorting: true,
		selecting: true,
		multiselect: false,
		selectingCheckboxes: true,
		selectOnRowClick: false,
		defaultSorting: 'agentId ASC', //Set default sorting
        //dialogShowEffect: 'scale',
        //dialogHideEffect: 'scale',
        //animationsEnabled:true,
        deleteConfirmation: function (data) {
	  		data.deleteConfirmMessage = 'Are you sure to delete Agent ' + data.record.agentId + '?';
  		},
       
        messages:	{
      	  addNewRecord: 'Add New Agent',
      	  editRecord: 'Edit Agent',
      	  areYouSure: 'Are you sure?',
      	  	
      	    //deleteConfirmation: 'This agent record {agentId} will be deleted. Are you sure?',
      	    save: 'Save',
      	    saving: 'Saving',
      	    cancel: 'Cancel',
      	    deleteText: 'Delete',
      	    deleting: 'Deleting',
      	    error: 'Error',
      	    close: 'Close',
      	    cannotLoadOptionsFor: 'Can not load options for field {0}',
      	    pagingInfo: 'Showing {0}-{1} of {2}',
      	    pageSizeChangeLabel: 'Row count',
      	    gotoPageLabel: 'Go to page',
      	    canNotDeletedRecords: 'Can not deleted {0} of {1} records!',
      	    deleteProggress: 'Deleted {0} of {1} records, processing...'
	          },
        toolbar: {
            items: [{
                icon: 'images/printer.png',
                text: 'print',
                click: function () {
                    //perform your custom job...
                	var divToPrint = document.getElementById('AgentContainer');
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
                	if (viewportHeight > 500) viewportHeight = 200;
                	
                	var dialogContainer=document.body.appendChild(document.createElement("div"));
                	dialogContainer.style.position="fixed";
                	dialogContainer.style.top=dialogContainer.style.left="50%";
                    //perform your custom job...
                	//var $div = $("<div id='dialog' title='Search Agent'>" + "<strong><label class='jtable-input-label'>Agent Id </label></strong><input type='text' name='srcAgent'/> <BR><strong><label class='jtable-input-label'>Agent Name </label><input type='text' name='srcAgent'/></strong></div>");
                //var $div = $("<div id='srcdialog' title='Search Agent'><form name ='tudlform1'><TABLE border='0'><TR><TD>" + "<strong><label class='jtable-input-label'>Agent Id:</label></strong></TD><TD><input type='text' id='searchAgentId' name='searchAgentId'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Agent Name:</label></strong></TD><TD><input type='text' id='searchAgentName' name='searchAgentName'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Participant Code </label></strong></TD><TD><input type='text' name='searchPartCode' id='searchPartCode'/></TD></TR></TABLE> </form></div>");
                $( "#srchAgentdialog" ).dialog({
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
                                      searchAgentRecord();
                                    }
                                  
                                    // Uncommenting the following line would hide the text,
                                    // resulting in the label being used as a tooltip
                                    //showText: false
                                  }
                                ]
                    }); 
                	 $( "#srchAgentdialog" ).dialog( "open" );
                }
            },{
	            	icon: 'images/refresh.png',
	                text: 'Reset Search',
	                click: function () {
	                    //perform your custom job...
	                	//var $div = $("<div id='dialog' title='Search Agent'>" + "<strong><label class='jtable-input-label'>Agent Id </label></strong><input type='text' name='srcAgent'/> <BR><strong><label class='jtable-input-label'>Agent Name </label><input type='text' name='srcAgent'/></strong></div>");
	                	ResetAgentSearch();
	                }
	            }]
        },
        actions : {
        
        	listAction : 'company?action=tplist_default_new&actionType=JSON',
			updateAction: 'company?action=tpmaint_save&newAgent=N&actionType=JSON',
			createAction : 'company?action=tpmaint_save&newAgent=Y&actionType=JSON'	,
			deleteAction:'company?action=tplist_delete&actionType=JSON'
        },
        fields : {

			Participants: {
				title: '',
                width: '5%',
                sorting: false,
                edit: false,
                create: false,
                selecting: true,
  	            multiselect: true,
  	            selectingCheckboxes: true,
                listClass: 'child-opener-image-column',
                display: function (studentData) {
                    //Create an image that will be used to open child table
                    if(studentData.record.numberOfPartcipants=='0')
                    	var $img = $('<img class="child-opener-image" src="images/delete_all_participants.png" title="No Participants" />');
                    else
                    	var $img = $('<img class="child-opener-image" src="images/participants.png" title="Show Participants" />');
                    //Open child table when user clicks the image
                    $img.click(function () {

                    	var check = $(this).attr('src');
                        if (check == 'images/participants.png' || check == 'images/delete_all_participants.png') {
                       // $(this).attr('src', 'images/list_all_participants.png');// change image here if want to display different image on click
                    	if ($('#AgentContainer').jtable('isChildRowOpen', $img.closest('tr'))) {
                            $('#AgentContainer').jtable('closeChildRow', $img.closest('tr'));
                            return;
                        }                              
                        $('#AgentContainer').jtable('openChildTable',
                            $img.closest('tr'),
                            {
                                title: studentData.record.name + ' - Participants',
                              pageSize: 5,
                  	          sorting: true,
                  	          selecting: true,
                  	          multiselect: true,
                  	          selectingCheckboxes: true,
                  	        selectOnRowClick: false,
                  	      	defaultSorting: 'participantCode ASC', //Set default sorting
                  	      	
                  	    	messages:	{
          	        	  	addNewRecord: 'Add New Participant         ',
          	        	    editRecord: 'Edit Participant              ',
          	        	    areYouSure: 'Are you sure?',
          	        	    deleteConfirmation: 'This record will be deleted. Are you sure?',
          	        	    save: 'Save',
          	        	    saving: 'Saving',
          	        	    cancel: 'Cancel',
          	        	    deleteText: 'Delete',
          	        	    deleting: 'Deleting',
          	        	    error: 'Error',
          	        	    close: 'Close',
          	        	    cannotLoadOptionsFor: 'Can not load options for field {0}',
          	        	    pagingInfo: 'Showing {0}-{1} of {2}',
          	        	    pageSizeChangeLabel: 'Row count',
          	        	    gotoPageLabel: 'Go to page',
          	        	    canNotDeletedRecords: 'Can not deleted {0} of {1} records!',
          	        	    deleteProggress: 'Deleted {0} of {1} records, processing...'
          		          },
          		        toolbar: {
          		            items: [{
          		                icon: 'images/DeleteRed.png',

          		                click: function (data) {
          		                    //perform your custom job...
                                  var $selectedRows = $(".jtable-child-table-container").jtable('selectedRows');
          		                	 if ($selectedRows.length > 0) {
          		   	            	var ids = [];
          		   	            	var agentId ;
          		   			      	var fileName ;
          		   			      	var keyLink;
          		   				      	$selectedRows.each(function (index,Element) {
          		   					     agentId = $(this).data('record').agentId;
          		   						fileName = $(this).data('record').filename;
          		   		                 ids.push(agentId+":" +fileName);
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
    		   	            		  url: 'company?action=partcode_delete&actionType=JSON&ids='+idsjoined,
    		   	            		  cache: false,
    		   	            		  success: function(result){
    		   	            			  $('#AgentContainer').jtable('load');
    		   	            		  }
    		   	            		});
          		   	            },
          		   	            Cancel: function() {
          		   	              $( this ).dialog( "close" );
          		   	            }
          		   	          }});

          		   	      
          		   	            	
          		   	            	// $('#StudentTableContainer').jtable('reload');
          		   	            }else{
          		   	           		var $div = $("<div id='dialog' title='Message'>" + "<strong><label class='jtable-input-label'>     No participant marked for delete </label></strong> </div>");
                               $div.dialog({
                                   modal : true
                               });
          		   	            }
          		                
          		                }
          		            }]
          		        },
                                actions: {
                                    listAction: 'company?action=partcode_list&actionType=JSON&agentId=' + studentData.record.agentId,
                                    updateAction: 'company?action=partcode_save&newPart=N&actionType=JSON&agentId=' + studentData.record.agentId,
                                    createAction:'company?action=partcode_save&newPart=Y&actionType=JSON&agentId=' + studentData.record.agentId,
                                  // deleteAction:'company?action=partcode_save&actionType=JSON'                                   		
                                    /* deleteAction: function(data) {
                                    	return 'company?action=partcode_save&actionType=JSON&agentId=' + data.record.agentId	
                                    }, */
                                   /*  deleteAction: function (postData) {
                                 
                                        return $.Deferred(function ($dfd) {
                                            $.ajax({
                                                url: '/company?action=partcode_save&actionType=JSON',
                                                type: 'POST',
                                                dataType: 'json',
                                                data: postData,
                                                success: function (postData) {
                                                    $dfd.resolve(postData);
                                                },
                                                error: function () {
                                                    $dfd.reject();
                                                }
                                            });
                                        });
                                    },  */
                                },
                                fields: {
                                	
                                	agentId: {
                                		title: 'Agency ID',
                                        type: 'hidden',
                                        key: true,
                                        list: true,
                                        defaultValue: studentData.record.agentId
                                    },
                                    filename: {
                                		title: 'Agency ID',
                                        type: 'hidden',
                                    },
                                    keylinkFile: {
                                    	title:	function(data) {
                                    		if(document.tudlform.keyLinkFileId &&  document.tudlform.keyLinkFileId.value=='Y') {
                                    			return 'Key link File';
                                    		}else {
                                    			return '';
                                    		}
                                    	},
                                	   width : '5%',
                                    	list:	false,
                                    	input: function(data) {

                                    		if(document.tudlform.keyLinkFileId && document.tudlform.keyLinkFileId.value=='Y') {
                                    			if(data.record){
                                    				return '<input type="text" name="partKeyLink" id ="partKeyLink"  style="width:5px" size="10" value="' + data.record.filename + '"   />';
                                    			}else {
                                    				return '<input type="text" name="partKeyLink" id ="partKeyLink"  style="width:5px" size="10" value=""   />';
                                    			}
                                    			
                                    		}else{
                                    			return '';
                                    		}
                                    	}
                                    	
                                    }, 
                                    participantCode: {
                                        title: 'Participant Agent Code',
                                        width: '30%',
                                        create : true,
                                        inputClass: 'validate[required]'
                                    },
                                    
                                    agentDestAddress : {
                        				title : 'Agent Destination Address',
                        				  list: false,
                        				width : '25%'
                        			},
                              
                               
                                    isPrimaryContact: {
                                        title: 'Same as primary Agent',
                                        list: false,
                                        type: 'checkbox',
                                        defaultValue :	'true',
                                        width: '30%',
                                        values: { 'false': '', 'true': '' },
                                    },
                                    agencyName: {
                                        title: 'Agency Name',
                                        width: '20%'
                                        
                                    },
                                    contactName: {
                                        title: 'Contact Name',
                                      //  inputClass: 'validate[required]',
                                        width: '30%'
                                    },
                                    contactEmail: {
                                        title: 'Email',
                                        width: '30%'
                                        //inputClass: 'validate[required,custom[email]]'
                                    },
                                	
                                    contactPhone: {
                                        title: 'Contact Phone',
                                        list: true,
                                        width: '30%'
                                    }, 
                                    city: {
                                        title: 'City',
                                        list: true,
                                        width: '30%'
                                    },
                                    stateId: {
                                        title: 'State',
                                        list: true,
                                        width: '30%',
                                        options : 'company?action=beanlist&method=slist&actionType=JSON'
                                    },
                                    zip: {
                                        title: 'zip',
                                        list: true,
                                        width: '30%'
                                    }
                                    
                     
                                    
                                },
                                recordAdded: function (event, data) {
                                    //$('#AgentContainer').jtable('reload');
                                },
                                closeRequested: function(event, studentData)  {
                                	//$img.attr('src', 'images/list_all_participants.png');
                                	$('#AgentContainer').jtable('closeChildTable',$img.closest('tr'));
                                    },
                              //Initialize validation logic when a form is created
                                formCreated: function (event, studentData) {
                                	studentData.form.validationEngine();
                                	if(studentData.formType=='create') {
                                		studentData.form.find('[name=participantCode]').addClass('validate[required,ajax[ajaxCheckExistingParticipant]]');	
                                	}
                                	
                                	studentData.form.find('[name=contactPhone]').mask('(999) 999-9999? x99999');
                                	if(studentData.formType=='edit') {
                                        $('#Edit-participantCode').prop('readonly', true);
                                        $('#Edit-participantCode').addClass('jtable-input-readonly');
                                    }
                                },
                                //Validate form when it is being submitted
                                formSubmitting: function (event, studentData) {
                                	if (studentData.formType == 'create') {
                                		var partCode = studentData.form.find('[name=participantCode]').val();
                                		if (partCode == '') {
                                			return studentData.form.validationEngine('validate');
                                		}
                                	}
                                },
                                //Dispose validation logic when form is closed
                                formClosed: function (event, studentData) {
                                	studentData.form.validationEngine('hide');
                                	studentData.form.validationEngine('detach');
                                },
                        		//Register to selectionChanged event to hanlde events
                                selectionChanged: function () {
                                    //Get all selected rows
                                    //var $selectedRows = $('#AgentContainer').jtable('selectedRows');
                                  var $selectedRows = $(".jtable-child-table-container").jtable('selectedRows');
                                    $('#SelectedRowList').empty();
                                    if ($selectedRows.length > 0) {
                                        //Show selected rows
                                        $selectedRows.each(function () {
                                            var record = $(this).data('record');
                                            $el = $(this).parents('table:first :checkbox');
                                            $el.attr('checked', 'checked');
                                            $('#SelectedRowList').append(
                                                '<b>StudentId</b>: ' + record.participantCode +
                                                '<br /><b>Name</b>:' + record.Name + '<br /><br />'
                                                );
                                        });
                                    } else {
                                        //No rows selected
                                        $('#SelectedRowList').append('No row selected! Select rows to see here...');
                                    }
                                }
                            }, 
                            function (data) { //opened handler
                                data.childTable.jtable('load');
                            });
                        }else {
                        	$('#AgentContainer').jtable('closeChildTable',$img.closest('tr'));
                            $(this).attr('src', 'images/participants.png');
                            }
                    
                    });
                    //Return image to show on the person row
                    return $img;
                }
        
				},
				LOB: {
	                width: '2%',
					title: '',
	                edit: false,
					create: false,
					listClass: 'child-opener-image-column',
	                display: function (Data) {
	                
	                var $test =false;
	               // if($test) {
	                var $img = $('<img class="child-opener-image" id="showLanguage" src="images/LOB.png" title="LOB Download" /> ');
	                	
	                  $img.click(function() {
	                  
	                        
	                		if ($('#AgentContainer').jtable('isChildRowOpen', $img.closest('tr'))) {
	                            $('#AgentContainer').jtable('closeChildRow', $img.closest('tr'));
	                            return;
	                        }                              
	                	  
	                	  $('#AgentContainer').jtable('openChildTable',
	                                 $img.closest('tr'),
	                          {
	                        	 title: ' Exclude Line of Business From Import for Agent: ' +Data.record.name,
	                        	 selecting: true,
	                        	 selectingCheckboxes: true,
	                   	        selectOnRowClick: false,
	                   	       multiselect: true,
	                          
	                   	  toolbar: {
	          		            items: [{
	          		                //icon: 'images/DeleteRed.png',
					                text: 'Exclude from Import',
	          		                click: function (data) {
	          		                    //perform your custom job...
                                    var $selectedRows = $('#AgentContainer>.jtable-main-container>.jtable>tbody>.jtable-child-row .jtable-row-selected');
                                    if ($selectedRows.length > 0) {
                                        //Show selected rows
                                        var ids = [];
                                        var lobId ;
              		   			      	var agentId;
              		   			   		var exIds;

                                        $selectedRows.each(function (index,Element) {
                                        	lobId = $(this).data('record').lobId;
                                        	agentId = $(this).data('record').agentId;
                                        	exIds = $(this).data('record').excludeId;
             		   		                 ids.push(lobId+"::"+ agentId +"::"+exIds);
             		   	                });
                                        var idsjoined = ids.join();
                                        
                                        var idsjoined = ids.join();
              		   	            	$.ajax({
              		   	            		  url: 'company?action=partcode_loblist&actionType=JSON&method=ex_update&type=exclude&Ids='+idsjoined,
              		   	            		  cache: false,
              		   	            		  success: function(result){
              		   	            			$(".jtable-child-table-container").jtable('reload')
              		   	            		  }
              		   	            		});

                                    }else{
	          		   	           		var $div = $("<div id='dialog' title='Message'>" + "<strong><label class='jtable-input-label'>     No Lob selected </label></strong> </div>");
	                               $div.dialog({
	                                   modal : true
	                               });
	          		   	            }
	          		                
	          		                }
	          		            },
	          		          {
	          		                //icon: 'images/DeleteRed.png',
					                text: 'Include into Import',
	          		                click: function (data) {
	          		                    //perform your custom job...
                                    var $selectedRows = $('#AgentContainer>.jtable-main-container>.jtable>tbody>.jtable-child-row .jtable-row-selected');
                                    if ($selectedRows.length > 0) {
                                        //Show selected rows
                                        var ids = [];
                                        var lobId ;
              		   			      	var agentId;
              		   			   		var exIds;

                                        $selectedRows.each(function (index,Element) {
                                        	lobId = $(this).data('record').lobId;
                                        	agentId = $(this).data('record').agentId;
                                        	exIds = $(this).data('record').excludeId;
             		   		                 ids.push(lobId+"::"+ agentId +"::"+exIds);
             		   	                });
                                        var idsjoined = ids.join();
                                        
                                        var idsjoined = ids.join();
              		   	            	$.ajax({
              		   	            		  url: 'company?action=partcode_loblist&method=ex_update&actionType=JSON&type=include&Ids='+idsjoined,
              		   	            		  cache: false,
              		   	            		  success: function(result){
              		   	            			$(".jtable-child-table-container").jtable('reload')
              		   	            		  }
              		   	            		});

                                    }else{
	          		   	           		var $div = $("<div id='dialog' title='Message'>" + "<strong><label class='jtable-input-label'>     No Lob selected </label></strong> </div>");
	                               $div.dialog({
	                                   modal : true
	                               });
	          		   	            }
	          		                
	          		                }
	          		            }
	          		            ]
	          		        },
	                         actions: {
                                 listAction: 'company?action=partcode_loblist&method=exlist&actionType=JSON&agentId=' + Data.record.agentId,
                               },
                             
                             fields: {
                             	
                            	 lobId: {
                             		title: 'LOB ID',
                                     type: 'hidden',
                                     key: true,
                                     list: true,
                                     defaultValue: Data.record.lobId
                                 },
                                 code: {
                             		title: 'LOB Code',
                             			width: '20%',
                             				display : function(data) {
                                        		if(data.record.excludeId!='0'){
                                        			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                                        			return '<B><font color="red">'+data.record.code+'</font></B>';
                                        		}else{
                                        			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                                        			return data.record.code;
                                        			
                                        		}
                             				}
                                 },
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
                                /* name: {
                              		title: 'LOB Name',
                              		width: '20%',
                              		list: false,
                              		display : function(data) {
                                		if(data.record.excludeId!='0'){
                                			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                                			return '<B><font color="red">'+data.record.name+'</font></B>';
                                		}else{
                                			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                                			return data.record.name;
                                			
                                		}
                     				}
                              		
                                  },
                                                                     
                                    
                                    type : {
                    					title : 'LOB Type',
                    					width : '10%',
                    					edit : false,
                    					list: false,
                    					display : function(data) {
                    						
                    						
                    						if(data.record.excludeId!='0'){
                                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                                    			
                                    			return '<B><font color="red">'+data.record.type+'</font></B>';
                                    		}else{
                                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                                    			return data.record.type;
                                    			
                                    		}
                         				}
                    				},*/
                                    agentId: {
                                		title: 'AgentId',
                                		width: '20%',
                                		list: false
                                    },
                                    excludeId: {
                                		title: 'Exclude',
                                		width: '20%',
                                		list: false
                                    },
                                    exclude :{
                                    	title : 'Excluded from import List',
                                    	display : function(data) {
                                    		if(data.record.excludeId!='0'){
                                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" value="' + data.record.excludeId + '"  checked>'
                                    			return '<B><font color="red">YES</font></B>';
                                    		}else{
                                    			//return '<input type="checkbox" name ="chkExclude" id ="chkExclude" >'
                                    			return 'NO';
                                    			
                                    		}
                                    	}
                                    },
                                 
                  
                                 
                             },
                             
                     		//Register to selectionChanged event to hanlde events
                              selectionChanged: function () {
                                    //Get all selected rows
                                   // $('#SelectedRowList').empty();
                                   // var $selectedRows = $(".jtable-child-table-container").jtable('selectedRows');
                                    var $selectedRows = $('#AgentContainer>.jtable-main-container>.jtable>tbody>.jtable-child-row .jtable-row-selected');
                                     if ($selectedRows.length > 0) {
                                        //Show selected rows
                                        var ids = [];
                                        var lobId ;
              		   			      	var agentId;
              		   			   		var exIds;

                                        $selectedRows.each(function (index,Element) {
                                        	lobId = $(this).data('record').lobId;
                                        	agentId = $(this).data('record').agentId;
                                        	exIds = $(this).data('record').exIds;
             		   		                 ids.push(lobId+"::"+ agentId +"::"+exIds);
             		   	                });
                                        var idsjoined = ids.join();
                                        var transactionIds = "";
                                         if(document.tudlform.transactionIds.value !=''){
                                        	transactionIds = document.tudlform.transactionIds.value;
                                        }
										transactionIds = transactionIds + idsjoined;
										//alert(transactionIds);
										document.tudlform.transactionIds.value = transactionIds; 

                                    } else {
                                        //No rows selected
                                        $('#SelectedRowList').append('No row selected! Select rows to see here...');
                                    } 
                                }
                         }, 
                         function (data) { //opened handler
                             data.childTable.jtable('load');
                         });
                    
                 
                 });
                 //Return image to show on the person row
                 return $img;
	               //  }
	               }
	           
	            },
        	agentId : {
				title : 'Agency Id',
				width : '10%',
				key : true,
				list : true,
				create : true,
				 display : function(data) {
             		if(data.record.active==false){
             			return '<B><font color="red">'+data.record.agentId+'</font></B>';
             		}else{
             			return data.record.agentId;
             			
             		}
  				},
				inputClass: 'validate[required]'
			},
			name : {
				title : 'Agency Name',
				width : '25%',
				inputClass: 'validate[required]',
				display : function(data) {
					if(data.record.name){
						if(data.record.active==false){
	             			return '<B><font color="red">'+data.record.name+'</font></B>';
	             		}else{
	             			return data.record.name;
	             			
	             		}
					}else{
						return '';
					}
  				}
			},
			contactName : {
				title : 'Contact Name',
				width : '25%',
				inputClass: 'validate[required]',
				display : function(data) {
					if(data.record.contactName){
						if(data.record.active==false){
	             			return '<B><font color="red">'+data.record.contactName+'</font></B>';
	             		}else{
	             			return data.record.contactName;
	             			
	             		}
					}else{
						return '';
					}
  				}
			},
			contactEmail : {
				title : 'contact Email',
				width : '20%',
				inputClass: 'validate[required,custom[email]]',
                list: false
			},
			contactPhone: {
                    title: 'contact Phone',
                    list: false,
                    width: '5%'
                    
                },
                city : {
    				title : 'Agent City',
    				width : '25%',
    				display : function(data) {
    					if(data.record.city){
	    					if( data.record.active==false){
	                 			return '<B><font color="red">'+data.record.city+'</font></B>';
	                 		}else{
	                 			return data.record.city;
	                 			
	                 		}
    					}else{
    						return '';
    					}
      				}
    			},
    			locationState: {
                    title: 'Agent State',
                    width: '10%',
                    list : true,
                    edit : true,
                    options : 'company?action=beanlist&method=slist&actionType=JSON',
                    display : function(data) {
                 		if(data.record.locationState){
                 			if(data.record.active==false){
                 				if(data.record.locationState!='-1')
                 				return '<B><font color="red">'+data.record.locationState+'</font></B>';
                 				else
                 					return '';
                 			}else{
                 				if(data.record.locationState!='-1')
                 					return data.record.locationState;
                 				else
                 					return '';
                 			}
                 		}
      				}
                },
                zip : {
    				title : 'Postal Code',
    				width : '25%',
    				  display : function(data) {
    					  if(data.record.zip){	
    						  if( data.record.active==false){
    							  return '<B><font color="red">'+data.record.zip+'</font></B>';
    						  }else{
                     			return data.record.zip;
                     			
                     		}
    					 }else {
    						 return '';
    					 }
          			}
    			},
    			  amsId: {
  	                title: 'Agency Management System',
  	                width: '30%',
  	                options : 'company?action=beanlist&method=amslist&actionType=JSON',
  	                display : function(data) {
  	                	if(data.record.amsId){
  	                		if(data.record.active==false){
  	                			if(data.record.amsId!='-1')
  	                				return '<B><font color="red">'+data.record.amsId+'</font></B>';
  	                			else
  	                				return '';
  	                		}else{
  	                			if(data.record.amsId!='-1')
  	                				return data.record.amsId;
  	                			else
  	                				return '';
  	                		}
  	                	}
  	               			
  	    			}
  	                
  	            },
       
              destAddress : {
				title : 'Agent Destination Address',
				width : '25%',
			      display : function(data) {
			    	  if(data.record.destAddress){
	               		if( data.record.active==false){
	               			return '<B><font color="red">'+data.record.destAddress+'</font></B>';
	               		}else{
	               				return data.record.destAddress;
	               		}
	               	}else{
	               		return '';
	               			
	               		}
	    		}
			},
            remoteDir : {
				title : 'AL3 Download Directory',
				list: false,
				width : '30%',
				edit : true,
				input: function (data) {
			        if (data.record) {
			            return '<input type="text" name="remoteDir" id="remoteDir" style="width:200px" value="' + data.record.remoteDir + '" /> ';
			        } else {
			        	return '<input type="text" name="remoteDir" id="remoteDir" style="width:200px" placeholder="Enter download directory" /> ';
			        }
		}
			},
			remoteClaimDir : {
				title : 'Claims XML Download Directory',
				list: false,
				width : '30%',
				edit : true,
				input: function (data) {
			        if (data.record) {
			            if(data.record.remoteClaimDir && data.record.remoteClaimDir != 'null') {
			            	return '<input type="text" name="remoteClaimDir" id="remoteClaimDir" style="width:200px" value="' + data.record.remoteClaimDir + '" /> ';
			            }else{
			            	return '<input type="text" name="remoteClaimDir" id="remoteClaimDir" style="width:200px" placeholder="Enter download directory for Claim" /> ';
			            }
			        } else {
			        	return '<input type="text" name="remoteClaimDir" id="remoteClaimDir" style="width:200px" placeholder="Enter download directory for Claim" /> ';
			        }
		}
			},
			remotePolicyDir : {
				title : 'Policy XML Download Directory',
				list: false,
				width : '30%',
				edit : true,
				input: function (data) {
			        if (data.record) {
			        	if (data.record.remotePolicyDir && data.record.remotePolicyDir != 'null') {
			        		return '<input type="text" name="remotePolicyDir" id="remotePolicyDir" style="width:200px" value="' + data.record.remotePolicyDir + '" />  <br><label>Use Default  <input type="checkbox" name ="chkDefault" id ="chkDefault" ></label>';
			        	}else{
			        		return '<input type="text" name="remotePolicyDir" id="remotePolicyDir" style="width:200px" placeholder="Enter download directory for Policy XML" />  <br><label>Use Default  <input type="checkbox" name ="chkDefault" id ="chkDefault" ></label>';
			        	}
			        } else {
			        	return '<input type="text" name="remotePolicyDir" id="remotePolicyDir" style="width:200px" placeholder="Enter download directory for Policy XML" />  <br><label>Use Default  <input type="checkbox" name ="chkDefault" id ="chkDefault" ></label>';
			        }
		}
			},
			 keylinkFile: {
	            	title:	function(data) {
	            		if(document.tudlform.keyLinkFileId.value=='Y') {
	            			return 'Key link File';
	            		}else {
	            			return '';
	            		}
	            	},
	        	   width : '5%',
	            	list:	false,
	            	input: function(data) {

	            		if(document.tudlform.keyLinkFileId.value=='Y') {
	            			if(data.record){
		            			return '<input type="text" name="filename" id ="filename"  style="width:5px" size="10" value="' + data.record.keylinkFile + '"   />';	            				
	            			}else {
		            			return '<input type="text" name="filename" id ="filename"  style="width:5px" size="10" value=""   />';
	            			}

	            		}else{
	            			return '';
	            		}
	            	}
	            	
	            }, 
            active: {
                title: 'Active',
                width: '15%',
                list : false,
                type: 'checkbox',
                values: { 'false': 'Disabled', 'true': 'Active' },
                defaultValue: 'true'
            },
            testAgent: {
                title: 'Test Agent',
                width: '15%',
                list : false,
                type: 'checkbox',
                values: { 'false': 'No', 'true': 'Yes' }
            },
            status : {
				title : 'Status',
				list: true,
                create:false,
				width : '20%',
				edit: false,
				display: function (data) {
			        if (data.record) {
			        	if(data.record.status =='0'){
			        		if(data.record.active==false){
		               			return '<B><font color="red">Disabled</font></B>';
		               		}else{
		               			return 'Disabled';
		               			
		               		}
			            }
			        	else if(data.record.status =='1'){
			        		if(data.record.active==false){
		               			return '<B><font color="red">Registred</font></B>';
		               		}else{
		               			return 'Registred';
		               			
		               		}
			            }else if(data.record.status =='2') {
			            	if(data.record.active==false){
		               			return '<B><font color="red">UnRegistred</font></B>';
		               		}else{
		               			return 'UnRegistred';
		               			
		               		}
			            }else if(data.record.status =='4') {
			            	if(data.record.active==false){
		               			return '<B><font color="red">Live</font></B>';
		               		}else{
		               			return 'Live';
		               			
		               		}
			            }else if(data.record.status =='3') {
			            	if(data.record.active==false){
		               			return '<B><font color="red">Registred</font></B>';
		               		}else{
		               			return 'Registred';
		               			
		               		}
			            }
			        } else {
			        	return '';
			        }
		}
				
			},
			interactiveFlag : {
				title : 'Scheduled Download',
				list: true,
                create: false,
                edit: false,
				width : '20%',
				edit: false,
				display: function (data) {
					if (data.record) {
						if(data.record.active==false){
							if(data.record.interactiveFlag =='N'){
		               			return '<B><font color="red">Yes</font></B>';
							}
							else {
								return '<B><font color="red">No</font></B>';
							}
						}else {
							if(data.record.interactiveFlag =='N'){
		               			return '<B><font color="green">Yes</font></B>';
							}
							else {
								return '<B><font color="blue">No</font></B>';
							}
						}
			         }
			         else {
			        	 return '<B><font color="blue">No</font></B>';
			        }
				}
			},
			defaultFilename: {
                title: 'Default AL3 Filename',
                list: false,
                create: false,
                edit: false,
                width: '20%'
                
            },
            defaultClaimFilename: {
                title: 'Default Claim Filename',
                list: false,
                create: false,
                edit: false,
                width: '20%'
                
            },
            defaultPolicyFilename: {
                title: 'Default Policy XML Filename',
                list: false,
                create: false,
                edit: false,
                width: '20%'
                
            },
            
            groupName: {
                title: 'Group Name',
                width: '15%',
                list: false,
                create:false,
                input: function(data) {
                	if(data.record.groupName){
                		return '<B>'+data.record.groupName+'</B>';
                	}
                		
                }
            },
			resetPassword: {
                title: 'Reset Password',
                width: '15%',
                list: false,
                create:false,
                type: 'checkbox',
                values: { 'false': '', 'true': '' },
                defaultValue: 'false'
            },
            newPassword: {
                title: 'New Password',
                create:false,
                width: '15%',
                list: false,
            },
            lastDownload_Date: {
                title: 'Last Download Date',
                width: '15%',
                create: false,
                edit: false,
                display : function(data) {
               		if(data.record.active==false){
               			return '<B><font color="red">'+data.record.lastDownload_Date+'</font></B>';
               		}else{
               			return data.record.lastDownload_Date;
               			
               		}
    			}

            },
            resendRegEmail: {
                title: 'Send registration email to new trading partner',
                width: '15%',
                list: false,
                type: 'checkbox',
                values: { 'false': '', 'true': '' },
                defaultValue: 'true'
            },
          
            ViewArchives: {
                width: '2%',
				title: '',
                edit: false,
				create: false,
				listClass: 'child-opener-image-column',
                display: function (Data) {
                	 var $img = $('<img class="child-opener-image" src="images/_view.png" title="Show Files" />');
                	
                	 /* $img.click(function() {
                         var $div = $("<div id='dialog' title='Information'>" + "<strong><label class='jtable-input-label'>Nombre Completo: </label></strong>" + Data.record.agentId + " " + Data.record.agentId + "<br>" + "<strong><label class='jtable-input-label'>Sexo: </label></strong>" + Data.record.agentId + "<br>" + "<strong><label class='jtable-input-label'>Dirección Completa: </label></strong>" + Data.record.agentId + ", " + Data.record.agentId + ", " + Data.record.agentId + ", " + Data.record.agentId + "<br>" + "<strong><label class='jtable-input-label'>Periodo de Registro: </label></strong>" + Data.record.agentId + " Del " + Data.record.agentId + "<br>" + "<strong><label class='jtable-input-label'>Periodo Actual: </label></strong>" + Data.record.agentId + " Del " + Data.record.agentId + "<br>" + "<strong><label class='jtable-input-label'>Semestre: </label></strong>" + Data.record.agentId + "</div>");
                         $div.dialog({
                             modal : true
                         });
                     }); */
                     
                     $img.click(function() {
                    	 document.tudlform.agentID.value = Data.record.agentId;
	                        do_files();
                     });
                     return $img;
                 }
           
            },
           
        },
        recordDeleted: function (event, data) {
           // $('#AgentContainer').jtable('reload');
        },
        recordUpdated:function(event, data){
           // $('#AgentContainer').jtable('reload');
        },
        recordAdded: function (event, data) {
           // $('#AgentContainer').jtable('reload');
        },
      //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
            data.form.find('[name=agentId]').addClass('validate[required,ajax[ajaxCheckExistingAgentId]]');
            data.form.find('[name=contactPhone]').mask('(999) 999-9999? x99999');

        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
        	
        	if (data.formType == 'create') {
        		var agentKey = data.form.find('[name=agentId]').val();
        		var agencyName = data.form.find('[name=name]').val();
        		var contact = data.form.find('[name=contactName]').val();
        		var email = data.form.find('[name=contactEmail]').val();
        		
        		if (agentKey == '' || agencyName == '' || contact == '' || email == '') {
        			return data.form.validationEngine('validate');
        		} 
        	}
        	
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        },
        selectionChanged: function () {
            //Get all selected rows
            var $selectedRows = $('#AgentContainer').jtable('selectedRows');

            $('#SelectedRowList').empty();
            if ($selectedRows.length > 0) {
                //Show selected rows
                $selectedRows.each(function () {
                    var record = $(this).data('record');
                    $el = $(this).parents('table:first :checkbox');
                    $el.attr('checked', 'checked');
                    document.tudlform.agentID.value = record.agentId;
                });
            } else {
                //No rows selected
                $('#SelectedRowList').append('No row selected! Select rows to see here...');
            }
        }
        
        
	});
	//Load file list from server
	$('#AgentContainer').jtable('load');
	
});
