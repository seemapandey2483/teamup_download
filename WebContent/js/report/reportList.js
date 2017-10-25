$(document).ready(function() {
	 
	$('#ReportContainer').jtable({
		title: 'Trading Partner Maintenance',
		paging: true,
		sorting: true,
		selecting: false,
		multiselect: false,
		selectingCheckboxes: false,
		selectOnRowClick: false,
      
        actions : {
        
        	listAction : 'company?action=report_list&actionType=JSON'
        },
        fields : {
		   	agentID : {
				title : 'Agency Id',
				width : '10%',
				key : true,
				list : true,
				create : false,
			},
			origFileName : {
				title : 'File Name',
				width : '25%',
			},
			eventDate : {
				title : 'Event Date',
				width : '25%',
			},
			event_type : {
				title : 'Event Type',
				width : '20%',
                list: false
			},
			policyNumber: {
                    title: 'Policy Number',
                    list: false,
                    width: '5%'
                    
                },
                claimNumber : {
    				title : 'Claim Number',
    				width : '25%'
    			},
    			
                lob : {
    				title : 'LOB',
    				width : '25%'
    			},
				 insuredName : {
    				title : 'Insured Name',
    				width : '25%'
    			},
                
             txnType : {
    				title : 'Txn Type,
    				width : '25%'
    			},    
			fileType : {
    				title : 'File Type',
    				width : '25%'
    			} 				
        }
        
             
	});
	//Load file list from server
	$('#ReportContainer').jtable('load');
	
});
