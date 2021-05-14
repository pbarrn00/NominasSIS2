package nominassis2.controlador;

import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

// Clases para la creacion y manejo de XML
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class CoordinadorXML {

    // Objecto que representa al documento XML
    private Document documento;
    private Element trabajadores;
    private Element cuentas;

    /**
     * Creamos el documento XML
     *
     * @throws ParserConfigurationException
     */
    public CoordinadorXML(String tipo) throws ParserConfigurationException {
        // Creamos los objectos de creacion de Documentos XML
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder constructor = docFactory.newDocumentBuilder();

        // Creamos el documento XML
        documento = constructor.newDocument();
        
        if(tipo.equals("Cuentas")){
            cuentas = documento.createElement("Cuentas");
            documento.appendChild(cuentas);
        }else{
            trabajadores = documento.createElement("Trabajadores");
            documento.appendChild(trabajadores);
        }
    }

    /**
     * Creamos un documento con un elemento principal y varios subElementos
     */
    public void createTrabajadoresXML(String nombreTra, String ap1, String ap2, String empresaTra, String categoriaTra, int id) { 
        Element trabajador = documento.createElement("Trabajador");
        trabajadores.appendChild(trabajador);
        
        Attr attr = documento.createAttribute("id");
        attr.setValue(""+id); 
        trabajador.setAttributeNode(attr);
        
        Element nombre = documento.createElement("Nombre");
        nombre.setTextContent(nombreTra);
        trabajador.appendChild(nombre);
        
        Element primerAp = documento.createElement("PrimerApellido");
        Text textPrimerAp = documento.createTextNode(ap1);
        primerAp.appendChild(textPrimerAp);
        trabajador.appendChild(primerAp);
        
        Element segundoAp = documento.createElement("SegundoApellido");
        Text textSegundoAp = documento.createTextNode(ap2);
        segundoAp.appendChild(textSegundoAp);
        trabajador.appendChild(segundoAp);
        
        Element empresa = documento.createElement("Empresa");
        Text textEmpresa = documento.createTextNode(empresaTra);
        empresa.appendChild(textEmpresa);
        trabajador.appendChild(empresa);
                
        Element categoria = documento.createElement("Categoria");
        Text textCategoria = documento.createTextNode(categoriaTra);
        categoria.appendChild(textCategoria);
        trabajador.appendChild(categoria);
        
    }
    
    /**
     * Envia el documento XML a un archivo
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public void escribirArchivoTrabajadores() throws TransformerConfigurationException, TransformerException {
        // Creamos el objecto transformador
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        
        // Archivo donde almacenaremos el XML
        File archivo = new File("./recursos/Errores.xml");
        
        // Fuente de datos, en este caso el documento XML
        DOMSource source = new DOMSource(documento);
        // Resultado, el cual almacena en el archivo indicado
        StreamResult result = new StreamResult(archivo);
        // Transformamos de ña fuente DOM a el resultado, lo que almacena todo en el archivo
        transformer.transform(source, result);
    }
    
    /**
     * Creamos un documento con un elemento principal y varios subElementos
     */
    public void createCuentasXML(String nombreTra, String ap1, String ap2, String empresaTra, String cuentaErroneo, String ibanCorrecto, int id) { 
        Element cuenta = documento.createElement("Cuenta");
        cuentas.appendChild(cuenta);
        
        Attr attr = documento.createAttribute("id");
        attr.setValue(""+id); 
        cuenta.setAttributeNode(attr);
        
        Element nombre = documento.createElement("Nombre");
        nombre.setTextContent(nombreTra);
        cuenta.appendChild(nombre);
        
        Element aps = documento.createElement("Apellidos");
        Text textAp = documento.createTextNode(ap1+" "+ap2);
        aps.appendChild(textAp);
        cuenta.appendChild(aps);
        
        Element empresa = documento.createElement("Empresa");
        Text textEmpresa = documento.createTextNode(empresaTra);
        empresa.appendChild(textEmpresa);
        cuenta.appendChild(empresa);
                
        Element ccc = documento.createElement("CCCErroneo");
        Text textCCC = documento.createTextNode(cuentaErroneo);
        ccc.appendChild(textCCC);
        cuenta.appendChild(ccc);
        
        Element iban = documento.createElement("IBANCorrecto");
        Text textIban = documento.createTextNode(ibanCorrecto);
        iban.appendChild(textIban);
        cuenta.appendChild(iban);
        
    }
    
    /**
     * Envia el documento XML a un archivo
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public void escribirArchivoCuentas() throws TransformerConfigurationException, TransformerException {
        // Creamos el objecto transformador
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        
        // Archivo donde almacenaremos el XML
        File archivo = new File("./recursos/ErroresCCC.xml");
        
        // Fuente de datos, en este caso el documento XML
        DOMSource source = new DOMSource(documento);
        // Resultado, el cual almacena en el archivo indicado
        StreamResult result = new StreamResult(archivo);
        // Transformamos de ña fuente DOM a el resultado, lo que almacena todo en el archivo
        transformer.transform(source, result);
    }
}