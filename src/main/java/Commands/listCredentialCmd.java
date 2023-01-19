package Commands;

import Main.Context;
import Models.Credential;
import Models.User;

public class listCredentialCmd extends CommandAbstract {
    public listCredentialCmd() {
        this.set_commandName("List Credential");
        this.set_header("List Credential");
        this.set_footer("return");
    }

    @Override
    public void execute() {
        User user = Context.get_currentuser();
        for(Credential cred : user.getCredentials().values()) {
            _subCommands.add(new CredentialCmd(cred.getTitle()));
        }
        executeSubCommands();
    }

    @Override
    public void addCommands() {

    }
}
