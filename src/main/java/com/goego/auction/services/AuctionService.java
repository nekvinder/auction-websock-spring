package com.goego.auction.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goego.auction.model.Auction;
import com.goego.auction.repositories.AuctionRepository;

@Service("auctionService")
public class AuctionService {

	@Autowired
	AuctionRepository repository;

	public List<Auction> getAllAuctions() {
		List<Auction> auctionList = repository.findAll();

		if (auctionList.size() > 0) {
			return auctionList;
		} else {
			return new ArrayList<Auction>();
		}
	}

	public Auction getAuctionById(Long id) throws Exception {
		Optional<Auction> auction = repository.findById(id);

		if (auction.isPresent()) {
			return auction.get();
		} else {
			throw new Exception("No auction record exist for given id");
		}
	}

	public Auction createOrUpdateAuction(Auction entity) throws Exception {
		entity = repository.save(entity);
		return entity;
	}

	public void deleteAuctionById(Long id) throws Exception {
		Optional<Auction> auction = repository.findById(id);

		if (auction.isPresent()) {
			repository.deleteById(id);
		} else {
			throw new Exception("No auction record exist for given id");
		}
	}

	public Auction getLatestAuction() throws Exception{
		try {
			List<Auction> sortedList = this.getAllAuctions().stream().sorted(Comparator.comparing(Auction::getStartedAt).reversed())
					.collect(Collectors.toList());
			return sortedList.get(0);
		} catch (Exception e) {
			throw new Exception("No auction exists");
		}
	}

}
