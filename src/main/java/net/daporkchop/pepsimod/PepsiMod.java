package net.daporkchop.pepsimod;

import net.daporkchop.pepsimod.command.CommandRegistry;
import net.daporkchop.pepsimod.command.impl.*;
import net.daporkchop.pepsimod.event.GuiRenderHandler;
import net.daporkchop.pepsimod.key.KeyRegistry;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.*;
import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.daporkchop.pepsimod.module.impl.combat.AuraMod;
import net.daporkchop.pepsimod.module.impl.combat.CriticalsMod;
import net.daporkchop.pepsimod.module.impl.misc.AntiHungerMod;
import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.daporkchop.pepsimod.module.impl.misc.NoFallMod;
import net.daporkchop.pepsimod.module.impl.misc.TimerMod;
import net.daporkchop.pepsimod.module.impl.movement.VelocityMod;
import net.daporkchop.pepsimod.module.impl.render.*;
import net.daporkchop.pepsimod.util.Friend;
import net.daporkchop.pepsimod.util.Friends;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.datatag.DataTag;
import net.daporkchop.pepsimod.util.module.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

@Mod(name = "PepsiMod", modid = "pepsimod", version = PepsiMod.VERSION)
public class PepsiMod {
    public static final String VERSION = "11.1";
    public static final String chatPrefix = PepsiUtils.COLOR_ESCAPE + "0" + PepsiUtils.COLOR_ESCAPE + "l[" + PepsiUtils.COLOR_ESCAPE + "c" + PepsiUtils.COLOR_ESCAPE + "lpepsi" + PepsiUtils.COLOR_ESCAPE + "9" + PepsiUtils.COLOR_ESCAPE + "lmod" + PepsiUtils.COLOR_ESCAPE + "0" + PepsiUtils.COLOR_ESCAPE + "l]" + PepsiUtils.COLOR_ESCAPE + "r ";
    public static PepsiMod INSTANCE;
    public boolean isMcLeaksAccount = false;
    public Session originalSession = null;
    public Minecraft mc;
    public DataTag dataTag = null;
    public boolean hasInitializedModules = false;
    public TargetSettings targetSettings;
    public ESPSettings espSettings;
    public NoWeatherSettings noWeatherSettings;
    public TracerSettings tracerSettings;

    public static void registerModules(FMLStateEvent event) {
        ModuleManager.registerModule(new NoFallMod(false, -1, false));
        ModuleManager.registerModule(new AntiHungerMod(false, -1, false));
        ModuleManager.registerModule(new FullbrightMod(false, -1, false));
        ModuleManager.registerModule(new CriticalsMod(false, -1, false));
        ModuleManager.registerModule(new AuraMod(false, -1, false));
        ModuleManager.registerModule(new VelocityMod(false, -1, false));
        ModuleManager.registerModule(new TimerMod(false, -1, false));
        ModuleManager.registerModule(new XrayMod(false, -1, false));
        ModuleManager.registerModule(new AntiBlindMod(false, -1, false));
        ModuleManager.registerModule(new StorageESPMod(false, -1, false));
        ModuleManager.registerModule(new FreecamMod(false, -1, false));
        ModuleManager.registerModule(new HealthTagsMod(false, -1, false));
        ModuleManager.registerModule(new NameTagsMod(false, -1, false));
        ModuleManager.registerModule(new NoHurtCamMod(false, -1, false));
        ModuleManager.registerModule(new NoOverlayMod(false, -1, false));
        ModuleManager.registerModule(new NoWeatherMod(false, -1, false));
        ModuleManager.registerModule(new AntiInvisibleMod(false, -1, false));
        ModuleManager.registerModule(new TrajectoriesMod(false, -1, false));
        ModuleManager.registerModule(new TracersMod(false, -1, false));
    }

    public static void registerCommands(FMLStateEvent event) {
        CommandRegistry.registerCommand(new HelpCommand());
        CommandRegistry.registerCommand(new SetRotCommand());
        CommandRegistry.registerCommand(new ToggleCommand());
        CommandRegistry.registerCommand(new SortModulesCommand());
        CommandRegistry.registerCommand(new SaveCommand());
        CommandRegistry.registerCommand(new LoadCommand());
        CommandRegistry.registerCommand(new ListCommand());
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
        INSTANCE = this;
        MinecraftForge.EVENT_BUS.register(new KeyRegistry());
        this.mc = Minecraft.getMinecraft();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        loadConfig();
        registerModules(event);
        registerCommands(event);
        initModules();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new GuiRenderHandler());
        PepsiUtils.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                PepsiMod.INSTANCE.saveConfig();
            }
        }, 360000, 360000);
    }

    public void loadConfig() {
        File file = new File(PepsiMod.getWorkingFolder(), "/pepsimod.dat");

        if (!file.exists()) {
            dataTag = new DataTag(file);
            for (Module module : ModuleManager.AVALIBLE_MODULES) {
                dataTag.setSerializableArray("settings" + module.nameFull, module.defaultOptions());
            }
            dataTag.setSerializable("friends", new HashMap<String, Friend>());
            dataTag.save();
        } else {
            dataTag = new DataTag(file);
        }

        Friends.FRIENDS = (HashMap<String, Friend>) dataTag.getSerializable("friends", new HashMap<String, Friend>());
        ModuleManager.sortType = (ModuleSortType) dataTag.getSerializable("sortType", ModuleSortType.SIZE);
        targetSettings = (TargetSettings) dataTag.getSerializable("targetSettings", new TargetSettings());
        XrayUtils.target_blocks = (ArrayList<Integer>) dataTag.getSerializable("xrayBlocks", new ArrayList<Integer>());
        espSettings = (ESPSettings) dataTag.getSerializable("espSettings", new ESPSettings());
        NameTagsMod.scale = dataTag.getFloat("NameTags_scale", 1.0f);
        FreecamMod.SPEED = dataTag.getFloat("Freecam_speed", 1.0f);
        noWeatherSettings = (NoWeatherSettings) dataTag.getSerializable("noweatherSettings", new NoWeatherSettings());
        tracerSettings = (TracerSettings) dataTag.getSerializable("tracerSettings", new TracerSettings());

        //save the tag in case new fields are added, this way they are saved right away
        dataTag.save();
        initModules();
    }

    public void saveConfig() {
        for (Module module : ModuleManager.AVALIBLE_MODULES) {
            ModuleOption[] options = new ModuleOption[module.options.length];
            for (int i = 0; i < options.length; i++) {
                ModuleOption option = module.options[i];
                if (option instanceof CustomOption) {
                    CustomOptionSave save = new CustomOptionSave((CustomOption) option);
                    options[i] = save;
                } else {
                    options[i] = option;
                }
            }
            dataTag.setSerializableArray("settings" + module.nameFull, options);
        }
        dataTag.setSerializable("friends", Friends.FRIENDS);
        dataTag.setSerializable("sortType", ModuleManager.sortType);
        dataTag.setSerializable("targetSettings", targetSettings);
        dataTag.setSerializable("xrayBlocks", XrayUtils.target_blocks);
        dataTag.setSerializable("espSettings", espSettings);
        dataTag.setFloat("Freecam_speed", FreecamMod.SPEED);
        dataTag.setFloat("NameTags_scale", NameTagsMod.scale);
        dataTag.setSerializable("noweatherSettings", noWeatherSettings);
        dataTag.setSerializable("tracerSettings", tracerSettings);
        dataTag.save();
    }

    public void initModules() {
        for (Module module : ModuleManager.AVALIBLE_MODULES) {
            ModuleOption[] defaultOptions = module.defaultOptions();
            module.options = (ModuleOption[]) dataTag.getSerializableArray("settings" + module.nameFull, defaultOptions);
            if (defaultOptions.length != module.options.length) {
                //TODO: remove by name, not by index
                if (defaultOptions.length > module.options.length) {
                    System.out.println("New options have been added to module: " + module.nameFull);
                    ArrayList<ModuleOption> tempList = new ArrayList<>();
                    for (ModuleOption option : module.options) {
                        tempList.add(option);
                    }
                    for (int i = tempList.size(); i < defaultOptions.length; i++) {
                        tempList.add(defaultOptions[i]);
                    }
                    module.options = tempList.toArray(new ModuleOption[tempList.size()]);
                } else {
                    System.out.println("Options have been removed from module: " + module.nameFull);
                    ArrayList<ModuleOption> tempList = new ArrayList<>();
                    for (int i = 0; i < defaultOptions.length; i++) {
                        tempList.add(module.options[i]);
                    }
                    module.options = tempList.toArray(new ModuleOption[tempList.size()]);
                }
            }
            for (int i = 0; i < module.options.length; i++) {
                ModuleOption option = module.options[i];
                if (option instanceof CustomOptionSave) {
                    CustomOptionSave save = (CustomOptionSave) option;
                    CustomOption defaultOption = (CustomOption) defaultOptions[i];
                    CustomOption customOption = new CustomOption(save, defaultOption.SET, defaultOption.GET);
                    module.options[i] = customOption;
                }
            }

            module.getOptionByName("enabled").setValue(Module.shouldBeEnabled((boolean) module.getOptionByName("enabled").getValue(), module.getLaunchState()));
            if (((OptionTypeBoolean) module.getOptionByName("enabled")).getValue()) {
                ModuleManager.enableModule(module);
            } else {
                ModuleManager.disableModule(module);
            }
            module.hide = ((OptionTypeBoolean) module.getOptionByName("hidden")).getValue();
        }
    }
}
