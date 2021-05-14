/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominassis2.vista;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import nominassis2.controlador.Coordinador;
import nominassis2.exceptions.ObjectNotFoundException;
import nominassis2.modelo.dao.CategoriaDAO;
import nominassis2.modelo.dao.EmpresasDAO;
import nominassis2.modelo.dao.NominaDAO;
import nominassis2.modelo.dao.TrabajadorDAO;
import nominassis2.modelo.vo.Categorias;
import nominassis2.modelo.vo.Empresas;
import nominassis2.modelo.vo.Nomina;
import nominassis2.modelo.vo.Trabajadorbbdd;
import nominassis2.utils.Utils;

/**
 *
 * @author diego
 */
public class PracticaCuatro {
    
    public PracticaCuatro() {

    }

    public void practicaCuatro(LinkedList<Nomina> listaNominas, String fecha) throws FileNotFoundException, MalformedURLException, ObjectNotFoundException {
        
        /**
         * PARTE 1.
         */
        boolean esExtra;
        DecimalFormat df = new DecimalFormat("0.00");

        for (Nomina nomina : listaNominas) {
            esExtra = false;
            if (nomina.getBaseEmpresario() == 0.0) {
                esExtra = true;
            }
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            PdfWriter writer = new PdfWriter("./recursos/nominas/"+getNombrePdf(nomina, esExtra)+".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc, PageSize.LETTER);
            Table tabla1 = new Table(2);
            tabla1.setWidth(500);

            Paragraph nom = new Paragraph(nomina.getTrabajadorbbdd().getNombreEmpresa());
            Paragraph cif = new Paragraph(nomina.getTrabajadorbbdd().getCifEmpresa());

            Paragraph dir1 = new Paragraph("Avenida de la Facultad - 6");
            Paragraph dir2 = new Paragraph("24001 León");

            Cell cell1 = new Cell();
            cell1.setBorder(new SolidBorder(1));
            cell1.setWidth(250);
            cell1.setTextAlignment(TextAlignment.CENTER);

            cell1.add(nom);
            cell1.add(cif);
            cell1.add(dir1);
            cell1.add(dir2);
            tabla1.addCell(cell1);

            Cell cell2 = new Cell();
            cell2.setPadding(10);
            cell2.setTextAlignment(TextAlignment.RIGHT);
            cell2.add(new Paragraph("IBAN: " + nomina.getTrabajadorbbdd().getIban()));
            cell2.add(new Paragraph("Bruto anual: " + nomina.getBrutoAnual()));
            cell2.add(new Paragraph("Categoría: " + nomina.getTrabajadorbbdd().getCategoria()));
            cell2.add(new Paragraph("Fecha de alta: " + formatoFecha.format(nomina.getTrabajadorbbdd().getFechaAlta())));
            cell2.setBorder(new SolidBorder(ColorConstants.WHITE, 0));
            tabla1.addCell(cell2);

            Table tabla2 = new Table(2);
            tabla2.setWidth(500);
            String image = "recursos/logo.png";
            Image img;
            Cell cellimg = new Cell();
            try {
                img = new Image(ImageDataFactory.create(image));
                img.scaleAbsolute(250, 100);
                img.setPadding(10);
                cellimg.add(img);
            } catch (MalformedURLException ex) {
                System.out.println("ex");
            }

            cellimg.setBorderRight(new SolidBorder(ColorConstants.BLACK, 1));
            cellimg.setBorderTop(new SolidBorder(ColorConstants.WHITE, 1));
            cellimg.setBorderBottom(new SolidBorder(ColorConstants.WHITE, 1));
            cellimg.setBorderLeft(new SolidBorder(ColorConstants.WHITE, 1));
            Cell cell3 = new Cell();
            cell3.setTextAlignment(TextAlignment.RIGHT);
            Paragraph p22 = new Paragraph("Destinatario:");
            p22.setBold();
            p22.setTextAlignment(TextAlignment.LEFT);
            p22.setPaddingLeft(10);
            cell3.add(p22);
            String aux;
            if (nomina.getTrabajadorbbdd().getApellido2() == null) {
                aux = nomina.getTrabajadorbbdd().getNombre() + " " + nomina.getTrabajadorbbdd().getApellido1();
            } else {
                aux = nomina.getTrabajadorbbdd().getNombre() + " " + nomina.getTrabajadorbbdd().getApellido1() + " " + nomina.getTrabajadorbbdd().getApellido2();
            }
            cell3.add(new Paragraph(aux));
            cell3.add(new Paragraph("DNI: " + nomina.getTrabajadorbbdd().getNifnie()));
            cell3.add(new Paragraph("Avenida de la facultad"));
            cell3.add(new Paragraph("24001 León"));
            cell3.setPaddingTop(20);
            cell3.setPaddingRight(10);
            cell3.setFontSize(10);
            cell3.setWidth(250);
            cell3.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

            tabla2.addCell(cellimg);
            tabla2.addCell(cell3);

            Cell cell4 = new Cell();
            String mesString;
            String aux1;
            if (esExtra) {
                mesString = Utils.getNombreMes(nomina.getMes());
                aux1 = "Nómina: Extra de " + mesString.toLowerCase() + " de " + String.valueOf(nomina.getAnio());
            } else {
                mesString = Utils.getNombreMes(nomina.getMes());
                aux1 = "Nómina: " + mesString + " de " + String.valueOf(nomina.getAnio());
            }
            cell4.add(new Paragraph(aux1));
            cell4.setPaddingTop(50);
            cell4.setPaddingBottom(10);
            cell4.setBold();
            cell4.setTextAlignment(TextAlignment.CENTER);

            Table tabla5 = new Table(5);
            tabla5.setWidth(500);
            tabla5.setHeight(140);

            Cell cell5 = new Cell();
            cell5.add(new Paragraph("Conceptos").setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1)));
            cell5.add(new Paragraph("Salario Base"));
            cell5.add(new Paragraph("Prorrateo"));
            cell5.add(new Paragraph("Complemento"));
            cell5.add(new Paragraph("Antigüedad"));
            cell5.add(new Paragraph("Contingencias generales"));
            cell5.add(new Paragraph("Desempleo"));
            cell5.add(new Paragraph("Cuota formación"));
            cell5.add(new Paragraph("IRPF"));
            cell5.setTextAlignment(TextAlignment.LEFT);
            cell5.setFontSize(10);
            cell5.setWidth(120);
            tabla5.addCell(cell5);

            Cell cell6 = new Cell();
            cell6.add(new Paragraph("Cantidad").setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1)));
            cell6.add(new Paragraph("30 días"));
            cell6.add(new Paragraph("30 días"));
            cell6.add(new Paragraph("30 días"));
            cell6.add(new Paragraph(nomina.getNumeroTrienios() + " Trienios"));
            cell6.add(new Paragraph(nomina.getSeguridadSocialTrabajador().toString() + "% de " + df.format(nomina.getBaseEmpresario())));
            cell6.add(new Paragraph(nomina.getDesempleoTrabajador().toString() + "% de " + df.format(nomina.getBaseEmpresario())));
            cell6.add(new Paragraph(nomina.getFormacionTrabajador().toString() + "% de " + df.format(nomina.getBaseEmpresario())));
            cell6.add(new Paragraph(nomina.getIrpf().toString() + "% de " + df.format(nomina.getBrutoNomina())));
            cell6.setTextAlignment(TextAlignment.CENTER);
            cell6.setFontSize(10);
            cell6.setWidth(120);
            tabla5.addCell(cell6);

            
            Cell cell7 = new Cell();
            cell7.add(new Paragraph("Imp. Unitario").setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1)));
            cell7.add(new Paragraph(String.valueOf(df.format(nomina.getImporteSalarioMes() / 30))));
            cell7.add(new Paragraph(String.valueOf(df.format(nomina.getValorProrrateo() / 30))));
            cell7.add(new Paragraph(String.valueOf(df.format(nomina.getImporteComplementoMes() / 30))));
            cell7.add(new Paragraph(String.valueOf(df.format(nomina.getImporteTrienios() / nomina.getNumeroTrienios()))));
            cell7.setTextAlignment(TextAlignment.CENTER);
            cell7.setFontSize(10);
            cell7.setWidth(80);
            tabla5.addCell(cell7);

            Cell cell8 = new Cell();
            cell8.add(new Paragraph("Devengo").setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1)));
            cell8.add(new Paragraph(df.format(nomina.getImporteSalarioMes())));
            cell8.add(new Paragraph(df.format(nomina.getValorProrrateo())));
            cell8.add(new Paragraph(df.format(nomina.getImporteComplementoMes())));
            cell8.add(new Paragraph(df.format(nomina.getImporteTrienios())));
            cell8.setTextAlignment(TextAlignment.CENTER);
            cell8.setFontSize(10);
            cell8.setWidth(90);
            tabla5.addCell(cell8);

            Cell cell9 = new Cell();
            cell9.add(new Paragraph("Deduccion").setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1)));
            cell9.add(new Paragraph("\n"));
            cell9.add(new Paragraph("\n"));
            cell9.add(new Paragraph("\n"));
            cell9.add(new Paragraph("\n"));
            cell9.add(new Paragraph(df.format(nomina.getImporteSeguridadSocialTrabajador())));
            cell9.add(new Paragraph(df.format(nomina.getImporteDesempleoTrabajador())));
            cell9.add(new Paragraph(df.format(nomina.getImporteFormacionTrabajador())));
            cell9.add(new Paragraph(df.format(nomina.getImporteIrpf())));
            cell9.setTextAlignment(TextAlignment.CENTER);
            cell9.setFontSize(10);
            cell9.setWidth(100);

            tabla5.addCell(cell9);

            Paragraph p = new Paragraph("Total deducciones");
            p.add(new Tab());
            p.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            Double deducciones = nomina.getImporteSeguridadSocialTrabajador() + nomina.getImporteDesempleoTrabajador() + nomina.getImporteFormacionTrabajador() + nomina.getImporteIrpf();
            p.add(df.format(deducciones));
            p.setFontSize(10);
            p.setPaddingRight(75);
            p.setPaddingBottom(-8);
            Paragraph p1 = new Paragraph("Total devengos");
            p1.add(new Tab());
            p1.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            p1.add(df.format(nomina.getBrutoNomina()));
            p1.setFontSize(10);
            p1.setPaddingRight(165);

            Paragraph p2 = new Paragraph("Liquido a percibir \t\t\t\t\t\t\t\t\t\t\t " + df.format(nomina.getLiquidoNomina()));
            p2.setFontSize(10);
            p2.setPaddingRight(50);
            p2.setPaddingBottom(15);
            p2.setTextAlignment(TextAlignment.RIGHT);

            Paragraph p3 = new Paragraph("Calculo empresario: BASE");
            p3.add(new Tab());
            p3.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            p3.add(df.format(nomina.getBaseEmpresario()));
            p3.setFontSize(10);
            p3.setPaddingRight(50);
            p3.setFontColor(ColorConstants.GRAY);

            Paragraph p4 = new Paragraph("Contigencias comunies empresario " + df.format(nomina.getImporteSeguridadSocialEmpresario()) + "%");
            p4.add(new Tab());
            p4.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            p4.add(df.format(nomina.getImporteSeguridadSocialEmpresario()));
            p4.setPaddingBottom(-8);
            p4.setFontSize(10);
            p4.setPaddingRight(50);

            p4.setPaddingTop(15);
            p4.setFontColor(ColorConstants.GRAY);

            Paragraph p5 = new Paragraph("Desempleo " + df.format(nomina.getImporteDesempleoEmpresario()) + "%");
            p5.add(new Tab());
            p5.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            p5.add(df.format(nomina.getImporteDesempleoEmpresario()));
            p5.setFontSize(10);
            p5.setPaddingBottom(-8);
            p5.setPaddingRight(50);
            p5.setFontColor(ColorConstants.GRAY);

            Paragraph p6 = new Paragraph("Formación " + df.format(nomina.getImporteFormacionEmpresario()) + "%");
            p6.add(new Tab());
            p6.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            p6.add(df.format(nomina.getImporteFormacionEmpresario()));
            p6.setFontSize(10);
            p6.setPaddingRight(50);
            p6.setPaddingBottom(-8);
            p6.setFontColor(ColorConstants.GRAY);

            Paragraph p7 = new Paragraph("Accidentes de Trabajo " + df.format(nomina.getAccidentesTrabajoEmpresario()) + "%");
            p7.add(new Tab());
            p7.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            p7.add(df.format(nomina.getImporteAccidentesTrabajoEmpresario()));
            p7.setFontSize(10);
            p7.setPaddingBottom(-8);
            p7.setPaddingRight(50);
            p7.setFontColor(ColorConstants.GRAY);

            Paragraph p8 = new Paragraph("FOGASA " + df.format(nomina.getFogasaempresario()) + "%");
            p8.add(new Tab());
            p8.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            p8.add(df.format(nomina.getImporteFogasaempresario()));
            p8.setFontSize(10);
            p8.setPaddingRight(50);
            p8.setFontColor(ColorConstants.GRAY);

            Paragraph p9 = new Paragraph("Total empresario");
            p9.add(new Tab());
            p9.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            Double totalEmpresario = nomina.getImporteSeguridadSocialEmpresario() + nomina.getImporteDesempleoEmpresario() + nomina.getImporteFormacionEmpresario() + nomina.getImporteAccidentesTrabajoEmpresario() + nomina.getImporteFogasaempresario();
            p9.add(df.format(totalEmpresario));
            p9.setFontSize(10);
            p9.setPaddingRight(50);
            p9.setFontColor(ColorConstants.GRAY);

            Paragraph p10 = new Paragraph("COSTE TOTAL TRABAJADOR: ");
            p10.add(new Tab());
            p10.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            p10.add(df.format(nomina.getCosteTotalEmpresario()));
            p10.setFontSize(12);
            p10.setPaddingRight(18);
            p10.setFontColor(ColorConstants.RED);
            Table tabla10 = new Table(1);
            tabla10.setWidth(500);
            Cell cell10 = new Cell();
            cell10.add(p10);
            tabla10.addCell(cell10);
            tabla10.setMarginTop(50);
            tabla10.setMarginRight(50);
            tabla10.setBorder(new SolidBorder(3));

            doc.add(tabla1);
            doc.add(tabla2);
            doc.add(cell4);
            doc.add(tabla5);
            doc.add(p);
            doc.add(p1);
            doc.add(new LineSeparator(new SolidLine(0.5f)).setWidth(500));
            doc.add(p2);
            doc.add(new LineSeparator(new SolidLine(0.5f)).setWidth(500));
            doc.add(p3);
            doc.add(new LineSeparator(new SolidLine(0.5f)).setWidth(500));
            doc.add(p4);
            doc.add(p5);
            doc.add(p6);
            doc.add(p7);
            doc.add(p8);
            doc.add(new LineSeparator(new SolidLine(0.5f)).setWidth(500));
            doc.add(p9);
            doc.add(tabla10);
            doc.close();
        }

        /**
         * PARTE 2.
         */
        ejercicio2(listaNominas);
    }
    
    private void ejercicio2(LinkedList<Nomina> listaNominas){
        /* PREGUNTAR DUDA ID EMPRESA RANDOM */
        DecimalFormat df = new DecimalFormat("0.00");
        int idEmpresa = 23;
        int idCategoria = 32;
        int idTrabajador = 15;
        for (Nomina nomina : listaNominas) {
            Empresas empresa = new Empresas();
            empresa.setCif(nomina.getTrabajadorbbdd().getCifEmpresa());
            empresa.setNombre(nomina.getTrabajadorbbdd().getNombreEmpresa());
            if (!Coordinador.getCoordinador().buscarEmpresa(empresa)) {
               empresa.setIdEmpresa(idEmpresa);
               Coordinador.getCoordinador().addEmpresa(empresa);
               idEmpresa++;
            }else{
                //Coordinador.getCoordinador().actualizarEmpresa(empresa);
            }
            
            Categorias categoria = new Categorias();
            categoria.setNombreCategoria(nomina.getTrabajadorbbdd().getCategoria());
            categoria.setSalarioBaseCategoria(Utils.round2decimasl(nomina.getImporteSalarioMes()*14));
            categoria.setComplementoCategoria(Utils.round2decimasl(nomina.getImporteComplementoMes()*14));
            if(!Coordinador.getCoordinador().buscarCategoria(categoria)) {
                categoria.setIdCategoria(idCategoria);
                idCategoria++;
                Coordinador.getCoordinador().addCategoria(categoria);
            }else{
                Coordinador.getCoordinador().actualizarCategoria(categoria);
            }
            
            Trabajadorbbdd trabajador = new Trabajadorbbdd();
            trabajador.setNombre(nomina.getTrabajadorbbdd().getNombre());
            trabajador.setApellido1(nomina.getTrabajadorbbdd().getApellido1());
            if(nomina.getTrabajadorbbdd().getApellido2()!=null) {
                trabajador.setApellido2(nomina.getTrabajadorbbdd().getApellido2());
            }
            trabajador.setNifnie(nomina.getTrabajadorbbdd().getNifnie());
            trabajador.setEmail(nomina.getTrabajadorbbdd().getEmail());
            trabajador.setFechaAlta(nomina.getTrabajadorbbdd().getFechaAlta());
            trabajador.setCodigoCuenta(nomina.getTrabajadorbbdd().getCodigoCuenta());
            trabajador.setIban(nomina.getTrabajadorbbdd().getIban());
            trabajador.setEmpresas(empresa);
            trabajador.setCategorias(categoria);
            if(!Coordinador.getCoordinador().buscarTrabajador(trabajador)) {
                trabajador.setIdTrabajador(idTrabajador);
                idTrabajador++;
                Coordinador.getCoordinador().addTrabajador(trabajador);
            }else{
                Coordinador.getCoordinador().actualizarTrabajador(trabajador);
            }
            
            nomina.setTrabajadorbbdd(trabajador);
            if(!Coordinador.getCoordinador().buscarNomina(nomina)) {
                Coordinador.getCoordinador().addNomina(nomina);
            }else{
                Coordinador.getCoordinador().actualizarNomina(nomina);
            }
        }
        EmpresasDAO.getInstance().cerrarSesion();
        CategoriaDAO.getInstance().cerrarSesion();
        TrabajadorDAO.getInstance().cerrarSesion();
        NominaDAO.getInstance().cerrarSesion();
    }
    
    public static String getNombrePdf(Nomina nomina, boolean extra) {
        Trabajadorbbdd trabajador = nomina.getTrabajadorbbdd();
        String resultado = "";
        String nif = trabajador.getNifnie().toUpperCase();
        String nombre = trabajador.getNombre();
        String ap1 = trabajador.getApellido1();
        String ap2 = "";
        if (trabajador.getApellido2() != null && !trabajador.getApellido2().equals("")) {
            ap2 = trabajador.getApellido2();
        }
        String mes = Utils.getNombreMes(nomina.getMes());
        String anio = String.valueOf(nomina.getAnio());
        resultado = nif + nombre + ap1 + ap2 + mes + anio;
        if (extra) {
            resultado += "EXTRA";
        }
        return resultado;
    }
}
