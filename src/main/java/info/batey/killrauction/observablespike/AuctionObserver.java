package info.batey.killrauction.observablespike;

import info.batey.killrauction.domain.BidVo;
import rx.Observable;
import rx.Subscriber;

public class AuctionObserver implements Observable.OnSubscribe<BidVo> {

    @Override
    public void call(Subscriber<? super BidVo> subscriber) {

    }
}
