package Commands;

import Main.Context;

public class deleteAccountCmd extends CommandAbstract {

    public deleteAccountCmd() {
        this.set_commandName("Delete Account");
        this.set_header("Delete Account");
        this.set_footer("Return");
    }
    @Override
    public void execute() {
        if (Context.get_database().deleteuser(Context.get_currentuser().getEmailId())) {
            System.out.println("Account successfully deleted");
        }
    }

    @Override
    public void addCommands() {

    }
}
