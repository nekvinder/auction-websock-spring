package com.goego.auction.services;

import com.goego.auction.model.APMessageUpdateAuction;
import com.goego.auction.repositories.APMUpdateRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("apmUpdateService")
public class APMUpdateService {

  @Autowired
  APMUpdateRepository repository;

  public List<APMessageUpdateAuction> getAllAPMessageUpdateAuctions() {
    List<APMessageUpdateAuction> auctionList = repository.findAll();

    if (auctionList.size() > 0) {
      return auctionList;
    } else {
      return new ArrayList<APMessageUpdateAuction>();
    }
  }

  public APMessageUpdateAuction getAPMessageUpdateAuctionById(Long id)
    throws Exception {
    Optional<APMessageUpdateAuction> auction = repository.findById(id);

    if (auction.isPresent()) {
      return auction.get();
    } else {
      throw new Exception("No auction record exist for given id");
    }
  }

  public APMessageUpdateAuction createOrUpdateAPMessageUpdateAuction(
    APMessageUpdateAuction entity
  )
    throws Exception {
    entity = repository.save(entity);
    return entity;
  }

  public void deleteAPMessageUpdateAuctionById(Long id) throws Exception {
    Optional<APMessageUpdateAuction> auction = repository.findById(id);

    if (auction.isPresent()) {
      repository.deleteById(id);
    } else {
      throw new Exception("No auction record exist for given id");
    }
  }
}
