package interpretator.api.ast;

import java.util.List;

/**
 * Whole program.
 * 
 * @author alex
 */
public interface ProgramAST extends AST {

    /**
     * 
     * @return program statements.
     */
    List<StatementAST> getStatements();
    
}
