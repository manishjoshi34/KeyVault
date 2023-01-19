package Commands;

import Main.Context;
import Main.MyDatabase;

import java.util.Scanner;

public class UpdateEmailIdCmd extends CommandAbstract {

    public UpdateEmailIdCmd() {
        this.set_commandName("Update EmailId");
        this.set_header("Update EmailId");
        this.set_footer("return");
    }

    @Override
    public void execute() {
        MyDatabase database = Context.get_database();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new emailId: ");
        String newEmailId = scanner.nextLine();
        if (database.updateEmailId(newEmailId)) {
            logger.info("EmailId updated successfully");
        }
        logger.info("Email id not updated");
    }

    @Override
    public void addCommands() {

    }
}
