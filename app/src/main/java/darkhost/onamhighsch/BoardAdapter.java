package darkhost.onamhighsch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by 민재 on 2016-10-09.
 */
public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    Context context;
    List<BoardItem> items;
    int item_layout;

    public BoardAdapter(Context context, List<BoardItem> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.boardlist, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BoardItem item=items.get(position);
        holder.Title.setText(item.getTitle());
        holder.Writer.setText(item.getWriter());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(item.getURL());
                Intent open = new Intent(Intent.ACTION_VIEW, uri);
                view.getContext().startActivity(open);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        TextView Writer;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            Title = (TextView)itemView.findViewById(R.id.title);
            Writer = (TextView)itemView.findViewById(R.id.writer);
            cardView = (CardView)itemView.findViewById(R.id.view);
        }
    }
}

class BoardItem {
    String Title, Writer, URL;

    String getTitle() {
        return this.Title;
    }
    String getWriter() {
        return this.Writer;
    }
    String getURL() { return this.URL; }

    BoardItem(String title, String writer, String url) {
        this.Title = title;
        this.Writer = writer;
        this.URL = url;
    }
}
