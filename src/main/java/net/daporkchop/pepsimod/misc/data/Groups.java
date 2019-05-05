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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * @author DaPorkchop_
 */
public class Groups extends PepsiConstants implements AutoCloseable {
    protected Map<UUID, Group> playerToGroup = Collections.emptyMap();
    protected Collection<Group> groups = Collections.emptyList();

    public void addGroup(Group group) {
        if (this.groups.isEmpty()) {
            this.groups = new HashSet<>();
        }
        this.groups.add(group);
        group.members.forEach(uuid -> this.addPlayerMapping(uuid, group));
    }

    public void addPlayerMapping(UUID uuid, Group group) {
        if (this.playerToGroup.isEmpty()) {
            this.playerToGroup = new HashMap<>();
        }
        this.playerToGroup.put(uuid, group);
    }

    @Override
    public void close() {
        this.groups.forEach(Group::close);

        this.playerToGroup.clear();
        this.groups.clear();
    }
}
