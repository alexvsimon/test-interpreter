package interpretator.parser;

import interpretator.api.ast.ASTKind;
import interpretator.api.ast.StatementAST;
import java.util.ArrayList;
import java.util.List;
import interpretator.api.ast.ProgramAST;
import interpretator.api.lexer.Token;

/**
 *
 * @author alex
 */
/*package-local*/ class ProgramImpl implements ProgramAST {
    private final List<StatementAST> statements = new ArrayList<>();

    /*package-local*/ ProgramImpl() {
    }
    
    void add(StatementAST statement) {
        statements.add(statement);
    }
    
    @Override
    public List<StatementAST> getStatements(){
        return new ArrayList<>(statements);
    }

    @Override
    public ASTKind getKind() {
        return ASTKind.Program;
    }

    @Override
    public Token getFistToken() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return getKind().name() + " size=" + statements.size();
    }
}
