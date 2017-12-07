package toolkits.dependency;

import global.GlobalTag;

public class SimpleAttributeDependency implements IDependency {

    String sourceClassName;
    String targetClassName;
    String type;

    public SimpleAttributeDependency(String sourceClassName, String targetClassName) {
        this.sourceClassName = sourceClassName;
        this.targetClassName = targetClassName;
        this.type = GlobalTag.DEP_ATTR;
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
