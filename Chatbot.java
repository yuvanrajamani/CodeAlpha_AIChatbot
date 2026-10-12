import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/** The chatbot brain: rule-based commands first, then ML-style FAQ matching. */
public class Chatbot {
    private static final double THRESHOLD = 0.30;
    private final KnowledgeBase kb = new KnowledgeBase(new NLPProcessor());
    private final Random random = new Random();
    private boolean finished = false;

    private static final String[] FALLBACKS = {
        "Sorry, I don't know that yet. You can teach me with: teach: question => answer",
        "I'm not sure about that. Try rephrasing, or teach me using: teach: question => answer",
        "Hmm, I don't have an answer for that. Type 'help' to see what I can do."
    };

    public boolean isFinished() { return finished; }

    public String greeting() {
        return "Hi! I'm CodeBot (" + kb.size() + " answers learned). Ask me something, or type 'help'. Type 'exit' to quit.";
    }

    public String respond(String input) {
        String text = input.trim();
        if (text.isEmpty()) return "Please type something.";
        String lower = text.toLowerCase();

        // --- Rule-based commands ---
        if (lower.equals("exit") || lower.equals("quit") || lower.equals("bye")) {
            finished = true;
            return "Goodbye! Good luck with your internship!";
        }
        if (lower.equals("help")) {
            return "Try: 'how many tasks must I complete?', 'what is an ArrayList?', 'what is the time?'\n"
                 + "Teach me: teach: <question> => <answer>";
        }
        if (lower.contains("time") && (lower.contains("what") || lower.contains("current")) && !lower.contains("task")) {
            return "The current time is " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")) + ".";
        }
        if (lower.contains("date") || lower.contains("today")) {
            return "Today's date is " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")) + ".";
        }
        if (lower.startsWith("teach:")) {
            String body = text.substring(6);
            int i = body.indexOf("=>");
            if (i < 0) return "Use the format: teach: question => answer";
            String q = body.substring(0, i).trim();
            String a = body.substring(i + 2).trim();
            if (q.isEmpty() || a.isEmpty()) return "Both the question and answer are required.";
            kb.learn(q, a);
            return "Thanks! I've learned a new answer. Ask me that question to test it.";
        }

        // --- NLP + similarity matching ---
        FaqEntry match = kb.findBestMatch(text, THRESHOLD);
        if (match != null) return match.getAnswer();
        return FALLBACKS[random.nextInt(FALLBACKS.length)];
    }
}
