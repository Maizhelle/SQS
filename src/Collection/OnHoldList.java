package Collection;

import Model.Player;
import Model.PlayerStatus;
import Model.MatchFormat;

import java.util.ArrayList;
import java.util.List;

public class OnHoldList {
    private List<Player> players = new ArrayList<>();

    public void add(Player player) {
        player.setStatus(PlayerStatus.OH_HOLD);
        players.add(player);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public List<Player> getAllPlayers() {
        return new ArrayList<>(players);
    }

    public void clearAll() {
        players.clear();
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }

    public int size() {
        return players.size();
    }

    public List<Player> getPlayersByMode(MatchFormat mode) {
        List<Player> result = new ArrayList<>();
        for (Player p : players) {
            if (p.getFormat() == mode) {
                result.add(p);
            }
        }
        return result;
    }
}
