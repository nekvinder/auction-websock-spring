package com.goego.auction.repositories;

import com.goego.auction.model.APMessageUpdateAuction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface APMUpdateRepository
  extends JpaRepository<APMessageUpdateAuction, Long> {}
