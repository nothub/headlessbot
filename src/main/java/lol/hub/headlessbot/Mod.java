package lol.hub.headlessbot;

import baritone.api.BaritoneAPI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.network.AbstractClientPlayerEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Mod implements ModInitializer, ClientModInitializer {

    public static long lastKeepAlive;
    public static long ticksOnline;
    public Set<Object> modules = new HashSet<>();

    @Override
    public void onInitialize() {
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {

        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!MC.inGame()) return;
            ticksOnline++;

            if (MC.player().isDead()) {
                if (ticksOnline % 20 == 0) {
                    Log.info("requesting respawn");
                    MC.player().requestRespawn();
                }
                return;
            }

            for (AbstractClientPlayerEntity player : MC.world().getPlayers()) {
                if (player.getUuid().equals(MC.player().getUuid())) continue;
                // Log.info("player in close proximity: %s (%s)", player.getGameProfile().getName(), player.getUuid().toString());
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            var netHandler = client.getNetworkHandler();
            if (netHandler == null) return;

            var serverInfo = netHandler.getServerInfo();
            if (serverInfo == null) return;

            Log.info("client connected to %s (%s)", serverInfo.name, serverInfo.address);

            configureBaritone();
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            Log.warn("client disconnected");
            try {
                client.close();
            } catch (Exception ex) {
                Log.error(ex.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.error(ex.getMessage());
                }
                System.exit(0);
            }
        });

        modules.add(new Chat());
    }

    private void configureBaritone() {
        var settings = BaritoneAPI.getSettings();

        settings.allowBreak.value = true;
        settings.allowPlace.value = true;
        settings.allowInventory.value = true;
        settings.allowJumpAt256.value = true;
        settings.allowWaterBucketFall.value = true;
        settings.maxFallHeightBucket.value = 256;

        settings.allowParkour.value = true;
        settings.allowParkourPlace.value = true;
        settings.allowParkourAscend.value = true;
        settings.allowDiagonalDescend.value = true;
        settings.allowDiagonalAscend.value = true;

        settings.exploreForBlocks.value = true;

        // assume no jesus as default
        settings.assumeWalkOnWater.value = false;
        settings.assumeWalkOnLava.value = false;

        settings.allowDownward.value = true;
        settings.allowVines.value = true;
        settings.allowWalkOnBottomSlab.value = true;
        settings.enterPortal.value = true;

        settings.considerPotionEffects.value = true;
        settings.rightClickContainerOnArrival.value = true;

        settings.replantCrops.value = true;
        settings.replantNetherWart.value = true;

        settings.mineScanDroppedItems.value = true;
        settings.legitMine.value = false;

        settings.pauseMiningForFallingBlocks.value = true;
        settings.avoidUpdatingFallingBlocks.value = true;

        settings.buildIgnoreExisting.value = true;
        settings.okIfWater.value = true;

        settings.avoidance.value = true;
        settings.mobSpawnerAvoidanceRadius.value = 16;
        settings.mobAvoidanceRadius.value = 16;

        settings.followOffsetDistance.value = 4d;
        settings.followRadius.value = 8;

        settings.echoCommands.value = true;
        settings.prefixControl.value = true;
        settings.chatControl.value = false;
        settings.chatControlAnyway.value = false;
        settings.chatDebug.value = false;

        // we dont see this stuff anyways
        settings.renderPath.value = false;
        settings.renderPathAsLine.value = false;
        settings.renderGoal.value = false;
        settings.renderSelectionBoxes.value = false;
        settings.renderGoalXZBeacon.value = false;
        settings.renderCachedChunks.value = false;
        settings.renderSelection.value = false;
        settings.renderSelectionCorners.value = false;
        settings.desktopNotifications.value = false;

        //settings.acceptableThrowawayItems.value.addAll(BlockGroups.PATHING_BLOCKS.items);

        Log.info("Baritone initialized with settings:\n" + settings.allSettings.stream().map(s -> "  - " + s.getName() + ": " + s.value).collect(Collectors.joining("\n")));

        /*
         *       TODO: check hunger levels
         *     public final Settings$Setting<Boolean> allowSprint;
         *     public final Settings$Setting<Boolean> sprintInWater;
         *
         *       TODO: sync from module states
         *     public final Settings$Setting<Integer> maxFallHeightNoWater;
         *     public final Settings$Setting<Boolean> antiCheatCompatibility;
         *     public final Settings$Setting<Boolean> assumeExternalAutoTool;
         *     public final Settings$Setting<Boolean> assumeWalkOnWater;
         *     public final Settings$Setting<Boolean> assumeWalkOnLava;
         *     public final Settings$Setting<Boolean> assumeStep;
         *     public final Settings$Setting<Boolean> assumeSafeWalk;
         *
         *       TODO: builder mode
         *     public final Settings$Setting<Boolean> buildInLayers;
         *     public final Settings$Setting<Boolean> layerOrder;
         *     public final Settings$Setting<Integer> startAtLayer;
         *     public final Settings$Setting<Boolean> skipFailedLayers;
         *     public final Settings$Setting<Boolean> mapArtMode;
         *     public final Settings$Setting<Boolean> schematicOrientationX;
         *     public final Settings$Setting<Boolean> schematicOrientationY;
         *     public final Settings$Setting<Boolean> schematicOrientationZ;
         *
         */

    }
}
