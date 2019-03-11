package liquidstars.cashtracker.view;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.MainModel;
import liquidstars.cashtracker.model.database.Database;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.model.database.models.Record;
import liquidstars.cashtracker.presenter.MainPresenter;
import liquidstars.cashtracker.view.adapters.AccountDialogListAdapter;
import liquidstars.cashtracker.view.adapters.MainRecordsAdapter;
import liquidstars.cashtracker.view.interfaces.IMainView;
import liquidstars.cashtracker.view.util.SettingsHelper;

public class MainActivity extends AppCompatActivity implements IMainView {

    public static final int ACTIVITY_ADD = 1, ACTIVITY_JOURNAL = 2, ACTIVITY_CATEGORIES = 3, ACTIVITY_ACCOUNTS = 4, ACTIVITY_STATISTICS = 5, ACTIVITY_SETTINGS = 6,
        REQUEST_ADD = 1, REQUEST_JOURNAL = 2, REQUEST_CATEGORIES = 3, REQUEST_ACCOUNTS = 4, REQUEST_SETTINGS = 5,
        RESPONSE_SOMETHING_CHANGED = 50;
    public static final String ACCOUNT = "account", TARGET = "target", // true - create, false - edit
        DATA_ADD_VALUE = "value", DATA_ADD_DATE = "date", DATA_ADD_ACCOUNT = "account_id", DATA_ADD_CATEGORY = "category", DATA_ADD_TYPE = "type", DATA_ADD_DESC = "desc";

    private long account_id;
    private String currency = "";
    private boolean type = false;

    private MainPresenter mPresenter;
    private MainRecordsAdapter mAdapter;
    @BindView(R.id.recyclerView) RecyclerView mRecycler;
    @BindView(R.id.activity_main_fab) FloatingActionButton mFab;
    @BindView(R.id.main_account_title) TextView mAccountTitle;
    @BindView(R.id.activity_main_navigation) NavigationView mNavigation;
    @BindView(R.id.activity_main_diagram) PieChart mDiagram;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        account_id = 1;

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mPresenter = new MainPresenter(new MainModel(Database.getInstance(this)));
        mPresenter.attachView(this);
        if(!SettingsHelper.checkBoolean(this, SettingsHelper.SETTING_NOT_FIRST_LAUNCH)) {
            mPresenter.prePopulate();
            SettingsHelper.putBoolean(this, SettingsHelper.SETTING_NOT_FIRST_LAUNCH, true);
        }
        else mPresenter.loadFirstAccount();

        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_journal:
                        mPresenter.startJournalActivity();
                        break;
                    case R.id.nav_categories:
                        mPresenter.startCategoriesActivity();
                        break;
                    case R.id.nav_accounts:
                        mPresenter.startAccountsActivity();
                        break;
                    case R.id.nav_statistic:
                        mPresenter.startStatisticsActivity();
                        break;
                    case R.id.nav_settings:
                        mPresenter.startSettingsActivity();
                        break;
                }
                //showToast(item.getTitle().toString());
                return true;
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.startAddActivity();
            }
        });
    }

    // menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_action_accounts) {
            mPresenter.showAccountDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // work with activities

    @SuppressLint("RestrictedApi")
    @Override
    public void startActivity(int activityId) {
        mDrawer.closeDrawers();
        Intent intent;
        switch (activityId){
            case ACTIVITY_ADD:
                Bundle b = ActivityOptions.makeScaleUpAnimation(mFab, (int)mFab.getPivotX(), (int)mFab.getPivotY(), mFab.getWidth(), mFab.getHeight()).toBundle();
                intent = new Intent(this, AddActivity.class);
                intent.putExtra(ACCOUNT, account_id);
                intent.putExtra(TARGET, true);
                startActivityForResult(intent, REQUEST_ADD, b);
                break;
            case ACTIVITY_JOURNAL:
                intent = new Intent(this, JournalActivity.class);
                startActivity(intent);
                break;
            case ACTIVITY_CATEGORIES:
                intent = new Intent(this, CategoriesActivity.class);
                startActivity(intent);
                break;
            case ACTIVITY_ACCOUNTS:
                intent = new Intent(this, AccountsActivity.class);
                startActivity(intent);
                break;
            case ACTIVITY_STATISTICS:
                showToast("Pooph!");
                break;
            case ACTIVITY_SETTINGS:
                showToast("Pooph!");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ADD:
                switch (resultCode) {
                    case AddActivity.RESULT_CREATE:
                        mPresenter.addRecord(new Record(data.getLongExtra(DATA_ADD_ACCOUNT, 1),
                            data.getDoubleExtra(DATA_ADD_VALUE, 0.0),
                            data.getStringExtra(DATA_ADD_DATE),
                            data.getLongExtra(DATA_ADD_CATEGORY, 1),
                            data.getBooleanExtra(DATA_ADD_TYPE, true),
                            data.getStringExtra(DATA_ADD_DESC)));
                        break;
                    case AddActivity.RESULT_EDIT:
                        // update data
                        break;
                    case AddActivity.RESULT_BACK:
                        break;
                }
                break;
            case REQUEST_JOURNAL:
                if(resultCode == RESPONSE_SOMETHING_CHANGED){
                    // do something if journal changed
                }
                break;
            case REQUEST_CATEGORIES:
                if(resultCode == RESPONSE_SOMETHING_CHANGED){
                    // do something if categories changed
                }
                break;
            case REQUEST_ACCOUNTS:
                if(resultCode == RESPONSE_SOMETHING_CHANGED){
                    // do something if accounts changed
                }
                break;
            case REQUEST_SETTINGS:
                if(resultCode == RESPONSE_SOMETHING_CHANGED){
                    // do something if settings changed
                }
                break;
        }
    }

    // dialogs

    @Override
    public void showAccountsDialog(final List<Account> accounts) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.activity_main_dialog_accounts_title)
                .setAdapter(new AccountDialogListAdapter(this, R.layout.item_list_dialog_account, accounts),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(accounts.get(i)!=null){
                                    mPresenter.loadAccountDataById(accounts.get(i).get_id());
                                } else showCreateAccountDialog();
                            }
                        })
                .show();
    }

    private void showCreateAccountDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_account_create, null, false);
        final EditText title = view.findViewById(R.id.dialog_account_create_input_title);
        final EditText cur = view.findViewById(R.id.dialog_account_create_input_currency);
        final Button button = view.findViewById(R.id.dialog_account_create_type);
        type = true;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = !type;
                button.setCompoundDrawablesRelativeWithIntrinsicBounds(type ? R.drawable.icon_card : R.drawable.icon_cash, 0,0,0);
                button.setText(type ? R.string.activity_main_dialog_account_create_button_type_card : R.string.activity_main_dialog_account_create_button_type_cash);
            }
        });
        new AlertDialog.Builder(this)
                .setTitle(R.string.activity_main_dialog_account_create_title)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.activity_main_dialog_account_create_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!title.getText().toString().isEmpty() && !cur.getText().toString().isEmpty()){
                            Account account = new Account(title.getText().toString(), cur.getText().toString(), type, 0);
                            mPresenter.createAccount(account);
                        }
                        showToast(R.string.activity_main_string_account_error);
                    }
                })
                .setNegativeButton(R.string.text_cancel, null).show();
    }

    // adapter and data

    @Override
    public void addRecord() {
        mAdapter.refresh();
    }

    @Override
    public void loadRecords(List<Record> records) {
        if (records.size()==0) {mAdapter.setEnd(); Log.d("JOURNAL", "End of list");}
        else {mAdapter.addData(records); Log.d("JOURNAL", "Adding records...");}
    }

    @Override
    public void setCurrency(String s) {
        currency = s;
        mAdapter = new MainRecordsAdapter(mRecycler, mPresenter, this, " " + currency, account_id);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void setTitle(String s) {
        mAccountTitle.setText(getString(R.string.activity_main_account_title, s));
        //mPresenter.setUpDiagram(account_id, DateHelper.getCurrentDateString(), DateHelper.getCurrentDateString());
    }

    @Override
    public void setUpDiagram(PieDataSet dataSet) {
        mDiagram.setData(new PieData(dataSet));
        mDiagram.setDrawCenterText(true);
    }

    @Override
    public void setAccountId(long id) {
        account_id = id;
    }

    // utils

    @Override
    public String getStringResource(int id) {
        return getResources().getString(id);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}
