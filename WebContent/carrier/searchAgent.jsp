<Script>
$(document).ready(function(){    

    $('#srchAgentdialogFromHome').hide(); 
});
function callSearch()  {
	var viewportWidth = window.innerWidth-20;
	var viewportHeight = window.innerHeight-20;
	if (viewportWidth > 1000) viewportWidth = 400;
	if (viewportHeight > 500) viewportHeight = 200;
	
	var dialogContainer=document.body.appendChild(document.createElement("div"));
	dialogContainer.style.position="fixed";
	dialogContainer.style.top=dialogContainer.style.left="50%";
	
	
    $( "#srchAgentdialogFromHome" ).dialog({
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
                          searchAgentFromHome();
                        }
                      

                      }
                    ]
        }); 
    	 $( "#srchAgentdialogFromHome" ).dialog( "open" );
    }
    
function reSetSearchDialog() {
	 $( "#srchAgentdialogFromHome" ).dialog( "close" );
	 $( "#srchAgentdialogFromHome" ).dialog( "open" );
    $( "#srchAgentdialogFromHome" ).dialog({ position: 'top' });
}
function searchAgentFromHome() {
	if(document.getElementById('searchAgentIdH')){
		document.tudlsearchAgentform.searchAgentIdHome.value =  document.getElementById('searchAgentIdH').value;
	}
	if (document.getElementById('searchAgentNameH')) {
		document.tudlsearchAgentform.searchAgentNameHome.value =  document.getElementById('searchAgentNameH').value;	
	}
	 
	document.tudlsearchAgentform.action.value = 'tplist_default_new';
	document.tudlsearchAgentform.submit();
}

function searchKeyPress(e)
{
    // look for window.event in case event isn't passed in
    e = e || window.event;
    if (e.keyCode == 13)
    {
    	searchAgentFromHome();
    	$( "#srchAgentdialogFromHome" ).dialog( "close" );
        return false;
    }
    return true;
}
</Script>

<FORM name="tudlsearchAgentform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
	<INPUT type="hidden" name="agentID" id="agentID" value="">
	  <INPUT type="hidden" name="action" value="">
	<INPUT type="hidden" name="searchAgentIdHome" id="searchAgentIdHome" value="">
	<INPUT type="hidden" name="searchAgentNameHome" id="searchAgentNameHome" value="">
<div id="srchAgentdialogFromHome" title="Search Trading Partner">
 <TABLE border='0'>
			<TR>
				<TD><strong><label class='jtable-input-label'>Agent Id:</label></strong></TD>
				<TD><input type='text' id='searchAgentIdH' name='searchAgentIdH' onkeypress="return searchKeyPress(event);"/></TD>
			</TR>
			<TR>
				<TD><strong><label class='jtable-input-label'>Agent Name:</label></strong></TD>
				<TD><input type='text' id='searchAgentNameH' name='searchAgentNameH' onkeypress="return searchKeyPress(event);"/></TD>
			</TR>
		</TABLE> 
</div>
</FORM>