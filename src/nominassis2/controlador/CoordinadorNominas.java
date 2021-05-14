/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominassis2.controlador;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import nominassis2.modelo.vo.Trabajadorbbdd;

/**
 *
 * @author pablo
 */
public class CoordinadorNominas {

    public CoordinadorNominas() {
    }

    public int calcularTrienios(Trabajadorbbdd tra, Date fecha) {
        Date fechaAlta = tra.getFechaAlta();
        LocalDate fechaNomina = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaTra = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int mes = fechaNomina.getMonthValue() - fechaTra.getMonthValue();
        int anyos = fechaNomina.getYear() - fechaTra.getYear();
        int trienios;
        if (anyos % 3 != 0) {
            trienios = anyos / 3;
        } else {
            if (mes <= 0) {
                trienios = (anyos) / 3 - 1;
            } else {
                trienios = (anyos) / 3;
            }
        }
        return trienios;
    }

    public double calcularBrutoAnual(double salarioBase, double complementos, Trabajadorbbdd tra, Date fecha, HashMap<Integer, Integer> map, int trienios) throws IOException {

        //TODO PDF NOMINAS2021 PÁG 17
        Date fechaAlta = tra.getFechaAlta();
        LocalDate fechaNomina = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaTra = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        double importeTrieniosConflicto = this.importeTrienios(trienios - 1, map);
        double importeTrieniosSinConflicto = this.importeTrienios(trienios, map);
        int conflicto = this.calcularConflicto(fechaNomina, fechaTra);
        if (fechaNomina.getYear() != fechaTra.getYear()) {
            double bruto = (salarioBase + complementos + importeTrieniosConflicto * conflicto + importeTrieniosSinConflicto * (12 - conflicto));
            if (conflicto != 0) {
                if (calcularTotalExtras(conflicto) == 1) {
                    return bruto + importeTrieniosSinConflicto + importeTrieniosConflicto;
                } else {
                    return bruto + 2 * importeTrieniosSinConflicto;
                }
            } else {
                if (tra.isProrrateo()) {
                    return (salarioBase + complementos + importeTrieniosSinConflicto * 14);
                } else {
                    return (salarioBase + complementos + importeTrieniosSinConflicto * 14);
                }
            }
        } else {
            if (tra.isProrrateo()) {
                double base = (salarioBase + complementos);
                double mesesTra = fechaTra.getMonthValue() - 1;
                return base * mesesTra * (1 - (mesesTra / 12));
            } else {
                double baseMeses = ((salarioBase + complementos) / 14) * (13 - conflicto);
                double extraJunio = ((salarioBase + complementos) / 14) * calcularExtraJunio(conflicto);
                double extraDiciembre = ((salarioBase + complementos) / 14) * calcularExtraDiciembre(conflicto);
                return baseMeses + extraJunio + extraDiciembre;
            }
        }
    }

    public double importeTrienios(int trienios, HashMap<Integer, Integer> map) {
        if (trienios > 0) {
            for (Entry<Integer, Integer> entry : map.entrySet()) {
                if (entry.getKey() == trienios) {
                    return entry.getValue();
                }
            }
        } else {
            return 0;
        }
        return 0;
    }

    private int calcularConflicto(LocalDate fechaNomina, LocalDate fechaTra) {
        if ((fechaNomina.getYear() - fechaTra.getYear()) % 3 == 0) {
            return fechaTra.getMonthValue();
        } else {
            //NO HAY CONFLICTO
            return 0;
        }
    }

    public double calcularBrutoMensual(double salarioBase, double complemento, double prorateo, double antiguedad) {
        //TODO Hacer calculos pag 17 y sucesivas pdf
        return salarioBase + complemento + prorateo + antiguedad;
    }

    public double calcularSalarioBaseMes(double salarioBase) {
        return salarioBase / 14;
    }

    public double calcularComplementoMes(double complemento) {
        return complemento / 14;
    }

    public double calcularProrrateo(double salarioBase, double complento, double antiguedad, boolean prorrateo) {
        if (prorrateo) {
            return salarioBase / 6 + complento / 6 + antiguedad / 6;
        } else {
            return 0.0;
        }
    }

    private int calcularTotalExtras(int conflicto) {
        if (conflicto < 6) {
            return 2;
        } else {
            return 1;
        }
    }

    private int calcularExtraJunio(int mes) {
        if (mes >= 6) {
            return 0;
        } else {
            return 1 - (mes / 6);
        }
    }

    private int calcularExtraDiciembre(int mes) {
        if (mes <= 6) {
            return 1;
        } else {
            return 1 - ((mes - 6) / 6);
        }
    }

    public double calcularSeguridadSocial(File excel) throws IOException {
        CoordinadorExcel manejadora = new CoordinadorExcel();
        return manejadora.obtenerPorcentajesExcel(excel, 1);
    }

    public double calcularDesempleo(File excel) throws IOException {
        CoordinadorExcel manejadora = new CoordinadorExcel();
        return manejadora.obtenerPorcentajesExcel(excel, 2);
    }

    public double calcularFormacion(File excel) throws IOException {
        CoordinadorExcel manejadora = new CoordinadorExcel();
        return manejadora.obtenerPorcentajesExcel(excel, 3);
    }

    public double calcularContingenciasEmpresario(File excel) throws IOException {
        CoordinadorExcel manejadora = new CoordinadorExcel();
        return manejadora.obtenerPorcentajesExcel(excel, 4);
    }

    public double calcularFogasa(File excel) throws IOException {
        CoordinadorExcel manejadora = new CoordinadorExcel();
        return manejadora.obtenerPorcentajesExcel(excel, 5);
    }

    public double calcularDesempleoEmpresario(File excel) throws IOException {
        CoordinadorExcel manejadora = new CoordinadorExcel();
        return manejadora.obtenerPorcentajesExcel(excel, 6);
    }

    public double calcularFormacionEmpresario(File excel) throws IOException {
        CoordinadorExcel manejadora = new CoordinadorExcel();
        return manejadora.obtenerPorcentajesExcel(excel, 7);
    }

    public double calcularAccidentesTrabajo(File excel) throws IOException {
        CoordinadorExcel manejadora = new CoordinadorExcel();
        return manejadora.obtenerPorcentajesExcel(excel, 8);
    }

    public double calcularIRPF(double brutoAnual, HashMap<Integer, Double> map) {
        double valor = 0;
        Iterator<Integer> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            int clave = iter.next();
            valor = map.get(clave);
            if (brutoAnual < clave) {
                break;
            }
        }
        return valor;
    }
}
