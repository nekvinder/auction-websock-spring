package com.goego.auction.model;

import com.google.gson.Gson;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
