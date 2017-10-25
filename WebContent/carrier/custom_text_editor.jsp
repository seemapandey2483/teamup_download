<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<jsp:useBean id="DisplayBean" scope="request" type="connective.teamup.download.beans.CustomTextEditorDisplayBean" />
<TITLE>TEAM-UP Download Carrier Administration</TITLE>
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

<jsp:include page="javascript.jsp" flush="true" />

function throw_change_flag()
{
	document.tudlform.data_changed.value = "Y";
}

function do_cancel()
{
	if (document.tudlform.data_changed.value == "Y")
	{
		if (confirm("This action will cause you to lose any changes you have made.\n\nDo you wish to continue?"))
			do_page_action("custom_list");
	}
	else
	{
		do_page_action("custom_list");
	}
}

function use_default()
{
	if (confirm("Are you sure you want to replace all custom text with the default for this email?"))
	{
		do_page_action("custom_set_default");
	}
}

function default_text()
{
	document.tudlform.text_editor.disabled = document.tudlform.text_default_check.checked;
}

function default_html()
{
	document.tudlform.html_editor.disabled = document.tudlform.html_default_check.checked;
}

function sel_company_tag()
{
<%
for (int i=0; i < DisplayBean.getCompanyTagCount(); i++)
{
%>	<% if (i > 0) { %>else <% } %>if (document.tudlform.company_select.value == "<%= DisplayBean.getCompanyTagId(i) %>")
	{
		document.tudlform.company_view.value = "<%= DisplayBean.getCompanyTagSample(i) %>";
		document.tudlform.company_code.value = "<%= DisplayBean.getCompanyTagCode(i) %>";
	}
<%
}
%>
	document.tudlform.company_code.select();
	document.tudlform.company_select.focus();
}

function sel_agent_tag()
{
<%
for (int i=0; i < DisplayBean.getAgentTagCount(); i++)
{
%>	<% if (i > 0) { %>else <% } %>if (document.tudlform.agent_select.value == "<%= DisplayBean.getAgentTagId(i) %>")
	{
		document.tudlform.agent_view.value = "<%= DisplayBean.getAgentTagSample(i) %>";
		document.tudlform.agent_code.value = "<%= DisplayBean.getAgentTagCode(i) %>";
	}
<%
}
%>
	document.tudlform.agent_code.select();
	document.tudlform.agent_select.focus();
}

function clear_text()
{
	document.tudlform.text_default_check.checked = false;
	document.tudlform.text_editor.disabled = false;
	document.tudlform.text_editor.value = "";
	document.tudlform.text_editor.focus();
}

function clear_html()
{
	document.tudlform.html_default_check.checked = false;
	document.tudlform.html_editor.disabled = false;
	document.tudlform.html_editor.value = "";
	document.tudlform.html_editor.focus();
}

function copy_to_html()
{
	var msg = "This command will copy the TEXT from above to the custom HTML window.\n" +
			  "Any HTML tags that have previously been saved will be lost.\n\n" +
			  "Do you want to continue?";
	if (confirm(msg) == true)
	{
		document.tudlform.html_default_check.checked = false;
		document.tudlform.html_editor.disabled = false;
		document.tudlform.html_editor.value = document.tudlform.text_editor.value;
		document.tudlform.html_editor.focus();
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
    <TD class="menubar" nowrap="nowrap"><jsp:include page="menu.jsp" flush="true" /></TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="tudlform" method="POST" action="<c:url value="/company"/>" onsubmit="return false">
      <INPUT type="hidden" name="action" value="">
      <INPUT type="hidden" name="data_changed" value="">
      <INPUT type="hidden" name="text_file" value="<%= DisplayBean.getTextId() %>">

<!-- BEGIN BODY -->

      <H1>Customizable Email: <U><%= DisplayBean.getFileDescription() %></U></H1>
      <H2>Edit Email Text</H2>
      <TABLE cellpadding="5" cellspacing="0" border="2" frame="void" rules="cols">
        <TBODY>
          <TR>
            <TD>&nbsp;</TD>
            <TD nowrap="nowrap">&nbsp;<BR>
                <B>Default Subject:&nbsp;&nbsp;</B><%= DisplayBean.getEditableSubjectDefault() %>
            </TD>
          </TR>
          <TR>
            <TD>&nbsp;</TD>
            <TD nowrap="nowrap"><B>Custom Subject:&nbsp;</B>
                <INPUT type="text" name="subject" size="50" value="<%= DisplayBean.getEditableSubject() %>" onchange="throw_change_flag()">
            </TD>
          </TR>
          <TR>
            <TD rowspan="4" valign="top">
              <TABLE>
                <TBODY>
                  <TR>
                    <TD class="small_text" nowrap="nowrap">
                        To insert dynamic text into your file:<br>
                        (1) select the data to insert (below),<br>
                        (2) view the sample value, and<br>
                        (3) either <u>cut &amp; paste</u> or<br>
                        &nbsp; &nbsp; &nbsp; <u>drag &amp; drop</u> the special<br>
                        &nbsp; &nbsp; &nbsp; code into your customized text<br>
                        &nbsp; &nbsp; &nbsp; to the right.
                    </TD>
                  </TR>
                  <TR>
                    <TD>&nbsp;</TD>
                  </TR>
                  <TR bordercolor="black" bgcolor="ff9999">
                    <TD nowrap="nowrap">
                        <b>Company data:</b><br>
                        select: <SELECT name="company_select" onchange="sel_company_tag()">
                          <OPTION></OPTION>
<%
for (int i=0; i < DisplayBean.getCompanyTagCount(); i++)
{
%>                          <OPTION value="<%= DisplayBean.getCompanyTagId(i) %>"><%= DisplayBean.getCompanyTagDesc(i) %></OPTION>
<%
}
%>                        </SELECT><br>
                        view: &nbsp;&nbsp;<INPUT type="text" name="company_view" size="25" readonly="readonly"><br>
                        code: &nbsp; <INPUT type="text" name="company_code" size="25">
                    </TD>
                  </TR>
                  <TR>
                    <TD>&nbsp;</TD>
                  </TR>
                  <TR bordercolor="black" bgcolor="aaaaff">
                    <TD nowrap="nowrap">
                        <b>Agency data:</b><br>
                        select: <SELECT name="agent_select" onchange="sel_agent_tag()">
                          <OPTION></OPTION>
<%
for (int i=0; i < DisplayBean.getAgentTagCount(); i++)
{
%>                          <OPTION value="<%= DisplayBean.getAgentTagId(i) %>"><%= DisplayBean.getAgentTagDesc(i) %></OPTION>
<%
}
%>                        </SELECT><br>
                        view: &nbsp;&nbsp;<INPUT type="text" name="agent_view" size="25" readonly="readonly"><br>
                        code: &nbsp; <INPUT type="text" name="agent_code" size="25">
                    </TD>
                  </TR>
                </TBODY>
              </TABLE>
            </TD>
            <TD>&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap="nowrap">
                <b>Custom Text:</b> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
                <INPUT type="checkbox" name="text_default_check" value="default" <% if (!DisplayBean.isTextCustom()) { %>checked<% } %> onclick="default_text()"> Use default<br>
                &nbsp; &nbsp; <TEXTAREA name="text_editor" cols="80" rows="15" onchange="throw_change_flag()" <% if (!DisplayBean.isTextCustom()) { %>disabled="disabled"<% } %>><%= DisplayBean.getEditableText() %></TEXTAREA>
            </TD>
          </TR>
          <TR>
            <TD align="center" nowrap="nowrap">
                <INPUT type="button" value="&uarr;  Clear text" onclick="clear_text()">&nbsp; &nbsp;
                <INPUT type="button" value="Copy text to HTML  &darr;" onclick="copy_to_html()">&nbsp; &nbsp;
                <INPUT type="button" value="Clear HTML  &darr;" onclick="clear_html()">
            </TD>
          </TR>
          <TR>
            <TD nowrap="nowrap">
                <b>Custom HTML:</b> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
                <INPUT type="checkbox" name="html_default_check" value="default" <% if (!DisplayBean.isHtmlCustom()) { %>checked<% } %> onclick="default_html()"> Use default<br>
                &nbsp; &nbsp; <TEXTAREA name="html_editor" cols="80" rows="15" onchange="throw_change_flag()" <% if (!DisplayBean.isHtmlCustom()) { %>disabled="disabled"<% } %>><%= DisplayBean.getEditableHtml() %></TEXTAREA>
            </TD>
          </TR>
          <TR>
            <TD>&nbsp;</TD>
            <TD>&nbsp;</TD>
          </TR>
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
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_cancel()">Cancel</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:do_page_action('custom_save')">Save &amp; Preview</A></TD>
          <TD class="pagebar" nowrap="nowrap"><A class="nav" href="javascript:use_default()">Replace with Default</A></TD>
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
