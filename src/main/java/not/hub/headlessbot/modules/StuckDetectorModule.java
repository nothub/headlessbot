package not.hub.headlessbot.modules;

import cc.neckbeard.utils.ExpiringFlag;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Bot;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.util.ChunkLocation;
import not.hub.headlessbot.util.StringFormat;
import not.hub.headlessbot.util.Webhook;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class StuckDetectorModule extends Module {

    private final ExpiringFlag cooldown = new ExpiringFlag(15, ChronoUnit.MINUTES);
    private final Set<Long> visited = new HashSet<>();

    @Override
    void onActivate() {
        cooldown.reset();
        visited.clear();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;
        visited.add(ChunkLocation.of(mc.player).key());
        if (cooldown.isValid()) return;
        else cooldown.reset();
        if (visited.size() > 4) {
            Log.info(getClass(), "Visited " + visited.size() + " different chunks in the last 15 minutes!");
            visited.clear();
            return;
        }
        Bot.WEBHOOK.alarm("Bot is stuck!",
            Webhook.Field.of("\uD83D\uDE29", "Stuck in the same chunks as 15 minutes ago, imma get out of here..."),
            Webhook.Field.of("location", StringFormat.of(mc.player))
        );
        sendChat("/kill");
    }

}
