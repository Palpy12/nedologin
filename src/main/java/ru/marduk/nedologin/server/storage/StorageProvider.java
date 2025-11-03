package ru.marduk.nedologin.server.storage;

import java.io.IOException;
import java.util.Collection;

public interface StorageProvider {
    boolean checkPassword(String username, String password);

    void unregister(String username);

    boolean registered(String username);

    void register(String username, String password);

    void save() throws IOException;

    void changePassword(String username, String newPassword);

    boolean dirty();

    /**
     * Should be immutable
     *
     * @return all registered username
     */
    Collection<String> getAllRegisteredUsername();
}
