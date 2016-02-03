import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.international.negra.NegraPennLanguagePack;
import edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack;

import java.io.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by krayush on 19-12-2015.
 */
public class ParsingNonEnglish {
    public static void main(String[] args) throws IOException {

        String line;
        String rootDir = System.getProperty("user.dir");
        //PrintWriter write = new PrintWriter(new File(rootDir + "\\resources\\english\\POS_Laptops_English.txt"), "UTF-8");
        //PrintWriter write1 = new PrintWriter(new File(rootDir + "\\resources\\english\\UniversalParsed_Laptops_English.txt"), "UTF-8");
        //PrintWriter write2 = new PrintWriter(new File(rootDir + "\\resources\\english\\GrammaticalParsed_Laptops_English.txt"), "UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(rootDir + "\\resources\\dutch\\Restaurants_Dutch_Train_Without_Duplicates_2016.txt"), "UTF-8"));

        //MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
        //MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/german-hgc.tagger");

        LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/germanPCFG.ser.gz");

        //lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
        //lp.setOptionFlags(new String[]{"-outputFormat", "penn,typedDependenciesCollapsed", "-retainTmpSubcategories"});

        TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");

        while ((line = reader.readLine()) != null) {
            //String tagged = tagger.tagString(line);
            String[] str = line.split("\\|");

            List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(new StringReader(str[3])).tokenize();
            Tree bestParse = lp.parseTree(rawWords);
            //System.out.println(bestParse);
            //String tagged = tagger.tagString(str[3]);

            TreebankLanguagePack tlp = new NegraPennLanguagePack();
            tlp.setGenerateOriginalDependencies(true);
            GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
            GrammaticalStructure gs = gsf.newGrammaticalStructure(bestParse);
            List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
            System.out.println(tdl);
            //System.out.println(tdl);
            /*write.println(tagged);
            write1.println(bestParse);
            write2.println(tdl);*/
        }
        /*write.close();
        write1.close();
        write2.close();*/

        System.out.println();

        //TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
        //tp.printTree(parse);

        /*PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<CoreLabel>(new FileReader(rootDir + "\\resources\\english\\Laptops_English_Train_Without_Duplicates_2016.txt"),
                new CoreLabelTokenFactory(), "");
        while (ptbt.hasNext()) {
            CoreLabel label = ptbt.next();
            System.out.println(label);

            write.close();
        }*/

        /*String sent = "This is one last day!";
        LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        //lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
        lp.setOptionFlags(new String[]{"-outputFormat", "penn,wordsAndTags,typedDependenciesCollapsed", "-retainTmpSubcategories"});
        /*TreebankLanguagePack tlp = lp.getOp().langpack();
        Tokenizer<? extends HasWord> toke = tlp.getTokenizerFactory().getTokenizer(new StringReader(sent));
        List<? extends HasWord> sentence = toke.tokenize();
        lp.apply(sentence).pennPrint();
        */
        /*TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(new StringReader(sent)).tokenize();
        Tree bestParse = lp.parseTree(rawWords);
        System.out.println(bestParse);*/
        //return bestParse;
        //lp.apply(sent).pennPrint();
    }
}
