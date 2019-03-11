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
import liquidstars.cashtracker.model.database.models.Category;

public class CategoryDialogListAdapter extends ArrayAdapter<Category> {
    private LayoutInflater inflater;

    public CategoryDialogListAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCategoryView(position,convertView,parent);
    }

    private View getCategoryView(int position, View view, ViewGroup parent){
        if(getItem(position) == null) {
            return inflater.inflate(R.layout.item_list_dialog_category_add, parent, false);
        }
        View categoryView = inflater.inflate(R.layout.item_list_dialog_category, parent, false);
        ImageView color = categoryView.findViewById(R.id.item_list_dialog_category_color);
        TextView title = categoryView.findViewById(R.id.item_list_dialog_category_title);
        title.setText(getItem(position).getTitle());
        color.setBackgroundColor(getItem(position).getColor());
        return categoryView;
    }
}
