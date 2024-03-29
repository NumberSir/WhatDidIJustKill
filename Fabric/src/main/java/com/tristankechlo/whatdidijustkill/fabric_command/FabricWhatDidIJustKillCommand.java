package com.tristankechlo.whatdidijustkill.fabric_command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.config.ConfigManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;


public class FabricWhatDidIJustKillCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> command = literal(WhatDidIJustKill.MOD_ID)
                .then(literal("config")
                        .then(literal("reload").executes(FabricWhatDidIJustKillCommand::configReload))
                        .then(literal("show").executes(FabricWhatDidIJustKillCommand::configShow))
                        .then(literal("reset").executes(FabricWhatDidIJustKillCommand::configReset)))
                .then(literal("github").executes(FabricWhatDidIJustKillCommand::github))
                .then(literal("issue").executes(FabricWhatDidIJustKillCommand::issue))
                .then(literal("wiki").executes(FabricWhatDidIJustKillCommand::wiki))
                .then(literal("discord").executes(FabricWhatDidIJustKillCommand::discord))
                .then(literal("curseforge").executes(FabricWhatDidIJustKillCommand::curseforge))
                .then(literal("modrinth").executes(FabricWhatDidIJustKillCommand::modrinth));
        dispatcher.register(command);
        WhatDidIJustKill.LOGGER.info("Command '/{}' registered", WhatDidIJustKill.MOD_ID);
    }

    private static int configReload(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        boolean success = ConfigManager.reloadConfig();
        FabricResponseHelper.sendMessageConfigReload(source, success);
        return 1;
    }

    private static int configShow(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        FabricResponseHelper.sendMessageConfigShow(source);
        return 1;
    }

    private static int configReset(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        boolean success = ConfigManager.resetConfig();
        FabricResponseHelper.sendMessageConfigReset(source, success);
        return 1;
    }

    private static int github(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        Component link = FabricResponseHelper.clickableLink(WhatDidIJustKill.GITHUB_URL);
        Component message = Component.literal("Check out the source code on GitHub: ").withStyle(ChatFormatting.WHITE).append(link);
        FabricResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int issue(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        Component link = FabricResponseHelper.clickableLink(WhatDidIJustKill.GITHUB_ISSUE_URL);
        Component message = Component.literal("If you found an issue, submit it here: ").withStyle(ChatFormatting.WHITE).append(link);
        FabricResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int wiki(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        Component link = FabricResponseHelper.clickableLink(WhatDidIJustKill.GITHUB_WIKI_URL);
        Component message = Component.literal("The wiki can be found here: ").withStyle(ChatFormatting.WHITE).append(link);
        FabricResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int discord(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        Component link = FabricResponseHelper.clickableLink(WhatDidIJustKill.DISCORD_URL);
        Component message = Component.literal("Join the Discord here: ").withStyle(ChatFormatting.WHITE).append(link);
        FabricResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int curseforge(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        Component link = FabricResponseHelper.clickableLink(WhatDidIJustKill.CURSEFORGE_URL);
        Component message = Component.literal("Check out the CurseForge page here: ").withStyle(ChatFormatting.WHITE).append(link);
        FabricResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int modrinth(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        Component link = FabricResponseHelper.clickableLink(WhatDidIJustKill.MODRINTH_URL);
        Component message = Component.literal("Check out the Modrinth page here: ").withStyle(ChatFormatting.WHITE).append(link);
        FabricResponseHelper.sendMessage(source, message, false);
        return 1;
    }

}
