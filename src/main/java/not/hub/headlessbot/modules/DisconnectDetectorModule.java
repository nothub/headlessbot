package not.hub.headlessbot.modules;

import net.minecraft.client.gui.GuiDisconnected;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import not.hub.headlessbot.Bot;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.fsm.StartupFsm;

// TODO: why is this a module? we also need this logic while in queue...
public class DisconnectDetectorModule extends Module {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.getGui() instanceof GuiDisconnected)) return;
        Log.info(getClass(), "Server connection lost...");
        if (Bot.FSM_STARTUP.current() == StartupFsm.State.ACTIVE
            || Bot.FSM_STARTUP.current() == StartupFsm.State.QUEUE)
            Bot.FSM_STARTUP.transition(false);
        else throw new IllegalStateException("Invalid fsm transition source state" + Bot.FSM_STARTUP.current().name());
    }

}
