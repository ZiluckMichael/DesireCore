package com.desiremc.core.commands.punishment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.utils.StringUtils;

/**
 * Command used to display the player's basic information values.
 *
 * @author Christian Tooley
 * @since 12/14/2017
 */
public class HistoryCommand extends ValidCommand
{

    private static HashMap<UUID, Integer> pages = new HashMap<>();

    public HistoryCommand()
    {
        super("history", "Get punishment information about a player.", Rank.HELPER);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();

        openPunishmentGUI(sender.getPlayer(), target);
    }

    private void openPunishmentGUI(Player p, Session target)
    {
        Inventory inv = Bukkit.createInventory(null, 54, DesireCore.getLangHandler().renderMessage("history.inventory.title", false, false, "{player}", target.getName()));

        List<Punishment> punishments = PunishmentHandler.getInstance().getPunishments(target.getUniqueId());

        boolean next = true;
        int itemsPerPage = 45;
        int startingIndex = (pages.getOrDefault(p.getUniqueId(), 1) - 1) * itemsPerPage;
        int endingIndex = startingIndex + itemsPerPage;

        if (endingIndex > punishments.size())
        {
            endingIndex = punishments.size();
            next = false;
        }

        for (Punishment punishment : punishments.subList(startingIndex, endingIndex))
        {
            String issuer = punishment.getIssuer().toString().equalsIgnoreCase(DesireCore.getConsoleUUID().toString()) ? "Console" : SessionHandler.getGeneralSession(punishment.getIssuer()).getName();
            String reason = punishment.getReason();
            String type = StringUtils.capitalize(punishment.getType().name().toLowerCase().replace("_", " "));
            String repealed = BooleanUtils.toString(punishment.isRepealed(), "true", "false");
            String permanent = BooleanUtils.toString(punishment.isPermanent(), "true", "false");
            long time = punishment.getIssued();

            ItemStack item = new ItemStack(Material.PAPER);

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(DesireCore.getLangHandler().renderMessage("history.inventory.item.name", false, false, "{type}", type));

            List<String> lore = new ArrayList<>();

            for (String loreString : DesireCore.getLangHandler().getStringList("history.inventory.item.lore"))
            {
                lore.add(DesireCore.getLangHandler().renderString(loreString, "{issuer}", issuer, "{date}",
                        DateUtils.formatDateDiff(time), "{reason}", reason, "{repealed}", repealed, "{permanent}", permanent));
            }

            meta.setLore(lore);

            item.setItemMeta(meta);
            inv.addItem(item);
        }

        if (next)
        {
            ItemStack nextItem = new ItemStack(Material.matchMaterial(DesireCore.getLangHandler().getString("history.inventory.next.item")));
            ItemMeta nextMeta = nextItem.getItemMeta();

            nextMeta.setDisplayName(DesireCore.getLangHandler().renderString("history.inventory.next.name"));

            List<String> lore = new ArrayList<>();

            for (String loreString : DesireCore.getLangHandler().getStringList("history.inventory.next.lore"))
            {
                lore.add(DesireCore.getLangHandler().renderString(loreString));
            }

            nextMeta.setLore(lore);

            nextItem.setItemMeta(nextMeta);
            inv.setItem(53, nextItem);
        }

        if (pages.getOrDefault(p.getUniqueId(), 1) != 1)
        {
            ItemStack nextItem = new ItemStack(Material.matchMaterial(DesireCore.getLangHandler().getString("history.inventory.back.item")));
            ItemMeta nextMeta = nextItem.getItemMeta();

            nextMeta.setDisplayName(DesireCore.getLangHandler().renderString("history.inventory.back.name"));

            List<String> lore = new ArrayList<>();

            for (String loreString : DesireCore.getLangHandler().getStringList("history.inventory.back.lore"))
            {
                lore.add(DesireCore.getLangHandler().renderString(loreString));
            }

            nextMeta.setLore(lore);

            nextItem.setItemMeta(nextMeta);
            inv.setItem(45, nextItem);
        }

        p.openInventory(inv);
    }

    public static void addPage(UUID uuid)
    {
        pages.put(uuid, pages.get(uuid) + 1);
    }

    public static void minusPage(UUID uuid)
    {
        pages.put(uuid, pages.get(uuid) - 1);
    }
}
