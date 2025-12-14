package ru.marduk.nedologin;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static net.minecraft.server.command.CommandManager.*;

public class CommandHandler
{
    public static void init()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("spermobak").requires(source -> source.hasPermissionLevel(4)
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
                                NLStorage.instance().storageProvider.unregister(name);

                                context.getSource().sendSuccess(() -> 
                                        Text.literal("Убрали, человека"),
                                        false);
                                return 1;
                            }))
                .executes(context -> {
                    context.getSource().sendFeedback(() -> 
                            Text.literal("Я спермобак, и я всегда на раздаче!"), false);

                    return 1;
                })));
    }
}
