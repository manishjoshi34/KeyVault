package Commands;

import Main.Context;
import Main.MyDatabase;

import java.util.Scanner;

public class UpdatePasswordCmd extends CommandAbstract{

    public UpdatePasswordCmd(String credkey){
        this.set_commandName("Update Password");
        this.set_header(credkey);
        this.set_footer("return");
    }
    @Override
    public void execute() {
        MyDatabase database = Context.get_database();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new password: ");
        String newPassword = scanner.nextLine();
        if (database.updateCredentialData(get_header(),newPassword)) {
            logger.info("Password updated successfully");
        }
    }

    @Override
    public void addCommands() {

    }
}
