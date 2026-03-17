package com.bergerkiller.bukkit.tc.properties.registry;

import com.bergerkiller.bukkit.common.cloud.CloudSimpleHandler;
import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorCondition;
import com.bergerkiller.bukkit.tc.commands.selector.TCSelectorHandlerRegistry;
import com.bergerkiller.bukkit.tc.exception.command.NoPermissionForAnyPropertiesException;
import com.bergerkiller.bukkit.tc.exception.command.NoPermissionForPropertyException;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.IProperty;
import com.bergerkiller.bukkit.tc.properties.api.IPropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.IPropertyRegistry;
import com.bergerkiller.bukkit.tc.properties.api.IPropertySelectorCondition;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyInvalidInputException;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParseResult;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.PropertySelectorCondition;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyInputContext;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.mountiplex.reflection.ReflectionUtil;
import com.bergerkiller.mountiplex.reflection.util.BoxedType;
import com.bergerkiller.mountiplex.reflection.util.FastMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;

public final class TCPropertyRegistry implements IPropertyRegistry, LibraryComponent {
   private static final Pattern LITERALS_PATTERN = Pattern.compile("([\\w\\s]+)\\|?");
   private final TrainCarts plugin;
   private final CloudSimpleHandler commands;
   private final Map<IProperty<Object>, TCPropertyRegistry.PropertyDetails<Object>> properties = new HashMap();
   private final Map<String, IProperty<Object>> propertiesByListedName = new HashMap();
   private Collection<IProperty<Object>> cachedPropertiesAll = null;
   private Map<String, IProperty<Object>> cachedPropertiesByListedName = null;
   private final Map<String, TCPropertyRegistry.PropertyParserElement<?>> parsersByName = new HashMap();
   private final Map<String, TCPropertyRegistry.PropertyParserElement<?>> parsersByPreProcessedName = new HashMap();
   private final List<TCPropertyRegistry.PropertyParserElement<?>> parsersWithComplexRegex = new ArrayList();
   private final List<IProperty<?>> pendingProperties = new ArrayList();
   private IProperty<?> currentPropertyBeingParsed = null;

   public TCPropertyRegistry(TrainCarts plugin, CloudSimpleHandler commands) {
      this.plugin = plugin;
      this.commands = commands;
   }

   public void enable() {
      this.commands.getParser().registerBuilderModifier(PropertyCheckPermission.class, (annot, builder) -> {
         IProperty<?> property = this.currentlyParsedProperty();
         String propertyName = annot.value();
         return builder.prependHandler((context) -> {
            CommandSender sender = (CommandSender)context.sender();
            if (!Permission.COMMAND_PROPERTIES.has(sender) && !Permission.COMMAND_GLOBALPROPERTIES.has(sender)) {
               throw new NoPermissionForAnyPropertiesException();
            } else if (!property.hasPermission(sender, propertyName)) {
               throw new NoPermissionForPropertyException(propertyName);
            }
         });
      });
      this.pendingProperties.forEach(this::parsePropertyAnnotations);
      this.pendingProperties.clear();
   }

   public void disable() {
   }

   private IProperty<?> currentlyParsedProperty() {
      if (this.currentPropertyBeingParsed == null) {
         throw new IllegalStateException("No property is being parsed right now");
      } else {
         return this.currentPropertyBeingParsed;
      }
   }

   public void register(IProperty<?> property) {
      TCPropertyRegistry.PropertyDetails<Object> details = (TCPropertyRegistry.PropertyDetails)CommonUtil.unsafeCast(this.createDetails(property));
      TCPropertyRegistry.PropertyDetails<Object> previous = (TCPropertyRegistry.PropertyDetails)this.properties.put(details.property, details);
      this.invalidateCachedCollections();
      if (previous != null) {
         this.onPropertyRemoved(previous);
      }

      details.parsers.forEach(this::registerParser);
      details.conditions.forEach(this::registerCondition);
      if (details.property.isListed()) {
         this.propertiesByListedName.put(details.listedName, details.property);
      }

      if (this.commands.isEnabled()) {
         this.parsePropertyAnnotations(property);
      } else {
         this.pendingProperties.add(property);
      }

   }

   private void parsePropertyAnnotations(IProperty<?> property) {
      this.currentPropertyBeingParsed = property;

      try {
         this.commands.annotations(property);
      } finally {
         this.currentPropertyBeingParsed = null;
      }

   }

   public void unregister(IProperty<?> property) {
      TCPropertyRegistry.PropertyDetails<Object> removed = (TCPropertyRegistry.PropertyDetails)this.properties.remove(property);
      if (removed != null) {
         this.onPropertyRemoved(removed);
         this.invalidateCachedCollections();
      }

   }

   private void invalidateCachedCollections() {
      this.cachedPropertiesAll = null;
      this.cachedPropertiesByListedName = null;
   }

   private void onPropertyRemoved(TCPropertyRegistry.PropertyDetails<Object> details) {
      details.parsers.forEach(this::unregisterParser);
      details.conditions.forEach(this::unregisterCondition);
      if (details.property.isListed()) {
         this.propertiesByListedName.remove(details.listedName, details.property);
      }

   }

   public <T> Optional<IPropertyParser<T>> findParser(String name) {
      TCPropertyRegistry.RegistryPropertyParser<T> search = new TCPropertyRegistry.RegistryPropertyParser(this.plugin, name);
      return this.findParserElement(search) ? Optional.of(search) : Optional.empty();
   }

   public Collection<IProperty<Object>> all() {
      Collection<IProperty<Object>> all = this.cachedPropertiesAll;
      if (all == null) {
         this.cachedPropertiesAll = all = Collections.unmodifiableCollection(new ArrayList(this.properties.keySet()));
      }

      return all;
   }

   public Map<String, IProperty<Object>> byListedName() {
      Map<String, IProperty<Object>> byListedName = this.cachedPropertiesByListedName;
      if (byListedName == null) {
         this.cachedPropertiesByListedName = byListedName = Collections.unmodifiableMap(new HashMap(this.propertiesByListedName));
      }

      return byListedName;
   }

   private void registerCondition(TCPropertyRegistry.PropertySelectorConditionElement<?> condition) {
      TCSelectorHandlerRegistry registry = (TCSelectorHandlerRegistry)this.plugin.getSelectorHandlerRegistry();
      String[] var3 = condition.names;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String name = var3[var5];
         registry.registerCondition(name, condition);
      }

   }

   private void unregisterCondition(TCPropertyRegistry.PropertySelectorConditionElement<?> condition) {
      TCSelectorHandlerRegistry registry = (TCSelectorHandlerRegistry)this.plugin.getSelectorHandlerRegistry();
      String[] var3 = condition.names;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String name = var3[var5];
         registry.unregisterCondition(name);
      }

   }

   private <T> void registerParser(TCPropertyRegistry.PropertyParserElement<T> parser) {
      List<String> literals = findPatternLiterals(parser.options.value());
      if (literals.isEmpty()) {
         this.parsersWithComplexRegex.add(parser);
      } else {
         Map<String, TCPropertyRegistry.PropertyParserElement<?>> literalMap = parser.options.preProcess() ? this.parsersByPreProcessedName : this.parsersByName;
         literals.forEach((literal) -> {
            literalMap.put(literal, parser);
         });
      }

   }

   private <T> void unregisterParser(TCPropertyRegistry.PropertyParserElement<T> parser) {
      List<String> literals = findPatternLiterals(parser.options.value());
      if (literals.isEmpty()) {
         this.parsersWithComplexRegex.remove(parser);
      } else {
         Map<String, TCPropertyRegistry.PropertyParserElement<?>> literalMap = parser.options.preProcess() ? this.parsersByPreProcessedName : this.parsersByName;
         Iterator var4 = literals.iterator();

         while(var4.hasNext()) {
            String literal = (String)var4.next();
            TCPropertyRegistry.PropertyParserElement<?> removed = (TCPropertyRegistry.PropertyParserElement)literalMap.remove(literal);
            if (removed != parser && removed != null) {
               literalMap.put(literal, removed);
            }
         }
      }

   }

   private <T> boolean findParserElement(TCPropertyRegistry.RegistryPropertyParser<T> parser) {
      TCPropertyRegistry.PropertyParserElement result;
      if ((result = (TCPropertyRegistry.PropertyParserElement)CommonUtil.unsafeCast(this.parsersByName.get(parser.name))) != null && result.match(parser)) {
         return true;
      } else if ((result = (TCPropertyRegistry.PropertyParserElement)CommonUtil.unsafeCast(this.parsersByPreProcessedName.get(parser.namePreProcessed))) != null && result.match(parser)) {
         return true;
      } else {
         Iterator var3 = this.parsersWithComplexRegex.iterator();

         TCPropertyRegistry.PropertyParserElement complexParserElementRaw;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            complexParserElementRaw = (TCPropertyRegistry.PropertyParserElement)var3.next();
         } while(!((TCPropertyRegistry.PropertyParserElement)CommonUtil.unsafeCast(complexParserElementRaw)).match(parser));

         return true;
      }
   }

   private <T> TCPropertyRegistry.PropertyDetails<T> createDetails(IProperty<T> property) {
      List<TCPropertyRegistry.PropertyParserElement<T>> parsers = (List)ReflectionUtil.getAllMethods(property.getClass()).map((method) -> {
         PropertyParser parser = (PropertyParser)method.getAnnotation(PropertyParser.class);
         if (parser == null) {
            return null;
         } else {
            try {
               return new TCPropertyRegistry.PropertyParserElement(property, parser, method);
            } catch (PatternSyntaxException var5) {
               this.plugin.getLogger().log(Level.WARNING, "Invalid syntax of property parser " + method.toGenericString(), var5);
               return null;
            } catch (TCPropertyRegistry.ParserIncorrectSignatureException var6) {
               this.plugin.getLogger().log(Level.WARNING, "Invalid method signature of property parser", var6);
               return null;
            }
         }
      }).filter(Objects::nonNull).collect(Collectors.toList());
      List<TCPropertyRegistry.PropertySelectorConditionElement<T>> conditions = (List)ReflectionUtil.getAllMethods(property.getClass()).map((method) -> {
         PropertySelectorCondition[] options = (PropertySelectorCondition[])method.getAnnotationsByType(PropertySelectorCondition.class);
         if (options.length == 0) {
            return null;
         } else {
            try {
               return new TCPropertyRegistry.PropertySelectorConditionElement(property, options, method);
            } catch (TCPropertyRegistry.SelectorConditionIncorrectSignatureException var5) {
               this.plugin.getLogger().log(Level.WARNING, "Invalid method signature of property selector condition", var5);
               return null;
            }
         }
      }).filter(Objects::nonNull).collect(Collectors.toList());
      return new TCPropertyRegistry.PropertyDetails(property, parsers, conditions);
   }

   public static List<String> findPatternLiterals(String pattern) {
      if (pattern.startsWith("(") && pattern.endsWith(")")) {
         pattern = pattern.substring(1, pattern.length() - 1);
      }

      Matcher matcher = LITERALS_PATTERN.matcher(pattern);
      int expectedStart = 0;
      int endIndex = pattern.length();

      for(ArrayList literals = new ArrayList(); matcher.find() && matcher.start() == expectedStart; expectedStart = matcher.end()) {
         literals.add(matcher.group(1));
         if (matcher.end() == endIndex) {
            return literals;
         }
      }

      return Collections.emptyList();
   }

   private static class PropertyDetails<T> {
      public final IProperty<T> property;
      public final String listedName;
      public final List<TCPropertyRegistry.PropertyParserElement<T>> parsers;
      public final List<TCPropertyRegistry.PropertySelectorConditionElement<T>> conditions;

      public PropertyDetails(IProperty<T> property, List<TCPropertyRegistry.PropertyParserElement<T>> parsers, List<TCPropertyRegistry.PropertySelectorConditionElement<T>> conditions) {
         this.property = property;
         this.listedName = property.getListedName();
         this.parsers = parsers;
         this.conditions = conditions;
      }
   }

   private static class RegistryPropertyParser<T> implements IPropertyParser<T> {
      public final TrainCarts plugin;
      public final String name;
      public final String namePreProcessed;
      public TCPropertyRegistry.PropertyParserElement<T> parser;
      public MatchResult matchResult;

      public RegistryPropertyParser(TrainCarts plugin, String name) {
         this.plugin = plugin;
         this.name = name;
         this.namePreProcessed = name.trim().toLowerCase(Locale.ENGLISH);
         this.parser = null;
         this.matchResult = null;
      }

      public IProperty<T> getProperty() {
         return this.parser.property;
      }

      public String getName() {
         return this.parser.options.preProcess() ? this.namePreProcessed : this.name;
      }

      public boolean isInputPreProcessed() {
         return this.parser.options.preProcess();
      }

      public boolean isProcessedPerCart() {
         return this.parser.options.processPerCart();
      }

      public PropertyParseResult<T> parse(IProperties properties, PropertyInputContext inputContext) {
         IProperty<T> property = this.getProperty();
         String name = this.getName();

         try {
            Object value;
            if (this.parser.inputIsString) {
               value = this.parser.method.invoke(property, inputContext.input());
            } else {
               Object currentValue;
               if (properties != null) {
                  try {
                     currentValue = properties.get(property);
                  } catch (Throwable var8) {
                     this.plugin.getLogger().log(Level.SEVERE, "Failed to read property value of '" + this.name + "'", var8);
                     currentValue = property.getDefault();
                  }
               } else {
                  currentValue = property.getDefault();
               }

               PropertyParseContext<T> context = new PropertyParseContext(this.plugin, properties, currentValue, name, inputContext, this.matchResult);
               value = this.parser.method.invoke(property, context);
            }

            return PropertyParseResult.success(inputContext, property, name, value);
         } catch (PropertyInvalidInputException var9) {
            return PropertyParseResult.failInvalidInput(inputContext, property, name, Localization.PROPERTY_INVALID_INPUT.get(name, inputContext.input(), var9.getMessage()));
         } catch (Throwable var10) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to parse property '" + this.name + "'", var10);
            return PropertyParseResult.failError(inputContext, property, this.name);
         }
      }
   }

   public static class PropertySelectorConditionElement<T> implements IPropertySelectorCondition {
      public final IProperty<T> property;
      public final String[] names;
      private final TCPropertyRegistry.PropertySelectorConditionElement.ArgumentAdapter[] argumentAdapters;
      private final TCPropertyRegistry.PropertySelectorConditionElement.ReturnAdapter returnAdapter;
      private final FastMethod<Object> method;

      public PropertySelectorConditionElement(IProperty<T> property, PropertySelectorCondition[] options, Method method) throws TCPropertyRegistry.SelectorConditionIncorrectSignatureException {
         if (Modifier.isStatic(method.getModifiers())) {
            throw new TCPropertyRegistry.SelectorConditionIncorrectSignatureException(method, "Must not be a static method");
         } else if (method.getReturnType() == Void.TYPE) {
            throw new TCPropertyRegistry.SelectorConditionIncorrectSignatureException(method, "Method should return a value, but return type is void");
         } else {
            Class<?>[] argTypes = method.getParameterTypes();
            this.argumentAdapters = new TCPropertyRegistry.PropertySelectorConditionElement.ArgumentAdapter[argTypes.length];

            for(int i = 0; i < argTypes.length; ++i) {
               Class<?> argType = argTypes[i];
               if (argType.isAssignableFrom(TrainProperties.class)) {
                  this.argumentAdapters[i] = (properties, condition) -> {
                     return properties;
                  };
               } else {
                  if (!argType.isAssignableFrom(SelectorCondition.class)) {
                     throw new TCPropertyRegistry.SelectorConditionIncorrectSignatureException(method, "Method parameter #" + (i + 1) + " has incompatible type " + argType.getName());
                  }

                  this.argumentAdapters[i] = (properties, condition) -> {
                     return condition;
                  };
               }
            }

            Class<?> returnType = BoxedType.getBoxedType(method.getReturnType());
            if (returnType == null) {
               returnType = method.getReturnType();
            }

            if (returnType == Boolean.class) {
               this.returnAdapter = (condition, value) -> {
                  return (Boolean)value;
               };
            } else if (returnType == String.class) {
               this.returnAdapter = (condition, value) -> {
                  return condition.matchesText((String)value);
               };
            } else if (returnType != Float.class && returnType != Double.class) {
               if (!Number.class.isAssignableFrom(returnType)) {
                  throw new TCPropertyRegistry.SelectorConditionIncorrectSignatureException(method, "Method has incompatible return type " + returnType.getName());
               }

               this.returnAdapter = (condition, value) -> {
                  return condition.matchesNumber(((Number)value).longValue());
               };
            } else {
               this.returnAdapter = (condition, value) -> {
                  return condition.matchesNumber(((Number)value).doubleValue());
               };
            }

            this.property = property;
            this.names = (String[])Stream.of(options).map(PropertySelectorCondition::value).toArray((x$0) -> {
               return new String[x$0];
            });
            this.method = new FastMethod();
            this.method.init(method);
         }
      }

      public boolean matches(CommandSender sender, TrainProperties properties, SelectorCondition condition) {
         Object[] args = new Object[this.argumentAdapters.length];

         for(int i = 0; i < args.length; ++i) {
            args[i] = this.argumentAdapters[i].adapt(properties, condition);
         }

         Object result = this.method.invokeVA(this.property, args);
         return this.returnAdapter.adapt(condition, result);
      }

      @FunctionalInterface
      private interface ArgumentAdapter {
         Object adapt(TrainProperties var1, SelectorCondition var2);
      }

      @FunctionalInterface
      private interface ReturnAdapter {
         boolean adapt(SelectorCondition var1, Object var2);
      }
   }

   public static class PropertyParserElement<T> {
      public final IProperty<T> property;
      public final FastMethod<T> method;
      public final PropertyParser options;
      public final boolean inputIsString;
      private final Pattern pattern;

      public PropertyParserElement(IProperty<T> property, PropertyParser options, Method method) throws PatternSyntaxException, TCPropertyRegistry.ParserIncorrectSignatureException {
         if (Modifier.isStatic(method.getModifiers())) {
            throw new TCPropertyRegistry.ParserIncorrectSignatureException(method, "Must not be a static method");
         } else if (method.getParameterCount() != 1) {
            throw new TCPropertyRegistry.ParserIncorrectSignatureException(method, "Parameter count should be 1");
         } else if (method.getReturnType() == Void.TYPE) {
            throw new TCPropertyRegistry.ParserIncorrectSignatureException(method, "Method should return a value, but return type is void");
         } else {
            Class<?> paramType = method.getParameterTypes()[0];
            this.inputIsString = paramType.equals(String.class);
            if (!this.inputIsString && !paramType.isAssignableFrom(PropertyParseContext.class)) {
               throw new TCPropertyRegistry.ParserIncorrectSignatureException(method, "First argument should be PropertyParseContext or String");
            } else {
               this.property = property;
               this.options = options;
               this.pattern = Pattern.compile(anchorRegex(options.value()));
               this.method = new FastMethod();
               this.method.init(method);
            }
         }
      }

      public boolean match(TCPropertyRegistry.RegistryPropertyParser<T> parser) {
         Matcher matcher = this.pattern.matcher(this.options.preProcess() ? parser.namePreProcessed : parser.name);
         if (matcher.find()) {
            parser.parser = this;
            parser.matchResult = matcher;
            return true;
         } else {
            return false;
         }
      }

      private static String anchorRegex(String expression) {
         if (!expression.startsWith("^")) {
            expression = "^" + expression;
         }

         if (!expression.endsWith("$")) {
            expression = expression + "$";
         }

         return expression;
      }
   }

   public static class SelectorConditionIncorrectSignatureException extends Exception {
      private static final long serialVersionUID = 9081453776342069335L;

      public SelectorConditionIncorrectSignatureException(Method method, String reason) {
         super("Method " + method.toGenericString() + " has invalid signature for a selector condition: " + reason);
      }
   }

   public static class ParserIncorrectSignatureException extends Exception {
      private static final long serialVersionUID = -1679698260727072778L;

      public ParserIncorrectSignatureException(Method method, String reason) {
         super("Method " + method.toGenericString() + " has invalid signature for a parser: " + reason);
      }
   }
}
