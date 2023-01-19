package Commands;

import Main.Context;

import java.util.Scanner;

public class UpdateUserPasswordCmd extends CommandAbstract {


    public UpdateUserPasswordCmd() {
        this.set_commandName("Update password");
        this.set_header("Update password");
        this.set_footer("Return");
    }
    @Override
    public void execute() {
        String newPassword;
        Scanner sscanner = new Scanner(System.in);
        newPassword = sscanner.nextLine();
        String currentPassword = Context.get_currentuser().getPassword();
        if (currentPassword.compareTo(newPassword)==0) {
            String userId = Context.get_currentuser().getEmailId();
            if(Context.get_database().updateUserPassword(userId,newPassword)) {
                System.out.println("Password updated successfully");
            } else {
                System.out.println("Fail to update password");
            }
        }
    }

    @Override
    public void addCommands() {

    }
}
