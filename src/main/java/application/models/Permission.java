package application.models;

public class Permission {
    private int rankId;
    private String rankName;
    private int permissionTier;

    public Permission(int rankId, String rankName, int permissionTier) {
        this.rankId = rankId;
        this.rankName = rankName;
        this.permissionTier = permissionTier;
    }

    public int getPermissionTier() {
        return permissionTier;
    }
    public String getRankName() {return rankName;}
    public int getRankId() {return rankId;}

    @Override
    public String toString() {
        return rankName; // Zwraca nazwę rangi
    }
}
