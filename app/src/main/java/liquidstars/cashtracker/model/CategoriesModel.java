package liquidstars.cashtracker.model;

import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import liquidstars.cashtracker.model.database.Database;
import liquidstars.cashtracker.model.database.models.Category;
import liquidstars.cashtracker.presenter.callbacks.DatabaseCallback;

public class CategoriesModel {
    private Database database;

    public CategoriesModel(Database database) {
        this.database = database;
    }

    public void loadCategoriesByType(boolean type, final DatabaseCallback callback){
        database.getCategoryDao().getCategoriesByType(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        callback.onCompleteList(categories);
                    }
                });
    }

    public void doUpdate(final Category category, final DatabaseCallback callback){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                database.getCategoryDao().update(category);
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
                        Log.e("UPDATE_CAT", e.getMessage());
                        callback.onError();
                    }
                });
    }

    public void doInsert(final Category category, final DatabaseCallback callback){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                database.getCategoryDao().insert(category);
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
                    Log.e("INSERT_CAT", e.getMessage());
                    callback.onError();
                }
            });
    }
}
