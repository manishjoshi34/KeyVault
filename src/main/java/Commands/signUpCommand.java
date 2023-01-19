package Commands;

import Main.Context;
import Models.User;

import java.util.Scanner;

public class signUpCommand extends CommandAbstract{
    public signUpCommand() {
        this.set_commandName("signUp");
        this.set_header("create account");
        this.set_footer("Logout");
    }

    @Override
    public void execute() {
        String name, emailId, password;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your Name: ");
        name = scanner.nextLine();
        System.out.println("Enter your Email Id: ");
        emailId =  scanner.nextLine();
        System.out.println("Enter Password: ");
        password = scanner.nextLine();

        User user = new User(name,emailId, password);
        //System.out.println(user.toString());
        if (!Context.get_database().createUser(user)) {
            System.out.println("User already exist with given emailId.");
            System.out.println("Try with different emailId");
        } else  {
            Context.set_currentuser(user);
            System.out.println("Account created successfully!");
            set_header("Welcome "+user.getName());
        }
        executeSubCommands();
    }

    @Override
    public void clearCommands(){
        if (Context.get_database().logoutUser(Context.get_currentuser().getEmailId())) {
            System.out.println("You have logged out successfully");
        }
        super.clearCommands();
    }

    @Override
    public void addCommands() {
        _subCommands.add(new addCredentialCmd());
        _subCommands.add(new listCredentialCmd());
        _subCommands.add(new findCredentialCmd());
        _subCommands.add(new UpdateUserNameCmd());
        _subCommands.add(new UpdateEmailIdCmd());
        _subCommands.add(new UpdateUserPasswordCmd());
        _subCommands.add(new deleteAccountCmd());
    }
}
