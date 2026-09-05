package Model;

public enum SkillLevel {
    BEGINNER("Beginner", 1),
    HIGH_BEGINNER("High Beginner", 2),
    INTERMEDIATE("Intermediate", 3),
    HIGH_INTERMEDIATE("High Intermediate", 4),
    ADVANCED("Advanced", 5),
    EXPERT("Expert", 6),
    PROFESSIONAL("Professional", 7);

    private final String displayName;
    private final int rank;

    SkillLevel(String displayName, int rank) {
        this.displayName = displayName;
        this.rank = rank;
    }

    public String getDisplayName() { return displayName; }
    public int getRank() { return rank; }
}