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
 * ������
 * ����������������룬���������
 */
public class Manager {
	
	// �����ļ�
	Properties config;
	
	// ��������
	DataCenter 		dc;
	
	// ·��������
	LocationManager locMgr;
	
	// ���ع�����
	LoadManager 	loadMgr;
	
	// ת��������
	DumpManager 	dumpMgr;
	
	// ���������
	Analyszer 		analyszer;
	
	// ���ݴ�����
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
	
	// �������
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
	// ת����Ϣ
	public void dump(String dumpObject) {
		dumpMgr.dump(dumpObject);
	}
	
}
