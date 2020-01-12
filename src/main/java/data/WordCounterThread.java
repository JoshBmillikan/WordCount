package data;

import org.apache.commons.text.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class WordCounterThread implements Callable<Map<String, Integer>> {
    private final String txt;

    public WordCounterThread(final String txt) {
        this.txt = txt;
    }

    //common words to be ignored
    private static final String[] ignoreList = {"The", "it", "I", "A", "You", "He", "She", "They", "Me", "We", "His", "Mine", "Her", "Him", "", " "
            , "and", "was", "to", "in", "that", "with", "had", "not", "is", "by", "as", "on", "at", "of", "but", "for", "from", "who", "where", "what", "when", "or", "so"};

    @Override
    public Map<String, Integer> call() {
        Map<String, Integer> out = new HashMap<>();
        String[] words = txt.split("[.\\s]");
        for (String word : words) {
            word = StringEscapeUtils.escapeJava(word);
            word = word.toLowerCase().trim();
            boolean isIgnored = false;
            for (String s : ignoreList) {
                if (word.equalsIgnoreCase(s)) {
                    isIgnored = true;
                    break;
                }
            }
            if (!isIgnored) {
                if (out.containsKey(word)) {
                    int val = out.get(word);
                    val++;
                    out.replace(word, val);
                } else out.put(word, 1);
            }
        }
        return out;
    }
}
