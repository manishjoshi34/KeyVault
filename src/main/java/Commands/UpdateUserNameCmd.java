package Commands;

import Main.Context;
import Main.MyDatabase;

import java.util.Scanner;

public class UpdateUserNameCmd extends CommandAbstract {
    public UpdateUserNameCmd() {
        this.set_commandName("Update Username");
        this.set_header("Update Username");
        this.set_footer("Return");
    }
    @Override
    public void execute() {
        String newName;
        Scanner scanner = new Scanner(System.in);
        newName = scanner.nextLine();
        if (Context.get_database().updateUserName(Context.get_currentuser().getEmailId(),newName)){
            System.out.println("Name successfully updated");
        } else {
            System.out.println("Fail to update name");
        }
    }

    @Override
    public void addCommands() {

    }
}
