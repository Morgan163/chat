
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
  month: '2-digit',
  year: 'numeric',
  hour: 'numeric',
  minute: 'numeric',
  second: 'numeric'
};
function connect() {
    username = document.querySelector('#username').value.trim();
    if(username == null || username.match(/^ *$/) !== null){
        return;
    }
    if(username) {
        var socket = new SockJS('/chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
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
    if(message.messageString==""){
        return;
    }
    var date = new Date(message.messageDate);
    var li = document.createElement('li');
    li.classList.add('collection-item');
    li.classList.add('avatar');
    li.classList.add('noborder');
   /* var i = document.createElement('i');
    i.classList.add('material-icons');
     i.classList.add('circle');
     i.classList.add('right-align');
    i.appendChild(document.createTextNode(message.user.name[0]+message.user.name[1]));*/
    var span = document.createElement('span');
    span.classList.add('title');
    span.classList.add('small');
    var p = document.createElement('h5');
    p.appendChild(document.createTextNode(message.messageString));
     if(message.user.name==username){
            span.appendChild(document.createTextNode(date.toLocaleString("ru", options)+" "+message.user.name));
            li.classList.add('right-align');
     }else{
            span.appendChild(document.createTextNode(message.user.name+" "+date.toLocaleString("ru", options)));
     }
    li.appendChild(span);
    li.appendChild(p);
    chat.appendChild(li);
    chat.scrollTop = chat.scrollHeight;
    /*var newFirstRow = chatTable.insertRow(chatTable.rows.length);
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
    }*/
}

function sendMessage(event){
    var messageText = messageField.value;
    if(messageText!=""){
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
    }
    event.preventDefault();
}
messageForm.addEventListener('submit', sendMessage, true);
connect();