package liquidstars.cashtracker.model;

import android.util.Log;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import liquidstars.cashtracker.model.database.Database;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.model.database.models.Record;
import liquidstars.cashtracker.presenter.callbacks.DatabaseCallback;

public class JournalModel {
    private Database database;

    public JournalModel(Database database) {
        this.database = database;
    }

    public void loadFirstAccount(final DatabaseCallback callback){
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

    public void loadRecords(long accId, String from, String to, final DatabaseCallback callback){
        Log.d("RECORDS", "From " + from + " to " + to);
        database.getRecordDao().getRecordsInRange(accId, from, to)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Record>>() {
                    @Override
                    public void accept(List<Record> records) throws Exception {
                        Log.d("RECORDS", "Records loaded into model " + records.size());
                        callback.onCompleteList(records);
                    }
                });
    }

    public void loadCategoryTitle(long categoryId, boolean type, final DatabaseCallback callback){
        database.getCategoryDao().getCategoryTitle(type, categoryId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        callback.onCompleteObject(s);
                    }
                });
    }

    public void loadCurrency(long accId, final DatabaseCallback callback){
        database.getAccountDao().getCurrency(accId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        callback.onCompleteObject(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
