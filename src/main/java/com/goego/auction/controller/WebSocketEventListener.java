package com.goego.auction.controller;

import com.goego.auction.model.Auction;
import com.goego.auction.services.AuctionService;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketEventListener extends TextWebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	private Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();

	@Autowired
	AuctionService service;

	@Autowired

	public WebSocketEventListener(AuctionService service) {
		this.service = service;
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		logger.info(message.getPayload());
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.put(session.getId(), session);
		Auction auction = service.getLatestAuction();
		session.sendMessage(new TextMessage(auction.getJSON()));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		sessions.remove(session.getId());
	}
}
