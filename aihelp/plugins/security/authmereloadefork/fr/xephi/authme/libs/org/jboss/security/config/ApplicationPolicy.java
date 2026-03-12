package fr.xephi.authme.libs.org.jboss.security.config;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.auth.login.AuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.auth.login.BaseAuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.auth.login.JASPIAuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class ApplicationPolicy {
   private final String name;
   private BaseAuthenticationInfo authenticationInfo;
   private ACLInfo aclInfo;
   private AuthorizationInfo authorizationInfo;
   private AuditInfo auditInfo;
   private final Map<String, MappingInfo> mappingInfos;
   private IdentityTrustInfo identityTrustInfo;
   private String baseApplicationPolicyName;
   private PolicyConfig policyConfig;

   public ApplicationPolicy(String theName) {
      this.mappingInfos = new HashMap();
      this.policyConfig = new PolicyConfig();
      if (theName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("theName");
      } else {
         this.name = theName;
      }
   }

   public ApplicationPolicy(String theName, BaseAuthenticationInfo info) {
      this(theName);
      this.authenticationInfo = info;
   }

   public ApplicationPolicy(String theName, AuthorizationInfo info) {
      this(theName);
      this.authorizationInfo = info;
   }

   public ApplicationPolicy(String theName, BaseAuthenticationInfo info, AuthorizationInfo info2) {
      this(theName);
      this.authenticationInfo = info;
      this.authorizationInfo = info2;
   }

   public ACLInfo getAclInfo() {
      ACLInfo info = null;
      ApplicationPolicy basePolicy = this.getBaseApplicationPolicy();
      if (basePolicy != null) {
         info = basePolicy.getAclInfo();
      }

      if (info != null && this.aclInfo == null) {
         return info;
      } else {
         return info != null ? (ACLInfo)this.aclInfo.merge(info) : this.aclInfo;
      }
   }

   public void setAclInfo(ACLInfo aclInfo) {
      this.aclInfo = aclInfo;
   }

   public BaseAuthenticationInfo getAuthenticationInfo() {
      BaseAuthenticationInfo bai = null;
      ApplicationPolicy ap = this.getBaseApplicationPolicy();
      if (ap != null) {
         bai = ap.getAuthenticationInfo();
      }

      if (bai != null && this.authenticationInfo == null) {
         return bai;
      } else {
         return bai != null ? (BaseAuthenticationInfo)this.authenticationInfo.merge(bai) : this.authenticationInfo;
      }
   }

   public void setAuthenticationInfo(BaseAuthenticationInfo authenticationInfo) {
      this.authenticationInfo = authenticationInfo;
   }

   public AuthorizationInfo getAuthorizationInfo() {
      AuthorizationInfo bai = null;
      ApplicationPolicy ap = this.getBaseApplicationPolicy();
      if (ap != null) {
         bai = ap.getAuthorizationInfo();
      }

      if (bai != null && this.authorizationInfo == null) {
         return bai;
      } else {
         return bai != null ? (AuthorizationInfo)this.authorizationInfo.merge(bai) : this.authorizationInfo;
      }
   }

   public void setAuthorizationInfo(AuthorizationInfo authorizationInfo) {
      this.authorizationInfo = authorizationInfo;
   }

   /** @deprecated */
   @Deprecated
   public MappingInfo getRoleMappingInfo() {
      return this.getMappingInfo("role");
   }

   /** @deprecated */
   @Deprecated
   public void setRoleMappingInfo(MappingInfo roleMappingInfo) {
      this.setMappingInfo("role", roleMappingInfo);
   }

   /** @deprecated */
   @Deprecated
   public MappingInfo getPrincipalMappingInfo() {
      return this.getMappingInfo("principal");
   }

   /** @deprecated */
   @Deprecated
   public void setPrincipalMappingInfo(MappingInfo principalMappingInfo) {
      this.setMappingInfo("principal", principalMappingInfo);
   }

   /** @deprecated */
   @Deprecated
   public <T> MappingInfo getMappingInfo(Class<T> t) {
      if (t == RoleGroup.class) {
         return this.getRoleMappingInfo();
      } else if (t == Principal.class) {
         return this.getPrincipalMappingInfo();
      } else {
         throw PicketBoxMessages.MESSAGES.invalidType(RoleGroup.class.getName() + "/" + Principal.class.getName());
      }
   }

   public MappingInfo getMappingInfo(String mappingType) {
      mappingType = mappingType.toLowerCase(Locale.ENGLISH);
      MappingInfo bai = null;
      ApplicationPolicy ap = this.getBaseApplicationPolicy();
      if (ap != null) {
         bai = ap.getMappingInfo(mappingType);
      }

      MappingInfo mappings = (MappingInfo)this.mappingInfos.get(mappingType);
      if (bai != null && mappings == null) {
         return bai;
      } else {
         return bai != null ? (MappingInfo)mappings.merge(bai) : mappings;
      }
   }

   public void setMappingInfo(String mappingType, MappingInfo info) {
      mappingType = mappingType.toLowerCase(Locale.ENGLISH);
      if (this.mappingInfos.containsKey(mappingType)) {
         ((MappingInfo)this.mappingInfos.get(mappingType)).add(info.getModuleEntries());
      } else {
         this.mappingInfos.put(mappingType, info);
      }

   }

   public AuditInfo getAuditInfo() {
      AuditInfo bai = null;
      ApplicationPolicy ap = this.getBaseApplicationPolicy();
      if (ap != null) {
         bai = ap.getAuditInfo();
      }

      if (bai != null && this.auditInfo == null) {
         return bai;
      } else {
         return bai != null ? (AuditInfo)this.auditInfo.merge(bai) : this.auditInfo;
      }
   }

   public void setAuditInfo(AuditInfo auditInfo) {
      this.auditInfo = auditInfo;
   }

   public IdentityTrustInfo getIdentityTrustInfo() {
      IdentityTrustInfo bai = null;
      ApplicationPolicy ap = this.getBaseApplicationPolicy();
      if (ap != null) {
         bai = ap.getIdentityTrustInfo();
      }

      if (bai != null && this.identityTrustInfo == null) {
         return bai;
      } else {
         return bai != null ? (IdentityTrustInfo)this.identityTrustInfo.merge(bai) : this.identityTrustInfo;
      }
   }

   public void setIdentityTrustInfo(IdentityTrustInfo identityTrustInfo) {
      this.identityTrustInfo = identityTrustInfo;
   }

   public String getBaseApplicationPolicyName() {
      return this.baseApplicationPolicyName;
   }

   public void setBaseApplicationPolicyName(String baseApplicationPolicy) {
      this.baseApplicationPolicyName = baseApplicationPolicy;
   }

   public String getName() {
      return this.name;
   }

   public PolicyConfig getPolicyConfig() {
      return this.policyConfig;
   }

   public void setPolicyConfig(PolicyConfig policyConfig) {
      this.policyConfig = policyConfig;
   }

   private ApplicationPolicy getBaseApplicationPolicy() {
      ApplicationPolicy ap = null;
      if (this.baseApplicationPolicyName != null) {
         ap = this.policyConfig.get(this.baseApplicationPolicyName);
         if (ap == null) {
            ap = SecurityConfiguration.getApplicationPolicy(this.baseApplicationPolicyName);
         }
      }

      return ap;
   }

   public void writeContent(XMLStreamWriter writer) throws XMLStreamException {
      writer.writeStartElement(Element.SECURITY_DOMAIN.getLocalName());
      writer.writeAttribute(Attribute.NAME.getLocalName(), this.name);
      if (this.baseApplicationPolicyName != null) {
         writer.writeAttribute(Attribute.EXTENDS.getLocalName(), this.baseApplicationPolicyName);
      }

      if (this.authenticationInfo != null) {
         if (this.authenticationInfo instanceof AuthenticationInfo) {
            writer.writeStartElement(Element.AUTHENTICATION.getLocalName());
            ((AuthenticationInfo)this.authenticationInfo).writeContent(writer);
         } else {
            writer.writeStartElement(Element.AUTHENTICATION_JASPI.getLocalName());
            ((JASPIAuthenticationInfo)this.authenticationInfo).writeContent(writer);
         }
      }

      if (this.aclInfo != null) {
         writer.writeStartElement(Element.ACL.getLocalName());
         this.aclInfo.writeContent(writer);
      }

      if (this.authorizationInfo != null) {
         writer.writeStartElement(Element.AUTHORIZATION.getLocalName());
         this.authorizationInfo.writeContent(writer);
      }

      if (this.auditInfo != null) {
         writer.writeStartElement(Element.AUDIT.getLocalName());
         this.auditInfo.writeContent(writer);
      }

      if (this.identityTrustInfo != null) {
         writer.writeStartElement(Element.IDENTITY_TRUST.getLocalName());
         this.identityTrustInfo.writeContent(writer);
      }

      if (this.mappingInfos != null && this.mappingInfos.size() > 0) {
         writer.writeStartElement(Element.MAPPING.getLocalName());
         Iterator i$ = this.mappingInfos.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, MappingInfo> entry = (Entry)i$.next();
            ((MappingInfo)entry.getValue()).writeContent(writer);
         }
      }

      writer.writeEndElement();
   }
}
