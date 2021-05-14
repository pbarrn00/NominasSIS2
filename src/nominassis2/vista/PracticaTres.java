/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominassis2.vista;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import nominassis2.controlador.CoordinadorExcel;
import nominassis2.controlador.CoordinadorNominas;
import nominassis2.modelo.vo.Nomina;
import nominassis2.modelo.vo.Trabajadorbbdd;
import nominassis2.utils.Utils;
import static nominassis2.vista.NominasSIS2.ANSI_GREEN;
import static nominassis2.vista.NominasSIS2.ANSI_PURPLE;
import static nominassis2.vista.NominasSIS2.ANSI_RED;
import static nominassis2.vista.NominasSIS2.ANSI_RESET;

/**
 *
 * @author diego
 */
public class PracticaTres {
    
    public PracticaTres() { 
    }
    
    public LinkedList<Nomina> practicaTres(File excel, Date fch) throws IOException {
        LinkedList<Nomina> listaDeNominas = new LinkedList<>();
        CoordinadorNominas nominas = new CoordinadorNominas();
        CoordinadorExcel manejadora = new CoordinadorExcel();
        LinkedList<Trabajadorbbdd> listaTrabajadoresNomina;
        listaTrabajadoresNomina = manejadora.trabajadoresNominas(excel, fch);
        Iterator iter = listaTrabajadoresNomina.iterator();
        HashMap mapaTrienios = manejadora.mapaTrienios(excel);
        HashMap mapaIRPF = manejadora.mapaIRPF(excel);
        int counter = 0;
        while (iter.hasNext()) {
            Trabajadorbbdd trabajador = (Trabajadorbbdd) iter.next();
            int numTrienios = nominas.calcularTrienios(trabajador, fch);
            double salarioBaseExcel = manejadora.obtenerSalarioBase(excel, trabajador.getCategoria());
            double complementosExcel = manejadora.obtenerComplementos(excel, trabajador.getCategoria());
            LocalDate fechaNomina = fch.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String esExtra = "No";
            double salarioBase = nominas.calcularSalarioBaseMes(salarioBaseExcel);
            double complemento = nominas.calcularComplementoMes(complementosExcel);
            double importeTrienios = nominas.importeTrienios(numTrienios, mapaTrienios);
            double prorrateo = nominas.calcularProrrateo(salarioBase, complemento, importeTrienios, trabajador.isProrrateo());
            double brutoMensual = nominas.calcularBrutoMensual(salarioBase, complemento, prorrateo, importeTrienios);
            double brutoAnual = nominas.calcularBrutoAnual(salarioBaseExcel, complementosExcel, trabajador, fch, mapaTrienios, numTrienios);
            double salarioCalculos;
            if (trabajador.isProrrateo()) {
                salarioCalculos = brutoMensual;
            } else {
                salarioCalculos = brutoMensual + nominas.calcularProrrateo(salarioBase, complemento, importeTrienios, true);
            }
            if (fechaNomina.getMonthValue() == 12 || fechaNomina.getMonthValue() == 6) {
                if(!trabajador.isProrrateo()) esExtra = "Sí";
            }
            double porcentajeSeguridadSocial = nominas.calcularSeguridadSocial(excel);
            double seguridadSocial = salarioCalculos * (porcentajeSeguridadSocial / 100);
            double porcentajeDesempleo = nominas.calcularDesempleo(excel);
            double desempleo = salarioCalculos * (porcentajeDesempleo / 100);
            double porcentajeFormacion = nominas.calcularFormacion(excel);
            double formacion = salarioCalculos * (porcentajeFormacion / 100);
            double porcentajeIRPF = nominas.calcularIRPF(brutoAnual, mapaIRPF);
            double irpf = brutoMensual * (porcentajeIRPF / 100);
            double deducciones = seguridadSocial + desempleo + formacion + irpf;
            double liquidoPercibir = brutoMensual - deducciones;

            double porcentajeSeguridadSocialEmpresario = nominas.calcularContingenciasEmpresario(excel);
            double seguridadSocialEmpresario = salarioCalculos * (porcentajeSeguridadSocialEmpresario / 100);
            double porcentajeDesempleoEmpresario = nominas.calcularDesempleoEmpresario(excel);
            double desempleoEmpresario = salarioCalculos * (porcentajeDesempleoEmpresario / 100);
            double porcentajeFormacionEmpresario = nominas.calcularFormacionEmpresario(excel);
            double formacionEmpresario = salarioCalculos * (porcentajeFormacionEmpresario / 100);
            double porcentajeAccidentes = nominas.calcularAccidentesTrabajo(excel);
            double accidentes = salarioCalculos * (porcentajeAccidentes / 100);
            double porcentajeFogasa = nominas.calcularFogasa(excel);
            double fogasa = salarioCalculos * (porcentajeFogasa / 100);
            double totalEmpresario = seguridadSocialEmpresario + desempleoEmpresario + formacionEmpresario + accidentes + fogasa;
            double costeTotal = brutoMensual + totalEmpresario;

            //totalEmpresario, deducciones
            Nomina nomina = new Nomina(++counter, trabajador, fechaNomina.getMonthValue(), fechaNomina.getYear(),
                    numTrienios, importeTrienios, salarioBase, complemento, prorrateo, brutoAnual, porcentajeIRPF, irpf, salarioCalculos, porcentajeSeguridadSocialEmpresario,
                    seguridadSocialEmpresario, porcentajeDesempleoEmpresario, desempleoEmpresario, porcentajeFormacionEmpresario, formacionEmpresario, porcentajeAccidentes, accidentes,
                    porcentajeFogasa, fogasa, porcentajeSeguridadSocial, seguridadSocial, porcentajeDesempleo, desempleo, porcentajeFormacion, formacion, brutoMensual, liquidoPercibir,
                    costeTotal);
            listaDeNominas.add(nomina);
            //printInfor(nomina, fch, "No", salarioCalculos);
            if (esExtra.equals("Sí")) {
                salarioCalculos = 0.0;
                seguridadSocial = salarioCalculos * (porcentajeSeguridadSocial / 100);
                desempleo = salarioCalculos * (porcentajeDesempleo / 100);
                formacion = salarioCalculos * (porcentajeFormacion / 100);
                deducciones = seguridadSocial + desempleo + formacion + irpf;
                liquidoPercibir = brutoMensual - deducciones;
                seguridadSocialEmpresario = salarioCalculos * (porcentajeSeguridadSocialEmpresario / 100);
                desempleoEmpresario = salarioCalculos * (porcentajeDesempleoEmpresario / 100);
                formacionEmpresario = salarioCalculos * (porcentajeFormacionEmpresario / 100);
                accidentes = salarioCalculos * (porcentajeAccidentes / 100);
                fogasa = salarioCalculos * (porcentajeFogasa / 100);
                totalEmpresario = seguridadSocialEmpresario + desempleoEmpresario + formacionEmpresario + accidentes + fogasa;
                costeTotal = brutoMensual + totalEmpresario;
                Nomina nominaExtra = new Nomina(++counter, trabajador, fechaNomina.getMonthValue(), fechaNomina.getYear(),
                        numTrienios, importeTrienios, salarioBase, complemento, prorrateo, brutoAnual, porcentajeIRPF, irpf, salarioCalculos, porcentajeSeguridadSocialEmpresario,
                        seguridadSocialEmpresario, porcentajeDesempleoEmpresario, desempleoEmpresario, porcentajeFormacionEmpresario, formacionEmpresario, porcentajeAccidentes, accidentes,
                        porcentajeFogasa, fogasa, porcentajeSeguridadSocial, seguridadSocial, porcentajeDesempleo, desempleo, porcentajeFormacion, formacion, brutoMensual, liquidoPercibir,
                        costeTotal);
                listaDeNominas.add(nominaExtra);
                //printInfor(nominaExtra, fch, esExtra, 0.0);
            }       
        }
        return listaDeNominas;
    }

    private static void printInfor(Nomina nom, Date fch, String esExtra, double salarioCalculos) {
        DecimalFormat df = new DecimalFormat("0.00");
        double deducciones = nom.getImporteSeguridadSocialTrabajador() + nom.getImporteDesempleoTrabajador() + nom.getImporteFormacionTrabajador() + nom.getImporteIrpf();
        double totalEmpresario = nom.getImporteSeguridadSocialEmpresario() + nom.getImporteDesempleoEmpresario() + nom.getImporteFormacionEmpresario() + nom.getImporteAccidentesTrabajoEmpresario() + nom.getImporteFogasaempresario();
        Trabajadorbbdd trabajador = nom.getTrabajadorbbdd();
        NominasSIS2.debug(ANSI_RED + "+---------------------------------------------------" + ANSI_RESET);
        NominasSIS2.debug(ANSI_RED + "| " + ANSI_PURPLE + "Empresa ->" + ANSI_GREEN + " Nombre: " + ANSI_RESET + trabajador.getNombreEmpresa() + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     CIF de la empresa: " + ANSI_RESET + trabajador.getCifEmpresa());
        NominasSIS2.debug(ANSI_RED + "| " + ANSI_PURPLE + "Trabajador ->" + ANSI_GREEN + " Categoría del trabajador: " + ANSI_RESET + trabajador.getCategoria() + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Bruto anual: " + ANSI_RESET + df.format(nom.getBrutoAnual()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Fecha de alta: " + ANSI_RESET + trabajador.getFechaAlta() + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     IBAN: " + ANSI_RESET + trabajador.getIban() + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Nombre y apellidos: " + ANSI_RESET + trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2() + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Nif/Nie: " + ANSI_RESET + trabajador.getNifnie());
        NominasSIS2.debug(ANSI_RED + "| " + ANSI_PURPLE + "Fecha de la nomina -> " + ANSI_RESET + fch + ANSI_GREEN + " Es Extra: " + ANSI_RESET + esExtra);
        NominasSIS2.debug(ANSI_RED + "| " + ANSI_PURPLE + "Importes a percibir el trabajador ->" + ANSI_GREEN + " Salario base: " + ANSI_RESET + df.format(nom.getImporteSalarioMes()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Prorrateo: " + ANSI_RESET + df.format(nom.getValorProrrateo()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Complemento mes: " + ANSI_RESET + df.format(nom.getImporteComplementoMes()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Antigüedad mes: " + ANSI_RESET + nom.getNumeroTrienios() + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Importe unitario: " + ANSI_RESET + df.format(nom.getImporteTrienios()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Bruto Mensual: " + ANSI_RESET + df.format(nom.getBrutoNomina())); // ESTE NO HACE FALTA IMPRIMIRLO (PARA INFO)
        NominasSIS2.debug(ANSI_RED + "| " + ANSI_PURPLE + "Descuentos del trabajador ->" + ANSI_GREEN + " Seguridad social: " + ANSI_RESET + nom.getSeguridadSocialTrabajador() + "% de " + df.format(salarioCalculos) + " --> " + df.format(nom.getImporteSeguridadSocialTrabajador()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Desempleo: " + ANSI_RESET + nom.getDesempleoTrabajador() + "% de " + df.format(salarioCalculos) + " --> " + df.format(nom.getImporteDesempleoTrabajador()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Formacion: " + ANSI_RESET + nom.getFormacionTrabajador() + "% de " + df.format(salarioCalculos) + " --> " + df.format(nom.getImporteFormacionTrabajador()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     IRPF: " + ANSI_RESET + nom.getIrpf() + "% de " + df.format(nom.getBrutoNomina()) + " -> " + df.format(nom.getImporteIrpf()));
        NominasSIS2.debug(ANSI_RED + "| " + ANSI_PURPLE + "Totales ->" + ANSI_GREEN + " Total deducciones: " + ANSI_RESET + df.format(deducciones) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Total devengos: " + ANSI_RESET + df.format(nom.getBrutoNomina()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Liquido a percibir: " + ANSI_RESET + df.format(nom.getLiquidoNomina()));
        NominasSIS2.debug(ANSI_RED + "| " + ANSI_PURPLE + "Pagos empresario ->" + ANSI_GREEN + " Base de pagos: " + ANSI_RESET + df.format(salarioCalculos) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Seguridad social empresario: " + ANSI_RESET + nom.getSeguridadSocialEmpresario() + "% de " + df.format(salarioCalculos) + " --> " + df.format(nom.getImporteSeguridadSocialEmpresario()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Desempleo: " + ANSI_RESET + nom.getDesempleoEmpresario() + "% de " + df.format(salarioCalculos) + " --> " + df.format(nom.getImporteDesempleoEmpresario()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Formacion: " + ANSI_RESET + nom.getFormacionEmpresario() + "% de " + df.format(salarioCalculos) + " --> " + df.format(nom.getImporteFormacionEmpresario()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Accidentes: " + ANSI_RESET + nom.getAccidentesTrabajoEmpresario() + "% de " + df.format(salarioCalculos) + " --> " + df.format(nom.getImporteAccidentesTrabajoEmpresario()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Fogasa: " + ANSI_RESET + nom.getFogasaempresario() + "% de " + df.format(salarioCalculos) + " --> " + df.format(nom.getImporteFogasaempresario()) + "\n"
                + ANSI_RED + "| " + ANSI_GREEN + "\t     Total empresario: " + ANSI_RESET + df.format(totalEmpresario));
        NominasSIS2.debug(ANSI_RED + "| " + ANSI_PURPLE + "Coste total trabajador -> " + ANSI_RESET + df.format(nom.getCosteTotalEmpresario()));
        NominasSIS2.debug(ANSI_RED + "+---------------------------------------------------" + ANSI_RESET);
        //Cambiar Coste total empresario <-> totalEmpresario si precisa
    }
}
