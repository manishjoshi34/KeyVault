package Commands;

import Main.Context;
import Main.MyDatabase;

import java.util.Scanner;

public class UpdateUserIdCmd extends CommandAbstract{

    public UpdateUserIdCmd(String credTitle){
        this.set_commandName("Update userId");
        this.set_header(credTitle);
        this.set_footer("return");
    }
    @Override
    public void execute() {
        MyDatabase database = Context.get_database();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new userId: ");
        String newUserId = scanner.nextLine();
        if (database.updateUserId(get_header(),Context.get_currentuser().getEmailId(),newUserId)) {
            logger.info("userId updated successfully");
        }
    }

    @Override
    public void addCommands() {

    }

}
