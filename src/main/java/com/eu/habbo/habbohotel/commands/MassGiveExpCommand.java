package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.habbo.habbohotel.users.Habbo;

import java.util.Map;

public class MassGiveExpCommand extends Command {
    public MassGiveExpCommand() {
        super("cmd_masslevelpunkte", Emulator.getTexts().getValue("commands.keys.cmd_masslevelpunkte").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (params.length == 2) {
            int amount;

            try {
                amount = Integer.valueOf(params[1]);
            } catch (Exception e) {
                gameClient.getHabbo().whisper("Ungültiger Betrag! Bitte geben Sie eine Zahl ein.", RoomChatMessageBubbles.ALERT);
                return true;
            }

            if (amount != 0) {
                for (Map.Entry<Integer, Habbo> set : Emulator.getGameEnvironment().getHabboManager().getOnlineHabbos().entrySet()) {
                    Habbo habbo = set.getValue();

                    habbo.getHabboStats().achievementScore += amount;

                    if (habbo.getHabboInfo().getCurrentRoom() != null)
                        habbo.whisper("Sie haben " + amount + " Achievement-Punkte erhalten!", RoomChatMessageBubbles.ALERT);
                }
            }
            return true;
        }
        gameClient.getHabbo().whisper("Ungültiger Betrag! Bitte geben Sie eine Zahl ein.", RoomChatMessageBubbles.ALERT);
        return true;
    }
}
