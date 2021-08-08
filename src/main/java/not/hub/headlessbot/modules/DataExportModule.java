package not.hub.headlessbot.modules;

import cc.neckbeard.utils.ExpiringFlag;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.util.StringFormat;

import java.time.temporal.ChronoUnit;

public class DataExportModule extends Module {

    private static final ExpiringFlag consolePrint = new ExpiringFlag(1, ChronoUnit.MINUTES, false);

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;
        if (consolePrint.isExpired()) {
            consolePrint.reset();
            Log.info(getClass(), "Current location: " + StringFormat.of(mc.player));
        }
    }

}
