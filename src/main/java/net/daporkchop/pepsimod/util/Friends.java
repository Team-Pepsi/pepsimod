package net.daporkchop.pepsimod.util;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.HashMap;

public class Friends {
    /**
     * Friends are stored here
     * kek
     */
    public static HashMap<String, Friend> FRIENDS = null;

    /**
     * Adds a friend from a player
     * @param player the player to add
     * @return a friend because you don't have any
     */
    public static Friend addFriend(EntityPlayerMP player)   {
        Friend friend = new Friend(player.getGameProfile().getId().toString(), player.getName());
        FRIENDS.put(friend.UUID, friend);
        return friend;
    }

    /**
     * Updates a friend's name based on UUID
     * @param player the player to update
     * @return the updated friend
     */
    public static Friend updateFriendName(EntityPlayerMP player)    {
        Friend friend = FRIENDS.getOrDefault(player.getGameProfile().getId().toString(), null);
        if (friend == null) {
            return addFriend(player);
        } else {
            friend.lastKnownName = player.getName();
            FRIENDS.put(friend.UUID, friend);
            return friend;
        }
    }

    /**
     * Removes a friend
     * @param player the player to remove as a friend
     * @return the removed friend
     */
    public static Friend removeFriend(EntityPlayerMP player)    {
        return FRIENDS.remove(player.getGameProfile().getId().toString());
    }

    /**
     * checks if a player is a friend
     * @param player the player to check
     * @return whether or not the player is a friend
     */
    public static boolean isFriend(EntityPlayerMP player)   {
        return FRIENDS.containsKey(player.getGameProfile().getId().toString());
    }
}
