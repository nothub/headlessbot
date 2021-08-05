package not.hub.headlessbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class Config {

    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public final String username;
    public final String password;
    public final String hostname;
    public final String webhook;

    public Config() {
        this.username = "USERNAME";
        this.password = "PASSWORD";
        this.hostname = "EXAMPLE.ORG";
        this.webhook = "https://discord.com/api/webhooks/ID/TOKEN";
    }

    public static Config load() {
        Log.info("Config", "Loading from file...");
        final File file = new File("headless.json");
        try {
            return gson.fromJson(new String(Files.readAllBytes(file.toPath())), Config.class);
        } catch (IOException ex) {
            Log.warn("Config", "Unable to load from file: " + ex.getMessage());
        }
        Log.info("Config", "Saving default config");
        final Config defaults = new Config();
        try {
            FileUtils.writeStringToFile(file, gson.toJson(defaults), Charset.defaultCharset());
        } catch (IOException ex) {
            Log.error("Config", ex.getMessage());
            FMLCommonHandler.instance().exitJava(1, false);
        }
        return defaults;
    }

}
