/** Entry point. Run with no args for the GUI, or pass "console" for the terminal version. */
public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
            ConsoleChat.run();
        } else if (java.awt.GraphicsEnvironment.isHeadless()) {
            System.out.println("No display detected, starting console mode.");
            ConsoleChat.run();
        } else {
            ChatGUI.run();
        }
    }
}
