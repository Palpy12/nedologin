package ru.marduk.nedologin.client;

import net.fabricmc.api.ClientModInitializer;

public class NedologinClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientLoader.clientSetup();
        ChangePasswordCommand.register();
    }
}