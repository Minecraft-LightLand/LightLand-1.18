package dev.hikarishima.lightland.content.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.profession.Profession;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.network.packets.CapToClient;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class MainCommand extends BaseCommand {

    public MainCommand(LiteralArgumentBuilder<CommandSourceStack> lightland) {
        super(lightland, "main");
    }

    public void register() {
        registerCommand("sync", getPlayer()
                .executes(withPlayer((context, e) -> {
                    LLPlayerData handler = LLPlayerData.get(e);
                    new CapToClient(CapToClient.Action.ALL, handler).toClientPlayer(e);
                    send(context, LangData.IDS.ACTION_SUCCESS.get());
                    return 1;
                })));

        registerCommand("debug_sync", getPlayer()
                .executes(withPlayer((context, e) -> {
                    LLPlayerData handler = LLPlayerData.get(e);
                    new CapToClient(CapToClient.Action.DEBUG, handler).toClientPlayer(e);
                    send(context, LangData.IDS.ACTION_SUCCESS.get());
                    return 1;
                })));

        registerCommand("set_profession", getPlayer()
                .then(Commands.argument("profession", RegistryParser.PROFESSION)
                        .executes(withPlayer((context, e) -> {
                            LLPlayerData handler = LLPlayerData.get(e);
                            Profession prof = context.getArgument("profession", Profession.class);
                            if (handler.abilityPoints.setProfession(prof)) {
                                new CapToClient(CapToClient.Action.ALL, handler).toClientPlayer(e);
                                send(context, LangData.IDS.ACTION_SUCCESS.get());
                            } else {
                                send(context, LangData.IDS.PROF_EXIST.get());
                            }
                            return 1;
                        }))));

        registerCommand("reset", getPlayer()
                .then(Commands.argument("type", EnumParser.getParser(LLPlayerData.Reset.class))
                        .executes(withPlayer((context, e) -> {
                            LLPlayerData handler = LLPlayerData.get(e);
                            LLPlayerData.Reset r = context.getArgument("type", LLPlayerData.Reset.class);
                            handler.reset(r);
                            CapToClient.reset(e, r);
                            send(context, LangData.IDS.ACTION_SUCCESS.get());
                            return 1;
                        }))));

        registerCommand("add_exp", getPlayer()
                .then(Commands.argument("number", IntegerArgumentType.integer(0))
                        .executes(withPlayer((context, e) -> {
                            LLPlayerData handler = LLPlayerData.get(e);
                            int val = context.getArgument("number", Integer.class);
                            handler.abilityPoints.addExp(val);
                            new CapToClient(CapToClient.Action.ABILITY_POINT, handler).toClientPlayer(e);
                            send(context, LangData.IDS.ACTION_SUCCESS.get());
                            return 1;
                        }))));

    }

}
