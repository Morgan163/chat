var chat = document.querySelector('#chat');
var rooms = document.querySelector('#rooms');
var chatTable = document.querySelector('#chat-table');
var messageField = document.querySelector('#message-field');
var messageForm = document.querySelector('#messageForm');
var stompClient = null;
var username = null;
var roomName = null;
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
    if (username == null || username.match(/^ *$/) !== null) {
        return;
    }
    if (username) {
        var socket = new SockJS('/chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
}

function onConnected() {
    stompClient.subscribe('/topic/room', onMessageReceived);
    stompClient.subscribe('/user/queue/reply/rooms', onRoomsReceived);
    var clientMessage = {
        content: "",
        login: username,
        room: ""
    }
    var user = {
        login: username
    };
    stompClient.send("/app/chat.connect",
        {},
        JSON.stringify(clientMessage)
    )
}

function onError(error) {

}

function onRoomsReceived(payload) {
    var roomsObject = JSON.parse(payload.body);
    if (Array.isArray((roomsObject))) {
        while (rooms.firstChild) {
            rooms.removeChild(rooms.firstChild);
        }
        roomsObject.forEach(function (item) {
            var li = document.createElement('li');
            li.classList.add('collection-item');
            li.classList.add('noborder');
            var span = document.createElement('span');
            span.classList.add('title');
            span.classList.add('small');
            span.appendChild(document.createTextNode(item.name));
            li.appendChild(span);
            rooms.appendChild(li);
        })
    }
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if (Array.isArray(message)) {
        message.forEach(function (item) {
            writeMessage(item);
        });
    } else {
        writeMessage(message);
    }
}

function writeMessage(message) {
    if (message.messageString == "") {
        return;
    }
    var date = new Date(message.messageDate);
    var li = document.createElement('li');
    li.classList.add('collection-item');
    li.classList.add('avatar');
    li.classList.add('noborder');
    var span = document.createElement('span');
    span.classList.add('title');
    span.classList.add('small');
    var p = document.createElement('h5');
    p.appendChild(document.createTextNode(message.messageString));
    if (message.user.name == username) {
        span.appendChild(document.createTextNode(date.toLocaleString("ru", options) + " " + message.user.name));
        li.classList.add('right-align');
    } else {
        span.appendChild(document.createTextNode(message.user.name + " " + date.toLocaleString("ru", options)));
    }
    li.appendChild(span);
    li.appendChild(p);
    chat.appendChild(li);
    chat.scrollTop = chat.scrollHeight;
}

function sendMessage(event) {
    var messageText = messageField.value;
    if (messageText != "") {
        if (roomName == null) {
            var clientMessage = {
                content: messageText,
                login: username,
                room: ""
            };
            stompClient.send("/app/chat.general",
                {},
                JSON.stringify(clientMessage)
            );
            event.preventDefault();
            return;
        }
        var currentUser = {
            name: username
        };
        var message = {
            owner: currentUser,
            text: messageText
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