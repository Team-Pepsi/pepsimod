
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

package net.daporkchop.pepsimod.wdl;

import net.daporkchop.pepsimod.wdl.api.IMessageTypeAdder;
import net.daporkchop.pepsimod.wdl.api.IWDLMessageType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

/**
 * Enum containing WDL's default {@link IWDLMessageType}s.
 * <p>
 * <b>Mostly intended for internal use.</b> Extensions may use {@link #INFO} and
 * {@link #ERROR}, but if they need something more complex, they should
 * implement {@link IMessageTypeAdder} and create new ones with that unless
 * it's a perfect fit.
 */
public enum WDLMessageTypes implements IWDLMessageType {
    INFO("net.daporkchop.pepsimod.wdl.messages.message.info", TextFormatting.RED,
            TextFormatting.GOLD, true, MessageTypeCategory.CORE_RECOMMENDED),
    ERROR("net.daporkchop.pepsimod.wdl.messages.message.error", TextFormatting.DARK_GREEN,
            TextFormatting.DARK_RED, true,
            MessageTypeCategory.CORE_RECOMMENDED),
    UPDATES("net.daporkchop.pepsimod.wdl.messages.message.updates", TextFormatting.RED,
            TextFormatting.GOLD, true, MessageTypeCategory.CORE_RECOMMENDED),
    LOAD_TILE_ENTITY("net.daporkchop.pepsimod.wdl.messages.message.loadingTileEntity", false),
    ON_WORLD_LOAD("net.daporkchop.pepsimod.wdl.messages.message.onWorldLoad", false),
    ON_BLOCK_EVENT("net.daporkchop.pepsimod.wdl.messages.message.blockEvent", true),
    ON_MAP_SAVED("net.daporkchop.pepsimod.wdl.messages.message.mapDataSaved", false),
    ON_CHUNK_NO_LONGER_NEEDED("net.daporkchop.pepsimod.wdl.messages.message.chunkUnloaded", false),
    ON_GUI_CLOSED_INFO("net.daporkchop.pepsimod.wdl.messages.message.guiClosedInfo", true),
    ON_GUI_CLOSED_WARNING("net.daporkchop.pepsimod.wdl.messages.message.guiClosedWarning", true),
    SAVING("net.daporkchop.pepsimod.wdl.messages.message.saving", true),
    REMOVE_ENTITY("net.daporkchop.pepsimod.wdl.messages.message.removeEntity", false),
    PLUGIN_CHANNEL_MESSAGE("net.daporkchop.pepsimod.wdl.messages.message.pluginChannel", false),
    UPDATE_DEBUG("net.daporkchop.pepsimod.wdl.messages.message.updateDebug", false);

    /**
     * I18n key for the text to display on a button for this enum value.
     */
    private final String displayTextKey;
    /**
     * Format code for the '[WorldDL]' label.
     */
    private final TextFormatting titleColor;
    /**
     * Format code for the text after the label.
     */
    private final TextFormatting textColor;
    /**
     * I18n key for the description text.
     */
    private final String descriptionKey;
    /**
     * Whether this type of message is enabled by default.
     */
    private final boolean enabledByDefault;

    /**
     * Constructor with the default values for a debug message.
     */
    WDLMessageTypes(String i18nKey,
                    boolean enabledByDefault) {
        this(i18nKey, TextFormatting.DARK_GREEN,
                TextFormatting.GOLD, enabledByDefault,
                MessageTypeCategory.CORE_DEBUG);
    }

    /**
     * Constructor that allows specification of all values.
     */
    WDLMessageTypes(String i18nKey, TextFormatting titleColor,
                    TextFormatting textColor, boolean enabledByDefault,
                    MessageTypeCategory category) {
        this.displayTextKey = i18nKey + ".text";
        this.titleColor = titleColor;
        this.textColor = textColor;
        this.descriptionKey = i18nKey + ".description";
        this.enabledByDefault = enabledByDefault;

        WDLMessages.registerMessage(this.name(), this, category);
    }

    @Override
    public String getDisplayName() {
        return I18n.format(displayTextKey);
    }

    @Override
    public TextFormatting getTitleColor() {
        return titleColor;
    }

    @Override
    public TextFormatting getTextColor() {
        return textColor;
    }

    @Override
    public String getDescription() {
        return I18n.format(descriptionKey);
    }

    @Override
    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }
}