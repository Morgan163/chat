
var usernamePart = document.querySelector('#username-part');
var usernameForm = document.querySelector('#username-form');
var chat = document.querySelector('#chat');
var chatTable = document.querySelector('#chat-table');
var messageField = document.querySelector('#message-field');
var messageForm = document.querySelector('#messageForm');
var stompClient = null;
var username = null;
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
var options = {
  day: 'numeric',
  month: 'long',
  year: 'numeric',
  hour: 'numeric',
  minute: 'numeric',
  second: 'numeric'
};
function connect(event) {
    username = document.querySelector('#username').value.trim();
    if(username) {
        var socket = new SockJS('/chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}
function onConnected() {
    stompClient.subscribe('/topic/room', onMessageReceived);
    stompClient.subscribe('/user/queue/reply', onMessageReceived);

    var user = {
        name : username
    };
    stompClient.send("/app/chat.addUserToRoom",
        {},
        JSON.stringify(user)
    )
    stompClient.send("/app/chat.notifyOtherUsers",
            {},
            JSON.stringify(user)
        )
}
function onError(error) {

}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if(Array.isArray(message)){
        message.forEach(function(item){
            writeMessage(item);
        });
    }else{
        writeMessage(message);
    }
}

function writeMessage(message){
    var newFirstRow = chatTable.insertRow(chatTable.rows.length);
    var cell00 = newFirstRow.insertCell(0);
    var cell01 = newFirstRow.insertCell(1);
    var newRow = chatTable.insertRow(chatTable.rows.length);
    var cell10 = newRow.insertCell(0);
    var cell11 = newRow.insertCell(1);
    var date = new Date(message.messageDate);
    if(message.user.name == username){
        cell01.innerHTML = message.user.name + " " + date.toLocaleString("ru", options)
        cell11.innerHTML = message.messageString;
    }else{
        cell00.innerHTML = message.user.name + " " + date.toLocaleString("ru", options)
        cell10.innerHTML = message.messageString;
    }
}

function sendMessage(event){
    var messageText = messageField.value;
    var currentUser = {
            name : username
        };
    var message = {
        user : currentUser,
        messageString : messageText
    }
    stompClient.send("/app/chat.senMessage",
            {},
            JSON.stringify(message)
            )
    messageField.value = '';
    event.preventDefault();
}
usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);