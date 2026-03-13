/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.effects.effects.conditions;

import java.util.Locale;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.advancedplugins.as.impl.effects.effects.actions.ActionExecution;
import net.advancedplugins.as.impl.effects.effects.conditions.ConResult;
import net.advancedplugins.as.impl.effects.effects.conditions.ConditionType;
import net.advancedplugins.as.impl.effects.effects.variables.Variables;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.ColorUtils;
import net.advancedplugins.as.impl.utils.MathUtils;
import net.advancedplugins.as.impl.utils.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Fractor {
    private static ConditionType getOriginalCondition(String string) {
        if (string.contains("%stop%")) {
            return ConditionType.STOP;
        }
        if (string.contains("%force%")) {
            return ConditionType.FORCE;
        }
        if (string.contains("%continue%")) {
            return ConditionType.CONTINUE;
        }
        if (string.contains("%allow%")) {
            return ConditionType.ALLOW;
        }
        if (string.contains("-")) {
            if (string.contains("%chance%")) {
                return ConditionType.REMOVE;
            }
        } else if (string.contains("+") && string.contains("%chance%")) {
            string = string.replaceAll(" ", "");
            string = string.replace("%chance%", "");
            if (!StringUtils.isNumeric(string = string.replace("+", ""))) {
                return ConditionType.NONE;
            }
            return ConditionType.ADD;
        }
        return ConditionType.NONE;
    }

    public static ConResult getResult(String string, LivingEntity livingEntity, LivingEntity livingEntity2, ActionExecution actionExecution) {
        if (string.startsWith("[")) {
            string = string.substring(1);
        }
        if (string.endsWith("]")) {
            string = string.substring(0, string.length() - 1);
        }
        try {
            string = string.toLowerCase(Locale.ROOT);
            Fractor.debug("------------------------------------");
            if (livingEntity != null) {
                Fractor.debug("Attacker: " + String.valueOf(livingEntity.getType()));
            }
            Fractor.debug("Victim: " + String.valueOf(livingEntity2 == null ? "N/A" : livingEntity2.getType()));
            Fractor.debug("Condition in: \"" + string + "\"");
            ConResult conResult = Fractor.parseCondition(string, livingEntity, livingEntity2, actionExecution);
            if (conResult != null) {
                Fractor.debug("Condition result: " + String.valueOf((Object)conResult.getCondition()));
            }
            Fractor.debug("------------------------------------");
            return conResult;
        } catch (Exception exception) {
            ASManager.reportIssue(exception, "Invalid condition: " + string);
            return new ConResult(ConditionType.STOP, "", ConditionType.STOP);
        }
    }

    private static ConResult parseCondition(String string, LivingEntity livingEntity, LivingEntity livingEntity2, ActionExecution actionExecution) {
        if (livingEntity == null && livingEntity2 == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Invalid entity for condition: " + string);
            return null;
        }
        if (livingEntity2 == null) {
            livingEntity2 = livingEntity;
        } else if (livingEntity == null) {
            livingEntity = livingEntity2;
        }
        string = Variables.replaceVariables(string, livingEntity, livingEntity2, actionExecution);
        Fractor.debug("Condition out: \"" + string + "\"");
        String[] stringArray = string.split(" : ");
        if (stringArray.length < 2) {
            Bukkit.getLogger().warning("Invalid condition: " + string + ", enchant was stopped from parsing");
            if (livingEntity instanceof Player && livingEntity.isOp()) {
                livingEntity.sendMessage(Text.modify("&cInvalid condition: " + string + ", enchant was stopped from parsing"));
            }
            return new ConResult(ConditionType.STOP, 0, Fractor.getOriginalCondition(string));
        }
        String string2 = stringArray[0].trim();
        String string3 = stringArray[1].trim();
        boolean bl = Fractor.evaluateExpression(string2, livingEntity);
        if (bl) {
            return Fractor.getEnding(string3, livingEntity);
        }
        return new ConResult(ConditionType.NONE, null, Fractor.getOriginalCondition(string));
    }

    private static boolean evaluateExpression(String string, LivingEntity livingEntity) {
        Stack<Boolean> stack = new Stack<Boolean>();
        Stack<Character> stack2 = new Stack<Character>();
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = string.contains("matchesregex");
        if (!bl) {
            string = string.replaceAll(" ", "");
        }
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            Character c2 = Fractor.peekChar(string, i + 1);
            if (c == '(' && !bl) {
                int n = Fractor.findClosingParenthesis(string, i);
                stack.push(Fractor.evaluateExpression(string.substring(i + 1, n), livingEntity));
                i = n;
                continue;
            }
            if ((c == '&' || c == '|') && Objects.equals(Character.valueOf(c), c2)) {
                if (!stringBuilder.isEmpty()) {
                    stack.push(Fractor.read(stringBuilder.toString().trim(), livingEntity));
                    stringBuilder.setLength(0);
                }
                while (!stack2.isEmpty() && Fractor.precedence(((Character)stack2.peek()).charValue()) >= Fractor.precedence(c)) {
                    stack.push(Fractor.applyOperator(((Character)stack2.pop()).charValue(), (Boolean)stack.pop(), (Boolean)stack.pop()));
                }
                stack2.push(Character.valueOf(c));
                if (c == '&') {
                    ++i;
                }
                if (c != '|') continue;
                ++i;
                continue;
            }
            stringBuilder.append(c);
        }
        if (!stringBuilder.isEmpty()) {
            stack.push(Fractor.read(stringBuilder.toString().trim(), livingEntity));
        }
        while (!stack2.isEmpty()) {
            stack.push(Fractor.applyOperator(((Character)stack2.pop()).charValue(), (Boolean)stack.pop(), (Boolean)stack.pop()));
        }
        return (Boolean)stack.pop();
    }

    private static Character peekChar(String string, int n) {
        if (n >= string.length()) {
            return null;
        }
        return Character.valueOf(string.charAt(n));
    }

    private static int findClosingParenthesis(String string, int n) {
        int n2 = 0;
        for (int i = n; i < string.length(); ++i) {
            if (string.charAt(i) == '(') {
                ++n2;
            }
            if (string.charAt(i) == ')') {
                --n2;
            }
            if (n2 != 0) continue;
            return i;
        }
        throw new IllegalArgumentException("Mismatched parentheses in expression: " + string);
    }

    private static int precedence(char c) {
        return switch (c) {
            case '&' -> 2;
            case '|' -> 1;
            default -> 0;
        };
    }

    private static boolean applyOperator(char c, boolean bl, boolean bl2) {
        return switch (c) {
            case '&' -> {
                if (bl2 && bl) {
                    yield true;
                }
                yield false;
            }
            case '|' -> {
                if (bl2 || bl) {
                    yield true;
                }
                yield false;
            }
            default -> throw new IllegalArgumentException("Invalid operator: " + c);
        };
    }

    private static ConResult getEnding(String string, LivingEntity livingEntity) {
        ConResult conResult = null;
        if (string.contains("%stop%")) {
            conResult = new ConResult(ConditionType.STOP, 0, Fractor.getOriginalCondition(string));
        } else if (string.contains("%force%")) {
            conResult = new ConResult(ConditionType.FORCE, 0, Fractor.getOriginalCondition(string));
        } else if (string.contains("%continue%")) {
            conResult = new ConResult(ConditionType.CONTINUE, 0, Fractor.getOriginalCondition(string));
        } else if (string.contains("%allow%")) {
            conResult = new ConResult(ConditionType.ALLOW, 0, Fractor.getOriginalCondition(string));
        } else if (string.contains("-")) {
            if (string.contains("%chance%")) {
                string = string.replaceAll(" ", "");
                string = string.replace("%chance%", "");
                if (!StringUtils.isNumeric(string = string.replace("-", ""))) {
                    if (livingEntity instanceof Player && livingEntity.isOp()) {
                        livingEntity.sendMessage(ColorUtils.format("&4Condition (" + string + ") contains letters, but must be numeric only. Check if this condition fits this enchant type if you see no visible error"));
                    }
                    return new ConResult(ConditionType.NONE, null, Fractor.getOriginalCondition(string));
                }
                conResult = new ConResult(ConditionType.REMOVE, ASManager.parseInt(string.split("-")[1].replace("+", "")), Fractor.getOriginalCondition(string));
            }
        } else if (string.contains("+") && string.contains("%chance%")) {
            string = string.replaceAll(" ", "");
            string = string.replace("%chance%", "");
            if (!StringUtils.isNumeric(string = string.replace("+", ""))) {
                if (livingEntity instanceof Player && livingEntity.isOp()) {
                    livingEntity.sendMessage(ColorUtils.format("&4Condition (" + string + ") contains letters, but must be numeric only."));
                }
                return new ConResult(ConditionType.NONE, null, Fractor.getOriginalCondition(string));
            }
            conResult = new ConResult(ConditionType.ADD, ASManager.parseInt(string), Fractor.getOriginalCondition(string));
        }
        return conResult;
    }

    private static boolean read(String string, LivingEntity livingEntity) {
        boolean bl;
        block15: {
            block18: {
                String[] stringArray;
                block19: {
                    block17: {
                        block16: {
                            block14: {
                                bl = false;
                                if (!string.contains(">=")) break block14;
                                String[] stringArray2 = string.replaceAll(" ", "").split(">=");
                                if (!MathUtils.isDouble(stringArray2[0]) || !MathUtils.isDouble(stringArray2[1])) {
                                    if (livingEntity instanceof Player && livingEntity.isOp()) {
                                        livingEntity.sendMessage(ColorUtils.format("&4Condition (" + string + ") contains letters, but must be numeric only."));
                                    }
                                    return false;
                                }
                                bl = Double.parseDouble(stringArray2[0]) >= Double.parseDouble(stringArray2[1]);
                                break block15;
                            }
                            if (!string.contains("<=")) break block16;
                            String[] stringArray3 = string.replaceAll(" ", "").split("<=");
                            if (!MathUtils.isDouble(stringArray3[0]) || !MathUtils.isDouble(stringArray3[1])) {
                                if (livingEntity instanceof Player && livingEntity.isOp()) {
                                    livingEntity.sendMessage(ColorUtils.format("&4Condition (" + string + ") contains letters, but must be numeric only."));
                                }
                                return false;
                            }
                            bl = Double.parseDouble(stringArray3[0]) <= Double.parseDouble(stringArray3[1]);
                            break block15;
                        }
                        if (!string.contains(">")) break block17;
                        String[] stringArray4 = string.replaceAll(" ", "").split(">");
                        if (!MathUtils.isDouble(stringArray4[0]) || !MathUtils.isDouble(stringArray4[1])) {
                            if (livingEntity instanceof Player && livingEntity.isOp()) {
                                livingEntity.sendMessage(ColorUtils.format("&4Condition (" + string + ") contains letters, but must be numeric only."));
                            }
                            return false;
                        }
                        bl = Double.parseDouble(stringArray4[0]) > Double.parseDouble(stringArray4[1]);
                        break block15;
                    }
                    if (!string.contains("<")) break block18;
                    stringArray = string.replaceAll(" ", "").split("<");
                    if (MathUtils.isDouble(stringArray[0]) && MathUtils.isDouble(stringArray[1])) break block19;
                    if (!(livingEntity instanceof Player) || !livingEntity.isOp()) break block15;
                    ColorUtils.format("&4Condition (" + string + ") contains letters, but must be numeric only.");
                    break block15;
                }
                bl = Double.parseDouble(stringArray[0]) < Double.parseDouble(stringArray[1]);
                break block15;
            }
            if (string.contains("!=")) {
                String[] stringArray = string.replaceAll(" ", "").split("!=");
                bl = !MathUtils.isDouble(stringArray[0]) || !MathUtils.isDouble(stringArray[1]) ? !stringArray[0].equalsIgnoreCase(stringArray[1]) : Double.parseDouble(stringArray[0]) != Double.parseDouble(stringArray[1]);
            } else if (string.contains("=")) {
                String[] stringArray = string.replaceAll(" ", "").split("=");
                bl = !MathUtils.isDouble(stringArray[0]) || !MathUtils.isDouble(stringArray[1]) ? stringArray[0].equalsIgnoreCase(stringArray[1]) : Double.parseDouble(stringArray[0]) == Double.parseDouble(stringArray[1]);
            } else if (string.contains("matchesregex")) {
                String[] stringArray = string.split("matchesregex");
                Pattern pattern = Pattern.compile(stringArray[1].trim(), 2);
                Matcher matcher = pattern.matcher(stringArray[0]);
                bl = matcher.find();
            } else if (string.contains("contains")) {
                String[] stringArray;
                String[] stringArray5 = string.replaceAll(" ", "").toLowerCase(Locale.ROOT).split("contains");
                for (String string2 : stringArray = stringArray5[1].split("\\|")) {
                    if (!stringArray5[0].contains(string2)) continue;
                    bl = true;
                    break;
                }
            }
        }
        return bl;
    }

    private static void debug(String string) {
        ASManager.debug(string);
    }
}

