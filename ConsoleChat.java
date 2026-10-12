import java.util.Scanner;

/** Console interface for the chatbot. */
public class ConsoleChat {
    public static void run() {
        Chatbot bot = new Chatbot();
        Scanner scanner = new Scanner(System.in);
        System.out.println("CodeBot: " + bot.greeting());
        while (!bot.isFinished()) {
            System.out.print("You: ");
            if (!scanner.hasNextLine()) break;
            System.out.println("CodeBot: " + bot.respond(scanner.nextLine()));
        }
    }
}
