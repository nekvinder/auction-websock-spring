package com.goego.auction.repositories;

import com.goego.auction.model.APMessageJoinAuction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface APMJoinRepository
  extends JpaRepository<APMessageJoinAuction, Long> {}
