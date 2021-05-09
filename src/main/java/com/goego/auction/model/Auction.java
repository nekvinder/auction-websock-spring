package com.goego.auction.model;

import com.google.gson.Gson;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@Data
@Entity
public class Auction {

	@Id
	@GeneratedValue
	public Long id;

	public String auctioName;
	public LocalDateTime expiry;
	public Boolean isExpired;
	public Float currentBid;
	public Integer connectedUsers;

	@CreatedDate
	public LocalDateTime startedAt;

	public Boolean updateAuctionExipryStatus() {
		this.isExpired = this.expiry.isBefore(LocalDateTime.now());
		return this.isExpired;
	}

	public Long remainingTime() {
		Duration duration = Duration.between(LocalDateTime.now(), this.expiry);
		return duration.getSeconds();
	}

	public LocalDateTime getStartedAt() {
		return this.startedAt;
	}

	public Auction() {
	}

	public Auction(String auctionName, LocalDateTime expiry) {
		this.auctioName = auctionName;
		this.expiry = expiry;
		this.isExpired = this.updateAuctionExipryStatus();
		this.currentBid = 0.0f;
		this.connectedUsers = 0;
		this.startedAt = LocalDateTime.now();
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
