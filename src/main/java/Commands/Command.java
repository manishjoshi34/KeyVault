package Commands;

import Main.Context;
import Main.MyDatabase;

import java.util.ArrayList;

public class Command extends CommandAbstract {
    public Command(String name, String header, String footer){
        this.set_commandName(name);
        this.set_header(header);
        this.set_footer(footer);
    }

    public void execute() {
        Context.initialize();
        //Connect to mongoDB and initialize Database
        MyDatabase database = Context.get_database();
        if(database == null){
            logger.severe("Database is not available");
        }
        executeSubCommands();
        Context.clearContext();
        clearCommands();
    }

    public void addCommands() {
        _subCommands.add(new loginCommand());
        _subCommands.add(new signUpCommand());
    }
}
