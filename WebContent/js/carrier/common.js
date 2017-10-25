 function checkForClaimActivation() {
	  	  $.ajax({
	  	        url: "company?action=licence&actionType=JSON&method=claimAct",
	  			method: "GET",
	  			async: false, 
	  			success: function(data) {
	  				console.log(data);
	  				if('Y'==data.isClaimActivated){
	  					$("#claimFileInfo").show();
	  					document.getElementById("files").children[2].style.display = "block";
	  				}else{
	  					$("#claimFileInfo").hide();
	  					document.getElementById("files").children[2].style.display = "none";
	  					 
	  				}
	  				
	  			},
	  			error: function(data) {
	  				console.log(data);
	  			}
	  		});
	  		
	}
 
 
 function checkForClaimActivationOnReport() {
 	  $.ajax({
 	        url: "company?action=licence&actionType=JSON&method=claimAct",
 			method: "GET",
 			async: false, 
 			success: function(data) {
 				console.log(data);
 				if('Y'==data.isClaimActivated){
 					$('#ReportContainer').jtable('changeColumnVisibility','claimNumber','visible');
 					var rows = $('table.searchClass tr');
 					var claim = rows.filter('.claimC');
 					claim.show();
 					
 				}else{
 					$('#ReportContainer').jtable('changeColumnVisibility','claimNumber','hidden');
 					var rows = $('table.searchClass tr');
 					var claim = rows.filter('.claimC');
 					claim.hide();
 					 
 				}
 				
 			},
 			error: function(data) {
 				console.log(data);
 			}
 		});
 		
}