<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<jsp:useBean id="MenuBean" scope="request" type="connective.teamup.download.beans.MenuBean" />
<link rel="stylesheet" href="css/jquery-ui.css">
<link rel="stylesheet" href="css/style.css">
<!-- Include one of jTable styles. -->
<link href="css/lightcolor/blue/jtable.css" rel="stylesheet" type="text/css" />
<link href="css/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" />
<!-- Include jTable script file. -->
<script src="js/jquery-1.8.2.js" type="text/javascript"></script>
<script src="js/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>
<script src="js/jquery.jtable.js" type="text/javascript"></script>

<script src="js/jquery.maskedinput.js" type="text/javascript"></script>

<link href="css/validationEngine/validationEngine.jquery.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/validationEngine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="js/validationEngine/jquery.validationEngine-en.js"></script>
<!-- script src="js/carrier/tplist.js" type="text/javascript"></script-->

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
<script>
function AddSearch(){
	$('#PersonTableContainer').jtable('showCreateForm'); 
}

function makePasswd() {
	  var passwd = '';
	  var chars = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
	  for (i=1;i<8;i++) {
	    var c = Math.floor(Math.random()*chars.length + 1);
	    passwd += chars.charAt(c)
	  }

	  return passwd;

	}


</script>
<script type="text/javascript">
    $(document).ready(function () {
	$('#PersonTableContainer').hide();       
 $('#PersonTableContainer').jtable({
      title: 'Trading Partner Maintenance',
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
	      actions: {
        	                createAction:'company?action=tpmaint_save&newAgent=Y&actionType=JSON',
        	            },
            fields: {
	
                agentId : {
					title : 'Agency Id',
					width : '10%',
					key : true,
					list : true,
					create : true,
					inputClass: 'validate[required]'
				
				},
              name : {
					title : 'Agency Name',
					width : '25%',
					inputClass: 'validate[required]'
				},
			contactName : {
					title : 'Contact Name',
					width : '25%',
					inputClass: 'validate[required]'
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
				width : '25%'
			},
		locationState: {
	        title: 'Agent State',
	        width: '10%',
	        list : true,
	        edit : true,
	        options : 'company?action=beanlist&method=slist&actionType=JSON'
	    },
	    zip : {
			title : 'Postal Code',
			width : '25%'
		},
		amsId: {
		    title: 'Agency Management System',
		    width: '30%',
		    options : 'company?action=beanlist&method=amslist&actionType=JSON'
		    
		},

		updateSystemSettings: {
		    title: 'Update system settings',
		    width: '20%',
		    list: false,
		    create:	false,
		    edit:	false,
		    type: 'checkbox',
		    values: { 'false': '', 'true': '' },
		    defaultValue: 'false'
		    
			} ,
		destAddress : {
			title : 'Agent Destination Address',
			width : '25%'
		},
		amsVer: {
		    title: 'amsVer',
		    list: false,
		    create:false,
		    edit:false,
		    width: '20%'
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
		            return '<input type="text" name="remoteClaimDir" id="remoteClaimDir" style="width:200px" value="' + data.record.remoteClaimDir + '" /> ';
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
		            return '<input type="text" name="remotePolicyDir" id="remotePolicyDir" style="width:200px" value="' + data.record.remotePolicyDir + '" />  <br><label>Use Default  <input type="checkbox" name ="chkDefault" id ="chkDefault" ></label>';
		        } else {
		        	return '<input type="text" name="remotePolicyDir" id="remotePolicyDir" style="width:200px" placeholder="Enter download directory for Policy XML" />  <br><label>Use Default  <input type="checkbox" name ="chkDefault" id ="chkDefault" ></label>';
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
		defaultFilename: {
	    title: 'Default Filename',
	    list: false,
	    create: false,
	    width: '20%'
	    
		} ,
		defaultClaimFilename: {
            title: 'Default Claim Filename',
            list: false,
            create: false,
            width: '20%'
            
        },
        defaultPolicyFilename: {
            title: 'Default Policy XML Filename',
            list: false,
            create: false,
            width: '20%'
            
        },
	resetTradingP: {
	    title: 'Reset trading partner, require re-registration:',
	    create:false,
	    width: '15%',
	    list: false,
	    type: 'checkbox',
	    values: { 'false': '', 'true': '' },
	    defaultValue: 'false'
	
	},
	password: {
        title: 'Initial Password',
        width: '15%',
        list: false,
        edit:false,
        input: function(data) {
        	var $pwd =makePasswd();
        	return '<input type="text" name="Password" id ="Password"  style="width:5px" size="10" value="' + $pwd + '" />';
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
    //return data.form.validationEngine('validate');
},
//Dispose validation logic when form is closed
formClosed: function (event, data) {
    data.form.validationEngine('hide');
    data.form.validationEngine('detach');
}
        	
        });
        //$('#PersonTableContainer').jtable('load');
    });
</script>
 
<table height="35" cellpadding="0" cellspacing="0" border="0" align ="right">
<tr>
	<td align="center" valign="top">&nbsp;<a href="#" onclick="AddSearch()" ><img title="Add Agent" border="0" src="<c:url value="images/users_add_new.png"/>"></a></td>
	<td align="center" valign="top">&nbsp;<a href="#" onclick="callSearch()" ><img title="Search Agent" border="0" src="<c:url value="images/users_search_new.png"/>"></a></td>
	<td align="center" valign="top">&nbsp;<a href="javascript:do_action('home');" ><img title="Home" border="0" src="<c:url value="images/home_1.png"/>"></a></td>
<%
if (MenuBean.getCarrierInfo().getContactEmail() != null)
{
%>	<td align="center" valign="top">&nbsp;<a href="mailto:<%= MenuBean.getCarrierInfo().getContactEmail() %>" ><img title="Contact Us" border="0" src="<c:url value="images/contact_new.png"/>"></a></td>
<%
}
%>	<!-- td align="center" valign="top">&nbsp;<a href="logout" ><img title="Log out" border="0" src="<c:url value="images/logout_new.png"/>"></a></td-->
<td align="center" valign="top">&nbsp;<a href="javascript:do_action('log_off');" ><img title="Log out" border="0" src="<c:url value="images/logout_new.png"/>"></a></td>	
</tr>
<div id="PersonTableContainer"></div>
</table>
<%@include file="searchAgent.jsp"%>
