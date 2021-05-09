package com.goego.auction.model;

import com.google.gson.Gson;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class APMessageUpdateAuction {

  @Id
  @GeneratedValue
  UUID MESSAGE_ID;

  Long auctionId;
  String auctionName;
  Long remainingTime;
  Integer connectedUsers;
  Float currentBid;

  public APMessageUpdateAuction() {}

  public APMessageUpdateAuction(Auction auction) {
    this.auctionId = auction.id;
    this.auctionName = auction.auctioName;
    this.remainingTime = auction.remainingTime();
    this.connectedUsers = auction.connectedUsers;
    this.currentBid = auction.currentBid;
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    String response =
      "[" +
      this.MESSAGE_ID +
      "," +
      "UPDATE_AUCTION" +
      "," +
      gson.toJson(this) +
      "]";
    return response;
  }
}
