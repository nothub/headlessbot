package not.hub.headlessbot.modules;

import net.minecraft.client.gui.GuiDisconnected;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.fsm.Startup;

// TODO: why is this a module?
public class DisconnectDetectorModule extends Module {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.getGui() instanceof GuiDisconnected)) return;
        Log.info(getClass(), "Server connection lost...");
        if (Startup.getCurrent() == Startup.State.ACTIVE || Startup.getCurrent() == Startup.State.QUEUE) Startup.transition(false);
        else throw new IllegalStateException("Invalid fsm transition source state" + Startup.getCurrent().name());
    }

}
