package fr.xephi.authme.settings.commandconfig;

public class Command {
   private String command;
   private Executor executor;
   private long delay;

   public Command() {
      this.executor = Executor.PLAYER;
      this.delay = 0L;
   }

   public Command copyWithCommand(String command) {
      Command copy = new Command();
      this.setValuesToCopyWithNewCommand(copy, command);
      return copy;
   }

   protected void setValuesToCopyWithNewCommand(Command copy, String newCommand) {
      copy.command = newCommand;
      copy.executor = this.executor;
      copy.delay = this.delay;
   }

   public String getCommand() {
      return this.command;
   }

   public void setCommand(String command) {
      this.command = command;
   }

   public Executor getExecutor() {
      return this.executor;
   }

   public void setExecutor(Executor executor) {
      this.executor = executor;
   }

   public long getDelay() {
      return this.delay;
   }

   public void setDelay(long delay) {
      this.delay = delay;
   }

   public String toString() {
      return this.command + " (" + this.executor + ")";
   }
}
