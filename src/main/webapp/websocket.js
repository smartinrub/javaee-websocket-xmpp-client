let socket = null;
var username = null;
var selectedContact = null;

function connect() {
    username = document.getElementById("username").value;
    $("#loginComponent").attr("hidden", true);
    if (socket === null) {
        console.log("Logging in...");
        $("#chatContainer").attr("hidden", false);
        $("#mesgs").attr("hidden", true);
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
    $("#loginComponent").attr("hidden", false);
}

function selectContact(contact) {
    selectedContact = contact;

    let contacts = document.getElementsByClassName('chat-list');

    for (var i = 0; i < contacts.length; i++) {
        contacts[i].classList.remove('active-chat');
    }

    let contactComponent = document.getElementById(contact);
    contactComponent.classList.add("active-chat");
    $("#mesgs").attr("hidden", false);
}

function sendMessage() {
    var messageInput = document.getElementById("outgoingMessage");
    var messageContent = messageInput.value.trim();
    if (messageContent) {
        let chatMessage = {
            from: username,
            to: selectedContact + "@localhost",
            content: messageInput.value
        };
        console.log(chatMessage);
        socket.send(JSON.stringify(chatMessage));


        let outgoingMsg = document.createElement('div');
        outgoingMsg.classList.add("outgoing-msg");

        let sentMsg = document.createElement('div');
        sentMsg.classList.add("sent-msg");

        let msg = document.createElement('p');

        let text = document.createTextNode(messageInput.value);

        let timeDate = document.createElement('span');
        timeDate.classList.add("time-date");

        msg.appendChild(text);

        sentMsg.appendChild(msg);
        sentMsg.appendChild(timeDate);

        outgoingMsg.appendChild(sentMsg);

        let msgHistory = document.getElementById("msgHistory");
        msgHistory.appendChild(outgoingMsg);

        messageInput.value = "";
    }
}

function onMessageReceive(event) {
    let message = JSON.parse(event.data);
    console.log(message);

    if (message.messageType === 'JOIN') {
        $("#chatContainer").attr("hidden", false);
        $("#messageArea").attr("hidden", false);
        $(username).attr("hidden", true);
    } else if (message.messageType === 'ERROR') {
        $("#chatContainer").attr("hidden", true);
        $("#loginComponent").attr("hidden", false);
        username = null;
        socket = null;
    } else {
        let incomingMsg = document.createElement('div');
        incomingMsg.classList.add("incoming-msg");

        let receivedMsg = document.createElement('div');
        receivedMsg.classList.add("received-msg");

        let receivedWithdMsg = document.createElement('div');
        receivedWithdMsg.classList.add("received-withd-msg");

        let msg = document.createElement('p');

        let text = document.createTextNode(message.content);

        document.getElementById("lastMessage" + capitalize(message.from)).textContent = message.content;

        let timeDate = document.createElement('span');
        timeDate.classList.add("time-date");

        msg.appendChild(text);

        receivedWithdMsg.appendChild(msg);
        receivedWithdMsg.appendChild(timeDate);

        receivedMsg.appendChild(receivedWithdMsg);

        incomingMsg.appendChild(receivedMsg);

        let msgHistory = document.getElementById("msgHistory");
        msgHistory.appendChild(incomingMsg)
    }

    function capitalize(string)
    {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }

}
