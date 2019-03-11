package liquidstars.cashtracker.model;

import android.util.Log;

import java.util.List;

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
import liquidstars.cashtracker.presenter.callbacks.DatabaseCallback;

public class AddModel {
    private Database database;

    public AddModel(Database database) {
        this.database = database;
    }

    public Single<List<Account>> getAccounts(){
        return database.getAccountDao().getAccounts();
    }

    public Flowable<List<Category>> getCategories(boolean type){
        return database.getCategoryDao().getCategoriesByType(type);
    }

    public void getFirstCategory(boolean type, final DatabaseCallback callback){
        database.getCategoryDao().getFirstCategory(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Category>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("SINGLE_CAT", "Subscribed!");
                    }

                    @Override
                    public void onSuccess(Category category) {
                        Log.d("SINGLE_CAT", "Got category " + category.getTitle());
                        callback.onComplete(category);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SINGLE_CAT", e.getMessage());
                    }
                });
    }
}
