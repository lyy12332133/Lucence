package com.huawei.lucene;

/**
 * Created by dllo on 17/11/24.
 */
public class MainTest {

    public static void main(String[] args) {
        Index index = new Index();
        index.index();

        Serach serach = new Serach();

        serach.search("景甜");
    }


}
