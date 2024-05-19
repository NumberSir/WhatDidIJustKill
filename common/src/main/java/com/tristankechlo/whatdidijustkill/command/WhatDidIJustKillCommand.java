package com.tristankechlo.whatdidijustkill.command;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.tristankechlo.whatdidijustkill.WhatDidIJustKill;
import com.tristankechlo.whatdidijustkill.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.literal;

public class WhatDidIJustKillCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = literal(WhatDidIJustKill.MOD_ID)
                .then(literal("config")
                        .then(literal("reload").executes(WhatDidIJustKillCommand::configReload))
                        .then(literal("show").executes(WhatDidIJustKillCommand::configShow))
                        .then(literal("reset").executes(WhatDidIJustKillCommand::configReset)))
                .then(literal("github").executes(WhatDidIJustKillCommand::github))
                .then(literal("issue").executes(WhatDidIJustKillCommand::issue))
                .then(literal("wiki").executes(WhatDidIJustKillCommand::wiki))
                .then(literal("discord").executes(WhatDidIJustKillCommand::discord))
                .then(literal("curseforge").executes(WhatDidIJustKillCommand::curseforge))
                .then(literal("modrinth").executes(WhatDidIJustKillCommand::modrinth));
        dispatcher.register(command);
        WhatDidIJustKill.LOGGER.info("Command '/{}' registered", WhatDidIJustKill.MOD_ID);
    }

    private static int configReload(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        boolean success = ConfigManager.reloadConfig();
        ResponseHelper.sendMessageConfigReload(source, success);
        return 1;
    }

    private static int configShow(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ResponseHelper.sendMessageConfigShow(source);
        return 1;
    }

    private static int configReset(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        boolean success = ConfigManager.resetConfig();
        ResponseHelper.sendMessageConfigReset(source, success);
        return 1;
    }

    private static int github(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(WhatDidIJustKill.GITHUB_URL);
        Component message = Component.literal("Check out the source code on GitHub: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int issue(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(WhatDidIJustKill.GITHUB_ISSUE_URL);
        Component message = Component.literal("If you found an issue, submit it here: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int wiki(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(WhatDidIJustKill.GITHUB_WIKI_URL);
        Component message = Component.literal("The wiki can be found here: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int discord(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(WhatDidIJustKill.DISCORD_URL);
        Component message = Component.literal("Join the Discord here: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int curseforge(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(WhatDidIJustKill.CURSEFORGE_URL);
        Component message = Component.literal("Check out the CurseForge page here: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int modrinth(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(WhatDidIJustKill.MODRINTH_URL);
        Component message = Component.literal("Check out the Modrinth page here: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

}
