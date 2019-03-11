package liquidstars.cashtracker.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.CategoriesModel;
import liquidstars.cashtracker.model.database.models.Category;
import liquidstars.cashtracker.presenter.callbacks.DatabaseCallback;
import liquidstars.cashtracker.view.interfaces.ICategoriesView;

public class CategoriesPresenter {
    private ICategoriesView view;
    private CategoriesModel model;

    public CategoriesPresenter(ICategoriesView view, CategoriesModel model) {
        this.view = view;
        this.model = model;
    }

    public void loadCategories(boolean type){
        model.loadCategoriesByType(type, new DatabaseCallback() {
            @Override
            public void onCompleteList(List<?> list) {
                List<Category> categories = new ArrayList<>();
                for(Object obj : list){
                    categories.add((Category)obj);
                }
                view.updateAdapter(categories);
            }
        });
    }

    public void doUpdate(Category category){
        model.doUpdate(category, new DatabaseCallback() {
            @Override
            public void onComplete() {
                view.showToast(R.string.toast_category_edited);
            }

            @Override
            public void onError() {
                Log.e("CATEGORY_UPDATE", "Error!");
            }
        });
    }

    public void doInsert(Category category){
        model.doInsert(category, new DatabaseCallback() {
            @Override
            public void onComplete() {
                view.showToast(R.string.toast_category_added);
            }

            @Override
            public void onError() {
                Log.e("CATEGORY_INSERT", "Error!");
            }
        });
    }

    public void onListItemClick(Category category, int pos){
        view.performItemClick(category, pos);
    }
}
