package not.hub.headlessbot.fsm.behaviour;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.pathing.goals.GoalXZ;
import baritone.api.utils.BlockOptionalMeta;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Bot;
import not.hub.headlessbot.Cooldowns;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.ModuleRegistry;
import not.hub.headlessbot.modules.Module;
import not.hub.headlessbot.util.BlockGroups;
import not.hub.headlessbot.util.MC;

import java.util.concurrent.ThreadLocalRandom;

import static not.hub.headlessbot.util.PlayerStuff.hasBlocks;
import static not.hub.headlessbot.util.PlayerStuff.hasPickaxe;

// TODO: implement as FSM
public class Controller implements MC {

    private boolean active = false;

    public void activate() {
        if (active) return;
        active = true;
        Log.info(getClass(), "Activating modules and behaviour");
        MinecraftForge.EVENT_BUS.register(this);
        ModuleRegistry.getAll().forEach(Module::activate);
    }

    public void deactivate() {
        if (!active) return;
        active = false;
        Log.info(getClass(), "Deactivating modules and behaviour");
        MinecraftForge.EVENT_BUS.unregister(this);
        ModuleRegistry.getAll().forEach(Module::deactivate);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;

        // TODO: move this out of controller context
        if (Bot.isShutdown()) {
            if (Bot.FSM_STARTUP.current() == StartupFsm.State.ACTIVE) Bot.FSM_STARTUP.transition(true);
            else throw new IllegalStateException("Invalid fsm transition source state" + Bot.FSM_STARTUP.current().name());
            return;
        }

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
        } else if (ThreadLocalRandom.current().nextInt(10) == 0) {
            // get some pathing blocks
            // too much cpu for a lot of bots on a small vps
            if (!hasBlocks()) {
                Log.info(getClass(), "Got no blocks, gonna get some...");
                if (hasPickaxe()) BaritoneAPI
                    .getProvider()
                    .getPrimaryBaritone()
                    .getMineProcess()
                    .mine(64, BlockGroups.PATHING_BLOCKS.blocks.toArray(new Block[0]));
                else BaritoneAPI
                    .getProvider()
                    .getPrimaryBaritone()
                    .getMineProcess()
                    .mine(8, BlockGroups.MINEABLE_HAND.blocks.toArray(new Block[0]));
                return;
            }
        }

        // travel somewhere
        switch (mc.player.dimension) {
            case 0:  // Overworld
                if (mc.world.getBlockState(mc.player.getPosition()).getMaterial() == Material.PORTAL) break;
                BaritoneAPI.getProvider().getPrimaryBaritone().getGetToBlockProcess().getToBlock(new BlockOptionalMeta("portal"));
                Log.info(getClass(), "Searching for nether portal...");
                break;
            case -1:  // Nether
                final GoalBlock netherGoal = randomGoal(0, 120, 0, 128);
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(netherGoal);
                Log.info(getClass(), "Going to nether coords: " + netherGoal);
                break;
            case 1:  // End
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(randomGoal(mc.player.getPosition().getX(), mc.player.getPosition().getZ(), 64));
                Log.info(getClass(), "Chilling in the end...");
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
