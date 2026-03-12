package fr.xephi.authme.libs.org.jboss.security;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyException;
import java.security.ProviderException;
import java.util.Arrays;
import java.util.List;
import javax.management.AttributeNotFoundException;
import javax.naming.NamingException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;

public class PicketBoxMessages_$bundle implements Serializable, PicketBoxMessages {
   private static final long serialVersionUID = 1L;
   private static final String projectCode = "PBOX";
   public static final PicketBoxMessages_$bundle INSTANCE = new PicketBoxMessages_$bundle();
   private static final String unableToStoreKeyStoreToFile = "Unable to store keystore to file (%s)";
   private static final String unexpectedSecurityDomainInContext = "The context security domain does not match expected domain %s";
   private static final String failedToObtainInfoFromAppPolicy = "Application policy has no info of type %s";
   private static final String failedToCreatePrincipal = "Failed to create principal: %s";
   private static final String failedToValidateURL = "Failed to validate %s as a URL, file or classpath resource";
   private static final String invalidBase64String = "Invalid Base64 string: %s";
   private static final String failedToGetTransactionManager = "Unable to get TransactionManager";
   private static final String failedToObtainPassword = "Failed to obtain the password";
   private static final String invalidNullBaseContextDN = "Invalid configuration: baseCtxDN is null";
   private static final String failedToObtainAuthenticationInfo = "AuthenticationInfo not set in security domain %s";
   private static final String missingCallerInfoMessage = "Either caller subject or caller run-as should be non-null";
   private static final String failedToObtainSecDomainFromContextOrConfig = "Failed to obtain security domain from security context or configuration";
   private static final String invalidKeyFormat = "Invalid key format: %s";
   private static final String invalidDelegateMapEntry = "Invalid delegate map entry: %s";
   private static final String failedToParseJACCStatesConfigFile = "Failed to parse jacc-policy-config-states.xml";
   private static final String unableToWriteVaultDataFileMessage = "Unable to write vault data file (%s)";
   private static final String failedToInstantiateDelegateModule = "Failed to instantiate delegate module %s";
   private static final String unableToParseReferralAbsoluteName = "Unable to parse referral absolute name: %s";
   private static final String vaultNotInitializedMessage = "Vault is not initialized";
   private static final String unexpectedSecurityDomainInInfo = "Application policy -> %s does not match expected security domain %s";
   private static final String invalidNullOrEmptyOptionMap = "Options map %s is null or empty";
   private static final String unexpectedNamespace = "Unexpected namespace %s encountered";
   private static final String failedToInstantiateClassMessage = "Failed to instantiate %s class";
   private static final String unableToFindSchema = "Unable to find schema file %s";
   private static final String identityTypeFactoryNotImplemented = "IdentityFactory for type %s not implemented";
   private static final String noPolicyContextForIdMessage = "No PolicyContext exists for contextID %s";
   private static final String pbeUtilsMessage = "Ecrypt a password using the JaasSecurityDomain password\n\nUsage: PBEUtils salt count domain-password password\n  salt : the Salt attribute from the JaasSecurityDomain\n  count : the IterationCount attribute from the JaasSecurityDomain\n  domain-password : the plaintext password that maps to the KeyStorePass attribute from the JaasSecurityDomain\n  password : the plaintext password that should be encrypted with the JaasSecurityDomain password\n";
   private static final String failedToFindResource = "Failed to find resource %s";
   private static final String unableToFollowReferralForAuth = "Unable to follow referral for authentication: %s";
   private static final String enterUsernameMessage = "Enter the username: ";
   private static final String missingRequiredAttributes = "Missing required attribute(s): %s";
   private static final String missingSystemProperty = "The specified system property %s is missing";
   private static final String failedToDecodeBindCredential = "Failed to decode bindCredential";
   private static final String unableToLoadPropertiesFile = "Properties file %s not available for loading";
   private static final String failedToObtainSHAMessageDigest = "Failed to obtain SHA MessageDigest";
   private static final String failedToResolveTargetStateMessage = "Failed to resolve target state %s for transition %s";
   private static final String unableToCreateVaultMessage = "Unable to instantiate vault class";
   private static final String missingDelegateForLayer = "Delegate is missing for layer %s";
   private static final String invalidKeystorePasswordFormatMessage = "Keystore password should be either masked or prefixed with one of {EXT}, {EXTC}, {CMD}, {CMDC}, {CLASS}";
   private static final String invalidThreadContextClassLoader = "Thread context classloader has not been set";
   private static final String failedToCreateSealedObject = "Failed to create SealedObject";
   private static final String noMatchForAliasMessage = "No match for alias %s, existing aliases: %s";
   private static final String unsupportedQOP = "Unsupported quality of protection: %s";
   private static final String invalidSecurityAnnotationConfig = "Invalid annotation configuration: either @SecurityConfig or @Authentication is needed";
   private static final String failedToLookupDataSourceMessage = "Error looking up DataSource from %s";
   private static final String missingXACMLPolicyForContextId = "Missing XACML policy for contextID %s";
   private static final String sharedKeyMismatchMessage = "Vault mismatch: shared key does not match for vault block %s and attribute name %s";
   private static final String deniedByIdentityTrustMessage = "Caller denied by identity trust framework";
   private static final String invalidLoginModuleStackRef = "auth-module references a login module stack that doesn't exist: %s";
   private static final String unableToGetPrincipalOrCredsForAssociation = "Unable to get the calling principal or its credentials for resource association";
   private static final String invalidType = "Class is not an instance of %s";
   private static final String missingServiceAuthToken = "JSSE domain %s has been requested to provide sensitive security information, but no service authentication token has been configured on it. Use setServiceAuthToken()";
   private static final String filePasswordUsageMessage = "Write a password in opaque form to a file for use with the FilePassword accessor\n\nUsage: FilePassword salt count password password-file\n  salt  : an 8 char sequence for PBEKeySpec\n  count : iteration count for PBEKeySpec\n  password : the clear text password to write\n  password-file : the path to the file to write the password to\n";
   private static final String unableToEncryptDataMessage = "Unable to encrypt data";
   private static final String unableToInitializeLoginContext = "Unable to initialize login context";
   private static final String invalidNullSecurityContext = "Unable to proceed: security context is null";
   private static final String invalidNullKeyStoreURL = "Cannot load KeyStore of type %s: required keyStoreURL is null";
   private static final String noSecretKeyandAliasAlreadyUsed = "There is no SecretKey under the alias (%s) and the alias is already used to denote diffrent crypto object in the keystore.";
   private static final String unexpectedElement = "Unexpected element %s encountered";
   private static final String invalidPasswordType = "Invalid password type: %s";
   private static final String cacheValidationFailedMessage = "Cache validation failed";
   private static final String missingPropertiesFile = "Missing properties file: %s";
   private static final String base64EncoderMessage = "Usage: Base64Encoder <string> <optional hash algorithm>";
   private static final String invalidPermissionChecks = "Permission checks must be different";
   private static final String unableToFindSetSecurityInfoMessage = "Unable to find setSecurityInfo(Principal, Object) in CallbackHandler";
   private static final String invalidEJBVersion = "Invalid EJB version: %s";
   private static final String sizeMismatchMessage = "Size mismatch between %s and %s";
   private static final String missingXMLUserRolesMapping = "Missing XML configuration for user/roles mapping";
   private static final String failedToObtainAuthorizationInfo = "AuthorizationInfo not set in security domain %s";
   private static final String failedToObtainUsername = "Failed to obtain the username";
   private static final String failedToMatchCredential = "Supplied credential did not match existing credential for alias %s";
   private static final String invalidVaultStringFormat = "Invalid vaultString format: %s";
   private static final String invalidNullAuthConfigProviderForLayer = "AuthConfigProvider is null for layer %s, contextID: %s";
   private static final String noMatchingRoleFoundInDescriptor = "No matching role found in deployment descriptor for role %s";
   private static final String cacheMissMessage = "Cache miss";
   private static final String unableToCreateACLPersistenceStrategy = "Failed to instantiate persistence strategy class";
   private static final String unrecognizedVaultContentVersion = "Unrecognized security vault content version (%s), expecting (from %s to %s)";
   private static final String failedToRetrieveCertificateMessage = "Failed to retrieve certificate from keystore using alias %s";
   private static final String unableToInitSecurityFactory = "Unable to initialize security factory";
   private static final String invalidPassword = "Password invalid/Password required";
   private static final String unableToWriteShareKeyFileMessage = "Unable to write shared key file";
   private static final String unableToGetCertificateFromClass = "Unable to obtain a X509Certificate from %s";
   private static final String existingCredentialMessage = "Existing credential: ";
   private static final String unableToLoadVaultMessage = "Unable to load vault class";
   private static final String failedToProcessQueryMessage = "Error processing query";
   private static final String illegalBase64Character = "Illegal Base64 character";
   private static final String unableToFindPropertiesFile = "Properties file %s not found";
   private static final String identityTrustValidationFailedMessage = "Identity trust validation failed";
   private static final String vaultDoesnotContainSecretKey = "Security Vault does not contain SecretKey entry under alias (%s)";
   private static final String noMatchingUsernameFoundInPrincipals = "No matching username found in principals";
   private static final String noCallbackHandlerAvailable = "No CallbackHandler available to collect authentication information";
   private static final String missingAdminKeyInOriginalVaultData = "Security Vault conversion unsuccessful missing admin key in original vault data";
   private static final String missingRequiredModuleOptionMessage = "Missing required module option: %s";
   private static final String noServerAuthModuleForRequestType = "No ServerAuthModule configured to support type %s";
   private static final String failedToObtainApplicationPolicy = "Failed to obtain ApplicationPolicy for domain %s";
   private static final String invalidUnmaskedKeystorePasswordMessage = "Keystore password is not masked";
   private static final String unableToLocateMBeanServer = "Unable to locate MBean server";
   private static final String fileOrDirectoryDoesNotExistMessage = "File or directory %s does not exist";
   private static final String invalidControlFlag = "Invalid control flag: %s";
   private static final String invalidPolicyRegistrationType = "Unsupported policy registration type: %s";
   private static final String nullRolesInSubjectMessage = "Subject contains a null set of roles";
   private static final String unableToGetPasswordFromVault = "Unable to get password value from vault";
   private static final String unableToGetKeyStore = "Unable to get keystore (%s)";
   private static final String failedToRegisterAuthConfigProvider = "Failed to register AuthConfigProvider %s";
   private static final String invalidNullTransactionManager = "Invalid null TransactionManager";
   private static final String callbackHandlerSysPropertyNotSet = "CallbackHandler not specified by system property %s";
   private static final String unableToLookupDataSource = "Unable to lookup DataSource - the DS JNDI name is null";
   private static final String authenticationFailedMessage = "Access denied: authentication failed";
   private static final String failedToCreateDocumentBuilder = "Failed to create DocumentBuilder";
   private static final String failedToInvokeCallbackHandler = "Failed to invoke CallbackHandler";
   private static final String unsupportedAlgorithm = "Unsupported algorithm: %s";
   private static final String aclEntryPermissionAlreadySet = "ACLEntry permission has already been set";
   private static final String noMatchingUsernameFoundInRoles = "No matching username found in roles";
   private static final String failedToRetrievePublicKeyMessage = "Failed to retrieve public key from keystore using alias %s";
   private static final String invalidNullProperty = "The property %s is null";
   private static final String unableToLocateACLForResourceMessage = "Unable to locate ACL for resource %s";
   private static final String invalidNullOrEmptyOptionMessage = "Option %s is null or empty";
   private static final String invalidTransitionForActionMessage = "No transition for action %s from state %s ";
   private static final String malformedIdentityString = "Malformed identity string: %s. Expected Identity_Class:Identity_Name";
   private static final String failedToVerifyServiceAuthToken = "Service authentication token verification failed";
   private static final String invalidNullLoginConfig = "'java.security.auth.login.config' system property not set and auth.conf file not present";
   private static final String authorizationFailedMessage = "Acces denied: authorization failed";
   private static final String moduleAbortFailedMessage = "Invocation of abort on module failed";
   private static final String invalidSharedKeyMessage = "The shared key is invalid or has been incorrectly encoded";
   private static final String invalidDirectoryFormatMessage = "Directory %s does not end with / or \\";
   private static final String invalidPasswordCommandType = "Invalid password command type: %s";
   private static final String failedToMatchStrings = "Failed to match %s and %s";
   private static final String aclResourceAlreadySet = "ACL resource has already been set";
   private static final String failedToFindNamespaceURI = "Failed to find namespace URI for %s";
   private static final String invalidNullArgument = "Argument %s cannot be null";
   private static final String moduleCommitFailedMessage = "Invocation of commit on module failed";
   private static final String mixedVaultDataFound = "Security Vault contains both covnerted (%s) and pre-conversion data (%s), failed to load vault";
   private static final String enterPasswordMessage = "Enter the password: ";
   private static final String suppliedCredentialMessage = "Supplied credential: ";
   private static final String failedToCreateSecretKeySpec = "Failed to create SecretKeySpec from session key";
   private static final String unableToLocateACLWithNoStrategyMessage = "Unable to locate ACL: persistence strategy has not been set";
   private static final String failedToFindBaseContextDN = "Search for context %s found no results";
   private static final String unableToHandleCallback = "%s does not handle a callback of type %s";
   private static final String invalidMBeanAttribute = "%s is not an MBean attribute";
   private static final String unexpectedAttribute = "Unexpected attribute %s encountered";
   private static final String unableToFindPrincipalInDB = "No matching principal found in DB: %s";
   private static final String operationNotAllowedMessage = "Operation not allowed";
   private static final String unexpectedExceptionDuringSecretKeyCreation = "Unexpected exception during SecretKeySpec creation";

   protected PicketBoxMessages_$bundle() {
   }

   protected PicketBoxMessages_$bundle readResolve() {
      return INSTANCE;
   }

   public final RuntimeException unableToStoreKeyStoreToFile(Throwable throwable, String file) {
      RuntimeException result = new RuntimeException(String.format("PBOX000139: " + this.unableToStoreKeyStoreToFile$str(), file), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToStoreKeyStoreToFile$str() {
      return "Unable to store keystore to file (%s)";
   }

   public final IllegalArgumentException unexpectedSecurityDomainInContext(String securityDomain) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000103: " + this.unexpectedSecurityDomainInContext$str(), securityDomain));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unexpectedSecurityDomainInContext$str() {
      return "The context security domain does not match expected domain %s";
   }

   public final IllegalStateException failedToObtainInfoFromAppPolicy(String infoType) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000097: " + this.failedToObtainInfoFromAppPolicy$str(), infoType));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToObtainInfoFromAppPolicy$str() {
      return "Application policy has no info of type %s";
   }

   public final LoginException failedToCreatePrincipal(String message) {
      LoginException result = new LoginException(String.format("PBOX000051: " + this.failedToCreatePrincipal$str(), message));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToCreatePrincipal$str() {
      return "Failed to create principal: %s";
   }

   public final MalformedURLException failedToValidateURL(String urlString) {
      MalformedURLException result = new MalformedURLException(String.format("PBOX000114: " + this.failedToValidateURL$str(), urlString));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToValidateURL$str() {
      return "Failed to validate %s as a URL, file or classpath resource";
   }

   public final IllegalArgumentException invalidBase64String(String base64Str) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000112: " + this.invalidBase64String$str(), base64Str));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidBase64String$str() {
      return "Invalid Base64 string: %s";
   }

   public final RuntimeException failedToGetTransactionManager(Throwable throwable) {
      RuntimeException result = new RuntimeException(String.format("PBOX000060: " + this.failedToGetTransactionManager$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToGetTransactionManager$str() {
      return "Unable to get TransactionManager";
   }

   public final SecurityException failedToObtainPassword(Throwable throwable) {
      SecurityException result = new SecurityException(String.format("PBOX000032: " + this.failedToObtainPassword$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToObtainPassword$str() {
      return "Failed to obtain the password";
   }

   public final NamingException invalidNullBaseContextDN() {
      NamingException result = new NamingException(String.format("PBOX000036: " + this.invalidNullBaseContextDN$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidNullBaseContextDN$str() {
      return "Invalid configuration: baseCtxDN is null";
   }

   public final IllegalStateException failedToObtainAuthenticationInfo(String securityDomain) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000049: " + this.failedToObtainAuthenticationInfo$str(), securityDomain));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToObtainAuthenticationInfo$str() {
      return "AuthenticationInfo not set in security domain %s";
   }

   public final String missingCallerInfoMessage() {
      String result = String.format("PBOX000100: " + this.missingCallerInfoMessage$str());
      return result;
   }

   protected String missingCallerInfoMessage$str() {
      return "Either caller subject or caller run-as should be non-null";
   }

   public final IllegalStateException failedToObtainSecDomainFromContextOrConfig() {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000047: " + this.failedToObtainSecDomainFromContextOrConfig$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToObtainSecDomainFromContextOrConfig$str() {
      return "Failed to obtain security domain from security context or configuration";
   }

   public final RuntimeException invalidKeyFormat(String key) {
      RuntimeException result = new RuntimeException(String.format("PBOX000044: " + this.invalidKeyFormat$str(), key));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidKeyFormat$str() {
      return "Invalid key format: %s";
   }

   public final IllegalStateException invalidDelegateMapEntry(String entry) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000079: " + this.invalidDelegateMapEntry$str(), entry));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidDelegateMapEntry$str() {
      return "Invalid delegate map entry: %s";
   }

   public final IllegalStateException failedToParseJACCStatesConfigFile(Throwable throwable) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000094: " + this.failedToParseJACCStatesConfigFile$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToParseJACCStatesConfigFile$str() {
      return "Failed to parse jacc-policy-config-states.xml";
   }

   public final String unableToWriteVaultDataFileMessage(String fileName) {
      String result = String.format("PBOX000130: " + this.unableToWriteVaultDataFileMessage$str(), fileName);
      return result;
   }

   protected String unableToWriteVaultDataFileMessage$str() {
      return "Unable to write vault data file (%s)";
   }

   public final LoginException failedToInstantiateDelegateModule(String loginModuleName) {
      LoginException result = new LoginException(String.format("PBOX000068: " + this.failedToInstantiateDelegateModule$str(), loginModuleName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToInstantiateDelegateModule$str() {
      return "Failed to instantiate delegate module %s";
   }

   public final RuntimeException unableToParseReferralAbsoluteName(URISyntaxException cause, String absoluteName) {
      RuntimeException result = new RuntimeException(String.format("PBOX000141: " + this.unableToParseReferralAbsoluteName$str(), absoluteName), cause);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToParseReferralAbsoluteName$str() {
      return "Unable to parse referral absolute name: %s";
   }

   public final String vaultNotInitializedMessage() {
      String result = String.format("PBOX000008: " + this.vaultNotInitializedMessage$str());
      return result;
   }

   protected String vaultNotInitializedMessage$str() {
      return "Vault is not initialized";
   }

   public final IllegalStateException unexpectedSecurityDomainInInfo(String infoType, String securityDomain) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000098: " + this.unexpectedSecurityDomainInInfo$str(), infoType, securityDomain));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unexpectedSecurityDomainInInfo$str() {
      return "Application policy -> %s does not match expected security domain %s";
   }

   public final IllegalArgumentException invalidNullOrEmptyOptionMap(String mapName) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000120: " + this.invalidNullOrEmptyOptionMap$str(), mapName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidNullOrEmptyOptionMap$str() {
      return "Options map %s is null or empty";
   }

   public final XMLStreamException unexpectedNamespace(String namespaceURI, Location location) {
      XMLStreamException result = new XMLStreamException(String.format("PBOX000088: " + this.unexpectedNamespace$str(), namespaceURI), location);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unexpectedNamespace$str() {
      return "Unexpected namespace %s encountered";
   }

   public final String failedToInstantiateClassMessage(Class clazz) {
      String result = String.format("PBOX000071: " + this.failedToInstantiateClassMessage$str(), clazz);
      return result;
   }

   protected String failedToInstantiateClassMessage$str() {
      return "Failed to instantiate %s class";
   }

   public final RuntimeException unableToFindSchema(String schemaFile) {
      RuntimeException result = new RuntimeException(String.format("PBOX000084: " + this.unableToFindSchema$str(), schemaFile));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToFindSchema$str() {
      return "Unable to find schema file %s";
   }

   public final IllegalArgumentException identityTypeFactoryNotImplemented(String identityType) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000002: " + this.identityTypeFactoryNotImplemented$str(), identityType));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String identityTypeFactoryNotImplemented$str() {
      return "IdentityFactory for type %s not implemented";
   }

   public final String noPolicyContextForIdMessage(String contextID) {
      String result = String.format("PBOX000092: " + this.noPolicyContextForIdMessage$str(), contextID);
      return result;
   }

   protected String noPolicyContextForIdMessage$str() {
      return "No PolicyContext exists for contextID %s";
   }

   public final String pbeUtilsMessage() {
      String result = String.format("PBOX000105: " + this.pbeUtilsMessage$str());
      return result;
   }

   protected String pbeUtilsMessage$str() {
      return "Ecrypt a password using the JaasSecurityDomain password\n\nUsage: PBEUtils salt count domain-password password\n  salt : the Salt attribute from the JaasSecurityDomain\n  count : the IterationCount attribute from the JaasSecurityDomain\n  domain-password : the plaintext password that maps to the KeyStorePass attribute from the JaasSecurityDomain\n  password : the plaintext password that should be encrypted with the JaasSecurityDomain password\n";
   }

   public final IOException failedToFindResource(String resourceName) {
      IOException result = new IOException(String.format("PBOX000043: " + this.failedToFindResource$str(), resourceName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToFindResource$str() {
      return "Failed to find resource %s";
   }

   public final NamingException unableToFollowReferralForAuth(String name) {
      NamingException result = new NamingException(String.format("PBOX000038: " + this.unableToFollowReferralForAuth$str(), name));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToFollowReferralForAuth$str() {
      return "Unable to follow referral for authentication: %s";
   }

   public final String enterUsernameMessage() {
      String result = String.format("PBOX000029: " + this.enterUsernameMessage$str());
      return result;
   }

   protected String enterUsernameMessage$str() {
      return "Enter the username: ";
   }

   public final XMLStreamException missingRequiredAttributes(String attributes, Location location) {
      XMLStreamException result = new XMLStreamException(String.format("PBOX000085: " + this.missingRequiredAttributes$str(), attributes), location);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String missingRequiredAttributes$str() {
      return "Missing required attribute(s): %s";
   }

   public final IllegalArgumentException missingSystemProperty(String sysProperty) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000132: " + this.missingSystemProperty$str(), sysProperty));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String missingSystemProperty$str() {
      return "The specified system property %s is missing";
   }

   public final IllegalArgumentException failedToDecodeBindCredential(Throwable throwable) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000066: " + this.failedToDecodeBindCredential$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToDecodeBindCredential$str() {
      return "Failed to decode bindCredential";
   }

   public final IOException unableToLoadPropertiesFile(String fileName) {
      IOException result = new IOException(String.format("PBOX000073: " + this.unableToLoadPropertiesFile$str(), fileName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToLoadPropertiesFile$str() {
      return "Properties file %s not available for loading";
   }

   public final ProviderException failedToObtainSHAMessageDigest(Throwable throwable) {
      ProviderException result = new ProviderException(String.format("PBOX000025: " + this.failedToObtainSHAMessageDigest$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToObtainSHAMessageDigest$str() {
      return "Failed to obtain SHA MessageDigest";
   }

   public final String failedToResolveTargetStateMessage(String targetName, String transitionName) {
      String result = String.format("PBOX000106: " + this.failedToResolveTargetStateMessage$str(), targetName, transitionName);
      return result;
   }

   protected String failedToResolveTargetStateMessage$str() {
      return "Failed to resolve target state %s for transition %s";
   }

   public final String unableToCreateVaultMessage() {
      String result = String.format("PBOX000007: " + this.unableToCreateVaultMessage$str());
      return result;
   }

   protected String unableToCreateVaultMessage$str() {
      return "Unable to instantiate vault class";
   }

   public final IllegalStateException missingDelegateForLayer(String layer) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000078: " + this.missingDelegateForLayer$str(), layer));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String missingDelegateForLayer$str() {
      return "Delegate is missing for layer %s";
   }

   public final String invalidKeystorePasswordFormatMessage() {
      String result = String.format("PBOX000142: " + this.invalidKeystorePasswordFormatMessage$str());
      return result;
   }

   protected String invalidKeystorePasswordFormatMessage$str() {
      return "Keystore password should be either masked or prefixed with one of {EXT}, {EXTC}, {CMD}, {CMDC}, {CLASS}";
   }

   public final IllegalStateException invalidThreadContextClassLoader() {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000011: " + this.invalidThreadContextClassLoader$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidThreadContextClassLoader$str() {
      return "Thread context classloader has not been set";
   }

   public final GeneralSecurityException failedToCreateSealedObject(Throwable throwable) {
      GeneralSecurityException result = new GeneralSecurityException(String.format("PBOX000028: " + this.failedToCreateSealedObject$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToCreateSealedObject$str() {
      return "Failed to create SealedObject";
   }

   public final String noMatchForAliasMessage(String alias, List existingAliases) {
      String result = String.format("PBOX000058: " + this.noMatchForAliasMessage$str(), alias, existingAliases);
      return result;
   }

   protected String noMatchForAliasMessage$str() {
      return "No match for alias %s, existing aliases: %s";
   }

   public final IllegalArgumentException unsupportedQOP(String qop) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000041: " + this.unsupportedQOP$str(), qop));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unsupportedQOP$str() {
      return "Unsupported quality of protection: %s";
   }

   public final RuntimeException invalidSecurityAnnotationConfig() {
      RuntimeException result = new RuntimeException(String.format("PBOX000015: " + this.invalidSecurityAnnotationConfig$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidSecurityAnnotationConfig$str() {
      return "Invalid annotation configuration: either @SecurityConfig or @Authentication is needed";
   }

   public final String failedToLookupDataSourceMessage(String jndiName) {
      String result = String.format("PBOX000064: " + this.failedToLookupDataSourceMessage$str(), jndiName);
      return result;
   }

   protected String failedToLookupDataSourceMessage$str() {
      return "Error looking up DataSource from %s";
   }

   public final IllegalStateException missingXACMLPolicyForContextId(String contextID) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000080: " + this.missingXACMLPolicyForContextId$str(), contextID));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String missingXACMLPolicyForContextId$str() {
      return "Missing XACML policy for contextID %s";
   }

   public final String sharedKeyMismatchMessage(String vaultBlock, String attributeName) {
      String result = String.format("PBOX000131: " + this.sharedKeyMismatchMessage$str(), vaultBlock, attributeName);
      return result;
   }

   protected String sharedKeyMismatchMessage$str() {
      return "Vault mismatch: shared key does not match for vault block %s and attribute name %s";
   }

   public final String deniedByIdentityTrustMessage() {
      String result = String.format("PBOX000005: " + this.deniedByIdentityTrustMessage$str());
      return result;
   }

   protected String deniedByIdentityTrustMessage$str() {
      return "Caller denied by identity trust framework";
   }

   public final RuntimeException invalidLoginModuleStackRef(String stackRef) {
      RuntimeException result = new RuntimeException(String.format("PBOX000083: " + this.invalidLoginModuleStackRef$str(), stackRef));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidLoginModuleStackRef$str() {
      return "auth-module references a login module stack that doesn't exist: %s";
   }

   public final LoginException unableToGetPrincipalOrCredsForAssociation() {
      LoginException result = new LoginException(String.format("PBOX000119: " + this.unableToGetPrincipalOrCredsForAssociation$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToGetPrincipalOrCredsForAssociation$str() {
      return "Unable to get the calling principal or its credentials for resource association";
   }

   public final IllegalArgumentException invalidType(String type) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000003: " + this.invalidType$str(), type));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidType$str() {
      return "Class is not an instance of %s";
   }

   public final IllegalStateException missingServiceAuthToken(String securityDomain) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000115: " + this.missingServiceAuthToken$str(), securityDomain));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String missingServiceAuthToken$str() {
      return "JSSE domain %s has been requested to provide sensitive security information, but no service authentication token has been configured on it. Use setServiceAuthToken()";
   }

   public final String filePasswordUsageMessage() {
      String result = String.format("PBOX000102: " + this.filePasswordUsageMessage$str());
      return result;
   }

   protected String filePasswordUsageMessage$str() {
      return "Write a password in opaque form to a file for use with the FilePassword accessor\n\nUsage: FilePassword salt count password password-file\n  salt  : an 8 char sequence for PBEKeySpec\n  count : iteration count for PBEKeySpec\n  password : the clear text password to write\n  password-file : the path to the file to write the password to\n";
   }

   public final String unableToEncryptDataMessage() {
      String result = String.format("PBOX000128: " + this.unableToEncryptDataMessage$str());
      return result;
   }

   protected String unableToEncryptDataMessage$str() {
      return "Unable to encrypt data";
   }

   public final String unableToInitializeLoginContext(Throwable cause) {
      String result = String.format("PBOX000143: " + this.unableToInitializeLoginContext$str());
      return result;
   }

   protected String unableToInitializeLoginContext$str() {
      return "Unable to initialize login context";
   }

   public final RuntimeException invalidNullSecurityContext() {
      RuntimeException result = new RuntimeException(String.format("PBOX000035: " + this.invalidNullSecurityContext$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidNullSecurityContext$str() {
      return "Unable to proceed: security context is null";
   }

   public final RuntimeException invalidNullKeyStoreURL(String keystoreType) {
      RuntimeException result = new RuntimeException(String.format("PBOX000117: " + this.invalidNullKeyStoreURL$str(), keystoreType));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidNullKeyStoreURL$str() {
      return "Cannot load KeyStore of type %s: required keyStoreURL is null";
   }

   public final RuntimeException noSecretKeyandAliasAlreadyUsed(String alias) {
      RuntimeException result = new RuntimeException(String.format("PBOX000138: " + this.noSecretKeyandAliasAlreadyUsed$str(), alias));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String noSecretKeyandAliasAlreadyUsed$str() {
      return "There is no SecretKey under the alias (%s) and the alias is already used to denote diffrent crypto object in the keystore.";
   }

   public final XMLStreamException unexpectedElement(String elementName, Location location) {
      XMLStreamException result = new XMLStreamException(String.format("PBOX000086: " + this.unexpectedElement$str(), elementName), location);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unexpectedElement$str() {
      return "Unexpected element %s encountered";
   }

   public final RuntimeException invalidPasswordType(Class type) {
      RuntimeException result = new RuntimeException(String.format("PBOX000039: " + this.invalidPasswordType$str(), type));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidPasswordType$str() {
      return "Invalid password type: %s";
   }

   public final String cacheValidationFailedMessage() {
      String result = String.format("PBOX000082: " + this.cacheValidationFailedMessage$str());
      return result;
   }

   protected String cacheValidationFailedMessage$str() {
      return "Cache validation failed";
   }

   public final LoginException missingPropertiesFile(String fileName) {
      LoginException result = new LoginException(String.format("PBOX000059: " + this.missingPropertiesFile$str(), fileName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String missingPropertiesFile$str() {
      return "Missing properties file: %s";
   }

   public final String base64EncoderMessage() {
      String result = String.format("PBOX000111: " + this.base64EncoderMessage$str());
      return result;
   }

   protected String base64EncoderMessage$str() {
      return "Usage: Base64Encoder <string> <optional hash algorithm>";
   }

   public final IllegalStateException invalidPermissionChecks() {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000077: " + this.invalidPermissionChecks$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidPermissionChecks$str() {
      return "Permission checks must be different";
   }

   public final String unableToFindSetSecurityInfoMessage() {
      String result = String.format("PBOX000010: " + this.unableToFindSetSecurityInfoMessage$str());
      return result;
   }

   protected String unableToFindSetSecurityInfoMessage$str() {
      return "Unable to find setSecurityInfo(Principal, Object) in CallbackHandler";
   }

   public final IllegalArgumentException invalidEJBVersion(String version) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000099: " + this.invalidEJBVersion$str(), version));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidEJBVersion$str() {
      return "Invalid EJB version: %s";
   }

   public final String sizeMismatchMessage(String param1, String param2) {
      String result = String.format("PBOX000042: " + this.sizeMismatchMessage$str(), param1, param2);
      return result;
   }

   protected String sizeMismatchMessage$str() {
      return "Size mismatch between %s and %s";
   }

   public final LoginException missingXMLUserRolesMapping() {
      LoginException result = new LoginException(String.format("PBOX000074: " + this.missingXMLUserRolesMapping$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String missingXMLUserRolesMapping$str() {
      return "Missing XML configuration for user/roles mapping";
   }

   public final IllegalStateException failedToObtainAuthorizationInfo(String securityDomain) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000096: " + this.failedToObtainAuthorizationInfo$str(), securityDomain));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToObtainAuthorizationInfo$str() {
      return "AuthorizationInfo not set in security domain %s";
   }

   public final SecurityException failedToObtainUsername(Throwable throwable) {
      SecurityException result = new SecurityException(String.format("PBOX000031: " + this.failedToObtainUsername$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToObtainUsername$str() {
      return "Failed to obtain the username";
   }

   public final FailedLoginException failedToMatchCredential(String alias) {
      FailedLoginException result = new FailedLoginException(String.format("PBOX000052: " + this.failedToMatchCredential$str(), alias));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToMatchCredential$str() {
      return "Supplied credential did not match existing credential for alias %s";
   }

   public final IllegalArgumentException invalidVaultStringFormat(String vaultString) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000009: " + this.invalidVaultStringFormat$str(), vaultString));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidVaultStringFormat$str() {
      return "Invalid vaultString format: %s";
   }

   public final IllegalStateException invalidNullAuthConfigProviderForLayer(String layer, String contextID) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000095: " + this.invalidNullAuthConfigProviderForLayer$str(), layer, contextID));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidNullAuthConfigProviderForLayer$str() {
      return "AuthConfigProvider is null for layer %s, contextID: %s";
   }

   public final RuntimeException noMatchingRoleFoundInDescriptor(String roleName) {
      RuntimeException result = new RuntimeException(String.format("PBOX000076: " + this.noMatchingRoleFoundInDescriptor$str(), roleName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String noMatchingRoleFoundInDescriptor$str() {
      return "No matching role found in deployment descriptor for role %s";
   }

   public final String cacheMissMessage() {
      String result = String.format("PBOX000081: " + this.cacheMissMessage$str());
      return result;
   }

   protected String cacheMissMessage$str() {
      return "Cache miss";
   }

   public final RuntimeException unableToCreateACLPersistenceStrategy(Throwable throwable) {
      RuntimeException result = new RuntimeException(String.format("PBOX000021: " + this.unableToCreateACLPersistenceStrategy$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToCreateACLPersistenceStrategy$str() {
      return "Failed to instantiate persistence strategy class";
   }

   public final RuntimeException unrecognizedVaultContentVersion(String readVersion, String fromVersion, String toVersion) {
      RuntimeException result = new RuntimeException(String.format("PBOX000134: " + this.unrecognizedVaultContentVersion$str(), readVersion, fromVersion, toVersion));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unrecognizedVaultContentVersion$str() {
      return "Unrecognized security vault content version (%s), expecting (from %s to %s)";
   }

   public final String failedToRetrieveCertificateMessage(String publicKeyAlias) {
      String result = String.format("PBOX000126: " + this.failedToRetrieveCertificateMessage$str(), publicKeyAlias);
      return result;
   }

   protected String failedToRetrieveCertificateMessage$str() {
      return "Failed to retrieve certificate from keystore using alias %s";
   }

   public final RuntimeException unableToInitSecurityFactory(Throwable throwable) {
      RuntimeException result = new RuntimeException(String.format("PBOX000013: " + this.unableToInitSecurityFactory$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToInitSecurityFactory$str() {
      return "Unable to initialize security factory";
   }

   public final FailedLoginException invalidPassword() {
      FailedLoginException result = new FailedLoginException(String.format("PBOX000070: " + this.invalidPassword$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidPassword$str() {
      return "Password invalid/Password required";
   }

   public final String unableToWriteShareKeyFileMessage() {
      String result = String.format("PBOX000129: " + this.unableToWriteShareKeyFileMessage$str());
      return result;
   }

   protected String unableToWriteShareKeyFileMessage$str() {
      return "Unable to write shared key file";
   }

   public final LoginException unableToGetCertificateFromClass(Class certClass) {
      LoginException result = new LoginException(String.format("PBOX000054: " + this.unableToGetCertificateFromClass$str(), certClass));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToGetCertificateFromClass$str() {
      return "Unable to obtain a X509Certificate from %s";
   }

   public final String existingCredentialMessage() {
      String result = String.format("PBOX000057: " + this.existingCredentialMessage$str());
      return result;
   }

   protected String existingCredentialMessage$str() {
      return "Existing credential: ";
   }

   public final String unableToLoadVaultMessage() {
      String result = String.format("PBOX000006: " + this.unableToLoadVaultMessage$str());
      return result;
   }

   protected String unableToLoadVaultMessage$str() {
      return "Unable to load vault class";
   }

   public final String failedToProcessQueryMessage() {
      String result = String.format("PBOX000065: " + this.failedToProcessQueryMessage$str());
      return result;
   }

   protected String failedToProcessQueryMessage$str() {
      return "Error processing query";
   }

   public final NumberFormatException illegalBase64Character() {
      NumberFormatException result = new NumberFormatException(String.format("PBOX000113: " + this.illegalBase64Character$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String illegalBase64Character$str() {
      return "Illegal Base64 character";
   }

   public final IOException unableToFindPropertiesFile(String fileName) {
      IOException result = new IOException(String.format("PBOX000072: " + this.unableToFindPropertiesFile$str(), fileName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToFindPropertiesFile$str() {
      return "Properties file %s not found";
   }

   public final String identityTrustValidationFailedMessage() {
      String result = String.format("PBOX000089: " + this.identityTrustValidationFailedMessage$str());
      return result;
   }

   protected String identityTrustValidationFailedMessage$str() {
      return "Identity trust validation failed";
   }

   public final RuntimeException vaultDoesnotContainSecretKey(String alias) {
      RuntimeException result = new RuntimeException(String.format("PBOX000137: " + this.vaultDoesnotContainSecretKey$str(), alias));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String vaultDoesnotContainSecretKey$str() {
      return "Security Vault does not contain SecretKey entry under alias (%s)";
   }

   public final FailedLoginException noMatchingUsernameFoundInPrincipals() {
      FailedLoginException result = new FailedLoginException(String.format("PBOX000062: " + this.noMatchingUsernameFoundInPrincipals$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String noMatchingUsernameFoundInPrincipals$str() {
      return "No matching username found in principals";
   }

   public final LoginException noCallbackHandlerAvailable() {
      LoginException result = new LoginException(String.format("PBOX000053: " + this.noCallbackHandlerAvailable$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String noCallbackHandlerAvailable$str() {
      return "No CallbackHandler available to collect authentication information";
   }

   public final RuntimeException missingAdminKeyInOriginalVaultData() {
      RuntimeException result = new RuntimeException(String.format("PBOX000136: " + this.missingAdminKeyInOriginalVaultData$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String missingAdminKeyInOriginalVaultData$str() {
      return "Security Vault conversion unsuccessful missing admin key in original vault data";
   }

   public final String missingRequiredModuleOptionMessage(String moduleOption) {
      String result = String.format("PBOX000067: " + this.missingRequiredModuleOptionMessage$str(), moduleOption);
      return result;
   }

   protected String missingRequiredModuleOptionMessage$str() {
      return "Missing required module option: %s";
   }

   public final IllegalStateException noServerAuthModuleForRequestType(Class requestType) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000050: " + this.noServerAuthModuleForRequestType$str(), requestType));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String noServerAuthModuleForRequestType$str() {
      return "No ServerAuthModule configured to support type %s";
   }

   public final IllegalStateException failedToObtainApplicationPolicy(String securityDomain) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000048: " + this.failedToObtainApplicationPolicy$str(), securityDomain));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToObtainApplicationPolicy$str() {
      return "Failed to obtain ApplicationPolicy for domain %s";
   }

   public final String invalidUnmaskedKeystorePasswordMessage() {
      String result = String.format("PBOX000122: " + this.invalidUnmaskedKeystorePasswordMessage$str());
      return result;
   }

   protected String invalidUnmaskedKeystorePasswordMessage$str() {
      return "Keystore password is not masked";
   }

   public final IllegalStateException unableToLocateMBeanServer() {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000108: " + this.unableToLocateMBeanServer$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToLocateMBeanServer$str() {
      return "Unable to locate MBean server";
   }

   public final String fileOrDirectoryDoesNotExistMessage(String fileName) {
      String result = String.format("PBOX000123: " + this.fileOrDirectoryDoesNotExistMessage$str(), fileName);
      return result;
   }

   protected String fileOrDirectoryDoesNotExistMessage$str() {
      return "File or directory %s does not exist";
   }

   public final IllegalArgumentException invalidControlFlag(String flag) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000001: " + this.invalidControlFlag$str(), flag));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidControlFlag$str() {
      return "Invalid control flag: %s";
   }

   public final RuntimeException invalidPolicyRegistrationType(String type) {
      RuntimeException result = new RuntimeException(String.format("PBOX000104: " + this.invalidPolicyRegistrationType$str(), type));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidPolicyRegistrationType$str() {
      return "Unsupported policy registration type: %s";
   }

   public final String nullRolesInSubjectMessage() {
      String result = String.format("PBOX000018: " + this.nullRolesInSubjectMessage$str());
      return result;
   }

   protected String nullRolesInSubjectMessage$str() {
      return "Subject contains a null set of roles";
   }

   public final LoginException unableToGetPasswordFromVault() {
      LoginException result = new LoginException(String.format("PBOX000069: " + this.unableToGetPasswordFromVault$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToGetPasswordFromVault$str() {
      return "Unable to get password value from vault";
   }

   public final RuntimeException unableToGetKeyStore(Throwable throwable, String file) {
      RuntimeException result = new RuntimeException(String.format("PBOX000140: " + this.unableToGetKeyStore$str(), file), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToGetKeyStore$str() {
      return "Unable to get keystore (%s)";
   }

   public final SecurityException failedToRegisterAuthConfigProvider(String providerClass, Throwable throwable) {
      SecurityException result = new SecurityException(String.format("PBOX000045: " + this.failedToRegisterAuthConfigProvider$str(), providerClass), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToRegisterAuthConfigProvider$str() {
      return "Failed to register AuthConfigProvider %s";
   }

   public final IllegalStateException invalidNullTransactionManager() {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000061: " + this.invalidNullTransactionManager$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidNullTransactionManager$str() {
      return "Invalid null TransactionManager";
   }

   public final IllegalStateException callbackHandlerSysPropertyNotSet(String systemPropertyName) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000046: " + this.callbackHandlerSysPropertyNotSet$str(), systemPropertyName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String callbackHandlerSysPropertyNotSet$str() {
      return "CallbackHandler not specified by system property %s";
   }

   public final RuntimeException unableToLookupDataSource() {
      RuntimeException result = new RuntimeException(String.format("PBOX000034: " + this.unableToLookupDataSource$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToLookupDataSource$str() {
      return "Unable to lookup DataSource - the DS JNDI name is null";
   }

   public final String authenticationFailedMessage() {
      String result = String.format("PBOX000016: " + this.authenticationFailedMessage$str());
      return result;
   }

   protected String authenticationFailedMessage$str() {
      return "Access denied: authentication failed";
   }

   public final RuntimeException failedToCreateDocumentBuilder(Throwable throwable) {
      RuntimeException result = new RuntimeException(String.format("PBOX000109: " + this.failedToCreateDocumentBuilder$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToCreateDocumentBuilder$str() {
      return "Failed to create DocumentBuilder";
   }

   public final LoginException failedToInvokeCallbackHandler() {
      LoginException result = new LoginException(String.format("PBOX000055: " + this.failedToInvokeCallbackHandler$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToInvokeCallbackHandler$str() {
      return "Failed to invoke CallbackHandler";
   }

   public final IllegalArgumentException unsupportedAlgorithm(String algorithm) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000040: " + this.unsupportedAlgorithm$str(), algorithm));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unsupportedAlgorithm$str() {
      return "Unsupported algorithm: %s";
   }

   public final IllegalStateException aclEntryPermissionAlreadySet() {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000019: " + this.aclEntryPermissionAlreadySet$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String aclEntryPermissionAlreadySet$str() {
      return "ACLEntry permission has already been set";
   }

   public final FailedLoginException noMatchingUsernameFoundInRoles() {
      FailedLoginException result = new FailedLoginException(String.format("PBOX000063: " + this.noMatchingUsernameFoundInRoles$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String noMatchingUsernameFoundInRoles$str() {
      return "No matching username found in roles";
   }

   public final String failedToRetrievePublicKeyMessage(String publicKeyAlias) {
      String result = String.format("PBOX000125: " + this.failedToRetrievePublicKeyMessage$str(), publicKeyAlias);
      return result;
   }

   protected String failedToRetrievePublicKeyMessage$str() {
      return "Failed to retrieve public key from keystore using alias %s";
   }

   public final IllegalStateException invalidNullProperty(String property) {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000075: " + this.invalidNullProperty$str(), property));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidNullProperty$str() {
      return "The property %s is null";
   }

   public final String unableToLocateACLForResourceMessage(String resource) {
      String result = String.format("PBOX000022: " + this.unableToLocateACLForResourceMessage$str(), resource);
      return result;
   }

   protected String unableToLocateACLForResourceMessage$str() {
      return "Unable to locate ACL for resource %s";
   }

   public final String invalidNullOrEmptyOptionMessage(String optionName) {
      String result = String.format("PBOX000121: " + this.invalidNullOrEmptyOptionMessage$str(), optionName);
      return result;
   }

   protected String invalidNullOrEmptyOptionMessage$str() {
      return "Option %s is null or empty";
   }

   public final String invalidTransitionForActionMessage(String actionName, String stateName) {
      String result = String.format("PBOX000107: " + this.invalidTransitionForActionMessage$str(), actionName, stateName);
      return result;
   }

   protected String invalidTransitionForActionMessage$str() {
      return "No transition for action %s from state %s ";
   }

   public final IllegalArgumentException malformedIdentityString(String identityString) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000024: " + this.malformedIdentityString$str(), identityString));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String malformedIdentityString$str() {
      return "Malformed identity string: %s. Expected Identity_Class:Identity_Name";
   }

   public final SecurityException failedToVerifyServiceAuthToken() {
      SecurityException result = new SecurityException(String.format("PBOX000116: " + this.failedToVerifyServiceAuthToken$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToVerifyServiceAuthToken$str() {
      return "Service authentication token verification failed";
   }

   public final IllegalStateException invalidNullLoginConfig() {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000012: " + this.invalidNullLoginConfig$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidNullLoginConfig$str() {
      return "'java.security.auth.login.config' system property not set and auth.conf file not present";
   }

   public final String authorizationFailedMessage() {
      String result = String.format("PBOX000017: " + this.authorizationFailedMessage$str());
      return result;
   }

   protected String authorizationFailedMessage$str() {
      return "Acces denied: authorization failed";
   }

   public final String moduleAbortFailedMessage() {
      String result = String.format("PBOX000091: " + this.moduleAbortFailedMessage$str());
      return result;
   }

   protected String moduleAbortFailedMessage$str() {
      return "Invocation of abort on module failed";
   }

   public final String invalidSharedKeyMessage() {
      String result = String.format("PBOX000127: " + this.invalidSharedKeyMessage$str());
      return result;
   }

   protected String invalidSharedKeyMessage$str() {
      return "The shared key is invalid or has been incorrectly encoded";
   }

   public final String invalidDirectoryFormatMessage(String directory) {
      String result = String.format("PBOX000124: " + this.invalidDirectoryFormatMessage$str(), directory);
      return result;
   }

   protected String invalidDirectoryFormatMessage$str() {
      return "Directory %s does not end with / or \\";
   }

   public final IllegalArgumentException invalidPasswordCommandType(String type) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000118: " + this.invalidPasswordCommandType$str(), type));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidPasswordCommandType$str() {
      return "Invalid password command type: %s";
   }

   public final RuntimeException failedToMatchStrings(String one, String two) {
      RuntimeException result = new RuntimeException(String.format("PBOX000133: " + this.failedToMatchStrings$str(), one, two));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToMatchStrings$str() {
      return "Failed to match %s and %s";
   }

   public final IllegalStateException aclResourceAlreadySet() {
      IllegalStateException result = new IllegalStateException(String.format("PBOX000020: " + this.aclResourceAlreadySet$str()));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String aclResourceAlreadySet$str() {
      return "ACL resource has already been set";
   }

   public final RuntimeException failedToFindNamespaceURI(String elementName) {
      RuntimeException result = new RuntimeException(String.format("PBOX000110: " + this.failedToFindNamespaceURI$str(), elementName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToFindNamespaceURI$str() {
      return "Failed to find namespace URI for %s";
   }

   public final IllegalArgumentException invalidNullArgument(String argumentName) {
      IllegalArgumentException result = new IllegalArgumentException(String.format("PBOX000004: " + this.invalidNullArgument$str(), argumentName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidNullArgument$str() {
      return "Argument %s cannot be null";
   }

   public final String moduleCommitFailedMessage() {
      String result = String.format("PBOX000090: " + this.moduleCommitFailedMessage$str());
      return result;
   }

   protected String moduleCommitFailedMessage$str() {
      return "Invocation of commit on module failed";
   }

   public final RuntimeException mixedVaultDataFound(String vaultDatFile, String encDatFile) {
      RuntimeException result = new RuntimeException(String.format("PBOX000135: " + this.mixedVaultDataFound$str(), vaultDatFile, encDatFile));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String mixedVaultDataFound$str() {
      return "Security Vault contains both covnerted (%s) and pre-conversion data (%s), failed to load vault";
   }

   public final String enterPasswordMessage() {
      String result = String.format("PBOX000030: " + this.enterPasswordMessage$str());
      return result;
   }

   protected String enterPasswordMessage$str() {
      return "Enter the password: ";
   }

   public final String suppliedCredentialMessage() {
      String result = String.format("PBOX000056: " + this.suppliedCredentialMessage$str());
      return result;
   }

   protected String suppliedCredentialMessage$str() {
      return "Supplied credential: ";
   }

   public final KeyException failedToCreateSecretKeySpec(Throwable throwable) {
      KeyException result = new KeyException(String.format("PBOX000026: " + this.failedToCreateSecretKeySpec$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToCreateSecretKeySpec$str() {
      return "Failed to create SecretKeySpec from session key";
   }

   public final String unableToLocateACLWithNoStrategyMessage() {
      String result = String.format("PBOX000023: " + this.unableToLocateACLWithNoStrategyMessage$str());
      return result;
   }

   protected String unableToLocateACLWithNoStrategyMessage$str() {
      return "Unable to locate ACL: persistence strategy has not been set";
   }

   public final NamingException failedToFindBaseContextDN(String baseDN) {
      NamingException result = new NamingException(String.format("PBOX000037: " + this.failedToFindBaseContextDN$str(), baseDN));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String failedToFindBaseContextDN$str() {
      return "Search for context %s found no results";
   }

   public final UnsupportedCallbackException unableToHandleCallback(Callback callback, String callbackHandler, String callbackType) {
      UnsupportedCallbackException result = new UnsupportedCallbackException(callback);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToHandleCallback$str() {
      return "%s does not handle a callback of type %s";
   }

   public final AttributeNotFoundException invalidMBeanAttribute(String attrName) {
      AttributeNotFoundException result = new AttributeNotFoundException(String.format("PBOX000101: " + this.invalidMBeanAttribute$str(), attrName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String invalidMBeanAttribute$str() {
      return "%s is not an MBean attribute";
   }

   public final XMLStreamException unexpectedAttribute(String attributeName, Location location) {
      XMLStreamException result = new XMLStreamException(String.format("PBOX000087: " + this.unexpectedAttribute$str(), attributeName), location);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unexpectedAttribute$str() {
      return "Unexpected attribute %s encountered";
   }

   public final RuntimeException unableToFindPrincipalInDB(String principalName) {
      RuntimeException result = new RuntimeException(String.format("PBOX000033: " + this.unableToFindPrincipalInDB$str(), principalName));
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unableToFindPrincipalInDB$str() {
      return "No matching principal found in DB: %s";
   }

   public final String operationNotAllowedMessage() {
      String result = String.format("PBOX000093: " + this.operationNotAllowedMessage$str());
      return result;
   }

   protected String operationNotAllowedMessage$str() {
      return "Operation not allowed";
   }

   public final KeyException unexpectedExceptionDuringSecretKeyCreation(Throwable throwable) {
      KeyException result = new KeyException(String.format("PBOX000027: " + this.unexpectedExceptionDuringSecretKeyCreation$str()), throwable);
      StackTraceElement[] st = result.getStackTrace();
      result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
      return result;
   }

   protected String unexpectedExceptionDuringSecretKeyCreation$str() {
      return "Unexpected exception during SecretKeySpec creation";
   }
}
