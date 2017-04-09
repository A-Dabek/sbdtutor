var isEmpty = function (arg_1) {
    "use strict";
    if (arg_1.length === 0) {
        return true;
    } else {
        return false;
    }
};

function isWhiteSpace(str) {
    var ws = "\t\n\r ";
    for (var i = 0; i < str.length; i++) {
        var c = str.charAt(i);
        if (ws.indexOf(c) == -1) {
            return false;
        }
    }
    return true;
}


var checkString = function (str, rep) {
    "use strict";
    if (!isEmpty(str) && !isWhiteSpace(str)) {
        return true;
    }
    alert(rep);
    return false;
};


var validate = function (arg_form) {
    var ret = true;
    "use strict";
    var elem = ['f_imie', 'f_nazwisko', 'f_miasto', 'f_ulica'];
    
    
    if(checkZIPCodeRegEx(arg_form.elements['f_kod'].value) == false) {
        ret = false;
    }
    
    if (checkEmailRegEx(arg_form.elements['f_email'].value) == false) {
        ret = false;
    }
    for (var i=0; i<elem.length; i++) {
        var str = arg_form.elements[elem[i]];
        //if (checkString(str, 'Podaj ' + elem[i]) == false) {
        //    return false;
        //}
        if (checkStringAndFocus(str, 'Podaj ' + elem[i]) == false) {
            ret = false;
            break;
        }
    }
    /*if (checkEmail(arg_form.elements['f_email'].value) == false) {
        return false;
    }*/
    var lista = document.getElementsByTagName('input');
    for (var el in lista) {
        if (ret == false)
            lista[el].className = 'wrong';
        else
            lista[el].className = '';
    }
    return ret;
};

function checkEmail(str) {
    if (isWhiteSpace(str)) {
        alert("whitespace problem");
        return false;
    }
    else {
        var at = str.indexOf("@");
        if (at < 1) {
            alert("at problem");
            return false;
        }
        else {
            var l = -1;
            for (var i = 0; i < str.length; i++) {
                var c = str.charAt(i);
                if (c == ".") {
                    l = i;
                }
            }
            if ((l < (at + 2)) || (l == str.length - 1)) {
                alert("dot problem");
                return false;
            }
        }
        return true;
    }
}

function checkStringAndFocus(obj, msg) {
    var str = obj.value;
    var errorFieldName = "e_" + obj.name.substr(2, obj.name.length);
    if (isWhiteSpace(str) || isEmpty(str)) {
        document.getElementById(errorFieldName).innerHTML = msg;
        obj.focus();
        startTimer(errorFieldName);
        return false;
    }
    else {
        return true;
    }
}

var errorField = "";
function startTimer(fName) {
    errorField = fName;
    window.setTimeout("clearError(errorField)", 5000);
}
function clearError(objName) {
    document.getElementById(objName).innerHTML = "";    
}

function showElement(e) {
    document.getElementById(e).style.visibility = 'visible';
}
function hideElement(e) {
    document.getElementById(e).style.visibility = 'hidden';
}

function checkEmailRegEx(str) {
    var email = /[a-zA-Z_0-9\.]+@[a-zA-Z_0-9\.]+\.[a-zA-Z][a-zA-Z]+/;
    if (email.test(str))
        return true;
    else {
        alert("Podaj właściwy e-mail");
        return false;
    }
}

function checkZIPCodeRegEx(elem) {
    var zipcode = /[0-9][0-9]-[0-9][0-9][0-9]/;
    if (zipcode.test(elem)) {
        document.getElementById('kod').innerHTML = 'OK';
        document.getElementById('kod').className = 'green';
        return true;
    } else {
        document.getElementById('kod').innerHTML = 'nie OK';
        document.getElementById('kod').className = 'red';
        return false;
    }
    
}

function alterRows(i, e) {
    if (e) {
        if (i % 2 == 1) {
                e.setAttribute("style", "background-color: Aqua;");
            }
        e = e.nextSibling;
        while (e && e.nodeType != 1) {
            e = e.nextSibling;
        }
        alterRows(++i, e);
    }
}

window.onload = tobeginwith;

function tobeginwith() {
    alterRows(1, document.getElementsByTagName('tbody')[0].firstElementChild);
}


function nextNode(e) {
    while (e && e.nodeType != 1) {
        e = e.nextSibling;
    }
    return e;
}

function prevNode(e) {
    while (e && e.nodeType != 1) {
        e = e.previousSibling;
    }
    return e;
}
function swapRows(b) {
    var tab = prevNode(b.previousSibling);
    var tBody = nextNode(tab.firstChild);
    var lastNode = prevNode(tBody.lastChild);
    tBody.removeChild(lastNode);
    var firstNode = nextNode(tBody.firstChild);
    tBody.insertBefore(lastNode, firstNode);
}

function cnt(form, msg, maxSize) {
    if (form.value.length > maxSize)
        form.value = form.value.substring(0, maxSize);
    else
        msg.innerHTML = maxSize - form.value.length;
}




