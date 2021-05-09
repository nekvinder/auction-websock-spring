package com.goego.auction.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.goego.auction.model.APMessage.ActionType;
import com.google.gson.Gson;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.json.JSONObject;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data
@Entity
public class APMessageJoinAuction {

	@Id
	@GeneratedValue
	UUID MESSAGE_ID;

	Long auctionId;
	String auctionName;
	Long remainingTime;
	Integer connectedUsers;
	Float currentBid;

	public APMessageJoinAuction() {
	}

	public APMessageJoinAuction(Auction auction) {
		this.auctionId = auction.id;
		this.auctionName = auction.auctioName;
		this.remainingTime = auction.remainingTime();
		this.connectedUsers = auction.connectedUsers;
		this.currentBid = auction.currentBid;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		String response = "[" + this.MESSAGE_ID + "," + "JOIN_AUCTION" + "," + gson.toJson(this) + "]";
		return response;
	}

}
