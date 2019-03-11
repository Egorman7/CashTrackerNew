package liquidstars.cashtracker.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.AccountsModel;
import liquidstars.cashtracker.model.database.Database;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.presenter.AccountsPresenter;
import liquidstars.cashtracker.view.adapters.AccountsAdapter;
import liquidstars.cashtracker.view.interfaces.IAccountsView;

public class AccountsActivity extends AppCompatActivity implements IAccountsView {

    public static final int TASK_EDIT = 1, TASK_CREATE = 2;

    private boolean type;


    private AccountsPresenter mPresenter;
    private AccountsAdapter mAdapter;

    @BindView(R.id.activity_accounts_recycler) RecyclerView mRecycler;
    @BindView(R.id.activity_accounts_fab) FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mPresenter = new AccountsPresenter(this, new AccountsModel(Database.getInstance(this)));
        mPresenter.loadData();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateEditDialog(TASK_CREATE, null);
            }
        });
    }

    @Override
    public void showCreateEditDialog(final int task, final Account account) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_account_create, null, false);
        final EditText title = view.findViewById(R.id.dialog_account_create_input_title);
        final EditText currency = view.findViewById(R.id.dialog_account_create_input_currency);
        final Button typeButton = view.findViewById(R.id.dialog_account_create_type);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = !type;
                typeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(type ? R.drawable.icon_card : R.drawable.icon_cash, 0, 0, 0);
                typeButton.setText(type ? R.string.activity_main_dialog_account_create_button_type_card : R.string.activity_main_dialog_account_create_button_type_cash);
            }
        });
        type = true;
        typeButton.setText(R.string.activity_main_dialog_account_create_button_type_card);
        String titleString = "Create";
        if(task == TASK_EDIT){
            titleString = "Edit";
            type = account.isType();
            typeButton.setText(type ? R.string.activity_main_dialog_account_create_button_type_card : R.string.activity_main_dialog_account_create_button_type_cash);
            title.setText(account.getTitle());
            currency.setText(account.getCurrency());
        }
        new AlertDialog.Builder(this)
                .setTitle(titleString)
                .setView(view)
                .setPositiveButton(titleString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(task == TASK_EDIT){
                            Account acc = new Account(account.get_id(), title.getText().toString(), currency.getText().toString(), type, account.getValue());
                            mPresenter.updateAccount(acc);
                        } else {
                            Account acc = new Account(title.getText().toString(), currency.getText().toString(), type, 0.0);
                            mPresenter.insertAccount(acc);
                        }
                    }
                })
                .show();

    }

    @Override
    public void updateAdapter(List<Account> accounts) {
        mAdapter = new AccountsAdapter(this, accounts, mPresenter);
        if(mRecycler.getAdapter() == null){
            mRecycler.setLayoutManager(new LinearLayoutManager(this));
            mRecycler.setAdapter(mAdapter);
        } else mRecycler.swapAdapter(mAdapter, true);
    }

    @Override
    public void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
