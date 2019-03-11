package liquidstars.cashtracker.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import liquidstars.cashtracker.R;
import liquidstars.cashtracker.model.database.models.Record;
import liquidstars.cashtracker.presenter.MainPresenter;

public class MainRecordsAdapter extends RecyclerView.Adapter<MainRecordsAdapter.ViewHolder> {
    private List<Record> records;
    private MainPresenter mPresenter;
    private Context context;
    private String currency;
    private long mLast, mStep;
    private long mAccountId;
    private boolean isLoading, isEnd;
    private int mVisibleThreshold;

    public MainRecordsAdapter(RecyclerView list, MainPresenter presenter, Context context, String currency, long accountId) {
        //this.records = records;
        mAccountId = accountId;
        records = new ArrayList<>();
        this.mPresenter = presenter;
        this.context = context;
        this.currency = currency;
        mLast = 0; mStep = 10;
        isLoading = false; isEnd = false;
        mVisibleThreshold = 3;
        loadData();

        final LinearLayoutManager llm = (LinearLayoutManager)list.getLayoutManager();
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int total = llm.getItemCount();
                int last = llm.findLastVisibleItemPosition();
                if(!isEnd && !isLoading && total <= last + mVisibleThreshold){
                    mPresenter.showRecords(mAccountId, mLast, mStep);
                    mLast+=mStep;
                    isLoading = true;
                    Log.d("LOADING", "Load data for list! " + mLast + " to " + (mLast+mStep));
                }
            }
        });
    }

    public long addData(List<Record> list){
        records.addAll(list);
        notifyItemRangeInserted(records.size(), list.size());
        isLoading = false;
        return records.size()-1;
    }

    public void loadData(){mPresenter.showRecords(mAccountId, mLast, mStep); mLast+=mStep;}

    public void refresh(){
        records.clear();
        //records = new ArrayList<>();
        notifyDataSetChanged();
        mLast = 0;
        loadData();
        isEnd=false;
    }

    public void setEnd(){isEnd = true;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_journal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Record record = records.get(position);
        holder.vhValue.setText((record.isType() ? "+":"-") + record.getValue() +" "+currency);
        holder.vhValue.setTextColor(record.isType() ? ContextCompat.getColor(context, R.color.colorIncome) : ContextCompat.getColor(context, R.color.colorOutcome));
        holder.vhDate.setText(record.getDate());
        mPresenter.setCategoryToTextView(record.isType(), record.getCategory_id(), holder.vhCategory);

//        holder.vhValue.setText((records.get(position).isType() ? "+" : "-") + records.get(position).getValue() + currency);
//        holder.vhValue.setTextColor(records.get(position).isType() ? ContextCompat.getColor(context, R.color.colorIncome) : ContextCompat.getColor(context, R.color.colorOutcome));
//        holder.vhDate.setText(records.get(position).getDate());
//        mPresenter.setCategoryToTextView(records.get(position).isType(), records.get(position).get_id(), holder.vhCategory);
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
