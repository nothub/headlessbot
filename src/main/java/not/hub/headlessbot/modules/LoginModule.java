package not.hub.headlessbot.modules;

import cc.neckbeard.utils.ExpiringFlag;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.earth.headlessforge.util.NetworkUtil;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.mixins.IMinecraft;

import java.net.Proxy;
import java.time.temporal.ChronoUnit;

import static not.hub.headlessbot.Bot.CONFIG;

public class LoginModule extends Module {

    // TODO: split login and server connection
    // TODO: save session to file

    private static final ExpiringFlag cooldown = new ExpiringFlag(5, ChronoUnit.MINUTES, false);

    public LoginModule() {
        super(Type.SITUATIONAL);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        deactivate();
        login();
        connect();
    }

    private void login() {
        if (cooldown.isValid()) {
            Log.warn(name, "I am on cooldown!");
            return;
        } else cooldown.reset();
        if (mc.getSession().getUsername().equals(CONFIG.username)) {
            Log.warn(name, "I am already logged in!");
            return;
        }
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(CONFIG.username);
        auth.setPassword(CONFIG.password);
        try {
            auth.logIn();
            final Session session = new Session(
                auth.getSelectedProfile().getName(),
                auth.getSelectedProfile().getId().toString(),
                auth.getAuthenticatedToken(), "mojang");
            ((IMinecraft) mc).session(session);
            Log.info(name, "Success! Username is " + mc.getSession().getUsername());
        } catch (AuthenticationUnavailableException e) {
            Log.error(name, "Error! Authentication servers are unavailable (ratelimit/blacklist)! " + e.getMessage());
            FMLCommonHandler.instance().exitJava(1, false);
        } catch (AuthenticationException e) {
            Log.error(name, "Error! " + e.getMessage());
            FMLCommonHandler.instance().exitJava(1, false);
        }
    }

    private void connect() {
        Log.info(name, CONFIG.hostname);
        NetworkUtil.INSTANCE.connect(CONFIG.hostname);
    }

}
