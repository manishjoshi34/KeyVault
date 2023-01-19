package Commands;

import Main.Context;
import Models.Credential;

import java.util.Scanner;

public class findCredentialCmd extends CommandAbstract {

    public findCredentialCmd() {
        this.set_commandName("Search Credential");
        this.set_header("Search");
        this.set_footer("Return");
    }

    @Override
    public void execute() {
        String title;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter title: ");
        title =  scanner.nextLine();
        Credential cred = Context.get_database().getCredential(Context.get_currentuser().getEmailId(),title);
        if (cred != null) {
            System.out.println("User Id: " + cred.getUserId());
            System.out.println(("Password: " + cred.getData()));
        } else {
            System.out.println("Credential not found");
        }
        executeSubCommands();
    }

    @Override
    public void addCommands() {
        _subCommands.add(new findCredentialCmd());
    }
}
