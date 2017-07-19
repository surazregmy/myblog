package com.example.suraz.myblog.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.suraz.myblog.R;

import java.util.List;

/**
 * Created by suraz on 7/16/17.
 */

public class PostListAdapter extends BaseAdapter {

    private Context context;
    private List<Post> posts;
    private int layoutId;

    public PostListAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
        layoutId = R.layout.custom_post_list;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return posts.get(i); //i is the position
    }

    @Override
    public long getItemId(int i) {
        return posts.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View result = convertView;
        if(result == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            result = inflater.inflate(layoutId, parent, false);
        }
        TextView title = (TextView) result.findViewById(R.id.title);
        TextView author = (TextView) result.findViewById(R.id.author);
        TextView content = (TextView) result.findViewById(R.id.content);
        title.setText(posts.get(i).getTitle());
        author.setText(posts.get(i).getAuthor());
        content.setText(posts.get(i).getContent()+" ");
        return result;
    }
}
