package not.hub.headlessbot.modules;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalXZ;
import cc.neckbeard.utils.ExpiringFlag;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Log;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

import static not.hub.headlessbot.Bot.CONFIG;

public class WalkingSimulatorModule extends Module {


    private final ExpiringFlag printPosition = new ExpiringFlag(10, ChronoUnit.SECONDS);
    private final ExpiringFlag printQueue = new ExpiringFlag(1, ChronoUnit.MINUTES);

    // TODO: replace this with an actual check
    private final ExpiringFlag baritoneGoalDelay = new ExpiringFlag(5, ChronoUnit.SECONDS);

    public WalkingSimulatorModule() {
        super(Type.ALWAYS_ACTIVE);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;

        // on server
        if (mc.getCurrentServerData() == null) return;

        // in 2b queue
        final BlockPos pos = Minecraft.getMinecraft().player.getPosition();
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

        // print infos
        if (!printPosition.isValid()) {
            Log.info(name, "I am at: " + pos.getX() + "x " + pos.getY() + "y " + pos.getZ() + "z");
            printPosition.reset();
        }

        // baritone
        try {
            // TODO: this is wonky and triggers 20-30 times before baritone actually starts walking
            if (baritoneGoalDelay.isValid()) return;
            if (!BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()) {
                final Goal goal = randomGoal(420, 420, 128);
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(goal);
                Log.info(name, "I am going to: " + goal);
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
