package emanondev.itemedit.command;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.command.serveritem.Buy;
import emanondev.itemedit.command.serveritem.BuyMax;
import emanondev.itemedit.command.serveritem.Delete;
import emanondev.itemedit.command.serveritem.Drop;
import emanondev.itemedit.command.serveritem.Give;
import emanondev.itemedit.command.serveritem.GiveAll;
import emanondev.itemedit.command.serveritem.Save;
import emanondev.itemedit.command.serveritem.Sell;
import emanondev.itemedit.command.serveritem.SellMax;
import emanondev.itemedit.command.serveritem.SetNick;
import emanondev.itemedit.command.serveritem.Show;
import emanondev.itemedit.command.serveritem.Take;
import emanondev.itemedit.command.serveritem.Update;

public class ServerItemCommand extends AbstractCommand {
   public static ServerItemCommand instance;

   public ServerItemCommand() {
      super("serveritem", ItemEdit.get());
      instance = this;
      this.registerSubCommand(new Save(this));
      this.registerSubCommand(new Update(this));
      this.registerSubCommand(new Delete(this));
      this.registerSubCommand(new Show(this));
      this.registerSubCommand(new SetNick(this));
      this.registerSubCommand(new Give(this));
      this.registerSubCommand(new GiveAll(this));
      this.registerSubCommand(new Drop(this));
      this.registerSubCommand(new Take(this));

      try {
         this.registerSubCommand(new Sell(this));
         this.registerSubCommand(new Buy(this));
         this.registerSubCommand(new SellMax(this));
         this.registerSubCommand(new BuyMax(this));
         ItemEdit.get().log("Hooking into Vault");
      } catch (IllegalStateException | NoClassDefFoundError var2) {
         ItemEdit.get().log("Unable to hook into Vault");
      }

   }

   public static ServerItemCommand get() {
      return instance;
   }
}
