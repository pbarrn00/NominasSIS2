package nominassis2.utils;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;

public class Utils {

    /**
     * Letters used to forms DNI's.
     */
    private static final String[] DNILETTERS = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J",
        "Z", "S", "Q", "V", "H", "L", "C", "K", "E"};

    private static final int[] NUMBERSCCC = {1, 2, 4, 8, 5, 10, 9, 7, 3, 6};

    private static HashMap<Character, Integer> map = new HashMap();

    private static boolean correcto = true;

    public Utils() {

    }

    /**
     * Check if the string given could be a valid DNI.
     *
     * @param dni - string to check if it could be a valid DNI.
     *
     * @return true if it is a valid DNI or false if not.
     */
    public static boolean isAValidDNI(String dni) {
        /* Data length values ​​are checked. */
        if (dni.length() != 9 || !Character.isLetter(dni.charAt(8)) || !isANum(dni.substring(0, 8))) {
            return false;
        }

        /*
         * The letter that the DNI inserted by their numbers would have is calculated.
         */
        int numDNI = Integer.parseInt(dni.substring(0, 8));
        int rest = numDNI % 23;

        /* If the calculated letter matches the entered letter, true is returned. */
        return Utils.DNILETTERS[rest].equalsIgnoreCase((dni.substring(8)));
    }

    public static String getCorrectDNI(String dni) {
        int numDNI = Integer.parseInt(dni.substring(0, 8));
        int rest = numDNI % 23;
        String dniS = dni.substring(0, 8);

        return dniS + Utils.DNILETTERS[rest];
    }

    /**
     * Check if the string given is a number.
     *
     * @param num - string to check if it is a number.
     *
     * @return true if it is a number or false if not.
     */
    public static boolean isANum(String num) {
        for (Character character : num.toCharArray()) {
            if (!Character.isDigit(character)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Cambia el nie a nif
     *
     * @param nie
     *
     * @return nif
     */
    public static String nieToNif(String nie) {

        String nif = "";

        switch (nie.charAt(0)) {
            case 'X':
                nif = "0";
                break;
            case 'Y':
                nif = "1";
                break;
            case 'Z':
                nif = "2";
                break;
        }

        nif += nie.substring(1);
        return nif;

    }

    public static String construirIBAN(String ccc, String pais) {
        rellenarHashMap();
        String previo = ccc + map.get(pais.charAt(0)) + map.get(pais.charAt(1)) + "00";
        BigInteger num = new BigInteger(previo);
        BigInteger resto = num.mod(new BigInteger("97"));
        BigInteger diferencia = (new BigInteger("98")).subtract(resto);
        String n = "";
        if (diferencia.toString().length() < 10) {
            n = "0" + diferencia.toString();
        } else {
            n = diferencia.toString();
        }

        String iban = pais + n + ccc;

        return iban;
    }

    public static String comprobarCCC(String cuenta) {
        correcto = true;
        String cuentaFinal = cuenta.substring(0, 8);
        if (cuenta.length() == 20) {
            correcto = true;
            int primer = primerDigitoControl(cuenta);
            if (primer != Character.getNumericValue(cuenta.charAt(8))) {
                cuentaFinal += primer;
                correcto = false;
            } else {
                cuentaFinal += cuenta.substring(8, 9);
            }
            int segundo = segundoDigitoControl(cuenta);
            if (segundo != Character.getNumericValue(cuenta.charAt(9))) {
                cuentaFinal += segundo;
                correcto = false;
            } else {
                cuentaFinal += cuenta.substring(9, 10);
            }
            cuentaFinal += cuenta.substring(10);
            return cuentaFinal;
        } else {
            return null;
        }
    }

    public static boolean isCorrecto() {
        return correcto;
    }

    private static int primerDigitoControl(String cuenta) {
        String aux = cuenta.substring(0, 8);
        while (aux.length() < 10) {
            aux = '0' + aux;
        }
        int suma = 0, resto, digit;
        for (int i = 0; i < aux.length(); i++) {
            suma += Character.getNumericValue(aux.charAt(i)) * NUMBERSCCC[i];
        }
        resto = suma % 11;
        digit = 11 - resto;

        if (digit == 11) {
            return 0;
        }
        if (digit == 10) {
            return 1;
        }

        return digit;
    }

    private static int segundoDigitoControl(String cuenta) {
        String aux = cuenta.substring(10, 20);

        int suma = 0, resto, digit;
        for (int i = 0; i < aux.length(); i++) {
            suma += Character.getNumericValue(aux.charAt(i)) * NUMBERSCCC[i];
        }
        resto = suma % 11;
        digit = 11 - resto;

        if (digit == 11) {
            return 0;
        }
        if (digit == 10) {
            return 1;
        }

        return digit;
    }

    private static void rellenarHashMap() {
        int valor = 10;
        for (char i = 'A'; i <= 'Z'; i++) {
            map.put(i, valor++);
        }
    }

    public static LinkedList<String> generarMail(String nombre, String ap1, String ap2, String empresa, LinkedList<String> emails) {
        StringBuilder construirBase = new StringBuilder();
        StringBuilder sucedaneo = new StringBuilder();
        if (ap2.equals("")) {
            construirBase.append(nombre.charAt(0)).append(ap1.charAt(0));
        } else {
            construirBase.append(nombre.charAt(0)).append(ap1.charAt(0)).append(ap2.charAt(0));
        }
        sucedaneo.append(generarNumero(emails, construirBase.toString()));
        sucedaneo.append('@').append(empresa).append(".com");
        construirBase.append(sucedaneo.toString());
        emails.add(construirBase.toString());
        return emails;
    }

    private static String generarNumero(LinkedList<String> emails, String base) {
        int numero = 0;
        if (base.length() == 3) {
            for (int i = 0; i < emails.size(); i++) {
                if (emails.get(i).substring(0, 3).equals(base)) {
                    numero++;
                }
            }
        } else {
            for (int i = 0; i < emails.size(); i++) {
                if (emails.get(i).substring(0, 2).equals(base) && Utils.isANum(emails.get(i).substring(2, 3))) {
                    numero++;
                }
            }
        }

        Formatter fmt = new Formatter();
        fmt.format("%02d", numero);
        return fmt.toString();
    }

    /**
     * Permite convertir un String en fecha (Date).
     *
     * @param fecha Cadena de fecha dd/MM/yyyy
     * @return Objeto Date
     */
    public static Date ParseFecha(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("MM/yyyy");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        return fechaDate;
    }

    public static boolean parseProrrateo(String pro) {
        if (pro.equals("SI")) {
            return true;
        } else {
            return false;
        }
    }

    public static String parseCelda(Cell text) {
        if (text == null) {
            return "";
        } else {
            return text.toString();
        }
    }
    
    public static String getNombreMes(int month) {
        String monthString;
        switch (month) {
            case 1:
                monthString = "Enero";
                break;
            case 2:
                monthString = "Febrero";
                break;
            case 3:
                monthString = "Marzo";
                break;
            case 4:
                monthString = "Abril";
                break;
            case 5:
                monthString = "Mayo";
                break;
            case 6:
                monthString = "Junio";
                break;
            case 7:
                monthString = "Julio";
                break;
            case 8:
                monthString = "Agosto";
                break;
            case 9:
                monthString = "Septiembre";
                break;
            case 10:
                monthString = "Octubre";
                break;
            case 11:
                monthString = "Noviembre";
                break;
            default:
                monthString = "Diciembre";
                break;
        }
        return monthString;
    }
    
    public static double round2decimasl(double n){ return (double) Math.round((n+0.0000001) * 100) / 100; }
}
