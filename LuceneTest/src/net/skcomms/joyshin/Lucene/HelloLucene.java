package net.skcomms.joyshin.Lucene;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class HelloLucene {
	
	public static void main(String[] args) throws IOException, ParseException {
	    // 0. Specify the analyzer for tokenizing text.
	    //    The same analyzer should be used for indexing and searching
	    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);

	    // 1. create the index
	    Directory index = new RAMDirectory();

	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, analyzer);

	    IndexWriter w = new IndexWriter(index, config);
	    addDoc(w, "Lucene in Action");
	    addDoc(w, "Lucene for Dummies");
	    addDoc(w, "Managing Gigabytes");
	    addDoc(w, "The Art of Computer Science");
	    w.close();

	    // 2. query
	    String querystr = args.length > 0 ? args[0] : "art";

	    // the "title" arg specifies the default field to use
	    // when no field is explicitly specified in the query.
	    Query q = new QueryParser(Version.LUCENE_35, "title", analyzer).parse(querystr);

	    // 3. search
	    int hitsPerPage = 10;
	    IndexReader reader = IndexReader.open(index);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    
	    // 4. display results
	    System.out.println("Found " + hits.length + " hits.");
	    for(int i=0;i<hits.length;++i) {
	      int docId = hits[i].doc;
	      Document d = searcher.doc(docId);
	      System.out.println((i + 1) + ". " + d.get("title"));
	    }

	    // searcher can only be closed when there
	    // is no need to access the documents any more. 
	    searcher.close();
	  }

	  private static void addDoc(IndexWriter w, String value) throws IOException {
	    Document doc = new Document();
	    doc.add(new Field("title", value, Field.Store.YES, Field.Index.ANALYZED));
	    w.addDocument(doc);
	  }
	
//	public static void main(String[] args) {
//		StringReader reader = new StringReader(new String(
//				"jongwanyun@gmail.com �����ּ�"));
//
//		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
//		TokenStream tokens = analyzer.tokenStream(new String("email"), reader);
//		Token token = new Token();
//		try {
//			int tokenId = 0;
//			//while ((token = tokens.next()) != null) {
//			while ((token = tokens..next()) != null) {
//				StringBuffer out = new StringBuffer();
//				out.append(tokenId++);
//				out.append("\tToken: [");
//				//out.append(token.termBuffer());
//				out.append(token.buffer());
//				out.append("]\t");
//				out.append("Type/start/end/length: [");
//				out.append(token.type());
//				out.append(",");
//				out.append(token.startOffset());
//				out.append(",");
//				out.append(token.endOffset());
//				out.append(",");
//				//out.append(token.termLength());
//				out.append(token.length());
//				out.append("]");
//				System.out.println(out.toString());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
