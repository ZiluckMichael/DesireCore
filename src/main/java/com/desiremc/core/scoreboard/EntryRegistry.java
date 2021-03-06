package com.desiremc.core.scoreboard;

import com.desiremc.core.scoreboard.common.EntryBuilder;
import com.desiremc.core.scoreboard.type.Entry;
import com.desiremc.core.scoreboard.type.Scoreboard;
import com.desiremc.core.scoreboard.type.ScoreboardHandler;
import com.desiremc.core.scoreboard.type.SimpleScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class EntryRegistry implements ScoreboardHandler
{

    private static final boolean DEBUG = false;

    private static EntryRegistry instance;

    private HashMap<Player, PlayerEntry> entries = new HashMap<>();

    //private Pattern pat = Pattern.compile(".*\\{[a-zA-Z]+\\}.*");

    @Override
    public String getTitle(Player player)
    {
        return "&b&lDesireHCF";
    }

    @Override
    public List<Entry> getEntries(Player player)
    {
        PlayerEntry entry = getEntry(player);
        if (entry == null)
        {
            return new LinkedList<>();
        }

        return EntryBuilder.build(entry);
    }

    @Override
    public boolean hasEntries(Player player)
    {
        PlayerEntry entry = entries.get(player);

        if (entry == null)
        {
            return false;
        }
        return entry.hasEntries();
    }

    public PlayerEntry getEntry(Player player)
    {
        return entries.get(player);
    }

    /**
     * Set the value on a player's scoreboard.
     * 
     * @param player the player to target.
     * @param key the key to store as reference.
     * @param value the string displayed on the scoreboard.
     */
    public void setValue(Player player, String key, String value)
    {
        PlayerEntry entry = getEntry(player);
        if (entry == null)
        {
            entry = new PlayerEntry();
            entries.put(player, entry);
            Scoreboard board = new SimpleScoreboard(player).setHandler(instance).setUpdateInterval(2l);
            board.activate();
        }
        if (DEBUG)
        {
            System.out.println(key + " " + value);
        }
        entry.setEntry(key, value);
    }

    /**
     * Set the value on all player's scoreboard.
     * 
     * @param key the key to store as reference.
     * @param value the string displayed on the scoreboard.
     */
    public void setAll(String key, String value)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            setValue(player, key, value);
        }
    }

    /**
     * Clear a value on a player's scoreboard.
     * 
     * @param player the player to target.
     * @param key the key used as a reference.
     */
    public void removeValue(Player player, String key)
    {
        PlayerEntry entry = getEntry(player);
        if (entry != null)
        {
            entry.clearEntry(key);

            if (!entry.hasEntries())
            {
                ScoreboardRegistry.getInstance().clearScoreboard(player);
            }
        }
    }

    /**
     * Clear a value on all player's scoreboard.
     * 
     * @param key the key used as a reference.
     */
    public void removeAll(String key)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            removeValue(player, key);
        }
    }

    public static void initialize()
    {
        instance = new EntryRegistry();
    }

    public static EntryRegistry getInstance()
    {
        return instance;
    }

    public static class PlayerEntry
    {

        private LinkedHashMap<String, String> entries;

        public PlayerEntry()
        {
            this.entries = new LinkedHashMap<>();
        }

        public String getEntry(String key)
        {
            return entries.get(key);
        }

        public void setEntry(String key, String value)
        {
            entries.put(key, value);
        }

        public void clearEntry(String key)
        {
            entries.remove(key);
        }

        public Collection<String> getEntries()
        {
            return entries.values();
        }

        public HashMap<String, String> getEntryMap()
        {
            return entries;
        }

        public boolean hasEntries()
        {
            return entries.size() > 0;
        }
    }

}
