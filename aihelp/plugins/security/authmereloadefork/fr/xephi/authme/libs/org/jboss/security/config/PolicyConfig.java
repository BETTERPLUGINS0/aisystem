package fr.xephi.authme.libs.org.jboss.security.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PolicyConfig {
   Map<String, ApplicationPolicy> config = Collections.synchronizedMap(new HashMap());

   public void add(ApplicationPolicy ai) {
      this.config.put(ai.getName(), ai);
      ai.setPolicyConfig(this);
   }

   public ApplicationPolicy get(String name) {
      return (ApplicationPolicy)this.config.get(name);
   }

   public ApplicationPolicy remove(String name) {
      return (ApplicationPolicy)this.config.remove(name);
   }

   public void clear() {
      this.config.clear();
   }

   public Set<String> getConfigNames() {
      return this.config.keySet();
   }

   public int size() {
      return this.config.size();
   }

   public boolean containsKey(String name) {
      return this.config.containsKey(name);
   }

   public void copy(PolicyConfig pc) {
      this.config.putAll(pc.config);
   }

   public Collection<ApplicationPolicy> getPolicies() {
      return Collections.unmodifiableCollection(this.config.values());
   }
}
