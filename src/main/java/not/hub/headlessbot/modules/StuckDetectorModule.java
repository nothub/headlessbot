package not.hub.headlessbot.modules;

import cc.neckbeard.utils.ExpiringFlag;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Bot;
import not.hub.headlessbot.StringFormat;
import not.hub.headlessbot.util.Webhook;

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
            lastX = Integer.MAX_VALUE;
            lastZ = Integer.MAX_VALUE;
            Bot.WEBHOOK.alarm("Bot is stuck!",
                Webhook.Field.of("\uD83D\uDE29", "Stuck in the same chunk as 15 minutes ago, imma get out of here..."),
                Webhook.Field.of("location", StringFormat.of(mc.player))
            );
            sendChat("/kill");
        } else {
            lastX = mc.player.chunkCoordX;
            lastZ = mc.player.chunkCoordZ;
        }
    }

}
