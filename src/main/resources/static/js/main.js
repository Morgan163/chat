
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
    var newRow = chatTable.insertRow(chatTable.rows.length);
    var cell0 = newRow.insertCell(0);
    var cell1 = newRow.insertCell(1);
    if(message.user.name == username){
        cell1.innerHTML = message.messageString;
    }else{
        cell0.innerHTML = message.messageString;
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