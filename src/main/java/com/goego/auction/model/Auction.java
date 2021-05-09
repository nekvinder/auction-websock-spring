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
    Duration duration = Duration.between(LocalDateTime.now(), this.expiry);
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
    this.connectedUsers = 0;
    this.startedAt = LocalDateTime.now();
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
