package ru.marduk.nedologin.commands;



import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import ru.marduk.nedologin.server.storage.NLStorage;

import static net.minecraft.server.command.CommandManager.*;

public class CommandHandler
{
    public static void init()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("spermobak").requires(source -> source.hasPermissionLevel(4))
                    .then(literal("unregister")
                        .then(argument("name", StringArgumentType.string())
                            .executes(context -> {
                                String name = StringArgumentType.getString(context, "name");
                                if(!NLStorage.instance().storageProvider.registered(name))
                                {
                                    context.getSource().sendFeedback(() -> 
                                            Text.literal("Ты пиздокрысоблядское уёбище, он не зареган долбаёб"),
                                                false);
                                    return 0;
                                }
                                NLStorage.instance().storageProvider.unregister(name.toLowerCase());

                                context.getSource().sendFeedback(() ->
                                        Text.literal("Убрали, человека"),
                                        false);
                                return 1;
                            }))
                .executes(context -> {
                    context.getSource().sendFeedback(() -> 
                            Text.literal("Я спермобак, и я всегда на раздаче!"), false);

                    return 1;
                }))));
    }
}
