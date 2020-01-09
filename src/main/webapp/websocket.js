let socket = null;
var username = null;
var selectedContact = null;

function connect() {
    username = document.getElementById("username").value;
    $("#login-component").attr("hidden", true);
    if (socket === null || socket.readyState === WebSocket.CLOSED) {
        socket = new WebSocket("ws://localhost:8080/javaee-websocket-xmpp-client/chat/" + username);
        socket.onmessage = onMessageReceive;
        socket.onopen = onConnected;
        socket.onerror = onError;
    }
}

function onConnected(event) {
    console.log(event);
    console.log("Connected!");
    $("#connectSpinner").hide();
    $("#messageArea").attr("hidden", false);
}

function onError(error) {
    console.log(error);
    $("#chatContainer").hide();
    $("#login-component").attr("hidden", false);
}

function selectContact(contact) {
    selectedContact = contact;
}

function sendMessage() {
    var messageInput = document.getElementById("message");
    var messageContent = messageInput.value.trim();
    if (messageContent && socket) {
        let chatMessage = {
            from: username,
            to: selectedContact + "@localhost",
            content: messageInput.value
        };
        console.log(chatMessage);
        socket.send(JSON.stringify(chatMessage));
        messageInput.value = "";
    }
}

function onMessageReceive(event) {
    let message = JSON.parse(event.data);
    console.log(message);
    let messageElement = document.createElement('li');

    if (message.messageType === 'JOIN') {
        $("#chatContainer").attr("hidden", false);
        message.content = message.to + ' joined!';
    } else if (message.messageType === 'ERROR') {
        $("#chatContainer").hide();
        $("#login-component").attr("hidden", false);
    }
    else {
        let usernameElement = document.createElement('span');
        let usernameText = document.createTextNode(message.from);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);
    let messageArea = document.getElementById("messageArea");
    messageArea.appendChild(messageElement);
}
