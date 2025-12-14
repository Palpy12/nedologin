package ru.marduk.nedologin.server.storage;

import net.minecraft.util.Identifier;
import ru.marduk.nedologin.Nedologin;
import ru.marduk.nedologin.server.NLRegistries;

public class NLStorage {
    public final StorageProviderSQL storageProvider;
    private static NLStorage INSTANCE;

    public static NLStorage instance() {
        return INSTANCE;
    }

    public static void initialize(String provider) {
        if (INSTANCE == null) {
            try {
                INSTANCE = new NLStorage(provider);
            } catch (Exception e) {
                Nedologin.logger.fatal("Failed to initialize login provider '{}': {}", provider, e.getMessage());
            }
        }
    }

    private NLStorage(String provider) {
        storageProvider = NLRegistries.STORAGE_PROVIDERS.get(Identifier.of("nedologin", provider))
                .orElseThrow(() -> new RuntimeException("Storage provider not found: " + provider))
                .get();
    }
}
