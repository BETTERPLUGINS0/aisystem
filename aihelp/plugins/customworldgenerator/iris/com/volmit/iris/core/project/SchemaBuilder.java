package com.volmit.iris.core.project;

import com.volmit.iris.Iris;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.link.data.DataType;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.core.service.ExternalDataSVC;
import com.volmit.iris.engine.framework.ListFunction;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListBlockType;
import com.volmit.iris.engine.object.annotations.RegistryListEnchantment;
import com.volmit.iris.engine.object.annotations.RegistryListFont;
import com.volmit.iris.engine.object.annotations.RegistryListFunction;
import com.volmit.iris.engine.object.annotations.RegistryListItemType;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.RegistryListSpecialEntity;
import com.volmit.iris.engine.object.annotations.RegistryMapBlockState;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.reflect.KeyedType;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Function;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class SchemaBuilder {
   private static final String SYMBOL_LIMIT__N = "*";
   private static final String SYMBOL_TYPE__N = "";
   private static final JSONArray POTION_TYPES = getPotionTypes();
   private static final JSONArray ENCHANT_TYPES = getEnchantTypes();
   private static final JSONArray FONT_TYPES = new JSONArray(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
   private final KMap<String, JSONObject> definitions;
   private final Class<?> root;
   private final KList<String> warnings;
   private final IrisData data;

   public SchemaBuilder(Class<?> root, IrisData data) {
      this.data = var2;
      this.warnings = new KList();
      this.definitions = new KMap();
      this.root = var1;
   }

   private static JSONArray getPotionTypes() {
      JSONArray var0 = new JSONArray();
      PotionEffectType[] var1 = PotionEffectType.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PotionEffectType var4 = var1[var3];
         var0.put((Object)var4.getName().toUpperCase().replaceAll("\\Q \\E", "_"));
      }

      return var0;
   }

   private static JSONArray getEnchantTypes() {
      JSONArray var0 = new JSONArray();
      Enchantment[] var1 = Enchantment.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Enchantment var4 = var1[var3];
         var0.put((Object)var4.getKey().getKey());
      }

      return var0;
   }

   public JSONObject construct() {
      JSONObject var1 = new JSONObject();
      var1.put("$schema", (Object)"http://json-schema.org/draft-07/schema#");
      var1.put("$id", (Object)("https://volmit.com/iris-schema/" + this.root.getSimpleName().toLowerCase() + ".json"));
      JSONObject var2 = this.buildProperties(this.root);
      Iterator var3 = var2.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (!var1.has(var4)) {
            var1.put(var4, var2.get(var4));
         }
      }

      JSONObject var6 = new JSONObject();
      Iterator var7 = this.definitions.entrySet().iterator();

      while(var7.hasNext()) {
         Entry var5 = (Entry)var7.next();
         var6.put((String)var5.getKey(), var5.getValue());
      }

      var1.put("definitions", (Object)var6);
      var7 = this.warnings.iterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         Iris.warn(this.root.getSimpleName() + ": " + var8);
      }

      return var1;
   }

   private JSONObject buildProperties(Class<?> c) {
      JSONObject var2 = new JSONObject();
      JSONObject var3 = new JSONObject();
      String var4 = this.getDescription(var1);
      var2.put("description", (Object)var4);
      var2.put("x-intellij-html-description", (Object)var4.replace("\n", "<br>"));
      var2.put("type", (Object)this.getType(var1));
      JSONArray var5 = new JSONArray();
      JSONArray var6 = new JSONArray();

      for(Class var7 = var1.getSuperclass(); var7 != null && IrisRegistrant.class.isAssignableFrom(var7); var7 = var7.getSuperclass()) {
         this.buildProperties(var3, var5, var6, var7);
      }

      this.buildProperties(var3, var5, var6, var1);
      if (var5.length() > 0) {
         var2.put("required", (Object)var5);
      }

      if (var6.length() > 0) {
         var2.put("allOf", (Object)var6);
      }

      var2.put("properties", (Object)var3);
      return this.buildSnippet(var2, var1);
   }

   private void buildProperties(JSONObject properties, JSONArray required, JSONArray extended, Class<?> c) {
      Field[] var5 = var4.getDeclaredFields();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Field var8 = var5[var7];
         if (!Modifier.isStatic(var8.getModifiers()) && !Modifier.isFinal(var8.getModifiers()) && !Modifier.isTransient(var8.getModifiers())) {
            try {
               var8.setAccessible(true);
            } catch (InaccessibleObjectException var10) {
               continue;
            }

            JSONObject var9 = this.buildProperty(var8, var4);
            if (Boolean.TRUE == var9.remove("!top")) {
               var3.put((Object)var9);
            } else {
               if (Boolean.TRUE == var9.remove("!required")) {
                  var2.put((Object)var8.getName());
               }

               var1.put(var8.getName(), (Object)var9);
            }
         }
      }

   }

   private JSONObject buildProperty(Field k, Class<?> cl) {
      JSONObject var3 = new JSONObject();
      String var4 = this.getType(var1.getType());
      KList var5 = new KList();
      var3.put("!required", var1.isAnnotationPresent(Required.class));
      var3.put("type", (Object)var4);
      String var6 = "Unknown Type";
      byte var8 = -1;
      switch(var4.hashCode()) {
      case -1034364087:
         if (var4.equals("number")) {
            var8 = 2;
         }
         break;
      case -1023368385:
         if (var4.equals("object")) {
            var8 = 4;
         }
         break;
      case -891985903:
         if (var4.equals("string")) {
            var8 = 3;
         }
         break;
      case 64711720:
         if (var4.equals("boolean")) {
            var8 = 0;
         }
         break;
      case 93090393:
         if (var4.equals("array")) {
            var8 = 5;
         }
         break;
      case 1958052158:
         if (var4.equals("integer")) {
            var8 = 1;
         }
      }

      String var11;
      String var10001;
      String var15;
      String var10000;
      String var23;
      int var24;
      label331:
      switch(var8) {
      case 0:
         var6 = "Boolean";
         break;
      case 1:
         var6 = "Integer";
         if (var1.isAnnotationPresent(MinNumber.class)) {
            var24 = (int)((MinNumber)var1.getDeclaredAnnotation(MinNumber.class)).value();
            var3.put("minimum", var24);
            var5.add((Object)("* Minimum allowed is " + var24));
         }

         if (var1.isAnnotationPresent(MaxNumber.class)) {
            var24 = (int)((MaxNumber)var1.getDeclaredAnnotation(MaxNumber.class)).value();
            var3.put("maximum", var24);
            var5.add((Object)("* Maximum allowed is " + var24));
         }
         break;
      case 2:
         var6 = "Number";
         double var36;
         if (var1.isAnnotationPresent(MinNumber.class)) {
            var36 = ((MinNumber)var1.getDeclaredAnnotation(MinNumber.class)).value();
            var3.put("minimum", var36);
            var5.add((Object)("* Minimum allowed is " + var36));
         }

         if (var1.isAnnotationPresent(MaxNumber.class)) {
            var36 = ((MaxNumber)var1.getDeclaredAnnotation(MaxNumber.class)).value();
            var3.put("maximum", var36);
            var5.add((Object)("* Maximum allowed is " + var36));
         }
         break;
      case 3:
         var6 = "Text";
         if (var1.isAnnotationPresent(MinNumber.class)) {
            var24 = (int)((MinNumber)var1.getDeclaredAnnotation(MinNumber.class)).value();
            var3.put("minLength", var24);
            var5.add((Object)("* Minimum Length allowed is " + var24));
         }

         if (var1.isAnnotationPresent(MaxNumber.class)) {
            var24 = (int)((MaxNumber)var1.getDeclaredAnnotation(MaxNumber.class)).value();
            var3.put("maxLength", var24);
            var5.add((Object)("* Maximum Length allowed is " + var24));
         }

         JSONObject var39;
         if (var1.isAnnotationPresent(RegistryListResource.class)) {
            RegistryListResource var27 = (RegistryListResource)var1.getDeclaredAnnotation(RegistryListResource.class);
            ResourceLoader var28 = (ResourceLoader)this.data.getLoaders().get(var27.value());
            if (var28 != null) {
               var11 = "erz" + var28.getFolderName();
               if (!this.definitions.containsKey(var11)) {
                  var39 = new JSONObject();
                  var39.put("enum", (Object)(new JSONArray(var28.getPossibleKeys())));
                  this.definitions.put(var11, var39);
               }

               var6 = "Iris " + var28.getResourceTypeName();
               var3.put("$ref", (Object)("#/definitions/" + var11));
               var5.add((Object)("  Must be a valid " + var28.getFolderName() + " (use ctrl+space for auto complete!)"));
            } else {
               var10000 = String.valueOf(var27.value());
               Iris.error("Cannot find Registry Loader for type " + var10000 + " used in " + var1.getDeclaringClass().getCanonicalName() + " in field " + var1.getName());
            }
         } else {
            JSONObject var29;
            if (var1.isAnnotationPresent(RegistryListBlockType.class)) {
               var23 = "enum-block-type";
               if (!this.definitions.containsKey(var23)) {
                  var29 = new JSONObject();
                  JSONArray var30 = new JSONArray();
                  String[] var40 = this.data.getBlockLoader().getPossibleKeys();
                  int var46 = var40.length;

                  int var49;
                  for(var49 = 0; var49 < var46; ++var49) {
                     var15 = var40[var49];
                     var30.put((Object)var15);
                  }

                  var40 = B.getBlockTypes();
                  var46 = var40.length;

                  for(var49 = 0; var49 < var46; ++var49) {
                     var15 = var40[var49];
                     var30.put((Object)var15);
                  }

                  var29.put("enum", (Object)var30);
                  this.definitions.put(var23, var29);
               }

               var6 = "Block Type";
               var3.put("$ref", (Object)("#/definitions/" + var23));
               var5.add((Object)"  Must be a valid Block Type (use ctrl+space for auto complete!)");
            } else if (var1.isAnnotationPresent(RegistryListItemType.class)) {
               var23 = "enum-item-type";
               if (!this.definitions.containsKey(var23)) {
                  var29 = new JSONObject();
                  var29.put("enum", (Object)B.getItemTypes());
                  this.definitions.put(var23, var29);
               }

               var6 = "Item Type";
               var3.put("$ref", (Object)("#/definitions/" + var23));
               var5.add((Object)"  Must be a valid Item Type (use ctrl+space for auto complete!)");
            } else if (var1.isAnnotationPresent(RegistryListSpecialEntity.class)) {
               var23 = "enum-reg-specialentity";
               if (!this.definitions.containsKey(var23)) {
                  var29 = new JSONObject();
                  KList var31 = (KList)((ExternalDataSVC)Iris.service(ExternalDataSVC.class)).getAllIdentifiers(DataType.ENTITY).stream().map(Identifier::toString).collect(KList.collector());
                  var29.put("enum", (Object)var31.toJSONStringArray());
                  this.definitions.put(var23, var29);
               }

               var6 = "Custom Mob Type";
               var3.put("$ref", (Object)("#/definitions/" + var23));
               var5.add((Object)"  Must be a valid Custom Mob Type (use ctrl+space for auto complete!)");
            } else if (var1.isAnnotationPresent(RegistryListFont.class)) {
               var23 = "enum-font";
               if (!this.definitions.containsKey(var23)) {
                  var29 = new JSONObject();
                  var29.put("enum", (Object)FONT_TYPES);
                  this.definitions.put(var23, var29);
               }

               var6 = "Font Family";
               var3.put("$ref", (Object)("#/definitions/" + var23));
               var5.add((Object)"  Must be a valid Font Family (use ctrl+space for auto complete!)");
            } else if (var1.isAnnotationPresent(RegistryListEnchantment.class)) {
               var23 = "enum-enchantment";
               if (!this.definitions.containsKey(var23)) {
                  var29 = new JSONObject();
                  var29.put("enum", (Object)ENCHANT_TYPES);
                  this.definitions.put(var23, var29);
               }

               var6 = "Enchantment Type";
               var3.put("$ref", (Object)("#/definitions/" + var23));
               var5.add((Object)"  Must be a valid Enchantment Type (use ctrl+space for auto complete!)");
            } else if (var1.isAnnotationPresent(RegistryListFunction.class)) {
               Class var33 = ((RegistryListFunction)var1.getDeclaredAnnotation(RegistryListFunction.class)).value();

               try {
                  ListFunction var34 = (ListFunction)var33.getDeclaredConstructor().newInstance();
                  var11 = var34.key();
                  var6 = var34.fancyName();
                  if (!this.definitions.containsKey(var11)) {
                     var39 = new JSONObject();
                     var39.put("enum", var34.apply(this.data));
                     this.definitions.put(var11, var39);
                  }

                  var3.put("$ref", (Object)("#/definitions/" + var11));
                  var5.add((Object)("  Must be a valid " + var6 + " (use ctrl+space for auto complete!)"));
               } catch (Throwable var21) {
                  Iris.error("Could not execute apply method in " + var33.getName());
               }
            } else if (var1.getType().equals(PotionEffectType.class)) {
               var23 = "enum-potion-effect-type";
               if (!this.definitions.containsKey(var23)) {
                  var29 = new JSONObject();
                  var29.put("enum", (Object)POTION_TYPES);
                  this.definitions.put(var23, var29);
               }

               var6 = "Potion Effect Type";
               var3.put("$ref", (Object)("#/definitions/" + var23));
               var5.add((Object)"  Must be a valid Potion Effect Type (use ctrl+space for auto complete!)");
            } else if (KeyedType.isKeyed(var1.getType())) {
               var6 = this.addEnum(var1.getType(), var3, var5, KeyedType.values(var1.getType()), Function.identity());
            } else if (var1.getType().isEnum()) {
               var6 = this.addEnum(var1.getType(), var3, var5, var1.getType().getEnumConstants(), (var0) -> {
                  return ((Enum)var0).name();
               });
            }
         }
         break;
      case 4:
         if (var1.isAnnotationPresent(RegistryMapBlockState.class)) {
            var23 = ((RegistryMapBlockState)var1.getDeclaredAnnotation(RegistryMapBlockState.class)).value();
            var6 = "Block State";
            var3.put("!top", true);
            JSONArray var25 = new JSONArray();
            var3.put("anyOf", (Object)var25);
            B.getBlockStates().forEach((var4x, var5x) -> {
               if (!var4x.isEmpty()) {
                  String var6 = ((String)var4x.getFirst()).replace(':', '_');
                  String var7 = "enum-block-state-" + var6;
                  String var8 = "obj-block-state-" + var6;
                  var25.put((Object)(new JSONObject()).put("if", (Object)(new JSONObject()).put("properties", (Object)(new JSONObject()).put(var23, (Object)(new JSONObject()).put("type", (Object)"string").put("$ref", (Object)("#/definitions/" + var7))))).put("then", (Object)(new JSONObject()).put("properties", (Object)(new JSONObject()).put(var1.getName(), (Object)(new JSONObject()).put("type", (Object)"object").put("$ref", (Object)("#/definitions/" + var8))))).put("else", false));
                  if (!this.definitions.containsKey(var7)) {
                     JSONArray var9 = new JSONArray();
                     Objects.requireNonNull(var9);
                     var4x.forEach(var9::put);
                     this.definitions.put(var7, (new JSONObject()).put("type", (Object)"string").put("enum", (Object)var9));
                  }

                  if (!this.definitions.containsKey(var8)) {
                     JSONObject var10 = new JSONObject();
                     var5x.forEach((var1x) -> {
                        var10.put(var1x.name(), (Object)var1x.buildJson());
                     });
                     this.definitions.put(var8, (new JSONObject()).put("type", (Object)"object").put("properties", (Object)var10));
                  }

               }
            });
         } else {
            var10000 = var1.getType().getSimpleName();
            var6 = var10000.replaceAll("\\QIris\\E", "") + " (Object)";
            var10000 = var1.getType().getCanonicalName();
            var23 = "obj-" + var10000.replaceAll("\\Q.\\E", "-").toLowerCase();
            if (!this.definitions.containsKey(var23)) {
               this.definitions.put(var23, new JSONObject());
               this.definitions.put(var23, this.buildProperties(var1.getType()));
            }

            var3.put("$ref", (Object)("#/definitions/" + var23));
         }
         break;
      case 5:
         var6 = "List of Something...?";
         ArrayType var9 = (ArrayType)var1.getDeclaredAnnotation(ArrayType.class);
         if (var9 != null) {
            if (var9.min() > 0) {
               var3.put("minItems", var9.min());
               if (var9.min() == 1) {
                  var5.add((Object)"* At least one entry must be defined, or just remove this list.");
               } else {
                  var5.add((Object)("* Requires at least " + var9.min() + " entries."));
               }
            }

            String var10 = this.getType(var9.type());
            byte var12 = -1;
            switch(var10.hashCode()) {
            case -1034364087:
               if (var10.equals("number")) {
                  var12 = 1;
               }
               break;
            case -1023368385:
               if (var10.equals("object")) {
                  var12 = 2;
               }
               break;
            case -891985903:
               if (var10.equals("string")) {
                  var12 = 3;
               }
               break;
            case 1958052158:
               if (var10.equals("integer")) {
                  var12 = 0;
               }
            }

            String var38;
            JSONObject var41;
            switch(var12) {
            case 0:
               var6 = "List of Integers";
               break label331;
            case 1:
               var6 = "List of Numbers";
               break label331;
            case 2:
               var10000 = var9.type().getSimpleName();
               var6 = "List of " + var10000.replaceAll("\\QIris\\E", "") + "s (Objects)";
               var10000 = var9.type().getCanonicalName();
               var38 = "obj-" + var10000.replaceAll("\\Q.\\E", "-").toLowerCase();
               if (!this.definitions.containsKey(var38)) {
                  this.definitions.put(var38, new JSONObject());
                  this.definitions.put(var38, this.buildProperties(var9.type()));
               }

               var41 = new JSONObject();
               var41.put("$ref", (Object)("#/definitions/" + var38));
               var3.put("items", (Object)var41);
               break label331;
            case 3:
               var6 = "List of Text";
               JSONObject var16;
               if (var1.isAnnotationPresent(RegistryListResource.class)) {
                  RegistryListResource var13 = (RegistryListResource)var1.getDeclaredAnnotation(RegistryListResource.class);
                  ResourceLoader var14 = (ResourceLoader)this.data.getLoaders().get(var13.value());
                  if (var14 != null) {
                     var6 = "List<" + var14.getResourceTypeName() + ">";
                     var15 = "erz" + var14.getFolderName();
                     if (!this.definitions.containsKey(var15)) {
                        var16 = new JSONObject();
                        var16.put("enum", (Object)(new JSONArray(var14.getPossibleKeys())));
                        this.definitions.put(var15, var16);
                     }

                     var16 = new JSONObject();
                     var16.put("$ref", (Object)("#/definitions/" + var15));
                     var3.put("items", (Object)var16);
                     var5.add((Object)("  Must be a valid " + var14.getResourceTypeName() + " (use ctrl+space for auto complete!)"));
                  } else {
                     var10000 = String.valueOf(var13.value());
                     Iris.error("Cannot find Registry Loader for type (list schema) " + var10000 + " used in " + var1.getDeclaringClass().getCanonicalName() + " in field " + var1.getName());
                  }
               } else if (var1.isAnnotationPresent(RegistryListBlockType.class)) {
                  var6 = "List of Block Types";
                  var38 = "enum-block-type";
                  if (!this.definitions.containsKey(var38)) {
                     var41 = new JSONObject();
                     JSONArray var45 = new JSONArray();
                     String[] var47 = this.data.getBlockLoader().getPossibleKeys();
                     int var17 = var47.length;

                     int var18;
                     String var19;
                     for(var18 = 0; var18 < var17; ++var18) {
                        var19 = var47[var18];
                        var45.put((Object)var19);
                     }

                     var47 = B.getBlockTypes();
                     var17 = var47.length;

                     for(var18 = 0; var18 < var17; ++var18) {
                        var19 = var47[var18];
                        var45.put((Object)var19);
                     }

                     var41.put("enum", (Object)var45);
                     this.definitions.put(var38, var41);
                  }

                  var41 = new JSONObject();
                  var41.put("$ref", (Object)("#/definitions/" + var38));
                  var3.put("items", (Object)var41);
                  var5.add((Object)"  Must be a valid Block Type (use ctrl+space for auto complete!)");
               } else if (var1.isAnnotationPresent(RegistryListItemType.class)) {
                  var6 = "List of Item Types";
                  var38 = "enum-item-type";
                  if (!this.definitions.containsKey(var38)) {
                     var41 = new JSONObject();
                     var41.put("enum", (Object)B.getItemTypes());
                     this.definitions.put(var38, var41);
                  }

                  var41 = new JSONObject();
                  var41.put("$ref", (Object)("#/definitions/" + var38));
                  var3.put("items", (Object)var41);
                  var5.add((Object)"  Must be a valid Item Type (use ctrl+space for auto complete!)");
               } else if (var1.isAnnotationPresent(RegistryListFont.class)) {
                  var38 = "enum-font";
                  var6 = "List of Font Families";
                  if (!this.definitions.containsKey(var38)) {
                     var41 = new JSONObject();
                     var41.put("enum", (Object)FONT_TYPES);
                     this.definitions.put(var38, var41);
                  }

                  var41 = new JSONObject();
                  var41.put("$ref", (Object)("#/definitions/" + var38));
                  var3.put("items", (Object)var41);
                  var5.add((Object)"  Must be a valid Font Family (use ctrl+space for auto complete!)");
               } else if (var1.isAnnotationPresent(RegistryListEnchantment.class)) {
                  var6 = "List of Enchantment Types";
                  var38 = "enum-enchantment";
                  if (!this.definitions.containsKey(var38)) {
                     var41 = new JSONObject();
                     var41.put("enum", (Object)ENCHANT_TYPES);
                     this.definitions.put(var38, var41);
                  }

                  var41 = new JSONObject();
                  var41.put("$ref", (Object)("#/definitions/" + var38));
                  var3.put("items", (Object)var41);
                  var5.add((Object)"  Must be a valid Enchantment Type (use ctrl+space for auto complete!)");
               } else if (var1.isAnnotationPresent(RegistryListFunction.class)) {
                  Class var43 = ((RegistryListFunction)var1.getDeclaredAnnotation(RegistryListFunction.class)).value();

                  try {
                     ListFunction var48 = (ListFunction)var43.getDeclaredConstructor().newInstance();
                     var15 = var48.key();
                     var6 = var48.fancyName();
                     if (!this.definitions.containsKey(var15)) {
                        var16 = new JSONObject();
                        var16.put("enum", var48.apply(this.data));
                        this.definitions.put(var15, var16);
                     }

                     var16 = new JSONObject();
                     var16.put("$ref", (Object)("#/definitions/" + var15));
                     var3.put("items", (Object)var16);
                     var5.add((Object)("  Must be a valid " + var6 + " (use ctrl+space for auto complete!)"));
                  } catch (Throwable var20) {
                     Iris.error("Could not execute apply method in " + var43.getName());
                  }
               } else if (var9.type().equals(PotionEffectType.class)) {
                  var6 = "List of Potion Effect Types";
                  var38 = "enum-potion-effect-type";
                  if (!this.definitions.containsKey(var38)) {
                     var41 = new JSONObject();
                     var41.put("enum", (Object)POTION_TYPES);
                     this.definitions.put(var38, var41);
                  }

                  var41 = new JSONObject();
                  var41.put("$ref", (Object)("#/definitions/" + var38));
                  var3.put("items", (Object)var41);
                  var5.add((Object)"  Must be a valid Potion Effect Type (use ctrl+space for auto complete!)");
               } else if (KeyedType.isKeyed(var9.type())) {
                  var6 = this.addEnumList(var3, var5, var9, KeyedType.values(var9.type()), Function.identity());
               } else if (var9.type().isEnum()) {
                  var6 = this.addEnumList(var3, var5, var9, var9.type().getEnumConstants(), (var0) -> {
                     return ((Enum)var0).name();
                  });
               }
            }
         } else {
            KList var50 = this.warnings;
            var10001 = var1.getName();
            var50.add((Object)("Undefined array type for field " + var10001 + " (" + var1.getType().getSimpleName() + ") in class " + var2.getSimpleName()));
         }
         break;
      default:
         this.warnings.add((Object)("Unexpected Schema Type: " + var4 + " for field " + var1.getName() + " (" + var1.getType().getSimpleName() + ") in class " + var2.getSimpleName()));
      }

      KList var7 = new KList();
      var10001 = var1.getName();
      var7.add((Object)("<h>" + var10001 + "</h>"));
      var10001 = this.getFieldDescription(var1);
      var7.add((Object)(var10001 + "<hr></hr>"));
      var7.add((Object)("<h>" + var6 + "</h>"));
      String var26 = this.getDescription(var1.getType());
      boolean var42 = !var26.isBlank();
      if (var42) {
         var7.add((Object)var26);
      }

      Snippet var37 = (Snippet)var1.getType().getDeclaredAnnotation(Snippet.class);
      if (var37 == null) {
         ArrayType var32 = (ArrayType)var1.getType().getDeclaredAnnotation(ArrayType.class);
         if (var32 != null) {
            var37 = (Snippet)var32.type().getDeclaredAnnotation(Snippet.class);
         }
      }

      if (var37 != null) {
         var11 = var37.value();
         if (var42) {
            var7.add((Object)"    ");
         }

         var7.add((Object)("You can instead specify \"snippet/" + var11 + "/some-name.json\" to use a snippet file instead of specifying it here."));
         var42 = false;
      }

      try {
         var1.setAccessible(true);
         Object var35 = var1.get(var2.newInstance());
         if (var35 != null) {
            if (var42) {
               var7.add((Object)"    ");
            }

            if (var35 instanceof List) {
               var7.add((Object)"* Default Value is an empty list");
            } else if (!var1.getType().isPrimitive() && !(var35 instanceof Number) && !(var35 instanceof String) && !(var35 instanceof Enum) && !KeyedType.isKeyed(var1.getType())) {
               var7.add((Object)"* Default Value is a default object (create this object to see default properties)");
            } else {
               var7.add((Object)("* Default Value is " + String.valueOf(var35)));
            }
         }
      } catch (Throwable var22) {
      }

      var5.forEach((var1x) -> {
         var7.add((Object)var1x.trim());
      });
      var11 = var7.toString("\n").replace("<hr></hr>", "\n").replace("<h>", "").replace("</h>", "");
      String var44 = var7.toString("<br>");
      var3.put("type", (Object)var4);
      var3.put("description", (Object)var11);
      var3.put("x-intellij-html-description", (Object)var44);
      return this.buildSnippet(var3, var1.getType());
   }

   private JSONObject buildSnippet(JSONObject prop, Class<?> type) {
      Snippet var3 = (Snippet)var2.getDeclaredAnnotation(Snippet.class);
      if (var3 == null) {
         return var1;
      } else {
         JSONObject var4 = new JSONObject();
         JSONArray var5 = new JSONArray();
         JSONObject var6 = new JSONObject();
         var6.put("type", (Object)"string");
         String var7 = "enum-snippet-" + var3.value();
         var6.put("$ref", (Object)("#/definitions/" + var7));
         if (!this.definitions.containsKey(var7)) {
            JSONObject var8 = new JSONObject();
            JSONArray var9 = new JSONArray();
            KList var10000 = this.data.getPossibleSnippets(var3.value());
            Objects.requireNonNull(var9);
            var10000.forEach(var9::put);
            var8.put("enum", (Object)var9);
            this.definitions.put(var7, var8);
         }

         var5.put((Object)var1);
         var5.put((Object)var6);
         var6.put("description", (Object)var1.getString("description"));
         var6.put("x-intellij-html-description", (Object)var1.getString("x-intellij-html-description"));
         var4.put("anyOf", (Object)var5);
         var4.put("description", (Object)var1.getString("description"));
         var4.put("x-intellij-html-description", (Object)var1.getString("x-intellij-html-description"));
         var4.put("!required", var2.isAnnotationPresent(Required.class));
         return var4;
      }
   }

   @NotNull
   private <T> String addEnumList(JSONObject prop, KList<String> description, ArrayType t, T[] values, Function<T, String> function) {
      JSONObject var6 = new JSONObject();
      String var7 = this.addEnum(var3.type(), var6, var2, var4, var5);
      var1.put("items", (Object)var6);
      return "List of " + var7 + "s";
   }

   @NotNull
   private <T> String addEnum(Class<?> type, JSONObject prop, KList<String> description, T[] values, Function<T, String> function) {
      JSONArray var6 = new JSONArray();
      boolean var7 = var1.isAnnotationPresent(Desc.class);
      Object[] var8 = var4;
      int var9 = var4.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         Object var11 = var8[var10];
         if (var7) {
            try {
               JSONObject var12 = new JSONObject();
               String var13 = (String)var5.apply(var11);
               var12.put("const", (Object)var13);
               Desc var14 = (Desc)var1.getField(var13).getAnnotation(Desc.class);
               String var15 = var14 == null ? "No Description for " + var13 : var14.value();
               var12.put("description", (Object)var15);
               var12.put("x-intellij-html-description", (Object)var15.replace("\n", "<br>"));
               var6.put((Object)var12);
            } catch (Throwable var16) {
               Iris.reportError(var16);
               var16.printStackTrace();
            }
         } else {
            var6.put(var5.apply(var11));
         }
      }

      String var17 = (var7 ? "oneof-" : "") + "enum-" + var1.getCanonicalName().replaceAll("\\Q.\\E", "-").toLowerCase();
      if (!this.definitions.containsKey(var17)) {
         JSONObject var18 = new JSONObject();
         var18.put(var7 ? "oneOf" : "enum", (Object)var6);
         this.definitions.put(var17, var18);
      }

      var2.put("$ref", (Object)("#/definitions/" + var17));
      String var10001 = var1.getSimpleName();
      var3.add((Object)("  Must be a valid " + var10001.replaceAll("\\QIris\\E", "") + " (use ctrl+space for auto complete!)"));
      return var1.getSimpleName().replaceAll("\\QIris\\E", "");
   }

   private String getType(Class<?> c) {
      if (!var1.equals(Integer.TYPE) && !var1.equals(Integer.class) && !var1.equals(Long.TYPE) && !var1.equals(Long.class)) {
         if (!var1.equals(Float.TYPE) && !var1.equals(Double.TYPE) && !var1.equals(Float.class) && !var1.equals(Double.class)) {
            if (!var1.equals(Boolean.TYPE) && !var1.equals(Boolean.class)) {
               if (!var1.equals(String.class) && !var1.isEnum() && !KeyedType.isKeyed(var1)) {
                  if (var1.equals(KList.class)) {
                     return "array";
                  } else if (var1.equals(KMap.class)) {
                     return "object";
                  } else {
                     if (!var1.isAnnotationPresent(Desc.class) && var1.getCanonicalName().startsWith("com.volmit.iris.")) {
                        this.warnings.addIfMissing("Unsupported Type: " + var1.getCanonicalName() + " Did you forget @Desc?");
                     }

                     return "object";
                  }
               } else {
                  return "string";
               }
            } else {
               return "boolean";
            }
         } else {
            return "number";
         }
      } else {
         return "integer";
      }
   }

   private String getFieldDescription(Field r) {
      if (var1.isAnnotationPresent(Desc.class)) {
         return ((Desc)var1.getDeclaredAnnotation(Desc.class)).value();
      } else if (var1.getDeclaringClass().getName().startsWith("org.bukkit.")) {
         return "Bukkit package classes and enums have no descriptions";
      } else {
         KList var10000 = this.warnings;
         String var10001 = var1.getName();
         var10000.addIfMissing("Missing @Desc on field " + var10001 + " (" + String.valueOf(var1.getType()) + ") in " + var1.getDeclaringClass().getCanonicalName());
         return "No Field Description";
      }
   }

   private String getDescription(Class<?> r) {
      if (var1.isAnnotationPresent(Desc.class)) {
         return ((Desc)var1.getDeclaredAnnotation(Desc.class)).value();
      } else {
         if (!var1.isPrimitive() && !var1.equals(KList.class) && !var1.equals(KMap.class) && var1.getCanonicalName().startsWith("com.volmit.")) {
            KList var10000 = this.warnings;
            String var10001 = var1.getSimpleName();
            var10000.addIfMissing("Missing @Desc on " + var10001 + " in " + (var1.getDeclaringClass() != null ? var1.getDeclaringClass().getCanonicalName() : " NOSRC"));
         }

         return "";
      }
   }
}
