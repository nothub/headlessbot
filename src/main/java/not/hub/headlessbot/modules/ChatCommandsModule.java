package not.hub.headlessbot.modules;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import not.hub.headlessbot.Log;

public class ChatCommandsModule extends Module {

    public ChatCommandsModule() {
        super(Type.ALWAYS_ACTIVE);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent ev) {
        Log.info(getClass(), ev.getMessage().getUnformattedText());
    }

}
