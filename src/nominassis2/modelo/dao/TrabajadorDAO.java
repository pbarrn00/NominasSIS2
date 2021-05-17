package nominassis2.modelo.dao;

/* Clase que realiza la conexion con BBDD*/

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nominassis2.exceptions.ObjectNotFoundException;
import nominassis2.modelo.vo.Trabajadorbbdd;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import nominassis2.modelo.HibernateUtil;
import nominassis2.modelo.vo.Categorias;
import nominassis2.modelo.vo.Empresas;
import nominassis2.modelo.vo.Nomina;
import org.hibernate.Query;
import org.hibernate.Transaction;


public class TrabajadorDAO {

    /**
     * Constructor de la clase TrabajadorDAO
     */
    private static TrabajadorDAO instancia = null;
    SessionFactory sf = null;
    Session session = null;
    Transaction tx = null;

    private TrabajadorDAO() {
        sf = HibernateUtil.getSessionFactory();
        session = sf.openSession();
    }

    public static TrabajadorDAO getInstance() {
        if (instancia == null) {
            instancia = new TrabajadorDAO();
        }
        return instancia;
    }

    public List buscarTrabajadorBD(String nif) throws ObjectNotFoundException, ExceptionInInitializerError {
        sf = HibernateUtil.getSessionFactory();
        session = sf.openSession();
        List<Trabajadorbbdd> results;
        try {
            tx = session.beginTransaction();
            String hql = "SELECT T FROM Trabajadorbbdd T WHERE nifnie = :nif";
            Query query = session.createQuery(hql);
            query.setParameter("nif", nif);
            results = query.list();
            if(results.isEmpty()) {
                throw new ObjectNotFoundException("El trabajador con DNI: "+nif+", no se encuentra en la base de datos");
            }
            tx.commit();
        }
        catch(ExceptionInInitializerError e){
            if (tx!=null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return results;
    }
    
    public void borrarTrabajadorNomina(Empresas empresa) throws ObjectNotFoundException, ExceptionInInitializerError {
        sf = HibernateUtil.getSessionFactory();
        session = sf.openSession();
        List<Trabajadorbbdd> results;
        try {
            tx = session.beginTransaction();
            String hql = "SELECT T FROM Trabajadorbbdd T WHERE T.empresas.idEmpresa != :id";
            Query query = session.createQuery(hql);
            query.setParameter("id", empresa.getIdEmpresa());
            results = query.list();
            if(results.isEmpty()) {
                throw new ObjectNotFoundException("No se han encontrado trabajadores de otras empresas y no pueden ser borrados");
            }
            tx.commit();
            for (int i = 0; i < results.size(); i++) {
                Trabajadorbbdd trabajador = results.get(i);
                Set nominas = trabajador.getNominas();
                Iterator iterNominas = nominas.iterator();
                while(iterNominas.hasNext()){
                    Nomina nomina = (Nomina) iterNominas.next();
                    tx = session.beginTransaction();
                    String HQLborrado = "DELETE Nomina n WHERE n.idNomina = :param1";
                    session.createQuery(HQLborrado).setParameter("param1", nomina.getIdNomina()).executeUpdate();
                    tx.commit();
                }
            }
            
            for (int i = 0; i < results.size(); i++) {
                tx = session.beginTransaction();
                String HQLborrado = "DELETE Trabajadorbbdd t WHERE t.empresas.idEmpresa != :param1";
                session.createQuery(HQLborrado).setParameter("param1", empresa.getIdEmpresa()).executeUpdate();
                tx.commit();
            }
        }
        catch(ExceptionInInitializerError e){
            if (tx!=null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean buscarTrabajador(Trabajadorbbdd trabajador) {
        TrabajadorDAO.getInstance();
        Query query = session.createQuery("Select T FROM Trabajadorbbdd T WHERE T.nifnie=:param1");
        query.setParameter("param1", trabajador.getNifnie());
        List<Trabajadorbbdd> l = query.list();
        for(int i=0; i<l.size(); i++) {
            if(l.get(i).getNombre()==trabajador.getNombre() && l.get(i).getFechaAlta()==trabajador.getFechaAlta()){
                trabajador.setIdTrabajador(l.get(0).getIdTrabajador());
                return true;
            }
        }
        return false;
    }

    public void add(Trabajadorbbdd trabajador) {
        TrabajadorDAO.getInstance();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(trabajador);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println(e.getMessage());
        } 
    }
    
    public void update(Trabajadorbbdd trabajador) {
        TrabajadorDAO.getInstance();
        Trabajadorbbdd aux = getTrabajadorBD(trabajador.getIdTrabajador());
        if(!trabajador.equals(aux)) {
            tx = session.beginTransaction();
            session.saveOrUpdate(actualizarTrabajador(trabajador, aux));
            tx.commit(); 
        }
        
    }
    
    private Trabajadorbbdd getTrabajadorBD(int id){
        CategoriaDAO.getInstance();
        Query query = session.createQuery("Select t from Trabajadorbbdd t where c.idTrabajador=:param1");
        query.setParameter("param1", id);
        List<Categorias> l = query.list();
        return l.get(0);
    }
    
    private Trabajadorbbdd actualizarTrabajador(Trabajadorbbdd trabajador, Trabajadorbbdd aux) {
        aux.setApellido1(trabajador.getApellido1());
        aux.setApellido2(trabajador.getApellido2());
        aux.setNifnie(trabajador.getNifnie());
        aux.setEmail(trabajador.getEmail());
        aux.setCodigoCuenta(trabajador.getCodigoCuenta());
        aux.setIban(trabajador.getIban());
        aux.setEmpresas(trabajador.getEmpresas());
        aux.setCategorias(trabajador.getCategorias());
        return aux;
    }
    
    public void cerrarSesion() {
        session.close();
    }
}
