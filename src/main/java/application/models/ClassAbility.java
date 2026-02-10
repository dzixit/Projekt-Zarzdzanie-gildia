package application.models;

public class ClassAbility {
    private String className;
    private String abilityName;

    public ClassAbility(String className, String abilityName) {
        this.className = className;
        this.abilityName = abilityName;
    }

    public String getClassName() {
        return className;
    }

    public String getAbilityName() {
        return abilityName;
    }
}
