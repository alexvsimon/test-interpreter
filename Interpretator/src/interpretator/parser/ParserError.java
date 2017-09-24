/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpretator.parser;

/**
 *
 * @author as204739
 */
public class ParserError {
    private final String message;
    private final String context;
    private final int[] rowCol; 

    public ParserError(String message, String context, int[] rowCol) {
        this.message = message;
        this.context = context;
        this.rowCol = rowCol;
    }

    public String getMessage() {
        return message;
    }

    public String getContext() {
        return context;
    }

    public int[] getRowCol() {
        return rowCol;
    }
    
}
