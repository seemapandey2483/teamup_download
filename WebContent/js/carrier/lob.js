
	    $(document).ready(function (data) {
	    	$('#LineOfBusiness').jtable({
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
			                	var $selectedRowsP = $('#LineOfBusiness').jtable('selectedRows');
			                	 if ($selectedRowsP.length > 0) {
			                		 var ids = [];
			                		 var agentId ;
			   			      		var fileName ;
			   			      		var keyLink;
			   				      	$selectedRowsP.each(function (index,Element) {
			   				      		ids.push(($(this).data('record').agentId));
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
			   	            							$('#LineOfBusiness').jtable('reload');
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
			                	var $selectedRowsP = $('#LineOfBusiness').jtable('selectedRows');
			                	 if ($selectedRowsP.length > 0) {
			                		 var ids = [];
			                		 var agentId ;
			   			      		var fileName ;
			   			      		var keyLink;
			   				      	$selectedRowsP.each(function (index,Element) {
			   				      		ids.push($(this).data('record').id);
			   				      	});
			   	            	var idsjoined = ids.join();
			   	            	/*$( "#dialog-confirm-dact" ).dialog( "open" );
			   	            	$( "#dialog-confirm-dact" ).dialog({
			   	            		resizable: false,
			   	            		height:140,
			   	            		modal: true,
			   	            		buttons: {
			   	            			"Delete": function() {
			   	            				$( this ).dialog( "close" );
			   	            					$.ajax({
			   	            						url: "LOBController?action=updateActive&active=N&ids="+idsjoined,
			   	            						cache: false,
			   	            						success: function(result){
			   	            							$('.jtable-row-selected').removeClass('jtable-row-selected');
			   	            							$('#LineOfBusiness').jtable('reload');
			   	            							}
			   	            					});
			   	            			},
			   	            			Cancel: function() {
			   	            				$( this ).dialog( "close" );
			   	            				}
			   	            		}
			   	            	});*/

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
	                listAction: 'LOBController?action=pLobList',
	                createAction:'LOBController?action=pLobCreate',
	                updateAction: 'LOBController?action=pLobUpdate',
	                deleteAction: 'LOBController?action=pLobDelete'
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
	                    create:true
	                },
	                code: {
	                    title: 'Code',
	                    width: '30%',
	                    edit:true
	                },
	                type: {
	                    title: 'Type',
	                    width: '20%',
	                    edit: true,
	                    options: { 'P': 'Personal'}
	                },
	                description: {
	                    title: 'Description',
	                    width: '20%',
	                    edit: true
	                }     
	            }
	        });
	        $('#LineOfBusiness').jtable('load');
	    });
	    $(document).ready(function () {
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
	    	                click: function () {
	    	                    //perform your custom job...
	    	                	var divToPrint = document.getElementById('AgentContainer');
	    			        	newWin = window.open("");
	    			        	newWin.document.write(divToPrint.outerHTML);
	    			        	newWin.print();
	    			        	newWin.close();
	    	                }
	    	            },{
	    	            	//icon: 'images/find.png',
	    	                text: 'Deactivate',
	    	                click: function () {
	    	                    //perform your custom job...
	    	                	//var $div = $("<div id='dialog' title='Search Agent'>" + "<strong><label class='jtable-input-label'>Agent Id </label></strong><input type='text' name='srcAgent'/> <BR><strong><label class='jtable-input-label'>Agent Name </label><input type='text' name='srcAgent'/></strong></div>");
	    	                	var $div = $("<div id='srcdialog' title='Search Agent'><form name ='tudlform1'><TABLE border='0'><TR><TD>" + "<strong><label class='jtable-input-label'>Agent Id:</label></strong></TD><TD><input type='text' id='srcAgentId' name='srcAgentId'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Agent Name:</label></strong></TD><TD><input type='text' id='srcAgentName' name='srcAgentName'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Participant Code </label></strong></TD><TD><input type='text' name='srcAgentCode' name='srcAgentCode'/></TD></TR></TABLE> </form></div>");
	    	                    $div.dialog({
	    	                        modal : false,
	    	                        buttons: [
	    	                                  {
	    	                                    text: "search",
	    	                                    icons: {
	    	                                      primary: "ui-icon-heart"
	    	                                    },
	    	                                    click: function() {
	    	                                      $( this ).dialog( "close" );
	    	                                      searchRecord();
	    	                                    }
	    	                                  
	    	                                    // Uncommenting the following line would hide the text,
	    	                                    // resulting in the label being used as a tooltip
	    	                                    //showText: false
	    	                                  }
	    	                                ]
	    	                    });
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
	                    create:true
	                },
	                code: {
	                    title: 'Code',
	                    width: '30%',
	                    edit:true
	                },
	                type: {
	                    title: 'Type',
	                    width: '20%',
	                    edit: true,
	                    options: { 'C': 'Commercial Lines'}
	                },
	                description: {
	                    title: 'Description',
	                    width: '20%',
	                    edit: true
	                }     
	            }
	        });
	        $('#cLineOfBusiness').jtable('load');
	    });
	    $(document).ready(function () {
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
	    	                click: function () {
	    	                    //perform your custom job...
	    	                	var divToPrint = document.getElementById('AgentContainer');
	    			        	newWin = window.open("");
	    			        	newWin.document.write(divToPrint.outerHTML);
	    			        	newWin.print();
	    			        	newWin.close();
	    	                }
	    	            },{
	    	            	//icon: 'images/find.png',
	    	                text: 'Deactivate',
	    	                click: function () {
	    	                    //perform your custom job...
	    	                	//var $div = $("<div id='dialog' title='Search Agent'>" + "<strong><label class='jtable-input-label'>Agent Id </label></strong><input type='text' name='srcAgent'/> <BR><strong><label class='jtable-input-label'>Agent Name </label><input type='text' name='srcAgent'/></strong></div>");
	    	                	var $div = $("<div id='srcdialog' title='Search Agent'><form name ='tudlform1'><TABLE border='0'><TR><TD>" + "<strong><label class='jtable-input-label'>Agent Id:</label></strong></TD><TD><input type='text' id='srcAgentId' name='srcAgentId'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Agent Name:</label></strong></TD><TD><input type='text' id='srcAgentName' name='srcAgentName'/></TD></TR>" + "<TR><TD><strong><label class='jtable-input-label'>Participant Code </label></strong></TD><TD><input type='text' name='srcAgentCode' name='srcAgentCode'/></TD></TR></TABLE> </form></div>");
	    	                    $div.dialog({
	    	                        modal : false,
	    	                        buttons: [
	    	                                  {
	    	                                    text: "search",
	    	                                    icons: {
	    	                                      primary: "ui-icon-heart"
	    	                                    },
	    	                                    click: function() {
	    	                                      $( this ).dialog( "close" );
	    	                                      searchRecord();
	    	                                    }
	    	                                  
	    	                                    // Uncommenting the following line would hide the text,
	    	                                    // resulting in the label being used as a tooltip
	    	                                    //showText: false
	    	                                  }
	    	                                ]
	    	                    });
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
	                    create:true
	                },
	                code: {
	                    title: 'Code',
	                    width: '30%',
	                    edit:true
	                },
	                type: {
	                    title: 'Type',
	                    width: '20%',
	                    edit: true,
	                    options: { 'O': 'Other'}
	                },
	                description: {
	                    title: 'Description',
	                    width: '20%',
	                    edit: true
	                }    
	            }
	        });
	        $('#oLineOfBusiness').jtable('load');
	    });