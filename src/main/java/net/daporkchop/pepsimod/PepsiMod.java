/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod;

import net.daporkchop.pepsimod.clickgui.ClickGUI;
import net.daporkchop.pepsimod.clickgui.Window;
import net.daporkchop.pepsimod.command.CommandRegistry;
import net.daporkchop.pepsimod.command.impl.*;
import net.daporkchop.pepsimod.event.GuiRenderHandler;
import net.daporkchop.pepsimod.gui.clickgui.WindowCombat;
import net.daporkchop.pepsimod.gui.clickgui.WindowMisc;
import net.daporkchop.pepsimod.gui.clickgui.WindowMovement;
import net.daporkchop.pepsimod.gui.clickgui.WindowRender;
import net.daporkchop.pepsimod.key.KeyRegistry;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.ModuleOptionSave;
import net.daporkchop.pepsimod.module.api.ModuleSortType;
import net.daporkchop.pepsimod.module.impl.combat.AuraMod;
import net.daporkchop.pepsimod.module.impl.combat.CriticalsMod;
import net.daporkchop.pepsimod.module.impl.misc.*;
import net.daporkchop.pepsimod.module.impl.movement.VelocityMod;
import net.daporkchop.pepsimod.module.impl.render.*;
import net.daporkchop.pepsimod.util.*;
import net.daporkchop.pepsimod.util.datatag.DataTag;
import net.daporkchop.pepsimod.util.module.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

@Mod(modid = "pepsimoddev", name = "pepsimod - Dev", version = "11.1-dev")
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
    public MiscOptions miscOptions;

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
        ModuleManager.registerModule(new ClickGuiMod(false, Keyboard.KEY_RSHIFT, false));
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
        event.getModLog().info("launching preInit");
        INSTANCE = this;
        MinecraftForge.EVENT_BUS.register(new KeyRegistry());
        event.getModLog().info("setting MC instance!");
        this.mc = Minecraft.getMinecraft();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new ClickGUI(); //TODO: fix
        loadConfig();
        registerModules(event);

        registerCommands(event);
        initModules();
        ClickGUI.INSTANCE.windows.add(new WindowRender());
        ClickGUI.INSTANCE.windows.add(new WindowCombat());
        ClickGUI.INSTANCE.windows.add(new WindowMisc());
        ClickGUI.INSTANCE.windows.add(new WindowMovement());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ReflectionStuff.init();
        if (ImageUtils.imgs == null) {
            ImageUtils.imgs = new HashMap<>();
            for (int i = 0; i < ImageUtils.names.length; i++) {
                String s = ImageUtils.names[i];
                ImageUtils.imgs.put(i, new ResourceLocation("pepsimod", "textures/" + s));
            }
        }
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

        for (Window window : ClickGUI.INSTANCE.windows) {
            int[] data = dataTag.getIntegerArray(window.text + "¦window", new int[]{window.getX(), window.getY(), 0});

            window.setX(data[0]);
            window.setY(data[1]);
            window.isOpen = (data[2] == 1);
        }

        miscOptions = (MiscOptions) dataTag.getSerializable("miscOptions", new MiscOptions());

        //save the tag in case new fields are added, this way they are saved right away
        dataTag.save();
        initModules();
    }

    public void saveConfig() {
        for (Module module : ModuleManager.AVALIBLE_MODULES) {
            ModuleOptionSave[] options = new ModuleOptionSave[module.options.length];
            for (int i = 0; i < options.length; i++) {
                ModuleOption option = module.options[i];
                ModuleOptionSave save = new ModuleOptionSave(option, option.displayName);
                options[i] = save;
            }
            dataTag.setSerializableArray("settings" + module.nameFull, options);
        }
        for (Window window : ClickGUI.INSTANCE.windows) {
            dataTag.setIntegerArray(window.text + "¦window", new int[]{window.getX(), window.getY(), window.isOpen ? 1 : 0});
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
        dataTag.setSerializable("miscOptions", miscOptions);
        dataTag.save();
    }

    public void initModules() {
        for (Module module : ModuleManager.AVALIBLE_MODULES) {
            ModuleOption[] defaultOptions = module.defaultOptions();
            ModuleOptionSave[] backup = new ModuleOptionSave[defaultOptions.length];
            for (int i = 0; i < defaultOptions.length; i++) {
                backup[i] = new ModuleOptionSave(defaultOptions[i], defaultOptions[i].getName(), defaultOptions[i].getDefaultValue());
            }
            module.tempOptionLoading = (ModuleOptionSave[]) dataTag.getSerializableArray("settings" + module.nameFull, backup);
            module.options = new ModuleOption[module.tempOptionLoading.length];
            for (int i = 0; i < module.tempOptionLoading.length; i++) {
                module.options[i] = new ModuleOption(module.tempOptionLoading[i], defaultOptions[i].SET, defaultOptions[i].GET, defaultOptions[i].displayName, defaultOptions[i].extended, defaultOptions[i].makeButton);
            }
            module.tempOptionLoading = null;

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

            module.getOptionByName("enabled")
                    .setValue(Module.shouldBeEnabled(
                            (boolean) module.getOptionByName("enabled").getValue(),
                            module.getLaunchState()));
            if (((boolean) module.getOptionByName("enabled").getValue())) {
                ModuleManager.enableModule(module);
            } else {
                ModuleManager.disableModule(module);
            }
            module.hide = ((boolean) module.getOptionByName("hidden").getValue());
        }
    }
}
