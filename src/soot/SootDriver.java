package soot;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import soot.G;
import soot.Scene;
import soot.options.Options;

/**
 * ������ʼ��Soot���õ���
 */
public class SootDriver {
	/**
	 * ����
	 */
	private static SootDriver driver;
	/**
	 * Soot�еĳ�����������������ϵͳ
	 */
	Scene scene;
	/**
	 * ϵͳ����·��������ʹ�õ��Ŀ�
	 */
	String classpath;
	/**
	 * �����ռ䣬application���·��
	 */
	String workspace;
	
	private SootDriver() {};
	
	public synchronized static SootDriver getInstance() {
		if (driver == null) {
			driver = new SootDriver();
		}
		
		return driver;
	}
	
	/**
	 * ����Soot�Ĳ����б�
	 */
	private List<String> getOptions() {
		return Arrays.asList(new String[]{
			"-p", "jb", "enabled:true,use-original-names:true",
			"-p", "jb.ls", "enabled:true", 
			"-p", "jb.a", "enabled:true,only-stack-locals:true", 
			"-p", "jb.ulp", "enabled:false", 
			"-p", "wjtp", "enabled:true", 
			"-src-prec", "java", "-f", "n", "-w", "-keep-line-number", 
			"-allow-phantom-refs",
		});
	}
	
	/**
	 * �����ļ��С���ʼ��soot����·��
	 */
	public void prosessDir(String classpath, String workspace) {
		reset();
		
		this.scene = Scene.v();
//		scene.
		
		if (!classpath.endsWith(File.pathSeparator)) {
			classpath += File.pathSeparator;
		}
		
		// �ڴ������phantom���ʱ�򣬲��ܽ�Ҫ�����Դ�����workspace
		// ���������ӱ���֮���class�ļ��������ļ�����·��Ҫ��\��β
		classpath += workspace;
		this.classpath = classpath;
		this.workspace = workspace;
		
		Options.v().set_app(true);
		List<String> argsList = new ArrayList<String>();	// ����soot.Main()�����Ĳ���
		argsList.addAll(getOptions());
		
		String[] w = workspace.split(File.pathSeparator);
		for (int i = 0; i < w.length; ++i) {
			argsList.addAll(Arrays.asList(new String[] {"-process-dir", w[i]}));
		}
		
		argsList.addAll(Arrays.asList(new String[] {"-cp", classpath}));
		
		/*
		 * test1124
		 */
		for(String str : argsList){
			System.out.println("·��(�����`)/(��o��)/~~");
			System.out.println(str);
		}
		
		long begintime = System.currentTimeMillis();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
		String curTime = dateFormat.format(now);
		
		System.out.println(curTime + ", soot preprocessing start");
		
		soot.options.Options.v().parse(argsList.toArray(new String[0]));
		try {
			Scene.v().loadNecessaryClasses();
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("soot error");
		}
		
		now = new Date();
		curTime = dateFormat.format(now);
		long endtime = System.currentTimeMillis();
		System.out.println(curTime + ", soot preprocessing finished, " + (endtime - begintime) + "ms total.");
		
	}
	
	/**
	 * ����soot
	 */
	private void reset() {
		if (scene != null) {
			G.reset();
			scene = null;
		}
	}
	
}
