package com.bergerkiller.bukkit.tc.commands.parsers;

import com.bergerkiller.bukkit.common.cloud.CloudLocalizedException;
import com.bergerkiller.bukkit.common.cloud.parsers.QuotedArgumentParser;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParseResult;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ParserDescriptor;
import com.bergerkiller.bukkit.tc.properties.standard.type.TrainNameFormat;
import org.bukkit.command.CommandSender;

public class TrainNameFormatParser implements QuotedArgumentParser<CommandSender, TrainNameFormat> {
   public static ParserDescriptor<CommandSender, TrainNameFormat> trainNameFormatParser() {
      return (new TrainNameFormatParser()).createDescriptor(TrainNameFormat.class);
   }

   public ArgumentParseResult<TrainNameFormat> parseQuotedString(CommandContext<CommandSender> commandContext, String inputName) {
      TrainNameFormat name = TrainNameFormat.parse(inputName);
      TrainNameFormat.VerifyResult verify = name.verify();
      return verify != TrainNameFormat.VerifyResult.OK ? ArgumentParseResult.failure(new CloudLocalizedException(commandContext, verify.getMessage(), new String[]{inputName})) : ArgumentParseResult.success(name);
   }
}
