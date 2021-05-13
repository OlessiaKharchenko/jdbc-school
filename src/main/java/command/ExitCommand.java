package command;

public class ExitCommand implements Command {
    private static final String DESCRIPTION = "Exit from the application.";

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void doCommand() {
        System.exit(0);
    }
}