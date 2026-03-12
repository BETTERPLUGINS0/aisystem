package fr.xephi.authme.libs.ch.jalu.injector;

import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.dependency.CyclicDependenciesDetector;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.dependency.FactoryDependencyHandler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.dependency.SavedAnnotationsHandler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.dependency.SingletonStoreDependencyHandler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.DefaultInjectionProvider;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.ProviderHandler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.postconstruct.PostConstructMethodInvoker;
import fr.xephi.authme.libs.ch.jalu.injector.utils.InjectorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class InjectorBuilder {
   private InjectorConfig config = new InjectorConfig();

   public static List<Handler> createDefaultHandlers(String rootPackage) {
      InjectorUtils.checkNotNull(rootPackage, "root package may not be null");
      return new ArrayList(Arrays.asList(new SavedAnnotationsHandler(), new ProviderHandler(), new FactoryDependencyHandler(), new SingletonStoreDependencyHandler(), new CyclicDependenciesDetector(), new DefaultInjectionProvider(rootPackage), new PostConstructMethodInvoker()));
   }

   public static List<Handler> createInstantiationProviders(String rootPackage) {
      InjectorUtils.checkNotNull(rootPackage, "root package may not be null");
      return new ArrayList(Arrays.asList(new ProviderHandler(), new FactoryDependencyHandler(), new SingletonStoreDependencyHandler(), new DefaultInjectionProvider(rootPackage)));
   }

   public InjectorBuilder addDefaultHandlers(String rootPackage) {
      return this.addHandlers((Collection)createDefaultHandlers(rootPackage));
   }

   public InjectorBuilder addHandlers(Handler... handlers) {
      return this.addHandlers((Collection)Arrays.asList(handlers));
   }

   public InjectorBuilder addHandlers(Collection<? extends Handler> handlers) {
      this.config.addHandlers(handlers);
      return this;
   }

   public Injector create() {
      return new InjectorImpl(this.config);
   }
}
