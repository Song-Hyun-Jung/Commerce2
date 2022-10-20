package com.digital2.lucene;


import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

public class DataHandler {

	static String flag = "";
	private static Directory dir = null;
	static final File fileIndex = new File("C:\\Users\\aimie\\eclipse-workspace-unipoint\\unipoint_commerce2_lucene");

	static {

		if ("".equals(flag)) {
			synchronized (flag) {
				try {
					
					dir = FSDirectory.open(Paths.get(fileIndex.toURI()));
					flag = "OK";
					
					IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
					IndexWriter writer = new IndexWriter(dir, writerConfig);
					
					writer.commit();
					writer.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean write(Document doc) throws Exception {

		if (doc == null)
			return false;

		try {
			IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
			IndexWriter writer = new IndexWriter(dir, writerConfig);

			writer.addDocument(doc);
			writer.commit();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	public static boolean update(String findField, String findValue, Document doc) throws Exception {

		if (doc == null)
			return false;

		try {
			IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
			IndexWriter writer = new IndexWriter(dir, writerConfig);
			
			Term updateTerm = new Term(findField, findValue);
			writer.updateDocument(updateTerm, doc);
			writer.commit();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	public static boolean delete(String findField, String findValue) throws Exception {


		try {
			IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
			IndexWriter writer = new IndexWriter(dir, writerConfig);
			
			Term deleteTerm = new Term(findField, findValue);
			writer.deleteDocuments(deleteTerm);
			writer.commit();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}

	public static List<Document> wildCardQuery(String key, String value) {

		Document doc = null;
		List<Document> docList = new ArrayList<Document>();
		try {
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);

			doc = new Document();
			Query wordQuery = new WildcardQuery(new Term(key, "*" + value + "*"));

			TopDocs foundDocsBody = searcher.search(wordQuery, 1000);
			for (ScoreDoc sd : foundDocsBody.scoreDocs) {
				doc = searcher.doc(sd.doc);
				docList.add(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return docList;

	}

	public static List<Document> findListHardly(String key, String value) {

		Document doc = null;
		List<Document> docList = new ArrayList<Document>();

		try {

			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);

			Query wordQuery = new QueryBuilder(new StandardAnalyzer()).createBooleanQuery(key, value, Occur.MUST);
			TopDocs foundDocsBody = searcher.search(wordQuery, 1000);

			for (ScoreDoc sd : foundDocsBody.scoreDocs) {
				doc = searcher.doc(sd.doc);
				docList.add(doc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return docList;
	}
	
	public static Document findHardly(String key, String value) {

		Document doc = null;

		try {

			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);

			Query wordQuery = new QueryBuilder(new StandardAnalyzer()).createBooleanQuery(key, value, Occur.MUST);
			TopDocs foundDocsBody = searcher.search(wordQuery, 1000);

			for (ScoreDoc sd : foundDocsBody.scoreDocs) {
				doc = searcher.doc(sd.doc);
				return doc;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
