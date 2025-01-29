package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.outgoing.generic.alerts.GenericAlertComposer;

public class GiveExpCommand extends Command {
    public GiveExpCommand() {
        super("cmd_givelevelpunkte", Emulator.getTexts().getValue("commands.keys.cmd_givelevelpunkte").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) {
        Habbo habbo = gameClient.getHabbo();
        if (params.length < 2) {
            habbo.getClient().sendResponse(new GenericAlertComposer("Bitte geben Sie einen Benutzernamen an! Beispiel: :giveexp lariesse 10"));
            return true;
        }

        String username = params[1];
        int expAmount = 1;
        if (params.length > 2) {
            try {
                expAmount = Integer.parseInt(params[2]);
            } catch (NumberFormatException e) {
                habbo.getClient().sendResponse(new GenericAlertComposer("Bitte geben Sie eine gültige Zahl ein!"));
                return true;
            }
        }

        Habbo targetHabbo = Emulator.getGameServer().getGameClientManager().getHabbo(username);
        if (targetHabbo == null) {
            habbo.getClient().sendResponse(new GenericAlertComposer("Benutzer nicht gefunden: " + username));
            return true;
        }

        targetHabbo.getHabboStats().achievementScore += expAmount;

        habbo.getClient().getHabbo().whisper(username + " hat " + expAmount + " Achievement-Punkte erhalten!");
        targetHabbo.getClient().getHabbo().whisper("Glückwunsch! Sie haben " + expAmount + " Achievement-Punkte erhalten!");

        targetHabbo.getHabboStats().getAchievementCache().clear();
        return true;
    }
}
