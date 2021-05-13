package command;

import exceptions.DaoException;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MenuContainer {
    private static final String MENU_PATTERN = "Select option from the menu below:%n%s%nEnter selected item number:%n";
    private final List<Command> commands;

    public MenuContainer() {
        this.commands = new ArrayList<>();
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void runMenu() throws DaoException, SQLException {
        System.out.println(showMenu());
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int menuItemId = scanner.nextInt();
            commands.get(menuItemId).doCommand();
        }
    }

    private String showMenu() {
        final String menuItems = commands.stream()
                .map(element -> String.format("%d. %s", commands.indexOf(element), element.getDescription()))
                .collect(Collectors.joining(System.lineSeparator()));
        return String.format(MENU_PATTERN, menuItems);
    }
}