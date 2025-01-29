package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.outgoing.habboway.nux.NuxAlertComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PremiumOldSchoolWinCommand extends Command {
    public PremiumOldSchoolWinCommand() {
        super("cmd_win", Emulator.getTexts().getValue("commands.keys.cmd_premium_oldschool_win").split(";"));
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PremiumOldSchoolWinCommand.class);

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (gameClient.getHabbo().getHabboInfo().getCurrentRoom() != null ) {
            if (params.length == 2) {
                Habbo habbo = gameClient.getHabbo().getHabboInfo().getCurrentRoom().getHabbo(params[1]);

                if (habbo == null) {
                    gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_gift.user_not_found").replace("%username%", gameClient.getHabbo().getHabboInfo().getUsername()), RoomChatMessageBubbles.ALERT);
                    return true;
                } else {

                    String data = "ChoosePremiumOldSchoolPrizeView/show";
                    habbo.getClient().sendResponse((new NuxAlertComposer(data)).compose());
                    try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
                         PreparedStatement statement = connection.prepareStatement("UPDATE users SET is_win = 1 WHERE id = ?")) {
                        statement.setInt(1, habbo.getHabboInfo().getId());
                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            LOGGER.info("Successfully marked " + habbo.getHabboInfo().getUsername() + " as a winner!");
                            gameClient.getHabbo().whisper("Befehl erfolgreich ausgeführt");
                        } else {
                            LOGGER.warn("Failed to update winner status for " + habbo.getHabboInfo().getUsername());
                        }
                    } catch (Exception e) {
                        LOGGER.error("Error processing Win Command for " + habbo.getHabboInfo().getUsername(), e);
                    }

                }
            }
        }
        return true;
    }
}
