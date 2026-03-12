package fr.xephi.authme.libs.org.jboss.security.audit;

import java.util.ArrayList;
import java.util.List;

public abstract class AuditContext {
   protected String securityDomain = null;
   protected List<AuditProvider> providerList = new ArrayList();

   public void audit(AuditEvent ae) {
      int len = this.providerList.size();

      for(int i = 0; i < len; ++i) {
         AuditProvider ap = (AuditProvider)this.providerList.get(i);
         ap.audit(ae);
      }

   }

   public void addProvider(AuditProvider ap) {
      this.providerList.add(ap);
   }

   public void addProviders(List<AuditProvider> list) {
      this.providerList.addAll(list);
   }

   public List<AuditProvider> getProviders() {
      return this.providerList;
   }

   public void replaceProviders(List<AuditProvider> list) {
      this.providerList.clear();
      this.providerList = list;
   }
}
