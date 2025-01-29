package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.users.Habbo;


import com.eu.habbo.messages.outgoing.habboway.nux.NuxAlertComposer;
import java.util.Iterator;
import java.util.Map.Entry;

public class EventCommand extends Command {
    public EventCommand() {
        super("cmd_event", Emulator.getTexts().getValue("commands.keys.cmd_event").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (gameClient.getHabbo().getHabboInfo().getCurrentRoom() != null) {
            if (params.length >= 2) {
                StringBuilder message = new StringBuilder();

                for (int i = 1; i < params.length; ++i) {
                    message.append(params[i]).append(" ");
                }

                String username = gameClient.getHabbo().getHabboInfo().getUsername();
                int roomId = gameClient.getHabbo().getHabboInfo().getCurrentRoom().getId();
                String data = "customEventAlert/" + roomId + "/" + username + "/" + message;
                Iterator activeUsers = Emulator.getGameEnvironment().getHabboManager().getOnlineHabbos().entrySet().iterator();

                while(activeUsers.hasNext()) {
                    Entry<Integer, Habbo> set = (Entry)activeUsers.next();
                    Habbo habbo = (Habbo)set.getValue();
                    if (!habbo.getHabboStats().blockStaffAlerts) {
                        habbo.getClient().sendResponse((new NuxAlertComposer(data)).compose());
                    }
                }
            }
        }

        return true;
    }
}
