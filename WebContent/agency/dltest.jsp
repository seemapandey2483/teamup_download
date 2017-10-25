<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.DownloadDisplayBean" />
<TITLE><%= DisplayBean.getCarrierName() %> - TEAM-UP Download Agency Administration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--
<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	return true;
}

function allow_action()
{
	return true;
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
    <TD class="menubar" nowrap="nowrap">&nbsp;</TD>
  </TR>
  <TR>
    <TD class="blankbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
    <INPUT type="hidden" name="action" value="">
    <INPUT type="hidden" name="agentID" value="<%= DisplayBean.getAgentId() %>">

<!-- BEGIN BODY -->
<%
if (DisplayBean.isAgentRegistered())
{
%>
    <H1>Change Agency System Settings</H1>
<%
}
else
{
%>
    <H1>TEAM-UP Download Registration</H1>
<%
}
%>
    <CENTER><H2>Download Test File Instructions</H2></CENTER>
    <TABLE>
    <TR>
      <TD>This test will download a file to your <%= DisplayBean.getDownloadDir() %> directory,
        to verify the download configuration for your environment.
        <UL>
          <LI>Wait for the download box to appear below.  (This could take up to a minute with a broadband connection or up to 5 minutes over a dial-up connection.)<BR>&nbsp;</LI>
          <LI>If prompted with a dialog box asking if you would like to download and install
            a control, <FONT color="red">please click "<B>always trust controls from Ebix,Inc</B></FONT>."  This
            will not change the security settings of your machine, but it will allow the download
            to run properly.
          </LI>
        </UL>
        <UL>
          <LI><FONT color="red"><B>If you receive an error during this process, please <A href="mailto:<%= DisplayBean.getCarrierEmail() %>">contact technical support</A>.</B></FONT></LI>
        </UL>
      </TD>
    </TR>
    <TR>
      <TD>&nbsp;</TD>
    </TR>
    <TR>
      <TD align="center">
			<OBJECT id="ctlDownload" codeBase="<%= DisplayBean.getCodebase() %>"
				classid="<%= DisplayBean.getClassid() %>">
				<PARAM NAME="_ExtentX" VALUE="14843">
				<PARAM NAME="_ExtentY" VALUE="4921">
				<PARAM NAME="key" VALUE="<%= DisplayBean.getKey() %>">
				<PARAM NAME="getFileFrom" VALUE="<%= DisplayBean.getFileSource() %>">
				<PARAM NAME="putFileTo" VALUE="<%= DisplayBean.getDownloadDir() %>">
				<PARAM NAME="interactive" VALUE="N">
				<PARAM NAME="redirect" VALUE="<%= DisplayBean.getTestForwardUrl() %>">
				<PARAM NAME="file_status" VALUE="<%= DisplayBean.getStatusTest() %>"><%
if (!DisplayBean.isOldDownloadControl())
{
%>
				<PARAM NAME="append" VALUE="Y"><%
}
%>
			</OBJECT>
      </TD>
    </TR>
<!--  
    <TR>
      <TD valign="bottom" align="center">
        <P><a href="<c:url value="/dlsetup.jsp"/>" target="_blank">Click here</a> if you do not see the control</P>
      </TD>
    </TR>  -->
    
<!-- END BODY -->

    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('dltest_back')">Back</A></TD>
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
