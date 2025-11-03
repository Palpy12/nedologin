package ru.marduk.nedologin.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import ru.marduk.nedologin.network.MessageChangePassword;

import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class ChangePasswordCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literal("nedologin")
                    .then(literal("change_password"))
                        .then(argument("passwd", StringArgumentType.string()))
                            .executes(context -> {
                                final var to = StringArgumentType.getString(context, "passwd");
                                var msg = new MessageChangePassword(PasswordHolder.instance().password(), to);
                                PasswordHolder.instance().setPendingPassword(to);
                                //PacketDistributor.sendToServer(msg);
                                return Command.SINGLE_SUCCESS;
                            }));
        });
    }

}
