package Commands;

import Main.Context;
import Models.User;

import java.util.Scanner;

public class loginCommand extends signUpCommand {
    public loginCommand() {
        this.set_commandName("Login");
        this.set_header("Login");
        this.set_footer("Logout");
    }

    String getPassword(Scanner scanner) {
        System.out.println("Enter Password: ");
        return scanner.nextLine();
    }

    @Override
    public void execute() {
        int passwordTry = 0;
        String emailId, password;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your Email Id: ");
        emailId =  scanner.nextLine();
        if(!Context.get_database().isUserPresent(emailId)){
            System.out.println("No account associated with this email id");
            System.out.println("Create new account");
            return;
        } else {
            User user = Context.get_database().getUser(emailId);
            if (user.getIsUserLogin()) {
                System.out.println("You already logged in with different device");
                System.out.println("Logout from other device to login here");
                return;
            }
            Context.set_currentuser(user);
        }
        password = getPassword(scanner);
        String storedPassword = Context.get_currentuser().getPassword();
        if (storedPassword.compareTo(password)!=0) {
            System.out.println("Your password is invalid");
            return;
        } else {
            set_header("Welcome "+Context.get_currentuser().getName());
            if (!Context.get_database().loginUser(emailId)) {
                System.out.println("login fail");
                return;
            }
            executeSubCommands();
        }
        /*boolean matchPassword = false;
        while(passwordTry <3) {
            passwordTry +=1;
            password = getPassword(scanner);
            String storedPassword = Context.get_currentuser().getPassword();
            if (storedPassword.compareTo(password)!=0) {
                System.out.println("Your password is invalid. Try again");
            } else {
                matchPassword  =true;
                break;
            }
        }
        if(passwordTry==3) {
            System.out.println("You have reached to maximum try. Try to reset password");
            return;
        } else if (!matchPassword) {
            System.out.println("You have logged in");
        } else {
           return;
        }

        executeSubCommands();*/
    }

}
