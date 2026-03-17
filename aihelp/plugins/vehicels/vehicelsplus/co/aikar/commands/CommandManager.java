/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.ACFPatterns;
import co.aikar.commands.ACFUtil;
import co.aikar.commands.Annotations;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandConditions;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.CommandExecutionContext;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandHelpFormatter;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.CommandOperationContext;
import co.aikar.commands.CommandParameter;
import co.aikar.commands.CommandReplacements;
import co.aikar.commands.CommandRouter;
import co.aikar.commands.ConditionContext;
import co.aikar.commands.ExceptionHandler;
import co.aikar.commands.IssuerLocaleChangedCallback;
import co.aikar.commands.Locales;
import co.aikar.commands.LogLevel;
import co.aikar.commands.MessageFormatter;
import co.aikar.commands.MessageType;
import co.aikar.commands.RegisteredCommand;
import co.aikar.commands.RootCommand;
import co.aikar.commands.UnresolvedDependencyException;
import co.aikar.commands.UnstableAPI;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.lib.util.Table;
import co.aikar.locales.MessageKeyProvider;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

public abstract class CommandManager<IT, I extends CommandIssuer, FT, MF extends MessageFormatter<FT>, CEC extends CommandExecutionContext<CEC, I>, CC extends ConditionContext<I>> {
    static ThreadLocal<Stack<CommandOperationContext>> commandOperationContext = ThreadLocal.withInitial(() -> new Stack<CommandOperationContext>(){

        @Override
        public synchronized CommandOperationContext peek() {
            return super.size() == 0 ? null : (CommandOperationContext)super.peek();
        }
    });
    protected Map<String, RootCommand> rootCommands = new HashMap<String, RootCommand>();
    protected final CommandReplacements replacements = new CommandReplacements(this);
    protected final CommandConditions<I, CEC, CC> conditions = new CommandConditions(this);
    protected ExceptionHandler defaultExceptionHandler = null;
    boolean logUnhandledExceptions = true;
    protected Table<Class<?>, String, Object> dependencies = new Table();
    protected CommandHelpFormatter helpFormatter = new CommandHelpFormatter(this);
    protected boolean usePerIssuerLocale = false;
    protected List<IssuerLocaleChangedCallback<I>> localeChangedCallbacks = new ArrayList<IssuerLocaleChangedCallback<I>>();
    protected Set<Locale> supportedLanguages = new HashSet<Locale>(Arrays.asList(Locales.ENGLISH, Locales.DUTCH, Locales.GERMAN, Locales.SPANISH, Locales.FRENCH, Locales.CZECH, Locales.PORTUGUESE, Locales.SWEDISH, Locales.NORWEGIAN_BOKMAAL, Locales.NORWEGIAN_NYNORSK, Locales.RUSSIAN, Locales.BULGARIAN, Locales.HUNGARIAN, Locales.TURKISH, Locales.JAPANESE, Locales.CHINESE, Locales.SIMPLIFIED_CHINESE, Locales.TRADITIONAL_CHINESE, Locales.KOREAN, Locales.ITALIAN));
    protected Predicate<String> validNamePredicate = string -> true;
    protected Map<MessageType, MF> formatters = new IdentityHashMap<MessageType, MF>();
    protected MF defaultFormatter;
    protected int defaultHelpPerPage = 10;
    protected Map<UUID, Locale> issuersLocale = new ConcurrentHashMap<UUID, Locale>();
    private Set<String> unstableAPIs = new HashSet<String>();
    private Annotations annotations = new Annotations<CommandManager>(this);
    private CommandRouter router = new CommandRouter(this);

    public static CommandOperationContext getCurrentCommandOperationContext() {
        return commandOperationContext.get().peek();
    }

    public static CommandIssuer getCurrentCommandIssuer() {
        CommandOperationContext commandOperationContext = CommandManager.commandOperationContext.get().peek();
        return commandOperationContext != null ? (CommandIssuer)commandOperationContext.getCommandIssuer() : null;
    }

    public static CommandManager getCurrentCommandManager() {
        CommandOperationContext commandOperationContext = CommandManager.commandOperationContext.get().peek();
        return commandOperationContext != null ? commandOperationContext.getCommandManager() : null;
    }

    public MF setFormat(MessageType messageType, MF MF) {
        return (MF)((MessageFormatter)this.formatters.put(messageType, MF));
    }

    public MF getFormat(MessageType messageType) {
        return (MF)((MessageFormatter)this.formatters.getOrDefault(messageType, this.defaultFormatter));
    }

    public void setFormat(MessageType messageType, FT ... FTArray) {
        MF MF = this.getFormat(messageType);
        for (int i = 1; i <= FTArray.length; ++i) {
            ((MessageFormatter)MF).setColor(i, FTArray[i - 1]);
        }
    }

    public void setFormat(MessageType messageType, int n, FT FT) {
        MF MF = this.getFormat(messageType);
        ((MessageFormatter)MF).setColor(n, FT);
    }

    public MF getDefaultFormatter() {
        return this.defaultFormatter;
    }

    public void setDefaultFormatter(MF MF) {
        this.defaultFormatter = MF;
    }

    public CommandConditions<I, CEC, CC> getCommandConditions() {
        return this.conditions;
    }

    public abstract CommandContexts<?> getCommandContexts();

    public abstract CommandCompletions<?> getCommandCompletions();

    @Deprecated
    @UnstableAPI
    public CommandHelp generateCommandHelp(@NotNull String string) {
        this.verifyUnstableAPI("help");
        CommandOperationContext commandOperationContext = CommandManager.getCurrentCommandOperationContext();
        if (commandOperationContext == null) {
            throw new IllegalStateException("This method can only be called as part of a command execution.");
        }
        return this.generateCommandHelp((CommandIssuer)commandOperationContext.getCommandIssuer(), string);
    }

    @Deprecated
    @UnstableAPI
    public CommandHelp generateCommandHelp(CommandIssuer commandIssuer, @NotNull String string) {
        this.verifyUnstableAPI("help");
        return this.generateCommandHelp(commandIssuer, this.obtainRootCommand(string));
    }

    @Deprecated
    @UnstableAPI
    public CommandHelp generateCommandHelp() {
        this.verifyUnstableAPI("help");
        CommandOperationContext commandOperationContext = CommandManager.getCurrentCommandOperationContext();
        if (commandOperationContext == null) {
            throw new IllegalStateException("This method can only be called as part of a command execution.");
        }
        String string = commandOperationContext.getCommandLabel();
        return this.generateCommandHelp((CommandIssuer)commandOperationContext.getCommandIssuer(), this.obtainRootCommand(string));
    }

    @Deprecated
    @UnstableAPI
    public CommandHelp generateCommandHelp(CommandIssuer commandIssuer, RootCommand rootCommand) {
        this.verifyUnstableAPI("help");
        return new CommandHelp(this, rootCommand, commandIssuer);
    }

    @Deprecated
    @UnstableAPI
    public int getDefaultHelpPerPage() {
        this.verifyUnstableAPI("help");
        return this.defaultHelpPerPage;
    }

    @Deprecated
    @UnstableAPI
    public void setDefaultHelpPerPage(int n) {
        this.verifyUnstableAPI("help");
        this.defaultHelpPerPage = n;
    }

    @Deprecated
    @UnstableAPI
    public void setHelpFormatter(CommandHelpFormatter commandHelpFormatter) {
        this.helpFormatter = commandHelpFormatter;
    }

    @Deprecated
    @UnstableAPI
    public CommandHelpFormatter getHelpFormatter() {
        return this.helpFormatter;
    }

    CommandRouter getRouter() {
        return this.router;
    }

    public abstract void registerCommand(BaseCommand var1);

    public abstract boolean hasRegisteredCommands();

    public abstract boolean isCommandIssuer(Class<?> var1);

    public abstract I getCommandIssuer(Object var1);

    public abstract RootCommand createRootCommand(String var1);

    public abstract Locales getLocales();

    public boolean usingPerIssuerLocale() {
        return this.usePerIssuerLocale;
    }

    public boolean usePerIssuerLocale(boolean bl) {
        boolean bl2 = this.usePerIssuerLocale;
        this.usePerIssuerLocale = bl;
        return bl2;
    }

    public boolean isValidName(@NotNull String string) {
        return this.validNamePredicate.test(string);
    }

    @NotNull
    public Predicate<String> getValidNamePredicate() {
        return this.validNamePredicate;
    }

    public void setValidNamePredicate(@NotNull Predicate<String> predicate) {
        this.validNamePredicate = predicate;
    }

    public ConditionContext createConditionContext(CommandIssuer commandIssuer, String string) {
        return new ConditionContext<CommandIssuer>(commandIssuer, string);
    }

    public abstract CommandExecutionContext createCommandContext(RegisteredCommand var1, CommandParameter var2, CommandIssuer var3, List<String> var4, int var5, Map<String, Object> var6);

    public abstract CommandCompletionContext createCompletionContext(RegisteredCommand var1, CommandIssuer var2, String var3, String var4, String[] var5);

    public abstract void log(LogLevel var1, String var2, Throwable var3);

    public void log(LogLevel logLevel, String string) {
        this.log(logLevel, string, null);
    }

    public CommandReplacements getCommandReplacements() {
        return this.replacements;
    }

    public boolean hasPermission(CommandIssuer commandIssuer, Set<String> set) {
        for (String string : set) {
            if (this.hasPermission(commandIssuer, string)) continue;
            return false;
        }
        return true;
    }

    public boolean hasPermission(CommandIssuer commandIssuer, String string) {
        if (string == null || string.isEmpty()) {
            return true;
        }
        for (String string2 : ACFPatterns.PIPE.split(string)) {
            if (string2.isEmpty()) continue;
            boolean bl = false;
            for (String string3 : ACFPatterns.COMMA.split(string2)) {
                if (!string3.isEmpty() && !(bl = commandIssuer.hasPermission(string3))) break;
            }
            if (!bl) continue;
            return true;
        }
        return false;
    }

    public synchronized RootCommand getRootCommand(@NotNull String string) {
        return this.rootCommands.get(ACFPatterns.SPACE.split(string.toLowerCase(Locale.ENGLISH), 2)[0]);
    }

    public synchronized RootCommand obtainRootCommand(@NotNull String string) {
        return this.rootCommands.computeIfAbsent(ACFPatterns.SPACE.split(string.toLowerCase(Locale.ENGLISH), 2)[0], this::createRootCommand);
    }

    public abstract Collection<RootCommand> getRegisteredRootCommands();

    public RegisteredCommand createRegisteredCommand(BaseCommand baseCommand, String string, Method method, String string2) {
        return new RegisteredCommand(baseCommand, string, method, string2);
    }

    public void setDefaultExceptionHandler(ExceptionHandler exceptionHandler) {
        if (exceptionHandler == null && !this.logUnhandledExceptions) {
            throw new IllegalArgumentException("You may not disable the default exception handler and have logging of unhandled exceptions disabled");
        }
        this.defaultExceptionHandler = exceptionHandler;
    }

    public void setDefaultExceptionHandler(ExceptionHandler exceptionHandler, boolean bl) {
        if (exceptionHandler == null && !bl) {
            throw new IllegalArgumentException("You may not disable the default exception handler and have logging of unhandled exceptions disabled");
        }
        this.logUnhandledExceptions = bl;
        this.defaultExceptionHandler = exceptionHandler;
    }

    public boolean isLoggingUnhandledExceptions() {
        return this.logUnhandledExceptions;
    }

    public ExceptionHandler getDefaultExceptionHandler() {
        return this.defaultExceptionHandler;
    }

    protected boolean handleUncaughtException(BaseCommand baseCommand, RegisteredCommand registeredCommand, CommandIssuer commandIssuer, List<String> list, Throwable throwable) {
        if (throwable instanceof InvocationTargetException && throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        boolean bl = false;
        if (baseCommand.getExceptionHandler() != null) {
            bl = baseCommand.getExceptionHandler().execute(baseCommand, registeredCommand, commandIssuer, list, throwable);
        } else if (this.defaultExceptionHandler != null) {
            bl = this.defaultExceptionHandler.execute(baseCommand, registeredCommand, commandIssuer, list, throwable);
        }
        return bl;
    }

    public void sendMessage(IT IT, MessageType messageType, MessageKeyProvider messageKeyProvider, String ... stringArray) {
        this.sendMessage((CommandIssuer)this.getCommandIssuer(IT), messageType, messageKeyProvider, stringArray);
    }

    public void sendMessage(CommandIssuer commandIssuer, MessageType messageType, MessageKeyProvider messageKeyProvider, String ... stringArray) {
        String string = this.formatMessage(commandIssuer, messageType, messageKeyProvider, stringArray);
        for (String string2 : ACFPatterns.NEWLINE.split(string)) {
            commandIssuer.sendMessageInternal(ACFUtil.rtrim(string2));
        }
    }

    public String formatMessage(CommandIssuer commandIssuer, MessageType messageType, MessageKeyProvider messageKeyProvider, String ... stringArray) {
        String string = this.getLocales().getMessage(commandIssuer, messageKeyProvider.getMessageKey());
        if (stringArray.length > 0) {
            string = ACFUtil.replaceStrings(string, stringArray);
        }
        string = this.getCommandReplacements().replace(string);
        string = this.getLocales().replaceI18NStrings(string);
        MessageFormatter messageFormatter = (MessageFormatter)this.formatters.getOrDefault(messageType, this.defaultFormatter);
        if (messageFormatter != null) {
            string = messageFormatter.format(string);
        }
        return string;
    }

    public void onLocaleChange(IssuerLocaleChangedCallback<I> issuerLocaleChangedCallback) {
        this.localeChangedCallbacks.add(issuerLocaleChangedCallback);
    }

    public void notifyLocaleChange(I i, Locale locale, Locale locale2) {
        this.localeChangedCallbacks.forEach(issuerLocaleChangedCallback -> {
            try {
                issuerLocaleChangedCallback.onIssuerLocaleChange(i, locale, locale2);
            } catch (Exception exception) {
                this.log(LogLevel.ERROR, "Error in notifyLocaleChange", exception);
            }
        });
    }

    public Locale setIssuerLocale(IT IT, Locale locale) {
        I i = this.getCommandIssuer(IT);
        Locale locale2 = this.issuersLocale.put(i.getUniqueId(), locale);
        if (!Objects.equals(locale2, locale)) {
            this.notifyLocaleChange(i, locale2, locale);
        }
        return locale2;
    }

    public Locale getIssuerLocale(CommandIssuer commandIssuer) {
        Locale locale;
        if (this.usingPerIssuerLocale() && commandIssuer != null && (locale = this.issuersLocale.get(commandIssuer.getUniqueId())) != null) {
            return locale;
        }
        return this.getLocales().getDefaultLocale();
    }

    CommandOperationContext<I> createCommandOperationContext(BaseCommand baseCommand, CommandIssuer commandIssuer, String string, String[] stringArray, boolean bl) {
        return new CommandOperationContext<CommandIssuer>(this, commandIssuer, baseCommand, string, stringArray, bl);
    }

    public Set<Locale> getSupportedLanguages() {
        return this.supportedLanguages;
    }

    public void addSupportedLanguage(Locale locale) {
        this.supportedLanguages.add(locale);
        this.getLocales().loadMissingBundles();
    }

    public <T> void registerDependency(Class<? extends T> clazz, T t) {
        this.registerDependency(clazz, clazz.getName(), t);
    }

    public <T> void registerDependency(Class<? extends T> clazz, String string, T t) {
        if (this.dependencies.containsKey(clazz, string)) {
            throw new IllegalStateException("There is already an instance of " + clazz.getName() + " with the key " + string + " registered!");
        }
        this.dependencies.put(clazz, string, t);
    }

    public <T> void unregisterDependency(Class<? extends T> clazz) {
        this.unregisterDependency(clazz, clazz.getName());
    }

    public <T> void unregisterDependency(Class<? extends T> clazz, String string) {
        if (!this.dependencies.containsKey(clazz, string)) {
            throw new IllegalStateException("Unable to unregister a dependency of " + clazz.getName() + " with the key " + string + " because it wasn't registered");
        }
        this.dependencies.remove(clazz, string);
    }

    void injectDependencies(BaseCommand baseCommand) {
        Class<?> clazz = baseCommand.getClass();
        do {
            for (Field field : clazz.getDeclaredFields()) {
                if (!this.annotations.hasAnnotation(field, Dependency.class)) continue;
                String string = this.annotations.getAnnotationValue(field, Dependency.class);
                String string2 = string;
                string2 = string2.isEmpty() ? field.getType().getName() : string2;
                Object object = this.dependencies.row(field.getType()).get(string2);
                if (object == null) {
                    throw new UnresolvedDependencyException("Could not find a registered instance of " + field.getType().getName() + " with key " + string2 + " for field " + field.getName() + " in class " + baseCommand.getClass().getName());
                }
                try {
                    boolean bl = field.isAccessible();
                    if (!bl) {
                        field.setAccessible(true);
                    }
                    field.set(baseCommand, object);
                    field.setAccessible(bl);
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
            }
        } while (!(clazz = clazz.getSuperclass()).equals(BaseCommand.class));
    }

    @Deprecated
    public void enableUnstableAPI(String string) {
        this.unstableAPIs.add(string);
    }

    void verifyUnstableAPI(String string) {
        if (!this.unstableAPIs.contains(string)) {
            throw new IllegalStateException("Using an unstable API that has not been enabled ( " + string + "). See https://acfunstable.emc.gs");
        }
    }

    boolean hasUnstableAPI(String string) {
        return this.unstableAPIs.contains(string);
    }

    Annotations getAnnotations() {
        return this.annotations;
    }

    public String getCommandPrefix(CommandIssuer commandIssuer) {
        return "";
    }
}

