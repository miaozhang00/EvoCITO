package mgr;

import global.GlobalTag;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import process.Processor;

import data.DataCenter;
import analysis.Analyszer;

/*
 * 管理类
 * 负责整个程序的输入，分析和输出
 */
public class Manager {
	
	// 配置文件
	Properties config;
	
	// 数据中心
	DataCenter 		dc;
	
	// 路径管理器
	LocationManager locMgr;
	
	// 加载管理器
	LoadManager 	loadMgr;
	
	// 转储管理器
	DumpManager 	dumpMgr;
	
	// 程序分析器
	Analyszer 		analyszer;
	
	// 数据处理器
	Processor 		processor;
	
	public Manager() {
		initial();
		
		this.dc 		= new DataCenter(config);
		this.locMgr 	= new LocationManager(dc);
		this.loadMgr 	= new LoadManager(dc, locMgr);
		this.dumpMgr 	= new DumpManager(dc, locMgr);
		this.analyszer 	= new Analyszer(dc);
		this.processor 	= new Processor(dc);
	}
	
	private void initial() {
		initialConfig();
	}
	
	// 读入参数
	private void initialConfig() {
		this.config = new Properties();
		try {
			config.load(new FileInputStream(
					GlobalTag.LOC_CONFIG + "/" + GlobalTag.FILE_CONFIG));
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public void load(String loadType) {
		loadMgr.load(loadType);
	}
	
	public void analyze() {
		analyszer.analysis();
	}
	
	public void process() {
		processor.process();
	}
	
	public void processGA(){
		processor.processGA();
	}
	
	public void processRIA(){
		processor.processRIA();
	}
	// 转储信息
	public void dump(String dumpObject) {
		dumpMgr.dump(dumpObject);
	}
	
}
