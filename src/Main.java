import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: tanin
 * Date: 4/27/12
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {




    public static void main(String[] args) {
        
        try {
            String filename = "/home/tanin/IdeaProjects/QriAnalyzerTestingFramework/input";
            FileInputStream fis = new FileInputStream(new File(filename));
            Scanner input = new Scanner(new BufferedInputStream(fis));

            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            Analyzer experimentee = new StandardAnalyzer(Version.LUCENE_CURRENT);

            while (input.hasNextLine()) {

                String line = input.nextLine().trim();
                if (line.compareTo("") == 0) continue;

                String realLine = line.replaceAll("\\|", "");

                String[] expectedWords = getWords(line, analyzer);
                String[] words = getWords(realLine, experimentee);
                
                printArray(words);
                printArray(expectedWords);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void printArray(String[] arr) {
        System.out.print("Results: ");
        for (int i=0;i<arr.length;i++) {
            if (i > 0) System.out.print('|');
            System.out.print(arr[i]);
        }
        System.out.println();
    }


    public static String[] getWords(String line,  Analyzer analyzer) throws Exception {

        ArrayList<String> words = new ArrayList<String>();
        
        TokenStream stream = analyzer.tokenStream("content", new StringReader(line));

        OffsetAttribute offsetAttribute = stream.addAttribute(OffsetAttribute.class);
        CharTermAttribute charTermAttribute = stream.addAttribute(CharTermAttribute.class);


        while (stream.incrementToken()) {
            int startOffset = offsetAttribute.startOffset();
            int endOffset = offsetAttribute.endOffset();
            String term = charTermAttribute.toString();
            
            words.add(term);
        }

        return words.toArray(new String[words.size()]);
    }
}
