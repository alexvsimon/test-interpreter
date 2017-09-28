/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpretator.run;

/**
 *
 * @author alex
 */
/*package-local*/ class OneDoubleVarMap implements DoubleVarsMap {
    private final String parameter;
    private final double value;
    
    /*package-local*/ OneDoubleVarMap(String parameter, double value) {
        this.parameter = parameter;
        this.value = value;
    }
    
    @Override
    public double get(String name) {
        return value;
    }
}
