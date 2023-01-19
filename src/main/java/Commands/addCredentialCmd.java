package Commands;

import Main.Context;
import Models.Credential;
import Main.MyDatabase;

import java.util.Scanner;

public class addCredentialCmd extends CommandAbstract {
    public addCredentialCmd() {
        this.set_commandName("AddCredential");
        this.set_footer("return");
        this.set_header("Add Credential");
    }

    boolean isCredentialPresent(String title) {
        if(Context.get_currentuser().getCredential(title)!=null) {
            return true;
        }
        return false;
    }
    @Override
    public void execute() {
        MyDatabase database = Context.get_database();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter credential title ");
        String title = scanner.nextLine();
        if(isCredentialPresent(title)){
            System.out.println("Credential with same title present");
            executeSubCommands();
            return;
        } else {
            System.out.println("Enter userId");
            String userId = scanner.nextLine();
            System.out.println(("Enter password"));
            String password = scanner.nextLine();
            if (database.addCredential(new Credential(title,userId,password),Context.get_currentuser().getEmailId())) {
                System.out.println("Credential added successfully");
            }
        }


    }

    @Override
    public void addCommands() {
        _subCommands.add(new addCredentialCmd());
    }

    @Override
    public void finalize() {
        _subCommands.clear();
    }
}
