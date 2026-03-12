package fr.xephi.authme.message;

public enum MessageKey {
   DOUBLE_LOGIN_FIX("double_login_fix.fix_message", new String[0]),
   LOCATION_FIX_PORTAL("login_location_fix.fix_portal", new String[0]),
   LOCATION_FIX_UNDERGROUND("login_location_fix.fix_underground", new String[0]),
   LOCATION_FIX_UNDERGROUND_CANT_FIX("login_location_fix.cannot_fix_underground", new String[0]),
   BEDROCK_AUTO_LOGGED_IN("bedrock_auto_login.success", new String[0]),
   DENIED_COMMAND("error.denied_command", new String[0]),
   SAME_IP_ONLINE("on_join_validation.same_ip_online", new String[0]),
   DENIED_CHAT("error.denied_chat", new String[0]),
   KICK_ANTIBOT("antibot.kick_antibot", new String[0]),
   UNKNOWN_USER("error.unregistered_user", new String[0]),
   NOT_LOGGED_IN("error.not_logged_in", new String[0]),
   USAGE_LOGIN("login.command_usage", new String[0]),
   WRONG_PASSWORD("login.wrong_password", new String[0]),
   UNREGISTERED_SUCCESS("unregister.success", new String[0]),
   REGISTRATION_DISABLED("registration.disabled", new String[0]),
   SESSION_RECONNECTION("session.valid_session", new String[0]),
   LOGIN_SUCCESS("login.success", new String[0]),
   ACCOUNT_NOT_ACTIVATED("misc.account_not_activated", new String[0]),
   NAME_ALREADY_REGISTERED("registration.name_taken", new String[0]),
   NO_PERMISSION("error.no_permission", new String[0]),
   ERROR("error.unexpected_error", new String[0]),
   LOGIN_MESSAGE("login.login_request", new String[0]),
   REGISTER_MESSAGE("registration.register_request", new String[0]),
   MAX_REGISTER_EXCEEDED("error.max_registration", new String[]{"%max_acc", "%reg_count", "%reg_names"}),
   USAGE_REGISTER("registration.command_usage", new String[0]),
   USAGE_UNREGISTER("unregister.command_usage", new String[0]),
   PASSWORD_CHANGED_SUCCESS("misc.password_changed", new String[0]),
   PASSWORD_MATCH_ERROR("password.match_error", new String[0]),
   PASSWORD_IS_USERNAME_ERROR("password.name_in_password", new String[0]),
   PASSWORD_UNSAFE_ERROR("password.unsafe_password", new String[0]),
   PASSWORD_PWNED_ERROR("password.pwned_password", new String[]{"%pwned_count"}),
   PASSWORD_CHARACTERS_ERROR("password.forbidden_characters", new String[]{"%valid_chars"}),
   SESSION_EXPIRED("session.invalid_session", new String[0]),
   MUST_REGISTER_MESSAGE("registration.reg_only", new String[0]),
   ALREADY_LOGGED_IN_ERROR("error.logged_in", new String[0]),
   LOGOUT_SUCCESS("misc.logout", new String[0]),
   USERNAME_ALREADY_ONLINE_ERROR("on_join_validation.same_nick_online", new String[0]),
   REGISTER_SUCCESS("registration.success", new String[0]),
   INVALID_PASSWORD_LENGTH("password.wrong_length", new String[0]),
   CONFIG_RELOAD_SUCCESS("misc.reload", new String[0]),
   LOGIN_TIMEOUT_ERROR("login.timeout_error", new String[0]),
   USAGE_CHANGE_PASSWORD("misc.usage_change_password", new String[0]),
   INVALID_NAME_LENGTH("on_join_validation.name_length", new String[0]),
   INVALID_NAME_CHARACTERS("on_join_validation.characters_in_name", new String[]{"%valid_chars"}),
   ADD_EMAIL_MESSAGE("email.add_email_request", new String[0]),
   FORGOT_PASSWORD_MESSAGE("recovery.forgot_password_hint", new String[0]),
   USAGE_CAPTCHA("captcha.usage_captcha", new String[]{"%captcha_code"}),
   CAPTCHA_WRONG_ERROR("captcha.wrong_captcha", new String[]{"%captcha_code"}),
   CAPTCHA_SUCCESS("captcha.valid_captcha", new String[0]),
   CAPTCHA_FOR_REGISTRATION_REQUIRED("captcha.captcha_for_registration", new String[]{"%captcha_code"}),
   REGISTER_CAPTCHA_SUCCESS("captcha.register_captcha_valid", new String[0]),
   KICK_FOR_VIP("error.kick_for_vip", new String[0]),
   KICK_FULL_SERVER("on_join_validation.kick_full_server", new String[0]),
   KICK_UNRESOLVED_HOSTNAME("error.kick_unresolved_hostname", new String[0]),
   USAGE_ADD_EMAIL("email.usage_email_add", new String[0]),
   USAGE_CHANGE_EMAIL("email.usage_email_change", new String[0]),
   USAGE_RECOVER_EMAIL("recovery.command_usage", new String[0]),
   INVALID_NEW_EMAIL("email.new_email_invalid", new String[0]),
   INVALID_OLD_EMAIL("email.old_email_invalid", new String[0]),
   INVALID_EMAIL("email.invalid", new String[0]),
   EMAIL_ADDED_SUCCESS("email.added", new String[0]),
   EMAIL_ADD_NOT_ALLOWED("email.add_not_allowed", new String[0]),
   CONFIRM_EMAIL_MESSAGE("email.request_confirmation", new String[0]),
   EMAIL_CHANGED_SUCCESS("email.changed", new String[0]),
   EMAIL_CHANGE_NOT_ALLOWED("email.change_not_allowed", new String[0]),
   EMAIL_SHOW("email.email_show", new String[]{"%email"}),
   SHOW_NO_EMAIL("email.no_email_for_account", new String[0]),
   RECOVERY_EMAIL_SENT_MESSAGE("recovery.email_sent", new String[0]),
   COUNTRY_BANNED_ERROR("on_join_validation.country_banned", new String[0]),
   ANTIBOT_AUTO_ENABLED_MESSAGE("antibot.auto_enabled", new String[0]),
   ANTIBOT_AUTO_DISABLED_MESSAGE("antibot.auto_disabled", new String[]{"%m"}),
   EMAIL_ALREADY_USED_ERROR("email.already_used", new String[0]),
   TWO_FACTOR_CREATE("two_factor.code_created", new String[]{"%code", "%url"}),
   TWO_FACTOR_CREATE_CONFIRMATION_REQUIRED("two_factor.confirmation_required", new String[0]),
   TWO_FACTOR_CODE_REQUIRED("two_factor.code_required", new String[0]),
   TWO_FACTOR_ALREADY_ENABLED("two_factor.already_enabled", new String[0]),
   TWO_FACTOR_ENABLE_ERROR_NO_CODE("two_factor.enable_error_no_code", new String[0]),
   TWO_FACTOR_ENABLE_SUCCESS("two_factor.enable_success", new String[0]),
   TWO_FACTOR_ENABLE_ERROR_WRONG_CODE("two_factor.enable_error_wrong_code", new String[0]),
   TWO_FACTOR_NOT_ENABLED_ERROR("two_factor.not_enabled_error", new String[0]),
   TWO_FACTOR_REMOVED_SUCCESS("two_factor.removed_success", new String[0]),
   TWO_FACTOR_INVALID_CODE("two_factor.invalid_code", new String[0]),
   NOT_OWNER_ERROR("on_join_validation.not_owner_error", new String[0]),
   INVALID_NAME_CASE("on_join_validation.invalid_name_case", new String[]{"%valid", "%invalid"}),
   TEMPBAN_MAX_LOGINS("error.tempban_max_logins", new String[0]),
   ACCOUNTS_OWNED_SELF("misc.accounts_owned_self", new String[]{"%count"}),
   ACCOUNTS_OWNED_OTHER("misc.accounts_owned_other", new String[]{"%name", "%count"}),
   KICK_FOR_ADMIN_REGISTER("registration.kicked_admin_registered", new String[0]),
   INCOMPLETE_EMAIL_SETTINGS("email.incomplete_settings", new String[0]),
   EMAIL_SEND_FAILURE("email.send_failure", new String[0]),
   RECOVERY_CODE_SENT("recovery.code.code_sent", new String[0]),
   INCORRECT_RECOVERY_CODE("recovery.code.incorrect", new String[]{"%count"}),
   RECOVERY_TRIES_EXCEEDED("recovery.code.tries_exceeded", new String[0]),
   RECOVERY_CODE_CORRECT("recovery.code.correct", new String[0]),
   RECOVERY_CHANGE_PASSWORD("recovery.code.change_password", new String[0]),
   CHANGE_PASSWORD_EXPIRED("email.change_password_expired", new String[0]),
   EMAIL_COOLDOWN_ERROR("email.email_cooldown_error", new String[]{"%time"}),
   VERIFICATION_CODE_REQUIRED("verification.code_required", new String[0]),
   USAGE_VERIFICATION_CODE("verification.command_usage", new String[0]),
   INCORRECT_VERIFICATION_CODE("verification.incorrect_code", new String[0]),
   VERIFICATION_CODE_VERIFIED("verification.success", new String[0]),
   VERIFICATION_CODE_ALREADY_VERIFIED("verification.already_verified", new String[0]),
   VERIFICATION_CODE_EXPIRED("verification.code_expired", new String[0]),
   VERIFICATION_CODE_EMAIL_NEEDED("verification.email_needed", new String[0]),
   QUICK_COMMAND_PROTECTION_KICK("on_join_validation.quick_command", new String[0]),
   SECOND("time.second", new String[0]),
   SECONDS("time.seconds", new String[0]),
   MINUTE("time.minute", new String[0]),
   MINUTES("time.minutes", new String[0]),
   HOUR("time.hour", new String[0]),
   HOURS("time.hours", new String[0]),
   DAY("time.day", new String[0]),
   DAYS("time.days", new String[0]);

   private String key;
   private String[] tags;

   private MessageKey(String param3, String... param4) {
      this.key = key;
      this.tags = tags;
   }

   public String getKey() {
      return this.key;
   }

   public String[] getTags() {
      return this.tags;
   }

   public String toString() {
      return this.key;
   }

   // $FF: synthetic method
   private static MessageKey[] $values() {
      return new MessageKey[]{DOUBLE_LOGIN_FIX, LOCATION_FIX_PORTAL, LOCATION_FIX_UNDERGROUND, LOCATION_FIX_UNDERGROUND_CANT_FIX, BEDROCK_AUTO_LOGGED_IN, DENIED_COMMAND, SAME_IP_ONLINE, DENIED_CHAT, KICK_ANTIBOT, UNKNOWN_USER, NOT_LOGGED_IN, USAGE_LOGIN, WRONG_PASSWORD, UNREGISTERED_SUCCESS, REGISTRATION_DISABLED, SESSION_RECONNECTION, LOGIN_SUCCESS, ACCOUNT_NOT_ACTIVATED, NAME_ALREADY_REGISTERED, NO_PERMISSION, ERROR, LOGIN_MESSAGE, REGISTER_MESSAGE, MAX_REGISTER_EXCEEDED, USAGE_REGISTER, USAGE_UNREGISTER, PASSWORD_CHANGED_SUCCESS, PASSWORD_MATCH_ERROR, PASSWORD_IS_USERNAME_ERROR, PASSWORD_UNSAFE_ERROR, PASSWORD_PWNED_ERROR, PASSWORD_CHARACTERS_ERROR, SESSION_EXPIRED, MUST_REGISTER_MESSAGE, ALREADY_LOGGED_IN_ERROR, LOGOUT_SUCCESS, USERNAME_ALREADY_ONLINE_ERROR, REGISTER_SUCCESS, INVALID_PASSWORD_LENGTH, CONFIG_RELOAD_SUCCESS, LOGIN_TIMEOUT_ERROR, USAGE_CHANGE_PASSWORD, INVALID_NAME_LENGTH, INVALID_NAME_CHARACTERS, ADD_EMAIL_MESSAGE, FORGOT_PASSWORD_MESSAGE, USAGE_CAPTCHA, CAPTCHA_WRONG_ERROR, CAPTCHA_SUCCESS, CAPTCHA_FOR_REGISTRATION_REQUIRED, REGISTER_CAPTCHA_SUCCESS, KICK_FOR_VIP, KICK_FULL_SERVER, KICK_UNRESOLVED_HOSTNAME, USAGE_ADD_EMAIL, USAGE_CHANGE_EMAIL, USAGE_RECOVER_EMAIL, INVALID_NEW_EMAIL, INVALID_OLD_EMAIL, INVALID_EMAIL, EMAIL_ADDED_SUCCESS, EMAIL_ADD_NOT_ALLOWED, CONFIRM_EMAIL_MESSAGE, EMAIL_CHANGED_SUCCESS, EMAIL_CHANGE_NOT_ALLOWED, EMAIL_SHOW, SHOW_NO_EMAIL, RECOVERY_EMAIL_SENT_MESSAGE, COUNTRY_BANNED_ERROR, ANTIBOT_AUTO_ENABLED_MESSAGE, ANTIBOT_AUTO_DISABLED_MESSAGE, EMAIL_ALREADY_USED_ERROR, TWO_FACTOR_CREATE, TWO_FACTOR_CREATE_CONFIRMATION_REQUIRED, TWO_FACTOR_CODE_REQUIRED, TWO_FACTOR_ALREADY_ENABLED, TWO_FACTOR_ENABLE_ERROR_NO_CODE, TWO_FACTOR_ENABLE_SUCCESS, TWO_FACTOR_ENABLE_ERROR_WRONG_CODE, TWO_FACTOR_NOT_ENABLED_ERROR, TWO_FACTOR_REMOVED_SUCCESS, TWO_FACTOR_INVALID_CODE, NOT_OWNER_ERROR, INVALID_NAME_CASE, TEMPBAN_MAX_LOGINS, ACCOUNTS_OWNED_SELF, ACCOUNTS_OWNED_OTHER, KICK_FOR_ADMIN_REGISTER, INCOMPLETE_EMAIL_SETTINGS, EMAIL_SEND_FAILURE, RECOVERY_CODE_SENT, INCORRECT_RECOVERY_CODE, RECOVERY_TRIES_EXCEEDED, RECOVERY_CODE_CORRECT, RECOVERY_CHANGE_PASSWORD, CHANGE_PASSWORD_EXPIRED, EMAIL_COOLDOWN_ERROR, VERIFICATION_CODE_REQUIRED, USAGE_VERIFICATION_CODE, INCORRECT_VERIFICATION_CODE, VERIFICATION_CODE_VERIFIED, VERIFICATION_CODE_ALREADY_VERIFIED, VERIFICATION_CODE_EXPIRED, VERIFICATION_CODE_EMAIL_NEEDED, QUICK_COMMAND_PROTECTION_KICK, SECOND, SECONDS, MINUTE, MINUTES, HOUR, HOURS, DAY, DAYS};
   }
}
