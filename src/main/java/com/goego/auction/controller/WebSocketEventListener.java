package com.goego.auction.controller;

import com.goego.auction.model.APMessageJoinAuction;
import com.goego.auction.model.APMessageUpdateAuction;
import com.goego.auction.model.Auction;
import com.goego.auction.services.APMJoinService;
import com.goego.auction.services.APMUpdateService;
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

  private static final Logger logger = LoggerFactory.getLogger(
    WebSocketEventListener.class
  );
  private Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();

  @Autowired
  AuctionService auctionService;

  @Autowired
  APMJoinService apmJoinService;

  @Autowired
  APMUpdateService apmUpdateService;

  @Autowired
  public WebSocketEventListener(
    AuctionService auctionService,
    APMJoinService apmJoinService,
    APMUpdateService apmUpdateService
  ) {
    this.auctionService = auctionService;
    this.apmJoinService = apmJoinService;
    this.apmUpdateService = apmUpdateService;
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message)
    throws Exception {
    logger.info(message.getPayload());
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session)
    throws Exception {
    Auction auction = auctionService.joinUser(
      auctionService.getLatestAuction()
    );

    //Notify this user wiht latest auction
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
  public void afterConnectionClosed(
    WebSocketSession session,
    CloseStatus status
  )
    throws Exception {
    sessions.remove(session.getId());

    Auction auction = auctionService.getLatestAuction();
    auction = auctionService.removeUser(auction);

    APMessageJoinAuction message = new APMessageJoinAuction(auction);
    message = apmJoinService.createOrUpdateAPMessageJoinAuction(message);
  }
}
