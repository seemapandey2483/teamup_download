<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AmsClaimInfoDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
function do_load()
{ <%
  if (DisplayBean.isDuplicateSystem())
  {
%>
	alert("The system ID you entered already exists.  Please enter a new system ID, or return to the Agency Vendor System list to edit the existing system.");
<%
  }
  
  if (DisplayBean.isNewAms())
  {
%>
	document.tudlform.amsid.focus();
<%
  }
  else
  {
%>
	document.tudlform.amsname.focus();
<%
  }
%>
	return true;
}

function allow_action()
{
	if (document.tudlform.data_changed.value == "Y")
	{
		msg = "You are about to lose any changes you have made on this page.  To save your changes, click 'Cancel' and then use the 'Save' button at the bottom of the screen.";
		if (!confirm(msg))
			return false;
	}
	
	return true;
}

function throw_change_flag()
{
	document.tudlform.data_changed.value = "Y";
}

function save_is_valid()
{ <%
if (DisplayBean.isNewAms())
{
%>
	if (!document.tudlform.amsid.value)
	{
		alert("You must enter a unique identifier for this vendor system.");
		document.tudlform.amsid.focus();
		return false;
	}
<%
}
%>
	if (!document.tudlform.amsname.value)
	{
		alert("Please enter a display name for this vendor system.");
		document.tudlform.amsname.focus();
	}
	else if (!document.tudlform.control_type.value)
	{
		alert("Please select a registration control type for this vendor system.");
		document.tudlform.control_type.focus();
	}
<%
if (DisplayBean.isNewAms())
{
%>
	else if (document.tudlform.sort_alpha.checked == false && (!document.tudlform.sort_seq.value || !valid_integer(document.tudlform.sort_seq.value)))
	{
		alert("Please enter a valid sort sequence for this vendor system.");
		document.tudlform.sort_seq.focus();
	}
<%
}
else
{
%>
	else if (!document.tudlform.sort_seq.value || !valid_integer(document.tudlform.sort_seq.value))
	{
		alert("Please enter a valid sort sequence for this vendor system.");
		document.tudlform.sort_seq.focus();
	}
<%
}
%>
	else
	{ <%
if (DisplayBean.isNewAms())
{
%>
		if (document.tudlform.sort_alpha.checked == true)
			document.tudlform.sort_seq.value = "0";
<%
}
%>
		if (parseInt(document.tudlform.sort_seq.value) >= 999)
		{
			if (!confirm("PLEASE NOTE:  Systems with a sort sequence of '999' or greater will be included at the end of the list after an alphabetical resort."))
				return false;
		}
		
		return true;
	}
	
	return false;
}

function do_save()
{
	if (save_is_valid() == true)
	{
		do_page_action("ams_saveclaim");
	}
}

function do_edit_help()
{
	if (save_is_valid() == true)
	{
		document.tudlform.edit_help.value = "Y";
		do_page_action("ams_saveclaim");
	}
}

<%
if (!DisplayBean.isNewAms())
{
%>
function do_delete()
{
<%
  if (DisplayBean.getAgentCount() > 0)
  {
    String delMsg = "Delete invalid: There ";
    if (DisplayBean.getAgentCount() == 1)
      delMsg += "is 1 agent";
    else
      delMsg += "are " + DisplayBean.getAgentCount() + " agents";
    delMsg += " currently configured with this vendor system.";
%>
	alert("<%= delMsg %>");
<%
  }
  else
  {
%>
	if (confirm("Delete this custom vendor system?"))
	{
		do_page_action("ams_delete");
	}
<%
  }
%>
}
<%
}
%>

<jsp:include page="javascript.jsp" flush="true" />
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
    <TD class="menubar" nowrap="nowrap"><jsp:include page="menu.jsp" flush="true" /></TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="data_changed" value="N">
    <INPUT type="hidden" name="newsystem" value="<% if (DisplayBean.isNewAms()) { %>Y<% } else { %>N<% } %>">
    <INPUT type="hidden" name="edit_help" value="N">
    <INPUT type="hidden" name="customSystem" value="Y">

<!-- BEGIN BODY -->

    <H1>Agency Vendor System Maintenance<BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
<%
if (!DisplayBean.isNewAms())
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">System Name:</TD>
            <TD class="qcell_text" width="70%"><B><%= DisplayBean.getDisplayName() %></B></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">System ID:</TD>
            <TD class="qcell_text" width="70%"><%
if (DisplayBean.isNewAms())
{
%><INPUT type="text" name="amsid" size="10" maxlength="10" value="<%= DisplayBean.getId() %>" onchange="throw_change_flag()"><%
}
else
{
%><B><%= DisplayBean.getId() %></B><INPUT type="hidden" name="amsid" value="<%= DisplayBean.getId()%>"><%
}
%>
</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Vendor Name:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="vendor" size="20" maxlength="20" value="<%= DisplayBean.getVendor() %>" onchange="throw_change_flag()">&nbsp;&nbsp;&nbsp;<FONT size="-1">(if available)</FONT></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">System Name:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="amsname" size="50" maxlength="50" value="<%= DisplayBean.getName() %>" onchange="throw_change_flag()">&nbsp;&nbsp;&nbsp;<FONT size="-1">(required)</FONT></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Display Name Note:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="note" size="25" maxlength="25" value="<%= DisplayBean.getNote() %>" onchange="throw_change_flag()">&nbsp;&nbsp;&nbsp;<FONT size="-1">(optional)</FONT></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Sort Sequence:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="sort_seq" size="5" maxlength="3" value="<%= DisplayBean.getSortSequence() %>" onchange="throw_change_flag()"></TD>
          </TR>
<%
if (DisplayBean.isNewAms())
{
%>          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Sort systems alphabetically:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="checkbox" name="sort_alpha" value="Y" checked></TD>
          </TR><%
}
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%" nowrap="nowrap"><B>AL3 Download Directory Settings:&nbsp; &nbsp;</B></TD>
            <TD class="qcell_text" width="70%"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Default Download Directory:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="company_dir" size="50" maxlength="255" value="<%= DisplayBean.getCompanyDirectory() %>" onchange="throw_change_flag()"></TD>
          </TR>
<%
if (DisplayBean.getDirectoryNotes().length() > 0)
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Directory Notes:</TD>
            <TD class="qcell_text" width="70%"><%= DisplayBean.getDirectoryNotes() %></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Directory Recognition:</TD>
            <TD class="qcell_text" width="70%">
              <SELECT name="control_type" onchange="throw_change_flag()">
                <OPTION value="default"<% if (DisplayBean.getRegistrationControlType().equals("default")) { %> selected<% } %>>Search for default directory</OPTION>
                <OPTION value="createdir"<% if (DisplayBean.getRegistrationControlType().equals("createdir")) { %> selected<% } %>>Use default directory, create if not available</OPTION>
                <OPTION value="wildcard"<% if (DisplayBean.getRegistrationControlType().equals("wildcard")) { %> selected<% } %>>Search for directories using wildcard (%1) characters</OPTION>
                <OPTION value="afw"<% if (DisplayBean.getRegistrationControlType().equals("afw")) { %> selected<% } %>>Detect settings in AFW.INI file (AfW only)</OPTION>
              </SELECT>
            </TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Create Cleanup Batch File:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="checkbox" name="batchfile_flag" value="Y"<% if (DisplayBean.isBatchFileFlag()) { %> checked<% } %> onclick="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%" nowrap="nowrap"><B>AL3 Download Filename Settings:&nbsp; &nbsp;</B></TD>
            <TD class="qcell_text" width="70%"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Default Download Filename:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="company_filename" size="15" maxlength="50" value="<%= DisplayBean.getCompanyFilename() %>" onchange="throw_change_flag()"></TD>
          </TR>
<%
if (DisplayBean.getFilenameNotes().length() > 0)
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Filename Notes:</TD>
            <TD class="qcell_text" width="70%"><%= DisplayBean.getFilenameNotes() %></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Filename Increment Method:</TD>
            <TD class="qcell_text" width="70%">
              <SELECT name="filename_incr_type" onchange="throw_change_flag()">
                <OPTION value="X"<% if (DisplayBean.getFilenameIncrType().equals("X")) { %> selected<% } %>>Increment filename extension (file.001, file.002, etc)</OPTION>
                <OPTION value="#"<% if (DisplayBean.getFilenameIncrType().equals("#")) { %> selected<% } %>>Replace '#' characters (file##.dat = file01.dat)</OPTION>
                <OPTION value="A"<% if (DisplayBean.isAppendFlag()) { %> selected<% } %>>No increment - append to existing file</OPTION>
              </SELECT>
            </TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Allow Agent to Change Filename:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="checkbox" name="agent_change_flag" value="Y"<% if (DisplayBean.isAgentChangeFilename()) { %> checked<% } %> onclick="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;<BR>&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Directory Notes:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="dir_notes" size="50" maxlength="255" value="<%= DisplayBean.getDirectoryNotes() %>" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Filename Notes:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="filename_notes" size="50" maxlength="255" value="<%= DisplayBean.getFilenameNotes() %>" onchange="throw_change_flag()"></TD>
          </TR>
          
          <!-- added for Claim start -->
          
<TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%" nowrap="nowrap"><B>Claim Download Directory Settings:&nbsp; &nbsp;</B></TD>
            <TD class="qcell_text" width="70%"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Default Download Directory:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="company_cdir" size="50" maxlength="255" value="<%= DisplayBean.getCompanyClaimDir() %>" onchange="throw_change_flag()"></TD>
          </TR>

          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Directory Recognition:</TD>
            <TD class="qcell_text" width="70%">
              <SELECT name="control_ctype" onchange="throw_change_flag()">
                <OPTION value="default"<% if (DisplayBean.getRegClaimControlType().equals("default")) { %> selected<% } %>>Search for default directory</OPTION>
                <OPTION value="createdir"<% if (DisplayBean.getRegClaimControlType().equals("createdir")) { %> selected<% } %>>Use default directory, create if not available</OPTION>
                <OPTION value="wildcard"<% if (DisplayBean.getRegClaimControlType().equals("wildcard")) { %> selected<% } %>>Search for directories using wildcard (%1) characters</OPTION>
                <OPTION value="afw"<% if (DisplayBean.getRegClaimControlType().equals("afw")) { %> selected<% } %>>Detect settings in AFW.INI file (AfW only)</OPTION>
              </SELECT>
            </TD>
          </TR>
          
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%" nowrap="nowrap"><B>Claims Download Filename Settings:&nbsp; &nbsp;</B></TD>
            <TD class="qcell_text" width="70%"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Default Download Filename:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="company_cfilename" size="15" maxlength="50" value="<%= DisplayBean.getCompanyClaimFilename() %>" onchange="throw_change_flag()"></TD>
          </TR>

          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Filename Increment Method:</TD>
            <TD class="qcell_text" width="70%">
              <SELECT name="filename_incr_ctype" onchange="throw_change_flag()">
                <OPTION value="X"<% if (DisplayBean.getFilenameClaimIncrementType().equals("X")) { %> selected<% } %>>Increment filename extension (file.001, file.002, etc)</OPTION>
                <OPTION value="#"<% if (DisplayBean.getFilenameClaimIncrementType().equals("#")) { %> selected<% } %>>Replace '#' characters (file##.dat = file01.dat)</OPTION>
                <OPTION value="A"<% if (DisplayBean.isAppendFlag()) { %> selected<% } %>>No increment - append to existing file</OPTION>
              </SELECT>
            </TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Allow Agent to Change Filename:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="checkbox" name="agent_change_cflag" value="Y"<% if (DisplayBean.isAgentChangeClaimFilename()) { %> checked<% } %> onclick="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;<BR>&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
               
          <!--  added for claim end -->
          
          
            <!-- added for Policy start -->
          
<TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%" nowrap="nowrap"><B>Policy XML Download Directory Settings:&nbsp; &nbsp;</B></TD>
            <TD class="qcell_text" width="70%"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Default Download Directory:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="company_pdir" size="50" maxlength="255" value="<%= DisplayBean.getCompanyPolicyDir() %>" onchange="throw_change_flag()"></TD>
          </TR>

          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Directory Recognition:</TD>
            <TD class="qcell_text" width="70%">
              <SELECT name="control_ptype" onchange="throw_change_flag()">
                <OPTION value="default"<% if (DisplayBean.getRegPolicyControlType().equals("default")) { %> selected<% } %>>Search for default directory</OPTION>
                <OPTION value="createdir"<% if (DisplayBean.getRegPolicyControlType().equals("createdir")) { %> selected<% } %>>Use default directory, create if not available</OPTION>
                <OPTION value="wildcard"<% if (DisplayBean.getRegPolicyControlType().equals("wildcard")) { %> selected<% } %>>Search for directories using wildcard (%1) characters</OPTION>
                <OPTION value="afw"<% if (DisplayBean.getRegPolicyControlType().equals("afw")) { %> selected<% } %>>Detect settings in AFW.INI file (AfW only)</OPTION>
              </SELECT>
            </TD>
          </TR>
          
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%" nowrap="nowrap"><B>Policy XML Download Filename Settings:&nbsp; &nbsp;</B></TD>
            <TD class="qcell_text" width="70%"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Default Download Filename:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="company_pfilename" size="15" maxlength="50" value="<%= DisplayBean.getCompanyPolicyFilename() %>" onchange="throw_change_flag()"></TD>
          </TR>

          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Filename Increment Method:</TD>
            <TD class="qcell_text" width="70%">
              <SELECT name="filename_incr_ptype" onchange="throw_change_flag()">
                <OPTION value="X"<% if (DisplayBean.getFilenamePolicyIncrementType().equals("X")) { %> selected<% } %>>Increment filename extension (file.001, file.002, etc)</OPTION>
                <OPTION value="#"<% if (DisplayBean.getFilenamePolicyIncrementType().equals("#")) { %> selected<% } %>>Replace '#' characters (file##.dat = file01.dat)</OPTION>
                <OPTION value="A"<% if (DisplayBean.isAppendFlag()) { %> selected<% } %>>No increment - append to existing file</OPTION>
              </SELECT>
            </TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Allow Agent to Change Filename:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="checkbox" name="agent_change_pflag" value="Y"<% if (DisplayBean.isAgentChangeClaimFilename()) { %> checked<% } %> onclick="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;<BR>&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
               
          <!--  added for policy end -->
          
        </TBODY>
      </TABLE>


<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_page_action('menu_ams')">Cancel</A></TD>
<%
if (!DisplayBean.isNewAms())
{
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_delete()">Delete</A></TD>
<%
}
%>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_save()">Save</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_edit_help()">Save &amp; Edit Help Text</A></TD>
          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
 <%--  <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR> --%>
</TABLE>
<%@ include file="footer.jsp" %>
</BODY>
</HTML>
