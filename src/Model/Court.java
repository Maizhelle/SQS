package Model;

public class Court {

    private int courtNum;
    private boolean occupied;
    private Match currentMatch;

    public Court(int courtNum) {
    this.courtNum = courtNum;
    this.occupied = false;
    this.currentMatch = null;
}   

    public int getCourtNum() {
        return courtNum;
    }

    public void setCourtNum(int courtNum) {
        this.courtNum = courtNum;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Match getCurrentMatch() {
        return currentMatch;
    }

    public void setCurrentMatch(Match currentMatch) {
        this.currentMatch = currentMatch;
    }

    public boolean isAvailable() {
    return !occupied && currentMatch == null;
}

    public void assignMatch(Match match) {

        currentMatch = match;
        occupied = true;

        if (match != null) {
            match.setCourt(this);
        }
    }

    public void freeCourt() {

        if (currentMatch != null) {
            currentMatch.setCourt(null);
        }

        currentMatch = null;
        occupied = false;
    }
}