package com.sns.lucene;

import com.sns.lucene.document.LuceneWriteIndex;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LuceneApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuceneApplication.class, args);
        try {
            LuceneWriteIndex.createIndex();
            //LuceneReadIndex.readIndex("*boo*~0.6");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

