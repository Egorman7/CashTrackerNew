package liquidstars.cashtracker.view.interfaces;

import java.util.List;

import liquidstars.cashtracker.model.database.models.Category;

public interface ICategoriesView {
    void updateAdapter(List<Category> categories);
    void performItemClick(Category category, int pos);
    void showToast(int id);
}
