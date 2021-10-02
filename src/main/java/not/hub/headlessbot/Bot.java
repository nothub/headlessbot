package not.hub.headlessbot;

import net.minecraft.client.gui.GuiDisconnected;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import not.hub.headlessbot.fsm.behaviour.Controller;
import not.hub.headlessbot.fsm.behaviour.StartupFsm;
import not.hub.headlessbot.util.Webhook;


@Mod(modid = Bot.MODID, name = Bot.MODID, version = Bot.VERSION, clientSideOnly = true, acceptableRemoteVersions = "*")
public class Bot {

    public static final String MODID = "headlessbot";
    public static final String VERSION = "0.0.0-SNAPSHOT";
    public static final Config CONFIG = Config.load();

    public static final Webhook WEBHOOK = new Webhook(CONFIG.webhook);
    public static final Controller CONTROLLER = new Controller();

    public static StartupFsm FSM_STARTUP; // we have to init this with fml init, not before!

    private static boolean shutdown = false;

    public static boolean isShutdown() {
        return shutdown;
    }

    public static void setShutdown(boolean shutdown) {
        Bot.shutdown = shutdown;
    }

    @Mod.EventHandler
    public void fmlInit(FMLPreInitializationEvent event) {
        Log.info("FML init state", "PRE_INIT");
        Log.setLogger(event.getModLog());
    }

    @Mod.EventHandler
    public void fmlInit(FMLInitializationEvent event) {
        Log.info("FML init state", "INIT");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void fmlInit(FMLPostInitializationEvent event) {
        Log.info("FML init state", "POST_INIT");

        FSM_STARTUP = new StartupFsm();

        if (FSM_STARTUP.current() == StartupFsm.State.START) FSM_STARTUP.transition(true);
        else throw new IllegalStateException("Invalid fsm transition source state" + FSM_STARTUP.current().name());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (FSM_STARTUP.current() == StartupFsm.State.INIT_BOT) FSM_STARTUP.transition(true);
        else throw new IllegalStateException("Invalid fsm transition source state" + FSM_STARTUP.current().name());
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGuiDisconnected(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.getGui() instanceof GuiDisconnected)) return;
        FSM_STARTUP.transition(false);
    }

}
