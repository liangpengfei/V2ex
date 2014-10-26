package liang.peng.fei.com.v2ex.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.StringCallBack;

import butterknife.ButterKnife;
import butterknife.InjectView;
import liang.peng.fei.com.v2ex.R;

public class BoardActivity extends Activity {

    @InjectView(R.id.tv_board_author)TextView author;
    @InjectView(R.id.tv_board_time)TextView time;
    @InjectView(R.id.tv_board_title)TextView title;
    @InjectView(R.id.tv_board_content)TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        String timeString = intent.getStringExtra("article_time");
        String titleString = intent.getStringExtra("article_title");
        String path = intent.getStringExtra("article_path");
        String authorString = intent.getStringExtra("article_author");
        time.setText(timeString);
        title.setText(titleString);
        author.setText("liang");
        KJHttp kjHttp = new KJHttp();
        kjHttp.urlGet("http://"+path,new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                Document document = Jsoup.parse(s);
                Element element = document.select("div.topic_content").first();
                content.setText(element.text());
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(BoardActivity.this,"网络连接失败",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.board, menu);
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
