<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.CarGroupNameDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	document.tudlform.groupName.focus();
	return true;
}

function allow_action()
{
	return true;
}

function do_submit()
{
	if (!document.tudlform.groupName.value)
	{
		alert("Please enter the agency rollout group name.");
		document.tudlform.groupName.focus();
	}
	else if (!document.tudlform.groupDesc.value)
	{
		alert("Please enter a description for the agency rollout group.");
		document.tudlform.groupDesc.focus();
	}
	else
	{
		document.tudlform.action.value = "car_create_group";
		document.tudlform.submit();
	}
}
// -->
</SCRIPT>
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
    <TD class="menubar" nowrap="nowrap">&nbsp;</TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
      <INPUT type="hidden" name="action" value="">

<!-- BEGIN BODY -->

      <H1>CAR: Connective's Accelerated Rollout Process<BR>
          Rollout Group Creation<BR>&nbsp;
      </H1>
      
      <table>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td colspan="2"><font color="red">
			  The group '<b><%= DisplayBean.getGroupName() %></b>' already exists. 
			  Please enter a unique name to create a new agent rollout group, or
			  indicate below to add new agents to the existing group.</font>
			</td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="right">&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="right">Rollout Group Name:&nbsp;</td>
			<td><INPUT type="text" name="groupName" size="25" maxlength="30" value="<%= DisplayBean.getGroupName() %>"></td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="right">Group Description:&nbsp;</td>
			<td><INPUT type="text" name="groupDesc" size="50" maxlength="255" value="<%= DisplayBean.getGroupDesc() %>"></td>
		</tr>
		<tr valign="middle">
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="right">Add to Existing Group?&nbsp;</td>
			<td>
				<INPUT type="radio" name="addToExisting" value="Y"> Yes &nbsp;
				<INPUT type="radio" name="addToExisting" value="N" checked> No
			</td>
		</tr>
      </table>

<!-- END BODY -->
    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_submit()">Continue</A></TD>
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
