package com.goego.auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goego.auction.model.APMessageJoinAuction;
import com.goego.auction.model.Auction;

@Repository
public interface APMJoinRepository 
        extends JpaRepository<APMessageJoinAuction, Long> {
 
}