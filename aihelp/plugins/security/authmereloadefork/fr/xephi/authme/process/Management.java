package fr.xephi.authme.process;

import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.process.changepassword.AsyncChangePassword;
import fr.xephi.authme.process.email.AsyncAddEmail;
import fr.xephi.authme.process.email.AsyncChangeEmail;
import fr.xephi.authme.process.join.AsynchronousJoin;
import fr.xephi.authme.process.login.AsynchronousLogin;
import fr.xephi.authme.process.logout.AsynchronousLogout;
import fr.xephi.authme.process.quit.AsynchronousQuit;
import fr.xephi.authme.process.register.AsyncRegister;
import fr.xephi.authme.process.register.executors.RegistrationMethod;
import fr.xephi.authme.process.register.executors.RegistrationParameters;
import fr.xephi.authme.process.unregister.AsynchronousUnregister;
import fr.xephi.authme.service.BukkitService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Management {
   @Inject
   private BukkitService bukkitService;
   @Inject
   private AsyncAddEmail asyncAddEmail;
   @Inject
   private AsyncChangeEmail asyncChangeEmail;
   @Inject
   private AsynchronousLogout asynchronousLogout;
   @Inject
   private AsynchronousQuit asynchronousQuit;
   @Inject
   private AsynchronousJoin asynchronousJoin;
   @Inject
   private AsyncRegister asyncRegister;
   @Inject
   private AsynchronousLogin asynchronousLogin;
   @Inject
   private AsynchronousUnregister asynchronousUnregister;
   @Inject
   private AsyncChangePassword asyncChangePassword;

   Management() {
   }

   public void performLogin(Player player, String password) {
      this.runTask(() -> {
         this.asynchronousLogin.login(player, password);
      });
   }

   public void forceLogin(Player player) {
      this.runTask(() -> {
         this.asynchronousLogin.forceLogin(player);
      });
   }

   public void forceLogin(Player player, boolean quiet) {
      this.runTask(() -> {
         this.asynchronousLogin.forceLogin(player, quiet);
      });
   }

   public void performLogout(Player player) {
      this.runTask(() -> {
         this.asynchronousLogout.logout(player);
      });
   }

   public <P extends RegistrationParameters> void performRegister(RegistrationMethod<P> variant, P parameters) {
      this.runTask(() -> {
         this.asyncRegister.register(variant, parameters);
      });
   }

   public void performUnregister(Player player, String password) {
      this.runTask(() -> {
         this.asynchronousUnregister.unregister(player, password);
      });
   }

   public void performUnregisterByAdmin(CommandSender initiator, String name, Player player) {
      this.runTask(() -> {
         this.asynchronousUnregister.adminUnregister(initiator, name, player);
      });
   }

   public void performJoin(Player player) {
      this.runTask(() -> {
         this.asynchronousJoin.processJoin(player);
      });
   }

   public void performQuit(Player player) {
      this.runTask(() -> {
         this.asynchronousQuit.processQuit(player);
      });
   }

   public void performAddEmail(Player player, String newEmail) {
      this.runTask(() -> {
         this.asyncAddEmail.addEmail(player, newEmail);
      });
   }

   public void performChangeEmail(Player player, String oldEmail, String newEmail) {
      this.runTask(() -> {
         this.asyncChangeEmail.changeEmail(player, oldEmail, newEmail);
      });
   }

   public void performPasswordChange(Player player, String oldPassword, String newPassword) {
      this.runTask(() -> {
         this.asyncChangePassword.changePassword(player, oldPassword, newPassword);
      });
   }

   public void performPasswordChangeAsAdmin(CommandSender sender, String playerName, String newPassword) {
      this.runTask(() -> {
         this.asyncChangePassword.changePasswordAsAdmin(sender, playerName, newPassword);
      });
   }

   private void runTask(Runnable runnable) {
      this.bukkitService.runTaskOptionallyAsync(runnable);
   }
}
