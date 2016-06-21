package darkhost.onamhighsch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 민재 on 2016-04-02.
 */
public class SchAdapter extends RecyclerView.Adapter<SchAdapter.ViewHolder> {
    Context context;
    List<SchItem> items;
    int item_layout;

    public SchAdapter(Context context, List<SchItem> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.schlist, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SchItem item=items.get(position);
        holder.Date.setText(Integer.toString(item.getDate()));
        holder.Sch.setText(item.getSch());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Date;
        TextView Sch;

        public ViewHolder(View itemView) {
            super(itemView);
            Date= (TextView)itemView.findViewById(R.id.day);
            Sch= (TextView)itemView.findViewById(R.id.schedule);
        }
    }
}
