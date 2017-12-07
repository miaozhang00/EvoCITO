package compare;

import java.util.Comparator;

import toolkits.SourceClassInfo;

public class DependencyDESCComparator implements Comparator<SourceClassInfo> {

    public int compare(SourceClassInfo o1, SourceClassInfo o2) {
        int sizeOfDep_1 = o1.getSizeOfDeps();
        int sizeOfDep_2 = o2.getSizeOfDeps();

        if (sizeOfDep_1 < sizeOfDep_2) {
            return 1;
        } else if (sizeOfDep_1 > sizeOfDep_2) {
            return -1;
        } else {
            return 0;
        }
    }

}
