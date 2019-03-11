package liquidstars.cashtracker.presenter;

import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import liquidstars.cashtracker.model.JournalModel;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.model.database.models.Record;
import liquidstars.cashtracker.presenter.callbacks.DatabaseCallback;
import liquidstars.cashtracker.view.interfaces.IJournalView;
import liquidstars.cashtracker.view.util.DateHelper;

public class JournalPresenter {
    private IJournalView view;
    private JournalModel model;

    public JournalPresenter(IJournalView view, JournalModel model) {
        this.view = view;
        this.model = model;
    }

    // account

    public void loadFirstAccount(){
        model.loadFirstAccount(new DatabaseCallback() {
            @Override
            public void onError() {
                loadFirstAccount();
            }

            @Override
            public void onCompleteObject(Object obj) {
                loadAccount((Account)obj);
            }
        });
    }

    void loadAccount(Account account){
        loadRecords(account.get_id(), DateHelper.getDateSubDaysString(30), DateHelper.getCurrentDateString(), account.getCurrency());
        view.setAccountId(account.get_id());
    }

    public void loadAccountById(final long acc_id, final String from, final String to){
        model.loadCurrency(acc_id, new DatabaseCallback() {
            @Override
            public void onCompleteObject(Object obj) {
                loadRecords(acc_id, from, to, (String) obj);
            }
        });
    }

    public void loadRecords(long accountId, String dateFrom, String dateTo, final String currency){
        model.loadRecords(accountId, dateFrom, dateTo, new DatabaseCallback() {
            @Override
            public void onCompleteList(List<?> list) {
                Log.d("RECORDS_LOAD", "Records loaded " + list.size());
                List<Record> records = new ArrayList<>();
                for(Object obj : list){
                    records.add((Record)obj);
                }
                view.updateAdapter(records, currency);
            }
        });
    }

    public void loadCategoryToTextView(long cat_id, boolean type, final TextView view){
        model.loadCategoryTitle(cat_id, type, new DatabaseCallback() {
            @Override
            public void onCompleteObject(Object obj) {
                view.setText((String)obj);
            }
        });
    }

}
