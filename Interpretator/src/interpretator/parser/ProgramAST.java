package interpretator.parser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alex
 */
public class ProgramAST implements AST {
    private final List<AST> statements = new ArrayList<>();

    ProgramAST() {
    }
    
    void add(AST statement) {
        statements.add(statement);
    }
    
    public List<AST> getStatements(){
        return new ArrayList<>(statements);
    }

    @Override
    public ASTKind getKind() {
        return ASTKind.Program;
    }

    @Override
    public String toString() {
        return getKind().name() + " size=" + statements.size();
    }
}
