package liquidstars.cashtracker.view.interfaces;

import java.util.List;

import liquidstars.cashtracker.model.database.models.Account;

public interface IAccountsView {
    void showCreateEditDialog(int task, Account account);
    void updateAdapter(List<Account> accounts);
    void showToast(int id);
}
