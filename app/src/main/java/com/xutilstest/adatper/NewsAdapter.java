package com.xutilstest.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xutilstest.R;
import com.xutilstest.db.News;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements View.OnClickListener {

    private static List<News> newsList;
    private LayoutInflater inflater;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    static int pos = 0;
    private String imagePath;

    public NewsAdapter(Context context, List<News> newsList) {
        this.mContext = context;
        this.newsList = newsList;
        inflater = LayoutInflater.from(context);
    }

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Object data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = inflater.inflate(R.layout.item_card_layout, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        //将创建的View注册点击事件
        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // 给ViewHolder设置元素
        News news = newsList.get(i);
        viewHolder.tv_ctime.setText(news.getCtime());
        viewHolder.tv_title.setText(news.getTitle());
        viewHolder.tv_description.setText(news.getDescription());

        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(news.getNewsid());
        Picasso.with(mContext).load(news.getPicUrl()).into(viewHolder.iv_cover);

    }

    @Override
    public int getItemCount() {
        // 返回数据总数
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    // 重写的自定义ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_cover)
        ImageView iv_cover;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.tv_description)
        TextView tv_description;
        @Bind(R.id.tv_ctime)
        TextView tv_ctime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}