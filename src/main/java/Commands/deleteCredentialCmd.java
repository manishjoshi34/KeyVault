package Commands;

import Main.Context;
import Main.MyDatabase;

import java.util.Scanner;

public class deleteCredentialCmd extends CommandAbstract{
    public deleteCredentialCmd(String credkey){
        this.set_commandName("Delete Credential");
        this.set_header(credkey);
        this.set_footer("return");
    }

    @Override
    public void execute() {
        MyDatabase database = Context.get_database();
        if (database.removeCredential(get_header(),Context.get_currentuser().getEmailId())) {
            System.out.println("Credential removed successfully");
        }
    }

    @Override
    public void addCommands() {

    }
}
