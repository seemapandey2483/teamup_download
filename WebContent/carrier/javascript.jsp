///********************
//*  Hides the specified menu bar item
//********************/
function hidemenu(elmnt)
{
	document.all(elmnt).style.visibility = "hidden";
}

//********************
//*  Shows the specified menu bar item
//********************/
function showmenu(elmnt)
{
	document.all(elmnt).style.visibility = "visible";
}

//********************
//*  Submit the form using the specified action from the action bar
//********************/
function do_action(name)
{
	if (allow_action() == true)
	{
		document.tudlform.action.value = name;
		document.tudlform.submit();
	}
}

//********************
//*  Submit the form using the specified action from the menu bar
//********************/
function do_menu(name)
{
	if (allow_action() == true)
	{
		document.tudlform.action.value = name;
		document.tudlform.submit();
	}
}

//********************
//*  Submit the form using the specified action from the menu bar
//*******************/
function do_page_action(name)
{
	document.tudlform.action.value = name;
	document.tudlform.submit();
}

//********************
//* Verify that the entered string is a valid numeric (integer) string
//********************
function valid_integer(str)
{
	var nums = new String("0123456789");
	var valid = true;
	
	// check for any non-numeric characters
	for (i=0; i < str.length; i++)
	{
		if (nums.indexOf(str.substr(i, 1)) < 0)
			valid = false;
	}
	
	return valid;
}

//********************
//* Verify that the entered string is a valid numeric (decimal) string
//********************
function valid_numeric(str)
{
	var nums = new String("0123456789.");
	var valid = true;
	
	// check for any non-numeric characters
	for (i=0; i < str.length; i++)
	{
		if (nums.indexOf(str.substr(i, 1)) < 0)
			valid = false;
	}
	
	return valid;
}

//********************
//* Open a child window to the specified URL
//********************
function open_child(childUrl)
{
	theChildWindow = window.open(childUrl,'','toolbar=no,scrollbars=yes,resizable=yes');
	theChildWindow.focus();
}
