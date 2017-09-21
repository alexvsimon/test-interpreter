package interpretator.api.ast;

import java.util.List;

/**
 *
 * @author alex
 */
public interface ProgramAST extends AST {

    List<StatementAST> getStatements();
    
}
