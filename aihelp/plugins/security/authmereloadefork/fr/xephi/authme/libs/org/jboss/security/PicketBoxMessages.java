package fr.xephi.authme.libs.org.jboss.security;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyException;
import java.security.ProviderException;
import java.util.List;
import javax.management.AttributeNotFoundException;
import javax.naming.NamingException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import org.jboss.logging.Cause;
import org.jboss.logging.Message;
import org.jboss.logging.MessageBundle;
import org.jboss.logging.Messages;
import org.jboss.logging.Param;

@MessageBundle(
   projectCode = "PBOX"
)
public interface PicketBoxMessages {
   PicketBoxMessages MESSAGES = (PicketBoxMessages)Messages.getBundle(PicketBoxMessages.class);

   @Message(
      id = 1,
      value = "Invalid control flag: %s"
   )
   IllegalArgumentException invalidControlFlag(String var1);

   @Message(
      id = 2,
      value = "IdentityFactory for type %s not implemented"
   )
   IllegalArgumentException identityTypeFactoryNotImplemented(String var1);

   @Message(
      id = 3,
      value = "Class is not an instance of %s"
   )
   IllegalArgumentException invalidType(String var1);

   @Message(
      id = 4,
      value = "Argument %s cannot be null"
   )
   IllegalArgumentException invalidNullArgument(String var1);

   @Message(
      id = 5,
      value = "Caller denied by identity trust framework"
   )
   String deniedByIdentityTrustMessage();

   @Message(
      id = 6,
      value = "Unable to load vault class"
   )
   String unableToLoadVaultMessage();

   @Message(
      id = 7,
      value = "Unable to instantiate vault class"
   )
   String unableToCreateVaultMessage();

   @Message(
      id = 8,
      value = "Vault is not initialized"
   )
   String vaultNotInitializedMessage();

   @Message(
      id = 9,
      value = "Invalid vaultString format: %s"
   )
   IllegalArgumentException invalidVaultStringFormat(String var1);

   @Message(
      id = 10,
      value = "Unable to find setSecurityInfo(Principal, Object) in CallbackHandler"
   )
   String unableToFindSetSecurityInfoMessage();

   @Message(
      id = 11,
      value = "Thread context classloader has not been set"
   )
   IllegalStateException invalidThreadContextClassLoader();

   @Message(
      id = 12,
      value = "'java.security.auth.login.config' system property not set and auth.conf file not present"
   )
   IllegalStateException invalidNullLoginConfig();

   @Message(
      id = 13,
      value = "Unable to initialize security factory"
   )
   RuntimeException unableToInitSecurityFactory(@Cause Throwable var1);

   @Message(
      id = 14,
      value = "%s does not handle a callback of type %s"
   )
   UnsupportedCallbackException unableToHandleCallback(@Param Callback var1, String var2, String var3);

   @Message(
      id = 15,
      value = "Invalid annotation configuration: either @SecurityConfig or @Authentication is needed"
   )
   RuntimeException invalidSecurityAnnotationConfig();

   @Message(
      id = 16,
      value = "Access denied: authentication failed"
   )
   String authenticationFailedMessage();

   @Message(
      id = 17,
      value = "Acces denied: authorization failed"
   )
   String authorizationFailedMessage();

   @Message(
      id = 18,
      value = "Subject contains a null set of roles"
   )
   String nullRolesInSubjectMessage();

   @Message(
      id = 19,
      value = "ACLEntry permission has already been set"
   )
   IllegalStateException aclEntryPermissionAlreadySet();

   @Message(
      id = 20,
      value = "ACL resource has already been set"
   )
   IllegalStateException aclResourceAlreadySet();

   @Message(
      id = 21,
      value = "Failed to instantiate persistence strategy class"
   )
   RuntimeException unableToCreateACLPersistenceStrategy(@Cause Throwable var1);

   @Message(
      id = 22,
      value = "Unable to locate ACL for resource %s"
   )
   String unableToLocateACLForResourceMessage(String var1);

   @Message(
      id = 23,
      value = "Unable to locate ACL: persistence strategy has not been set"
   )
   String unableToLocateACLWithNoStrategyMessage();

   @Message(
      id = 24,
      value = "Malformed identity string: %s. Expected Identity_Class:Identity_Name"
   )
   IllegalArgumentException malformedIdentityString(String var1);

   @Message(
      id = 25,
      value = "Failed to obtain SHA MessageDigest"
   )
   ProviderException failedToObtainSHAMessageDigest(@Cause Throwable var1);

   @Message(
      id = 26,
      value = "Failed to create SecretKeySpec from session key"
   )
   KeyException failedToCreateSecretKeySpec(@Cause Throwable var1);

   @Message(
      id = 27,
      value = "Unexpected exception during SecretKeySpec creation"
   )
   KeyException unexpectedExceptionDuringSecretKeyCreation(@Cause Throwable var1);

   @Message(
      id = 28,
      value = "Failed to create SealedObject"
   )
   GeneralSecurityException failedToCreateSealedObject(@Cause Throwable var1);

   @Message(
      id = 29,
      value = "Enter the username: "
   )
   String enterUsernameMessage();

   @Message(
      id = 30,
      value = "Enter the password: "
   )
   String enterPasswordMessage();

   @Message(
      id = 31,
      value = "Failed to obtain the username"
   )
   SecurityException failedToObtainUsername(@Cause Throwable var1);

   @Message(
      id = 32,
      value = "Failed to obtain the password"
   )
   SecurityException failedToObtainPassword(@Cause Throwable var1);

   @Message(
      id = 33,
      value = "No matching principal found in DB: %s"
   )
   RuntimeException unableToFindPrincipalInDB(String var1);

   @Message(
      id = 34,
      value = "Unable to lookup DataSource - the DS JNDI name is null"
   )
   RuntimeException unableToLookupDataSource();

   @Message(
      id = 35,
      value = "Unable to proceed: security context is null"
   )
   RuntimeException invalidNullSecurityContext();

   @Message(
      id = 36,
      value = "Invalid configuration: baseCtxDN is null"
   )
   NamingException invalidNullBaseContextDN();

   @Message(
      id = 37,
      value = "Search for context %s found no results"
   )
   NamingException failedToFindBaseContextDN(String var1);

   @Message(
      id = 38,
      value = "Unable to follow referral for authentication: %s"
   )
   NamingException unableToFollowReferralForAuth(String var1);

   @Message(
      id = 39,
      value = "Invalid password type: %s"
   )
   RuntimeException invalidPasswordType(Class<?> var1);

   @Message(
      id = 40,
      value = "Unsupported algorithm: %s"
   )
   IllegalArgumentException unsupportedAlgorithm(String var1);

   @Message(
      id = 41,
      value = "Unsupported quality of protection: %s"
   )
   IllegalArgumentException unsupportedQOP(String var1);

   @Message(
      id = 42,
      value = "Size mismatch between %s and %s"
   )
   String sizeMismatchMessage(String var1, String var2);

   @Message(
      id = 43,
      value = "Failed to find resource %s"
   )
   IOException failedToFindResource(String var1);

   @Message(
      id = 44,
      value = "Invalid key format: %s"
   )
   RuntimeException invalidKeyFormat(String var1);

   @Message(
      id = 45,
      value = "Failed to register AuthConfigProvider %s"
   )
   SecurityException failedToRegisterAuthConfigProvider(String var1, @Cause Throwable var2);

   @Message(
      id = 46,
      value = "CallbackHandler not specified by system property %s"
   )
   IllegalStateException callbackHandlerSysPropertyNotSet(String var1);

   @Message(
      id = 47,
      value = "Failed to obtain security domain from security context or configuration"
   )
   IllegalStateException failedToObtainSecDomainFromContextOrConfig();

   @Message(
      id = 48,
      value = "Failed to obtain ApplicationPolicy for domain %s"
   )
   IllegalStateException failedToObtainApplicationPolicy(String var1);

   @Message(
      id = 49,
      value = "AuthenticationInfo not set in security domain %s"
   )
   IllegalStateException failedToObtainAuthenticationInfo(String var1);

   @Message(
      id = 50,
      value = "No ServerAuthModule configured to support type %s"
   )
   IllegalStateException noServerAuthModuleForRequestType(Class<?> var1);

   @Message(
      id = 51,
      value = "Failed to create principal: %s"
   )
   LoginException failedToCreatePrincipal(String var1);

   @Message(
      id = 52,
      value = "Supplied credential did not match existing credential for alias %s"
   )
   FailedLoginException failedToMatchCredential(String var1);

   @Message(
      id = 53,
      value = "No CallbackHandler available to collect authentication information"
   )
   LoginException noCallbackHandlerAvailable();

   @Message(
      id = 54,
      value = "Unable to obtain a X509Certificate from %s"
   )
   LoginException unableToGetCertificateFromClass(Class<?> var1);

   @Message(
      id = 55,
      value = "Failed to invoke CallbackHandler"
   )
   LoginException failedToInvokeCallbackHandler();

   @Message(
      id = 56,
      value = "Supplied credential: "
   )
   String suppliedCredentialMessage();

   @Message(
      id = 57,
      value = "Existing credential: "
   )
   String existingCredentialMessage();

   @Message(
      id = 58,
      value = "No match for alias %s, existing aliases: %s"
   )
   String noMatchForAliasMessage(String var1, List<String> var2);

   @Message(
      id = 59,
      value = "Missing properties file: %s"
   )
   LoginException missingPropertiesFile(String var1);

   @Message(
      id = 60,
      value = "Unable to get TransactionManager"
   )
   RuntimeException failedToGetTransactionManager(@Cause Throwable var1);

   @Message(
      id = 61,
      value = "Invalid null TransactionManager"
   )
   IllegalStateException invalidNullTransactionManager();

   @Message(
      id = 62,
      value = "No matching username found in principals"
   )
   FailedLoginException noMatchingUsernameFoundInPrincipals();

   @Message(
      id = 63,
      value = "No matching username found in roles"
   )
   FailedLoginException noMatchingUsernameFoundInRoles();

   @Message(
      id = 64,
      value = "Error looking up DataSource from %s"
   )
   String failedToLookupDataSourceMessage(String var1);

   @Message(
      id = 65,
      value = "Error processing query"
   )
   String failedToProcessQueryMessage();

   @Message(
      id = 66,
      value = "Failed to decode bindCredential"
   )
   IllegalArgumentException failedToDecodeBindCredential(@Cause Throwable var1);

   @Message(
      id = 67,
      value = "Missing required module option: %s"
   )
   String missingRequiredModuleOptionMessage(String var1);

   @Message(
      id = 68,
      value = "Failed to instantiate delegate module %s"
   )
   LoginException failedToInstantiateDelegateModule(String var1);

   @Message(
      id = 69,
      value = "Unable to get password value from vault"
   )
   LoginException unableToGetPasswordFromVault();

   @Message(
      id = 70,
      value = "Password invalid/Password required"
   )
   FailedLoginException invalidPassword();

   @Message(
      id = 71,
      value = "Failed to instantiate %s class"
   )
   String failedToInstantiateClassMessage(Class<?> var1);

   @Message(
      id = 72,
      value = "Properties file %s not found"
   )
   IOException unableToFindPropertiesFile(String var1);

   @Message(
      id = 73,
      value = "Properties file %s not available for loading"
   )
   IOException unableToLoadPropertiesFile(String var1);

   @Message(
      id = 74,
      value = "Missing XML configuration for user/roles mapping"
   )
   LoginException missingXMLUserRolesMapping();

   @Message(
      id = 75,
      value = "The property %s is null"
   )
   IllegalStateException invalidNullProperty(String var1);

   @Message(
      id = 76,
      value = "No matching role found in deployment descriptor for role %s"
   )
   RuntimeException noMatchingRoleFoundInDescriptor(String var1);

   @Message(
      id = 77,
      value = "Permission checks must be different"
   )
   IllegalStateException invalidPermissionChecks();

   @Message(
      id = 78,
      value = "Delegate is missing for layer %s"
   )
   IllegalStateException missingDelegateForLayer(String var1);

   @Message(
      id = 79,
      value = "Invalid delegate map entry: %s"
   )
   IllegalStateException invalidDelegateMapEntry(String var1);

   @Message(
      id = 80,
      value = "Missing XACML policy for contextID %s"
   )
   IllegalStateException missingXACMLPolicyForContextId(String var1);

   @Message(
      id = 81,
      value = "Cache miss"
   )
   String cacheMissMessage();

   @Message(
      id = 82,
      value = "Cache validation failed"
   )
   String cacheValidationFailedMessage();

   @Message(
      id = 83,
      value = "auth-module references a login module stack that doesn't exist: %s"
   )
   RuntimeException invalidLoginModuleStackRef(String var1);

   @Message(
      id = 84,
      value = "Unable to find schema file %s"
   )
   RuntimeException unableToFindSchema(String var1);

   @Message(
      id = 85,
      value = "Missing required attribute(s): %s"
   )
   XMLStreamException missingRequiredAttributes(String var1, @Param Location var2);

   @Message(
      id = 86,
      value = "Unexpected element %s encountered"
   )
   XMLStreamException unexpectedElement(String var1, @Param Location var2);

   @Message(
      id = 87,
      value = "Unexpected attribute %s encountered"
   )
   XMLStreamException unexpectedAttribute(String var1, @Param Location var2);

   @Message(
      id = 88,
      value = "Unexpected namespace %s encountered"
   )
   XMLStreamException unexpectedNamespace(String var1, @Param Location var2);

   @Message(
      id = 89,
      value = "Identity trust validation failed"
   )
   String identityTrustValidationFailedMessage();

   @Message(
      id = 90,
      value = "Invocation of commit on module failed"
   )
   String moduleCommitFailedMessage();

   @Message(
      id = 91,
      value = "Invocation of abort on module failed"
   )
   String moduleAbortFailedMessage();

   @Message(
      id = 92,
      value = "No PolicyContext exists for contextID %s"
   )
   String noPolicyContextForIdMessage(String var1);

   @Message(
      id = 93,
      value = "Operation not allowed"
   )
   String operationNotAllowedMessage();

   @Message(
      id = 94,
      value = "Failed to parse jacc-policy-config-states.xml"
   )
   IllegalStateException failedToParseJACCStatesConfigFile(@Cause Throwable var1);

   @Message(
      id = 95,
      value = "AuthConfigProvider is null for layer %s, contextID: %s"
   )
   IllegalStateException invalidNullAuthConfigProviderForLayer(String var1, String var2);

   @Message(
      id = 96,
      value = "AuthorizationInfo not set in security domain %s"
   )
   IllegalStateException failedToObtainAuthorizationInfo(String var1);

   @Message(
      id = 97,
      value = "Application policy has no info of type %s"
   )
   IllegalStateException failedToObtainInfoFromAppPolicy(String var1);

   @Message(
      id = 98,
      value = "Application policy -> %s does not match expected security domain %s"
   )
   IllegalStateException unexpectedSecurityDomainInInfo(String var1, String var2);

   @Message(
      id = 99,
      value = "Invalid EJB version: %s"
   )
   IllegalArgumentException invalidEJBVersion(String var1);

   @Message(
      id = 100,
      value = "Either caller subject or caller run-as should be non-null"
   )
   String missingCallerInfoMessage();

   @Message(
      id = 101,
      value = "%s is not an MBean attribute"
   )
   AttributeNotFoundException invalidMBeanAttribute(String var1);

   @Message(
      id = 102,
      value = "Write a password in opaque form to a file for use with the FilePassword accessor\n\nUsage: FilePassword salt count password password-file\n  salt  : an 8 char sequence for PBEKeySpec\n  count : iteration count for PBEKeySpec\n  password : the clear text password to write\n  password-file : the path to the file to write the password to\n"
   )
   String filePasswordUsageMessage();

   @Message(
      id = 103,
      value = "The context security domain does not match expected domain %s"
   )
   IllegalArgumentException unexpectedSecurityDomainInContext(String var1);

   @Message(
      id = 104,
      value = "Unsupported policy registration type: %s"
   )
   RuntimeException invalidPolicyRegistrationType(String var1);

   @Message(
      id = 105,
      value = "Ecrypt a password using the JaasSecurityDomain password\n\nUsage: PBEUtils salt count domain-password password\n  salt : the Salt attribute from the JaasSecurityDomain\n  count : the IterationCount attribute from the JaasSecurityDomain\n  domain-password : the plaintext password that maps to the KeyStorePass attribute from the JaasSecurityDomain\n  password : the plaintext password that should be encrypted with the JaasSecurityDomain password\n"
   )
   String pbeUtilsMessage();

   @Message(
      id = 106,
      value = "Failed to resolve target state %s for transition %s"
   )
   String failedToResolveTargetStateMessage(String var1, String var2);

   @Message(
      id = 107,
      value = "No transition for action %s from state %s "
   )
   String invalidTransitionForActionMessage(String var1, String var2);

   @Message(
      id = 108,
      value = "Unable to locate MBean server"
   )
   IllegalStateException unableToLocateMBeanServer();

   @Message(
      id = 109,
      value = "Failed to create DocumentBuilder"
   )
   RuntimeException failedToCreateDocumentBuilder(@Cause Throwable var1);

   @Message(
      id = 110,
      value = "Failed to find namespace URI for %s"
   )
   RuntimeException failedToFindNamespaceURI(String var1);

   @Message(
      id = 111,
      value = "Usage: Base64Encoder <string> <optional hash algorithm>"
   )
   String base64EncoderMessage();

   @Message(
      id = 112,
      value = "Invalid Base64 string: %s"
   )
   IllegalArgumentException invalidBase64String(String var1);

   @Message(
      id = 113,
      value = "Illegal Base64 character"
   )
   NumberFormatException illegalBase64Character();

   @Message(
      id = 114,
      value = "Failed to validate %s as a URL, file or classpath resource"
   )
   MalformedURLException failedToValidateURL(String var1);

   @Message(
      id = 115,
      value = "JSSE domain %s has been requested to provide sensitive security information, but no service authentication token has been configured on it. Use setServiceAuthToken()"
   )
   IllegalStateException missingServiceAuthToken(String var1);

   @Message(
      id = 116,
      value = "Service authentication token verification failed"
   )
   SecurityException failedToVerifyServiceAuthToken();

   @Message(
      id = 117,
      value = "Cannot load KeyStore of type %s: required keyStoreURL is null"
   )
   RuntimeException invalidNullKeyStoreURL(String var1);

   @Message(
      id = 118,
      value = "Invalid password command type: %s"
   )
   IllegalArgumentException invalidPasswordCommandType(String var1);

   @Message(
      id = 119,
      value = "Unable to get the calling principal or its credentials for resource association"
   )
   LoginException unableToGetPrincipalOrCredsForAssociation();

   @Message(
      id = 120,
      value = "Options map %s is null or empty"
   )
   IllegalArgumentException invalidNullOrEmptyOptionMap(String var1);

   @Message(
      id = 121,
      value = "Option %s is null or empty"
   )
   String invalidNullOrEmptyOptionMessage(String var1);

   @Message(
      id = 122,
      value = "Keystore password is not masked"
   )
   String invalidUnmaskedKeystorePasswordMessage();

   @Message(
      id = 123,
      value = "File or directory %s does not exist"
   )
   String fileOrDirectoryDoesNotExistMessage(String var1);

   @Message(
      id = 124,
      value = "Directory %s does not end with / or \\"
   )
   String invalidDirectoryFormatMessage(String var1);

   @Message(
      id = 125,
      value = "Failed to retrieve public key from keystore using alias %s"
   )
   String failedToRetrievePublicKeyMessage(String var1);

   @Message(
      id = 126,
      value = "Failed to retrieve certificate from keystore using alias %s"
   )
   String failedToRetrieveCertificateMessage(String var1);

   @Message(
      id = 127,
      value = "The shared key is invalid or has been incorrectly encoded"
   )
   String invalidSharedKeyMessage();

   @Message(
      id = 128,
      value = "Unable to encrypt data"
   )
   String unableToEncryptDataMessage();

   @Message(
      id = 129,
      value = "Unable to write shared key file"
   )
   String unableToWriteShareKeyFileMessage();

   @Message(
      id = 130,
      value = "Unable to write vault data file (%s)"
   )
   String unableToWriteVaultDataFileMessage(String var1);

   @Message(
      id = 131,
      value = "Vault mismatch: shared key does not match for vault block %s and attribute name %s"
   )
   String sharedKeyMismatchMessage(String var1, String var2);

   @Message(
      id = 132,
      value = "The specified system property %s is missing"
   )
   IllegalArgumentException missingSystemProperty(String var1);

   @Message(
      id = 133,
      value = "Failed to match %s and %s"
   )
   RuntimeException failedToMatchStrings(String var1, String var2);

   @Message(
      id = 134,
      value = "Unrecognized security vault content version (%s), expecting (from %s to %s)"
   )
   RuntimeException unrecognizedVaultContentVersion(String var1, String var2, String var3);

   @Message(
      id = 135,
      value = "Security Vault contains both covnerted (%s) and pre-conversion data (%s), failed to load vault"
   )
   RuntimeException mixedVaultDataFound(String var1, String var2);

   @Message(
      id = 136,
      value = "Security Vault conversion unsuccessful missing admin key in original vault data"
   )
   RuntimeException missingAdminKeyInOriginalVaultData();

   @Message(
      id = 137,
      value = "Security Vault does not contain SecretKey entry under alias (%s)"
   )
   RuntimeException vaultDoesnotContainSecretKey(String var1);

   @Message(
      id = 138,
      value = "There is no SecretKey under the alias (%s) and the alias is already used to denote diffrent crypto object in the keystore."
   )
   RuntimeException noSecretKeyandAliasAlreadyUsed(String var1);

   @Message(
      id = 139,
      value = "Unable to store keystore to file (%s)"
   )
   RuntimeException unableToStoreKeyStoreToFile(@Cause Throwable var1, String var2);

   @Message(
      id = 140,
      value = "Unable to get keystore (%s)"
   )
   RuntimeException unableToGetKeyStore(@Cause Throwable var1, String var2);

   @Message(
      id = 141,
      value = "Unable to parse referral absolute name: %s"
   )
   RuntimeException unableToParseReferralAbsoluteName(@Cause URISyntaxException var1, String var2);

   @Message(
      id = 142,
      value = "Keystore password should be either masked or prefixed with one of {EXT}, {EXTC}, {CMD}, {CMDC}, {CLASS}"
   )
   String invalidKeystorePasswordFormatMessage();

   @Message(
      id = 143,
      value = "Unable to initialize login context"
   )
   String unableToInitializeLoginContext(@Cause Throwable var1);
}
