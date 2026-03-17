package me.PM2.infinitevehicles.commands;

import java.util.ArrayList;
import java.util.List;

public class ShowCommandHelp extends InvalidCommandArgument {
   List<String> searchArgs;
   boolean search;

   public ShowCommandHelp() {
      this.searchArgs = null;
      this.search = false;
   }

   public ShowCommandHelp(boolean search) {
      this.searchArgs = null;
      this.search = false;
      this.search = var1;
   }

   public ShowCommandHelp(List<String> args) {
      this(true);
      this.searchArgs = new ArrayList(var1);
   }
}
