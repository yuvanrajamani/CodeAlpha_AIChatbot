import java.util.List;
import java.util.Map;
public class FaqEntry {
    private final String question;
    private final String answer;
    private final List<String> tokens;
    private Map<String, Double> vector;
    public FaqEntry(String question, String answer, List<String> tokens) {
        this.question = question;
        this.answer = answer;
        this.tokens = tokens;
    }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public List<String> getTokens() { return tokens; }
    public Map<String, Double> getVector() { return vector; }
    public void setVector(Map<String, Double> vector) { this.vector = vector; }
}
