package news.huoren.com.asynctask;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 13615 on 2016/9/25.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<NewsBean> mList;
    private LayoutInflater mInflater;


    public MyAdapter(Context context, List<NewsBean> data) {
        mList = data;
        //mInflater = LayoutInflater.from(context);

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, null);
        ViewHolder vh = new ViewHolder(view);

        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imgUrl.setImageResource(R.mipmap.ic_launcher);
        new ImageLoader().showImageByThread(holder.imgUrl, mList.get(position).newIconUrl);
        String url = mList.get(position).newIconUrl;
        holder.imgUrl.setTag(url);
        holder.title.setText(mList.get(position).newsTile);
        holder.content.setText(mList.get(position).newContent);
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgUrl;
        private TextView title;
        private TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            imgUrl = (ImageView) itemView.findViewById(R.id.newicon);
            title = (TextView) itemView.findViewById(R.id.Title);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }


}
