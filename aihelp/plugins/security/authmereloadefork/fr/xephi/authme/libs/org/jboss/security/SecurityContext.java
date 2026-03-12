package fr.xephi.authme.libs.org.jboss.security;

import java.io.Serializable;
import java.util.Map;

public interface SecurityContext extends SecurityManagerLocator, Serializable, Cloneable {
   ISecurityManagement getSecurityManagement();

   void setSecurityManagement(ISecurityManagement var1);

   Map<String, Object> getData();

   String getSecurityDomain();

   void setSecurityDomain(String var1);

   SubjectInfo getSubjectInfo();

   void setSubjectInfo(SubjectInfo var1);

   RunAs getIncomingRunAs();

   void setIncomingRunAs(RunAs var1);

   RunAs getOutgoingRunAs();

   void setOutgoingRunAs(RunAs var1);

   SecurityContextUtil getUtil();
}
