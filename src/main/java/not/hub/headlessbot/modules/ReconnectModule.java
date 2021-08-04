package not.hub.headlessbot.modules;

import cc.neckbeard.utils.ExpiringFlag;
import me.earth.headlessforge.util.NetworkUtil;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import not.hub.headlessbot.Log;

import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

import static not.hub.headlessbot.Bot.CONFIG;

public class ReconnectModule extends Module {

    private static final ExpiringFlag cooldown = new ExpiringFlag(20, ChronoUnit.SECONDS, false); // TODO: reset after kick etc.
    private static boolean mcLoaded = false; // TODO: replace with check via something better

    public ReconnectModule() {
        super(Type.ALWAYS_ACTIVE);
    }

    @Override
    void onActivate() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mcLoaded) return;
                if (!LoginModule.isLoggedIn()) return;
                if (mc.getCurrentServerData() != null) return;
                if (cooldown.isValid()) return;
                else cooldown.reset();
                Log.info(name, "Connecting to " + CONFIG.hostname);
                mc.addScheduledTask(() -> NetworkUtil.INSTANCE.connect(CONFIG.hostname));
            }
        }, 1000, 1000);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        mcLoaded = true;
    }

}
