# CodeAlpha_AIChatbot

A Java chatbot with NLP and machine-learning-style matching (CodeAlpha Java Internship, Task 3).

## Features
- **NLP pipeline** (`NLPProcessor`): lowercasing, punctuation removal, tokenization, stop-word removal, synonym normalisation and stemming
- **ML-style matching** (`KnowledgeBase`): each FAQ question is turned into a TF-IDF vector; the user's message is matched using cosine similarity
- **Rule-based answers** (`Chatbot`): greetings, help, date/time, exit
- **Trainable**: teach it new answers at runtime with `teach: question => answer` (saved to `faq.txt`, so it remembers next time)
- **Two interfaces**: Swing GUI (real-time chat window) and a console mode
- Pre-trained on FAQs about the CodeAlpha internship and Java basics

## Project Structure
```
src/
  Main.java          // entry point (GUI by default, "console" for terminal)
  Chatbot.java       // rules + response logic
  KnowledgeBase.java // FAQ storage, TF-IDF training, cosine similarity
  NLPProcessor.java  // tokenizer, stop words, synonyms, stemmer
  FaqEntry.java      // one question/answer pair
  ChatGUI.java       // Swing GUI
  ConsoleChat.java   // console interface
```

## How to Run
Requires JDK 17+.
```bash
cd src
javac *.java
java Main            # GUI window
java Main console    # terminal mode
```
A `faq.txt` file is created on first run in the folder you run from. You can edit it directly (format: `question => answer`).

## Example
```
You: how many projects do I have to complete?
CodeBot: You must complete at least 2 (ideally 3) of the 4 Java tasks to get your certificate...
You: teach: what is polymorphism => Polymorphism means one interface, many forms.
You: explain polymorphism
CodeBot: Polymorphism means one interface, many forms.
```
