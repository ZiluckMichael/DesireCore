package com.desiremc.core.commands.ticket;

import com.desiremc.core.DesireCore;
import com.desiremc.core.tickets.TicketHandler;
import com.desiremc.core.validators.TicketExistsValidator;
import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;

public class TicketCloseCommand extends ValidCommand
{

    public TicketCloseCommand()
    {
        super("close", "Close a ticket with a comment.", Rank.MODERATOR, ValidCommand.ARITY_REQUIRED_VARIADIC, new String[]{"ticket", "response"}, new String[]{});
        addValidator(new TicketExistsValidator(), "ticket");
        addParser(new IntegerParser(), "ticket");
        addParser(new StringParser(), "response");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        StringBuilder sb = new StringBuilder();

        if (args.length >= 2)
        {
            for (int i = 1; i < args.length; i++)
            {
                sb.append(args[i] + " ");
            }
        }

        TicketHandler.closeTicket(sender, TicketHandler.getTicket((int) args[0]), sb.toString().trim());

        DesireCore.getLangHandler().sendRenderMessage(sender, "ticket.close", "{id}", args[0] + "");
    }

}
