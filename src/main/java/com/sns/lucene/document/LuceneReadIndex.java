package com.sns.lucene.document;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneReadIndex
{
	private static final String INDEX_DIR = "c:/temp/lucene6index";

	public static void main(String[] args) {
//		Date date = new Date();
//		IndexSearcher searcher = createSearcher();
//
//		//Search by ID
////		TopDocs foundDocs = searchById(1, searcher);
////
////		System.out.println("Toral Results :: " + foundDocs.totalHits);
////
////		for (ScoreDoc sd : foundDocs.scoreDocs)
////		{
////			Document d = searcher.doc(sd.doc);
////			System.out.println(String.format(d.get("firstName")));
////		}
//
//		//Search by firstName
//		TopDocs foundDocs2 = searchByFirstName("Shir", searcher);
//
//		System.out.println("Total Results :: " + foundDocs2.totalHits);
//
//		for (ScoreDoc sd : foundDocs2.scoreDocs)
//		{
//			Document d = searcher.doc(sd.doc);
//			System.out.println(String.format(d.get("id"))+" - "+d.get("firstName")+" "+d.get("middleName")+" "+d.get("lastName"));
//		}
//		System.out.println(new Date().getTime()-date.getTime()+" milliseconds taken");
	}

	public static JsonObject readIndex(String keyword) throws Exception {
		JsonObject result = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		IndexSearcher searcher = createSearcher();
		long startTime = System.currentTimeMillis();

		TopDocs foundDocs2 = searchByFullName(keyword, searcher);

		long endTime = System.currentTimeMillis();

		long timeTaken = endTime - startTime;
		System.out.println("----------------------------------\n"+foundDocs2.totalHits +
							" documents found. Time :" + (endTime - startTime)
							+ "ms\n----------------------------------");
		result.addProperty("TimeTaken",timeTaken+" ms");
		result.addProperty("Count",foundDocs2.totalHits);
		List<JsonObject> jsonObjectList = new ArrayList<>();
		for (ScoreDoc sd : foundDocs2.scoreDocs)
		{
			Document d = searcher.doc(sd.doc);
			double ScoreBasedOnLength = (float)keyword.length()/(float)d.get("fullName").length();
			JsonObject person = new JsonObject();
			person.addProperty("id",d.get("id"));
			person.addProperty("firstName",d.get("firstName"));
			person.addProperty("middleName",d.get("middleName"));
			person.addProperty("lastName",d.get("lastName"));
			person.addProperty("score",sd.score+ScoreBasedOnLength);
			jsonObjectList.add(person);
//			System.out.println(String.format(d.get("id"))+" - "+d.get("fullName"));
			System.out.println(d.get("id") +" - "+d.get("firstName")+" "+d.get("middleName")+" "+d.get("lastName")+", Score:"+(sd.score+ScoreBasedOnLength));
		}
		jsonObjectList.sort((o1, o2) -> Double.compare(o2.get("score").getAsDouble(), o1.get("score").getAsDouble()));

		for (int i = 0; i < jsonObjectList.size(); i++) {
			JsonObject jo = jsonObjectList.get(i);
			jo.addProperty("Rank",i+1);
			jsonArray.add(jo);
		}
		result.add("Names",jsonArray);

		return result;
	}

//	public static TopDocs searchByFirstName(String firstName, IndexSearcher searcher) throws Exception
//	{
//		QueryParser qp = new QueryParser("firstName", new StandardAnalyzer());
////		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
////				new String[] {"firstName", "lastName"},new StandardAnalyzer());
////		BooleanQuery booleanQuery = new BooleanQuery();
//
//		Query firstNameQuery = qp.parse(firstName);
//		TopDocs hits = searcher.search(firstNameQuery, 10);
//		return hits;
//	}

	private static TopDocs searchByFullName(String partialName, IndexSearcher searcher) throws Exception
	{
		String qry = "(fullName:"+partialName.replaceFirst("\\*"," ")+") OR (firstName:"+partialName+" OR middleName:"+partialName+" OR LastName:"+partialName+")";
		QueryParserBase qpb = new QueryParser(qry,new StandardAnalyzer());
		qpb.setAllowLeadingWildcard(true);
//		((QueryParser) qpb).setSplitOnWhitespace(true);

		Query fullNameQuery = new BoostQuery(qpb.parse(qry),0.9f);
        return searcher.search(fullNameQuery, 10);
	}

//	private static TopDocs searchById(Integer id, IndexSearcher searcher) throws Exception
//	{
//		QueryParser qp = new QueryParser("id", new StandardAnalyzer());
//		Query idQuery = qp.parse(id.toString());
//		TopDocs hits = searcher.search(idQuery, 10);
//		return hits;
//	}

	private static IndexSearcher createSearcher() throws IOException {
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		IndexReader reader = DirectoryReader.open(dir);
        return new IndexSearcher(reader);
	}
}
