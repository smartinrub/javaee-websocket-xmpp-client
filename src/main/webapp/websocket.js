let socket = null;
let username = null;

function connect() {
    var username = document.getElementById("name").value;
    $("#username-page").hide();
    $("#chat-page").attr("hidden", false);
    socket = new WebSocket("ws://localhost:8080/javaee-websocket-xmpp-client/chat/" + username);
    socket.onmessage = onMessageReceive;
    socket.onopen = onConnected;
    socket.onerror = onError;
}

function onConnected() {
    $(".connecting").hide();
}

function onError(error) {
    let connectingElement = $.find("connecting");
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    let messageInput = $("message");
    let messageContent = messageInput.value.trim();
    if (messageContent && socket) {
        let chatMessage = {
            from: username,
            to: "sergio@localhost",
            content: messageInput.value
        };
        socket.send(JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceive(event) {
    let message = JSON.parse(event.data);
    let messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
        message.content = message.sender + ' joined!';
    } else {
        let usernameElement = document.createElement('span');
        let usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    let textElement = document.createElement('p');
    let messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    let messageArea = $("messageArea");
    messageArea.append(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}
