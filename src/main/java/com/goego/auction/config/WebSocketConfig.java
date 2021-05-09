package com.goego.auction.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import com.goego.auction.controller.WebSocketEventListener;
import com.goego.auction.services.AuctionService;
import com.goego.auction.services.SessionsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	AuctionService auctionService;

	@Autowired
	SessionsService sessionsService;

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new WebSocketEventListener(auctionService, sessionsService), "/auction")
				.setAllowedOrigins("*");
	}

}
