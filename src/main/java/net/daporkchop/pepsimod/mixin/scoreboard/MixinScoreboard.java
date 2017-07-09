package net.daporkchop.pepsimod.mixin.scoreboard;

import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(Scoreboard.class)
public class MixinScoreboard {
    @Shadow
    @Final
    private Map<String, ScorePlayerTeam> teamMemberships;

    @Shadow
    public ScorePlayerTeam getPlayersTeam(String username) {
        return null;
    }

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
            //seems to be caused in 2b2t's queue a lot, this just keeps the log nice and clean
        }
    }
}
