let socket;

function onMessage(event) {
    // let message = JSON.parse(event.data);
    console.log(event);

    // if (device.action === "add") {
    //     printDeviceElement(device);
    // }
    // if (device.action === "remove") {
    //     document.getElementById(device.id).remove();
    //     //device.parentNode.removeChild(device);
    // }
    // if (device.action === "toggle") {
    //     var node = document.getElementById(device.id);
    //     var statusText = node.children[2];
    //     if (device.status === "On") {
    //         statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn off</a>)";
    //     } else if (device.status === "Off") {
    //         statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn on</a>)";
    //     }
    // }
}

function formSubmit() {
    let form = document.getElementById("connectForm");
    let name = form.elements["name"].value;
    document.getElementById("connectForm").reset();
    connectToServer(name);
}


function connectToServer(name) {
    socket = new WebSocket("ws://localhost:8080/javaee-websocket-xmpp-client/chat/" + name);
    socket.onmessage = onMessage;
}

function sendMessage() {
    let form = document.getElementById("messageForm");
    let content = form.elements["content"].value;
    // let id = element;
    let message = {
        from: "user1@localhost",
        to: "sergio@localhost",
        content: content
    };
    socket.send(JSON.stringify(message));
}

// function printDeviceElement(device) {
//     var content = document.getElementById("content");
//
//     var deviceDiv = document.createElement("div");
//     deviceDiv.setAttribute("id", device.id);
//     deviceDiv.setAttribute("class", "device " + device.type);
//     content.appendChild(deviceDiv);
//
//     var deviceName = document.createElement("span");
//     deviceName.setAttribute("class", "deviceName");
//     deviceName.innerHTML = device.name;
//     deviceDiv.appendChild(deviceName);
//
//     var deviceType = document.createElement("span");
//     deviceType.innerHTML = "<b>Type:</b> " + device.type;
//     deviceDiv.appendChild(deviceType);
//
//     var deviceStatus = document.createElement("span");
//     if (device.status === "On") {
//         deviceStatus.innerHTML = "<b>Status:</b> " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn off</a>)";
//     } else if (device.status === "Off") {
//         deviceStatus.innerHTML = "<b>Status:</b> " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn on</a>)";
//         //deviceDiv.setAttribute("class", "device off");
//     }
//     deviceDiv.appendChild(deviceStatus);
//
//     var deviceDescription = document.createElement("span");
//     deviceDescription.innerHTML = "<b>Comments:</b> " + device.description;
//     deviceDiv.appendChild(deviceDescription);
//
//     var removeDevice = document.createElement("span");
//     removeDevice.setAttribute("class", "removeDevice");
//     removeDevice.innerHTML = "<a href=\"#\" OnClick=removeDevice(" + device.id + ")>Remove device</a>";
//     deviceDiv.appendChild(removeDevice);
// }
