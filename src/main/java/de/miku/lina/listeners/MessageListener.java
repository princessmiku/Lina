package de.miku.lina.listeners;

import de.miku.lina.utils.DataShare;
import de.miku.lina.utils.Logging;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Logging.debug("message received");
        if (!event.isFromType(ChannelType.TEXT)) return;
        Logging.debug("message received");
        DataShare.userHandler.insertUser(event.getAuthor());
        DataShare.commandHandler.onTextCommand(event);
    }
}
