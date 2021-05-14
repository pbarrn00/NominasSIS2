/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominassis2.vista;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import nominassis2.exceptions.ObjectNotFoundException;
import nominassis2.modelo.vo.Nomina;
import nominassis2.utils.Utils;

public class NominasSIS2 {

    /**
     * Debug is enabled if it is an debugging execution
     */
    private static final boolean DEBUG = true;

    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_RED = "\u001B[31m";

    public static final String ANSI_GREEN = "\u001B[32m";

    public static final String ANSI_PURPLE = "\u001B[35m";

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF);
        try {
            File excel = new File("recursos/SistemasInformacionII.xlsx");
            /**
             * Practica 1
             */
            //new PracticaUno().practicaUno();
            /**
             * Practica 2
             */
            //new PracticaDos().practicaDos(excel);
            /**
             * Practica 3
             */
            String fecha = "";
            Date fch = null;
            try (Scanner input = new Scanner(System.in)) {
                System.out.println("Introduzca el mes y el año del que generar las nominas");
                fecha = input.nextLine();
                fch = Utils.ParseFecha(fecha);
                if (fch == null) throw new Exception();
            }catch(Exception e){
                System.out.println(ANSI_RED+"[ERR] Error con el formato de la fecha introducida!\n"+e);
                System.exit(1);
            }
            LinkedList<Nomina> listaNominas = new PracticaTres().practicaTres(excel, fch);
            /**
             * Practica 4
             */
            new PracticaCuatro().practicaCuatro(listaNominas, fecha);

        } catch (ObjectNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        System.exit(0);
    }

    /**
     *
     * Log an string and decide if it is printed based on the value of the debug
     * variable.
     *
     * @param text to print and log.
     *
     */
    public static void debug(String text) {
        /*
         * For ease correction and simple debug tasks.
         */
        if (NominasSIS2.DEBUG) {
            System.out.println(text);
        }
    }

}
