package not.hub.headlessbot.modules.behaviour;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalXZ;
import baritone.api.utils.BlockOptionalMeta;
import cc.neckbeard.utils.ExpiringFlag;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.modules.Module;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

import static not.hub.headlessbot.Bot.CONFIG;

public class SpawnFagBehaviour extends Module {


    private final ExpiringFlag printPosition = new ExpiringFlag(10, ChronoUnit.SECONDS);
    private final ExpiringFlag printQueue = new ExpiringFlag(1, ChronoUnit.MINUTES);

    // TODO: replace this with an actual check
    private final ExpiringFlag baritoneGoalDelay = new ExpiringFlag(5, ChronoUnit.SECONDS);

    public SpawnFagBehaviour() {
        super();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;

        // TODO: put this somewhere else
        // in 2b queue
        if (mc.getCurrentServerData() == null) return;
        final BlockPos pos = mc.player.getPosition();
        if (mc.getCurrentServerData().serverIP.equals("2b2t.org")
            && pos.getX() == 0
            && pos.getY() == 240
            && pos.getZ() == 0) {
            if (!printQueue.isValid()) {
                Log.info(name, CONFIG.hostname + " is full...");
                printQueue.reset();
            }
            return;
        }

        // TODO: expose such data via json-file/redis/whatever instead
        // print infos
        if (!printPosition.isValid()) {
            Log.info(name, "I am at: " + pos.getX() + "x " + pos.getY() + "y " + pos.getZ() + "z (" + mc.player.dimension + ")");
            printPosition.reset();
        }

        // baritone
        try {
            if (baritoneGoalDelay.isValid()) return;
            if (!BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()) {
                switch (mc.player.dimension) {
                    case 0:  // Overworld
                        if (mc.world.getBlockState(mc.player.getPosition()).getMaterial() == Material.PORTAL) break;
                        if (ThreadLocalRandom.current().nextBoolean()) {
                            BaritoneAPI.getProvider().getPrimaryBaritone().getGetToBlockProcess().getToBlock(new BlockOptionalMeta("portal"));
                        } else {
                            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(randomGoal(0, 0, 256));
                        }
                        Log.info(name, "Searching for a nether portal...");
                        break;
                    case -1:  // Nether
                        final Goal netherGoal = randomGoal(0, 0, 128);
                        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(netherGoal);
                        Log.info(name, "Going to nether coords: " + netherGoal);
                        break;
                    case 1:  // End
                        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(randomGoal(mc.player.getPosition().getX(), mc.player.getPosition().getZ(), 128));
                        Log.info(name, "Chilling in the end...");
                        break;
                }
                baritoneGoalDelay.reset();
            }
        } catch (NoClassDefFoundError | NullPointerException ex) {
            Log.error(name, "Baritone API not found, shutting down... " + ex.getMessage());
            FMLCommonHandler.instance().exitJava(1, false);
        }
    }

    private Goal randomGoal(final int x, final int z, int radius) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        if (radius < 0) radius = Math.abs(radius);
        if (radius < 1) radius = 1;
        final int xRandom = random.nextInt(-1 * radius, radius + 1);
        final int zRandom = random.nextInt(-1 * radius, radius + 1);
        return new GoalXZ(x + xRandom, z + zRandom);
    }

}
