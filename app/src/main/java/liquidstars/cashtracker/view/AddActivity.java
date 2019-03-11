package liquidstars.cashtracker.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.AddModel;
import liquidstars.cashtracker.model.database.Database;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.model.database.models.Category;
import liquidstars.cashtracker.presenter.AddPresenter;
import liquidstars.cashtracker.view.adapters.AccountSpinnerAdapter;
import liquidstars.cashtracker.view.adapters.CategoryDialogListAdapter;
import liquidstars.cashtracker.view.interfaces.IAddView;
import liquidstars.cashtracker.view.util.DateHelper;

public class AddActivity extends AppCompatActivity implements IAddView {

    public static final int RESULT_BACK = 1, RESULT_CREATE = 2, RESULT_EDIT = 3;

    private long account_id;

    private AddPresenter mPresenter;
    private Category mCurrentCategory;
    @BindView(R.id.activity_add_spinner_account) Spinner mSpinner;
    @BindView(R.id.activity_add_value) EditText mValue;
    @BindView(R.id.activity_date_value) EditText mDate;
    @BindView(R.id.activity_add_description) EditText mDesc;
    @BindView(R.id.activity_add_radio_income) RadioButton mIncome;
    @BindView(R.id.activity_add_button_category) Button mCategoryButton;
    @BindView(R.id.activity_add_create_button) Button mCreateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        account_id = getIntent().getLongExtra(MainActivity.ACCOUNT, 1);

        mPresenter = new AddPresenter(new AddModel(Database.getInstance(this)));
        mPresenter.attachView(this);
        mDate.setText(DateHelper.getCurrentDateString());
        mPresenter.setUpSpinner();
        mPresenter.setUpCategoryButton(true);

        mIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPresenter.setUpCategoryButton(compoundButton.isChecked());
            }
        });
        mCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.showCategoriesDialog(mIncome.isChecked());
            }
        });
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(MainActivity.DATA_ADD_ACCOUNT, account_id);
                data.putExtra(MainActivity.DATA_ADD_VALUE, Double.valueOf(mValue.getText().toString()));
                data.putExtra(MainActivity.DATA_ADD_DATE, mDate.getText().toString());
                data.putExtra(MainActivity.DATA_ADD_CATEGORY, mCurrentCategory.get_id());
                data.putExtra(MainActivity.DATA_ADD_TYPE, mIncome.isChecked());
                data.putExtra(MainActivity.DATA_ADD_DESC, mDesc.getText().toString());
                setResult(getIntent().getBooleanExtra(MainActivity.TARGET, true) ? RESULT_CREATE : RESULT_EDIT, data);
                finish();
            }
        });
    }

    @Override
    public void setUpSpinner(final List<Account> accounts) {
        mSpinner.setAdapter(new AccountSpinnerAdapter(this, R.layout.item_spinner_account, accounts));
        int pos = 0;
        for(int i=0; i<accounts.size(); i++){
            if(accounts.get(i).get_id() == account_id){
                pos = i;
                break;
            }
        }
        mSpinner.setSelection(pos);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                account_id = accounts.get(i).get_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCategoryDialog(final List<Category> categories) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.activity_add_dialog_category_title)
                .setAdapter(new CategoryDialogListAdapter(this, R.layout.item_list_dialog_category, categories),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //showToast(categories.get(i).getTitle());
                                if(categories.get(i) == null) return;
                                mCurrentCategory = categories.get(i);
                                setUpCategoryButton(categories.get(i));
                            }
                        })
                .create().show();
    }

    @Override
    public void setUpCategoryButton(Category category) {
        mCurrentCategory = category;
        mCategoryButton.setText(category.getTitle());
        mCategoryButton.setBackgroundColor(category.getColor());
    }

    @Override
    public void updateCategoryButton() {

    }

    @Override
    public void showDatePickerDialog() {

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_BACK);
        super.onBackPressed();
    }
}
