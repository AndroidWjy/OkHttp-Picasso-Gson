package com.example.okhttp.Bean;

import java.util.ArrayList;

/**
 * 与Gson转换对象一致
 * Created by Administrator on 2016/7/27.
 */
public class Data {
    public DataCount paramz;

    public class DataCount {
        public ArrayList<ItemData> feeds;

        @Override
        public String toString() {
            return "DataCount{" +
                    "feeds=" + feeds +
                    '}';
        }
    }

    public class ItemData {
        public ItemDataDetail data;

        @Override
        public String toString() {
            return "ItemData{" +
                    "data=" + data +
                    '}';
        }
    }

    public class ItemDataDetail {
        public String cover;
        public String subject;
        public String summary;

        @Override
        public String toString() {
            return "ItemDataDetail{" +
                    "cover='" + cover + '\'' +
                    ", subject='" + subject + '\'' +
                    ", summary='" + summary + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Data{" +
                "paramz=" + paramz +
                '}';
    }
}
