/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominassis2.controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import nominassis2.modelo.vo.Trabajadorbbdd;
import nominassis2.utils.Utils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author pablo
 */
public class CoordinadorExcel {

    private XSSFWorkbook libro;

    private XSSFSheet hoja;

    private Iterator<Row> filaIterador;

    private Row fila;

    private Cell celda;

    public CoordinadorExcel() {
    }

    public void comprobarNif(File excel) throws IOException, InvalidFormatException {
        try {
            CoordinadorXML creador = new CoordinadorXML("Trabajadores");
            InputStream inp = new FileInputStream(excel);
            List<String> nifs = new ArrayList();

            libro = new XSSFWorkbook(inp);

            // Cojemos la hoja 2 del excel
            hoja = libro.getSheetAt(2);
            filaIterador = hoja.iterator();
            filaIterador.next();
            while (filaIterador.hasNext()) {
                fila = filaIterador.next();
                celda = fila.getCell(7);
                //System.out.println(celda);
                if (celda != null && !celda.toString().equals("")) {
                    if (nifs.contains(celda.toString())) {
                        //Reportar error en XML si esta repetido
                        creador.createTrabajadoresXML(fila.getCell(6).toString(), fila.getCell(4).toString(),
                                 fila.getCell(5).toString(), fila.getCell(0).toString(), fila.getCell(2).toString(), fila.getRowNum() + 1);
                    } else {
                        nifs.add(celda.toString());
                    }

                    if (Character.isLetter(celda.toString().charAt(0))) {
                        if (!Utils.isAValidDNI(Utils.nieToNif(celda.toString()))) {
                            //Cambiar a correcto
                            celda.setCellValue(Utils.getCorrectDNI(Utils.nieToNif(celda.toString())));
                        }
                    } else {
                        if (!Utils.isAValidDNI(celda.toString())) {
                            //Cambiar a correcto
                            //System.out.println("Antes de modificar: "+celda.toString());
                            celda.setCellValue(Utils.getCorrectDNI(celda.toString()));
                            //System.out.println("despues de modificar: "+celda.toString());

                        }
                    }
                } else {
                    boolean esFilaVacia = true;
                    if (fila.getCell(6) != null) {
                        esFilaVacia = false;
                    }

                    if (!esFilaVacia) {
                        //Reporte Error XML DNI vacio
                        creador.createTrabajadoresXML(fila.getCell(6).toString(), fila.getCell(4).toString(),
                                 fila.getCell(5).toString(), fila.getCell(0).toString(), fila.getCell(2).toString(), fila.getRowNum() + 1);
                    }
                    //Si es fila vacia no se hace nada
                }
            }
            creador.escribirArchivoTrabajadores();
            FileOutputStream archout = new FileOutputStream(excel);
            libro.write(archout);
            libro.close();
            archout.close();

        } catch (ParserConfigurationException ex) {

        } catch (TransformerException ex) {

        }

    }

    public void construirIBAN(File excel) throws IOException, InvalidFormatException {
        try {
            CoordinadorXML creador = new CoordinadorXML("Cuentas");
            InputStream inp = new FileInputStream(excel);
            List<String> nifs = new ArrayList();

            libro = new XSSFWorkbook(inp);

            // Cojemos la hoja 2 del excel
            hoja = libro.getSheetAt(2);
            filaIterador = hoja.iterator();
            filaIterador.next();
            while (filaIterador.hasNext()) {
                fila = filaIterador.next();
                celda = fila.getCell(9);
                //System.out.println(celda);
                if (celda != null && !celda.toString().equals("")) {
                    String cuenta = Utils.comprobarCCC(celda.toString());
                    if (cuenta != null) {
                        Cell celdilla = fila.createCell(11);
                        celdilla.setCellValue(Utils.construirIBAN(cuenta, fila.getCell(10).toString()));
                        if (!Utils.isCorrecto()) {
                            String ap2 = "";
                            if (fila.getCell(5) != null) {
                                ap2 = fila.getCell(5).toString();
                            }
                            creador.createCuentasXML(fila.getCell(6).toString(), fila.getCell(4).toString(),
                                     ap2, fila.getCell(0).toString(),
                                     fila.getCell(9).toString(), fila.getCell(11).toString(), fila.getRowNum() + 1);
                        }
                    }
                    //System.out.println(fila.getCell(11).toString());
                }
            }

            creador.escribirArchivoCuentas();
            FileOutputStream archout = new FileOutputStream(excel);
            libro.write(archout);
            libro.close();
            archout.close();

        } catch (ParserConfigurationException ex) {

        } catch (TransformerException ex) {

        }

    }

    public void construirEmail(File excel) throws IOException, InvalidFormatException {
        InputStream inp = new FileInputStream(excel);
        libro = new XSSFWorkbook(inp);
        // Cojemos la hoja 3 del excel
        hoja = libro.getSheetAt(2);
        filaIterador = hoja.iterator();
        filaIterador.next();
        LinkedList<String> emails = new LinkedList<>();
        while (filaIterador.hasNext()) {
            fila = filaIterador.next();
            //celda = fila.createCell(12);
            if (fila.getCell(6) != null) {
                if (fila.getCell(5) != null) {
                    emails = Utils.generarMail(fila.getCell(6).toString(), fila.getCell(4).toString(),
                             fila.getCell(5).toString(), fila.getCell(0).toString(), emails);
                } else {
                    emails = Utils.generarMail(fila.getCell(6).toString(),
                            fila.getCell(4).toString(), "", fila.getCell(0).toString(), emails);
                }
            }
            //System.out.println(emails.toString());
        }
        filaIterador = hoja.iterator();
        filaIterador.next();
        int i = 0;
        while (filaIterador.hasNext()) {
            fila = filaIterador.next();
            if (fila.getCell(6) != null) {
                celda = fila.createCell(12);
                celda.setCellValue(emails.get(i));
                i++;
            }
        }
        FileOutputStream archout = new FileOutputStream(excel);
        libro.write(archout);
        libro.close();
        archout.close();
    }

    public LinkedList<Trabajadorbbdd> trabajadoresNominas(File excel, Date fecha) throws FileNotFoundException, IOException {

        InputStream inp = new FileInputStream(excel);
        libro = new XSSFWorkbook(inp);
        // Cojemos la hoja 3 del excel
        hoja = libro.getSheetAt(2);
        filaIterador = hoja.iterator();
        filaIterador.next();
        LinkedList<Trabajadorbbdd> trabajadores = new LinkedList<>();

        while (filaIterador.hasNext()) {
            fila = filaIterador.next();
            //TODO DUDA CELDA CREADA PERO VACIA
            if (fila.getCell(3).getDateCellValue() != null) {
                Date fechaAlta = fila.getCell(3).getDateCellValue();
                if (fechaAlta.before(fecha)) {
                    trabajadores.add(new Trabajadorbbdd(fila.getCell(0).toString(), fila.getCell(1).toString(), fila.getCell(2).toString(), fila.getCell(3).getDateCellValue(),
                            fila.getCell(6).toString(), fila.getCell(4).toString(), Utils.parseCelda(fila.getCell(5)), Utils.parseCelda(fila.getCell(7)),
                            Utils.parseProrrateo(fila.getCell(8).toString()), fila.getCell(11).toString(), fila.getCell(12).toString(), fila.getCell(9).toString()));
                }
            }
        }
        return trabajadores;
    }

    public HashMap mapaTrienios(File excel) throws FileNotFoundException, IOException {
        HashMap mapa = new HashMap<Integer, Integer>();
        InputStream inp = new FileInputStream(excel);
        libro = new XSSFWorkbook(inp);
        // Cojemos la hoja 3 del excel
        hoja = libro.getSheetAt(0);
        filaIterador = hoja.iterator();
        filaIterador.next();
        while (filaIterador.hasNext()) {
            fila = filaIterador.next();
            mapa.put((int) (fila.getCell(5).getNumericCellValue()), (int) fila.getCell(6).getNumericCellValue());
        }
        return mapa;
    }

    public LinkedHashMap mapaIRPF(File excel) throws FileNotFoundException, IOException {
        LinkedHashMap mapa = new LinkedHashMap<Integer, Double>();
        InputStream inp = new FileInputStream(excel);
        libro = new XSSFWorkbook(inp);
        // Cojemos la hoja 3 del excel
        hoja = libro.getSheetAt(1);
        filaIterador = hoja.iterator();
        filaIterador.next();
        while (filaIterador.hasNext()) {
            fila = filaIterador.next();
            mapa.put((int) (fila.getCell(0).getNumericCellValue()), fila.getCell(1).getNumericCellValue());
        }
        return mapa;
    }

    public double obtenerSalarioBase(File excel, String categoria) throws FileNotFoundException, IOException {
        InputStream inp = new FileInputStream(excel);
        libro = new XSSFWorkbook(inp);
        // Cojemos la hoja 3 del excel
        hoja = libro.getSheetAt(0);
        filaIterador = hoja.iterator();
        filaIterador.next();
        while (filaIterador.hasNext()) {
            fila = filaIterador.next();
            if (fila.getCell(0).toString().equals(categoria)) {
                return fila.getCell(1).getNumericCellValue();
            }
        }
        return 0;
    }

    public double obtenerComplementos(File excel, String categoria) throws FileNotFoundException, IOException {
        InputStream inp = new FileInputStream(excel);
        libro = new XSSFWorkbook(inp);
        // Cojemos la hoja 3 del excel
        hoja = libro.getSheetAt(0);
        filaIterador = hoja.iterator();
        filaIterador.next();
        while (filaIterador.hasNext()) {
            fila = filaIterador.next();
            if (fila.getCell(0).toString().equals(categoria)) {
                return fila.getCell(2).getNumericCellValue();
            }
        }
        return 0;
    }

    public double obtenerPorcentajesExcel(File excel, int f) throws FileNotFoundException, IOException {
        InputStream inp = new FileInputStream(excel);
        libro = new XSSFWorkbook(inp);
        hoja = libro.getSheetAt(1);
        filaIterador = hoja.iterator();

        for (int i = 0; i < f; i++) {
            fila = filaIterador.next();
        }
        return fila.getCell(6).getNumericCellValue();
    }
}
