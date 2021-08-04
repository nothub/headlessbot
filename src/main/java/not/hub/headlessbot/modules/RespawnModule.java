package not.hub.headlessbot.modules;

import cc.neckbeard.utils.ExpiringFlag;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Log;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class RespawnModule extends Module {

    private static final Set<String> messages = new HashSet<>();
    private static final ExpiringFlag messageCooldown = new ExpiringFlag(1, ChronoUnit.MINUTES, false);

    public RespawnModule() {
        super(Type.ALWAYS_ACTIVE);
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
        if (mc.currentScreen instanceof GuiGameOver
            || mc.player.isDead
            || mc.player.getHealth() < 0) {
            Log.info(name, "Respawning...");
            mc.displayGuiScreen(null);
            mc.player.respawnPlayer();
            if (messageCooldown.isValid()) return;
            else messageCooldown.reset();
            mc.addScheduledTask(() -> mc.player.connection.sendPacket(
                new CPacketChatMessage(
                    messages.stream()
                        .skip(ThreadLocalRandom.current().nextInt(messages.size()))
                        .findAny()
                        .orElseThrow(IllegalStateException::new))));
        }
    }

}
