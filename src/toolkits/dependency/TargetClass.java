package toolkits.dependency;

public class TargetClass {

    String className;

    public TargetClass(String cn) {
        this.className = cn;
    }

    public String className() {
        return this.className;
    }
}
