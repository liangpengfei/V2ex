package liang.peng.fei.com.v2ex.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.kymjs.aframe.bitmap.KJBitmap;

import java.util.List;

import liang.peng.fei.com.v2ex.R;
import liang.peng.fei.com.v2ex.domain.News;

/**
 * Created by fei on 21/10/14.
 */
public class BoardAdapter extends BaseAdapter {

    private List<News> data;
    private LayoutInflater inflator;
    private Context context;
    private ImageView img;
    private TextView title,node,author,time;
    News newsitem;

    public BoardAdapter(Context context, List<News> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        KJBitmap kjBitmap = KJBitmap.create();
            inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.board_list_item, null);
            newsitem = data.get(position);
//            node = (TextView) convertView.findViewById(R.id.tv_node);   //为了减少开销，则只在第一页时调用findViewById
//            node.setText(newsitem.getNode());
            title = (TextView) convertView.findViewById(R.id.tv_title);
            title.setText(newsitem.getTitle());
        img= (ImageView) convertView.findViewById(R.id.img_board);
        kjBitmap.display(img,newsitem.getImg(),100,100);
        if(img.getDrawable()==null){
            Toast.makeText(context,"null pic",Toast.LENGTH_LONG).show();
            img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.defaultimg));
        }
//            img = (ImageView) convertView.findViewById(R.id.img_board);
//            img.setImageBitmap(newsitem.getImg());
//            author = (TextView) convertView.findViewById(R.id.tv_author);
//            author.setText(newsitem.getAuthor());
            time = (TextView) convertView.findViewById(R.id.tv_time);
            time.setText(newsitem.getTime());
//        author = (TextView) convertView.findViewById(R.id.tv_author);
//        author.setText(newsitem.getLinks());
            return convertView;
    }
}
