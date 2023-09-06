package lol.hub.headlessbot;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {
    public static final Pattern PATTERN_CHAT = Pattern.compile("<([a-zA-Z0-9_]{3,16})> (.+)");
    public static final Pattern PATTERN_CHAT_CONSOLE = Pattern.compile("\\[Server\\] (.+)");
    public static final Pattern PATTERN_WHISPER = Pattern.compile("([a-zA-Z0-9_]{3,16}) whispers to you: (.+)");
    private final Map<Pattern, Consumer<Matcher>> handlers;

    public Chat() {
        this.handlers = new HashMap<>();

        // default handlers
        register(PATTERN_CHAT, matcher -> {
            var msg = new Chat.Message(matcher.group(1), matcher.group(2));
            Log.info("detected chat message: %s", msg.toString());
        });
        register(PATTERN_CHAT_CONSOLE, matcher -> {
            var msg = new Chat.Message("Server", matcher.group(1));
            Log.info("detected server-console chat message: %s", msg.toString());
        });
        register(PATTERN_WHISPER, matcher -> {
            var msg = new Chat.Message(matcher.group(1), matcher.group(2));
            Log.info("detected whisper message: %s", msg.toString());
        });

        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            handle(message.getString());
        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (overlay) {
                Log.info("overlay message: %s", message.getString());
                return;
            }

            // Some servers send normal player chat with system type,
            // so this could be a normal player message.
            // For that reason, we just ignore message types
            // and handle all (non-overlay) messages in the same way.
            handle(message.getString());
        });
    }

    public void register(Pattern pattern, Consumer<Matcher> handler) {
        handlers.put(pattern, handler);
    }

    private void handle(String message) {
        for (Map.Entry<Pattern, Consumer<Matcher>> entry : handlers.entrySet()) {
            var matcher = entry.getKey().matcher(message);
            if (matcher.matches()) {
                entry.getValue().accept(matcher);
            }
        }
    }

    public record Message(String sender, String message, long time) {
        public Message(String sender, String message) {
            this(sender, message, System.currentTimeMillis());
        }
    }
}
