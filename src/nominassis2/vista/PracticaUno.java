/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominassis2.vista;

import java.io.File;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import nominassis2.controlador.Coordinador;
import nominassis2.exceptions.ObjectNotFoundException;
import nominassis2.modelo.vo.Nomina;
import nominassis2.modelo.vo.Trabajadorbbdd;

/**
 *
 * @author pablo
 */
public class PracticaUno {

    public PracticaUno() {

    }

    public void practicaUno() throws ObjectNotFoundException {

        Scanner sc = new Scanner(System.in);
        NominasSIS2.debug("********************************");
        NominasSIS2.debug("Introduce el NIF de un empleado:");
        String nif = sc.nextLine();

        /* Ejercicio 1 */
        try {
            Iterator iter = Coordinador.getCoordinador().buscarTrabajador(nif).iterator();
            Trabajadorbbdd trabajador = (Trabajadorbbdd) iter.next();
            NominasSIS2.debug("Nombre: " + trabajador.getNombre());
            NominasSIS2.debug("Apellidos: " + trabajador.getApellido1() + " " + trabajador.getApellido2());
            NominasSIS2.debug("NIF: " + trabajador.getNifnie());
            NominasSIS2.debug("Categoria: " + trabajador.getCategorias().getNombreCategoria());
            NominasSIS2.debug("Empresa: " + trabajador.getEmpresas().getNombre());
            Set nominas = trabajador.getNominas();
            Iterator iterNominas = nominas.iterator();
            while (iterNominas.hasNext()) {
                Nomina nextElement = (Nomina) iterNominas.next();
                NominasSIS2.debug("Fecha: " + nextElement.getMes() + "/" + nextElement.getAnio());
                NominasSIS2.debug("Salario Bruto: " + nextElement.getBrutoNomina());
            }
            /* Ejercicio 2 */
            int idEmpresa = trabajador.getEmpresas().getIdEmpresa();
            Coordinador.getCoordinador().actualizarEmpresas(idEmpresa);

            /* Ejercicio 3 */
            Coordinador.getCoordinador().borrarTrabajadorNomina(trabajador.getEmpresas());
        } catch (ObjectNotFoundException e) {
            NominasSIS2.debug(e.getMessage());
        }

    }
}
