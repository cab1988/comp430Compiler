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
public class stringToken implements Token{

    //stringToken(String theString) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   // }
public final String name;

    public stringToken(String name) {
        this.name = name;
    }

  
    public String toString() {
        return name;
    }
}
