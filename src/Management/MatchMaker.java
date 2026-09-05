package Management;

import Model.Match;
import Model.Player;

import java.util.List;

public class MatchMaker {

    public boolean isSkillCompatible(Player a, Player b) {
        return Math.abs(a.getSkillRank() - b.getSkillRank()) <= 1;
    }

    public Match findCompatibleMatch(
            Player player,
            List<Match> pendingMatches) {

        for (Match match : pendingMatches) {

            if (canJoinMatch(player, match)) {
                return match;
            }

        }

        return null;
    }

    public Match createNewMatch(Player player) {
        return new Match(player.getFormat());
    }

    public boolean canJoinMatch(
            Player player,
            Match match) {

        if (match.getPlayers().size()
                >= match.getFormat().getMaxPlayer()) {
            return false;
        }

        if (player.getFormat()
                != match.getFormat()) {
            return false;
        }

        if (match.getPlayers().isEmpty()) {
            return true;
        }

        return isSkillCompatible(
                player,
                match.getPlayers().get(0)
        );
    }
}