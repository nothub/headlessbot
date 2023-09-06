package lol.hub.headlessbot;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

import java.util.stream.Stream;

public class MC {

    private static MinecraftClient mc;

    public static synchronized MinecraftClient get() {
        if (mc == null) mc = MinecraftClient.getInstance();
        return mc;
    }

    public static ClientPlayerEntity player() {
        return get().player;
    }

    public static ClientWorld world() {
        return get().world;
    }

    public static boolean inGame() {
        return player() != null && world() != null;
    }

    public static Stream<AbstractClientPlayerEntity> players() {
        if (!inGame()) return Stream.empty();
        return world().getPlayers().stream();
    }

}
