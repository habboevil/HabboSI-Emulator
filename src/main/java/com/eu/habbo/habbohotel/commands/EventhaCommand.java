package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.outgoing.habboway.nux.NuxAlertComposer;

import java.util.Iterator;
import java.util.Map.Entry;

public class EventhaCommand extends Command {
    public EventhaCommand() {
        super("cmd_eventha", Emulator.getTexts().getValue("commands.keys.cmd_eventha").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (gameClient.getHabbo().getHabboInfo().getCurrentRoom() != null) {

            if (params.length == 2) {
                String username = params[1];
                Habbo habboUser = gameClient.getHabbo().getHabboInfo().getCurrentRoom().getHabbo(username);

                int roomId = gameClient.getHabbo().getHabboInfo().getCurrentRoom().getId();
                String roomName = gameClient.getHabbo().getHabboInfo().getCurrentRoom().getName();
                String data = "customEventHotelAlert/" + roomId + "/" + roomName + "/" + username;

                Iterator<Entry<Integer, Habbo>> activeUsers = Emulator.getGameEnvironment().getHabboManager().getOnlineHabbos().entrySet().iterator();

                while (activeUsers.hasNext()) {
                    Entry<Integer, Habbo> set = activeUsers.next();
                    Habbo habbo = set.getValue();
                    if (!habbo.getHabboStats().blockStaffAlerts) {
                        habbo.getClient().sendResponse(new NuxAlertComposer(data).compose());
                    }
                }
            }
        }

        return true;
    }
}
