package liang.peng.fei.com.v2ex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.StringCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import liang.peng.fei.com.v2ex.adapter.BoardAdapter;
import liang.peng.fei.com.v2ex.domain.News;
import liang.peng.fei.com.v2ex.ui.BoardActivity;


public class MainActivity extends Activity {

    public String path;
    List<News> list = new ArrayList<News>();
    String [] titles = new String[44];
    String [] links = new String[44];
    String [] times = new String[44];
    String [] authors = new String[44];
    String [] imgsouter = new String[44];
    private LayoutInflater layoutInflater;
    private ImageView touxiang;

    @InjectView(R.id.lv_board)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.board_list_item,null);
        touxiang = (ImageView) view.findViewById(R.id.img_board);
        touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"test MainActivity ImageView",Toast.LENGTH_LONG).show();
            }
        });
        KJHttp kjHttp = new KJHttp();
        kjHttp.urlGet("http://v2ex.com/?tab=tech", new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(MainActivity.this, "成功连接", Toast.LENGTH_LONG).show();
                path = s;
                //仅仅是得到了一个document对象，现在进行进一步的处理
                Document document = Jsoup.parse(path);
                Element body = document.body();
                Node wrapper = body.childNode(3);
                Node content = wrapper.childNode(1);
                Node box = content.childNode(1);
                List<Node> cellitem = box.childNodes();
                List<Node> divs =new ArrayList<Node>();
                for (int i =0 ;i<box.childNodeSize();i++){
                    if (i%2!=0){
                        divs.add(cellitem.get(i));
                    }
                }
                for (int j = 1 ;j<41;j++){
                    News news = new News();
                    Node table = divs.get(j).childNode(1);
                    Node tbody = table.childNode(1);
                    Node tr = tbody.childNode(0);


                    Node td = tr.childNode(5);

                    Node smallfade = td.childNode(8);
                    String time = smallfade.childNode(0).attr("text");
                    news.setTime(time);
                    times[j-1] = time;

//                    String author = smallfade.childNode(1).childNode(0).childNode(0).attr("text");
//                    authors[j-1] = author;

                    Node img = tr.childNode(1).childNode(0).childNode(0);
                    Node span = td.childNode(4);
                    Node a = span.childNode(0);
                    Node text = a.childNode(0);
                    String title = text.attr("text");
                    news.setTitle(title);
                    titles[j-1] = title;
                    String link = a.attr("href");
                    links[j-1] = "v2ex.com"+link;
                    String imgs []= img.attr("src").split("//");
                    news.setImg("http://"+imgs[1]);
                    imgsouter[j-1]="http://"+imgs[1];

                    list.add(news);
                }
                listView.setAdapter(new BoardAdapter(MainActivity.this,list));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(MainActivity.this,imgsouter[i],Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                        intent.putExtra("article_path",links[i]);
                        intent.putExtra("article_title",titles[i]);
                        intent.putExtra("article_time",times[i]);
                        intent.putExtra("article_imgpath",imgsouter[i]);
                       // intent.putExtra("article_author",authors[i]);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
