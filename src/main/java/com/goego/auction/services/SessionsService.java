package com.goego.auction.services;

import com.goego.auction.model.APMessageUpdateAuction;
import com.goego.auction.model.Auction;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service("sessionsService")
public class SessionsService {

	@Autowired
	AuctionService auctionService;

	private Map<String, Map<String, WebSocketSession>> sessions = new HashMap<String, Map<String, WebSocketSession>>();

	public void addSession(Auction auction, WebSocketSession session) {
		if (!sessions.containsKey(auction.id.toString())) {
			sessions.put(auction.id.toString(), new HashMap<String, WebSocketSession>());
		}
		Map<String, WebSocketSession> auctionSessions = sessions.get(auction.id.toString());
		auctionSessions.put(session.getId(), session);
	}

	public String removeSession(WebSocketSession session) {
		for (Map.Entry<String, Map<String, WebSocketSession>> auctionSessions : sessions.entrySet()) {
			if (auctionSessions.getValue().containsKey(session.getId())) {
				auctionSessions.getValue().remove(session.getId());
				return  auctionSessions.getKey();
			}
		}
		return "";
	}

	public Map<String, WebSocketSession> getSessions(Auction auction) {
		if (sessions.containsKey(auction.id.toString())) {
			Map<String, WebSocketSession> auctionList = sessions.get(auction.id.toString());
			if (auctionList.size() > 0) {
				return auctionList;
			} else {
				return new HashMap<String, WebSocketSession>();
			}
		} else {
			return new HashMap<String, WebSocketSession>();
		}
	}

	public void broadcastAuctionToSessions(Auction auction) throws Exception, IOException {
		System.out.println("Broadcasting " + auction.auctioName);
		APMessageUpdateAuction updateMessage = new APMessageUpdateAuction(auction);
		for (WebSocketSession userSession : getSessions(auction).values()) {
			if (userSession.isOpen()) {
				userSession.sendMessage(new TextMessage(updateMessage.toString()));
			} else {
				sessionEndAuctionUpdate(auction);
			}
		}
	}

	public void sessionEndAuctionUpdate(Auction auction) throws Exception, IOException {
		auction = auctionService.removeUser(auction);
		broadcastAuctionToSessions(auction);
	}
}
