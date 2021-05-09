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
	};
	ws.onmessage = function (e) {
		console.log("msg", JSON.parse(e.data));
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
	var data = JSON.stringify({
		'user': $("#user").val()
	})
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
