package com.eu.habbo.messages.incoming.users;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.incoming.MessageHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class ChoosePremiumOldSchoolPrizeEvent extends MessageHandler {
    private static final Logger LOGGER = Logger.getLogger(ChoosePremiumOldSchoolPrizeEvent.class.getName());

    @Override
    public void handle() throws Exception {
        int prizeId = this.packet.readInt();
        int userId = this.client.getHabbo().getHabboInfo().getId();

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection()) {
            String checkQuery = "SELECT is_win FROM users WHERE id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, userId);

                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt("is_win") == 1) {
                        if (prizeId == 1) {
                            Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(userId);
                            habbo.givePixels(200);
                            if (habbo != null) {
                                habbo.givePoints(103, 6);
                            }
                        } else {
                            Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(userId);
                            if (habbo != null) {
                                habbo.givePixels(500);
                            }
                        }

                        String updateQuery = "UPDATE users SET is_win = 0 WHERE id = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, userId);
                            int rowsUpdated = updateStmt.executeUpdate();

                            if (rowsUpdated > 0) {
                                LOGGER.info("Reward granted to User ID " + userId + " and is_win updated to 0.");
                            } else {
                                LOGGER.warning("Failed to update is_win for User ID " + userId + ".");
                            }
                        }
                    } else {
                        LOGGER.warning("User ID " + userId + " attempted to claim a reward but is_win = 0.");
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.severe("ChoosePremiumOldPrizeEvent sırasında bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
