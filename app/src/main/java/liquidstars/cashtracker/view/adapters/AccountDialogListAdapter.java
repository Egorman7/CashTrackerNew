package liquidstars.cashtracker.view.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

public class AccountDialogListAdapter extends ArrayAdapter<Account> {
    private LayoutInflater inflater;

    public AccountDialogListAdapter(@NonNull Context context, int resource, @NonNull List<Account> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getAccountView(position, convertView, parent);
    }

    private View getAccountView(int pos, View view, ViewGroup parent){
        if(getItem(pos) == null) return inflater.inflate(R.layout.item_list_dialog_account_add, parent, false);
        View v = inflater.inflate(R.layout.item_list_dialog_account, parent, false);
        TextView title = v.findViewById(R.id.item_list_dialog_account_title);
        ImageView image = v.findViewById(R.id.item_list_dialog_account_image);
        title.setText(getItem(pos).getTitle());
        image.setImageResource(getItem(pos).isType() ? R.drawable.icon_card : R.drawable.icon_cash);
        return v;
    }
}
