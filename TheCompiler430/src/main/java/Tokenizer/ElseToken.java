/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tokenizer;

/**
 *
 * @author Quelyn
 */
public class ElseToken implements Token {
    @Override
    public int hashCode() { return 9; }
    @Override
    public boolean equals(final Object other) {
        return other instanceof ElseToken;
    }
    @Override
    public String toString() {
        return "else";
    }
}
