var ws: WebSocket

function setConnected(connected) {
  $('#connect').prop('disabled', connected)
  $('#disconnect').prop('disabled', !connected)
}

function connect() {
  console.log('connecting...')
  ws = new WebSocket(`ws://${location.host}/auction`)

  ws.onopen = (e) => {
    console.log('open', e)
    sendData() // testing
  }

  ws.onmessage = (e) => {
    console.log('RAW MESSAGE:', e.data)
    executeMessage(e.data)
  }
  ws.onerror = (e) => {
    console.log('error', e)
  }
  ws.onclose = (e) => {
    console.log('close', e)
  }
  setConnected(true)
}

const disconnect = () => {
  if (ws != null) ws.close()
  setConnected(false)
  console.log('Websocket is in disconnected state')
}

const sendData = () => {
  var data = `[${uuidv4()},PUT_BID,` + JSON.stringify({ auctionId: 2, newBid: 10 }) + ']'
  console.log(1, 'sending', data)
  ws.send(data)
}

$(function () {
  $('form').on('submit', function (e) {
    e.preventDefault()
  })
  $('#connect').click(function () {
    connect()
  })
  $('#disconnect').click(function () {
    disconnect()
  })
  $('#send').click(function () {
    sendData()
  })
})

const uuidv4 = () => {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    var r = (Math.random() * 16) | 0,
      v = c == 'x' ? r : (r & 0x3) | 0x8
    return v.toString(16)
  })
}

enum ActionType {
  JOIN_AUCTION = 'JOIN_AUCTION',
  UPDATE_AUCTION = 'UPDATE_AUCTION',
  PUT_BID = 'PUT_BID',
}

interface Auction {
  auctionId: string
  auctionName: string
  remainingTime: number
  connectedUsers: number
  currentBid: number
}

const executeMessage = (data: string) => {
  const msg = data.replace('[', '').replace(']', '')
  const splitMsg = msg.split(',')
  const messageId = splitMsg[0]
  const actionType: ActionType = splitMsg[1] as ActionType
  const payload = msg.substring(messageId.length + actionType.length + 2)
  switch (actionType) {
    case ActionType.JOIN_AUCTION:
      console.log(messageId, actionType, payload)
      break
    case ActionType.UPDATE_AUCTION:
      console.log(messageId, actionType, payload)
      break
  }
}

connect() // testing
