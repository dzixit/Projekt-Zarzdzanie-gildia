package application.models;

public class Player_Class {
    private int classId;
    private String className;

    public Player_Class(int classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    public int getClassId() { return classId; }
    public String getClassName() { return className; }

    @Override
    public String toString() {
        return className;
    }
}
