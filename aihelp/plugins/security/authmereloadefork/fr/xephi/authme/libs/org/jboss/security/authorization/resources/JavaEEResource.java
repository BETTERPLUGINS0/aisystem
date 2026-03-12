package fr.xephi.authme.libs.org.jboss.security.authorization.resources;

import fr.xephi.authme.libs.org.jboss.security.RunAs;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.ResourceType;
import fr.xephi.authme.libs.org.jboss.security.javaee.SecurityRoleRef;
import java.security.CodeSource;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;

public abstract class JavaEEResource implements Resource {
   protected Map<String, Object> map = new HashMap();
   protected String policyContextID = null;
   protected Subject callerSubject = null;
   protected RunAs callerRunAsIdentity = null;
   protected CodeSource codeSource = null;
   protected Principal principal = null;
   protected Set<SecurityRoleRef> securityRoleReferences = null;

   public abstract ResourceType getLayer();

   public void add(String key, Object value) {
      this.map.put(key, value);
   }

   public Map<String, Object> getMap() {
      return Collections.unmodifiableMap(this.map);
   }

   public Subject getCallerSubject() {
      return this.callerSubject;
   }

   public void setCallerSubject(Subject callerSubject) {
      this.callerSubject = callerSubject;
   }

   public RunAs getCallerRunAsIdentity() {
      return this.callerRunAsIdentity;
   }

   public void setCallerRunAsIdentity(RunAs callerRunAsIdentity) {
      this.callerRunAsIdentity = callerRunAsIdentity;
   }

   public CodeSource getCodeSource() {
      return this.codeSource;
   }

   public void setCodeSource(CodeSource codeSource) {
      this.codeSource = codeSource;
   }

   public String getPolicyContextID() {
      return this.policyContextID;
   }

   public void setPolicyContextID(String policyContextID) {
      this.policyContextID = policyContextID;
   }

   public Principal getPrincipal() {
      return this.principal;
   }

   public void setPrincipal(Principal principal) {
      this.principal = principal;
   }

   public Set<SecurityRoleRef> getSecurityRoleReferences() {
      return this.securityRoleReferences;
   }

   public void setSecurityRoleReferences(Set<SecurityRoleRef> securityRoleReferences) {
      this.securityRoleReferences = securityRoleReferences;
   }
}
