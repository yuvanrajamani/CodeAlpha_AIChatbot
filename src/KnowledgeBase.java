import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class KnowledgeBase {
    private static final Path FILE = Paths.get("faq.txt");
    private final NLPProcessor nlp;
    private final List<FaqEntry> entries = new ArrayList<>();
    private final Map<String, Double> idf = new HashMap<>();

    public KnowledgeBase(NLPProcessor nlp) {
        this.nlp = nlp;
        if (Files.exists(FILE)) {
            loadFromFile();
        } else {
            loadDefaults();
            saveAll();
        }
        train();
    }
    private void loadDefaults() {
        add("hello", "Hello! I'm CodeBot. Ask me about the CodeAlpha internship, Java, or type 'help'.");
        add("goodbye see you later", "Goodbye! Good luck with your Java internship tasks!");
        add("thank you", "You're welcome! Happy to help.");
        add("who are you what is your name", "I'm CodeBot, a Java chatbot built using NLP and TF-IDF similarity matching.");
        add("what can you do help", "I can answer FAQs about the CodeAlpha Java internship and Java basics. You can also teach me new answers with: teach: question => answer");
        add("how many tasks must I complete", "You must complete at least 2 (ideally 3) of the 4 Java tasks to get your certificate. Submitting only one is considered incomplete.");
        add("what are the java internship tasks", "The tasks are: 1) Student Grade Tracker, 2) Stock Trading Platform, 3) AI Chatbot, 4) Hotel Reservation System.");
        add("what are the perks benefits of internship", "Perks: offer letter, QR-verified completion certificate, unique ID certificate, letter of recommendation, placement support and resume building support.");
        add("how do I submit my task submission", "Upload your code to GitHub in a repo named CodeAlpha_ProjectName, post a video on LinkedIn with the repo link, then submit via the form shared in your WhatsApp group.");
        add("what should I name my github repository", "Name your repository CodeAlpha_ProjectName, for example CodeAlpha_StudentGradeTracker.");
        add("how do I get the certificate", "Complete at least two tasks, upload them to GitHub, post your LinkedIn video, and submit through the form. Then you become eligible for the certificate.");
        add("how can I contact codealpha", "Website: www.codealpha.tech, Email: services@codealpha.tech, WhatsApp: +91 9336576683.");
        add("is there placement support job opportunities", "Yes, CodeAlpha offers job opportunities and placement support based on performance.");
        add("what is java", "Java is a popular, object-oriented, platform-independent programming language that runs on the Java Virtual Machine (JVM).");
        add("what is object oriented programming oop", "OOP organizes code into objects. Its four pillars are encapsulation, inheritance, polymorphism and abstraction.");
        add("what is the difference between jdk jre and jvm", "The JVM runs bytecode, the JRE = JVM + libraries to run Java programs, and the JDK = JRE + tools (like javac) to develop Java programs.");
        add("what is an arraylist", "An ArrayList is a resizable array from java.util that lets you add, remove and access elements by index.");
        add("what is multithreading", "Multithreading lets a Java program run multiple threads concurrently, using the Thread class or the Runnable interface.");
        add("what is the difference between interface and abstract class", "An interface defines a contract that classes implement (a class can implement many); an abstract class can hold state and partial implementation, but a class can extend only one.");
        add("what is spring framework", "Spring is a Java framework for building enterprise apps, offering dependency injection, REST APIs (Spring Boot), and data access support.");
        add("how do I run a java program", "Compile with 'javac MyFile.java' and run with 'java MyFile'. You need the JDK installed.");
    }
    private void loadFromFile() {
        try {
            for (String line : Files.readAllLines(FILE)) {
                int i = line.indexOf("=>");
                if (i > 0) add(line.substring(0, i).trim(), line.substring(i + 2).trim());
            }
        } catch (IOException e) {
            System.out.println("Could not read faq.txt, using defaults: " + e.getMessage());
            loadDefaults();
        }
    }
    private void saveAll() {
        List<String> lines = new ArrayList<>();
        for (FaqEntry e : entries) lines.add(e.getQuestion() + " => " + e.getAnswer());
        try {
            Files.write(FILE, lines);
        } catch (IOException e) {
            System.out.println("Could not save faq.txt: " + e.getMessage());
        }
    }
    private void add(String question, String answer) {
        entries.add(new FaqEntry(question, answer, nlp.process(question)));
    }
    public void learn(String question, String answer) {
        add(question, answer);
        try {
            Files.writeString(FILE, question + " => " + answer + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Could not save new knowledge: " + e.getMessage());
        }
        train();
    }
    private void train() {
        idf.clear();
        Map<String, Integer> docFreq = new HashMap<>();
        for (FaqEntry e : entries) {
            for (String t : new java.util.HashSet<>(e.getTokens())) docFreq.merge(t, 1, Integer::sum);
        }
        int n = entries.size();
        for (Map.Entry<String, Integer> d : docFreq.entrySet()) {
            idf.put(d.getKey(), Math.log((n + 1.0) / (d.getValue() + 1.0)) + 1.0);
        }
        for (FaqEntry e : entries) e.setVector(vectorize(e.getTokens()));
    }
    private Map<String, Double> vectorize(List<String> tokens) {
        Map<String, Double> v = new HashMap<>();
        for (String t : tokens) v.merge(t, 1.0, Double::sum);
        for (Map.Entry<String, Double> e : v.entrySet()) {
            e.setValue(e.getValue() * idf.getOrDefault(e.getKey(), 1.0));
        }
        return v;
    }
    private double cosine(Map<String, Double> a, Map<String, Double> b) {
        double dot = 0, na = 0, nb = 0;
        for (Map.Entry<String, Double> e : a.entrySet()) {
            na += e.getValue() * e.getValue();
            Double other = b.get(e.getKey());
            if (other != null) dot += e.getValue() * other;
        }
        for (double x : b.values()) nb += x * x;
        return (na == 0 || nb == 0) ? 0 : dot / (Math.sqrt(na) * Math.sqrt(nb));
    }
    public FaqEntry findBestMatch(String input, double threshold) {
        List<String> tokens = nlp.process(input);
        if (tokens.isEmpty()) return null;
        Map<String, Double> query = vectorize(tokens);
        FaqEntry best = null;
        double bestScore = 0;
        for (FaqEntry e : entries) {
            double score = cosine(query, e.getVector());
            if (score > bestScore) {
                bestScore = score;
                best = e;
            }
        }
        return bestScore >= threshold ? best : null;
    }

    public int size() { return entries.size(); }
}
