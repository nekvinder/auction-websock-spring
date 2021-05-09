package com.goego.auction.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goego.auction.model.APMessageJoinAuction;
import com.goego.auction.repositories.APMJoinRepository;


@Service("apmJoinService")
public class APMJoinService {

	@Autowired
	APMJoinRepository repository;

	public List<APMessageJoinAuction> getAllAPMessageJoinAuctions() {
		List<APMessageJoinAuction> auctionList = repository.findAll();

		if (auctionList.size() > 0) {
			return auctionList;
		} else {
			return new ArrayList<APMessageJoinAuction>();
		}
	}

	public APMessageJoinAuction getAPMessageJoinAuctionById(Long id) throws Exception {
		Optional<APMessageJoinAuction> auction = repository.findById(id);

		if (auction.isPresent()) {
			return auction.get();
		} else {
			throw new Exception("No auction record exist for given id");
		}
	}

	public APMessageJoinAuction createOrUpdateAPMessageJoinAuction(APMessageJoinAuction entity) throws Exception {
		entity = repository.save(entity);
		return entity;
	}

	public void deleteAPMessageJoinAuctionById(Long id) throws Exception {
		Optional<APMessageJoinAuction> auction = repository.findById(id);

		if (auction.isPresent()) {
			repository.deleteById(id);
		} else {
			throw new Exception("No auction record exist for given id");
		}
	}	

}
