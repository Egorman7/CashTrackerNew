package liquidstars.cashtracker.view.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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
import liquidstars.cashtracker.model.database.models.Category;
import liquidstars.cashtracker.presenter.CategoriesPresenter;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private Context context;
    private CategoriesPresenter presenter;
    private List<Category> categories;
    private boolean type;

    public CategoriesAdapter(Context context, CategoriesPresenter presenter, List<Category> categories, boolean type) {
        this.context = context;
        this.presenter = presenter;
        this.categories = categories;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Category category = categories.get(position);
        final int pos = position;
        holder.title.setText(category.getTitle());
        Bitmap bitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(category.getColor());
        holder.color.setImageBitmap(bitmap);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onListItemClick(category, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_list_categories_card) CardView card;
        @BindView(R.id.item_list_categories_title) TextView title;
        @BindView(R.id.item_list_categories_color) ImageView color;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
