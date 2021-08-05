package not.hub.headlessbot.util;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import not.hub.headlessbot.Bot;
import not.hub.headlessbot.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Webhook {

    private final WebhookClient client;

    public Webhook(String url) {
        WebhookClientBuilder builder = new WebhookClientBuilder(url);
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("discord_webhook");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        client = builder.build();
    }

    private static WebhookEmbedBuilder baseEmbed() {
        try {
            return new WebhookEmbedBuilder()
                .setFooter(
                    new WebhookEmbed.EmbedFooter(
                        "worker=" + InetAddress.getLocalHost().getHostName()
                            + " server=" + Bot.CONFIG.hostname,
                        null));
        } catch (UnknownHostException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    public void info(String title, Field... fields) {
        send(title, 0x00FF00, fields);
    }

    public void alarm(String title, Field... fields) {
        send(title, 0xFF0000, fields);
    }

    private void send(String title, int color, Field... fields) {
        Log.info(getClass(), "Sending webhook: " + title + " " + Arrays.stream(fields).map(Field::toString).collect(Collectors.joining(", ")) + " with color: " + Integer.toHexString(color));
        WebhookEmbedBuilder embed = baseEmbed()
            .setTitle(new WebhookEmbed.EmbedTitle(title, null))
            .setColor(color);
        for (Field field : fields) embed.addField(new WebhookEmbed.EmbedField(false, field.title, field.content));
        client.send(new WebhookMessageBuilder().addEmbeds(embed.build()).build());
    }

    public static class Field {
        public final String title;
        public final String content;

        public Field(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public static Field of(String title, String content) {
            return new Field(title, content);
        }

        @Override
        public String toString() {
            return "{title='" + title + "', content='" + content + "'}";
        }
    }

}
