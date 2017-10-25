<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<LINK rel="stylesheet" href="<c:url value="/css/custom.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.ActivationDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<link rel="stylesheet" href="css/jquery-ui.css">
<script src="js/jquery-1.11.2.js"></script>
<script src="js/jquery-ui.js"></script>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
	<jsp:include page="javascript.jsp" flush="true" />

	function allow_action() {
		return true;
	}

	function do_addnew() {
		document.tudlform.agentID.value = "";
		do_page_action("tplist_addnew");
	}

	function do_edit(agentnum) {
		document.tudlform.agentID.value = agentnum;
		do_page_action("tplist_edit");
	}

	function do_sort(sortby) {
		document.tudlform.sort_order.value = sortby;
		do_page_action("tplist_sort");
	}

	function on_select_agent(agentnum) {
		document.tudlform.agentID.value = agentnum;
	}

	function jumpTo(letter) {
		document.tudlform.alphaStart.value = letter;
		document.tudlform.startPos.value = 0;
		do_page_action("tplist_alpha");
	}

	function on_select_numrows(selbox) {
		if (selbox == "top") {
			document.tudlform.maxRows.value = document.tudlform.numrows_top.value;
			document.tudlform.numrows_bottom.value = document.tudlform.numrows_top.value;
		} else {
			document.tudlform.maxRows.value = document.tudlform.numrows_bottom.value;
			document.tudlform.numrows_top.value = document.tudlform.numrows_bottom.value;
		}
	}

	function search_by_agentid(editbox) {
		if (editbox == "top") {
			if (!document.tudlform.agentID_top.value) {
				alert("Please enter an agent ID or participant code to search for.");
				document.tudlform.agentID_top.focus();
				return false;
			}
			document.tudlform.agentID.value = document.tudlform.agentID_top.value;
		} else {
			if (!document.tudlform.agentID_bottom.value) {
				alert("Please enter an agent ID or participant code to search for.");
				document.tudlform.agentID_bottom.focus();
				return false;
			}
			document.tudlform.agentID.value = document.tudlform.agentID_bottom.value;
		}

		document.tudlform.agentSearch.value = document.tudlform.agentID.value;
		document.tudlform.search_type.value = "agentid";
		do_page_action("tplist_agentid_search");

		return true;
	}

	function do_continue() {
		do_page_action("tp_Separation_Only");
	}

	function do_cancel() {
		do_page_action("dist_cancel");
	}

	// -->
	
	function do_submit() {
		if (document.tudlform.getCode.value == '')
		{
			alert("Please get a new activation code.");
			document.tudlform.getCode.focus();
			return false;
		}else if (document.tudlform.actCode.value == document.tudlform.getCode.value)
		{
			alert("There is no change in Activation Code,hence no update is required.");
			document.tudlform.getCode.focus();
			return false;
		}
		
		else {
			do_page_action("activationCode");
		}
	}
	
	$(function() {
		$('.special_field_link').live('click', function() {
			$("#a_input_id").val($(this).html());
		});
	});
	
	$(function () {
        $("#btnGetCode").on("click", function () {
            $("#getCode").val(myfunction());
        });
    });

    function myfunction() {
        var retActCode = "";
    	 $.ajax({
             url: "company?action=activationCode&actionType=JSON&method=getActCode",
                 method: "GET",
                 async: false,
                 success: function(data) {
                	 retActCode = data.GetActivatedCode;
                	 document.getElementById("carrierRegMaster").value =data.carrierRegMaster;
                	 
                 },
                 error: function(data) {
                	 alert("An error occurred while processing your request. Please try again.");
                     console.log(data);
                 }
           });
    	 return retActCode;
    }
	
</SCRIPT>

</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" mm:layoutgroup="true" bgcolor="#FFFFFF">
		<TR>
			<TD>
				<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="black">
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
			<td>
				<table width="80%" height="100%" border="0" cellpadding="0" cellspacing="0" align="center">
					<tr>
						<td class="docbody">
							<FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
								<INPUT type="hidden" name="action" value=""> <br><br><br><br>
								<div id="wrapper">
									<fieldset>
										<legend>TEAM-UP Licence</legend>
										<div>
											<div class="small">Activation Code</div>
											<input id="actCode" name="actCode" type="text"
												value="<%=DisplayBean.getCurrentActivationCode()%>"
												readonly="readonly">
												
											<!-- div class="small">
												<a href="#" title="Get New Activation Code" style="text-decoration: none;" 
												class="special_field_link">Get New Activation Code</a>
											</div>
											<input id="a_input_id" type="text"-->
											
											<div align="center">
												<input type="button" class="btnGetCode" id="btnGetCode" value="Get New Activation Code">
											</div>
											<input type="text" name="getCode" id="getCode" readonly="readonly"/> 
											<input type="hidden" name="carrierRegMaster" id="carrierRegMaster"/> 
											<div align="center"><input type="submit" onclick="do_submit();" value="Update Activation Code"></div>
											<div align="center"><font size="0.5px" color="red">Expiration Date : <%=DisplayBean.getExpiredDate()%></font></div>
										</div>
									</fieldset>
								</div>
							</FORM>
						</td>
					</tr>
				</table>
			</td>
		</TR>
		<TR>
			<TD>
				<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="black">
					<TR>
						<TD class="pagebar" nowrap="nowrap" width="100%"></TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<%@ include file="footer.jsp"%>
</BODY>
</HTML>
