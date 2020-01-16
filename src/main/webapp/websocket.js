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
    $("#chat-container").hide();
    $("#login-component").attr("hidden", false);
}

function selectContact(contact) {
    selectedContact = contact;
}

function sendMessage() {
    var messageInput = document.getElementById("outgoingMessage");
    var messageContent = messageInput.value.trim();
    if (messageContent) {
        let chatMessage = {
            from: username,
            to: "sergio" + "@localhost",
            content: messageInput.value
        };
        console.log(chatMessage);
        socket.send(JSON.stringify(chatMessage));


        let outgoingMsg = document.createElement('div');
        outgoingMsg.classList.add("outgoing_msg");

        let sentMsg = document.createElement('div');
        sentMsg.classList.add("sent_msg");

        let msg = document.createElement('p');

        let text = document.createTextNode(messageInput.value);

        let timeDate = document.createElement('span');
        timeDate.classList.add("time_date");

        msg.appendChild(text);

        sentMsg.appendChild(msg);
        sentMsg.appendChild(timeDate);

        outgoingMsg.appendChild(sentMsg);

        let msgHistory = document.getElementById("msg_history");
        msgHistory.appendChild(outgoingMsg);

        messageInput.value = "";
    }
}

function onMessageReceive(event) {
    let message = JSON.parse(event.data);
    console.log(message);

    if (message.messageType === 'JOIN') {
        $("#chat-container").attr("hidden", false);
        $("#message-area").attr("hidden", false);
        console.log(message.to + ' joined!');
    } else if (message.messageType === 'ERROR') {
        $("#chat-container").attr("hidden", true);
        $("#login-component").attr("hidden", false);
        username = null;
        socket = null;
    } else {
        let incomingMsg = document.createElement('div');
        incomingMsg.classList.add("incoming_msg");

        let receivedMsg = document.createElement('div');
        receivedMsg.classList.add("received_msg");

        let receivedWithdMsg = document.createElement('div');
        receivedWithdMsg.classList.add("received_withd_msg");

        let msg = document.createElement('p');

        let text = document.createTextNode(message.content);

        let timeDate = document.createElement('span');
        timeDate.classList.add("time_date");

        msg.appendChild(text);

        receivedWithdMsg.appendChild(msg);
        receivedWithdMsg.appendChild(timeDate);

        receivedMsg.appendChild(receivedWithdMsg);

        incomingMsg.appendChild(receivedMsg);

        let msgHistory = document.getElementById("msg_history");
        msgHistory.appendChild(incomingMsg)
    }

}
