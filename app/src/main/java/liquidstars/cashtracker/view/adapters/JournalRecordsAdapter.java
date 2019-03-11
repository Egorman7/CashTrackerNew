package liquidstars.cashtracker.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.database.models.Record;
import liquidstars.cashtracker.presenter.JournalPresenter;

public class JournalRecordsAdapter extends RecyclerView.Adapter<JournalRecordsAdapter.ViewHolder> {

    private Context context;
    private JournalPresenter mPresenter;
    private List<Record> records;
    private String currency;

    public JournalRecordsAdapter(Context context, JournalPresenter mPresenter, List<Record> records, String currency) {
        this.context = context;
        this.mPresenter = mPresenter;
        this.records = records;
        this.currency = currency;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_journal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record record = records.get(position);
        holder.vhValue.setText((record.isType() ? "+":"-") + record.getValue() +" "+currency);
        holder.vhValue.setTextColor(record.isType() ? ContextCompat.getColor(context, R.color.colorIncome) : ContextCompat.getColor(context, R.color.colorOutcome));
        holder.vhDate.setText(record.getDate());
        mPresenter.loadCategoryToTextView(record.getCategory_id(), record.isType(), holder.vhCategory);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.card) CardView vhCard;
        @BindView(R.id.value) TextView vhValue;
        @BindView(R.id.date) TextView vhDate;
        @BindView(R.id.category) TextView vhCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
