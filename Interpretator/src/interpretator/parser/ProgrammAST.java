package interpretator.parser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alex
 */
public class ProgrammAST implements AST {
    private final List<AST> statements = new ArrayList<>();

    ProgrammAST() {
    }
    
    void add(AST statement) {
        statements.add(statement);
    }
    
    public List<AST> getStatements(){
        return new ArrayList<>(statements);
    }

    @Override
    public ASTKind getKind() {
        return ASTKind.Programm;
    }

    @Override
    public String toString() {
        return getKind().name() + " size=" + statements.size();
    }
}
