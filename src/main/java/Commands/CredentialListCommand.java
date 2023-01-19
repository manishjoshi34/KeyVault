package Commands;

import java.util.ArrayList;

public class CredentialListCommand extends CommandAbstract {
    public CredentialListCommand(String credTitle) {
        this.set_commandName("Credential");
        this.set_header(credTitle);
        this.set_footer("return");
    }

    @Override
    public void execute() {
        executeSubCommands();
    }

    @Override
    public void addCommands() {
        _subCommands.add(new UpdateUserIdCmd(get_header()));
        _subCommands.add(new UpdatePasswordCmd(get_header()));
        _subCommands.add(new UpdateTitleCmd(get_header()));
        _subCommands.add(new deleteCredentialCmd(get_header()));
    }
}
