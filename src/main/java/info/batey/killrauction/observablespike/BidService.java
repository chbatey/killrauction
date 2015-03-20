package info.batey.killrauction.observablespike;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import info.batey.killrauction.infrastruture.AuctionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
public class BidService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BidService.class);

    private AuctionDao auctionDao;

    @Inject
    private SimpMessagingTemplate messagingTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ConcurrentMap<String, Subject<BidVo, BidVo>> observers;

    @Inject
    public BidService(AuctionDao auctionDao) {
        this.auctionDao = auctionDao;
    }

    @PostConstruct
    public void initialise() {
        observers = auctionDao.getAllAuctionsSparse().stream()
                .map(auction -> auctionDao.getAuction(auction.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(auction -> {
                            PublishSubject<BidVo> tPublishSubject = PublishSubject.create();
                            Subject<BidVo, BidVo> bids = new SerializedSubject<>(tPublishSubject);
                            return new AuctionObservable(auction.getName(), bids);
                        }
                ).collect(Collectors.toConcurrentMap(AuctionObservable::getName, AuctionObservable::getBids));

        for (Map.Entry<String, Subject<BidVo, BidVo>> stringSubjectEntry : observers.entrySet()) {
            String auction = stringSubjectEntry.getKey();
            stringSubjectEntry.getValue().subscribe(bidVo -> {
                try {
                    messagingTemplate.convertAndSend("/topic/" + auction, objectMapper.writeValueAsString(bidVo));
                } catch (JsonProcessingException e) {
                    LOGGER.warn("Unable to send bid", e);
                }
            });
        }
    }

    public List<BidVo> subscribe(String auctionName) {
        Auction auction = auctionDao.getAuction(auctionName).get();
        return auction.getBids();
    }

    public void recordBid(String auctionName, BidVo bid) {
        LOGGER.debug("Recording bid {} for auction {}", bid, auctionName);
        Subject<BidVo, BidVo> bidVoBidVoSubject = this.observers.get(auctionName);
        bidVoBidVoSubject.onNext(bid);
    }

    public void addAuction(Auction auction) {
        PublishSubject<BidVo> tPublishSubject = PublishSubject.create();
        Subject<BidVo, BidVo> bids = new SerializedSubject<>(tPublishSubject);
        bids.subscribe(bidVo -> {
            try {
                messagingTemplate.convertAndSend("/topic/" + auction.getName(), objectMapper.writeValueAsString(bidVo));
            } catch (JsonProcessingException e) {
                LOGGER.warn("Unable to send bid", e);
            }
        } );
        observers.put(auction.getName(), bids);
    }
    private static class AuctionObservable {
        private String name;
        private Subject<BidVo, BidVo> bids;

        public AuctionObservable(String name, Subject<BidVo, BidVo> bids) {
            this.name = name;
            this.bids = bids;
        }

        public String getName() {
            return name;
        }

        public Subject<BidVo, BidVo> getBids() {
            return bids;
        }
    }

}
