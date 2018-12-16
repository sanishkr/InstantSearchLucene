package com.sns.lucene.document;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.sns.lucene.Tools.Utils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

public class LuceneWriteIndex
{
	private static final String INDEX_DIR = Utils.fetchProperties().getProperty("INDEX_DIR");

	public static void createIndex() throws Exception{
		IndexWriter writer = createWriter();
		List<Document> documents = new ArrayList<>();

		List<Utils.Person> personList = Utils.getAllPersons();
		int i=0;
		for (Utils.Person p: personList) {
			Document document = createDocument(i++, p.getfName(), p.getmName(), p.getlName(),
										p.getfName()+" "+p.getmName()+" "+p.getlName());
			documents.add(document);
		}

		writer.deleteAll();

		writer.addDocuments(documents);
		writer.commit();
		writer.close();
	}

	private static Document createDocument(Integer id, String firstName, String middleName, String lastName, String fullName)
	{
    	Document document = new Document();
    	document.add(new StringField("id", id.toString() , Field.Store.YES));
    	document.add(new TextField("firstName", firstName , Field.Store.YES));
    	document.add(new TextField("middleName", middleName , Field.Store.YES));
    	document.add(new TextField("lastName", lastName , Field.Store.YES));
		document.add(new TextField("fullName", fullName.toLowerCase() , Field.Store.YES));
		return document;
    }

	private static IndexWriter createWriter() throws IOException 
	{
		FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		return new IndexWriter(dir, config);
	}
}
