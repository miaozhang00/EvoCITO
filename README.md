# EvoCITO
Generation of Class Integration and Test Order Based on Evolutionary Algorithms

This tool is implemented to generate class integration test order based on three evolutionary algorithms, i.e., genetic algorithm, particle swarm optimization, and simulated annealing algorithm. It consists of four modules: loader, analyzer, generator and output module. Loader reads the programs' information from input files if accessible, otherwise analyzer would acquire the type of inter-class dependencies, attribute coupling and method coupling by static analysis built on Soot program analysis framework. generator can devise class integration test order with minimal stubbing efforts by using evolutionary algorithms. After that, the results can be outputed.
