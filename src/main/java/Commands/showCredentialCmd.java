package Commands;

import Main.Context;
import Models.Credential;
import Models.User;

public class showCredentialCmd extends CommandAbstract {
    public showCredentialCmd(String credKey) {
        set_commandName("Show Credential");
        set_header(credKey);
        set_footer("return");
    }

    @Override
    public void execute() {
        User user = Context.get_currentuser();
        Credential cred = user.getCredential(get_header());
        System.out.println("UserId: "+cred.getUserId());
        System.out.println("Password: "+cred.getData());
        System.out.println();
    }

    @Override
    public void addCommands() {

    }
}
