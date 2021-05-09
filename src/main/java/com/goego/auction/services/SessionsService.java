package com.goego.auction.services;

import com.goego.auction.model.APMessageJoinAuction;
import com.goego.auction.model.APMessageUpdateAuction;
import com.goego.auction.model.Auction;
import com.goego.auction.repositories.APMJoinRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service("sessionsService")
public class SessionsService {

	@Autowired
	APMUpdateService apmUpdateService;

	@Autowired
	AuctionService auctionService;

	public Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();

	public void broadcastAuctionToSessions(Auction auction) throws Exception, IOException {
		System.out.println("Broadcasting " + auction.auctioName);
		APMessageUpdateAuction updateMessage = new APMessageUpdateAuction(auction);
		updateMessage = apmUpdateService.createOrUpdateAPMessageUpdateAuction(updateMessage);
		for (WebSocketSession userSession : sessions.values()) {
			if (userSession.isOpen()) {
				userSession.sendMessage(new TextMessage(updateMessage.toString()));
			} else {
				sessionEndAuctionUpdate();
			}
		}
	}

	public void sessionEndAuctionUpdate() throws Exception, IOException {
		Auction auction = auctionService.getLatestAuction();
		auction = auctionService.removeUser(auction);
		broadcastAuctionToSessions(auction);
	}
}
