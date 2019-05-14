var chat = document.querySelector('#chat');
var rooms = document.querySelector('#rooms');
var chatTable = document.querySelector('#chat-table');
var messageField = document.querySelector('#message-field');
var messageForm = document.querySelector('#messageForm');
var commandForm = document.querySelector('#commandForm');
var commandField = document.querySelector('#command-field');
var stompClient = null;
var username = null;
var roomName = null;
var subscribe = null;
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
    //stompClient.subscribe('/topic/room', onMessageReceived);
    stompClient.subscribe('/user/queue/reply', onCommandReceived);
    var clientMessage = {
        content: "",
        login: username,
        room: ""
    };
    stompClient.send("/app/chat.connect",
        {},
        JSON.stringify(clientMessage)
    )
}

function onError(error) {

}

function onCommandReceived(payload) {
    var message = JSON.parse(payload.body);
    if (Array.isArray((message))) {
        onMessageReceived(payload);
        return;
    }
    var rooms = message.messageAbout.rooms;
    onRoomsReceived(rooms);
}

function onRoomsReceived(roomsObject) {
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
            span.innerText = item.name;
            li.appendChild(span);
            rooms.appendChild(li);
        })
        rooms.update
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
    var date = new Date(message.date);
    var li = document.createElement('li');
    li.classList.add('collection-item');
    li.classList.add('avatar');
    li.classList.add('noborder');
    var span = document.createElement('span');
    span.classList.add('title');
    span.classList.add('small');
    var p = document.createElement('h5');
    p.appendChild(document.createTextNode(message.text));
    if (message.owner.login == username) {
        span.appendChild(document.createTextNode(date.toLocaleString("ru", options) + " "
            + message.owner.login));
        li.classList.add('right-align');
    } else {
        span.appendChild(document.createTextNode(message.owner.login + " "
            + date.toLocaleString("ru", options)));
    }
    li.appendChild(span);
    li.appendChild(p);
    chat.appendChild(li);
    chat.scrollTop = chat.scrollHeight;
}

function sendCommand(event) {
    var commandText = commandField.value;
    if (commandText != "") {
        var clientMessage = {
            content: commandText,
            login: username,
            room: roomName
        };
        stompClient.send("/app/chat.command",
            {},
            JSON.stringify(clientMessage)
        );
        commandField.value = '';
        event.preventDefault();
        return;
    }
    event.preventDefault();
}

function sendMessage(event) {
    var messageText = messageField.value;
    if ((messageText != "") && (roomName != null)) {
        //SonarLint warning not understandable
        var clientMessageToRoom = {
            content: messageText,
            login: username,
            room: roomName
        };
        stompClient.send('/app/chat/' + roomName + '/sendMessage',
            {},
            JSON.stringify(clientMessageToRoom)
        );
        messageField.value = '';
    }
    event.preventDefault();
}

rooms.onclick = function (event) {
    var target = event.target;
    console.log(target);
    while (target != rooms) {
        if (target.tagName == 'SPAN') {
            if (roomName !== target.innerText) {
                while (chat.firstChild) {
                    chat.removeChild(chat.firstChild);
                }
            }
            console.log(target);
            roomName = target.innerText;
            document.querySelector('#roomName').value = roomName;
            if (subscribe) {
                subscribe.unsubscribe();
            }
            subscribe = stompClient.subscribe('/topic/' + roomName, onMessageReceived);
            var clientMessageToRoom = {
                content: "",
                login: username,
                room: roomName
            };
            stompClient.send('/app/chat/' + roomName + '/addUser',
                {},
                JSON.stringify(clientMessageToRoom)
            );
            return;
        }
        target = target.parentNode;
    }
}
messageForm.addEventListener('submit', sendMessage, true);
commandForm.addEventListener('submit', sendCommand, true);
connect();