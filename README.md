# Auction System

Problem Statement : https://gist.github.com/yendenikhil/628b6a8c858dcb14d55f7dc77be823c3

## Solution Approach

The app is implemented as follows :

1. The Websocket communicaion protocol for client-server duplex communication. Uses Spring's Websocket library in the backend, and browsers' WebSocket lib. on frontend.
2. Custom protocol on top of simple text based websocket, with string manipulation to parse the messages as per protocol.
3. The UI is in Vue.js, which is then complied and embed into the `static/` folder.



---------------------------------------------------------------------------------------------------

## Config

By default uses in memory db, to facilitate faster testing and dev., but can be changed to file based db by changing _Application.properties_ :
```spring.datasource.url=jdbc:h2:file:path/to/testdb```

---------------------------------------------------------------------------------------------------

## Note

For expiring the auctions, a concurrent scheduler is used which is instantiated at the start of auction.

For testing purposes, a Class `InitializingAuctions.java` starts two auctions at the application startup :

Test1 and Test2 in the sequence, so that the Test2 is latest running. `Test2` then expires after a minute, so the next auction in line i.e. `Test1` is live,for another 2 minutes or so.

These can be adjusted in the same with updating lines :
```
service.createOrUpdateAuction(new Auction("test1", expiryLate.plusMinutes(3l)));
service.createOrUpdateAuction(new Auction("test2", expiryLate.plusMinutes(1l)));
```

## Deployment Guide

### Backend - Spring boot project

To build the project on linux, execute following in the project root directory :
```
sudo apt install maven
mvn package
java -jar target/auction-0.0.1-SNAPSHOT.jar
```

Visit to view the UI: http://127.0.0.1:8082

To view the in memory db : http://127.0.0.1:8082/h2-console

### Frontend UI

** It is not necessary to build this, current version is embed into the static folder already **

https://github.com/nekvinder/auction-system-ui

```
npm i
npm run serve
```

** Update `/src/websocket.ts` -> `const auctionServer = window.location.host` to point to the backend uri i.e --> `const auctionServer = "192.168.0.2:8082"`
