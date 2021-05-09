package com.goego.auction.model;

import java.util.UUID;
import lombok.Data;

@Data
public class APMessageBidAuction {

	public Long auctionId;
	public Float newBid;

	public APMessageBidAuction() {
	}

	public APMessageBidAuction(UUID MESSAGE_ID, Long auctionId, Float newBid) {
		this.auctionId = auctionId;
		this.newBid = newBid;
	}
}
