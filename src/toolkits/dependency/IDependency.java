package toolkits.dependency;

public interface IDependency {
	
	/*
	 * ��ø�������
	 */
	public String getParentClassName();
	
	/*
	 * �����������
	 */
	public String getChildClassName();
	
	/*
	 * �����������
	 */
	public String getType();
	
	/*
	 * �ж����������Ƿ���succ
	 */
	public boolean isSucc();
	
	public String toDump();
	
}
