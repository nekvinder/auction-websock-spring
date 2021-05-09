package com.goego.auction.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.goego.auction.model.APMessageBidAuction;
import com.goego.auction.model.APMessageJoinAuction;
import com.goego.auction.model.APMessageUpdateAuction;
import com.goego.auction.model.Auction;
import com.goego.auction.services.APMJoinService;
import com.goego.auction.services.APMUpdateService;
import com.goego.auction.services.AuctionService;
import com.google.gson.Gson;

@Component
public class WebSocketEventListener extends TextWebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	private Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();

	@Autowired
	AuctionService auctionService;

	@Autowired
	APMJoinService apmJoinService;

	@Autowired
	APMUpdateService apmUpdateService;

	@Autowired
	public WebSocketEventListener(AuctionService auctionService, APMJoinService apmJoinService,
			APMUpdateService apmUpdateService) {
		this.auctionService = auctionService;
		this.apmJoinService = apmJoinService;
		this.apmUpdateService = apmUpdateService;

	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String msg = message.getPayload().replaceAll("[\\[\\]]", "");
		String[] splitMsg = msg.split(",");
		UUID messageId = UUID.fromString(splitMsg[0]);
		String actionType = splitMsg[1];

		if (actionType.equals("PUT_BID")) {
			Gson gson = new Gson();
			String jsonData = msg.substring(splitMsg[0].length() + splitMsg[1].length() + 2);
			APMessageBidAuction bidMessage = gson.fromJson(jsonData, APMessageBidAuction.class);
			logger.info(msg);
			logger.info(jsonData);
			logger.info(bidMessage.newBid.toString());
		} else {
			throw new Exception("Unknown Action Type" + actionType);
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Auction auction = auctionService.joinUser(auctionService.getLatestAuction());

		// Notify this user wiht latest auction
		APMessageJoinAuction joinMessage = new APMessageJoinAuction(auction);
		joinMessage = apmJoinService.createOrUpdateAPMessageJoinAuction(joinMessage);
		session.sendMessage(new TextMessage(joinMessage.toString()));

		// Notify all other with updated bid
		APMessageUpdateAuction updateMessage = new APMessageUpdateAuction(auction);
		updateMessage = apmUpdateService.createOrUpdateAPMessageUpdateAuction(updateMessage);
		for (WebSocketSession userSession : sessions.values()) {
			userSession.sendMessage(new TextMessage(updateMessage.toString()));
		}

		// store this users session
		sessions.put(session.getId(), session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session.getId());

		Auction auction = auctionService.getLatestAuction();
		auction = auctionService.removeUser(auction);

		APMessageJoinAuction message = new APMessageJoinAuction(auction);
		message = apmJoinService.createOrUpdateAPMessageJoinAuction(message);
	}
}
