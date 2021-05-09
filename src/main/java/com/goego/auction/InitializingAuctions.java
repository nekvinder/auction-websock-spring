package com.goego.auction;

import java.io.Console;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.goego.auction.model.Auction;
import com.goego.auction.services.AuctionService;

@Component
public class InitializingAuctions {

	@Autowired
	AuctionService service; 
	private static final Logger logger = LoggerFactory.getLogger(InitializingAuctions.class);

	@PostConstruct
	private void init() {
		LocalDateTime expiryLate = LocalDateTime.now();
//		expiryLate = expiryLate.plusMinutes(15);
		expiryLate = expiryLate.plusSeconds(25);
		try {
			service.createOrUpdateAuction(new Auction("test1", expiryLate));
			service.createOrUpdateAuction(new Auction("test2", expiryLate));
			logger.info(service.getLatestAuction().toString());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}
}