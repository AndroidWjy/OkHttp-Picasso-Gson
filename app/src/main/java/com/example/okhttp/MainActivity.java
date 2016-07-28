package com.example.okhttp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.okhttp.Bean.Data;
import com.example.okhttp.Bean.Item;
import com.example.okhttp.Manager.OkHttpManager;
import com.example.okhttp.Manager.PicassoManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private MyAdapter adapter;
    private MyAdapter1 adapter1;
    private final static String URL = "http://litchiapi.jstv.com/api/GetFeeds?column=17&PageSize=20&pageIndex=1&val=AD908EDAB9C3ED111A58AF86542CCF50";
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView = (ListView) findViewById(R.id.lv);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                final Picasso pi = Picasso.with(MainActivity.this);
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //空闲时才刷新界面
                    pi.resumeTag(MainActivity.this);
                } else {
                    pi.pauseTag(MainActivity.this);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

//        MyTask task = new MyTask();
//        task.execute("http://litchiapi.jstv.com/api/GetFeeds?column=17&PageSize=20&pageIndex=1&val=AD908EDAB9C3ED111A58AF86542CCF50");
        getNewsList();
    }

    public void getNewsList() {

        OkHttpManager.getInstance().asyncGetJsonString(URL, new OkHttpManager.Func() {
            @Override
            public void onResponse(String result) {
                ArrayList<Data.ItemDataDetail> list = new ArrayList<>();
                Gson gson = new Gson();
                Data datas = gson.fromJson(result, Data.class);
                Log.d(TAG, datas.toString());
                ArrayList<Data.ItemData> feeds = datas.paramz.feeds;
                for (int i = 0; i < feeds.size(); i++) {
                    Data.ItemDataDetail data = feeds.get(i).data;
                    list.add(data);
                }
                adapter1 = new MyAdapter1(list);
                listView.setAdapter(adapter1);

            }
        });
    }

    //不使用框架
    class MyAdapter extends BaseAdapter {
        List<Item> data;

        public MyAdapter(List<Item> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Item getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_list_view, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
                holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv1.setText(getItem(position).getSubject());
            holder.tv2.setText(getItem(position).getSummary());
            PicassoManager.loadImageWithSize(MainActivity.this, "http://litchiapi.jstv.com" + getItem(position).getCover(),
                    300, 300, holder.imageView);

            return convertView;
        }
    }

    class ViewHolder {
        private TextView tv1;
        private TextView tv2;
        private ImageView imageView;
    }

    //不使用框架进行解析
    class MyTask extends AsyncTask<String, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(String... params) {
            List<Item> list = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200) {
                    InputStream inputStream = conn.getInputStream();
                    String json = readFromStream(inputStream);
                    //将字符串转变为json对象，然后转为json数组
                    JSONArray array = new JSONObject(json).getJSONObject("paramz").getJSONArray("feeds");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i).getJSONObject("data");
                        Item item = new Item();
                        item.setSummary(obj.getString("summary"));
                        item.setCover(obj.getString("cover"));
                        item.setSubject(obj.getString("subject"));
                        list.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            super.onPostExecute(items);
            adapter = new MyAdapter(items);
            listView.setAdapter(adapter);
        }
    }

    class MyAdapter1 extends BaseAdapter {
        List<Data.ItemDataDetail> data;

        public MyAdapter1(List<Data.ItemDataDetail> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Data.ItemDataDetail getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_list_view, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
                holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv1.setText(getItem(position).subject);
            holder.tv2.setText(getItem(position).summary);
            PicassoManager.loadImageWithSize(MainActivity.this, "http://litchiapi.jstv.com" + getItem(position).cover,
                    300, 300, holder.imageView);

            return convertView;
        }
    }

    /**
     * 将流变化为字符串
     *
     * @param in
     * @return
     * @throws IOException
     */
    public String readFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bt = new byte[1024];
        int len;
        while ((len = in.read(bt)) != -1) {
            bos.write(bt, 0, len);
        }

        return bos.toString();
    }

}
