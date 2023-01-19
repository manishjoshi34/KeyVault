package Main;

import Commands.Command;
import Utility.Encryption;
import com.bettercloud.vault.VaultException;
import Main.*;

public class Main {
    public static void main(String[] args) throws VaultException {

         Command menu = new Command("main","Menu","exit");
         menu.execute();
    }
}
