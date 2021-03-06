package com.desiremc.core.commands.punishment;

import java.util.List;

import org.bukkit.Bukkit;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.SenderNotTargetValidator;
import com.desiremc.core.validators.SenderOutranksTargetValidator;

public class BlacklistCommand extends ValidCommand
{

    public BlacklistCommand()
    {
        super("blacklist", "Blacklist a user from the server.", Rank.ADMIN);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .addValidator(new SenderNotTargetValidator())
                .addValidator(new SenderOutranksTargetValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("reason")
                .setParser(new StringParser())
                .setVariableLength()
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();
        String reason = (String) args.get(1).getValue();

        if (reason.contains("-s"))
        {
            reason = reason.replace("-s", "");
            DesireCore.getLangHandler().sendRenderMessage(sender, "blacklist.blacklist_message", true, false,
                    "{player}", sender.getName(),
                    "target}", target.getName(),
                    "{reason}", reason);
        }
        else
        {
            Bukkit.broadcastMessage(DesireCore.getLangHandler().renderMessage("blacklist.blacklist_message", true, false,
                    "{player}", sender.getName(),
                    "target}", target.getName(),
                    "{reason}", reason));
        }

        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(Type.BAN);
        punishment.setPunished(target.getUniqueId());
        punishment.setIssuer(sender.getUniqueId());
        punishment.setReason(reason);
        punishment.setBlacklisted(true);
        punishment.save();

        PunishmentHandler.getInstance().refreshPunishments(target);

        if (target.isOnline())
        {
            target.getPlayer().kickPlayer(("&c&lYou are permanently blacklisted from the network!\n"
                    + "&cReason: &7{reason}\n"
                    + "&cBanned By: &7{issuer}\n"
                    + "&7Visit &ehttps://desirehcf.com/rules&7 for our terms and rules")
                            .replace("{reason}", reason)
                            .replace("{issuer}", sender.getName())
                            .replace("&", "§"));
        }
    }
}
