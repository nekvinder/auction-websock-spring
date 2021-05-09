package com.goego.auction.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Data
@Entity
public class Auction {

	@Id
	@GeneratedValue
	Long id;

	String auctioName;
	LocalDateTime expiry;
	Float currentBid;
	public Integer connectedUsers;

	@CreatedDate
	LocalDateTime startedAt;


	public Boolean isExpired() {
		return this.expiry.isBefore(LocalDateTime.now());
	}

	public Long remainingTime() {
		Duration duration = Duration.between(LocalDateTime.now(),this.expiry);
		return duration.getSeconds();
	}

	public LocalDateTime getStartedAt() {
		return this.startedAt;
	}

	
	public Auction() {}
	
	public Auction(String auctionName, LocalDateTime expiry) {
		this.auctioName = auctionName;
		this.expiry = expiry;
		this.currentBid = 0.0f;
		this.connectedUsers= 0;
		this.startedAt = LocalDateTime.now();
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
