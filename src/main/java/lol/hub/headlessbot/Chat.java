package lol.hub.headlessbot;

import net.minecraft.entity.Entity;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.regex.Pattern;

import static net.minecraft.util.Util.NIL_UUID;

public class Chat {
    private static final Pattern consoleSenderPattern = Pattern.compile("\\[Server\\] (.+)");
    private static final Pattern playerSenderPattern = Pattern.compile("<([a-zA-Z0-9_]{3,16})> (.+)");

    public static void handleMessage(String raw) {
        Message msg = null;

        var consoleSenderMatcher = consoleSenderPattern.matcher(raw);
        var playerSenderMatcher = playerSenderPattern.matcher(raw);
        if (consoleSenderMatcher.matches()) {
            msg = new Chat.Message("Server", consoleSenderMatcher.group(0));
        } else if (playerSenderMatcher.matches()) {
            var name = playerSenderMatcher.group(0);
            var uuid = MC.players().filter(p -> p.getName().getString().equalsIgnoreCase(name))
                .map(Entity::getUuid).findAny().orElse(null);
            msg = new Chat.Message(name, uuid, playerSenderMatcher.group(1));
        }

        if (msg == null) {
            Log.info("Unable to parse chat message: %s", raw);
            return;
        }

        Log.error("TODO publish message to module listeners: %s", ReflectionToStringBuilder.toString(msg));
    }

    public static final class Message {
        private final String senderName;
        private final UUID senderUUID;
        private final String message;
        private final long time;

        public Message(String senderName, @Nullable UUID senderUUID, String message, long time) {
            this.senderName = senderName;
            if (senderUUID == null) this.senderUUID = NIL_UUID;
            else this.senderUUID = senderUUID;
            this.message = message;
            this.time = time;
        }

        public Message(String senderName, @Nullable UUID senderUUID, String message) {
            this(senderName, senderUUID, message, System.currentTimeMillis());
        }

        public Message(String senderName, String message, long time) {
            this(senderName, NIL_UUID, message, time);
        }

        public Message(String senderName, String message) {
            this(senderName, NIL_UUID, message, System.currentTimeMillis());
        }

        public String senderName() {
            return senderName;
        }

        public UUID senderUUID() {
            return senderUUID;
        }

        public boolean isPlayer() {
            return senderUUID != null && !senderUUID.equals(NIL_UUID);
        }

        public String message() {
            return message;
        }

        public long time() {
            return time;
        }
    }
}
