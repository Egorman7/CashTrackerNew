package liquidstars.cashtracker.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.JournalModel;
import liquidstars.cashtracker.model.database.Database;
import liquidstars.cashtracker.model.database.models.Record;
import liquidstars.cashtracker.presenter.JournalPresenter;
import liquidstars.cashtracker.view.adapters.JournalRecordsAdapter;
import liquidstars.cashtracker.view.interfaces.IJournalView;
import liquidstars.cashtracker.view.util.DateHelper;

public class JournalActivity extends AppCompatActivity implements IJournalView {

    public final static int TARGET_DATE_FROM = 1, TARGET_DATE_TO = 2;

    private Date mDateFrom, mDateTo;
    private JournalPresenter mPresenter;
    private JournalRecordsAdapter mAdapter;
    private long mAccountId;
    private String currency;

    @BindView(R.id.activity_journal_recycler) RecyclerView mRecycler;
    @BindView(R.id.activity_journal_button_from) Button mButtonFrom;
    @BindView(R.id.activity_journal_button_to) Button mButtonTo;
    @BindView(R.id.activity_journal_fab) FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mDateFrom = new Date(); mDateTo = new Date();
        mDateFrom.setTime(DateHelper.getDateSubDays(30).getTime());
        mDateTo.setTime(DateHelper.getCurrentDate().getTime());

        mButtonFrom.setText(DateHelper.dateToString(mDateFrom));
        mButtonTo.setText(DateHelper.dateToString(mDateTo));

        mPresenter = new JournalPresenter(this, new JournalModel(Database.getInstance(this)));
        mPresenter.loadFirstAccount();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mButtonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(TARGET_DATE_FROM, mDateFrom);
            }
        });
        mButtonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(TARGET_DATE_TO, mDateTo);
            }
        });
    }

    public void updateDate(int target, Date date){
        switch (target){
            case TARGET_DATE_FROM:
                mDateFrom.setTime(date.getTime());
                mButtonFrom.setText(DateHelper.dateToString(mDateFrom));
                break;
            case TARGET_DATE_TO:
                mDateTo.setTime(date.getTime());
                mButtonTo.setText(DateHelper.dateToString(mDateTo));
                break;
        }
        Log.d("RECORDS", "Date updated. Loading records...");
        mPresenter.loadRecords(mAccountId, DateHelper.dateToString(mDateFrom), DateHelper.dateToString(mDateTo), currency);
    }


    @Override
    public void updateAdapter(List<Record> records, String currency) {
        this.currency=currency;
        mAdapter = new JournalRecordsAdapter(this, mPresenter, records, currency);
        if(mRecycler.getAdapter() == null) {
            Log.d("RECORDS", "No adapter");
            mRecycler.setLayoutManager(new LinearLayoutManager(this));
            mRecycler.setAdapter(mAdapter);
        } else {
            Log.d("RECORDS", "Swap adapter");
            mRecycler.swapAdapter(mAdapter, true);
        }
    }

    @Override
    public void showDatePickerDialog(final int target, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year); c.set(Calendar.MONTH, month); c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate(target, c.getTime());
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void setAccountId(long id) {
        mAccountId = id;
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
