var dateObject = new Date();
var month = dateObject.getMonth();
var monthArray = new Array("January" , "February" , "March"     ,
        "April"   , "May"      , "June"      ,
        "July"    , "August"   , "September" ,
        "October" , "November" , "December"  );
var dateToday = monthArray[month] + " " + dateObject.getDate() + ", " + dateObject.getFullYear();

var contactList = new Object();

function newContact() {
 // call "parent constructor"
    var obj = new Option();
    obj.last_name = "";
    obj.first_name = "";
    obj.telephone = "";
    obj.address = "";
    obj.city = "";
    obj.state = "";
    obj.zip = "";

 // method closures
    obj.setValues = function(form) {return setContactValues(this, form);};
    obj.getValues = function(form) {return getContactValues(this, form);};
    return obj;
}

function getForm()
{
    return document.forms[0];
}

function setContactValues(contact, form)
{
    if (contact === undefined) {
        ;
    } else {
        contact.last_name = form.last_name.value;
        contact.first_name = form.first_name.value;
        contact.telephone = form.telephone.value;
        contact.address = form.address.value;
        contact.city = form.city.value;
        contact.state = form.state.value;
        contact.zip = form.zip.value;
        contact.value = contact.last_name+ ", " +contact.first_name;
        contact.text = contact.last_name+ ", " +contact.first_name;
    }
}

function getContactValues(contact, form)
{
    if (contact === undefined) {
        ;
    } else {
        form.last_name.value = contact.last_name;
        form.first_name.value = contact.first_name;
        form.telephone.value = contact.telephone;
        form.address.value = contact.address;
        form.city.value = contact.city;
        form.state.value = contact.state;
        form.zip.value = contact.zip;
    }
}

function addContact() {
    var form = getForm();
    var length = form.contacts.options.length;
    var contactIndex = length;

    if ((length == 1) && (form.contacts.options[0].value == 'INITIAL')) {
        removeAllContacts();
        contactIndex = 0;
    }

    if (form.last_name.value == "" || form.last_name.value == "") {
        window.alert("You must enter the contact's first and last names.");
    } else {
        var contact = newContact();
        contact.setValues(form);
        form.contacts.options[contactIndex] = contact;
    }
}

function getSelectedContact() {
    var form = getForm();
    var index = form.contacts.selectedIndex;
    var contact = form.contacts.options[index];
    return contact;
}

function deleteContact() {
    var form = getForm();
    var index = form.contacts.selectedIndex;

    if (form.contacts.options[index] === undefined) {
        window.alert("You must select a contact in the list.");
    }
    else {
        form.contacts.options[index] = null;
    }
}

function resetContactEntry() {
    var form = getForm();
    form.last_name.value = "";
    form.first_name.value = "";
    form.telephone.value = "";
    form.address.value = "";
    form.city.value = "";
    form.state.value = "";
    form.zip.value = "";
}

function removeAllContacts() {
    var form = getForm();
    var count = form.contacts.options.length;
    while (count > 0) {
        count--;
        form.contacts.options[count] = null;
    }
}

function preSubmit() {
    resetContactEntry();
    removeAllContacts();
    return true;
}

