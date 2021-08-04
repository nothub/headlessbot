package not.hub.headlessbot.modules;

import cc.neckbeard.utils.ExpiringFlag;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Log;

import java.time.temporal.ChronoUnit;

public class StuckDetectorModule extends Module {
    private static final ExpiringFlag cooldown = new ExpiringFlag(1, ChronoUnit.MINUTES, false);
    private static int lastX;
    private static int lastZ;

    public StuckDetectorModule() {
        super(Type.ALWAYS_ACTIVE);
    }

    private static boolean isSolid(Material material) {
        return material != Material.AIR && material != Material.LAVA && material != Material.WATER;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;
        if (cooldown.isValid()) return;
        else cooldown.reset();
        if (mc.player.getPosition().getX() == lastX && mc.player.getPosition().getZ() == lastZ) {
            Log.warn(name, "Im stuck, still in the same place as last minute!");
        }
        lastX = mc.player.getPosition().getX();
        lastZ = mc.player.getPosition().getX();
    }

}
