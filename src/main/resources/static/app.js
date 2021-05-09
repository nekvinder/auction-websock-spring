var ws;
function setConnected(connected) {
    $('#connect').prop('disabled', connected);
    $('#disconnect').prop('disabled', !connected);
}
function connect() {
    console.log('connecting...');
    ws = new WebSocket("ws://" + location.host + "/auction");
    ws.onopen = function (e) {
        console.log('open', e);
        sendData(); // testing
    };
    ws.onmessage = function (e) {
        console.log('RAW MESSAGE:', e.data);
        executeMessage(e.data);
    };
    ws.onerror = function (e) {
        console.log('error', e);
    };
    ws.onclose = function (e) {
        console.log('close', e);
    };
    setConnected(true);
}
var disconnect = function () {
    if (ws != null)
        ws.close();
    setConnected(false);
    console.log('Websocket is in disconnected state');
};
var sendData = function () {
    var data = "[" + uuidv4() + ",PUT_BID," + JSON.stringify({ auctionId: 2, newBid: 10 }) + ']';
    console.log(1, 'sending', data);
    ws.send(data);
};
$(function () {
    $('form').on('submit', function (e) {
        e.preventDefault();
    });
    $('#connect').click(function () {
        connect();
    });
    $('#disconnect').click(function () {
        disconnect();
    });
    $('#send').click(function () {
        sendData();
    });
});
var uuidv4 = function () {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (Math.random() * 16) | 0, v = c == 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
    });
};
var ActionType;
(function (ActionType) {
    ActionType["JOIN_AUCTION"] = "JOIN_AUCTION";
    ActionType["UPDATE_AUCTION"] = "UPDATE_AUCTION";
    ActionType["PUT_BID"] = "PUT_BID";
})(ActionType || (ActionType = {}));
var executeMessage = function (data) {
    var msg = data.replace('[', '').replace(']', '');
    var splitMsg = msg.split(',');
    var messageId = splitMsg[0];
    var actionType = splitMsg[1];
    var payload = msg.substring(messageId.length + actionType.length + 2);
    switch (actionType) {
        case ActionType.JOIN_AUCTION:
            console.log(messageId, actionType, payload);
            break;
        case ActionType.UPDATE_AUCTION:
            console.log(messageId, actionType, payload);
            break;
    }
};
connect(); // testing
