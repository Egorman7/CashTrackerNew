package liquidstars.cashtracker.model;

import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import liquidstars.cashtracker.model.database.Database;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.model.database.models.Category;
import liquidstars.cashtracker.model.database.models.Record;
import liquidstars.cashtracker.presenter.callbacks.DatabaseCallback;

public class MainModel {

    private Database database;

    public MainModel(Database database) {
        this.database = database;
    }

    // account
    public void prePopulate(final DatabaseCallback callback){
        Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return database.getAccountDao().insert(new Account("Main Account", "$", true, 0));
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onSuccess(Long aLong) {
                        callback.onComplete(aLong);
                    }
                    @Override
                    public void onError(Throwable e) { callback.onError();}
                });
    }

    public void getFirstAccount(final DatabaseCallback callback){
        database.getAccountDao().getFirstAccount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Account>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Account account) {
                        callback.onCompleteObject(account);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError();
                    }
                });
    }

    public Single<List<Account>> getAccounts(){  //change to single
        return database.getAccountDao().getAccounts();
    }

    public Single<String> getCurrency(long id){
        return database.getAccountDao().getCurrency(id);
    }

    public Single<String> getTitle(long id){
        return database.getAccountDao().getTitle(id);
    }

    // records

    public void getRecordsInRange(long account_id, String date_start, String date_end, final DatabaseCallback callback){
        database.getRecordDao().getRecordsInRange(account_id, date_start, date_end)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Record>>() {
                    @Override
                    public void accept(List<Record> records) throws Exception {
                        callback.onCompleteList(records);
                    }
                });
    }

    public Single<List<Record>> getRecords(long acc_id, long from, long limit) {
        return database.getRecordDao().getRecordsByAccount(acc_id, from, limit);
    }

    // categories

    public Flowable<List<Category>> getAllCategories(){
        return database.getCategoryDao().getAllCategories();
    }

    public void getCategoryTitle(boolean type, long id, final DatabaseCallback callback){
        database.getCategoryDao().getCategoryTitle(type, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        callback.onCompleteObject(s);
                    }
                });
    }

    // adding data

    public void addAccount(final Account account, final DatabaseCallback callback){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                database.getAccountDao().insert(account);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        callback.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void addRecord(final Record record, final DatabaseCallback callback){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Log.d("INSERT", "id = " + database.getRecordDao().insert(record));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("INSERT", "Subscribed!");
                    }

                    @Override
                    public void onComplete() {
                        callback.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("INSERT", "ERROR!");
                    }
                });
    }
}
