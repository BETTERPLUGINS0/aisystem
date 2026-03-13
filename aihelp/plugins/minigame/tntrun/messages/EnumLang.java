package tntrun.messages;

public enum EnumLang {
   Arabic("ar-SA", "Arabic", false),
   Chinese_CN("zh-CN", "Chinese", true),
   Chinese_TW("zh-TW", "Chinese(Taiwan)", true),
   Czech("cs-CZ", "Czech", true),
   Danish("da-DK", "Danish", false),
   Dutch("nl-NL", "Dutch", true),
   English("en-GB", "English(UK)", true),
   English_US("en-US", "English(US)", false),
   Finnish("fi-FI", "Finnish", false),
   French("fr-FR", "French", true),
   German("de-DE", "German", true),
   Hungarian("hu-HU", "Hungarian", false),
   Italian("it-IT", "Italian", true),
   Japanese("ja-JP", "Japanese", true),
   Korean("ko-KR", "Korean", true),
   Norwegian("no-NO", "Norwegian", true),
   Polish("pl-PL", "Polish", true),
   Portugese("pt-PT", "Portugese", true),
   Russian("ru-RU", "Russian", true),
   Spanish("es-ES", "Spanish(Spain)", true),
   Swedish("sv-SE", "Swedish", true),
   Tamil("ta-IN", "Tamil", true),
   Ukranian("uk-UA", "Ukranian", true),
   Welsh("cy-GB", "Welsh", true);

   private final String code;
   private final String description;
   private final boolean supported;

   private EnumLang(String param3, String param4, boolean param5) {
      this.code = code;
      this.description = description;
      this.supported = supported;
   }

   public boolean isSupported() {
      return this.supported;
   }

   public String getDesc() {
      return this.description;
   }

   public String getCode() {
      return this.code;
   }

   // $FF: synthetic method
   private static EnumLang[] $values() {
      return new EnumLang[]{Arabic, Chinese_CN, Chinese_TW, Czech, Danish, Dutch, English, English_US, Finnish, French, German, Hungarian, Italian, Japanese, Korean, Norwegian, Polish, Portugese, Russian, Spanish, Swedish, Tamil, Ukranian, Welsh};
   }
}
