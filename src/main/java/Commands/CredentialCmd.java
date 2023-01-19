package Commands;

public class CredentialCmd extends CommandAbstract {
    public CredentialCmd(String title) {
        set_commandName(title);
        set_header(title);
        set_footer("return");
    }

    @Override
    public void execute() {
         /*System.out.print("Title: "+cred.getTitle());
            System.out.print("  UserId: "+cred.getUserId());
            System.out.print("  Password: "+cred.getData());
            System.out.println();*/
        executeSubCommands();
    }

    @Override
    public void addCommands() {
        _subCommands.add(new showCredentialCmd(get_header()));
        _subCommands.add(new UpdateTitleCmd(get_header()));
        _subCommands.add(new UpdateUserIdCmd(get_header()));
        _subCommands.add(new UpdatePasswordCmd(get_header()));
    }
}
