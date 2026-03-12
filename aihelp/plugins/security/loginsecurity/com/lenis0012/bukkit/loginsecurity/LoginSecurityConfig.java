package com.lenis0012.bukkit.loginsecurity;

import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.AbstractConfig;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.ConfigurationModule;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.mapping.ConfigHeader;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.mapping.ConfigKey;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.mapping.ConfigMapper;

@ConfigMapper(
   fileName = "config.yml",
   header = {"LoginSecurity configuration.", "Some information is provided in the form of comments", "For more info visit https://github.com/lenis0012/LoginSecurity-2/wiki/Configuration"}
)
public class LoginSecurityConfig extends AbstractConfig {
   @ConfigKey(
      path = "register.required"
   )
   private boolean passwordRequired = true;
   @ConfigHeader({"When enabled, users need to enter a captcha upon registration."})
   @ConfigKey(
      path = "register.captcha"
   )
   private boolean registerCaptcha = false;
   @ConfigHeader({"When enabled, requires users to enter their password twice upon registration."})
   @ConfigKey(
      path = "register.confirm-password"
   )
   private boolean registerConfirmPassword = false;
   @ConfigKey(
      path = "login.max-tries"
   )
   private int maxLoginTries = 5;
   @ConfigHeader({"Only allow registered players to join using exactly the same name as registered."})
   @ConfigKey(
      path = "login.username-match-exact"
   )
   private boolean matchUsernameExact = true;
   @ConfigKey(
      path = "password.min-length"
   )
   private int passwordMinLength = 6;
   @ConfigKey(
      path = "password-max-length"
   )
   private int passwordMaxLength = 32;
   @ConfigHeader({"When enabled, player gets a blindness effect when not logged in."})
   @ConfigKey(
      path = "join.blindness"
   )
   private boolean blindness = true;
   @ConfigHeader({"Temporarily login location until player has logged in.", "Available options: DEFAULT, SPAWN"})
   @ConfigKey(
      path = "join.location"
   )
   private String location = "DEFAULT";
   @ConfigHeader({"Safely hides the player's inventory until the player is logged in", "This required ProtocolLib to be installed"})
   @ConfigKey(
      path = "join.hide-inventory-safe"
   )
   private boolean hideInventory = false;
   @ConfigHeader({"Remove special characters like @ and # from the username.", "Disabling this can be a security risk!"})
   @ConfigKey(
      path = "username.filter-special-chars"
   )
   private boolean filterSpecialChars = true;
   @ConfigKey(
      path = "username.min-length"
   )
   private int usernameMinLength = 3;
   @ConfigKey(
      path = "username.max-length"
   )
   private int usernameMaxLength = 16;
   @ConfigHeader({"Whether to accept new registrations."})
   @ConfigKey(
      path = "registration.enabled"
   )
   private boolean registrationEnabled = true;
   @ConfigKey(
      path = "registration.disabled-message"
   )
   private String registrationDisabledMessage = "&cRegistrations are currently disabled!";
   @ConfigHeader(
      path = "command-shortcut",
      value = {"Shortcut that can be used as alternative to login/register command. Does not replace the defaults"}
   )
   @ConfigKey(
      path = "command-shortcut.enabled"
   )
   private boolean useCommandShortcut = false;
   @ConfigKey(
      path = "command-shortcut.login"
   )
   private String loginCommandShortcut = "/l";
   @ConfigKey(
      path = "command-shortcut.register"
   )
   private String registerCommandShortcut = "/reg";
   @ConfigKey(
      path = "updater.enabled"
   )
   private boolean updaterEnabled = true;
   @ConfigHeader({"The type of builds you are checking. RELEASE, BETA, ALPHA"})
   @ConfigKey(
      path = "updater.channel"
   )
   private String updaterChannel = "BETA";
   @ConfigHeader({"Session timeout in seconds, set to -1 to disable."})
   @ConfigKey
   private int sessionTimeout = 60;
   @ConfigHeader({"Login timeout in seconds, set to -1 to disable."})
   @ConfigKey
   private int loginTimeout = 120;
   @ConfigHeader({"Login/register message delay in seconds."})
   @ConfigKey
   private int loginMessageDelay = 10;
   @ConfigHeader({"Language for messages, check wiki for more info.", "List: https://github.com/lenis0012/Translations", "This setting should be set tot he file name without .json"})
   @ConfigKey
   private String language = "en_us";

   public LoginSecurityConfig(ConfigurationModule module) {
      super(module);
   }

   public boolean isPasswordRequired() {
      return this.passwordRequired;
   }

   public boolean isRegisterCaptcha() {
      return this.registerCaptcha;
   }

   public boolean isRegisterConfirmPassword() {
      return this.registerConfirmPassword;
   }

   public int getMaxLoginTries() {
      return this.maxLoginTries;
   }

   public boolean isMatchUsernameExact() {
      return this.matchUsernameExact;
   }

   public int getPasswordMinLength() {
      return this.passwordMinLength;
   }

   public int getPasswordMaxLength() {
      return this.passwordMaxLength;
   }

   public boolean isBlindness() {
      return this.blindness;
   }

   public String getLocation() {
      return this.location;
   }

   public boolean isHideInventory() {
      return this.hideInventory;
   }

   public boolean isFilterSpecialChars() {
      return this.filterSpecialChars;
   }

   public int getUsernameMinLength() {
      return this.usernameMinLength;
   }

   public int getUsernameMaxLength() {
      return this.usernameMaxLength;
   }

   public boolean isRegistrationEnabled() {
      return this.registrationEnabled;
   }

   public String getRegistrationDisabledMessage() {
      return this.registrationDisabledMessage;
   }

   public boolean isUseCommandShortcut() {
      return this.useCommandShortcut;
   }

   public String getLoginCommandShortcut() {
      return this.loginCommandShortcut;
   }

   public String getRegisterCommandShortcut() {
      return this.registerCommandShortcut;
   }

   public boolean isUpdaterEnabled() {
      return this.updaterEnabled;
   }

   public String getUpdaterChannel() {
      return this.updaterChannel;
   }

   public int getSessionTimeout() {
      return this.sessionTimeout;
   }

   public int getLoginTimeout() {
      return this.loginTimeout;
   }

   public int getLoginMessageDelay() {
      return this.loginMessageDelay;
   }

   public String getLanguage() {
      return this.language;
   }
}
