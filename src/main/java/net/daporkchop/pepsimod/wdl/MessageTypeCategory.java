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

import net.daporkchop.pepsimod.wdl.api.IWDLMessageType;
import net.minecraft.client.resources.I18n;

/**
 * Category / collection of {@link IWDLMessageType}s.
 */
public abstract class MessageTypeCategory {
    /**
     * Core recommended category.
     * <p>
     * Put in here instead of {@link WDLMessageTypes} because of field load
     * orders.
     */
    static final MessageTypeCategory CORE_RECOMMENDED =
            new MessageTypeCategory.I18nableMessageTypeCategory("CORE_RECOMMENDED",
                    "net.daporkchop.pepsimod.wdl.messages.category.core_recommended");
    /**
     * Core recommended category.
     * <p>
     * Put in here instead of {@link WDLMessageTypes} because of field load
     * orders.
     */
    static final MessageTypeCategory CORE_DEBUG =
            new MessageTypeCategory.I18nableMessageTypeCategory("CORE_DEBUG",
                    "net.daporkchop.pepsimod.wdl.messages.category.core_debug");
    /**
     * The internal name.
     * <p>
     * Used when saving.
     */
    public final String internalName;

    public MessageTypeCategory(String internalName) {
        this.internalName = internalName;
    }

    /**
     * Gets the user-facing display name.
     */
    public abstract String getDisplayName();

    @Override
    public String toString() {
        return "MessageTypeCategory [internalName=" + internalName
                + ", displayName=" + getDisplayName() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((internalName == null) ? 0 : internalName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MessageTypeCategory other = (MessageTypeCategory) obj;
        if (internalName == null) {
            if (other.internalName != null) {
                return false;
            }
        } else if (!internalName.equals(other.internalName)) {
            return false;
        }
        return true;
    }

    /**
     * Simple {@link MessageTypeCategory} that gets the display name from
     * an internationalization key.
     */
    public static class I18nableMessageTypeCategory extends MessageTypeCategory {
        public final String i18nKey;

        public I18nableMessageTypeCategory(String internalName, String i18nKey) {
            super(internalName);
            this.i18nKey = i18nKey;
        }

        @Override
        public String getDisplayName() {
            return I18n.format(i18nKey);
        }

    }
}
