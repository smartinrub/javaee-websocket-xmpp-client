let socket = null;
var username = null;
var selectedContact = null;

function connect() {
    username = document.getElementById("username").value;
    $("#login-component").attr("hidden", true);
    if (socket === null) {
        console.log("Logging in...");
        $("#chat-container").attr("hidden", false);
        socket = new WebSocket("ws://localhost:8080/javaee-websocket-xmpp-client/chat/" + username);
        socket.onmessage = onMessageReceive;
        socket.onopen = onConnected;
        socket.onerror = onError;
    }
}

function onConnected(event) {
    console.log(event);
    $("#connectSpinner").hide();
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
        $("#chat-container").attr("hidden", false);
        $("#messageArea").attr("hidden", false);
        console.log(message.to + ' joined!');
    } else if (message.messageType === 'ERROR') {
        $("#chat-container").attr("hidden", true);
        $("#login-component").attr("hidden", false);
        username = null;
        socket = null;
    } else {
        let usernameElement = document.createElement('span');
        let usernameText = document.createTextNode(message.from);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    // var textElement = document.createElement('p');
    // var messageText = document.createTextNode(message.content);
    // textElement.appendChild(messageText);
    // messageElement.appendChild(textElement);
    // let messageArea = document.getElementById("messageArea");
    // messageArea.appendChild(messageElement);
}
