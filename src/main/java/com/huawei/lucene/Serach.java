package com.huawei.lucene;

import com.hankcs.lucene.HanLPAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.StringReader;
import java.nio.file.FileSystems;

/**
 * Created by dllo on 17/11/24.
 */
public class Serach {

    // 搜索类
    public void search(String keyword) {

        Directory directory = null;
        // 读取索引文件的内容
        try {
            directory = FSDirectory.open(FileSystems.getDefault().getPath("/Users/dllo/Documents/Lucene/index"));

            // 创建索引文件的读对象
            DirectoryReader reader = DirectoryReader.open(directory);

            // 创建索引搜索对象
            IndexSearcher indexSearcher = new IndexSearcher(reader);

            // 创建标准分词器
//            Analyzer analyzer = new StandardAnalyzer();
            Analyzer analyzer = new HanLPAnalyzer();

            // 创建查询解析器
            // 参数1: 要查询的范围(文件名/文件内容)
            // 参数2: 分词器
            QueryParser queryParser = new QueryParser("content", analyzer);

            // 对搜索关键字进行分析
            Query query = queryParser.parse(keyword);

            // 使用indexSearcher进行查询, 得到结果集
            TopDocs hits = indexSearcher.search(query, 10);

            System.out.println("共搜索到结果的数量: " + hits.totalHits);

            // 获取结果集
//            ScoreDoc[] scoreDocs = hits.scoreDocs;
//            for (ScoreDoc scoreDoc : scoreDocs) {
//                Document document = indexSearcher.doc(scoreDoc.doc);
//                System.out.println(document.get("content"));
//            }
            // --------高亮--------
            // 高亮评分
            QueryScorer queryScorer = new QueryScorer(query);

            // 将原始的字符串拆分成独立的片段
            Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);

            // 定义高亮显示的html标签
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color = 'red'>", "</font>");

            // 高亮分析器
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, queryScorer);
            highlighter.setTextFragmenter(fragmenter);

            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document document = indexSearcher.doc(scoreDoc.doc);

                String content = document.get("content");

                if (content != null) {

                    // TokenStream
                    // 分词器做好处理之后得到的一个流
                    // 这个流中存储了分词的各项信息
                    // 可以通过TokenStream有效的获取分词单元

                    TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));

                    String hContent = highlighter.getBestFragment(tokenStream, content);
                    if (StringUtil.isEmpty(hContent)) {

                    }else {
                        System.out.println(hContent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
