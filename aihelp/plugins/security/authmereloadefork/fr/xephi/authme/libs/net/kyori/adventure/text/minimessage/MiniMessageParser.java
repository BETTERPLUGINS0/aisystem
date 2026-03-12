package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.RootNode;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Inserting;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Modifying;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import fr.xephi.authme.libs.net.kyori.examination.string.MultiLineStringExaminer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

final class MiniMessageParser {
   final TagResolver tagResolver;

   MiniMessageParser() {
      this.tagResolver = TagResolver.standard();
   }

   MiniMessageParser(final TagResolver tagResolver) {
      this.tagResolver = tagResolver;
   }

   @NotNull
   String escapeTokens(@NotNull final ContextImpl context) {
      StringBuilder sb = new StringBuilder(context.message().length());
      this.escapeTokens(sb, context);
      return sb.toString();
   }

   void escapeTokens(final StringBuilder sb, @NotNull final ContextImpl context) {
      this.escapeTokens(sb, context.message(), context);
   }

   private void escapeTokens(final StringBuilder sb, final String richMessage, final ContextImpl context) {
      this.processTokens(sb, richMessage, context, (token, builder) -> {
         builder.append('\\').append('<');
         if (token.type() == TokenType.CLOSE_TAG) {
            builder.append('/');
         }

         List<Token> childTokens = token.childTokens();

         for(int i = 0; i < childTokens.size(); ++i) {
            if (i != 0) {
               builder.append(':');
            }

            this.escapeTokens(builder, ((Token)childTokens.get(i)).get(richMessage).toString(), context);
         }

         builder.append('>');
      });
   }

   @NotNull
   String stripTokens(@NotNull final ContextImpl context) {
      StringBuilder sb = new StringBuilder(context.message().length());
      this.processTokens(sb, context, (token, builder) -> {
      });
      return sb.toString();
   }

   private void processTokens(@NotNull final StringBuilder sb, @NotNull final ContextImpl context, final BiConsumer<Token, StringBuilder> tagHandler) {
      this.processTokens(sb, context.message(), context, tagHandler);
   }

   private void processTokens(@NotNull final StringBuilder sb, @NotNull final String richMessage, @NotNull final ContextImpl context, final BiConsumer<Token, StringBuilder> tagHandler) {
      TagResolver combinedResolver = TagResolver.resolver(this.tagResolver, context.extraTags());
      List<Token> root = TokenParser.tokenize(richMessage, true);
      Iterator var7 = root.iterator();

      while(var7.hasNext()) {
         Token token = (Token)var7.next();
         switch(token.type()) {
         case TEXT:
            sb.append(richMessage, token.startIndex(), token.endIndex());
            break;
         case OPEN_TAG:
         case CLOSE_TAG:
         case OPEN_CLOSE_TAG:
            if (token.childTokens().isEmpty()) {
               sb.append(richMessage, token.startIndex(), token.endIndex());
            } else {
               String sanitized = TokenParser.TagProvider.sanitizePlaceholderName(((Token)token.childTokens().get(0)).get(richMessage).toString());
               if (combinedResolver.has(sanitized)) {
                  tagHandler.accept(token, sb);
               } else {
                  sb.append(richMessage, token.startIndex(), token.endIndex());
               }
            }
            break;
         default:
            throw new IllegalArgumentException("Unsupported token type " + token.type());
         }
      }

   }

   @NotNull
   RootNode parseToTree(@NotNull final ContextImpl context) {
      TagResolver combinedResolver = TagResolver.resolver(this.tagResolver, context.extraTags());
      String processedMessage = (String)context.preProcessor().apply(context.message());
      Consumer<String> debug = context.debugOutput();
      if (debug != null) {
         debug.accept("Beginning parsing message ");
         debug.accept(processedMessage);
         debug.accept("\n");
      }

      TokenParser.TagProvider transformationFactory;
      if (debug != null) {
         transformationFactory = (name, args, token) -> {
            try {
               debug.accept("Attempting to match node '");
               debug.accept(name);
               debug.accept("'");
               if (token != null) {
                  debug.accept(" at column ");
                  debug.accept(String.valueOf(token.startIndex()));
               }

               debug.accept("\n");
               Tag transformation = combinedResolver.resolve(name, new ArgumentQueueImpl(context, args), context);
               if (transformation == null) {
                  debug.accept("Could not match node '");
                  debug.accept(name);
                  debug.accept("'\n");
               } else {
                  debug.accept("Successfully matched node '");
                  debug.accept(name);
                  debug.accept("' to tag ");
                  debug.accept(transformation instanceof Examinable ? ((Examinable)transformation).examinableName() : transformation.getClass().getName());
                  debug.accept("\n");
               }

               return transformation;
            } catch (ParsingException var8) {
               if (token != null && var8 instanceof ParsingExceptionImpl) {
                  ParsingExceptionImpl impl = (ParsingExceptionImpl)var8;
                  if (impl.tokens().length == 0) {
                     impl.tokens(new Token[]{token});
                  }
               }

               debug.accept("Could not match node '");
               debug.accept(name);
               debug.accept("' - ");
               debug.accept(var8.getMessage());
               debug.accept("\n");
               return null;
            }
         };
      } else {
         transformationFactory = (name, args, token) -> {
            try {
               return combinedResolver.resolve(name, new ArgumentQueueImpl(context, args), context);
            } catch (ParsingException var6) {
               return null;
            }
         };
      }

      Predicate<String> tagNameChecker = (name) -> {
         String sanitized = TokenParser.TagProvider.sanitizePlaceholderName(name);
         return combinedResolver.has(sanitized);
      };
      String preProcessed = TokenParser.resolvePreProcessTags(processedMessage, transformationFactory);
      context.message(preProcessed);
      RootNode root = TokenParser.parse(transformationFactory, tagNameChecker, preProcessed, processedMessage, context.strict());
      if (debug != null) {
         debug.accept("Text parsed into element tree:\n");
         debug.accept(root.toString());
      }

      return root;
   }

   @NotNull
   Component parseFormat(@NotNull final ContextImpl context) {
      ElementNode root = this.parseToTree(context);
      return (Component)Objects.requireNonNull((Component)context.postProcessor().apply(this.treeToComponent(root, context)), "Post-processor must not return null");
   }

   @NotNull
   Component treeToComponent(@NotNull final ElementNode node, @NotNull final ContextImpl context) {
      Component comp = Component.empty();
      Tag tag = null;
      if (node instanceof ValueNode) {
         comp = Component.text(((ValueNode)node).value());
      } else if (node instanceof TagNode) {
         TagNode tagNode = (TagNode)node;
         tag = tagNode.tag();
         if (tag instanceof Modifying) {
            Modifying modTransformation = (Modifying)tag;
            this.visitModifying(modTransformation, tagNode, 0);
            modTransformation.postVisit();
         }

         if (tag instanceof Inserting) {
            comp = ((Inserting)tag).value();
         }
      }

      if (!node.unsafeChildren().isEmpty()) {
         List<Component> children = new ArrayList(((Component)comp).children().size() + node.children().size());
         children.addAll(((Component)comp).children());
         Iterator var10 = node.unsafeChildren().iterator();

         while(var10.hasNext()) {
            ElementNode child = (ElementNode)var10.next();
            children.add(this.treeToComponent(child, context));
         }

         comp = ((Component)comp).children(children);
      }

      if (tag instanceof Modifying) {
         comp = this.handleModifying((Modifying)tag, (Component)comp, 0);
      }

      Consumer<String> debug = context.debugOutput();
      if (debug != null) {
         debug.accept("==========\ntreeToComponent \n");
         debug.accept(node.toString());
         debug.accept("\n");
         debug.accept((String)((Stream)((Component)comp).examine(MultiLineStringExaminer.simpleEscaping())).collect(Collectors.joining("\n")));
         debug.accept("\n==========\n");
      }

      return (Component)comp;
   }

   private void visitModifying(final Modifying modTransformation, final ElementNode node, final int depth) {
      modTransformation.visit(node, depth);
      Iterator var4 = node.unsafeChildren().iterator();

      while(var4.hasNext()) {
         ElementNode child = (ElementNode)var4.next();
         this.visitModifying(modTransformation, child, depth + 1);
      }

   }

   private Component handleModifying(final Modifying modTransformation, final Component current, final int depth) {
      Component newComp = modTransformation.apply(current, depth);

      Component child;
      for(Iterator var5 = current.children().iterator(); var5.hasNext(); newComp = newComp.append(this.handleModifying(modTransformation, child, depth + 1))) {
         child = (Component)var5.next();
      }

      return newComp;
   }
}
