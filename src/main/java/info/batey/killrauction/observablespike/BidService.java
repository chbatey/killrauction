package info.batey.killrauction.observablespike;

import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import info.batey.killrauction.infrastruture.AuctionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
public class BidService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BidService.class);

    private AuctionDao auctionDao;

    private ConcurrentMap<String, Subject<BidVo, BidVo>> liveUpdates = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Observable<BidVo>> observers;

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
                            Observable<BidVo> bids = new SerializedSubject<>(tPublishSubject);
                            return new AuctionObservable(auction.getName(), bids);
                        }
                ).collect(Collectors.toConcurrentMap(AuctionObservable::getName, AuctionObservable::getBids));
    }

    public Observable<BidVo> getSubscriptionToAuction(String auctionName, Action1<BidVo> onNext) {
        Auction auction = auctionDao.getAuction(auctionName).get();
        PublishSubject<BidVo> tPublishSubject = PublishSubject.create();
        Subject<BidVo, BidVo> subject = new SerializedSubject<>(tPublishSubject);
        subject.subscribe(onNext);
        for (BidVo bidVo : auction.getBids()) {
            LOGGER.debug("Forwarding event {}", bidVo);
            subject.onNext(bidVo);
        }

        return subject;
    }
    
    private static class AuctionObservable {
        private String name;
        private Observable<BidVo> bids;

        public AuctionObservable(String name, Observable<BidVo> bids) {
            this.name = name;
            this.bids = bids;
        }

        public String getName() {
            return name;
        }

        public Observable<BidVo> getBids() {
            return bids;
        }
    }

}
