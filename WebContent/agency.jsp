<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<c:url value="/theme/tudl_ss.css"/>" type="text/css">
<TITLE>TEAM-UP Download Agency Administration</TITLE>
<STYLE type="text/css">
<!--
H1{
  text-align : left;
}
P{
  text-align : center;
  font-size : 12pt;
}
H2{
  color : #f77c1a;
  position : absolute;
  top : 480px;
  left : 360px;
}
-->
</STYLE>
<SCRIPT language="JavaScript">
<!--
// keep track of image to load (for animated wait bar)
var next_image = 2;
var Wait1 = new Image(82,10);
var Wait2 = new Image(82,10);
var Wait3 = new Image(82,10);
var Wait4 = new Image(82,10);
var Wait5 = new Image(82,10);
var Wait6 = new Image(82,10);
var increment = 1;

function do_load()
{
// load the image objects
	Wait1.src = "images/wait1.gif";
	Wait2.src = "images/wait2.gif";
	Wait3.src = "images/wait3.gif";
	Wait4.src = "images/wait4.gif";
	Wait5.src = "images/wait5.gif";
	Wait6.src = "images/wait6.gif";

// set the timer
	setTimeout("change_image('wait_image')", 750);
	
// submit the form to start the quote
	document.waitform.submit();
	
	return true;
}

function change_image(pic)
{
	document.images[pic].src = eval("Wait" + next_image + ".src");
	next_image = next_image + increment;
	if (next_image > 6 || next_image < 2)
	{
		increment = 0 - increment;
		next_image = next_image + (increment * 2);
	}

	setTimeout("change_image('wait_image')", 750);

	return true;			
}
// -->
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" onload="return do_load();">
<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" mm:layoutgroup="true" bgcolor="#FFFFFF">
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="actionbar" nowrap="nowrap" height="77">&nbsp;</TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
  <TR>
    <TD class="menubar" nowrap="nowrap">&nbsp;</TD>
  </TR>
  <TR>
    <TD class="docbody">
    <FORM name="waitform" method="POST" action="<c:url value="/agency"/>">
    <INPUT type="hidden" name="action" value="">
      <TABLE class="qcell_table" width="100%">
        <TBODY>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_number">&nbsp;</TD>
            <TD class="qcell_text"><FONT face="Arial" size="2"><B>Connecting to the download system...</B></FONT></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_number">&nbsp;</TD>
            <TD class="qcell_text">&nbsp;</TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2" align="center"><IMG name="wait_image" src="images/wait1.gif"></TD>
          </TR>
          <TR class="qcell_row" valign="top">
            <TD class="qcell_text" colspan="2" align="center">&nbsp;<BR>
            <FONT face="Arial" size="2"><B>PLEASE WAIT</B></FONT></TD>
          </TR>
        </TBODY>
      </TABLE>
    </FORM>
    </TD>
  </TR>
  <TR>
    <TD>
      <TABLE width="100%" border="0" cellpadding="0" cellspacing="0"  bgcolor="black">
        <TR>
          <TD class="pagebar" nowrap="nowrap"></TD>
          <TD class="pagelogo"><img src="images/small_grad_r.jpg" width="35" height="77"><img src="images/TUDL_by_Connective.jpg"></TD>
        </TR>
      </TABLE>
    </TD>
  </TR>
</TABLE>
</BODY>
</HTML>
