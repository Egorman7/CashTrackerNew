package liquidstars.cashtracker.presenter;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import liquidstars.cashtracker.model.AddModel;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.model.database.models.Category;
import liquidstars.cashtracker.presenter.callbacks.DatabaseCallback;
import liquidstars.cashtracker.view.interfaces.IAddView;

public class AddPresenter {
    private IAddView view;
    private AddModel model;

    public AddPresenter(AddModel model) {
        this.model = model;
    }

    public void attachView(IAddView view){this.view = view;}

    public void setUpSpinner(){
        model.getAccounts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Account>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Account> accounts) {
                        if(accounts!=null)
                            view.setUpSpinner(accounts);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void showCategoriesDialog(boolean type){
        model.getCategories(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        categories.add(null);
                        view.showCategoryDialog(categories);
                    }
                });
    }

    public void setUpCategoryButton(boolean type){
        model.getFirstCategory(type, new DatabaseCallback() {
            @Override
            public void onComplete(Category category) {
                view.setUpCategoryButton(category);
            }
        });
    }
}
