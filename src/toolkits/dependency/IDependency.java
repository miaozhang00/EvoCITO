package toolkits.dependency;

public interface IDependency {

    /*
     * 获得父依赖方
     */
    public String getParentClassName();

    /*
     * 获得子依赖方
     */
    public String getChildClassName();

    /*
     * 获得依赖类型
     */
    public String getType();

    /*
     * 判断子依赖方是否是succ
     */
    public boolean isSucc();

    public String toDump();

}
