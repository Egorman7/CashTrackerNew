package liquidstars.cashtracker.presenter;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.MainModel;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.model.database.models.Record;
import liquidstars.cashtracker.presenter.callbacks.DatabaseCallback;
import liquidstars.cashtracker.view.MainActivity;
import liquidstars.cashtracker.view.interfaces.IMainView;
import liquidstars.cashtracker.view.util.DateHelper;

public class MainPresenter {

    private IMainView view;
    private MainModel model;

    public MainPresenter(MainModel model) {
        this.model = model;
    }
    public void attachView(IMainView view){this.view=view;}

    // pre-populating database and getting 1'st account, loading account

    public void prePopulate(){
        model.prePopulate(new DatabaseCallback() {
            @Override
            public void onError() {
                prePopulate();
            }

            @Override
            public void onComplete(long id) {
                loadFirstAccount();
            }
        });
    }

    public void loadFirstAccount(){
        model.getFirstAccount(new DatabaseCallback() {
            @Override
            public void onError() {
                loadFirstAccount();
            }

            @Override
            public void onCompleteObject(Object obj) {
                Account account = (Account)obj;
                loadAccountData(account);
            }
        });
    }

    public void loadAccountData(Account account){
        view.setAccountId(account.get_id());
        view.setTitle(account.getTitle());
        view.setCurrency(account.getCurrency());
        setUpDiagram(account.get_id(), DateHelper.getDateSubDaysString(30), DateHelper.getCurrentDateString());
    }

    public void loadAccountDataById(long id){
        view.setAccountId(id);
        setCurrency(id);
        setTitle(id);
        setUpDiagram(id, DateHelper.getDateSubDaysString(30), DateHelper.getCurrentDateString());
    }

    // work with adapter

    public void showRecords(long acc_id, long from, long limit){
        model.getRecords(acc_id, from, limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Record>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("LOAD_DATA", "Subscribed!");
                    }

                    @Override
                    public void onSuccess(List<Record> records) {
                        view.loadRecords(records);
                        Log.d("LOAD_DATA", "Subscribed! Got " + records.size() + " elements!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOAD_DATA", e.getMessage());
                    }
                });

//                .subscribe(new Consumer<List<Record>>() {
//                    @Override
//                    public void accept(List<Record> records) throws Exception {
//                        view.loadRecords(records);
//                    }
//                });
    }

    public void setCategoryToTextView(boolean type, long id, final TextView textView){
        model.getCategoryTitle(type, id, new DatabaseCallback() {
            @Override
            public void onCompleteObject(Object obj) {
                textView.setText((String)obj);
            }
        });
    }

    // adding data

    public void addRecord(Record record){
        model.addRecord(record, new DatabaseCallback() {
            @Override
            public void onComplete() {
                view.addRecord();
                view.showToast(R.string.toast_record_added);
            }
        });
    }

    public void createAccount(Account account){
        model.addAccount(account, new DatabaseCallback() {
            @Override
            public void onComplete() {
                view.showToast(R.string.activity_main_string_account_created);
            }
        });
    }

    // setting data

    public void setUpDiagram(long account_id, String date_s, String date_e){
        model.getRecordsInRange(account_id, date_s, date_e, new DatabaseCallback() {
            @Override
            public void onCompleteList(List<?> list) {
                Map<String, Double> data = new HashMap<>();
                data.put("Income", 0.0); data.put("Outcome", 0.0);
                //Pair<String, Double> income = new Pair<>("Income", 0.0), outcome = new Pair<>("Outcome", 0.0);
                for(Object obj : list){
                    Record record = (Record) obj;
                    String label = record.isType() ? "Income" : "Outcome";
                    data.put(label, data.get(label)+record.getValue());
                }
                List<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(data.get("Income").floatValue(), view.getStringResource(R.string.diagram_income)));
                entries.add(new PieEntry(data.get("Outcome").floatValue(), view.getStringResource(R.string.diagram_outcome)));
                PieDataSet dataSet = new PieDataSet(entries, "Incomes/Outcomes");
                dataSet.setColors(Color.parseColor(view.getStringResource(R.color.colorIncome)), Color.parseColor(view.getStringResource(R.color.colorOutcome)));
                view.setUpDiagram(dataSet);
            }
        });
    }

    public void setCurrency(long id){
        model.getCurrency(id).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        view.setCurrency(s);
                    }
                });
    }

    public void setTitle(long id){
        model.getTitle(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        view.setTitle(s);
                    }
                });
    }

    // dialogs

    public void showAccountDialog(){
        model.getAccounts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Account>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Account> accounts) {
                        accounts.add(null);
                        view.showAccountsDialog(accounts);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    // activities

    public void startAddActivity(){
        view.startActivity(MainActivity.ACTIVITY_ADD);
    }

    public void startJournalActivity(){
        view.startActivity(MainActivity.ACTIVITY_JOURNAL);
    }

    public void startCategoriesActivity(){
        view.startActivity(MainActivity.ACTIVITY_CATEGORIES);
    }

    public void startAccountsActivity(){
        view.startActivity(MainActivity.ACTIVITY_ACCOUNTS);
    }

    public void startStatisticsActivity(){
        view.startActivity(MainActivity.ACTIVITY_STATISTICS);
    }

    public void startSettingsActivity(){
        view.startActivity(MainActivity.ACTIVITY_SETTINGS);
    }

    // wtf is that shit


}
