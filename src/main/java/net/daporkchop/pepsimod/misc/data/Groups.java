/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.misc.data;

import net.daporkchop.pepsimod.util.PepsiConstants;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author DaPorkchop_
 */
public class Groups extends PepsiConstants {
    public Map<UUID, Group> playerToGroup = Collections.emptyMap();
    public Collection<Group> groups = Collections.emptyList();

    public void cleanup()   {
        this.groups.forEach(Group::cleanup);
    }

    public static class Group   {
        public String id = "";
        public String name = "";
        public Set<UUID> members = Collections.emptySet();
        public ResourceLocation cape;
        public ResourceLocation icon;

        public void cleanup()   {
            if (this.cape != null)  {
                mc.getTextureManager().deleteTexture(this.cape);
                this.cape = null;
            }
            if (this.icon != null)  {
                mc.getTextureManager().deleteTexture(this.icon);
                this.icon = null;
            }
        }
    }
}
