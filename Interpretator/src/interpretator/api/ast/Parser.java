/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpretator.api.ast;

import java.util.List;

/**
 *
 * @author alex
 */
public interface Parser {
    
    List<ParserError> getErrors();

    ProgramAST parse();
    
}
