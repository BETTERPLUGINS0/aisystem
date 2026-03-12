package fr.xephi.authme.command;

import fr.xephi.authme.command.executable.HelpCommand;
import fr.xephi.authme.command.executable.authme.AccountsCommand;
import fr.xephi.authme.command.executable.authme.AuthMeCommand;
import fr.xephi.authme.command.executable.authme.BackupCommand;
import fr.xephi.authme.command.executable.authme.ChangePasswordAdminCommand;
import fr.xephi.authme.command.executable.authme.ConverterCommand;
import fr.xephi.authme.command.executable.authme.FirstSpawnCommand;
import fr.xephi.authme.command.executable.authme.ForceLoginCommand;
import fr.xephi.authme.command.executable.authme.GetEmailCommand;
import fr.xephi.authme.command.executable.authme.GetIpCommand;
import fr.xephi.authme.command.executable.authme.LastLoginCommand;
import fr.xephi.authme.command.executable.authme.PurgeBannedPlayersCommand;
import fr.xephi.authme.command.executable.authme.PurgeCommand;
import fr.xephi.authme.command.executable.authme.PurgeLastPositionCommand;
import fr.xephi.authme.command.executable.authme.PurgePlayerCommand;
import fr.xephi.authme.command.executable.authme.RecentPlayersCommand;
import fr.xephi.authme.command.executable.authme.RegisterAdminCommand;
import fr.xephi.authme.command.executable.authme.ReloadCommand;
import fr.xephi.authme.command.executable.authme.SetEmailCommand;
import fr.xephi.authme.command.executable.authme.SetFirstSpawnCommand;
import fr.xephi.authme.command.executable.authme.SetSpawnCommand;
import fr.xephi.authme.command.executable.authme.SpawnCommand;
import fr.xephi.authme.command.executable.authme.SwitchAntiBotCommand;
import fr.xephi.authme.command.executable.authme.TotpDisableAdminCommand;
import fr.xephi.authme.command.executable.authme.TotpViewStatusCommand;
import fr.xephi.authme.command.executable.authme.UnregisterAdminCommand;
import fr.xephi.authme.command.executable.authme.UpdateHelpMessagesCommand;
import fr.xephi.authme.command.executable.authme.VersionCommand;
import fr.xephi.authme.command.executable.authme.debug.DebugCommand;
import fr.xephi.authme.command.executable.captcha.CaptchaCommand;
import fr.xephi.authme.command.executable.changepassword.ChangePasswordCommand;
import fr.xephi.authme.command.executable.email.AddEmailCommand;
import fr.xephi.authme.command.executable.email.ChangeEmailCommand;
import fr.xephi.authme.command.executable.email.EmailBaseCommand;
import fr.xephi.authme.command.executable.email.EmailSetPasswordCommand;
import fr.xephi.authme.command.executable.email.ProcessCodeCommand;
import fr.xephi.authme.command.executable.email.RecoverEmailCommand;
import fr.xephi.authme.command.executable.email.ShowEmailCommand;
import fr.xephi.authme.command.executable.login.LoginCommand;
import fr.xephi.authme.command.executable.logout.LogoutCommand;
import fr.xephi.authme.command.executable.register.RegisterCommand;
import fr.xephi.authme.command.executable.totp.AddTotpCommand;
import fr.xephi.authme.command.executable.totp.ConfirmTotpCommand;
import fr.xephi.authme.command.executable.totp.RemoveTotpCommand;
import fr.xephi.authme.command.executable.totp.TotpBaseCommand;
import fr.xephi.authme.command.executable.totp.TotpCodeCommand;
import fr.xephi.authme.command.executable.unregister.UnregisterCommand;
import fr.xephi.authme.command.executable.verification.VerificationCommand;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.permission.AdminPermission;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.PlayerPermission;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CommandInitializer {
   private static final boolean OPTIONAL = true;
   private static final boolean MANDATORY = false;
   private List<CommandDescription> commands;

   public CommandInitializer() {
      this.buildCommands();
   }

   public List<CommandDescription> getCommands() {
      return this.commands;
   }

   private void buildCommands() {
      CommandDescription authMeBase = this.buildAuthMeBaseCommand();
      CommandDescription emailBase = this.buildEmailBaseCommand();
      CommandDescription loginBase = CommandDescription.builder().parent((CommandDescription)null).labels("login", "l", "log").description("Login command").detailedDescription("Command to log in using AuthMeReloaded.").withArgument("password", "Login password", false).permission(PlayerPermission.LOGIN).executableCommand(LoginCommand.class).register();
      CommandDescription logoutBase = CommandDescription.builder().parent((CommandDescription)null).labels("logout").description("Logout command").detailedDescription("Command to logout using AuthMeReloaded.").permission(PlayerPermission.LOGOUT).executableCommand(LogoutCommand.class).register();
      CommandDescription registerBase = CommandDescription.builder().parent((CommandDescription)null).labels("register", "reg").description("Register an account").detailedDescription("Command to register using AuthMeReloaded.").withArgument("password", "Password", true).withArgument("verifyPassword", "Verify password", true).permission(PlayerPermission.REGISTER).executableCommand(RegisterCommand.class).register();
      CommandDescription unregisterBase = CommandDescription.builder().parent((CommandDescription)null).labels("unregister", "unreg").description("Unregister an account").detailedDescription("Command to unregister using AuthMeReloaded.").withArgument("password", "Password", false).permission(PlayerPermission.UNREGISTER).executableCommand(UnregisterCommand.class).register();
      CommandDescription changePasswordBase = CommandDescription.builder().parent((CommandDescription)null).labels("changepassword", "changepass", "cp").description("Change password of an account").detailedDescription("Command to change your password using AuthMeReloaded.").withArgument("oldPassword", "Old password", false).withArgument("newPassword", "New password", false).permission(PlayerPermission.CHANGE_PASSWORD).executableCommand(ChangePasswordCommand.class).register();
      CommandDescription totpBase = this.buildTotpBaseCommand();
      CommandDescription captchaBase = CommandDescription.builder().parent((CommandDescription)null).labels("captcha").description("Captcha command").detailedDescription("Captcha command for AuthMeReloaded.").withArgument("captcha", "The Captcha", false).permission(PlayerPermission.CAPTCHA).executableCommand(CaptchaCommand.class).register();
      CommandDescription verificationBase = CommandDescription.builder().parent((CommandDescription)null).labels("verification").description("Verification command").detailedDescription("Command to complete the verification process for AuthMeReloaded.").withArgument("code", "The code", false).permission(PlayerPermission.VERIFICATION_CODE).executableCommand(VerificationCommand.class).register();
      List<CommandDescription> baseCommands = ImmutableList.of(authMeBase, emailBase, loginBase, logoutBase, registerBase, unregisterBase, changePasswordBase, totpBase, captchaBase, verificationBase);
      this.setHelpOnAllBases(baseCommands);
      this.commands = baseCommands;
   }

   private CommandDescription buildAuthMeBaseCommand() {
      CommandDescription authmeBase = CommandDescription.builder().labels("authme").description("AuthMe op commands").detailedDescription("The main AuthMeReloaded command. The root for all admin commands.").executableCommand(AuthMeCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("register", "reg", "r").description("Register a player").detailedDescription("Register the specified player with the specified password.").withArgument("player", "Player name", false).withArgument("password", "Password", false).permission(AdminPermission.REGISTER).executableCommand(RegisterAdminCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("unregister", "unreg", "unr").description("Unregister a player").detailedDescription("Unregister the specified player.").withArgument("player", "Player name", false).permission(AdminPermission.UNREGISTER).executableCommand(UnregisterAdminCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("forcelogin", "login").description("Enforce login player").detailedDescription("Enforce the specified player to login.").withArgument("player", "Online player name", true).permission(AdminPermission.FORCE_LOGIN).executableCommand(ForceLoginCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("password", "changepassword", "changepass", "cp").description("Change a player's password").detailedDescription("Change the password of a player.").withArgument("player", "Player name", false).withArgument("pwd", "New password", false).permission(AdminPermission.CHANGE_PASSWORD).executableCommand(ChangePasswordAdminCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("lastlogin", "ll").description("Player's last login").detailedDescription("View the date of the specified players last login.").withArgument("player", "Player name", true).permission(AdminPermission.LAST_LOGIN).executableCommand(LastLoginCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("accounts", "account").description("Display player accounts").detailedDescription("Display all accounts of a player by his player name or IP.").withArgument("player", "Player name or IP", true).permission(AdminPermission.ACCOUNTS).executableCommand(AccountsCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("email", "mail", "getemail", "getmail").description("Display player's email").detailedDescription("Display the email address of the specified player if set.").withArgument("player", "Player name", true).permission(AdminPermission.GET_EMAIL).executableCommand(GetEmailCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("setemail", "setmail", "chgemail", "chgmail").description("Change player's email").detailedDescription("Change the email address of the specified player.").withArgument("player", "Player name", false).withArgument("email", "Player email", false).permission(AdminPermission.CHANGE_EMAIL).executableCommand(SetEmailCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("getip", "ip").description("Get player's IP").detailedDescription("Get the IP address of the specified online player.").withArgument("player", "Player name", false).permission(AdminPermission.GET_IP).executableCommand(GetIpCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("totp", "2fa").description("See if a player has enabled TOTP").detailedDescription("Returns whether the specified player has enabled two-factor authentication.").withArgument("player", "Player name", false).permission(AdminPermission.VIEW_TOTP_STATUS).executableCommand(TotpViewStatusCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("disabletotp", "disable2fa", "deletetotp", "delete2fa").description("Delete TOTP token of a player").detailedDescription("Disable two-factor authentication for a player.").withArgument("player", "Player name", false).permission(AdminPermission.DISABLE_TOTP).executableCommand(TotpDisableAdminCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("spawn", "home").description("Teleport to spawn").detailedDescription("Teleport to the spawn.").permission(AdminPermission.SPAWN).executableCommand(SpawnCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("setspawn", "chgspawn").description("Change the spawn").detailedDescription("Change the player's spawn to your current position.").permission(AdminPermission.SET_SPAWN).executableCommand(SetSpawnCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("firstspawn", "firsthome").description("Teleport to first spawn").detailedDescription("Teleport to the first spawn.").permission(AdminPermission.FIRST_SPAWN).executableCommand(FirstSpawnCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("setfirstspawn", "chgfirstspawn").description("Change the first spawn").detailedDescription("Change the first player's spawn to your current position.").permission(AdminPermission.SET_FIRST_SPAWN).executableCommand(SetFirstSpawnCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("purge", "delete").description("Purge old data").detailedDescription("Purge old AuthMeReloaded data longer than the specified number of days ago.").withArgument("days", "Number of days", false).permission(AdminPermission.PURGE).executableCommand(PurgeCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("purgeplayer").description("Purges the data of one player").detailedDescription("Purges data of the given player.").withArgument("player", "The player to purge", false).withArgument("options", "'force' to run without checking if player is registered", true).permission(AdminPermission.PURGE_PLAYER).executableCommand(PurgePlayerCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("backup").description("Perform a backup").detailedDescription("Creates a backup of the registered users.").permission(AdminPermission.BACKUP).executableCommand(BackupCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("resetpos", "purgelastposition", "purgelastpos", "resetposition", "resetlastposition", "resetlastpos").description("Purge player's last position").detailedDescription("Purge the last know position of the specified player or all of them.").withArgument("player/*", "Player name or * for all players", false).permission(AdminPermission.PURGE_LAST_POSITION).executableCommand(PurgeLastPositionCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("purgebannedplayers", "purgebannedplayer", "deletebannedplayers", "deletebannedplayer").description("Purge banned players data").detailedDescription("Purge all AuthMeReloaded data for banned players.").permission(AdminPermission.PURGE_BANNED_PLAYERS).executableCommand(PurgeBannedPlayersCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("switchantibot", "toggleantibot", "antibot").description("Switch AntiBot mode").detailedDescription("Switch or toggle the AntiBot mode to the specified state.").withArgument("mode", "ON / OFF", true).permission(AdminPermission.SWITCH_ANTIBOT).executableCommand(SwitchAntiBotCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("reload", "rld").description("Reload plugin").detailedDescription("Reload the AuthMeReloaded plugin.").permission(AdminPermission.RELOAD).executableCommand(ReloadCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("version", "ver", "v", "about", "info").description("Version info").detailedDescription("Show detailed information about the installed AuthMeReloaded version, the developers, contributors, and license.").executableCommand(VersionCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("converter", "convert", "conv").description("Converter command").detailedDescription("Converter command for AuthMeReloaded.").withArgument("job", "Conversion job: xauth / crazylogin / rakamak / royalauth / vauth / sqliteToSql / mysqlToSqlite / loginsecurity", true).permission(AdminPermission.CONVERTER).executableCommand(ConverterCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("messages", "msg").description("Add missing help messages").detailedDescription("Adds missing texts to the current help messages file.").permission(AdminPermission.UPDATE_MESSAGES).executableCommand(UpdateHelpMessagesCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("recent").description("See players who have recently logged in").detailedDescription("Shows the last players that have logged in.").permission(AdminPermission.SEE_RECENT_PLAYERS).executableCommand(RecentPlayersCommand.class).register();
      CommandDescription.builder().parent(authmeBase).labels("debug", "dbg").description("Debug features").detailedDescription("Allows various operations for debugging.").withArgument("child", "The child to execute", true).withArgument("arg", "argument (depends on debug section)", true).withArgument("arg", "argument (depends on debug section)", true).permission(DebugSectionPermissions.DEBUG_COMMAND).executableCommand(DebugCommand.class).register();
      return authmeBase;
   }

   private CommandDescription buildEmailBaseCommand() {
      CommandDescription emailBase = CommandDescription.builder().parent((CommandDescription)null).labels("email").description("Add email or recover password").detailedDescription("The AuthMeReloaded email command base.").executableCommand(EmailBaseCommand.class).register();
      CommandDescription.builder().parent(emailBase).labels("show", "myemail").description("Show Email").detailedDescription("Show your current email address.").permission(PlayerPermission.SEE_EMAIL).executableCommand(ShowEmailCommand.class).register();
      CommandDescription.builder().parent(emailBase).labels("add", "addemail", "addmail").description("Add Email").detailedDescription("Add a new email address to your account.").withArgument("email", "Email address", false).withArgument("verifyEmail", "Email address verification", false).permission(PlayerPermission.ADD_EMAIL).executableCommand(AddEmailCommand.class).register();
      CommandDescription.builder().parent(emailBase).labels("change", "changeemail", "changemail").description("Change Email").detailedDescription("Change an email address of your account.").withArgument("oldEmail", "Old email address", false).withArgument("newEmail", "New email address", false).permission(PlayerPermission.CHANGE_EMAIL).executableCommand(ChangeEmailCommand.class).register();
      CommandDescription.builder().parent(emailBase).labels("recover", "recovery", "recoveremail", "recovermail").description("Recover password using email").detailedDescription("Recover your account using an Email address by sending a mail containing a new password.").withArgument("email", "Email address", false).permission(PlayerPermission.RECOVER_EMAIL).executableCommand(RecoverEmailCommand.class).register();
      CommandDescription.builder().parent(emailBase).labels("code").description("Submit code to recover password").detailedDescription("Recover your account by submitting a code delivered to your email.").withArgument("code", "Recovery code", false).permission(PlayerPermission.RECOVER_EMAIL).executableCommand(ProcessCodeCommand.class).register();
      CommandDescription.builder().parent(emailBase).labels("setpassword").description("Set new password after recovery").detailedDescription("Set a new password after successfully recovering your account.").withArgument("password", "New password", false).permission(PlayerPermission.RECOVER_EMAIL).executableCommand(EmailSetPasswordCommand.class).register();
      return emailBase;
   }

   private CommandDescription buildTotpBaseCommand() {
      CommandDescription totpBase = CommandDescription.builder().parent((CommandDescription)null).labels("totp", "2fa").description("TOTP commands").detailedDescription("Performs actions related to two-factor authentication.").executableCommand(TotpBaseCommand.class).register();
      CommandDescription.builder().parent(totpBase).labels("code", "c").description("Command for logging in").detailedDescription("Processes the two-factor authentication code during login.").withArgument("code", "The TOTP code to use to log in", false).executableCommand(TotpCodeCommand.class).register();
      CommandDescription.builder().parent(totpBase).labels("add").description("Enables TOTP").detailedDescription("Enables two-factor authentication for your account.").permission(PlayerPermission.ENABLE_TWO_FACTOR_AUTH).executableCommand(AddTotpCommand.class).register();
      CommandDescription.builder().parent(totpBase).labels("confirm").description("Enables TOTP after successful code").detailedDescription("Saves the generated TOTP secret after confirmation.").withArgument("code", "Code from the given secret from /totp add", false).permission(PlayerPermission.ENABLE_TWO_FACTOR_AUTH).executableCommand(ConfirmTotpCommand.class).register();
      CommandDescription.builder().parent(totpBase).labels("remove").description("Removes TOTP").detailedDescription("Disables two-factor authentication for your account.").withArgument("code", "Current 2FA code", false).permission(PlayerPermission.DISABLE_TWO_FACTOR_AUTH).executableCommand(RemoveTotpCommand.class).register();
      return totpBase;
   }

   private void setHelpOnAllBases(Collection<CommandDescription> commands) {
      List<String> helpCommandLabels = Arrays.asList("help", "hlp", "h", "sos", "?");
      Iterator var3 = commands.iterator();

      while(var3.hasNext()) {
         CommandDescription base = (CommandDescription)var3.next();
         CommandDescription.builder().parent(base).labels(helpCommandLabels).description("View help").detailedDescription("View detailed help for /" + (String)base.getLabels().get(0) + " commands.").withArgument("query", "The command or query to view help for.", true).executableCommand(HelpCommand.class).register();
      }

   }
}
