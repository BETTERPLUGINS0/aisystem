package advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.error.ErrorMissingJointActionData;
import advancedplugins.pm2.cv.models.api.model.rpc.error.ErrorWrongJointBehaviorDataType;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.BehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.DefaultRenderType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.IRenderType;
import com.google.gson.JsonDeserializer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;

public class JointActionType<T extends JointAction> {
   private final JointActionType.ActionProvider<T> actionProvider;
   private final JointActionType.BehaviorManagerProvider<T> behaviorManagerProvider;
   private final String id;
   private final Map<String, Class<?>> requiredArguments;
   private final Map<String, Class<?>> optionalArguments;
   private final Map<Class<?>, JsonDeserializer<?>> dataDeserializer;
   private final IRenderType renderType;
   private final Set<ProceduralType> proceduralTypes;
   private final Predicate<Set<JointActionType<?>>> predicate;
   private final JointActionType.ActionProvider<T> forcedActionProvider;
   private final boolean ignoreCubes;
   private final boolean pivot;

   protected JointActionType(JointActionType.ActionProvider<T> var1, JointActionType.BehaviorManagerProvider<T> var2, String var3, Map<String, Class<?>> var4, Map<String, Class<?>> var5, Map<Class<?>, JsonDeserializer<?>> var6, IRenderType var7, Set<ProceduralType> var8, Predicate<Set<JointActionType<?>>> var9, JointActionType.ActionProvider<T> var10, boolean var11, boolean var12) {
      this.actionProvider = var1;
      this.behaviorManagerProvider = var2;
      this.id = var3;
      this.requiredArguments = var4;
      this.optionalArguments = var5;
      this.dataDeserializer = var6;
      this.renderType = var7;
      this.proceduralTypes = var8;
      this.predicate = var9;
      this.forcedActionProvider = var10;
      this.ignoreCubes = var11;
      this.pivot = var12;
   }

   public static boolean noProcedural(Set<JointActionType<?>> var0) {
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         JointActionType var2 = (JointActionType)var1.next();
         if (!var2.getProceduralTypes().isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public void assignCachedProvider(BlueprintJoint var1, Map<String, Object> var2) {
      ConcurrentHashMap var3 = new ConcurrentHashMap();
      Iterator var4 = this.requiredArguments.entrySet().iterator();

      Entry var5;
      String var6;
      Class var7;
      Object var8;
      while(var4.hasNext()) {
         var5 = (Entry)var4.next();
         var6 = (String)var5.getKey();
         var7 = (Class)var5.getValue();
         var8 = var2.get(var6);
         if (var8 == null) {
            new ErrorMissingJointActionData(var1.getName(), this, var6);
            return;
         }

         if (!var7.isAssignableFrom(var8.getClass())) {
            new ErrorWrongJointBehaviorDataType(var1.getName(), this, var6, var7, var8.getClass());
            return;
         }

         var3.put(var6, var8);
      }

      var4 = this.optionalArguments.entrySet().iterator();

      while(var4.hasNext()) {
         var5 = (Entry)var4.next();
         var6 = (String)var5.getKey();
         var7 = (Class)var5.getValue();
         var8 = var2.get(var6);
         if (var8 != null) {
            if (!var7.isAssignableFrom(var8.getClass())) {
               new ErrorWrongJointBehaviorDataType(var1.getName(), this, var6, var7, var8.getClass());
               return;
            }

            var3.put(var6, var8);
         }
      }

      var1.getCachedBehaviorProvider().put(this, new JointActionType.CachedProvider(this.actionProvider, this, new JointBehaviorData(var3)));
   }

   public void assignForcedCachedProvider(BlueprintJoint var1) {
      if (this.forcedActionProvider != null && !var1.getCachedBehaviorProvider().containsKey(this)) {
         var1.getCachedBehaviorProvider().put(this, new JointActionType.CachedProvider(this.forcedActionProvider, this, new JointBehaviorData(new ConcurrentHashMap())));
      }

   }

   public boolean test(Set<JointActionType<?>> var1) {
      return this.predicate.test(var1);
   }

   public JointActionType.ActionProvider<T> getBehaviorProvider() {
      return this.actionProvider;
   }

   public JointActionType.BehaviorManagerProvider<T> getBehaviorManagerProvider() {
      return this.behaviorManagerProvider;
   }

   public String getId() {
      return this.id;
   }

   public Map<String, Class<?>> getRequiredArguments() {
      return this.requiredArguments;
   }

   public Map<String, Class<?>> getOptionalArguments() {
      return this.optionalArguments;
   }

   public Map<Class<?>, JsonDeserializer<?>> getDataDeserializer() {
      return this.dataDeserializer;
   }

   public IRenderType getRenderType() {
      return this.renderType;
   }

   public Set<ProceduralType> getProceduralTypes() {
      return this.proceduralTypes;
   }

   public Predicate<Set<JointActionType<?>>> getPredicate() {
      return this.predicate;
   }

   public JointActionType.ActionProvider<T> getForcedBehaviorProvider() {
      return this.forcedActionProvider;
   }

   public boolean isIgnoreCubes() {
      return this.ignoreCubes;
   }

   public boolean isPivot() {
      return this.pivot;
   }

   @FunctionalInterface
   public interface ActionProvider<T extends JointAction> {
      T create(IJoint var1, JointActionType<T> var2, JointBehaviorData var3);
   }

   @FunctionalInterface
   public interface BehaviorManagerProvider<T extends JointAction> {
      BehaviorManager<T> create(IVisualModel var1, JointActionType<T> var2);
   }

   public static class CachedProvider<T extends JointAction> {
      private final JointActionType.ActionProvider<T> actionProvider;
      private final JointActionType<T> type;
      private final JointBehaviorData data;

      public CachedProvider(JointActionType.ActionProvider<T> var1, JointActionType<T> var2, JointBehaviorData var3) {
         this.actionProvider = var1;
         this.type = var2;
         this.data = var3;
      }

      public T create(IJoint var1) {
         return this.actionProvider.create(var1, this.type, this.data);
      }

      public JointActionType<T> getType() {
         return this.type;
      }

      public JointBehaviorData getData() {
         return this.data;
      }
   }

   public static class Builder<T extends JointAction> {
      private final JointActionType.ActionProvider<T> actionProvider;
      private final JointActionType.BehaviorManagerProvider<T> behaviorManagerProvider;
      private final String id;
      private final Map<String, Class<?>> requiredArguments = new ConcurrentHashMap();
      private final Map<String, Class<?>> optionalArguments = new ConcurrentHashMap();
      private final Map<Class<?>, JsonDeserializer<?>> dataDeserializer = new ConcurrentHashMap();
      private final Set<ProceduralType> proceduralTypes = new HashSet();
      private IRenderType renderType;
      private Predicate<Set<JointActionType<?>>> predicate;
      private JointActionType.ActionProvider<T> forcedActionProvider;
      private boolean ignoreCubes;
      private boolean pivot;

      protected Builder(JointActionType.ActionProvider<T> var1, JointActionType.BehaviorManagerProvider<T> var2, String var3) {
         this.renderType = DefaultRenderType.ANY;
         this.predicate = (var0) -> {
            return true;
         };
         this.actionProvider = var1;
         this.behaviorManagerProvider = var2;
         this.id = var3;
      }

      public static <T extends JointAction> JointActionType.Builder<T> of(JointActionType.ActionProvider<T> var0, @Nullable JointActionType.BehaviorManagerProvider<T> var1, String var2) {
         return new JointActionType.Builder(var0, var1, var2);
      }

      public JointActionType.Builder<T> required(String var1, Class<?> var2) {
         this.requiredArguments.put(var1, var2);
         return this;
      }

      public <S> JointActionType.Builder<T> required(String var1, Class<S> var2, JsonDeserializer<S> var3) {
         this.requiredArguments.put(var1, var2);
         this.dataDeserializer.put(var2, var3);
         return this;
      }

      public JointActionType.Builder<T> optional(String var1, Class<?> var2) {
         this.optionalArguments.put(var1, var2);
         return this;
      }

      public <S> JointActionType.Builder<T> optional(String var1, Class<S> var2, JsonDeserializer<S> var3) {
         this.optionalArguments.put(var1, var2);
         this.dataDeserializer.put(var2, var3);
         return this;
      }

      public JointActionType.Builder<T> renderType(IRenderType var1) {
         this.renderType = var1;
         return this;
      }

      public JointActionType.Builder<T> procedural(ProceduralType... var1) {
         this.proceduralTypes.addAll(Arrays.asList(var1));
         return this;
      }

      public JointActionType.Builder<T> predicate(Predicate<Set<JointActionType<?>>> var1) {
         this.predicate = var1;
         return this;
      }

      public JointActionType.Builder<T> forced(JointActionType.ActionProvider<T> var1) {
         this.forcedActionProvider = var1;
         return this;
      }

      public JointActionType.Builder<T> ignoreCubes() {
         this.ignoreCubes = true;
         return this;
      }

      public JointActionType.Builder<T> pivot() {
         this.pivot = true;
         return this;
      }

      public JointActionType<T> build() {
         return new JointActionType(this.actionProvider, this.behaviorManagerProvider, this.id, this.requiredArguments, this.optionalArguments, this.dataDeserializer, this.renderType, this.proceduralTypes, this.predicate, this.forcedActionProvider, this.ignoreCubes, this.pivot);
      }
   }
}
