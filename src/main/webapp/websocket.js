let socket = null;
var username = null;
var selectedContact = null

function connect() {
    username = document.getElementById("username").value;
    $("#login-component").hide();
    $("#chat-page").attr("hidden", false);
    socket = new WebSocket("ws://localhost:8080/javaee-websocket-xmpp-client/chat/" + username);
    socket.onmessage = onMessageReceive;
    socket.onopen = onConnected;
    socket.onerror = onError;
}

function onConnected() {
    $("#connectSpinner").hide();
}

function onError(error) {
    let connectingElement = $("#connectSpinner");
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
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
        message.content = message.to + ' joined!';
    } else {
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
