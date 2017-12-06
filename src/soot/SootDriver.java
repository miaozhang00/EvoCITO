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
 * 用来初始化Soot设置的类
 */
public class SootDriver {
	/**
	 * 单例
	 */
	private static SootDriver driver;
	/**
	 * Soot中的场景，用来描述整个系统
	 */
	Scene scene;
	/**
	 * 系统的类路径，所有使用到的库
	 */
	String classpath;
	/**
	 * 工作空间，application类的路径
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
	 * 返回Soot的参数列表
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
	 * 处理文件夹。初始化soot的类路径
	 */
	public void prosessDir(String classpath, String workspace) {
		reset();
		
		this.scene = Scene.v();
//		scene.
		
		if (!classpath.endsWith(File.pathSeparator)) {
			classpath += File.pathSeparator;
		}
		
		// 在处理包含phantom类的时候，不能将要处理的源码加入workspace
		// 另外必须添加编译之后的class文件，并且文件夹做路径要以\结尾
		classpath += workspace;
		this.classpath = classpath;
		this.workspace = workspace;
		
		Options.v().set_app(true);
		List<String> argsList = new ArrayList<String>();	// 处理soot.Main()函数的参数
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
			System.out.println("路径(；′⌒`)/(ㄒoㄒ)/~~");
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
	 * 重置soot
	 */
	private void reset() {
		if (scene != null) {
			G.reset();
			scene = null;
		}
	}
	
}
