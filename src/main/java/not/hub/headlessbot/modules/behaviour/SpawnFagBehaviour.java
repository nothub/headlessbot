package not.hub.headlessbot.modules.behaviour;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.pathing.goals.GoalXZ;
import baritone.api.utils.BlockOptionalMeta;
import cc.neckbeard.utils.ExpiringFlag;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Cooldowns;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.StringFormat;
import not.hub.headlessbot.modules.Module;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnFagBehaviour extends Module {

    private final ExpiringFlag printPosition = new ExpiringFlag(10, ChronoUnit.SECONDS);

    public SpawnFagBehaviour() {
        super();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;

        // print infos
        // TODO: expose such data via json-file/redis/whatever instead
        if (printPosition.isExpired()) {
            printPosition.reset();
            Log.info(name, "Current location: " + StringFormat.of(mc.player));
        }

        // BARITONE

        // is cooldown
        if (Cooldowns.BARITONE.isValid()) return;
        Cooldowns.BARITONE.reset();

        // is busy pathing
        if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()) return;
        // is busy following
        if (BaritoneAPI.getProvider().getPrimaryBaritone().getFollowProcess().isActive()) return;
        // is busy mining
        if (BaritoneAPI.getProvider().getPrimaryBaritone().getMineProcess().isActive()) return;

        Log.info(getClass(), "Generating new baritone goal...");

        // blockgame is hard
        if (ThreadLocalRandom.current().nextInt(10) == 0) {
            final GoalXZ goal = randomGoal(0, 0, 1024);
            Log.info(getClass(), "Maybe we lucky? Gonna go to: " + goal);
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(goal);
            return;
        }

        // get some pathing blocks
        // too much cpu for a lot of bots on my vps
//        if (!hasBlocks()) {
//            Log.info(getClass(), "Got no blocks, gonna get some...");
//            if (hasPickaxe()) BaritoneAPI
//                .getProvider()
//                .getPrimaryBaritone()
//                .getMineProcess()
//                .mine(64, BlockGroups.PATHING_BLOCKS.blocks.toArray(new Block[0]));
//            else BaritoneAPI
//                .getProvider()
//                .getPrimaryBaritone()
//                .getMineProcess()
//                .mine(8, BlockGroups.MINEABLE_HAND.blocks.toArray(new Block[0]));
//            return;
//        }

        // travel
        switch (mc.player.dimension) {
            case 0:  // Overworld
                if (mc.world.getBlockState(mc.player.getPosition()).getMaterial() == Material.PORTAL) break;
                BaritoneAPI.getProvider().getPrimaryBaritone().getGetToBlockProcess().getToBlock(new BlockOptionalMeta("portal"));
                Log.info(name, "Searching for nether portal...");
                break;
            case -1:  // Nether
                final GoalBlock netherGoal = randomGoal(0, 120, 0, 128);
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(netherGoal);
                Log.info(name, "Going to nether coords: " + netherGoal);
                break;
            case 1:  // End
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(randomGoal(mc.player.getPosition().getX(), mc.player.getPosition().getZ(), 64));
                Log.info(name, "Chilling in the end...");
                break;
        }

    }

    private GoalXZ randomGoal(final int x, final int z, int radius) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        if (radius < 0) radius = Math.abs(radius);
        if (radius < 1) radius = 1;
        final int xRandom = random.nextInt(-1 * radius, radius + 1);
        final int zRandom = random.nextInt(-1 * radius, radius + 1);
        return new GoalXZ(x + xRandom, z + zRandom);
    }

    private GoalBlock randomGoal(final int x, final int y, final int z, int radius) {
        GoalXZ goalXZ = randomGoal(x, z, radius);
        return new GoalBlock(goalXZ.getX(), y, goalXZ.getZ());
    }

}
