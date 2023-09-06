package lol.hub.headlessbot;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

public class MC {

    private static MinecraftClient mc;

    public static synchronized MinecraftClient client() {
        if (mc == null) mc = MinecraftClient.getInstance();
        return mc;
    }

    public static ClientPlayerEntity player() {
        return client().player;
    }

    public static ClientWorld world() {
        return client().world;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean inGame() {
        return player() != null && world() != null;
    }

}
