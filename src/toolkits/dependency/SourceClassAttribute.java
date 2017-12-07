package toolkits.dependency;

public class SourceClassAttribute {

    String className;
    String methodName;
    int lineNumber;

    String variableName;

    public SourceClassAttribute(String cn, String mn, int ln, String vn) {
        this.className = cn;
        this.methodName = mn;
        this.lineNumber = ln;
        this.variableName = vn;
    }

    public String className() {
        return this.className;
    }

    public String methodName() {
        return this.methodName;
    }

    public int lineNumber() {
        return this.lineNumber;
    }

    public String variableName() {
        return this.variableName;
    }
}
