package liquidstars.cashtracker.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.database.models.Account;
import liquidstars.cashtracker.presenter.AccountsPresenter;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {
    private Context context;
    private List<Account> accounts;
    private AccountsPresenter presenter;

    public AccountsAdapter(Context context, List<Account> accounts, AccountsPresenter presenter) {
        this.context = context;
        this.accounts = accounts;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_accounts, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Account account = accounts.get(position);
        holder.image.setImageResource(account.isType() ? R.drawable.icon_card : R.drawable.icon_cash);
        holder.title.setText(account.getTitle());
        holder.currency.setText(account.getCurrency());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showEditDialog(account);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_list_accounts_card) CardView card;
        @BindView(R.id.item_list_accounts_image) ImageView image;
        @BindView(R.id.item_list_accounts_title) TextView title;
        @BindView(R.id.item_list_accounts_currency) TextView currency;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
