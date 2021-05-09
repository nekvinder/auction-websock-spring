var ws;
function setConnected (connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
}

function connect () {
	console.log("connecting...");
	ws = new WebSocket(`ws://${location.host}/auction`);

	ws.onopen = function (e) {
		console.log("open", e);
		sendData(); // testing
	};
	ws.onmessage = function (e) {
		console.log("msg raw", e.data);
		console.log("msg parsed", JSON.parse(e.data));
	};
	ws.onerror = function (e) {
		console.log("error", e);
	};
	ws.onclose = function (e) {
		console.log("close", e);
	};
	setConnected(true);
}

function disconnect () {
	if (ws != null) {
		ws.close();
	}
	setConnected(false);
	console.log("Websocket is in disconnected state");
}

function sendData () {
	var data = `[${uuidv4()},PUT_BID,` + JSON.stringify({ auctionId: 2, newBid: 10 }) + "]";
	console.log(1, "sending", data);
	ws.send(data);
}

function helloWorld (message) {
	$("#helloworldmessage").append("<tr><td> " + message + "</td></tr>");
}

$(function () {
	$("form").on('submit', function (e) {
		e.preventDefault();
	});
	$("#connect").click(function () {
		connect();
	});
	$("#disconnect").click(function () {
		disconnect();
	});
	$("#send").click(function () {
		sendData();
	});
});

function uuidv4 () {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
		var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
		return v.toString(16);
	});
}


connect(); // testing

