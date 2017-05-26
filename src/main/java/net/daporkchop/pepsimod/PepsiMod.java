package net.daporkchop.pepsimod;

import net.daporkchop.pepsimod.command.CommandRegistry;
import net.daporkchop.pepsimod.command.impl.Help;
import net.daporkchop.pepsimod.command.impl.SetRot;
import net.daporkchop.pepsimod.command.impl.SortModules;
import net.daporkchop.pepsimod.command.impl.Toggle;
import net.daporkchop.pepsimod.event.GuiRenderHandler;
import net.daporkchop.pepsimod.key.KeyRegistry;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.daporkchop.pepsimod.module.impl.AntiHunger;
import net.daporkchop.pepsimod.module.impl.Fullbright;
import net.daporkchop.pepsimod.module.impl.NoFall;
import net.daporkchop.pepsimod.util.Friend;
import net.daporkchop.pepsimod.util.Friends;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.datatag.DataTag;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;

import java.io.File;
import java.util.HashMap;
import java.util.TimerTask;

@Mod(name = "PepsiMod", modid = "pepsimod", version = PepsiMod.VERSION)
public class PepsiMod {
    public static final String VERSION = "11.0";
    public static final String chatPrefix = PepsiUtils.COLOR_ESCAPE + "0" + PepsiUtils.COLOR_ESCAPE + "l[" + PepsiUtils.COLOR_ESCAPE + "c" + PepsiUtils.COLOR_ESCAPE + "lpepsi" + PepsiUtils.COLOR_ESCAPE + "9" + PepsiUtils.COLOR_ESCAPE + "lmod" + PepsiUtils.COLOR_ESCAPE + "0" + PepsiUtils.COLOR_ESCAPE + "l]" + PepsiUtils.COLOR_ESCAPE + "r ";
    public static PepsiMod INSTANCE;
    public boolean isMcLeaksAccount = false;
    public Session originalSession = null;
    public Minecraft mc;
    public DataTag dataTag = null;
    public boolean hasInitializedModules = false;

    {
        INSTANCE = this;
    }

    public static void registerModules(FMLStateEvent event) {
        //TODO: save enabled status
        //TODO: save hidden status
        //i think i got keybinds, still need to test
        ModuleManager.registerModule(new NoFall(false, -1, false));
        ModuleManager.registerModule(new AntiHunger(false, -1, false));
        ModuleManager.registerModule(new Fullbright(false, -1, false));
    }

    public static void registerCommands(FMLStateEvent event) {
        CommandRegistry.registerCommand(new Help());
        CommandRegistry.registerCommand(new SetRot());
        CommandRegistry.registerCommand(new Toggle());
        CommandRegistry.registerCommand(new SortModules());
    }

    /**
     * Probably unneeded, as pepsimod is client-only
     *
     * @return a java.io.File with the .minecraft directory
     */
    public static File getWorkingFolder() {
        File toBeReturned;
        try {
            if (FMLCommonHandler.instance().getSide().isClient()) {
                toBeReturned = Minecraft.getMinecraft().mcDataDir;
            } else {
                toBeReturned = FMLCommonHandler.instance().getMinecraftServerInstance().getFile("");
            }
            return toBeReturned;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyRegistry());
        this.mc = Minecraft.getMinecraft();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        registerModules(event);
        registerCommands(event);
        loadConfig();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new GuiRenderHandler());
        PepsiUtils.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                PepsiMod.INSTANCE.saveConfig();
            }
        }, 300000, 300000);
    }

    public void loadConfig() {
        File file = new File(PepsiMod.getWorkingFolder(), "/pepsimod.dat");

        if (!file.exists()) {
            dataTag = new DataTag(file);
            for (Module module : ModuleManager.AVALIBLE_MODULES) {
                dataTag.setSerializableArray("settings" + module.name, module.defaultOptions());
            }
            dataTag.setSerializable("friends", new HashMap<String, Friend>());
            dataTag.save();
        } else {
            dataTag = new DataTag(file);
        }

        for (Module module : ModuleManager.AVALIBLE_MODULES) {
            module.options = (ModuleOption[]) dataTag.getSerializableArray("settings" + module.name, module.defaultOptions());
            if (((OptionTypeBoolean) module.getOptionByName("enabled")).getValue()) {
                ModuleManager.enableModule(module);
            }
            module.hide = ((OptionTypeBoolean) module.getOptionByName("hidden")).getValue();
        }
        Friends.FRIENDS = (HashMap<String, Friend>) dataTag.getSerializable("friends", new HashMap<String, Friend>());

        //save the tag in case new fields are added, this way they are saved right away
        dataTag.save();
    }

    public void saveConfig() {
        for (Module module : ModuleManager.AVALIBLE_MODULES) {
            dataTag.setSerializableArray("settings" + module.name, module.options);
        }
        dataTag.setSerializable("friends", Friends.FRIENDS);
        dataTag.save();
    }
}
