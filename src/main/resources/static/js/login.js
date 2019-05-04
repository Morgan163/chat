var usernameForm = document.querySelector('#username-form');
var usernameButton = document.querySelector('#username-button');
var usernameField = document.querySelector('#username');

function validateForm(event) {
    var username = document.querySelector('#username').value.trim();
    if(username == null || username.match(/^ *$/) !== null){
        usernameField.classList.add('invalid');
        event.preventDefault();
        return;
    }
    usernameForm.submit();
}

usernameForm.addEventListener('submit', validateForm, true);