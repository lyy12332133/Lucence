package com.huawei.lucene;

import com.hankcs.lucene.HanLPAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhongren1 on 2017/11/23.
 */
public class AnalyzerUtils {
    public static List<String> getWords(String str, Analyzer analyzer){
        List<String> result = new ArrayList<String>();
        TokenStream stream = null;
        try {
            stream = analyzer.tokenStream("content", new StringReader(str));
            CharTermAttribute attr = stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            while(stream.incrementToken()){
                result.add(attr.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(stream != null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String str = "李昕泽最初于 2015 年 7 月 17 日，成立了洛阳崇才网络科技有限公司（以下简称崇才科技），这家公司目前有 300 多人，其特殊之处是“员工”均为 00 后，没有固定的办公场所，都分散在一个个崇才的 QQ";
        List<String> lists = AnalyzerUtils.getWords(str, new HanLPAnalyzer());
        for (String s : lists) {
            System.out.println(s);
        }
    }

}
