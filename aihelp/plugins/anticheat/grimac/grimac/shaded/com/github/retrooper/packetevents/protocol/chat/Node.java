package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Optional;

public class Node {
   public static final byte TYPE_MASK = 3;
   public static final byte TYPE_ROOT = 0;
   public static final byte TYPE_LITERAL = 1;
   public static final byte TYPE_ARGUMENT = 2;
   public static final byte FLAG_MASK = -4;
   public static final byte FLAG_EXECUTABLE = 4;
   public static final byte FLAG_REDIRECT = 8;
   public static final byte FLAG_CUSTOM_SUGGESTIONS = 16;
   public static final byte FLAG_RESTRICTED = 32;
   private byte flags;
   private List<Integer> children;
   private int redirectNodeIndex;
   private Optional<String> name;
   private Optional<Parsers.Parser> parser;
   private Optional<List<Object>> properties;
   private Optional<ResourceLocation> suggestionsType;

   public Node(byte flags, List<Integer> children, int redirectNodeIndex, @Nullable String name, @Nullable Integer parserID, @Nullable List<Object> properties, @Nullable ResourceLocation suggestionsType) {
      this(flags, children, redirectNodeIndex, name, parserID == null ? null : Parsers.getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), parserID), properties, suggestionsType);
   }

   public Node(byte flags, List<Integer> children, int redirectNodeIndex, @Nullable String name, @Nullable Parsers.Parser parser, @Nullable List<Object> properties, @Nullable ResourceLocation suggestionsType) {
      this.flags = flags;
      this.children = children;
      this.redirectNodeIndex = redirectNodeIndex;
      this.name = Optional.ofNullable(name);
      this.parser = Optional.ofNullable(parser);
      this.properties = Optional.ofNullable(properties);
      this.suggestionsType = Optional.ofNullable(suggestionsType);
   }

   public byte getFlags() {
      return this.flags;
   }

   public void setFlags(byte flags) {
      this.flags = flags;
   }

   public List<Integer> getChildren() {
      return this.children;
   }

   public void setChildren(List<Integer> children) {
      this.children = children;
   }

   public int getRedirectNodeIndex() {
      return this.redirectNodeIndex;
   }

   public void setRedirectNodeIndex(int redirectNodeIndex) {
      this.redirectNodeIndex = redirectNodeIndex;
   }

   public Optional<String> getName() {
      return this.name;
   }

   public void setName(Optional<String> name) {
      this.name = name;
   }

   public Optional<Parsers.Parser> getParser() {
      return this.parser;
   }

   public void setParser(Optional<Parsers.Parser> parser) {
      this.parser = parser;
   }

   public Optional<Integer> getParserID() {
      return this.parser.map((parser) -> {
         return parser.getId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
      });
   }

   public void setParserID(Optional<Integer> parserID) {
      this.parser = parserID.map((id) -> {
         return Parsers.getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), id);
      });
   }

   public Optional<List<Object>> getProperties() {
      return this.properties;
   }

   public void setProperties(Optional<List<Object>> properties) {
      this.properties = properties;
   }

   public Optional<ResourceLocation> getSuggestionsType() {
      return this.suggestionsType;
   }

   public void setSuggestionsType(Optional<ResourceLocation> suggestionsType) {
      this.suggestionsType = suggestionsType;
   }
}
