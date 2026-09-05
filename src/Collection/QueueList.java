package Collection;

import Model.Match;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class QueueList {

    private List<Match> pendingMatches = new ArrayList<>();

    private Queue<Match> readyMatches = new LinkedList<>();

    public void addPending(Match match) {
        pendingMatches.add(match);
    }

    public void removePending(Match match) {
        pendingMatches.remove(match);
    }

    public void addReady(Match match) {
        readyMatches.offer(match);
    }

    public Match pollReady() {
        return readyMatches.poll();
    }

    public void removeReady(Match match) {
        readyMatches.remove(match);
    }

    public void removeMatch(Match match) {
        pendingMatches.remove(match);
        readyMatches.remove(match);
    }

    public void markAsReady(Match match) {
        if (pendingMatches.remove(match)) {
            readyMatches.offer(match);
        }
    }

    public List<Match> getAllPendingMatches() {
        return new ArrayList<>(pendingMatches);
    }

    public List<Match> getAllReadyMatches() {
        return new ArrayList<>(readyMatches);
    }

    public List<Match> getAllMatches() {
        List<Match> all = new ArrayList<>();
        all.addAll(pendingMatches);
        all.addAll(readyMatches);
        return all;
    }
}
