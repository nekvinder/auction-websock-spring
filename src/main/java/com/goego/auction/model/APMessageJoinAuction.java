package com.goego.auction.model;

import com.google.gson.Gson;
import java.util.UUID;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

@Data
public class APMessageJoinAuction {

	@Id
	@GeneratedValue
	UUID MESSAGE_ID;

	Long auctionId;
	String auctionName;
	Boolean isExpired;
	Long remainingTime;
	Integer connectedUsers;
	Float currentBid;

	public APMessageJoinAuction() {
	}

	public APMessageJoinAuction(Auction auction) {
		this.MESSAGE_ID = UUID.randomUUID();
		this.auctionId = auction.id;
		this.auctionName = auction.auctioName;
		this.remainingTime = auction.remainingTime();
		this.connectedUsers = auction.connectedUsers;
		this.currentBid = auction.currentBid;
		this.isExpired = auction.isExpired;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		String response = "[" + this.MESSAGE_ID + "," + "JOIN_AUCTION" + "," + gson.toJson(this) + "]";
		return response;
	}
}
