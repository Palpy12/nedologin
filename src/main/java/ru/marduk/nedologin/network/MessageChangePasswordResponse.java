package ru.marduk.nedologin.network;

import io.netty.buffer.ByteBuf;
import ru.marduk.nedologin.NLConstants;
import ru.marduk.nedologin.client.PasswordHolder;

public record MessageChangePasswordResponse(boolean success) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MessageChangePasswordResponse> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(NLConstants.MODID, "m_change_pwd_res"));
    public static final StreamCodec<ByteBuf, MessageChangePasswordResponse> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            MessageChangePasswordResponse::success,
            MessageChangePasswordResponse::new
    );

    public static void handle(final MessageChangePasswordResponse message, final IPayloadContext ctx) {
        if (message.success()) {
            PasswordHolder.instance().applyPending();
        } else {
            PasswordHolder.instance().dropPending();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
