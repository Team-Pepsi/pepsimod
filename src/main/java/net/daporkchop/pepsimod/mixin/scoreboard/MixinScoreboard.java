/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
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

package net.daporkchop.pepsimod.mixin.scoreboard;

import com.google.common.collect.Maps;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(Scoreboard.class)
public abstract class MixinScoreboard {
    @Shadow
    private final Map<String, ScorePlayerTeam> teamMemberships = Maps.<String, ScorePlayerTeam>newHashMap();

    @Overwrite
    public void removePlayerFromTeam(String username, ScorePlayerTeam playerTeam) {
        try {
            if (this.getPlayersTeam(username) != playerTeam) {
                throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + playerTeam.getName() + "'.");
            } else {
                this.teamMemberships.remove(username);
                playerTeam.getMembershipCollection().remove(username);
            }
        } catch (NullPointerException e) {

        }
    }

    @Shadow
    public ScorePlayerTeam getPlayersTeam(String username) {
        return null;
    }
}
