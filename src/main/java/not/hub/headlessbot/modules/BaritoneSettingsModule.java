package not.hub.headlessbot.modules;

import baritone.api.BaritoneAPI;
import baritone.api.Settings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.util.BlockGroups;

import java.util.stream.Collectors;

public class BaritoneSettingsModule extends Module {

    private boolean initialized = false;

    public BaritoneSettingsModule() {
        super();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;
        Settings settings = BaritoneAPI.getSettings();
        if (settings == null) return;

        // TODO: https://github.com/cabaletta/baritone/issues/1400 😩

        if (!initialized) {
            initialized = true;

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

            settings.allowDownward.value = false; // this creates more natural pathes (staircases etc.)
            settings.allowVines.value = true;
            settings.allowWalkOnBottomSlab.value = true;
            settings.enterPortal.value = true;

            settings.considerPotionEffects.value = true;
            settings.rightClickContainerOnArrival.value = true;
            //settings.itemSaver.value = true;

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

            settings.followOffsetDistance.value = 8d;
            settings.followRadius.value = 16;

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

            settings.acceptableThrowawayItems.value.addAll(BlockGroups.PATHING_BLOCKS.items);

            Log.info(name, "Baritone initialized with settings:\n"
                + settings
                .allSettings
                .stream()
                .map(s -> "  - " + s.getName() + ": " + s.value)
                .collect(Collectors.joining("\n"))
            );

        }

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
