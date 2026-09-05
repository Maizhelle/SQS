package Model;

public enum MatchFormat {
    SINGLE("Single", 2),
    DOUBLE("Double", 4);
    
    private final String displayName;
    private final int maxPlayer;
    
    MatchFormat(String displayName, int maxPlayer) {
        this.displayName = displayName;
        this.maxPlayer = maxPlayer;
    }

    public String getDisplayName() {
        return displayName;
    }
    public int getMaxPlayer() {
        return maxPlayer;
    }
    
    
    
}
