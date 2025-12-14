package ru.marduk.nedologin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import ru.marduk.nedologin.network.*;

import java.util.UUID;

public final class ClientLoader {
    public static void clientSetup() {
        if(!PasswordHolder.instance().initialized()) {
            PasswordHolder.instance().initialize(UUID.randomUUID().toString());
        }

        ClientPlayNetworking.registerGlobalReceiver(MessageRequestLogin.ID, (message, context) ->
            ClientPlayNetworking.send(new MessageLogin(PasswordHolder.instance().password()))
        );
    }
}
