package liquidstars.cashtracker.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.database.models.Account;

public class AccountSpinnerAdapter extends ArrayAdapter<Account> {
    private LayoutInflater inflater;

    public AccountSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Account> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int pos, View view, ViewGroup parent){
        View accountView = inflater.inflate(R.layout.item_spinner_account, parent, false);
        Account account = getItem(pos);

        ImageView image = accountView.findViewById(R.id.item_spinner_account_image);
        TextView title = accountView.findViewById(R.id.item_spinner_account_title);
        image.setImageResource(account.isType() ? R.drawable.icon_card : R.drawable.icon_cash);
        title.setText(account.getTitle());

        return accountView;
    }
}
