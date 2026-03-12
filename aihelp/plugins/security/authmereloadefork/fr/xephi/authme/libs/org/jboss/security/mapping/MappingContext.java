package fr.xephi.authme.libs.org.jboss.security.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MappingContext<T> {
   private List<MappingProvider<T>> modules = new ArrayList();
   private MappingResult<T> result;

   public MappingContext(List<MappingProvider<T>> mod) {
      this.modules = mod;
   }

   public List<MappingProvider<T>> getModules() {
      return this.modules;
   }

   public void performMapping(Map<String, Object> contextMap, T mappedObject) {
      int len = this.modules.size();
      this.result = new MappingResult();

      for(int i = 0; i < len; ++i) {
         MappingProvider<T> mp = (MappingProvider)this.modules.get(i);
         mp.setMappingResult(this.result);
         mp.performMapping(contextMap, mappedObject);
      }

   }

   public MappingResult<T> getMappingResult() {
      return this.result;
   }

   public boolean hasModules() {
      return this.modules.size() > 0;
   }
}
