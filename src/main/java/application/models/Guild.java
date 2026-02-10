package application.models;

public class Guild {
    private int guildId;
    private String name;
    private String creationDate;

    public Guild(int guildId, String name, String creationDate) {
        this.guildId = guildId;
        this.name = name;
        this.creationDate = creationDate;
    }

    public int getGuildId() { return guildId; }
    public String getName() { return name; }
    public String getCreationDate() { return creationDate; }

    @Override
    public String toString() {
        return name;
    }

}
