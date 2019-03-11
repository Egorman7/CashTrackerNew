package liquidstars.cashtracker.view.interfaces;

import java.util.Date;
import java.util.List;

import liquidstars.cashtracker.model.database.models.Record;

public interface IJournalView {
    void showDatePickerDialog(int target, Date date);
    void updateAdapter(List<Record> records, String currency);
    void setAccountId(long id);
    void showToast(String text);
    void showToast(int id);
}
