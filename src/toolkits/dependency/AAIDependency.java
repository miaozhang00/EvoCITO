package toolkits.dependency;

public class AAIDependency implements IDependency {

    String sourceClassName;
    String targetClassName;
    String type;

    public AAIDependency(String sourceClassName, String targetClassName, String type) {
        this.sourceClassName = sourceClassName;
        this.targetClassName = targetClassName;
        this.type = type;
    }

    public String getParentClassName() {
        return sourceClassName;
    }

    public String getChildClassName() {
        return targetClassName;
    }

    public String getType() {
        return type;
    }

    public boolean isSucc() {
        // TODO Auto-generated method stub
        return false;
    }

    public String toDump() {
        StringBuilder sb = new StringBuilder();
        String dp_class = sourceClassName;
        String dc_class = targetClassName;

        sb.append(type + "\t" + dp_class + "\t" + dc_class + "\n");

        return sb.toString();
    }

}
