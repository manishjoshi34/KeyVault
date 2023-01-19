package Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;


public abstract class CommandAbstract {
    static final Logger logger = Logger.getLogger(Command.class.getName());
    private String    _commandName;

    private String    _header;

    private String   _footer;

    List<CommandAbstract> _subCommands = new ArrayList<>();
    public  String get_commandName() {
        return _commandName;
    }
    public  String get_header() {
        return _header;
    }
    public  String get_footer() {
        return _footer;
    }

    public  void set_commandName(String commandName) {
        _commandName = commandName;
    }

    public  void set_header(String header) {
        _header = header;
    }

    public  void set_footer(String footer) {
        _footer = footer;
    }
    public void CommandMenu(){
        System.out.println("\n\t\t" + get_header() + "\t\t\n");
        System.out.println("........................................\n");
        int index = 1;
        for (CommandAbstract command : _subCommands) {
            System.out.println(index + ")\t\t" + command.get_commandName());
            index++;
        }
        System.out.println(index + ")\t\t" + get_footer() + "\n\n");
    }
    public void executeSubCommands(){
        addCommands();
        CommandMenu();
        while (true) {
            int userSelection;
            Scanner scanner = new Scanner(System.in);
            userSelection = scanner.nextInt();
            if (userSelection > _subCommands.size()) {
                clearCommands();
                return;
            }
            CommandAbstract current = _subCommands.get(userSelection - 1);
            current.execute();
            CommandMenu();
        }

    }

    public void clearCommands() {
        _subCommands.clear();
    }

    public  abstract void execute() ;

    public  abstract void addCommands();

}
