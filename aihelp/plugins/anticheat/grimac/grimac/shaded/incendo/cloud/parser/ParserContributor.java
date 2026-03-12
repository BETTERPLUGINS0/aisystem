package ac.grim.grimac.shaded.incendo.cloud.parser;

public interface ParserContributor {
   <C> void contribute(ParserRegistry<C> registry);
}
