package ac.grim.grimac.utils.common.arguments;

import ac.grim.grimac.platform.api.Platform;

public class CommonGrimArguments {
   private static final SystemArgumentFactory FACTORY = SystemArgumentFactory.Builder.of("Grim").optionModifier((builder) -> {
      builder.key("Grim" + builder.options().getKey());
   }).supportEnv().build();
   public static final SystemArgument<Boolean> KICK_ON_TRANSACTION_ERRORS;
   public static final SystemArgument<String> API_URL;
   public static final SystemArgument<String> PASTE_URL;
   public static final SystemArgument<Platform> PLATFORM_OVERRIDE;
   public static final SystemArgument<Boolean> USE_CHAT_FAST_BYPASS;

   static {
      KICK_ON_TRANSACTION_ERRORS = FACTORY.create(ArgumentUtils.string("KickOnTransactionTaskErrors", false));
      API_URL = FACTORY.create(ArgumentUtils.string("APIUrl", "https://api.grim.ac/v1/server/"));
      PASTE_URL = FACTORY.create(ArgumentUtils.string("PasteUrl", "https://paste.grim.ac/"));
      PLATFORM_OVERRIDE = FACTORY.create(ArgumentUtils.platform("PlatformOverride"));
      USE_CHAT_FAST_BYPASS = FACTORY.create(ArgumentUtils.string("ChatFastBypass", true));
   }
}
