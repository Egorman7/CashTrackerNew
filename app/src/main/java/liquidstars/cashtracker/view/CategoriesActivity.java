package liquidstars.cashtracker.view;

import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.CategoriesModel;
import liquidstars.cashtracker.model.database.Database;
import liquidstars.cashtracker.model.database.models.Category;
import liquidstars.cashtracker.presenter.CategoriesPresenter;
import liquidstars.cashtracker.view.adapters.CategoriesAdapter;
import liquidstars.cashtracker.view.interfaces.ICategoriesView;

public class CategoriesActivity extends AppCompatActivity implements ICategoriesView {

    private static final int TASK_CREATE = 1, TASK_EDIT = 2;

    private CategoriesAdapter mAdapter;
    private CategoriesPresenter mPresenter;
    private boolean isIncome = true;

    @BindView(R.id.categories_activity_recycler) RecyclerView mRecycler;
    @BindView(R.id.categories_activity_fab) FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mPresenter = new CategoriesPresenter(this, new CategoriesModel(Database.getInstance(this)));
        mPresenter.loadCategories(isIncome);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateEditDialog(TASK_CREATE, null, -1);
            }
        });
    }

    @Override
    public void updateAdapter(List<Category> categories) {
        mAdapter = new CategoriesAdapter(this, mPresenter, categories, isIncome);
        if(mRecycler.getAdapter() == null){
            mRecycler.setLayoutManager(new LinearLayoutManager(this));
            mRecycler.setAdapter(mAdapter);
        } else mRecycler.swapAdapter(mAdapter, true);
    }

    @Override
    public void performItemClick(Category category, int pos) {
        showCreateEditDialog(TASK_EDIT, category, pos);
    }

    void showCreateEditDialog(final int task, final Category category, int pos){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_category_create_edit, null, false);
        final EditText title = view.findViewById(R.id.dialog_category_create_input_title);
        final RadioButton isIncome = view.findViewById(R.id.dialog_category_create_radio_income);
        RadioButton isOutcome = view.findViewById(R.id.dialog_category_create_radio_outcome);
        // add color
        String titleString = "Create";
        if(task == TASK_EDIT){
            titleString = "Edit";
            title.setText(category.getTitle());
            if(category.isType()) isIncome.setChecked(true); else isOutcome.setChecked(true);
            // load color
        } else isIncome.setChecked(true);
        new AlertDialog.Builder(this)
                .setTitle(titleString)
                .setView(view)
                .setPositiveButton(titleString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Category cat;
                        if(task == TASK_EDIT) {
                            cat = new Category(category.get_id(), title.getText().toString(), isIncome.isChecked(), category.getColor());
                            mPresenter.doUpdate(cat);
                        }
                        else {
                            cat = new Category(title.getText().toString(), isIncome.isChecked(), Color.parseColor("#555"));
                            mPresenter.doInsert(cat);
                        }
                    }
                })
                .show();
    }

    @Override
    public void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
