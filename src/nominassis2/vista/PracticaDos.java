/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominassis2.vista;

import java.io.File;
import java.io.IOException;
import nominassis2.controlador.CoordinadorExcel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author pablo
 */
public class PracticaDos {
    
    public PracticaDos(){
        
    }
    
    public void practicaDos(File excel) {
        try {

            CoordinadorExcel manejadora = new CoordinadorExcel();
            manejadora.comprobarNif(excel);
            manejadora.construirIBAN(excel);
            manejadora.construirEmail(excel);
        } catch (IOException | InvalidFormatException e) {
            System.out.println(e);
        }

    }
}
