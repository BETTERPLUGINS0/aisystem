package fr.xephi.authme.libs.org.jboss.security;

import java.io.Serializable;
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
import org.jboss.logging.DelegatingBasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

public class PicketBoxLogger_$logger extends DelegatingBasicLogger implements Serializable, BasicLogger, PicketBoxLogger {
   private static final long serialVersionUID = 1L;
   private static final String projectCode = "PBOX";
   private static final String FQCN = PicketBoxLogger_$logger.class.getName();
   private static final String traceEndDoesUserHaveRole = "End doesUserHaveRole, result: %s";
   private static final String debugFailureToQueryLDAPAttribute = "Failed to query %s from %s";
   private static final String traceBeginIsValid = "Begin isValid, principal: %s, cache entry: %s";
   private static final String debugAuthorizationError = "Authorization processing error";
   private static final String traceRemoveAppConfig = "removeAppConfig(%s)";
   private static final String traceInsertedCacheInfo = "Inserted cache info: %s";
   private static final String traceCertificateFound = "Found certificate, serial number: %s, subject DN: %s";
   private static final String traceObtainedAuthInfoFromHandler = "Obtained auth info from handler, principal: %s, credential class: %s";
   private static final String vaultDoesnotContainSecretKey = "Security Vault does not contain SecretKey entry under alias (%s)";
   private static final String debugFailureToFindAttrInSearchResult = "No attribute %s found in search result %s";
   private static final String traceHasUserDataPermission = "hasUserDataPermission, permission: %s, allowed: %s";
   private static final String debugErrorGettingRequestFromPolicyContext = "Error getting request from policy context";
   private static final String debugLoadConfigAsXML = "Try loading config as XML from %s";
   private static final String traceAddPermissionToUncheckedPolicy = "addToUncheckedPolicy, permission: %s";
   private static final String debugBadPasswordForUsername = "Bad password for username %s";
   private static final String traceMappedX500Principal = "Mapped X500 principal, new principal: %s";
   private static final String traceBeginCommit = "Begin commit method, overall result: %s";
   private static final String debugJBossPolicyConfigurationConstruction = "Constructing JBossPolicyConfiguration with contextID %s";
   private static final String debugFailureToExecuteRolesDNSearch = "Failed to locate roles";
   private static final String unsupportedHashEncodingFormat = "Unsupported hash encoding format: %s";
   private static final String wrongBase64StringUsed = "Wrong Base64 string used with masked password utility. Following is correct (%s)";
   private static final String errorGettingModuleInformation = "Error getting the module classloader informations for cache";
   private static final String traceSecRolesAssociationSetSecurityRoles = "Setting security roles ThreadLocal: %s";
   private static final String debugFailureToLoadPropertiesFile = "Failed to load properties file %s";
   private static final String traceDefaultLoginPrincipal = "defaultLogin, principal: %s";
   private static final String debugIgnoredException = "Exception caught";
   private static final String errorCannotGetMD5AlgorithmInstance = "Cannot get MD5 algorithm instance for hashing password commands. Using NULL.";
   private static final String traceDeregisterPolicy = "Deregistered policy for contextID: %s, type: %s";
   private static final String traceCurrentCallingPrincipal = "Current calling principal: %s, thread name: %s";
   private static final String traceAddPermissionToRole = "addToRole, permission: %s";
   private static final String traceBeginResolvePublicID = "Begin resolvePublicId, publicId: %s";
   private static final String traceMappedSystemIdToFilename = "Mapped systemId to filename %s";
   private static final String traceResettingCache = "Resetting cache";
   private static final String traceBeginGetIdentity = "Begin getIdentity, username: %s";
   private static final String traceFollowRoleDN = "Following roleDN %s";
   private static final String traceBeginGetAppConfigEntry = "Begin getAppConfigurationEntry(%s), size: %s";
   private static final String tracePolicyConfigurationCommit = "commit, contextID: %s";
   private static final String debugImpliesParameters = "Checking role: %s, permissions: %s";
   private static final String debugInvalidWebJaccCheck = "Check is not resourcePerm, userDataPerm or roleRefPerm";
   private static final String traceStoringPasswordToCache = "Storing password to the cache for key: %s";
   private static final String debugInsufficientMethodPermissions = "Insufficient method permissions [principal: %s, EJB name: %s, method: %s, interface: %s, required roles: %s, principal roles: %s, run-as roles: %s]";
   private static final String ambiguosKeyForSecurityVaultTransformation = "Ambiguos vault block and attribute name stored in original security vault. Delimiter (%s) is part of vault block or attribute name. Took the first delimiter. Result vault block (%s) attribute name (%s). Modify security vault manually.";
   private static final String traceExecuteQuery = "Executing query %s with username %s";
   private static final String debugFailedLogin = "Login failure";
   private static final String cannotDeleteOriginalVaultFile = "Cannot delete original security vault file (%s). Delete the file manually before next start, please.";
   private static final String errorGettingJSSESecurityDomain = "The JSSE security domain %s is not valid. All authentication using this login module will fail!";
   private static final String traceEndGetAliasAndCert = "End getAliasAndCert method";
   private static final String traceHostThreadLocalSet = "Setting host %s on thread [id: %s]";
   private static final String errorDecryptingBindCredential = "Exception while decrypting bindCredential";
   private static final String debugLoadConfigAsSun = "Failed to load config as XML. Try loading as Sun format from %s";
   private static final String traceUpdateCache = "updateCache, input subject: %s, cached subject: %s";
   private static final String errorParsingTimeoutNumber = "Error parsing time out number.";
   private static final String warnResolvingSystemIdAsNonFileURL = "Trying to resolve systemId %s as a non-file URL";
   private static final String traceAddPermissionsToExcludedPolicy = "addToExcludedPolicy, permission collection: %s";
   private static final String traceBeginDoesUserHaveRole = "Begin doesUserHaveRole, principal: %s, roles: %s";
   private static final String traceImpliesMatchesExcludedSet = "Denied: matched excluded set, permission %s";
   private static final String traceNoMethodPermissions = "No method permissions assigned to method: %s, interface: %s";
   private static final String debugFailureExecutingMethod = "%s processing failed";
   private static final String traceNoPrincipalsInProtectionDomain = "Not principals found in protection domain %s";
   private static final String traceStateMachineNextState = "nextState for action %s: %s";
   private static final String tracePolicyConfigurationDelete = "delete, contextID: %s";
   private static final String traceRemoveRole = "removeRole, role name: %s, contextID: %s";
   private static final String debugPasswordHashing = "Password hashing activated, algorithm: %s, encoding: %s, charset: %s, callback: %s, storeCallBack: %s";
   private static final String debugEJBPolicyModuleDelegateState = "Method: %s, interface: %s, required roles: %s";
   private static final String traceBeginValidateCredential = "Begin validateCredential method";
   private static final String traceNoPolicyContextForId = "No PolicyContext found for contextID %s";
   private static final String traceEndGetAppConfigEntryWithSuccess = "End getAppConfigurationEntry(%s), AuthInfo: %s";
   private static final String traceQueryWithEmptyResult = "Query returned an empty result";
   private static final String traceCacheEntryLogoutFailure = "Cache entry logout failed";
   private static final String debugFailureToOpenPropertiesFromURL = "Failed to open properties file from URL";
   private static final String warnNullCredentialFromCallbackHandler = "CallbackHandler did not provide a credential";
   private static final String traceBeginResolveSystemIDasURL = "Begin resolveSystemIdasURL, systemId: %s";
   private static final String traceRemoveUncheckedPolicy = "removeUncheckedPolicy, contextID: %s";
   private static final String traceEndLogin = "End login method, isValid: %s";
   private static final String traceEndLoadConfigWithSuccess = "End loadConfig, loginConfigURL: %s";
   private static final String traceFlushCacheEntry = "Flushing %s from security cache";
   private static final String traceBindingLDAPUsername = "Binding username %s";
   private static final String traceLogoutSubject = "JAAS logout, login context: %s, subject: %s";
   private static final String errorFindingCharset = "Charset %s not found. Using platform default";
   private static final String debugJACCDeniedAccess = "JACC delegate access denied [permission: %s, caller: %s, roles: %s";
   private static final String traceEndValidteCache = "End validateCache, result = %s";
   private static final String traceCheckSearchResult = "Checking search result %s";
   private static final String traceAssignUserToRole = "Assigning user to role %s";
   private static final String traceEndExecPasswordCmd = "End execPasswordCmd, exit code: %s";
   private static final String traceBeginExecPasswordCmd = "Begin execPasswordCmd, command: %s";
   private static final String traceMappedResourceToURL = "Mapped resource %s to URL %s";
   private static final String traceIgnoreXMLAttribute = "Ignore attribute [uri: %s, qname: %s, value: %s]";
   private static final String traceBeginGetAliasAndCert = "Begin getAliasAndCert method";
   private static final String traceRebindWithConfiguredPrincipal = "Rebind security principal to %s";
   private static final String warnFailureToValidateCertificate = "Failed to validate certificate: SecurityDomain, Keystore or certificate is null";
   private static final String debugNullAuthorizationManager = "AuthorizationManager is null for security domain %s";
   private static final String traceSystemIDMismatch = "systemId argument '%s' for publicId '%s' is different from the registered systemId '%s', resolution will be based on the argument";
   private static final String traceAddPermissionsToRole = "addToRole, permission collection: %s";
   private static final String traceBeginAbort = "Begin abort method";
   private static final String traceRetrievingPasswordFromCache = "Retrieving password from the cache for key: %s";
   private static final String traceValidatingUsingVerifier = "Validating certificate using verifier %s";
   private static final String traceDBCertLoginModuleOptions = "Module options [dsJndiName: %s, principalsQuery: %s, rolesQuery: %s, suspendResume: %s]";
   private static final String securityVaultContentVersion = "Reading security vault data version %s target version is %s";
   private static final String traceDefaultLoginSubject = "defaultLogin, login context: %s, subject: %s";
   private static final String traceFoundEntityFromID = "Found entity from %s: %s, filename: %s";
   private static final String debugRequisiteModuleFailure = "Requisite module %s failed";
   private static final String debugModuleOption = "Module option: %s, value: %s";
   private static final String traceBeginLogout = "Begin logout method";
   private static final String errorLoadingConfigFile = "Exception loading file %s";
   private static final String traceSuccessfulLogInToLDAP = "Logged into LDAP server, context: %s";
   private static final String errorGettingServerAuthConfig = "Error getting ServerAuthConfig for layer %s and appContext %s";
   private static final String debugPasswordNotACertificate = "javax.security.auth.login.password is not a X509Certificate";
   private static final String traceAdditionOfRoleToGroup = "Adding role %s to group %s";
   private static final String traceNoAuditContextFoundForDomain = "No audit context found for security domain %s; using default context";
   private static final String traceImpliesMatchesUncheckedSet = "Allowed: matched unchecked set, permission %s";
   private static final String traceRemoveExcludedPolicy = "removeExcludedPolicy, contextID: %s";
   private static final String warnFailureToCreateUnauthIdentity = "Failed to create custom unauthenticated identity";
   private static final String traceBeginInitialize = "Begin initialize method";
   private static final String errorConvertingUsernameUTF8 = "Failed to convert username to byte[] using UTF-8";
   private static final String traceBeginLoadConfig = "Begin loadConfig, loginConfigURL: %s";
   private static final String debugFailureToResolveEntity = "Cannot resolve entity, systemId: %s, publicId: %s";
   private static final String mixedVaultDataFound = "Security Vault contains both covnerted (%s) and pre-conversion data (%s). Try to delete %s file and start over again.";
   private static final String traceUsingUnauthIdentity = "Authenticating using unauthenticated identity %s";
   private static final String traceBeginResolveSystemID = "Begin resolveSystemId, systemId: %s";
   private static final String warnEndLoadConfigWithFailure = "End loadConfig, failed to load config: %s";
   private static final String traceFoundUserRolesContextDN = "Found user roles context DN: %s";
   private static final String traceCreateDigestCallback = "Created DigestCallback %s";
   private static final String errorGettingServerAuthContext = "Error getting ServerAuthContext for authContextId %s and security domain %s";
   private static final String warnFailureToFindCertForAlias = "Failed to find certificate for alias &%s";
   private static final String traceEndInitialize = "End initialize method";
   private static final String errorCheckingStrongJurisdictionPolicyFiles = "Failed to check if the strong jurisdiction policy files have been installed";
   private static final String debugFailureToCreatePrincipal = "Failed to create principal %s";
   private static final String traceRolesBeforeMapping = "Roles before mapping: %s";
   private static final String traceUnauthenticatedIdentity = "Saw unauthenticated indentity: %s";
   private static final String traceProtectionDomainPrincipals = "Protection domain principals: %s";
   private static final String traceRegisterPolicy = "Registered policy for contextID: %s, type: %s, location: %s";
   private static final String traceRolesDNSearch = "Searching rolesCtxDN %s with roleFilter: %s, filterArgs: %s, roleAttr: %s, searchScope: %s, searchTimeLimit: %s";
   private static final String traceRejectingEmptyPassword = "Rejecting empty password as allowEmptyPasswords option has not been set to true";
   private static final String traceLinkConfiguration = "linkConfiguration, link to contextID: %s";
   private static final String traceBeginValidateCache = "Begin validateCache, domainInfo: %s, credential class: %s";
   private static final String traceHostThreadLocalGet = "Returning host %s from thread [id: %s]";
   private static final String traceAddPermissionsToUncheckedPolicy = "addToUncheckedPolicy, permission collection: %s";
   private static final String debugMappingProviderOptions = "Mapping provider options [principal: %s, principal to roles map: %s, subject principals: %s]";
   private static final String debugRealHostForTrust = "The real host for trust is %s";
   private static final String traceBeginResolveClasspathName = "Begin resolveClasspathName, systemId: %s";
   private static final String warnModuleCreationWithEmptyPassword = "Creating login module with empty password";
   private static final String debugNullAuthenticationManager = "AuthenticationManager is null for security domain %s";
   private static final String errorUsingDisabledDomain = "The security domain %s has been disabled. All authentication will fail";
   private static final String traceHasResourcePermission = "hasResourcePermission, permission: %s, allowed: %s";
   private static final String traceAddAppConfig = "addAppConfig(%s), AuthInfo: %s";
   private static final String traceGetAppConfigEntryViaParent = "getAppConfigurationEntry(%s), no entry found, trying parent config %s";
   private static final String traceEndGetAppConfigEntryWithFailure = "End getAppConfigurationEntry(%s), failed to find entry";
   private static final String errorCalculatingPasswordHash = "Password hash calculation failed";
   private static final String debugImpliesResult = "Checking result, implies: %s";
   private static final String traceAttemptToLoadResource = "Attempting to load resource %s";
   private static final String traceRolesAfterMapping = "Roles after mapping: %s";
   private static final String tracePropertiesFileLoaded = "Properties file %s loaded, users: %s";
   private static final String traceJSSEDomainGetKey = "JSSE domain got request for key with alias %s";
   private static final String traceJSSEDomainGetCertificate = "JSSE domain got request for certificate with alias %s";
   private static final String traceLDAPConnectionEnv = "Logging into LDAP server with env %s";
   private static final String warnFailureToFindConfig = "Failed to find config: %s";
   private static final String infoVaultInitialized = "Default Security Vault Implementation Initialized and Ready";
   private static final String warnInvalidModuleOption = "Invalid or misspelled module option: %s";
   private static final String keyStoreConvertedToJCEKS = "Security Vault key store successfuly converted to JCEKS type (%s). From now on use JCEKS as KEYSTORE_TYPE in Security Vault configuration.";
   private static final String traceSecurityDomainFound = "Found security domain: %s";
   private static final String traceHasRolePermission = "hasRolePermission, permission: %s, allowed: %s";
   private static final String errorCreatingCertificateVerifier = "Failed to create X509CertificateVerifier";
   private static final String warnSecurityMagementNotSet = "SecurityManagement is not set, creating a default one";
   private static final String traceBindDNNotFound = "bindDN is not found";
   private static final String traceBeginLogin = "Begin login method";
   private static final String traceGetAppConfigEntryViaDefault = "getAppConfigurationEntry(%s), no entry in parent config, trying default %s";
   private static final String errorLoadingUserRolesPropertiesFiles = "Failed to load users/passwords/roles files";
   private static final String traceBeginGetRoleSets = "Begin getRoleSets";
   private static final String debugFailureToParseNumberProperty = "Failed to parse %s as number, using default value %s";
   private static final String errorFindingSecurityDomain = "Unable to find the security domain %s";
   private static final String debugRequiredModuleFailure = "Required module %s failed";
   private static final String debugFailureToInstantiateClass = "Failed to instantiate class %s";
   private static final String warnFailureToLoadIDFromResource = "Cannot load %s from %s resource: %s";
   private static final String debugFailureToCreateIdentityForAlias = "Failed to create identity for alias %s";
   private static final String traceAddPermissionToExcludedPolicy = "addToExcludedPolicy, permission: %s";
   private static final String traceEndValidateCredential = "End validateCredential method, result: %s";
   private static final String traceFlushWholeCache = "Flushing all entries from security cache";
   private static final String traceEndIsValid = "End isValid, result = %s";

   public PicketBoxLogger_$logger(Logger log) {
      super(log);
   }

   public final void traceEndDoesUserHaveRole(boolean hasRole) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000330: " + this.traceEndDoesUserHaveRole$str(), hasRole);
   }

   protected String traceEndDoesUserHaveRole$str() {
      return "End doesUserHaveRole, result: %s";
   }

   public final void debugFailureToQueryLDAPAttribute(String attributeName, String contextName, Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000270: " + this.debugFailureToQueryLDAPAttribute$str(), attributeName, contextName);
   }

   protected String debugFailureToQueryLDAPAttribute$str() {
      return "Failed to query %s from %s";
   }

   public final void traceBeginIsValid(Principal principal, String cacheEntry) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000200: " + this.traceBeginIsValid$str(), principal, cacheEntry);
   }

   protected String traceBeginIsValid$str() {
      return "Begin isValid, principal: %s, cache entry: %s";
   }

   public final void debugAuthorizationError(Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000325: " + this.debugAuthorizationError$str(), new Object[0]);
   }

   protected String debugAuthorizationError$str() {
      return "Authorization processing error";
   }

   public final void traceRemoveAppConfig(String appName) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000227: " + this.traceRemoveAppConfig$str(), appName);
   }

   protected String traceRemoveAppConfig$str() {
      return "removeAppConfig(%s)";
   }

   public final void traceInsertedCacheInfo(String cacheInfo) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000208: " + this.traceInsertedCacheInfo$str(), cacheInfo);
   }

   protected String traceInsertedCacheInfo$str() {
      return "Inserted cache info: %s";
   }

   public final void traceCertificateFound(String serialNumber, String subjectDN) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000253: " + this.traceCertificateFound$str(), serialNumber, subjectDN);
   }

   protected String traceCertificateFound$str() {
      return "Found certificate, serial number: %s, subject DN: %s";
   }

   public final void traceObtainedAuthInfoFromHandler(Principal loginPrincipal, Class credentialClass) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000351: " + this.traceObtainedAuthInfoFromHandler$str(), loginPrincipal, credentialClass);
   }

   protected String traceObtainedAuthInfoFromHandler$str() {
      return "Obtained auth info from handler, principal: %s, credential class: %s";
   }

   public final void vaultDoesnotContainSecretKey(String alias) {
      super.log.logf(FQCN, Level.INFO, (Throwable)null, "PBOX000371: " + this.vaultDoesnotContainSecretKey$str(), alias);
   }

   protected String vaultDoesnotContainSecretKey$str() {
      return "Security Vault does not contain SecretKey entry under alias (%s)";
   }

   public final void debugFailureToFindAttrInSearchResult(String attrName, String searchResult) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000277: " + this.debugFailureToFindAttrInSearchResult$str(), attrName, searchResult);
   }

   protected String debugFailureToFindAttrInSearchResult$str() {
      return "No attribute %s found in search result %s";
   }

   public final void traceHasUserDataPermission(String permission, boolean allowed) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000297: " + this.traceHasUserDataPermission$str(), permission, allowed);
   }

   protected String traceHasUserDataPermission$str() {
      return "hasUserDataPermission, permission: %s, allowed: %s";
   }

   public final void debugErrorGettingRequestFromPolicyContext(Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000235: " + this.debugErrorGettingRequestFromPolicyContext$str(), new Object[0]);
   }

   protected String debugErrorGettingRequestFromPolicyContext$str() {
      return "Error getting request from policy context";
   }

   public final void debugLoadConfigAsXML(URL configURL) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000232: " + this.debugLoadConfigAsXML$str(), configURL);
   }

   protected String debugLoadConfigAsXML$str() {
      return "Try loading config as XML from %s";
   }

   public final void traceAddPermissionToUncheckedPolicy(Permission permission) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000312: " + this.traceAddPermissionToUncheckedPolicy$str(), permission);
   }

   protected String traceAddPermissionToUncheckedPolicy$str() {
      return "addToUncheckedPolicy, permission: %s";
   }

   public final void debugBadPasswordForUsername(String username) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000283: " + this.debugBadPasswordForUsername$str(), username);
   }

   protected String debugBadPasswordForUsername$str() {
      return "Bad password for username %s";
   }

   public final void traceMappedX500Principal(Principal newPrincipal) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000320: " + this.traceMappedX500Principal$str(), newPrincipal);
   }

   protected String traceMappedX500Principal$str() {
      return "Mapped X500 principal, new principal: %s";
   }

   public final void traceBeginCommit(boolean loginOk) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000242: " + this.traceBeginCommit$str(), loginOk);
   }

   protected String traceBeginCommit$str() {
      return "Begin commit method, overall result: %s";
   }

   public final void debugJBossPolicyConfigurationConstruction(String contextID) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000307: " + this.debugJBossPolicyConfigurationConstruction$str(), contextID);
   }

   protected String debugJBossPolicyConfigurationConstruction$str() {
      return "Constructing JBossPolicyConfiguration with contextID %s";
   }

   public final void debugFailureToExecuteRolesDNSearch(Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000278: " + this.debugFailureToExecuteRolesDNSearch$str(), new Object[0]);
   }

   protected String debugFailureToExecuteRolesDNSearch$str() {
      return "Failed to locate roles";
   }

   public final void unsupportedHashEncodingFormat(String hashEncoding) {
      super.log.logf(FQCN, Level.ERROR, (Throwable)null, "PBOX000215: " + this.unsupportedHashEncodingFormat$str(), hashEncoding);
   }

   protected String unsupportedHashEncodingFormat$str() {
      return "Unsupported hash encoding format: %s";
   }

   public final void wrongBase64StringUsed(String fixedBase64) {
      super.log.logf(FQCN, Level.WARN, (Throwable)null, "PBOX000376: " + this.wrongBase64StringUsed$str(), fixedBase64);
   }

   protected String wrongBase64StringUsed$str() {
      return "Wrong Base64 string used with masked password utility. Following is correct (%s)";
   }

   public final void errorGettingModuleInformation(Throwable cause) {
      super.log.logf(FQCN, Level.ERROR, cause, "PBOX000375: " + this.errorGettingModuleInformation$str(), new Object[0]);
   }

   protected String errorGettingModuleInformation$str() {
      return "Error getting the module classloader informations for cache";
   }

   public final void traceSecRolesAssociationSetSecurityRoles(Map roles) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000354: " + this.traceSecRolesAssociationSetSecurityRoles$str(), roles);
   }

   protected String traceSecRolesAssociationSetSecurityRoles$str() {
      return "Setting security roles ThreadLocal: %s";
   }

   public final void debugFailureToLoadPropertiesFile(String fileName, Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000280: " + this.debugFailureToLoadPropertiesFile$str(), fileName);
   }

   protected String debugFailureToLoadPropertiesFile$str() {
      return "Failed to load properties file %s";
   }

   public final void traceDefaultLoginPrincipal(Principal principal) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000209: " + this.traceDefaultLoginPrincipal$str(), principal);
   }

   protected String traceDefaultLoginPrincipal$str() {
      return "defaultLogin, principal: %s";
   }

   public final void debugIgnoredException(Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000293: " + this.debugIgnoredException$str(), new Object[0]);
   }

   protected String debugIgnoredException$str() {
      return "Exception caught";
   }

   public final void errorCannotGetMD5AlgorithmInstance() {
      super.log.logf(FQCN, Level.ERROR, (Throwable)null, "PBOX000362: " + this.errorCannotGetMD5AlgorithmInstance$str(), new Object[0]);
   }

   protected String errorCannotGetMD5AlgorithmInstance$str() {
      return "Cannot get MD5 algorithm instance for hashing password commands. Using NULL.";
   }

   public final void traceDeregisterPolicy(String contextID, String type) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000333: " + this.traceDeregisterPolicy$str(), contextID, type);
   }

   protected String traceDeregisterPolicy$str() {
      return "Deregistered policy for contextID: %s, type: %s";
   }

   public final void traceCurrentCallingPrincipal(String username, String threadName) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000359: " + this.traceCurrentCallingPrincipal$str(), username, threadName);
   }

   protected String traceCurrentCallingPrincipal$str() {
      return "Current calling principal: %s, thread name: %s";
   }

   public final void traceAddPermissionToRole(Permission permission) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000310: " + this.traceAddPermissionToRole$str(), permission);
   }

   protected String traceAddPermissionToRole$str() {
      return "addToRole, permission: %s";
   }

   public final void traceBeginResolvePublicID(String publicId) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000341: " + this.traceBeginResolvePublicID$str(), publicId);
   }

   protected String traceBeginResolvePublicID$str() {
      return "Begin resolvePublicId, publicId: %s";
   }

   public final void traceMappedSystemIdToFilename(String filename) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000348: " + this.traceMappedSystemIdToFilename$str(), filename);
   }

   protected String traceMappedSystemIdToFilename$str() {
      return "Mapped systemId to filename %s";
   }

   public final void traceResettingCache() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000365: " + this.traceResettingCache$str(), new Object[0]);
   }

   protected String traceResettingCache$str() {
      return "Resetting cache";
   }

   public final void traceBeginGetIdentity(String username) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000357: " + this.traceBeginGetIdentity$str(), username);
   }

   protected String traceBeginGetIdentity$str() {
      return "Begin getIdentity, username: %s";
   }

   public final void traceFollowRoleDN(String roleDN) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000276: " + this.traceFollowRoleDN$str(), roleDN);
   }

   protected String traceFollowRoleDN$str() {
      return "Following roleDN %s";
   }

   public final void traceBeginGetAppConfigEntry(String appName, int size) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000221: " + this.traceBeginGetAppConfigEntry$str(), appName, size);
   }

   protected String traceBeginGetAppConfigEntry$str() {
      return "Begin getAppConfigurationEntry(%s), size: %s";
   }

   public final void tracePolicyConfigurationCommit(String contextID) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000314: " + this.tracePolicyConfigurationCommit$str(), contextID);
   }

   protected String tracePolicyConfigurationCommit$str() {
      return "commit, contextID: %s";
   }

   public final void debugImpliesParameters(String roleName, Permissions permissions) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000304: " + this.debugImpliesParameters$str(), roleName, permissions);
   }

   protected String debugImpliesParameters$str() {
      return "Checking role: %s, permissions: %s";
   }

   public final void debugInvalidWebJaccCheck() {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000294: " + this.debugInvalidWebJaccCheck$str(), new Object[0]);
   }

   protected String debugInvalidWebJaccCheck$str() {
      return "Check is not resourcePerm, userDataPerm or roleRefPerm";
   }

   public final void traceStoringPasswordToCache(String newKey) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000364: " + this.traceStoringPasswordToCache$str(), newKey);
   }

   protected String traceStoringPasswordToCache$str() {
      return "Storing password to the cache for key: %s";
   }

   public final void debugInsufficientMethodPermissions(Principal ejbPrincipal, String ejbName, String methodName, String interfaceName, String requiredRoles, String principalRoles, String runAsRoles) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000292: " + this.debugInsufficientMethodPermissions$str(), new Object[]{ejbPrincipal, ejbName, methodName, interfaceName, requiredRoles, principalRoles, runAsRoles});
   }

   protected String debugInsufficientMethodPermissions$str() {
      return "Insufficient method permissions [principal: %s, EJB name: %s, method: %s, interface: %s, required roles: %s, principal roles: %s, run-as roles: %s]";
   }

   public final void ambiguosKeyForSecurityVaultTransformation(String delimiter, String vaultBlock, String attributeName) {
      super.log.logf(FQCN, Level.INFO, (Throwable)null, "PBOX000369: " + this.ambiguosKeyForSecurityVaultTransformation$str(), delimiter, vaultBlock, attributeName);
   }

   protected String ambiguosKeyForSecurityVaultTransformation$str() {
      return "Ambiguos vault block and attribute name stored in original security vault. Delimiter (%s) is part of vault block or attribute name. Took the first delimiter. Result vault block (%s) attribute name (%s). Modify security vault manually.";
   }

   public final void traceExecuteQuery(String query, String username) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000263: " + this.traceExecuteQuery$str(), query, username);
   }

   protected String traceExecuteQuery$str() {
      return "Executing query %s with username %s";
   }

   public final void debugFailedLogin(Throwable t) {
      super.log.logf(FQCN, Level.DEBUG, t, "PBOX000206: " + this.debugFailedLogin$str(), new Object[0]);
   }

   protected String debugFailedLogin$str() {
      return "Login failure";
   }

   public final void cannotDeleteOriginalVaultFile(String file) {
      super.log.logf(FQCN, Level.WARN, (Throwable)null, "PBOX000370: " + this.cannotDeleteOriginalVaultFile$str(), file);
   }

   protected String cannotDeleteOriginalVaultFile$str() {
      return "Cannot delete original security vault file (%s). Delete the file manually before next start, please.";
   }

   public final void errorGettingJSSESecurityDomain(String domain) {
      super.log.logf(FQCN, Level.ERROR, (Throwable)null, "PBOX000246: " + this.errorGettingJSSESecurityDomain$str(), domain);
   }

   protected String errorGettingJSSESecurityDomain$str() {
      return "The JSSE security domain %s is not valid. All authentication using this login module will fail!";
   }

   public final void traceEndGetAliasAndCert() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000255: " + this.traceEndGetAliasAndCert$str(), new Object[0]);
   }

   protected String traceEndGetAliasAndCert$str() {
      return "End getAliasAndCert method";
   }

   public final void traceHostThreadLocalSet(String host, long threadId) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000328: " + this.traceHostThreadLocalSet$str(), host, threadId);
   }

   protected String traceHostThreadLocalSet$str() {
      return "Setting host %s on thread [id: %s]";
   }

   public final void errorDecryptingBindCredential(Throwable throwable) {
      super.log.logf(FQCN, Level.ERROR, throwable, "PBOX000219: " + this.errorDecryptingBindCredential$str(), new Object[0]);
   }

   protected String errorDecryptingBindCredential$str() {
      return "Exception while decrypting bindCredential";
   }

   public final void debugLoadConfigAsSun(URL configURL, Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000233: " + this.debugLoadConfigAsSun$str(), configURL);
   }

   protected String debugLoadConfigAsSun$str() {
      return "Failed to load config as XML. Try loading as Sun format from %s";
   }

   public final void traceUpdateCache(String inputSubject, String cachedSubject) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000207: " + this.traceUpdateCache$str(), inputSubject, cachedSubject);
   }

   protected String traceUpdateCache$str() {
      return "updateCache, input subject: %s, cached subject: %s";
   }

   public final void errorParsingTimeoutNumber() {
      super.log.logf(FQCN, Level.ERROR, (Throwable)null, "PBOX000366: " + this.errorParsingTimeoutNumber$str(), new Object[0]);
   }

   protected String errorParsingTimeoutNumber$str() {
      return "Error parsing time out number.";
   }

   public final void warnResolvingSystemIdAsNonFileURL(String systemId) {
      super.log.logf(FQCN, Level.WARN, (Throwable)null, "PBOX000346: " + this.warnResolvingSystemIdAsNonFileURL$str(), systemId);
   }

   protected String warnResolvingSystemIdAsNonFileURL$str() {
      return "Trying to resolve systemId %s as a non-file URL";
   }

   public final void traceAddPermissionsToExcludedPolicy(PermissionCollection permissions) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000309: " + this.traceAddPermissionsToExcludedPolicy$str(), permissions);
   }

   protected String traceAddPermissionsToExcludedPolicy$str() {
      return "addToExcludedPolicy, permission collection: %s";
   }

   public final void traceBeginDoesUserHaveRole(Principal principal, String roles) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000329: " + this.traceBeginDoesUserHaveRole$str(), principal, roles);
   }

   protected String traceBeginDoesUserHaveRole$str() {
      return "Begin doesUserHaveRole, principal: %s, roles: %s";
   }

   public final void traceImpliesMatchesExcludedSet(Permission permission) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000300: " + this.traceImpliesMatchesExcludedSet$str(), permission);
   }

   protected String traceImpliesMatchesExcludedSet$str() {
      return "Denied: matched excluded set, permission %s";
   }

   public final void traceNoMethodPermissions(String methodName, String interfaceName) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000290: " + this.traceNoMethodPermissions$str(), methodName, interfaceName);
   }

   protected String traceNoMethodPermissions$str() {
      return "No method permissions assigned to method: %s, interface: %s";
   }

   public final void debugFailureExecutingMethod(String methodName, Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000326: " + this.debugFailureExecutingMethod$str(), methodName);
   }

   protected String debugFailureExecutingMethod$str() {
      return "%s processing failed";
   }

   public final void traceNoPrincipalsInProtectionDomain(ProtectionDomain domain) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000303: " + this.traceNoPrincipalsInProtectionDomain$str(), domain);
   }

   protected String traceNoPrincipalsInProtectionDomain$str() {
      return "Not principals found in protection domain %s";
   }

   public final void traceStateMachineNextState(String action, String nextState) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000337: " + this.traceStateMachineNextState$str(), action, nextState);
   }

   protected String traceStateMachineNextState$str() {
      return "nextState for action %s: %s";
   }

   public final void tracePolicyConfigurationDelete(String contextID) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000315: " + this.tracePolicyConfigurationDelete$str(), contextID);
   }

   protected String tracePolicyConfigurationDelete$str() {
      return "delete, contextID: %s";
   }

   public final void traceRemoveRole(String roleName, String contextID) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000318: " + this.traceRemoveRole$str(), roleName, contextID);
   }

   protected String traceRemoveRole$str() {
      return "removeRole, role name: %s, contextID: %s";
   }

   public final void debugPasswordHashing(String algorithm, String encoding, String charset, String callback, String storeCallBack) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000281: " + this.debugPasswordHashing$str(), new Object[]{algorithm, encoding, charset, callback, storeCallBack});
   }

   protected String debugPasswordHashing$str() {
      return "Password hashing activated, algorithm: %s, encoding: %s, charset: %s, callback: %s, storeCallBack: %s";
   }

   public final void debugEJBPolicyModuleDelegateState(String methodName, String interfaceName, String requiredRoles) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000291: " + this.debugEJBPolicyModuleDelegateState$str(), methodName, interfaceName, requiredRoles);
   }

   protected String debugEJBPolicyModuleDelegateState$str() {
      return "Method: %s, interface: %s, required roles: %s";
   }

   public final void traceBeginValidateCredential() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000256: " + this.traceBeginValidateCredential$str(), new Object[0]);
   }

   protected String traceBeginValidateCredential$str() {
      return "Begin validateCredential method";
   }

   public final void traceNoPolicyContextForId(String contextID) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000306: " + this.traceNoPolicyContextForId$str(), contextID);
   }

   protected String traceNoPolicyContextForId$str() {
      return "No PolicyContext found for contextID %s";
   }

   public final void traceEndGetAppConfigEntryWithSuccess(String appName, String authInfo) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000224: " + this.traceEndGetAppConfigEntryWithSuccess$str(), appName, authInfo);
   }

   protected String traceEndGetAppConfigEntryWithSuccess$str() {
      return "End getAppConfigurationEntry(%s), AuthInfo: %s";
   }

   public final void traceQueryWithEmptyResult() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000321: " + this.traceQueryWithEmptyResult$str(), new Object[0]);
   }

   protected String traceQueryWithEmptyResult$str() {
      return "Query returned an empty result";
   }

   public final void traceCacheEntryLogoutFailure(Throwable t) {
      super.log.logf(FQCN, Level.TRACE, t, "PBOX000211: " + this.traceCacheEntryLogoutFailure$str(), new Object[0]);
   }

   protected String traceCacheEntryLogoutFailure$str() {
      return "Cache entry logout failed";
   }

   public final void debugFailureToOpenPropertiesFromURL(Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000287: " + this.debugFailureToOpenPropertiesFromURL$str(), new Object[0]);
   }

   protected String debugFailureToOpenPropertiesFromURL$str() {
      return "Failed to open properties file from URL";
   }

   public final void warnNullCredentialFromCallbackHandler() {
      super.log.logf(FQCN, Level.WARN, (Throwable)null, "PBOX000254: " + this.warnNullCredentialFromCallbackHandler$str(), new Object[0]);
   }

   protected String warnNullCredentialFromCallbackHandler$str() {
      return "CallbackHandler did not provide a credential";
   }

   public final void traceBeginResolveSystemIDasURL(String systemId) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000345: " + this.traceBeginResolveSystemIDasURL$str(), systemId);
   }

   protected String traceBeginResolveSystemIDasURL$str() {
      return "Begin resolveSystemIdasURL, systemId: %s";
   }

   public final void traceRemoveUncheckedPolicy(String contextID) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000319: " + this.traceRemoveUncheckedPolicy$str(), contextID);
   }

   protected String traceRemoveUncheckedPolicy$str() {
      return "removeUncheckedPolicy, contextID: %s";
   }

   public final void traceEndLogin(boolean loginOk) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000241: " + this.traceEndLogin$str(), loginOk);
   }

   protected String traceEndLogin$str() {
      return "End login method, isValid: %s";
   }

   public final void traceEndLoadConfigWithSuccess(URL configURL) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000230: " + this.traceEndLoadConfigWithSuccess$str(), configURL);
   }

   protected String traceEndLoadConfigWithSuccess$str() {
      return "End loadConfig, loginConfigURL: %s";
   }

   public final void traceFlushCacheEntry(String key) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000203: " + this.traceFlushCacheEntry$str(), key);
   }

   protected String traceFlushCacheEntry$str() {
      return "Flushing %s from security cache";
   }

   public final void traceBindingLDAPUsername(String username) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000266: " + this.traceBindingLDAPUsername$str(), username);
   }

   protected String traceBindingLDAPUsername$str() {
      return "Binding username %s";
   }

   public final void traceLogoutSubject(String loginContext, String subject) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000377: " + this.traceLogoutSubject$str(), loginContext, subject);
   }

   protected String traceLogoutSubject$str() {
      return "JAAS logout, login context: %s, subject: %s";
   }

   public final void errorFindingCharset(String charSet, Throwable throwable) {
      super.log.logf(FQCN, Level.ERROR, throwable, "PBOX000214: " + this.errorFindingCharset$str(), charSet);
   }

   protected String errorFindingCharset$str() {
      return "Charset %s not found. Using platform default";
   }

   public final void debugJACCDeniedAccess(String permission, Subject caller, String roles) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000289: " + this.debugJACCDeniedAccess$str(), permission, caller, roles);
   }

   protected String debugJACCDeniedAccess$str() {
      return "JACC delegate access denied [permission: %s, caller: %s, roles: %s";
   }

   public final void traceEndValidteCache(boolean isValid) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000205: " + this.traceEndValidteCache$str(), isValid);
   }

   protected String traceEndValidteCache$str() {
      return "End validateCache, result = %s";
   }

   public final void traceCheckSearchResult(String searchResult) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000275: " + this.traceCheckSearchResult$str(), searchResult);
   }

   protected String traceCheckSearchResult$str() {
      return "Checking search result %s";
   }

   public final void traceAssignUserToRole(String role) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000268: " + this.traceAssignUserToRole$str(), role);
   }

   protected String traceAssignUserToRole$str() {
      return "Assigning user to role %s";
   }

   public final void traceEndExecPasswordCmd(int exitCode) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000356: " + this.traceEndExecPasswordCmd$str(), exitCode);
   }

   protected String traceEndExecPasswordCmd$str() {
      return "End execPasswordCmd, exit code: %s";
   }

   public final void traceBeginExecPasswordCmd(String passwordCmd) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000355: " + this.traceBeginExecPasswordCmd$str(), passwordCmd);
   }

   protected String traceBeginExecPasswordCmd$str() {
      return "Begin execPasswordCmd, command: %s";
   }

   public final void traceMappedResourceToURL(String resource, URL url) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000349: " + this.traceMappedResourceToURL$str(), resource, url);
   }

   protected String traceMappedResourceToURL$str() {
      return "Mapped resource %s to URL %s";
   }

   public final void traceIgnoreXMLAttribute(String uri, String qName, String value) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000338: " + this.traceIgnoreXMLAttribute$str(), uri, qName, value);
   }

   protected String traceIgnoreXMLAttribute$str() {
      return "Ignore attribute [uri: %s, qname: %s, value: %s]";
   }

   public final void traceBeginGetAliasAndCert() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000252: " + this.traceBeginGetAliasAndCert$str(), new Object[0]);
   }

   protected String traceBeginGetAliasAndCert$str() {
      return "Begin getAliasAndCert method";
   }

   public final void traceRebindWithConfiguredPrincipal(String principal) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000272: " + this.traceRebindWithConfiguredPrincipal$str(), principal);
   }

   protected String traceRebindWithConfiguredPrincipal$str() {
      return "Rebind security principal to %s";
   }

   public final void warnFailureToValidateCertificate() {
      super.log.logf(FQCN, Level.WARN, (Throwable)null, "PBOX000259: " + this.warnFailureToValidateCertificate$str(), new Object[0]);
   }

   protected String warnFailureToValidateCertificate$str() {
      return "Failed to validate certificate: SecurityDomain, Keystore or certificate is null";
   }

   public final void debugNullAuthorizationManager(String securityDomain) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000324: " + this.debugNullAuthorizationManager$str(), securityDomain);
   }

   protected String debugNullAuthorizationManager$str() {
      return "AuthorizationManager is null for security domain %s";
   }

   public final void traceSystemIDMismatch(String systemId, String publicId, String registeredId) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000339: " + this.traceSystemIDMismatch$str(), systemId, publicId, registeredId);
   }

   protected String traceSystemIDMismatch$str() {
      return "systemId argument '%s' for publicId '%s' is different from the registered systemId '%s', resolution will be based on the argument";
   }

   public final void traceAddPermissionsToRole(PermissionCollection permissions) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000311: " + this.traceAddPermissionsToRole$str(), permissions);
   }

   protected String traceAddPermissionsToRole$str() {
      return "addToRole, permission collection: %s";
   }

   public final void traceBeginAbort() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000244: " + this.traceBeginAbort$str(), new Object[0]);
   }

   protected String traceBeginAbort$str() {
      return "Begin abort method";
   }

   public final void traceRetrievingPasswordFromCache(String newKey) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000363: " + this.traceRetrievingPasswordFromCache$str(), newKey);
   }

   protected String traceRetrievingPasswordFromCache$str() {
      return "Retrieving password from the cache for key: %s";
   }

   public final void traceValidatingUsingVerifier(Class verifier) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000257: " + this.traceValidatingUsingVerifier$str(), verifier);
   }

   protected String traceValidatingUsingVerifier$str() {
      return "Validating certificate using verifier %s";
   }

   public final void traceDBCertLoginModuleOptions(String dsJNDIName, String principalsQuery, String rolesQuery, boolean suspendResume) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000262: " + this.traceDBCertLoginModuleOptions$str(), new Object[]{dsJNDIName, principalsQuery, rolesQuery, suspendResume});
   }

   protected String traceDBCertLoginModuleOptions$str() {
      return "Module options [dsJndiName: %s, principalsQuery: %s, rolesQuery: %s, suspendResume: %s]";
   }

   public final void securityVaultContentVersion(String dataVersion, String targetVersion) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000367: " + this.securityVaultContentVersion$str(), dataVersion, targetVersion);
   }

   protected String securityVaultContentVersion$str() {
      return "Reading security vault data version %s target version is %s";
   }

   public final void traceDefaultLoginSubject(String loginContext, String subject) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000210: " + this.traceDefaultLoginSubject$str(), loginContext, subject);
   }

   protected String traceDefaultLoginSubject$str() {
      return "defaultLogin, login context: %s, subject: %s";
   }

   public final void traceFoundEntityFromID(String idName, String idValue, String fileName) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000342: " + this.traceFoundEntityFromID$str(), idName, idValue, fileName);
   }

   protected String traceFoundEntityFromID$str() {
      return "Found entity from %s: %s, filename: %s";
   }

   public final void debugRequisiteModuleFailure(String moduleName) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000298: " + this.debugRequisiteModuleFailure$str(), moduleName);
   }

   protected String debugRequisiteModuleFailure$str() {
      return "Requisite module %s failed";
   }

   public final void debugModuleOption(String optionName, Object optionValue) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000350: " + this.debugModuleOption$str(), optionName, optionValue);
   }

   protected String debugModuleOption$str() {
      return "Module option: %s, value: %s";
   }

   public final void traceBeginLogout() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000243: " + this.traceBeginLogout$str(), new Object[0]);
   }

   protected String traceBeginLogout$str() {
      return "Begin logout method";
   }

   public final void errorLoadingConfigFile(String filename, Throwable throwable) {
      super.log.logf(FQCN, Level.ERROR, throwable, "PBOX000212: " + this.errorLoadingConfigFile$str(), filename);
   }

   protected String errorLoadingConfigFile$str() {
      return "Exception loading file %s";
   }

   public final void traceSuccessfulLogInToLDAP(String context) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000271: " + this.traceSuccessfulLogInToLDAP$str(), context);
   }

   protected String traceSuccessfulLogInToLDAP$str() {
      return "Logged into LDAP server, context: %s";
   }

   public final void errorGettingServerAuthConfig(String layer, String appContext, Throwable cause) {
      super.log.logf(FQCN, Level.ERROR, cause, "PBOX000373: " + this.errorGettingServerAuthConfig$str(), layer, appContext);
   }

   protected String errorGettingServerAuthConfig$str() {
      return "Error getting ServerAuthConfig for layer %s and appContext %s";
   }

   public final void debugPasswordNotACertificate() {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000249: " + this.debugPasswordNotACertificate$str(), new Object[0]);
   }

   protected String debugPasswordNotACertificate$str() {
      return "javax.security.auth.login.password is not a X509Certificate";
   }

   public final void traceAdditionOfRoleToGroup(String roleName, String groupName) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000285: " + this.traceAdditionOfRoleToGroup$str(), roleName, groupName);
   }

   protected String traceAdditionOfRoleToGroup$str() {
      return "Adding role %s to group %s";
   }

   public final void traceNoAuditContextFoundForDomain(String securityDomain) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000323: " + this.traceNoAuditContextFoundForDomain$str(), securityDomain);
   }

   protected String traceNoAuditContextFoundForDomain$str() {
      return "No audit context found for security domain %s; using default context";
   }

   public final void traceImpliesMatchesUncheckedSet(Permission permission) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000301: " + this.traceImpliesMatchesUncheckedSet$str(), permission);
   }

   protected String traceImpliesMatchesUncheckedSet$str() {
      return "Allowed: matched unchecked set, permission %s";
   }

   public final void traceRemoveExcludedPolicy(String contextID) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000317: " + this.traceRemoveExcludedPolicy$str(), contextID);
   }

   protected String traceRemoveExcludedPolicy$str() {
      return "removeExcludedPolicy, contextID: %s";
   }

   public final void warnFailureToCreateUnauthIdentity(Throwable throwable) {
      super.log.logf(FQCN, Level.WARN, throwable, "PBOX000238: " + this.warnFailureToCreateUnauthIdentity$str(), new Object[0]);
   }

   protected String warnFailureToCreateUnauthIdentity$str() {
      return "Failed to create custom unauthenticated identity";
   }

   public final void traceBeginInitialize() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000236: " + this.traceBeginInitialize$str(), new Object[0]);
   }

   protected String traceBeginInitialize$str() {
      return "Begin initialize method";
   }

   public final void errorConvertingUsernameUTF8(Throwable throwable) {
      super.log.logf(FQCN, Level.ERROR, throwable, "PBOX000213: " + this.errorConvertingUsernameUTF8$str(), new Object[0]);
   }

   protected String errorConvertingUsernameUTF8$str() {
      return "Failed to convert username to byte[] using UTF-8";
   }

   public final void traceBeginLoadConfig(URL configURL) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000229: " + this.traceBeginLoadConfig$str(), configURL);
   }

   protected String traceBeginLoadConfig$str() {
      return "Begin loadConfig, loginConfigURL: %s";
   }

   public final void debugFailureToResolveEntity(String systemId, String publicId) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000340: " + this.debugFailureToResolveEntity$str(), systemId, publicId);
   }

   protected String debugFailureToResolveEntity$str() {
      return "Cannot resolve entity, systemId: %s, publicId: %s";
   }

   public final void mixedVaultDataFound(String vaultDatFile, String encDatFile, String encDatFile2) {
      super.log.logf(FQCN, Level.ERROR, (Throwable)null, "PBOX000368: " + this.mixedVaultDataFound$str(), vaultDatFile, encDatFile, encDatFile2);
   }

   protected String mixedVaultDataFound$str() {
      return "Security Vault contains both covnerted (%s) and pre-conversion data (%s). Try to delete %s file and start over again.";
   }

   public final void traceUsingUnauthIdentity(String unauthenticatedIdentity) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000250: " + this.traceUsingUnauthIdentity$str(), unauthenticatedIdentity);
   }

   protected String traceUsingUnauthIdentity$str() {
      return "Authenticating using unauthenticated identity %s";
   }

   public final void traceBeginResolveSystemID(String systemId) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000344: " + this.traceBeginResolveSystemID$str(), systemId);
   }

   protected String traceBeginResolveSystemID$str() {
      return "Begin resolveSystemId, systemId: %s";
   }

   public final void warnEndLoadConfigWithFailure(URL configURL, Throwable throwable) {
      super.log.logf(FQCN, Level.WARN, throwable, "PBOX000231: " + this.warnEndLoadConfigWithFailure$str(), configURL);
   }

   protected String warnEndLoadConfigWithFailure$str() {
      return "End loadConfig, failed to load config: %s";
   }

   public final void traceFoundUserRolesContextDN(String context) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000273: " + this.traceFoundUserRolesContextDN$str(), context);
   }

   protected String traceFoundUserRolesContextDN$str() {
      return "Found user roles context DN: %s";
   }

   public final void traceCreateDigestCallback(String callback) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000284: " + this.traceCreateDigestCallback$str(), callback);
   }

   protected String traceCreateDigestCallback$str() {
      return "Created DigestCallback %s";
   }

   public final void errorGettingServerAuthContext(String authContextId, String securityDomain, Throwable cause) {
      super.log.logf(FQCN, Level.ERROR, cause, "PBOX000374: " + this.errorGettingServerAuthContext$str(), authContextId, securityDomain);
   }

   protected String errorGettingServerAuthContext$str() {
      return "Error getting ServerAuthContext for authContextId %s and security domain %s";
   }

   public final void warnFailureToFindCertForAlias(String alias, Throwable throwable) {
      super.log.logf(FQCN, Level.WARN, throwable, "PBOX000258: " + this.warnFailureToFindCertForAlias$str(), alias);
   }

   protected String warnFailureToFindCertForAlias$str() {
      return "Failed to find certificate for alias &%s";
   }

   public final void traceEndInitialize() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000239: " + this.traceEndInitialize$str(), new Object[0]);
   }

   protected String traceEndInitialize$str() {
      return "End initialize method";
   }

   public final void errorCheckingStrongJurisdictionPolicyFiles(Throwable throwable) {
      super.log.logf(FQCN, Level.ERROR, throwable, "PBOX000217: " + this.errorCheckingStrongJurisdictionPolicyFiles$str(), new Object[0]);
   }

   protected String errorCheckingStrongJurisdictionPolicyFiles$str() {
      return "Failed to check if the strong jurisdiction policy files have been installed";
   }

   public final void debugFailureToCreatePrincipal(String name, Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000264: " + this.debugFailureToCreatePrincipal$str(), name);
   }

   protected String debugFailureToCreatePrincipal$str() {
      return "Failed to create principal %s";
   }

   public final void traceRolesBeforeMapping(String roles) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000331: " + this.traceRolesBeforeMapping$str(), roles);
   }

   protected String traceRolesBeforeMapping$str() {
      return "Roles before mapping: %s";
   }

   public final void traceUnauthenticatedIdentity(String name) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000237: " + this.traceUnauthenticatedIdentity$str(), name);
   }

   protected String traceUnauthenticatedIdentity$str() {
      return "Saw unauthenticated indentity: %s";
   }

   public final void traceProtectionDomainPrincipals(List principalNames) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000302: " + this.traceProtectionDomainPrincipals$str(), principalNames);
   }

   protected String traceProtectionDomainPrincipals$str() {
      return "Protection domain principals: %s";
   }

   public final void traceRegisterPolicy(String contextID, String type, String location) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000334: " + this.traceRegisterPolicy$str(), contextID, type, location);
   }

   protected String traceRegisterPolicy$str() {
      return "Registered policy for contextID: %s, type: %s, location: %s";
   }

   public final void traceRolesDNSearch(String dn, String roleFilter, String filterArgs, String roleAttr, int searchScope, int searchTimeLimit) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000274: " + this.traceRolesDNSearch$str(), new Object[]{dn, roleFilter, filterArgs, roleAttr, searchScope, searchTimeLimit});
   }

   protected String traceRolesDNSearch$str() {
      return "Searching rolesCtxDN %s with roleFilter: %s, filterArgs: %s, roleAttr: %s, searchScope: %s, searchTimeLimit: %s";
   }

   public final void traceRejectingEmptyPassword() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000267: " + this.traceRejectingEmptyPassword$str(), new Object[0]);
   }

   protected String traceRejectingEmptyPassword$str() {
      return "Rejecting empty password as allowEmptyPasswords option has not been set to true";
   }

   public final void traceLinkConfiguration(String contextID) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000316: " + this.traceLinkConfiguration$str(), contextID);
   }

   protected String traceLinkConfiguration$str() {
      return "linkConfiguration, link to contextID: %s";
   }

   public final void traceBeginValidateCache(String info, Class credentialClass) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000204: " + this.traceBeginValidateCache$str(), info, credentialClass);
   }

   protected String traceBeginValidateCache$str() {
      return "Begin validateCache, domainInfo: %s, credential class: %s";
   }

   public final void traceHostThreadLocalGet(String host, long threadId) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000327: " + this.traceHostThreadLocalGet$str(), host, threadId);
   }

   protected String traceHostThreadLocalGet$str() {
      return "Returning host %s from thread [id: %s]";
   }

   public final void traceAddPermissionsToUncheckedPolicy(PermissionCollection permissions) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000313: " + this.traceAddPermissionsToUncheckedPolicy$str(), permissions);
   }

   protected String traceAddPermissionsToUncheckedPolicy$str() {
      return "addToUncheckedPolicy, permission collection: %s";
   }

   public final void debugMappingProviderOptions(Principal principal, Map principalRolesMap, Set subjectPrincipals) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000322: " + this.debugMappingProviderOptions$str(), principal, principalRolesMap, subjectPrincipals);
   }

   protected String debugMappingProviderOptions$str() {
      return "Mapping provider options [principal: %s, principal to roles map: %s, subject principals: %s]";
   }

   public final void debugRealHostForTrust(String host) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000279: " + this.debugRealHostForTrust$str(), host);
   }

   protected String debugRealHostForTrust$str() {
      return "The real host for trust is %s";
   }

   public final void traceBeginResolveClasspathName(String systemId) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000347: " + this.traceBeginResolveClasspathName$str(), systemId);
   }

   protected String traceBeginResolveClasspathName$str() {
      return "Begin resolveClasspathName, systemId: %s";
   }

   public final void warnModuleCreationWithEmptyPassword() {
      super.log.logf(FQCN, Level.WARN, (Throwable)null, "PBOX000360: " + this.warnModuleCreationWithEmptyPassword$str(), new Object[0]);
   }

   protected String warnModuleCreationWithEmptyPassword$str() {
      return "Creating login module with empty password";
   }

   public final void debugNullAuthenticationManager(String securityDomain) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000336: " + this.debugNullAuthenticationManager$str(), securityDomain);
   }

   protected String debugNullAuthenticationManager$str() {
      return "AuthenticationManager is null for security domain %s";
   }

   public final void errorUsingDisabledDomain(String securityDomain) {
      super.log.logf(FQCN, Level.ERROR, (Throwable)null, "PBOX000265: " + this.errorUsingDisabledDomain$str(), securityDomain);
   }

   protected String errorUsingDisabledDomain$str() {
      return "The security domain %s has been disabled. All authentication will fail";
   }

   public final void traceHasResourcePermission(String permission, boolean allowed) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000295: " + this.traceHasResourcePermission$str(), permission, allowed);
   }

   protected String traceHasResourcePermission$str() {
      return "hasResourcePermission, permission: %s, allowed: %s";
   }

   public final void traceAddAppConfig(String appName, String authInfo) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000226: " + this.traceAddAppConfig$str(), appName, authInfo);
   }

   protected String traceAddAppConfig$str() {
      return "addAppConfig(%s), AuthInfo: %s";
   }

   public final void traceGetAppConfigEntryViaParent(String appName, String parentConfig) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000222: " + this.traceGetAppConfigEntryViaParent$str(), appName, parentConfig);
   }

   protected String traceGetAppConfigEntryViaParent$str() {
      return "getAppConfigurationEntry(%s), no entry found, trying parent config %s";
   }

   public final void traceEndGetAppConfigEntryWithFailure(String appName) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000225: " + this.traceEndGetAppConfigEntryWithFailure$str(), appName);
   }

   protected String traceEndGetAppConfigEntryWithFailure$str() {
      return "End getAppConfigurationEntry(%s), failed to find entry";
   }

   public final void errorCalculatingPasswordHash(Throwable throwable) {
      super.log.logf(FQCN, Level.ERROR, throwable, "PBOX000216: " + this.errorCalculatingPasswordHash$str(), new Object[0]);
   }

   protected String errorCalculatingPasswordHash$str() {
      return "Password hash calculation failed";
   }

   public final void debugImpliesResult(boolean implies) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000305: " + this.debugImpliesResult$str(), implies);
   }

   protected String debugImpliesResult$str() {
      return "Checking result, implies: %s";
   }

   public final void traceAttemptToLoadResource(String resourceURL) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000286: " + this.traceAttemptToLoadResource$str(), resourceURL);
   }

   protected String traceAttemptToLoadResource$str() {
      return "Attempting to load resource %s";
   }

   public final void traceRolesAfterMapping(String roles) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000332: " + this.traceRolesAfterMapping$str(), roles);
   }

   protected String traceRolesAfterMapping$str() {
      return "Roles after mapping: %s";
   }

   public final void tracePropertiesFileLoaded(String fileName, Set users) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000288: " + this.tracePropertiesFileLoaded$str(), fileName, users);
   }

   protected String tracePropertiesFileLoaded$str() {
      return "Properties file %s loaded, users: %s";
   }

   public final void traceJSSEDomainGetKey(String alias) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000352: " + this.traceJSSEDomainGetKey$str(), alias);
   }

   protected String traceJSSEDomainGetKey$str() {
      return "JSSE domain got request for key with alias %s";
   }

   public final void traceJSSEDomainGetCertificate(String alias) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000353: " + this.traceJSSEDomainGetCertificate$str(), alias);
   }

   protected String traceJSSEDomainGetCertificate$str() {
      return "JSSE domain got request for certificate with alias %s";
   }

   public final void traceLDAPConnectionEnv(Properties env) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000220: " + this.traceLDAPConnectionEnv$str(), env);
   }

   protected String traceLDAPConnectionEnv$str() {
      return "Logging into LDAP server with env %s";
   }

   public final void warnFailureToFindConfig(String loginConfig) {
      super.log.logf(FQCN, Level.WARN, (Throwable)null, "PBOX000228: " + this.warnFailureToFindConfig$str(), loginConfig);
   }

   protected String warnFailureToFindConfig$str() {
      return "Failed to find config: %s";
   }

   public final void infoVaultInitialized() {
      super.log.logf(FQCN, Level.INFO, (Throwable)null, "PBOX000361: " + this.infoVaultInitialized$str(), new Object[0]);
   }

   protected String infoVaultInitialized$str() {
      return "Default Security Vault Implementation Initialized and Ready";
   }

   public final void warnInvalidModuleOption(String option) {
      super.log.logf(FQCN, Level.WARN, (Throwable)null, "PBOX000234: " + this.warnInvalidModuleOption$str(), option);
   }

   protected String warnInvalidModuleOption$str() {
      return "Invalid or misspelled module option: %s";
   }

   public final void keyStoreConvertedToJCEKS(String keyStoreFile) {
      super.log.logf(FQCN, Level.INFO, (Throwable)null, "PBOX000372: " + this.keyStoreConvertedToJCEKS$str(), keyStoreFile);
   }

   protected String keyStoreConvertedToJCEKS$str() {
      return "Security Vault key store successfuly converted to JCEKS type (%s). From now on use JCEKS as KEYSTORE_TYPE in Security Vault configuration.";
   }

   public final void traceSecurityDomainFound(String domain) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000245: " + this.traceSecurityDomainFound$str(), domain);
   }

   protected String traceSecurityDomainFound$str() {
      return "Found security domain: %s";
   }

   public final void traceHasRolePermission(String permission, boolean allowed) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000296: " + this.traceHasRolePermission$str(), permission, allowed);
   }

   protected String traceHasRolePermission$str() {
      return "hasRolePermission, permission: %s, allowed: %s";
   }

   public final void errorCreatingCertificateVerifier(Throwable throwable) {
      super.log.logf(FQCN, Level.ERROR, throwable, "PBOX000248: " + this.errorCreatingCertificateVerifier$str(), new Object[0]);
   }

   protected String errorCreatingCertificateVerifier$str() {
      return "Failed to create X509CertificateVerifier";
   }

   public final void warnSecurityMagementNotSet() {
      super.log.logf(FQCN, Level.WARN, (Throwable)null, "PBOX000335: " + this.warnSecurityMagementNotSet$str(), new Object[0]);
   }

   protected String warnSecurityMagementNotSet$str() {
      return "SecurityManagement is not set, creating a default one";
   }

   public final void traceBindDNNotFound() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000218: " + this.traceBindDNNotFound$str(), new Object[0]);
   }

   protected String traceBindDNNotFound$str() {
      return "bindDN is not found";
   }

   public final void traceBeginLogin() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000240: " + this.traceBeginLogin$str(), new Object[0]);
   }

   protected String traceBeginLogin$str() {
      return "Begin login method";
   }

   public final void traceGetAppConfigEntryViaDefault(String appName, String defaultConfig) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000223: " + this.traceGetAppConfigEntryViaDefault$str(), appName, defaultConfig);
   }

   protected String traceGetAppConfigEntryViaDefault$str() {
      return "getAppConfigurationEntry(%s), no entry in parent config, trying default %s";
   }

   public final void errorLoadingUserRolesPropertiesFiles(Throwable throwable) {
      super.log.logf(FQCN, Level.ERROR, throwable, "PBOX000261: " + this.errorLoadingUserRolesPropertiesFiles$str(), new Object[0]);
   }

   protected String errorLoadingUserRolesPropertiesFiles$str() {
      return "Failed to load users/passwords/roles files";
   }

   public final void traceBeginGetRoleSets() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000358: " + this.traceBeginGetRoleSets$str(), new Object[0]);
   }

   protected String traceBeginGetRoleSets$str() {
      return "Begin getRoleSets";
   }

   public final void debugFailureToParseNumberProperty(String property, long defaultValue) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000269: " + this.debugFailureToParseNumberProperty$str(), property, defaultValue);
   }

   protected String debugFailureToParseNumberProperty$str() {
      return "Failed to parse %s as number, using default value %s";
   }

   public final void errorFindingSecurityDomain(String domain, Throwable throwable) {
      super.log.logf(FQCN, Level.ERROR, throwable, "PBOX000247: " + this.errorFindingSecurityDomain$str(), domain);
   }

   protected String errorFindingSecurityDomain$str() {
      return "Unable to find the security domain %s";
   }

   public final void debugRequiredModuleFailure(String moduleName) {
      super.log.logf(FQCN, Level.DEBUG, (Throwable)null, "PBOX000299: " + this.debugRequiredModuleFailure$str(), moduleName);
   }

   protected String debugRequiredModuleFailure$str() {
      return "Required module %s failed";
   }

   public final void debugFailureToInstantiateClass(String className, Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000282: " + this.debugFailureToInstantiateClass$str(), className);
   }

   protected String debugFailureToInstantiateClass$str() {
      return "Failed to instantiate class %s";
   }

   public final void warnFailureToLoadIDFromResource(String idName, String resourceType, String resourceName) {
      super.log.logf(FQCN, Level.WARN, (Throwable)null, "PBOX000343: " + this.warnFailureToLoadIDFromResource$str(), idName, resourceType, resourceName);
   }

   protected String warnFailureToLoadIDFromResource$str() {
      return "Cannot load %s from %s resource: %s";
   }

   public final void debugFailureToCreateIdentityForAlias(String alias, Throwable throwable) {
      super.log.logf(FQCN, Level.DEBUG, throwable, "PBOX000251: " + this.debugFailureToCreateIdentityForAlias$str(), alias);
   }

   protected String debugFailureToCreateIdentityForAlias$str() {
      return "Failed to create identity for alias %s";
   }

   public final void traceAddPermissionToExcludedPolicy(Permission permission) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000308: " + this.traceAddPermissionToExcludedPolicy$str(), permission);
   }

   protected String traceAddPermissionToExcludedPolicy$str() {
      return "addToExcludedPolicy, permission: %s";
   }

   public final void traceEndValidateCredential(boolean isValid) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000260: " + this.traceEndValidateCredential$str(), isValid);
   }

   protected String traceEndValidateCredential$str() {
      return "End validateCredential method, result: %s";
   }

   public final void traceFlushWholeCache() {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000202: " + this.traceFlushWholeCache$str(), new Object[0]);
   }

   protected String traceFlushWholeCache$str() {
      return "Flushing all entries from security cache";
   }

   public final void traceEndIsValid(boolean isValid) {
      super.log.logf(FQCN, Level.TRACE, (Throwable)null, "PBOX000201: " + this.traceEndIsValid$str(), isValid);
   }

   protected String traceEndIsValid$str() {
      return "End isValid, result = %s";
   }
}
