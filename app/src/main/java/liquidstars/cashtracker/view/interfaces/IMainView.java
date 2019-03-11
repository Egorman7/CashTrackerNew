package liquidstars.cashtracker.view.interfaces;

import com.github.mikephil.charting.data.PieDataSet;

import java.util.List;

import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.model.database.models.Category;
import liquidstars.cashtracker.model.database.models.Record;

public interface IMainView {
    // account
    void setAccountId(long id);
    void setCurrency(String s);
    void setTitle(String s);
    // records & data
    void loadRecords(List<Record> records);
    void addRecord();
    void setUpDiagram(PieDataSet dataSet);
    // dialogs
    void showAccountsDialog(List<Account> accounts);
    // activities
    void startActivity(int activityId);
    // util (toast, res)
    void showToast(String text);
    void showToast(int resId);
    String getStringResource(int id);
}
