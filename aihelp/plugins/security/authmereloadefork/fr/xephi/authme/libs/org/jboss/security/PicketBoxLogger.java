package fr.xephi.authme.libs.org.jboss.security;

import java.net.URL;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.security.auth.Subject;
import org.jboss.logging.BasicLogger;
import org.jboss.logging.Cause;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Logger;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;
import org.jboss.logging.Logger.Level;

@MessageLogger(
   projectCode = "PBOX"
)
public interface PicketBoxLogger extends BasicLogger {
   PicketBoxLogger LOGGER = (PicketBoxLogger)Logger.getMessageLogger(PicketBoxLogger.class, PicketBoxLogger.class.getPackage().getName());
   PicketBoxLogger AUDIT_LOGGER = (PicketBoxLogger)Logger.getMessageLogger(PicketBoxLogger.class, PicketBoxLogger.class.getPackage().getName() + ".audit");

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 200,
      value = "Begin isValid, principal: %s, cache entry: %s"
   )
   void traceBeginIsValid(Principal var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 201,
      value = "End isValid, result = %s"
   )
   void traceEndIsValid(boolean var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 202,
      value = "Flushing all entries from security cache"
   )
   void traceFlushWholeCache();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 203,
      value = "Flushing %s from security cache"
   )
   void traceFlushCacheEntry(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 204,
      value = "Begin validateCache, domainInfo: %s, credential class: %s"
   )
   void traceBeginValidateCache(String var1, Class<?> var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 205,
      value = "End validateCache, result = %s"
   )
   void traceEndValidteCache(boolean var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 206,
      value = "Login failure"
   )
   void debugFailedLogin(@Cause Throwable var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 207,
      value = "updateCache, input subject: %s, cached subject: %s"
   )
   void traceUpdateCache(String var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 208,
      value = "Inserted cache info: %s"
   )
   void traceInsertedCacheInfo(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 209,
      value = "defaultLogin, principal: %s"
   )
   void traceDefaultLoginPrincipal(Principal var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 210,
      value = "defaultLogin, login context: %s, subject: %s"
   )
   void traceDefaultLoginSubject(String var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 211,
      value = "Cache entry logout failed"
   )
   void traceCacheEntryLogoutFailure(@Cause Throwable var1);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 212,
      value = "Exception loading file %s"
   )
   void errorLoadingConfigFile(String var1, @Cause Throwable var2);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 213,
      value = "Failed to convert username to byte[] using UTF-8"
   )
   void errorConvertingUsernameUTF8(@Cause Throwable var1);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 214,
      value = "Charset %s not found. Using platform default"
   )
   void errorFindingCharset(String var1, @Cause Throwable var2);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 215,
      value = "Unsupported hash encoding format: %s"
   )
   void unsupportedHashEncodingFormat(String var1);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 216,
      value = "Password hash calculation failed"
   )
   void errorCalculatingPasswordHash(@Cause Throwable var1);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 217,
      value = "Failed to check if the strong jurisdiction policy files have been installed"
   )
   void errorCheckingStrongJurisdictionPolicyFiles(@Cause Throwable var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 218,
      value = "bindDN is not found"
   )
   void traceBindDNNotFound();

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 219,
      value = "Exception while decrypting bindCredential"
   )
   void errorDecryptingBindCredential(@Cause Throwable var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 220,
      value = "Logging into LDAP server with env %s"
   )
   void traceLDAPConnectionEnv(Properties var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 221,
      value = "Begin getAppConfigurationEntry(%s), size: %s"
   )
   void traceBeginGetAppConfigEntry(String var1, int var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 222,
      value = "getAppConfigurationEntry(%s), no entry found, trying parent config %s"
   )
   void traceGetAppConfigEntryViaParent(String var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 223,
      value = "getAppConfigurationEntry(%s), no entry in parent config, trying default %s"
   )
   void traceGetAppConfigEntryViaDefault(String var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 224,
      value = "End getAppConfigurationEntry(%s), AuthInfo: %s"
   )
   void traceEndGetAppConfigEntryWithSuccess(String var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 225,
      value = "End getAppConfigurationEntry(%s), failed to find entry"
   )
   void traceEndGetAppConfigEntryWithFailure(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 226,
      value = "addAppConfig(%s), AuthInfo: %s"
   )
   void traceAddAppConfig(String var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 227,
      value = "removeAppConfig(%s)"
   )
   void traceRemoveAppConfig(String var1);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 228,
      value = "Failed to find config: %s"
   )
   void warnFailureToFindConfig(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 229,
      value = "Begin loadConfig, loginConfigURL: %s"
   )
   void traceBeginLoadConfig(URL var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 230,
      value = "End loadConfig, loginConfigURL: %s"
   )
   void traceEndLoadConfigWithSuccess(URL var1);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 231,
      value = "End loadConfig, failed to load config: %s"
   )
   void warnEndLoadConfigWithFailure(URL var1, @Cause Throwable var2);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 232,
      value = "Try loading config as XML from %s"
   )
   void debugLoadConfigAsXML(URL var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 233,
      value = "Failed to load config as XML. Try loading as Sun format from %s"
   )
   void debugLoadConfigAsSun(URL var1, @Cause Throwable var2);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 234,
      value = "Invalid or misspelled module option: %s"
   )
   void warnInvalidModuleOption(String var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 235,
      value = "Error getting request from policy context"
   )
   void debugErrorGettingRequestFromPolicyContext(@Cause Throwable var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 236,
      value = "Begin initialize method"
   )
   void traceBeginInitialize();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 237,
      value = "Saw unauthenticated indentity: %s"
   )
   void traceUnauthenticatedIdentity(String var1);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 238,
      value = "Failed to create custom unauthenticated identity"
   )
   void warnFailureToCreateUnauthIdentity(@Cause Throwable var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 239,
      value = "End initialize method"
   )
   void traceEndInitialize();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 240,
      value = "Begin login method"
   )
   void traceBeginLogin();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 241,
      value = "End login method, isValid: %s"
   )
   void traceEndLogin(boolean var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 242,
      value = "Begin commit method, overall result: %s"
   )
   void traceBeginCommit(boolean var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 243,
      value = "Begin logout method"
   )
   void traceBeginLogout();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 244,
      value = "Begin abort method"
   )
   void traceBeginAbort();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 245,
      value = "Found security domain: %s"
   )
   void traceSecurityDomainFound(String var1);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 246,
      value = "The JSSE security domain %s is not valid. All authentication using this login module will fail!"
   )
   void errorGettingJSSESecurityDomain(String var1);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 247,
      value = "Unable to find the security domain %s"
   )
   void errorFindingSecurityDomain(String var1, @Cause Throwable var2);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 248,
      value = "Failed to create X509CertificateVerifier"
   )
   void errorCreatingCertificateVerifier(@Cause Throwable var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 249,
      value = "javax.security.auth.login.password is not a X509Certificate"
   )
   void debugPasswordNotACertificate();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 250,
      value = "Authenticating using unauthenticated identity %s"
   )
   void traceUsingUnauthIdentity(String var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 251,
      value = "Failed to create identity for alias %s"
   )
   void debugFailureToCreateIdentityForAlias(String var1, @Cause Throwable var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 252,
      value = "Begin getAliasAndCert method"
   )
   void traceBeginGetAliasAndCert();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 253,
      value = "Found certificate, serial number: %s, subject DN: %s"
   )
   void traceCertificateFound(String var1, String var2);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 254,
      value = "CallbackHandler did not provide a credential"
   )
   void warnNullCredentialFromCallbackHandler();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 255,
      value = "End getAliasAndCert method"
   )
   void traceEndGetAliasAndCert();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 256,
      value = "Begin validateCredential method"
   )
   void traceBeginValidateCredential();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 257,
      value = "Validating certificate using verifier %s"
   )
   void traceValidatingUsingVerifier(Class<?> var1);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 258,
      value = "Failed to find certificate for alias &%s"
   )
   void warnFailureToFindCertForAlias(String var1, @Cause Throwable var2);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 259,
      value = "Failed to validate certificate: SecurityDomain, Keystore or certificate is null"
   )
   void warnFailureToValidateCertificate();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 260,
      value = "End validateCredential method, result: %s"
   )
   void traceEndValidateCredential(boolean var1);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 261,
      value = "Failed to load users/passwords/roles files"
   )
   void errorLoadingUserRolesPropertiesFiles(@Cause Throwable var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 262,
      value = "Module options [dsJndiName: %s, principalsQuery: %s, rolesQuery: %s, suspendResume: %s]"
   )
   void traceDBCertLoginModuleOptions(String var1, String var2, String var3, boolean var4);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 263,
      value = "Executing query %s with username %s"
   )
   void traceExecuteQuery(String var1, String var2);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 264,
      value = "Failed to create principal %s"
   )
   void debugFailureToCreatePrincipal(String var1, @Cause Throwable var2);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 265,
      value = "The security domain %s has been disabled. All authentication will fail"
   )
   void errorUsingDisabledDomain(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 266,
      value = "Binding username %s"
   )
   void traceBindingLDAPUsername(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 267,
      value = "Rejecting empty password as allowEmptyPasswords option has not been set to true"
   )
   void traceRejectingEmptyPassword();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 268,
      value = "Assigning user to role %s"
   )
   void traceAssignUserToRole(String var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 269,
      value = "Failed to parse %s as number, using default value %s"
   )
   void debugFailureToParseNumberProperty(String var1, long var2);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 270,
      value = "Failed to query %s from %s"
   )
   void debugFailureToQueryLDAPAttribute(String var1, String var2, @Cause Throwable var3);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 271,
      value = "Logged into LDAP server, context: %s"
   )
   void traceSuccessfulLogInToLDAP(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 272,
      value = "Rebind security principal to %s"
   )
   void traceRebindWithConfiguredPrincipal(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 273,
      value = "Found user roles context DN: %s"
   )
   void traceFoundUserRolesContextDN(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 274,
      value = "Searching rolesCtxDN %s with roleFilter: %s, filterArgs: %s, roleAttr: %s, searchScope: %s, searchTimeLimit: %s"
   )
   void traceRolesDNSearch(String var1, String var2, String var3, String var4, int var5, int var6);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 275,
      value = "Checking search result %s"
   )
   void traceCheckSearchResult(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 276,
      value = "Following roleDN %s"
   )
   void traceFollowRoleDN(String var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 277,
      value = "No attribute %s found in search result %s"
   )
   void debugFailureToFindAttrInSearchResult(String var1, String var2);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 278,
      value = "Failed to locate roles"
   )
   void debugFailureToExecuteRolesDNSearch(@Cause Throwable var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 279,
      value = "The real host for trust is %s"
   )
   void debugRealHostForTrust(String var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 280,
      value = "Failed to load properties file %s"
   )
   void debugFailureToLoadPropertiesFile(String var1, @Cause Throwable var2);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 281,
      value = "Password hashing activated, algorithm: %s, encoding: %s, charset: %s, callback: %s, storeCallBack: %s"
   )
   void debugPasswordHashing(String var1, String var2, String var3, String var4, String var5);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 282,
      value = "Failed to instantiate class %s"
   )
   void debugFailureToInstantiateClass(String var1, @Cause Throwable var2);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 283,
      value = "Bad password for username %s"
   )
   void debugBadPasswordForUsername(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 284,
      value = "Created DigestCallback %s"
   )
   void traceCreateDigestCallback(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 285,
      value = "Adding role %s to group %s"
   )
   void traceAdditionOfRoleToGroup(String var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 286,
      value = "Attempting to load resource %s"
   )
   void traceAttemptToLoadResource(String var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 287,
      value = "Failed to open properties file from URL"
   )
   void debugFailureToOpenPropertiesFromURL(@Cause Throwable var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 288,
      value = "Properties file %s loaded, users: %s"
   )
   void tracePropertiesFileLoaded(String var1, Set<?> var2);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 289,
      value = "JACC delegate access denied [permission: %s, caller: %s, roles: %s"
   )
   void debugJACCDeniedAccess(String var1, Subject var2, String var3);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 290,
      value = "No method permissions assigned to method: %s, interface: %s"
   )
   void traceNoMethodPermissions(String var1, String var2);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 291,
      value = "Method: %s, interface: %s, required roles: %s"
   )
   void debugEJBPolicyModuleDelegateState(String var1, String var2, String var3);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 292,
      value = "Insufficient method permissions [principal: %s, EJB name: %s, method: %s, interface: %s, required roles: %s, principal roles: %s, run-as roles: %s]"
   )
   void debugInsufficientMethodPermissions(Principal var1, String var2, String var3, String var4, String var5, String var6, String var7);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 293,
      value = "Exception caught"
   )
   void debugIgnoredException(@Cause Throwable var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 294,
      value = "Check is not resourcePerm, userDataPerm or roleRefPerm"
   )
   void debugInvalidWebJaccCheck();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 295,
      value = "hasResourcePermission, permission: %s, allowed: %s"
   )
   void traceHasResourcePermission(String var1, boolean var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 296,
      value = "hasRolePermission, permission: %s, allowed: %s"
   )
   void traceHasRolePermission(String var1, boolean var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 297,
      value = "hasUserDataPermission, permission: %s, allowed: %s"
   )
   void traceHasUserDataPermission(String var1, boolean var2);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 298,
      value = "Requisite module %s failed"
   )
   void debugRequisiteModuleFailure(String var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 299,
      value = "Required module %s failed"
   )
   void debugRequiredModuleFailure(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 300,
      value = "Denied: matched excluded set, permission %s"
   )
   void traceImpliesMatchesExcludedSet(Permission var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 301,
      value = "Allowed: matched unchecked set, permission %s"
   )
   void traceImpliesMatchesUncheckedSet(Permission var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 302,
      value = "Protection domain principals: %s"
   )
   void traceProtectionDomainPrincipals(List<String> var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 303,
      value = "Not principals found in protection domain %s"
   )
   void traceNoPrincipalsInProtectionDomain(ProtectionDomain var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 304,
      value = "Checking role: %s, permissions: %s"
   )
   void debugImpliesParameters(String var1, Permissions var2);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 305,
      value = "Checking result, implies: %s"
   )
   void debugImpliesResult(boolean var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 306,
      value = "No PolicyContext found for contextID %s"
   )
   void traceNoPolicyContextForId(String var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 307,
      value = "Constructing JBossPolicyConfiguration with contextID %s"
   )
   void debugJBossPolicyConfigurationConstruction(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 308,
      value = "addToExcludedPolicy, permission: %s"
   )
   void traceAddPermissionToExcludedPolicy(Permission var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 309,
      value = "addToExcludedPolicy, permission collection: %s"
   )
   void traceAddPermissionsToExcludedPolicy(PermissionCollection var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 310,
      value = "addToRole, permission: %s"
   )
   void traceAddPermissionToRole(Permission var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 311,
      value = "addToRole, permission collection: %s"
   )
   void traceAddPermissionsToRole(PermissionCollection var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 312,
      value = "addToUncheckedPolicy, permission: %s"
   )
   void traceAddPermissionToUncheckedPolicy(Permission var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 313,
      value = "addToUncheckedPolicy, permission collection: %s"
   )
   void traceAddPermissionsToUncheckedPolicy(PermissionCollection var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 314,
      value = "commit, contextID: %s"
   )
   void tracePolicyConfigurationCommit(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 315,
      value = "delete, contextID: %s"
   )
   void tracePolicyConfigurationDelete(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 316,
      value = "linkConfiguration, link to contextID: %s"
   )
   void traceLinkConfiguration(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 317,
      value = "removeExcludedPolicy, contextID: %s"
   )
   void traceRemoveExcludedPolicy(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 318,
      value = "removeRole, role name: %s, contextID: %s"
   )
   void traceRemoveRole(String var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 319,
      value = "removeUncheckedPolicy, contextID: %s"
   )
   void traceRemoveUncheckedPolicy(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 320,
      value = "Mapped X500 principal, new principal: %s"
   )
   void traceMappedX500Principal(Principal var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 321,
      value = "Query returned an empty result"
   )
   void traceQueryWithEmptyResult();

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 322,
      value = "Mapping provider options [principal: %s, principal to roles map: %s, subject principals: %s]"
   )
   void debugMappingProviderOptions(Principal var1, Map<String, Set<String>> var2, Set<Principal> var3);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 323,
      value = "No audit context found for security domain %s; using default context"
   )
   void traceNoAuditContextFoundForDomain(String var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 324,
      value = "AuthorizationManager is null for security domain %s"
   )
   void debugNullAuthorizationManager(String var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 325,
      value = "Authorization processing error"
   )
   void debugAuthorizationError(@Cause Throwable var1);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 326,
      value = "%s processing failed"
   )
   void debugFailureExecutingMethod(String var1, @Cause Throwable var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 327,
      value = "Returning host %s from thread [id: %s]"
   )
   void traceHostThreadLocalGet(String var1, long var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 328,
      value = "Setting host %s on thread [id: %s]"
   )
   void traceHostThreadLocalSet(String var1, long var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 329,
      value = "Begin doesUserHaveRole, principal: %s, roles: %s"
   )
   void traceBeginDoesUserHaveRole(Principal var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 330,
      value = "End doesUserHaveRole, result: %s"
   )
   void traceEndDoesUserHaveRole(boolean var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 331,
      value = "Roles before mapping: %s"
   )
   void traceRolesBeforeMapping(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 332,
      value = "Roles after mapping: %s"
   )
   void traceRolesAfterMapping(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 333,
      value = "Deregistered policy for contextID: %s, type: %s"
   )
   void traceDeregisterPolicy(String var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 334,
      value = "Registered policy for contextID: %s, type: %s, location: %s"
   )
   void traceRegisterPolicy(String var1, String var2, String var3);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 335,
      value = "SecurityManagement is not set, creating a default one"
   )
   void warnSecurityMagementNotSet();

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 336,
      value = "AuthenticationManager is null for security domain %s"
   )
   void debugNullAuthenticationManager(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 337,
      value = "nextState for action %s: %s"
   )
   void traceStateMachineNextState(String var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 338,
      value = "Ignore attribute [uri: %s, qname: %s, value: %s]"
   )
   void traceIgnoreXMLAttribute(String var1, String var2, String var3);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 339,
      value = "systemId argument '%s' for publicId '%s' is different from the registered systemId '%s', resolution will be based on the argument"
   )
   void traceSystemIDMismatch(String var1, String var2, String var3);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 340,
      value = "Cannot resolve entity, systemId: %s, publicId: %s"
   )
   void debugFailureToResolveEntity(String var1, String var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 341,
      value = "Begin resolvePublicId, publicId: %s"
   )
   void traceBeginResolvePublicID(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 342,
      value = "Found entity from %s: %s, filename: %s"
   )
   void traceFoundEntityFromID(String var1, String var2, String var3);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 343,
      value = "Cannot load %s from %s resource: %s"
   )
   void warnFailureToLoadIDFromResource(String var1, String var2, String var3);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 344,
      value = "Begin resolveSystemId, systemId: %s"
   )
   void traceBeginResolveSystemID(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 345,
      value = "Begin resolveSystemIdasURL, systemId: %s"
   )
   void traceBeginResolveSystemIDasURL(String var1);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 346,
      value = "Trying to resolve systemId %s as a non-file URL"
   )
   void warnResolvingSystemIdAsNonFileURL(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 347,
      value = "Begin resolveClasspathName, systemId: %s"
   )
   void traceBeginResolveClasspathName(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 348,
      value = "Mapped systemId to filename %s"
   )
   void traceMappedSystemIdToFilename(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 349,
      value = "Mapped resource %s to URL %s"
   )
   void traceMappedResourceToURL(String var1, URL var2);

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 350,
      value = "Module option: %s, value: %s"
   )
   void debugModuleOption(String var1, Object var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 351,
      value = "Obtained auth info from handler, principal: %s, credential class: %s"
   )
   void traceObtainedAuthInfoFromHandler(Principal var1, Class<?> var2);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 352,
      value = "JSSE domain got request for key with alias %s"
   )
   void traceJSSEDomainGetKey(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 353,
      value = "JSSE domain got request for certificate with alias %s"
   )
   void traceJSSEDomainGetCertificate(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 354,
      value = "Setting security roles ThreadLocal: %s"
   )
   void traceSecRolesAssociationSetSecurityRoles(Map<String, Set<String>> var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 355,
      value = "Begin execPasswordCmd, command: %s"
   )
   void traceBeginExecPasswordCmd(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 356,
      value = "End execPasswordCmd, exit code: %s"
   )
   void traceEndExecPasswordCmd(int var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 357,
      value = "Begin getIdentity, username: %s"
   )
   void traceBeginGetIdentity(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 358,
      value = "Begin getRoleSets"
   )
   void traceBeginGetRoleSets();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 359,
      value = "Current calling principal: %s, thread name: %s"
   )
   void traceCurrentCallingPrincipal(String var1, String var2);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 360,
      value = "Creating login module with empty password"
   )
   void warnModuleCreationWithEmptyPassword();

   @LogMessage(
      level = Level.INFO
   )
   @Message(
      id = 361,
      value = "Default Security Vault Implementation Initialized and Ready"
   )
   void infoVaultInitialized();

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 362,
      value = "Cannot get MD5 algorithm instance for hashing password commands. Using NULL."
   )
   void errorCannotGetMD5AlgorithmInstance();

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 363,
      value = "Retrieving password from the cache for key: %s"
   )
   void traceRetrievingPasswordFromCache(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 364,
      value = "Storing password to the cache for key: %s"
   )
   void traceStoringPasswordToCache(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 365,
      value = "Resetting cache"
   )
   void traceResettingCache();

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 366,
      value = "Error parsing time out number."
   )
   void errorParsingTimeoutNumber();

   @LogMessage(
      level = Level.DEBUG
   )
   @Message(
      id = 367,
      value = "Reading security vault data version %s target version is %s"
   )
   void securityVaultContentVersion(String var1, String var2);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 368,
      value = "Security Vault contains both covnerted (%s) and pre-conversion data (%s). Try to delete %s file and start over again."
   )
   void mixedVaultDataFound(String var1, String var2, String var3);

   @LogMessage(
      level = Level.INFO
   )
   @Message(
      id = 369,
      value = "Ambiguos vault block and attribute name stored in original security vault. Delimiter (%s) is part of vault block or attribute name. Took the first delimiter. Result vault block (%s) attribute name (%s). Modify security vault manually."
   )
   void ambiguosKeyForSecurityVaultTransformation(String var1, String var2, String var3);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 370,
      value = "Cannot delete original security vault file (%s). Delete the file manually before next start, please."
   )
   void cannotDeleteOriginalVaultFile(String var1);

   @LogMessage(
      level = Level.INFO
   )
   @Message(
      id = 371,
      value = "Security Vault does not contain SecretKey entry under alias (%s)"
   )
   void vaultDoesnotContainSecretKey(String var1);

   @LogMessage(
      level = Level.INFO
   )
   @Message(
      id = 372,
      value = "Security Vault key store successfuly converted to JCEKS type (%s). From now on use JCEKS as KEYSTORE_TYPE in Security Vault configuration."
   )
   void keyStoreConvertedToJCEKS(String var1);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 373,
      value = "Error getting ServerAuthConfig for layer %s and appContext %s"
   )
   void errorGettingServerAuthConfig(String var1, String var2, @Cause Throwable var3);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 374,
      value = "Error getting ServerAuthContext for authContextId %s and security domain %s"
   )
   void errorGettingServerAuthContext(String var1, String var2, @Cause Throwable var3);

   @LogMessage(
      level = Level.ERROR
   )
   @Message(
      id = 375,
      value = "Error getting the module classloader informations for cache"
   )
   void errorGettingModuleInformation(@Cause Throwable var1);

   @LogMessage(
      level = Level.WARN
   )
   @Message(
      id = 376,
      value = "Wrong Base64 string used with masked password utility. Following is correct (%s)"
   )
   void wrongBase64StringUsed(String var1);

   @LogMessage(
      level = Level.TRACE
   )
   @Message(
      id = 377,
      value = "JAAS logout, login context: %s, subject: %s"
   )
   void traceLogoutSubject(String var1, String var2);
}
