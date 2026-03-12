package fr.xephi.authme.settings.commandconfig;

import java.util.Optional;

public class OnLoginCommand extends Command {
   private Optional<Integer> ifNumberOfAccountsAtLeast = Optional.empty();
   private Optional<Integer> ifNumberOfAccountsLessThan = Optional.empty();

   public OnLoginCommand copyWithCommand(String command) {
      OnLoginCommand copy = new OnLoginCommand();
      this.setValuesToCopyWithNewCommand(copy, command);
      copy.ifNumberOfAccountsAtLeast = this.ifNumberOfAccountsAtLeast;
      copy.ifNumberOfAccountsLessThan = this.ifNumberOfAccountsLessThan;
      return copy;
   }

   public Optional<Integer> getIfNumberOfAccountsAtLeast() {
      return this.ifNumberOfAccountsAtLeast;
   }

   public void setIfNumberOfAccountsAtLeast(Optional<Integer> ifNumberOfAccountsAtLeast) {
      this.ifNumberOfAccountsAtLeast = ifNumberOfAccountsAtLeast;
   }

   public Optional<Integer> getIfNumberOfAccountsLessThan() {
      return this.ifNumberOfAccountsLessThan;
   }

   public void setIfNumberOfAccountsLessThan(Optional<Integer> ifNumberOfAccountsLessThan) {
      this.ifNumberOfAccountsLessThan = ifNumberOfAccountsLessThan;
   }
}
