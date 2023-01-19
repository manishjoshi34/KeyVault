package Commands;

import Main.Context;
import Main.MyDatabase;

import java.util.Scanner;

public class UpdateTitleCmd extends CommandAbstract{

    public UpdateTitleCmd(String credkey){
        this.set_commandName("Update Title");
        this.set_header(credkey);
        this.set_footer("return");
    }
    @Override
    public void execute() {
        MyDatabase database = Context.get_database();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new title: ");
        String title = scanner.nextLine();
        if (database.updateTitle(get_header(),Context.get_currentuser().getEmailId(),title)) {
            logger.info("title updated successfully");
        }
    }

    @Override
    public void addCommands() {

    }
}
