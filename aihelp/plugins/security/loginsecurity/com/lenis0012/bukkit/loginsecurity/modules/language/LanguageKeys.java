package com.lenis0012.bukkit.loginsecurity.modules.language;

public enum LanguageKeys {
   GENERAL_NOT_AUTHENTICATED("generalNotAuthenticated"),
   GENERAL_NOT_LOGGED_IN("generalNotLoggedIn"),
   GENERAL_PASSWORD_LENGTH("generalPasswordLength"),
   GENERAL_UNKNOWN_HASH("generalUnknownHash"),
   LOGIN_TRIES_EXCEEDED("loginTriesExceeded"),
   LOGIN_FAIL("loginFail"),
   LOGIN_SUCCESS("loginSuccess"),
   CHANGE_FAIL("changeFail"),
   CHANGE_SUCCESS("changeSuccess"),
   LOGOUT_FAIL("logoutFail"),
   LOGOUT_SUCCESS("logoutSuccess"),
   REGISTER_ALREADY("registerAlready"),
   REGISTER_CAPTCHA("registerCaptcha"),
   REGISTER_SUCCESS("registerSuccess"),
   UNREGISTER_SUCCESS("unregisterSuccess"),
   UNREGISTER_FAIL("unregisterFail"),
   UNREGISTER_NOT_POSSIBLE("unregisterNotPossible"),
   COMMAND_ERROR("commandError"),
   COMMAND_UNKNOWN("commandUnknown"),
   COMMAND_NOT_ENOUGH_ARGS("commandArguments"),
   LAC_HELP("lacHelp"),
   LAC_RMPASS("lacRmpass"),
   LAC_RMPASS_ARGS("lacRmpassArgs"),
   LAC_NOT_REGISTERED("lacNotRegistered"),
   LAC_RESET_PLAYER("lacResetPlayer"),
   LAC_CHANGED_PASSWORD("lacChangedPassword"),
   LAC_IMPORT("lacImport"),
   LAC_UNKNOWN_SOURCE("lacUnknownSource"),
   LAC_IMPORT_FAILED("lacImportFailed"),
   LAC_RELOAD_SUCCESS("lacReloadSuccess"),
   SESSION_CONTINUE("sessionContinue"),
   MESSAGE_LOGIN("messageLogin"),
   MESSAGE_REGISTER("messageRegister"),
   MESSAGE_REGISTER2("messageRegister2"),
   ERROR_REFRESH_PROFILE("errorRefreshProfile"),
   ERROR_NOT_REGISTERED("errorNotRegistered"),
   ERROR_MATCH_PASSWORD("errorMatchPassword"),
   KICK_ALREADY_ONLINE("kickAlreadyOnline"),
   KICK_USERNAME_CHARS("kickUsernameChars"),
   KICK_USERNAME_LENGTH("kickUsernameLength"),
   KICK_TIME_OUT("kickTimeOut"),
   KICK_USERNAME_REGISTERED("kickUsernameRegistered");

   private final String value;

   private LanguageKeys(String value) {
      this.value = value;
   }

   public String toString() {
      return this.value;
   }

   // $FF: synthetic method
   private static LanguageKeys[] $values() {
      return new LanguageKeys[]{GENERAL_NOT_AUTHENTICATED, GENERAL_NOT_LOGGED_IN, GENERAL_PASSWORD_LENGTH, GENERAL_UNKNOWN_HASH, LOGIN_TRIES_EXCEEDED, LOGIN_FAIL, LOGIN_SUCCESS, CHANGE_FAIL, CHANGE_SUCCESS, LOGOUT_FAIL, LOGOUT_SUCCESS, REGISTER_ALREADY, REGISTER_CAPTCHA, REGISTER_SUCCESS, UNREGISTER_SUCCESS, UNREGISTER_FAIL, UNREGISTER_NOT_POSSIBLE, COMMAND_ERROR, COMMAND_UNKNOWN, COMMAND_NOT_ENOUGH_ARGS, LAC_HELP, LAC_RMPASS, LAC_RMPASS_ARGS, LAC_NOT_REGISTERED, LAC_RESET_PLAYER, LAC_CHANGED_PASSWORD, LAC_IMPORT, LAC_UNKNOWN_SOURCE, LAC_IMPORT_FAILED, LAC_RELOAD_SUCCESS, SESSION_CONTINUE, MESSAGE_LOGIN, MESSAGE_REGISTER, MESSAGE_REGISTER2, ERROR_REFRESH_PROFILE, ERROR_NOT_REGISTERED, ERROR_MATCH_PASSWORD, KICK_ALREADY_ONLINE, KICK_USERNAME_CHARS, KICK_USERNAME_LENGTH, KICK_TIME_OUT, KICK_USERNAME_REGISTERED};
   }
}
