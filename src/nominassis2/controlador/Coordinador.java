package nominassis2.controlador;

import java.util.LinkedList;
import java.util.List;
import nominassis2.modelo.Logic;
import nominassis2.modelo.vo.Empresas;
import nominassis2.exceptions.ObjectNotFoundException;
import nominassis2.modelo.vo.Categorias;
import nominassis2.modelo.vo.Nomina;
import nominassis2.modelo.vo.Trabajadorbbdd;

public class Coordinador {

    /**
     * Instancia propia de la clase
     */
    private static Coordinador instancia;

    /**
     * Constructor de la clase Coordinador
     *
     * Vacio debido a que los atributos de la clase son introducidos por setters
     * (Singleton)
     */
    private Coordinador() {
    }

    /**
     * Devuelve una instancia del Coordinador del contexto actual de la
     * aplicacion
     *
     * @return una instancia del Coordinador del contexto actual de la
     * aplicacion
     */
    public static Coordinador getCoordinador() {
        if (instancia == null) {
            instancia = new Coordinador();
        }
        return instancia;
    }

    /**
     * Devuelve una instancia de la logica en el contactual de la aplicaci *
     *
     * @return una instancia de la logica en el contexto actual de la aplicacion
     */
    public Logic getLogic() {
        return Logic.getLogic();
    }

    /**
     * Delega la responsabilidad de buscar el trabajador a la clase Logica
     *
     * @param nif - nif del trabajador
     * @return el trabajadorVO encontrado
     * @throws ObjectNotFoundException - si el objecto no se encuentra en la
     * base de datos
     */
    public List buscarTrabajador(String nif) throws ObjectNotFoundException, ExceptionInInitializerError {
        return Logic.getLogic().buscarTrabajador(nif);
    }

    /**
     * Delega la responsabilidad de actualizar las empresas a la clase Logica
     *
     * @param idEmpresa - id de la empresa a actualizar
     * @throws nominassis2.exceptions.ObjectNotFoundException
     */
    public void actualizarEmpresas(int idEmpresa) throws ObjectNotFoundException, ExceptionInInitializerError {
        Logic.getLogic().actualizarEmpresas(idEmpresa);
    }

    /**
     * Delega la responsabilidad de eliminar el trabajador y su nomina a la
     * clase Logica
     *
     * @param empresa - objeto empresa del trabajador
     * @throws nominassis2.exceptions.ObjectNotFoundException
     */
    public void borrarTrabajadorNomina(Empresas empresa) throws ObjectNotFoundException, ExceptionInInitializerError {
        Logic.getLogic().borrarTrabajadorNomina(empresa);
    }
    
    
    public void addNominas(LinkedList<Nomina> listaNominas) {
        Logic.getLogic().addNominas(listaNominas);
    }

    public boolean buscarEmpresa(Empresas empresa) {
        return Logic.getLogic().buscarEmpresa(empresa);
    }

    public void addEmpresa(Empresas empresa) {
        Logic.getLogic().addEmpresa(empresa);
    }

    public void actualizarEmpresa(Empresas empresa) {
        Logic.getLogic().actualizarEmpresas(empresa);
    }

    public boolean buscarCategoria(Categorias categoria) {
        return Logic.getLogic().buscarCategoria(categoria);
    }

    public void addCategoria(Categorias categoria) {
        Logic.getLogic().addCategoria(categoria);
    }

    public boolean buscarTrabajador(Trabajadorbbdd trabajador) {
        return Logic.getLogic().buscarTrabajadorBBDD(trabajador);
    }

    public void addTrabajador(Trabajadorbbdd trabajador) {
        Logic.getLogic().addTrabajador(trabajador);
    }

    public boolean buscarNomina(Nomina nomina) {
        return Logic.getLogic().buscarNomina(nomina);
    }

    public void addNomina(Nomina nomina) {
        Logic.getLogic().addNomina(nomina);
    }

    public void actualizarCategoria(Categorias categoria) {
        Logic.getLogic().actualizarEmpresas(categoria);
    } 

    public void actualizarTrabajador(Trabajadorbbdd trabajador) {
        Logic.getLogic().actualizarTrabajador(trabajador);
    }

    public void actualizarNomina(Nomina nomina) {
        Logic.getLogic().actualizarNomina(nomina);
    }

    public Categorias getCategoria(String nombre) {
        return Logic.getLogic().getCategoria(nombre);
    }

    public List<Empresas> getEmpresas() throws ObjectNotFoundException {
        return Logic.getLogic().getEmpresas();
    }
    
    public int getContadorIDEmpresas() throws ObjectNotFoundException {
        return Logic.getLogic().getContadorIDEmpresa();
    }

    public List<Trabajadorbbdd> getTrabajadores() throws ObjectNotFoundException {
        return Logic.getLogic().getTrabajadores();
    }

    public int getIdNomina() {
        return  Logic.getLogic().getIdNomina();
    }

}
