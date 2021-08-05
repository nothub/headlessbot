package not.hub.headlessbot.modules;

import cc.neckbeard.utils.ExpiringFlag;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ChatSpamModule extends Module {

    private static final Set<String> messages = new HashSet<>();
    private static final ExpiringFlag cooldown = new ExpiringFlag(5, ChronoUnit.MINUTES, true);

    public ChatSpamModule() {
        super();
        messages.add("Hey bitches whats up?");
        messages.add("I live at spawn, visit me at 420 ;>");
        messages.add("Who has no head but can speak anyways?");
        messages.add("Have you tried with vanilla client?");
        messages.add("But I'm going to do it anyway, because yolo!");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent ev) {
        if (cooldown.isValid()) return;
        else cooldown.reset();
        sendChat(messages
            .stream()
            .skip(ThreadLocalRandom.current().nextInt(messages.size()))
            .findAny()
            .orElseThrow(IllegalStateException::new));
    }

}
