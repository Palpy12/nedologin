package ru.marduk.nedologin.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class NetworkLoader {
    public static final Identifier REQUEST_LOGIN = Identifier.of("nedologin", "request_login");
    public static final Identifier LOGIN = Identifier.of("nedologin", "login");
    public static final Identifier CHANGE_PASSWORD = Identifier.of("nedologin", "change_password");
    public static final Identifier CHANGE_PASSWORD_RESPONSE = Identifier.of("nedologin", "change_password_response");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(LOGIN, (server, player, handler, buf, responseSender) -> {
            String password = buf.readString();
            player.server().execute(() -> MessageLogin.handle(player, password));
        });
    }

    private NetworkLoader() {
        throw new UnsupportedOperationException("No instance");
    }
}
