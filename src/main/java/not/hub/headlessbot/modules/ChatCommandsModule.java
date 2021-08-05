package not.hub.headlessbot.modules;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.util.TriConsumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCommandsModule extends Module {

    private static final String PREFIX = "~";

    private static final Pattern com = Pattern.compile("^(<[a-zA-Z0-9_]{3,16}>)\\s(.+)$");
    private static final Pattern tpa = Pattern.compile("^([a-zA-Z0-9_]{3,16}) wants to teleport to you\\.$");

    private static final Map<String, TriConsumer<String, String, List<String>>> commands = new ConcurrentHashMap<>();

    private final TriConsumer<String, String, List<String>> defaultCommand = (sender, label, args) -> Log.info(getClass(), sender + " issued unknown command " + label + " with args: " + String.join(" ", args));

    public ChatCommandsModule() {
        super();
        commands.put("test", (sender, label, args) -> sendChat(sender + " I can hear you but i have no brain sorry... You said: " + label + String.join(" ", args)));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent ev) {
        // Important! We ignore the difference of chat and system type messages,
        // because half of the servers have fucked the logic with plugins anyways...
        Matcher tpaMatcher = tpa.matcher(ev.getMessage().getUnformattedText());
        if (tpaMatcher.find()) {
            final String requester = tpaMatcher.group(1);
            if (requester != null && !requester.isEmpty()) {
                sendChat("/tpy " + requester);
                Log.info(name, "Accepting tpa from " + requester);
                return;
            }
        }
        Matcher commandMatcher = com.matcher(ev.getMessage().getUnformattedText());
        if (commandMatcher.find()) {
            final String sender = commandMatcher.group(1);
            final String message = commandMatcher.group(2);
            if (sender != null && !sender.isEmpty() && message != null && message.startsWith(PREFIX)) {
                final CommandData command = new CommandData(message);
                Log.info(name, sender + " sent command: " + command);
                commands
                    .computeIfAbsent(command.label, unused -> defaultCommand)
                    .accept(sender, command.label, command.args);
            }
        }
    }

    public static class CommandData {
        public final String label;
        public final List<String> args;

        public CommandData(String raw) {
            String[] split = raw.split(" ");
            switch (split.length) {
                case 0:
                    this.label = "";
                    this.args = Collections.emptyList();
                    break;
                case 1:
                    this.label = split[0].replaceFirst(PREFIX, "");
                    this.args = Collections.emptyList();
                    break;
                default:
                    this.label = split[0].replaceFirst(PREFIX, "");
                    this.args = Arrays.asList(Arrays.copyOfRange(split, 1, split.length));
                    break;
            }
        }

        @Override
        public String toString() {
            return label + " " + args;
        }
    }

}
