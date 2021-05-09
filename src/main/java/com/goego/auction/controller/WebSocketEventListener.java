package com.goego.auction.controller;

import java.io.IOException;
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
import com.goego.auction.services.SessionsService;
import com.google.gson.Gson;

@Component
public class WebSocketEventListener extends TextWebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	

	@Autowired
	AuctionService auctionService;

	@Autowired
	SessionsService sessionService;
	
	@Autowired
	APMJoinService apmJoinService;

	@Autowired
	APMUpdateService apmUpdateService;

	@Autowired
	public WebSocketEventListener(AuctionService auctionService, APMJoinService apmJoinService,
			APMUpdateService apmUpdateService,SessionsService sessionService) {
		this.auctionService = auctionService;
		this.apmJoinService = apmJoinService;
		this.apmUpdateService = apmUpdateService;
		this.sessionService= sessionService;
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
			Auction auction = (auctionService.getAuctionById(bidMessage.auctionId));
			if (auction.currentBid < bidMessage.newBid) {
				auction.currentBid = bidMessage.newBid;
				auction = auctionService.createOrUpdateAuction(auction);
				sessionService.broadcastAuctionToSessions(auction);
			} else {
				logger.info("Smaller bid , no changes applicable");
			}

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
		sessionService.broadcastAuctionToSessions(auction);

		// store this users session
		sessionService.sessions.put(session.getId(), session);
	}

	

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionService.sessions.remove(session.getId());
		sessionService.sessionEndAuctionUpdate();
	}

	
}
