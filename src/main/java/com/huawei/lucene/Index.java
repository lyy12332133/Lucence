package com.huawei.lucene;

import com.hankcs.lucene.HanLPAnalyzer;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

/**
 * Created by dllo on 17/11/24.
 */
public class Index {

    //只用Lucene做文件索引
    //索引的写入

    public void index(){

        IndexWriter indexWriter = null;
        // 创建索引的目录对象
        try {
            FSDirectory directory = FSDirectory.open(FileSystems.getDefault().getPath("/Users/dllo/Documents/Lucene/index"));

            // 创建分词器
//            Analyzer analyzer = new StandardAnalyzer();
            Analyzer analyzer = new HanLPAnalyzer();

            // 创建写入索引对象的配置
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

            // 创建写入索引的对象
            // 需要传入索引的保存路径和分词器配置
            indexWriter = new IndexWriter(directory,indexWriterConfig);

            // 写入之前清除之前的所有索引
            // TODO: 实际使用时注意删除这一行
            indexWriter.deleteAll();

            // 读取要进行索引的文件
            File dataFile = new File("/Users/dllo/Documents/Lucene/data");

            // 获取目录下的所有txt文件
            File[] txtFile = dataFile.listFiles();
            for (File file : txtFile) {
                // 将每个文件写入到索引中
                Document doc = new Document();

                // Field构造函数的三个参数
                // name : 自定义的key值, 方便搜索时确认范围
                // value: 实际需要写入索引的内容
                // type : 是否需要持久化
                doc.add(new Field("content", FileUtils.readFileToString(file,"UTF-8"), TextField.TYPE_STORED));
                doc.add(new Field("filename",file.getName(),TextField.TYPE_STORED));
                // 写入
                indexWriter.addDocument(doc);


            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (indexWriter != null){
                try {
                    indexWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
