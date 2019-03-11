package liquidstars.cashtracker.presenter;

import java.util.ArrayList;
import java.util.List;

import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.AccountsModel;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.presenter.callbacks.DatabaseCallback;
import liquidstars.cashtracker.view.AccountsActivity;
import liquidstars.cashtracker.view.interfaces.IAccountsView;

public class AccountsPresenter {
    private IAccountsView view;
    private AccountsModel model;

    public AccountsPresenter(IAccountsView view, AccountsModel model) {
        this.view = view;
        this.model = model;
    }

    public void loadData(){
        model.loadData(new DatabaseCallback() {
            @Override
            public void onCompleteList(List<?> list) {
                List<Account> accounts = new ArrayList<>();
                for(Object obj : list) accounts.add((Account)obj);
                view.updateAdapter(accounts);
            }
        });
    }

    public void insertAccount(Account account){
        model.insertAccount(account, new DatabaseCallback() {
            @Override
            public void onComplete() {
                view.showToast(R.string.toast_account_created);
            }
        });
    }

    public void updateAccount(Account account){
        model.updateAccount(account, new DatabaseCallback() {
            @Override
            public void onComplete() {
                view.showToast(R.string.toast_account_edited);
            }
        });
    }

    public void showEditDialog(Account account){
        view.showCreateEditDialog(AccountsActivity.TASK_EDIT, account);
    }
}
