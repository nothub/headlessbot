package not.hub.headlessbot.modules;

import cc.neckbeard.utils.ExpiringFlag;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Log;

import java.time.temporal.ChronoUnit;

public class StuckDetectorModule extends Module {
    private static final ExpiringFlag cooldown = new ExpiringFlag(15, ChronoUnit.MINUTES, true);
    private static int lastX;
    private static int lastZ;

    public StuckDetectorModule() {
        super();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;
        if (cooldown.isValid()) return;
        else cooldown.reset();
        if (mc.player.chunkCoordX == lastX && mc.player.chunkCoordZ == lastZ) {
            Log.warn(name, "Im stuck in the same chunk (" + lastX + "x " + lastZ + "z) as 15 minutes ago, imma get out of here...");
            mc.player.connection.sendPacket(new CPacketChatMessage("/kill"));
            return;
        }
        lastX = mc.player.chunkCoordX;
        lastZ = mc.player.chunkCoordZ;
    }

}
