package ru.marduk.nedologin.mixin;

import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.marduk.nedologin.Nedologin;
import ru.marduk.nedologin.server.storage.NLStorage;

import java.net.SocketAddress;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow @Nullable public abstract ServerPlayerEntity getPlayer(String name);

    @Inject(method = "checkCanJoin", at = @At("HEAD"), cancellable = true)
    public void checkCanJoin(SocketAddress address, PlayerConfigEntry configEntry, CallbackInfoReturnable<Text> cir) {
        ServerPlayerEntity onlinePlayer = getPlayer(configEntry.name());

        if (onlinePlayer != null) {
            cir.setReturnValue(Text.literal("Someone is already playing with that nickname."));
            Nedologin.logger.warn("Someone tried to log in as an already present player {}", configEntry.name());
        }

        // && !NLConfig.SERVER.autoRegister.get()
        if (!NLStorage.instance().storageProvider.registered(configEntry.name())) {
            // this sounds like that one ""
            Nedologin.logger.warn("Player {} tried to register (automatic registration is disabled)", configEntry.name());
            cir.setReturnValue(Text.literal("Automatic registration is disabled on this server."));
        }
    }
}
