package liquidstars.cashtracker.view.interfaces;

import java.util.List;

import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.model.database.models.Category;

public interface IAddView {
    void showToast(String text);
    void showCategoryDialog(List<Category> categories);
    void setUpCategoryButton(Category category);
    void updateCategoryButton();
    void showDatePickerDialog();
    void setUpSpinner(List<Account> accounts);
}
