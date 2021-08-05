package not.hub.headlessbot.modules;

import cc.neckbeard.utils.ExpiringFlag;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Bot;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.StringFormat;
import not.hub.headlessbot.util.Webhook;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class RespawnModule extends Module {

    private static final ExpiringFlag respawnCooldown = new ExpiringFlag(10, ChronoUnit.SECONDS, false);
    private static final ExpiringFlag messageCooldown = new ExpiringFlag(1, ChronoUnit.MINUTES, false);
    private static final Set<String> messages = new HashSet<>();

    public RespawnModule() {
        super();
        messages.add("Fuck dude...");
        messages.add("Wtf is this shit?");
        messages.add("Damn man, again...");
        messages.add("All of my dried kelp is gone now ;(");
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;
        if (respawnCooldown.isValid()) return;
        else respawnCooldown.reset();
        if (mc.currentScreen instanceof GuiGameOver
            || mc.player.isDead
            || mc.player.getHealth() < 0) {
            Log.info(name, "Respawning...");
            mc.displayGuiScreen(null);
            Bot.WEBHOOK.alarm("Bot died!", Webhook.Field.of("location", StringFormat.of(mc.player)));
            mc.player.respawnPlayer();
            if (messageCooldown.isValid()) return;
            else messageCooldown.reset();
            sendChat(messages
                .stream()
                .skip(ThreadLocalRandom.current().nextInt(messages.size()))
                .findAny()
                .orElseThrow(IllegalStateException::new));
        }
    }

}
