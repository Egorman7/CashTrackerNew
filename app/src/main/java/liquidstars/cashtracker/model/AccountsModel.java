package liquidstars.cashtracker.model;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import liquidstars.cashtracker.model.database.Database;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.presenter.callbacks.DatabaseCallback;

public class AccountsModel {
    private Database database;

    public AccountsModel(Database database) {
        this.database = database;
    }

    public void loadData(final DatabaseCallback callback){
        database.getAccountDao().getAccountsF()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Account>>() {
                    @Override
                    public void accept(List<Account> accounts) throws Exception {
                        callback.onCompleteList(accounts);
                    }
                });
    }

    public void insertAccount(final Account account, final DatabaseCallback callback){
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

    public void updateAccount(final Account account, final DatabaseCallback callback){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                database.getAccountDao().update(account);
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
}
