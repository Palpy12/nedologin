package ru.marduk.nedologin.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.marduk.nedologin.server.handler.PlayerLoginHandler;
import ru.marduk.nedologin.network.MessageRequestLogin;


@Environment(EnvType.SERVER)
public class ServerSideEventHandler {

    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerLoginHandler.instance().playerJoin((ServerPlayer) event.getEntity());
        PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new MessageRequestLogin());
        //NetworkLoader.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new MessageRequestLogin());
    }

    @SubscribeEvent
    public static void playerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerLoginHandler.instance().playerLeave((ServerPlayer) event.getEntity());
    }
}
