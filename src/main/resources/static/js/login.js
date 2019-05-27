var usernameForm = document.querySelector('#username-form');
var usernameButton = document.querySelector('#username-button');
var usernameField = document.querySelector('#username');
var passwordField = document.querySelector("#password");

function validateForm(event) {
    var username = document.querySelector('#username').value.trim();
    if(username == null || username.match(/^ *$/) !== null){
        usernameField.classList.add('invalid');
        event.preventDefault();
        return;
    }
    var password = document.querySelector('#password').value.trim();
    if(password == null || password.match(/^ *$/) !== null){
        passwordField.classList.add('invalid');
        event.preventDefault();
        return;
    }
    usernameForm.submit();
}

usernameButton.addEventListener('submit', validateForm, true);