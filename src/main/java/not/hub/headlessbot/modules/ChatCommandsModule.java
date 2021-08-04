package not.hub.headlessbot.modules;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import not.hub.headlessbot.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCommandsModule extends Module {

    public static final String PREFIX = "~";

    Pattern com = Pattern.compile("^(<[a-zA-Z0-9_]{3,16}>)\\s(.+)$");
    Pattern tpa = Pattern.compile("^([a-zA-Z0-9_]{3,16}) wants to teleport to you\\.$");

    public ChatCommandsModule() {
        super(Type.ALWAYS_ACTIVE);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent ev) {
        // Important! We ignore the difference of chat and system type messages,
        // because half of the server have fucked the logic with plugins anyways...
        Matcher tpaMatcher = tpa.matcher(ev.getMessage().getUnformattedText());
        if (tpaMatcher.find()) {
            final String requester = tpaMatcher.group(1);
            if (requester != null && !requester.isEmpty()) {
                mc.addScheduledTask(() -> mc.player.connection.sendPacket(
                    new CPacketChatMessage("/tpy " + requester)));
                Log.info(name, "Accepting tpa from " + requester);
                return;
            }
        }
        Matcher commandMatcher = com.matcher(ev.getMessage().getUnformattedText());
        if (commandMatcher.find()) {
            final String sender = commandMatcher.group(1);
            final String message = commandMatcher.group(2);
            if (sender != null && !sender.isEmpty() && message != null && !message.isEmpty() && message.startsWith(PREFIX)) {
                final String command = message.replaceFirst(PREFIX, "");
                Log.info(name, sender + " sent command: " + command);
            }
        }
    }

}
