/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpretator.parser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alex
 */
public class ProgrammAST {
    private final List<AST> statements = new ArrayList<>();

    public ProgrammAST() {
    }
    
    void add(AST statement) {
        statements.add(statement);
    }
    
    public List<AST> getStatements(){
        return new ArrayList<AST>(statements);
    }
}
