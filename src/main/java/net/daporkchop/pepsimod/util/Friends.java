package net.daporkchop.pepsimod.util;

import net.minecraft.entity.player.EntityPlayerMP;

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

    /**
     * Adds a friend from a player
     *
     * @param uuid the player to add
     * @param name the name of the player
     * @return a friend because you don't have any
     */
    public static Friend addFriend(String uuid, String name) {
        Friend friend = new Friend(uuid, name);
        FRIENDS.put(friend.UUID, friend);
        return friend;
    }

    /**
     * Updates a friend's name based on UUID
     *
     * @param uuid the player to update
     * @param name the new name
     * @return the updated friend
     */
    public static Friend updateFriendName(String uuid, String name) {
        Friend friend = FRIENDS.getOrDefault(uuid, null);
        if (friend == null) {
            return addFriend(uuid, name);
        } else {
            friend.lastKnownName = name;
            FRIENDS.put(friend.UUID, friend);
            return friend;
        }
    }

    /**
     * Removes a friend
     *
     * @param uuid the player to remove as a friend
     * @return the removed friend
     */
    public static Friend removeFriend(String uuid) {
        return FRIENDS.remove(uuid);
    }

    /**
     * checks if a player is a friend
     *
     * @param uuid the player to check
     * @return whether or not the player is a friend
     */
    public static boolean isFriend(String uuid) {
        return FRIENDS.containsKey(uuid);
    }
}
