<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.TransLogDisplayBean" />
<TITLE><%= DisplayBean.getCarrierName() %> - TEAM-UP Download Agency Report</TITLE>
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

function do_resort()
{
	if (document.tudlform.sortOrder.value && document.tudlform.sortOrder.value == "DESC")
		document.tudlform.sortOrder.value = "ASC";
	else
		document.tudlform.sortOrder.value = "DESC";
	do_page_action("<%= DisplayBean.getReportAction() %>");
}
// -->
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" text="#000000" link="#FF0000" vlink="#FF0000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
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
    <FORM name="tudlform" method="POST" action="<c:url value="/agency"/>" onsubmit="return false">
      <INPUT type="hidden" name="action" value="">
      <INPUT type="hidden" name="sortOrder" value="<%= DisplayBean.getSortOrder() %>">
      <INPUT type="hidden" name="agentId" value="<%= DisplayBean.getAgentId() %>">
      <INPUT type="hidden" name="startdt" value="<%= DisplayBean.getStartDate() %>">
      <INPUT type="hidden" name="enddt" value="<%= DisplayBean.getEndDate() %>">
    </FORM>

<!-- BEGIN REPORT BODY -->

      <H1>Download Details</H1>

      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD><b>Carrier:</b>&nbsp;</TD>
            <TD><%= DisplayBean.getCarrierName() %></TD>
          </TR>
          <TR class="qcell_row" valign="bottom">
            <TD><b>Agent ID:</b>&nbsp;</TD>
            <TD><%= DisplayBean.getAgentId() %></TD>
          </TR>
          <TR class="qcell_row" valign="bottom">
            <TD><B>Download Period:</B>&nbsp;</TD>
            <TD><%= DisplayBean.getDateRange() %></TD>
          </TR>
<%
if (DisplayBean.getStartDate() != null && !DisplayBean.getStartDate().equals(""))
{
%>
          <TR class="qcell_row" valign="top">
            <TD><B>Sorted by:</B>&nbsp;</TD>
<%
  if (DisplayBean.getSortOrder() != null && DisplayBean.getSortOrder().equals("DESC"))
  {
%>            <TD>Most recent download first &nbsp;<BR>
                 <INPUT type="button" value="Sort by Download Date" onclick="do_resort()">
            </TD>
<%
  }
  else
  {
%>            <TD>Download date &nbsp;<BR>
                 <INPUT type="button" value="Sort Newest to Oldest" onclick="do_resort()">
            </TD>
<%
  }
%>
          </TR>
<%
}
%>
        </TBODY>
      </TABLE>
      <p>&nbsp;</p>
<%
int NUM_COLUMNS = 5;
if (DisplayBean.getFileCount() == 0)
{
%>
      <I>No records found for the specified parameters</I>
<%
}
else
{
%>
      <TABLE class="qcell_table">
        <TBODY>
          <TR class="qcell_row" valign="bottom">
            <TD class="qcell_question" align="center"><B>File</B></TD>
            <TD class="qcell_question" align="center">&nbsp;&nbsp;&nbsp;<B>Created</B>&nbsp;&nbsp;&nbsp;</TD>
<%
  if (DisplayBean.isParticipantUsed())
  {
    NUM_COLUMNS++;
%>            <TD class="qcell_question" align="center"><B>Participant<BR>Code</B></TD>
<%
  }
%>
            <TD class="qcell_question" align="center"><B>Message<BR>&nbsp;&nbsp;&nbsp;Sequence&nbsp;&nbsp;&nbsp;</B></TD>
            <TD class="qcell_question" align="center"><B>No.&nbsp;of<BR>Trans</B></TD>
            <TD class="qcell_question" align="center"><B>File<BR>&nbsp;&nbsp;&nbsp;Type&nbsp;&nbsp;&nbsp;</B></TD>
            <TD class="qcell_question" width="40%">&nbsp; <B>Download Date</B></TD>
          </TR>
          <TR>
            <TD colspan="<%= NUM_COLUMNS %>"><hr></TD>
          </TR>
<%
  for (int i=0; i < DisplayBean.getFileCount(); i++)
  {
    connective.teamup.download.ws.objects.DownloadFileInfo file = DisplayBean.getFile(i);
    String fileType = "";
    if("E".equals(file.getStatus()) || "P".equals(file.getStatus()) || "Q".equals(file.getStatus())){
    	fileType ="CLAIM";
    }else {
    	fileType ="POLICY";
    }
%>          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" align="center" nowrap="nowrap"><%= file.getFilename() %></TD>
            <TD class="qcell_text" align="center"><%= DisplayBean.formatDate(file.getCreatedDate()) %></TD>
<%
    if (DisplayBean.isParticipantUsed())
    {
%>            <TD class="qcell_question" align="center"><%= file.getParticipantCode() %> <% if (file.getParticipantCode().equals("")) { %> <%= DisplayBean.getAgentId() %> <% } %></TD>
<%
    }
%>
            <TD class="qcell_text" align="center"><%= file.getMsgSeq() %>&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_text" align="center"><%= file.getTransactionCount() %></TD>
             <TD class="qcell_text" align="center"><%= fileType %>&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            <TD class="qcell_text" width="40%"><a title="<%= DisplayBean.formatDateStrLong(file.getLastDownloadedDate()) %>"><%= DisplayBean.formatDateStrShort(file.getLastDownloadedDate()) %></a></TD>
            
          </TR>
<%
    if (file.getTransactionCount() == 0)
    {
%>          <TR class="qcell_row" valign="top">
              <TD>&nbsp;</TD>
              <TD colspan="<%= NUM_COLUMNS - 1 %>"><I>No transaction level info is available</I></TD>
            </TR>
<%
    }
    else
    {
%>          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="<%= NUM_COLUMNS %>">
              <TABLE class="qcell_table" cellpadding="0" cellspacing="0" border="1" frame="hsides" rules="rows" width="100%">
                <TBODY>
                  <TR class="qcell_row" valign="bottom" bgcolor="#c0c0c0">
                    <TD align="center"><FONT size="-2"><B>Cust. ID</B></FONT>&nbsp;</TD>
                    <TD align="center"><FONT size="-2"><B>Policy No/Claim Number</B></FONT></TD>
                    <TD align="center"><FONT size="-2"><B>Eff. Dt.</B></FONT></TD>
                    <TD align="center"><FONT size="-2"><B>Trans.</B></FONT></TD>
                    <TD align="center"><FONT size="-2"><B>Seq.</B></FONT></TD>
                    <TD align="center"><FONT size="-2"><B>Named Insured</B></FONT></TD>
                    <TD align="center"><FONT size="-2"><B>LOB</B></FONT></TD>
                  </TR>
<%
      for (int t=0; t < file.getTransactionCount(); t++)
      {
        connective.teamup.download.ws.objects.DownloadTransactionInfo trans = file.getTransactions()[t];
%>                  <TR class="qcell_row" valign="top" bgcolor="#c0c0c0">
                    <TD class="qcell_text" align="center"><%= trans.getCustomerId() %>&nbsp;</TD>
                    <TD class="qcell_text"><%= trans.getPolicyNumber() %>&nbsp;</TD>
                    <TD class="qcell_text" align="center">&nbsp;<%= trans.getPolicyEffDate() %>&nbsp;</TD>
                    <TD class="qcell_text" align="center"><%= trans.getTransactionType() %></TD>
                    <TD class="qcell_text" align="center">&nbsp;<%= trans.getTransactionSeq() %>&nbsp;</TD>
                    <TD class="qcell_text"><%= trans.getInsuredName() %> </TD>
                    <TD class="qcell_text"><%= trans.getLob() %> </TD>
                  </TR>
<%
      }
%>                </TBODY>
              </TABLE>
            </TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="<%= NUM_COLUMNS %>" align="center">&nbsp;</TD>
          </TR>
<%
    }
  }
%>
        </TBODY>
      </TABLE>
<%
}
%>

<!-- END REPORT BODY -->

    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap" width="100%"></TD>
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
