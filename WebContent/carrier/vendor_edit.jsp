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
{
	document.tudlform.sort_seq.focus();
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
{
	if (!document.tudlform.sort_seq.value || !valid_integer(document.tudlform.sort_seq.value))
	{
		alert("Please enter a valid sort sequence for this vendor system.");
		document.tudlform.sort_seq.focus();
	}
	else
	{
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

<%
if (DisplayBean.isChangeDirectory())
{
%>
function setDefaultDir()
{
	document.tudlform.company_dir.value = "<%= DisplayBean.getDefaultDirectoryEscaped() %>";
	document.tudlform.company_dir.focus();
}
<%
}
if (DisplayBean.isChangeFilename())
{
%>
function setDefaultFile()
{
	document.tudlform.company_filename.value = "<%= DisplayBean.getDefaultFilename() %>";
	document.tudlform.company_filename.focus();
}
<%
}
if (DisplayBean.isCompanyChangeClaimFilename())
{
%>
function setDefaultClaimFile()
{
	document.tudlform.company_cfilename.value = "<%= DisplayBean.getDeafultClaimFileName() %>";
	document.tudlform.company_cfilename.focus();
}
<%
}

if (DisplayBean.isCompanyChangePolicyFilename())
{
%>
function setDefaultPolicyFile()
{
	document.tudlform.company_pfilename.value = "<%= DisplayBean.getDeafultPolicyFileName() %>";
	document.tudlform.company_pfilename.focus();
}
<%
}
%>


function show_batchfile_msg()
{
	alert("In the agency application, clicking this URL will initiate a download of the 'DLCLEAN.BAT' batch file.");
}

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
    <INPUT type="hidden" name="newsystem" value="N">
    <INPUT type="hidden" name="edit_help" value="N">
    <INPUT type="hidden" name="customSystem" value="N">
    <INPUT type="hidden" name="amsid" value="<%= DisplayBean.getId()%>">

<!-- BEGIN BODY -->

    <H1>Agency Vendor System Maintenance<BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">System Name:</TD>
            <TD class="qcell_text" width="70%"><B><%= DisplayBean.getDisplayName() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">System ID:</TD>
            <TD class="qcell_text" width="70%"><B><%= DisplayBean.getId() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Sort Sequence:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="sort_seq" size="5" maxlength="3" value="<%= DisplayBean.getSortSequence() %>" onchange="throw_change_flag()"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Claim Supported:</TD>
            <TD class="qcell_text" width="70%"><% if (DisplayBean.isClaimSupported()) { %>Yes<% } else { %>No<% } %></TD>
          </TR>
           <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Policy XML Supported:</TD>
            <TD class="qcell_text" width="70%"><% if (DisplayBean.isPolicyXMLSupported()) { %>Yes<% } else { %>No<% } %></TD>
          </TR>
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
            <TD class="qcell_text" width="70%"><%= DisplayBean.getDefaultDirectory() %></TD>
          </TR>
<%
if (DisplayBean.isChangeDirectory())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Download Directory:</TD>
            <TD class="qcell_text" width="70%">
              <INPUT type="text" name="company_dir" size="40" maxlength="255" value="<%= DisplayBean.getCompanyDirectory() %>" onchange="throw_change_flag()">&nbsp;
              <INPUT type="button" value="Use default" onclick="javascript:setDefaultDir()">
            </TD>
          </TR>
<%
}
if (DisplayBean.getDirectoryNotes().length() > 0)
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Directory Notes:</TD>
            <TD class="qcell_text" width="70%"><%= DisplayBean.getDirectoryNotes() %></TD>
          </TR>
<%
}

String regControl = "";
if ("default".equals(DisplayBean.getRegistrationControlType()))
	regControl = "Search for default directory";
else if ("createdir".equals(DisplayBean.getRegistrationControlType()))
	regControl = "Use default directory, create if not available";
else if ("wildcard".equals(DisplayBean.getRegistrationControlType()))
	regControl = "Search for directories using wildcard (%1) characters";
else if ("afw".equals(DisplayBean.getRegistrationControlType()))
	regControl = "Detect drive settings in AFW.INI file";
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Directory Recognition:</TD>
            <TD class="qcell_text" width="70%"><%= regControl %></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Create Cleanup Batch File:</TD>
            <TD class="qcell_text" width="70%"><% if (DisplayBean.isBatchFileFlag()) { %>Yes<% } else { %>No<% } %></TD>
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
            <TD class="qcell_text" width="70%"><%= DisplayBean.getDefaultFilename() %></TD>
          </TR>
<%
if (DisplayBean.isChangeFilename())
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Download Filename:</TD>
            <TD class="qcell_text" width="70%">
              <INPUT type="text" name="company_filename" size="15" maxlength="50" value="<%= DisplayBean.getCompanyFilename() %>" onchange="throw_change_flag()">&nbsp;
              <INPUT type="button" value="Use default" onclick="javascript:setDefaultFile()">
            </TD>
          </TR>
<%
}
if (DisplayBean.getFilenameNotes().length() > 0)
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Filename Notes:</TD>
            <TD class="qcell_text" width="70%"><%= DisplayBean.getFilenameNotes() %></TD>
          </TR>
<%
}

String fileIncr = "";
if (DisplayBean.isAppendFlag() || "A".equals(DisplayBean.getFilenameIncrType()))
	fileIncr = "No increment - append to existing file";
else if ("X".equals(DisplayBean.getFilenameIncrType()))
	fileIncr = "Increment filename extension (file.001, file.002, etc)";
else if ("#".equals(DisplayBean.getFilenameIncrType()))
	fileIncr = "Replace '#' characters (file##.dat &raquo; file01.dat, in###.dat &raquo; in001.dat, etc)";
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Filename Increment Method:</TD>
            <TD class="qcell_text" width="70%"><%= fileIncr %></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Allow Agent to Change Filename:</TD>
            <TD class="qcell_text" width="70%"><% if (DisplayBean.isAgentChangeFilename()) { %>Yes<% } else { %>No<% } %></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;<BR>&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          
          
          <!-- Properties added for Claim Start -->
            <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%" nowrap="nowrap"><B>Claims Download Directory Settings:&nbsp; &nbsp;</B></TD>
            <TD class="qcell_text" width="70%"></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Default Download Directory:</TD>
            <TD class="qcell_text" width="70%"><%= DisplayBean.getDefaultClaimDirectory()!=null?DisplayBean.getDefaultClaimDirectory():"" %></TD>
          </TR>
<%
if (DisplayBean.isCompanyClaimChangeDirectory())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Download Directory:</TD>
            <TD class="qcell_text" width="70%">
              <INPUT type="text" name="company_cdir" size="40" maxlength="255" value="<%= DisplayBean.getCompanyClaimDir() !=null?DisplayBean.getCompanyClaimDir():"" %>" onchange="throw_change_flag()">&nbsp;
              <INPUT type="button" value="Use default" onclick="javascript:setDefaultDir()">
            </TD>
          </TR>
<%
}


String regClaimControl = "";
if ("default".equals(DisplayBean.getRegClaimControlType()))
	regClaimControl = "Search for default directory";
else if ("createdir".equals(DisplayBean.getRegClaimControlType()))
	regClaimControl = "Use default directory, create if not available";
else if ("wildcard".equals(DisplayBean.getRegClaimControlType()))
	regClaimControl = "Search for directories using wildcard (%1) characters";
else if ("afw".equals(DisplayBean.getRegClaimControlType()))
	regClaimControl = "Detect drive settings in AFW.INI file";
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Directory Recognition:</TD>
            <TD class="qcell_text" width="70%"><%= regClaimControl %></TD>
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
            <TD class="qcell_text" width="70%"><%= DisplayBean.getDeafultClaimFileName()!=null?DisplayBean.getDeafultClaimFileName():"" %></TD>
          </TR>
<%
if (DisplayBean.isCompanyChangeClaimFilename())
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Download Filename:</TD>
            <TD class="qcell_text" width="70%">
              <INPUT type="text" name="company_cfilename" size="15" maxlength="50" value="<%= DisplayBean.getCompanyClaimFilename()!=null?DisplayBean.getCompanyClaimFilename():"" %>" onchange="throw_change_flag()">&nbsp;
              <INPUT type="button" value="Use default" onclick="javascript:setDefaultClaimFile()">
            </TD>
          </TR>
<%
}


String fileClaimIncr = "";
if (DisplayBean.isAppendFlag() || "A".equals(DisplayBean.getFilenameClaimIncrementType()))
	fileClaimIncr = "No increment - append to existing file";
else if ("X".equals(DisplayBean.getFilenameClaimIncrementType()))
	fileClaimIncr = "Increment filename extension (file.001, file.002, etc)";
else if ("#".equals(DisplayBean.getFilenameClaimIncrementType()))
	fileClaimIncr = "Replace '#' characters (file##.dat &raquo; file01.dat, in###.dat &raquo; in001.dat, etc)";
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Filename Increment Method:</TD>
            <TD class="qcell_text" width="70%"><%= fileClaimIncr %></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Allow Agent to Change Filename:</TD>
            <TD class="qcell_text" width="70%"><% if (DisplayBean.isAgentChangeClaimFilename()) { %>Yes<% } else { %>No<% } %></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;<BR>&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          <!--  properties added for Claim End -->
          

         <!-- Properties added for Policy XML Start -->
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
            <TD class="qcell_text" width="70%"><%= DisplayBean.getDefaultPolicyDirectory()!=null?DisplayBean.getDefaultPolicyDirectory():"" %></TD>
          </TR>
<%
if (DisplayBean.isCompanyPolicyChangeDirectory())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Download Directory:</TD>
            <TD class="qcell_text" width="70%">
              <INPUT type="text" name="company_pdir" size="40" maxlength="255" value="<%= DisplayBean.getCompanyPolicyDir()!=null?DisplayBean.getCompanyPolicyDir():"" %>" onchange="throw_change_flag()">&nbsp;
              <INPUT type="button" value="Use default" onclick="javascript:setDefaultDir()">
            </TD>
          </TR>
<%
}


String regPolicyControl = "";
if ("default".equals(DisplayBean.getRegPolicyControlType()))
	regPolicyControl = "Search for default directory";
else if ("createdir".equals(DisplayBean.getRegPolicyControlType()))
	regPolicyControl = "Use default directory, create if not available";
else if ("wildcard".equals(DisplayBean.getRegPolicyControlType()))
	regPolicyControl = "Search for directories using wildcard (%1) characters";
else if ("afw".equals(DisplayBean.getRegPolicyControlType()))
	regPolicyControl = "Detect drive settings in AFW.INI file";
	
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Directory Recognition:</TD>
            <TD class="qcell_text" width="70%"><%= regPolicyControl %></TD>
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
            <TD class="qcell_text" width="70%"><%= DisplayBean.getDeafultPolicyFileName()!=null?DisplayBean.getDeafultPolicyFileName():"" %></TD>
          </TR>
<%
if (DisplayBean.isCompanyChangePolicyFilename())
{
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Download Filename:</TD>
            <TD class="qcell_text" width="70%">
              <INPUT type="text" name="company_pfilename" size="15" maxlength="50" value="<%= DisplayBean.getCompanyPolicyFilename()!=null?DisplayBean.getCompanyPolicyFilename():"" %>" onchange="throw_change_flag()">&nbsp;
              <INPUT type="button" value="Use default" onclick="javascript:setDefaultPolicyFile()">
            </TD>
          </TR>
<%
}


String filePolicyIncr = "";
if (DisplayBean.isAppendFlag() || "A".equals(DisplayBean.getFilenamePolicyIncrementType()))
	filePolicyIncr = "No increment - append to existing file";
else if ("X".equals(DisplayBean.getFilenamePolicyIncrementType()))
	filePolicyIncr = "Increment filename extension (file.001, file.002, etc)";
else if ("#".equals(DisplayBean.getFilenamePolicyIncrementType()))
	filePolicyIncr = "Replace '#' characters (file##.dat &raquo; file01.dat, in###.dat &raquo; in001.dat, etc)";
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Filename Increment Method:</TD>
            <TD class="qcell_text" width="70%"><%= filePolicyIncr %></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">Allow Agent to Change Filename:</TD>
            <TD class="qcell_text" width="70%"><% if (DisplayBean.isAgentChangePolicyFilename()) { %>Yes<% } else { %>No<% } %></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;<BR>&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          <!--  properties added for Claim End -->
          
<%
if (DisplayBean.getSetupNotes() != null && !DisplayBean.getSetupNotes().equals(""))
{
%>
          <TR class="qcell_row" valign="middle">
            <TD colspan="2"><hr></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2"><B>Setup Notes:</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2"><%= DisplayBean.getSetupNotes() %></TD>
          </TR>
<%
   if (DisplayBean.isBatchFileFlag())
   {
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2">
              A clean-up utility can be copied to your desktop to remove any old download files.  
              <A href="javascript:show_batchfile_msg()">Click here</A> to download this file and save 
              it to your Windows desktop.  Then run the "dlclean.bat" icon from your desktop each day 
              after successfully importing your download files into your agency management system.
            </TD>
          </TR>
<%
   }
%>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;<BR>&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
<%
}
if (DisplayBean.getRuntimeNotes() != null && !DisplayBean.getRuntimeNotes().equals(""))
{
%>
          <TR class="qcell_row" valign="middle">
            <TD colspan="2"><hr></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2"><B>Runtime Notes:</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2"><%= DisplayBean.getRuntimeNotes() %></TD>
          </TR>
          <TR class="qcell_row" valign="middle">
            <TD class="qcell_question" align="right" width="30%">&nbsp;<BR>&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
<%
}
%>
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="#" onclick="javascript:do_save()">Save</A></TD>
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
