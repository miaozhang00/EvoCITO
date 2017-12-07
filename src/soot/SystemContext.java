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
     * 单例模式
     */
    private static SystemContext instance;

    public static synchronized SystemContext getInstance() {
        if (instance == null) {
            instance = new SystemContext();
        }
        return instance;
    }

    private SystemContext() {
    }

    /**
     * 返回是否包含类scName
     */
    public boolean containsClass(final String scName) {
        return Scene.v().containsClass(scName);
    }

    /**
     * 返回所有的应用类
     */
    public Collection<SootClass> getApplicationClasses() {
        return Collections.unmodifiableCollection(Scene.v().getApplicationClasses());
    }

    /**
     * 返回名为className的类，包括包名
     */
    public SootClass getClassByName(final String className) {
        Scene _scene = Scene.v();
        if (!_scene.containsClass(className)) {
            _scene.loadClassAndSupport(className);
        }
        return _scene.getSootClass(className);
    }

    /**
     * 返回所有的类
     */
    public Collection<SootClass> getClasses() {
        return Scene.v().getClasses();
    }

    /**
     * 返回pgName包中所有的application类
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
     * 返回所有包含application类的包
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
     * 返回所有入口方法
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
