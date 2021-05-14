package nominassis2.modelo;

import java.util.LinkedList;
import java.util.List;

import nominassis2.exceptions.ObjectNotFoundException;
import nominassis2.modelo.dao.CategoriaDAO;
import nominassis2.modelo.dao.EmpresasDAO;
import nominassis2.modelo.dao.NominaDAO;
import nominassis2.modelo.dao.TrabajadorDAO;
import nominassis2.modelo.vo.Categorias;
import nominassis2.modelo.vo.Empresas;
import nominassis2.modelo.vo.Nomina;
import nominassis2.modelo.vo.Trabajadorbbdd;

public class Logic {

    /**
     * La propia instancia de la clase.
     */
    private static Logic instance;

    /**
     * Constructor de la calse Logica.
     *
     * Vacio ya que no posee ningun atributo
     */
    private Logic() {
    }

    /**
     * Devuelve la instancia de la Logica del contexto de la aplicación actual.
     *
     * @return la instancia de la Logica del contexto de aplicación actual.
     */
    public static Logic getLogic() {
        if (instance == null) {
            instance = new Logic();
        }
        return instance;
    }

    /**
     * Delega la responsabilidad de buscar al trabajador a través de la clase
     * DAO
     *
     * @param nif - nif del trabajador
     * @return el empleado encontrado
     * @throws ObjectNotFoundException - si el objecto no se encuentra en la
     * base de datos
     */
    public List buscarTrabajador(String nif) throws ObjectNotFoundException {
        return TrabajadorDAO.getInstance().buscarTrabajadorBD(nif);
    }

    /**
     * Delega la responsabilidad de actualizar las empresas a la clase DAO
     *
     * @param idEmpresa id de la empresa a actualizar
     * @throws nominassis2.exceptions.ObjectNotFoundException
     */
    public void actualizarEmpresas(int idEmpresa) throws ObjectNotFoundException, ExceptionInInitializerError {
        EmpresasDAO.getInstance().actualizarEmpresas(idEmpresa);
    }

    /**
     * Delega la responsabilidad de e eliminar el trabajador y su nomina a
     * través de la clase DAO
     *
     * @param empresa - objeto empresa del trabajador
     * @throws nominassis2.exceptions.ObjectNotFoundException
     */
    public void borrarTrabajadorNomina(Empresas empresa) throws ObjectNotFoundException, ExceptionInInitializerError {
        TrabajadorDAO.getInstance().borrarTrabajadorNomina(empresa);
    }

    public void addNominas(LinkedList<Nomina> listaNominas) {
       NominaDAO.getInstance().add(listaNominas);       
    }

    public boolean buscarEmpresa(Empresas empresa) {
       return EmpresasDAO.getInstance().existeEmpresa(empresa);
    }

    public void addEmpresa(Empresas empresa) {
        EmpresasDAO.getInstance().add(empresa);
    }

    public void actualizarEmpresas(Empresas empresa) {
        EmpresasDAO.getInstance().update(empresa);
    }

    public boolean buscarCategoria(Categorias categoria) {
        return CategoriaDAO.getInstance().existeCategoria(categoria);
    }

    public void addCategoria(Categorias categoria) {
        CategoriaDAO.getInstance().add(categoria);
    }

    public boolean buscarTrabajadorBBDD(Trabajadorbbdd trabajador) {
        return TrabajadorDAO.getInstance().buscarTrabajador(trabajador);
    }

    public void addTrabajador(Trabajadorbbdd trabajador) {
        TrabajadorDAO.getInstance().add(trabajador);
    }

    public void addNomina(Nomina nomina) {
        NominaDAO.getInstance().addDB(nomina);
    }

    public boolean buscarNomina(Nomina nomina) {
        return NominaDAO.getInstance().buscarNomina(nomina);
    }

    public void actualizarEmpresas(Categorias categoria) {
        CategoriaDAO.getInstance().update(categoria);
    }

    public void actualizarTrabajador(Trabajadorbbdd trabajador) {
        TrabajadorDAO.getInstance().update(trabajador);
    }

    public void actualizarNomina(Nomina nomina) {
        NominaDAO.getInstance().update(nomina);
    }
    
    

}
