package ru.marduk.nedologin.server.storage;

import net.minecraft.util.Identifier;
import ru.marduk.nedologin.server.NLRegistries;

public class NLStorage {
    public StorageProviderSQL storageProvider;
    private static NLStorage INSTANCE;
    public static NLStorage instance() {
        return INSTANCE;
    }

    private NLStorage() {
        storageProvider = NLRegistries.STORAGE_PROVIDERS.get(Identifier.of("nedologin", "sqlite"))
                .orElseThrow(() -> new RuntimeException("Storage provider not found"))
                .get();
    }

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = new NLStorage();
        }
    }
}
