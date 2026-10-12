import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class NLPProcessor {
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "is", "are", "am", "was", "were", "be", "been", "do", "does", "did",
            "i", "me", "my", "you", "your", "we", "our", "it", "its", "to", "of", "in", "on", "at",
            "for", "with", "and", "or", "but", "can", "could", "would", "should", "will", "shall",
            "please", "tell", "about", "this", "that", "there", "so", "if", "as", "by", "have", "has",
            "what", "how", "which", "who", "when", "where", "why", "get", "give", "want", "need", "know"));
    private static final Map<String, String> SYNONYMS = new HashMap<>();
    static {
        SYNONYMS.put("hi", "hello");
        SYNONYMS.put("hey", "hello");
        SYNONYMS.put("hola", "hello");
        SYNONYMS.put("greetings", "hello");
        SYNONYMS.put("bye", "goodbye");
        SYNONYMS.put("cya", "goodbye");
        SYNONYMS.put("thanks", "thank");
        SYNONYMS.put("thx", "thank");
        SYNONYMS.put("certificate", "certificate");
        SYNONYMS.put("cert", "certificate");
        SYNONYMS.put("job", "placement");
        SYNONYMS.put("jobs", "placement");
        SYNONYMS.put("hiring", "placement");
        SYNONYMS.put("assignment", "task");
        SYNONYMS.put("project", "task");
        SYNONYMS.put("oop", "object");
        SYNONYMS.put("jdk", "java");
        SYNONYMS.put("jre", "java");
        SYNONYMS.put("deadline", "time");
        SYNONYMS.put("duration", "time");
        SYNONYMS.put("long", "time");
        SYNONYMS.put("submit", "submission");
        SYNONYMS.put("upload", "submission");
    }

    public List<String> process(String text) {
        List<String> tokens = new ArrayList<>();
        String cleaned = text.toLowerCase().replaceAll("[^a-z0-9\\s]", " ");
        for (String word : cleaned.split("\\s+")) {
            if (word.isEmpty() || STOP_WORDS.contains(word)) continue;
            word = SYNONYMS.getOrDefault(word, word);
            tokens.add(stem(word));
        }
        return tokens;
    }
    private String stem(String w) {
        if (w.length() > 5 && w.endsWith("ing")) return w.substring(0, w.length() - 3);
        if (w.length() > 4 && w.endsWith("ed")) return w.substring(0, w.length() - 2);
        if (w.length() > 4 && w.endsWith("es")) return w.substring(0, w.length() - 2);
        if (w.length() > 3 && w.endsWith("s") && !w.endsWith("ss")) return w.substring(0, w.length() - 1);
        return w;
    }
}
