package cn.trustway.nb.trustwayutil;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by huzan on 2017/11/10.
 * 描述：
 */

public class TextRxActivity extends Activity {
    private int test = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("TextRxActivity.onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("TextRxActivity.onNext");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("TextRxActivity.onError");
            }

            @Override
            public void onComplete() {
                System.out.println("TextRxActivity.onComplete");
            }
        };


        Observable<String> observable = Observable.create(e -> {
//
//            subscriber.onNext("12");
//            subscriber.onComplete();
        });
//
//        Flowable<Integer> retry = getTest().retry(3);
//        retry.subscribe(new DisposableSubscriber<Integer>() {
//            @Override
//            protected void onStart() {
//                super.onStart();
//                cancel();
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
        Completable.complete();
//        Flowable.merge(Completable.me(),Completable.complete(),)
                Completable.mergeArray(getTest(),getTest())
                        .doOnError(throwable -> {

                        })
                        .onErrorComplete()
                 ;
//        getTest().f;

//        Single.create(e -> {
//            e.onSuccess();
//
//        })

        observable.subscribe(observer);
    }


    private Completable getTest() {
        return Completable.create(e -> {
            e.onComplete();
        });
    }
    private Flowable<Integer> getTest1() {
        return Flowable.generate(integerEmitter ->
        {
//            integerEmitter.onError();
        });
    }
}
