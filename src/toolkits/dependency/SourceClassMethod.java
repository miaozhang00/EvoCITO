package toolkits.dependency;

public class SourceClassMethod {

    String className;
    String methodName;
    int lineNumber;

    String invokeName;

    public SourceClassMethod(String cn, String mn, int ln, String in) {
        this.className = cn;
        this.methodName = mn;
        this.lineNumber = ln;
        this.invokeName = in;
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

    public String invokeName() {
        return this.invokeName;
    }
}
