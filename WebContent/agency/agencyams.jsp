<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.AgencyRegisterDisplayBean" />
<TITLE><%= DisplayBean.getCarrierInfo().getName() %> - TEAM-UP Download Agency Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
function do_load()
{
	document.tudlform.amsVersion.focus();

	return true;
}

function allow_action()
{
	return true;
}

<jsp:include page="javascript.jsp" flush="true" />

function do_save()
{
	document.tudlform.remoteDirectory.value = document.tudlform.ucAMSPath.useLocation;
	if (!document.tudlform.ucAMSPath.bProceed)
	{
		alert("Please click the 'SELECT PATH' button to confirm your choice.");		
	}
	else if (!document.tudlform.remoteDirectory.value)
	{
		alert("Please enter a directory path to store download files.");
	}
<%
if (DisplayBean.canAgentChangeFilename())
{
%>	else if (!document.tudlform.filename.value)
	{
		alert("You must enter a default filename to be used for downloaded files, or click 'Use Default' for the company default filename.");
		document.tudlform.filename.focus();
	}
<%
}
%>
	else
	{
<%
if (DisplayBean.getControlType().equalsIgnoreCase("afw"))
{
%>		document.tudlform.versionDetected.value = document.tudlform.ucAMSPath.afw_version;
		if (document.tudlform.versionDetected.value)
			document.tudlform.amsVersion.value = document.tudlform.versionDetected.value;
<%
}
%>
		if (document.tudlform.amsVersion.value != "<%= DisplayBean.getAmsVersion() %>")
			document.tudlform.vendor_changed.value = "Y";
		
		do_page_action("agt_ams_save");
	}
}

<%
if (DisplayBean.canAgentChangeFilename())
{
%>
function use_default_filename()
{
	document.tudlform.filename.value = "<%= DisplayBean.getAmsFilename() %>";
}
<%
}
%>
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
<%
if (DisplayBean.isAgentRegistered())
{
%>          <TD class="actionbar" nowrap="nowrap"><jsp:include page="actionbar.jsp" flush="true" /></TD>
<%
}
else
{
%>          <TD class="actionbar" nowrap="nowrap"><jsp:include page="actionbar_nohome.jsp" flush="true" /></TD>
<%
}
%>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap"><%
if (DisplayBean.isAgentRegistered())
{
	// display nothing
}
else
{
%><a href="javascript:void(0);" onClick="javascript:open_prn_win('agency/print_agencyams.jsp')" class="menu">Print Instructions</a><%
}
%></TD>
  </TR>
  <TR>
    <TD class="blankbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">
    <INPUT type="hidden" name="amsid" value="<%= DisplayBean.getAmsId() %>">
    <INPUT type="hidden" name="remoteDirectory" value="">
    <INPUT type="hidden" name="versionDetected" value="">
    <INPUT type="hidden" name="vendor_changed" value="<%= DisplayBean.getVendorChanged() %>">
<%
if (!DisplayBean.canAgentChangeFilename())
{
%>    <INPUT type="hidden" name="filename" value="<%= DisplayBean.getAgentFilename() %>">
<%
}
%>

<!-- BEGIN BODY -->

<%
if (DisplayBean.isAgentRegistered())
{
%>
    <H1>Change Agency System Settings<BR>&nbsp;</H1>
<%
}
else
{
%>
    <H1>TEAM-UP Download Registration - Agency System Settings <IMG border="0" src="<c:url value="/images/qBubble.gif"/>" alt="Defaults for all major agency systems have been defined in TEAM-UP.  You may have to hit 'Select Path' or hit 'Change Directory' if you are sure that your agency uses a different directory for Download." width="20" height="22"><BR>&nbsp;</H1>
<%
}
%>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="2">
              <B>TEAM-UP Download will now search for your download directory based 
              on your agency management system vendor's recommended defaults.</B><BR>&nbsp;
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Agency Management System:<BR>&nbsp;</TD>
            <TD class="qcell_text" width="70%"><B><%= DisplayBean.getAmsName() %></B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Current Software Version:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="amsVersion" size="8" maxlength="10" value="<%= DisplayBean.getAmsVersion() %>"></TD>
          </TR>
<%
if (DisplayBean.canAgentChangeFilename())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">Download Filename:</TD>
            <TD class="qcell_text" width="70%"><INPUT type="text" name="filename" size="12" maxlength="12" value="<%= DisplayBean.getAgentFilename() %>">&nbsp;&nbsp;&nbsp;<INPUT type="button" name="useDefaultBtn" value="Use Default" onclick="javascript:use_default_filename()"></TD>
          </TR>
<%
}
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%">&nbsp;</TD>
            <TD class="qcell_text" width="70%">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD colspan="2" align="center">
              <OBJECT id=ucAMSPath style="LEFT: 0px; TOP: 0px" codeBase="controls/<%= DisplayBean.getCodebase() %>" classid="<%= DisplayBean.getClassid() %>" VIEWASTEXT>
                <PARAM NAME="_ExtentX" VALUE="15452">
                <PARAM NAME="_ExtentY" VALUE="3281">
                <PARAM NAME="FILE_LOC" VALUE="<%= DisplayBean.getAmsDirectory() %>">
                <PARAM NAME="NEW_FILE_LOC" VALUE="<%= DisplayBean.getAgentDirectory() %>">
              	<PARAM NAME="VENDORID" VALUE="<%= DisplayBean.getAmsId() %>">
              </OBJECT>
            </TD>
          </TR>
<%
//if (!DisplayBean.getAmsDirectoryNote().equals(""))
if (false)
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" align="right" width="30%"><B><I>Note:</I></B></TD>
            <TD class="qcell_text" width="70%"><I><%= DisplayBean.getAmsDirectoryNote() %></I></TD>
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
<%
if (DisplayBean.isAgentRegistered())
{
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('menu_chg_vendor')">Back</A></TD>
<%
}
else
{
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('agt_ams_back')">Back</A></TD>
<%
}
%>          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Continue</A></TD>
          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
          <TD class="pagelogo"><jsp:include page="pagebar_logo.jsp" flush="true" /></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="copyright"><%= DisplayBean.getCarrierInfo().getCopyrightNotice() %></TD>
  </TR>
</TABLE>
</BODY>
</HTML>
