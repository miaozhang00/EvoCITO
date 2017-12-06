package soot;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import soot.ArrayType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.VoidType;

public class SystemContext {
	/**
	 * ����ģʽ
	 */
	private static SystemContext instance;
	
	public static synchronized SystemContext getInstance() {
		if (instance == null) {
			instance = new SystemContext();
		}
		return instance;
	}
	
	private SystemContext() {}
	
	/**
	 * �����Ƿ������scName
	 */
	public boolean containsClass(final String scName) {
		return Scene.v().containsClass(scName);
	}
	
	/**
	 * �������е�Ӧ����
	 */
	public Collection<SootClass> getApplicationClasses() {
		return Collections.unmodifiableCollection(Scene.v().getApplicationClasses());
	}
	
	/**
	 * ������ΪclassName���࣬��������
	 */
	public SootClass getClassByName(final String className) {
		Scene _scene = Scene.v();
		if (!_scene.containsClass(className)) {
			_scene.loadClassAndSupport(className);
		}
		return _scene.getSootClass(className);
	}
	
	/**
	 * �������е���
	 */
	public Collection<SootClass> getClasses() {
		return Scene.v().getClasses();
	}
	
	/**
	 * ����pgName�������е�application��
	 */
	public List<SootClass> getClassesOfPackage(String pgName) {
		List<SootClass> _result = new ArrayList<SootClass>();
		for (SootClass _sc : getApplicationClasses()) {
			if (_sc.getPackageName().equals(pgName)) {
				_result.add(_sc);
			}
		}
		return _result;
	}
	
	/**
	 * �������а���application��İ�
	 */
	public List<String> getPackagesName() {
		Collection<SootClass> _classes = Scene.v().getApplicationClasses();
		List<String> _result = new ArrayList<String>();
		for (SootClass _sc : _classes) {
			String packageName = _sc.getPackageName();
			if (_result.indexOf(packageName) >= 0) {
				continue;
			}
			_result.add(packageName);
		}
		return _result;
	}
	
	/**
	 * ����������ڷ���
	 */
	public Collection<SootMethod> getRoots() {
		final Collection<SootMethod> _temp = new HashSet<SootMethod>();
		final List<ArrayType> _argList = Collections.singletonList(ArrayType.v(RefType.v("java.lang.String"), 1));
		
		for (final Iterator<SootClass> _i = getClasses().iterator(); _i.hasNext();) {
			final SootClass _sc = _i.next();
			final SootMethod _sm = _sc.getMethod("main", _argList, VoidType.v());
			
			if (_sm != null && _sm.isStatic() && _sm.isPublic()) {
				_temp.add(_sm);
			}
		}
		
		return _temp;
	}
}
