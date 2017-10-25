<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.DatabaseConfigDisplayBean" />
<TITLE>TEAM-UP Download Configuration</TITLE>
<SCRIPT language="JavaScript" type="text/javascript">
<!--

<jsp:include page="javascript.jsp" flush="true" />

function do_load()
{
	document.tudlform.dbtype.focus();
	return true;
}

function allow_action()
{
	return true;
}

function do_save()
{
	if (!document.tudlform.dbtype.value)
	{
		alert("Please select the database type.");
		document.tudlform.dbtype.focus();
	}
	else
	{
		document.tudlform.submit();
	}
}

function throw_change_flag()
{
	document.tudlform.data_changed.value = "Y";
}

function change_type()
{
	throw_change_flag();
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
          <TD class="actionbar" nowrap="nowrap">&nbsp;</TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap">&nbsp;</TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="${requestScope.DisplayBean.servletPath}" />" onsubmit="return false">
    <INPUT type="hidden" name="action" value="save">
    <INPUT type="hidden" name="data_changed" value="N">

<!-- BEGIN BODY -->

      <H1>TEAM-UP Download Configuration Wizard &nbsp;(Page 1 of 4)<BR>&nbsp;</H1>
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question" colspan="3"><B>Database Configuration</B></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question"><A title="The database system to be used">Database Type:</A></TD>
            <TD class="qcell_question">
              <SELECT name="dbtype" onchange="change_type()">
              	<OPTION value=""></OPTION>
<% 
for (int i=0; i < DisplayBean.getTypeCount(); i++)
{
%>              
                <OPTION value="<%= DisplayBean.getTypeId(i) %>"><%= DisplayBean.getTypeDescription(i) %></OPTION>
<%
}
%>              
              </SELECT>
            </TD>
          </TR>
<%
if (DisplayBean.isInvalid())
{
%>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
            <TD class="qcell_question">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD colspan="3" class="qcell_question"><font color="red">Database parameters are invalid.  Please check the parameters carefully.  Also, please ensure that the JDBC driver for your database is on the application server classpath.</font></TD>
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_save()">Continue</A></TD>
          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
          <TD class="pagebar" nowrap="nowrap" width="100%">&nbsp;</TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
</TABLE>
</BODY>
</HTML>
