package not.hub.headlessbot;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import not.hub.headlessbot.behavior.FSM;


@Mod(modid = Bot.MODID, name = Bot.MODID, version = Bot.VERSION, clientSideOnly = true, acceptableRemoteVersions = "*")
public class Bot {

    public static final String MODID = "headlessbot";
    public static final String VERSION = "0.0.0-SNAPSHOT";
    public static final Config CONFIG = Config.load();

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
        FSM.transition(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (FSM.getCurrent() == FSM.State.INIT_BOT) FSM.transition(true);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

}
