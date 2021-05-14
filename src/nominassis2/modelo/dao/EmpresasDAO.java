/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominassis2.modelo.dao;

import java.util.Iterator;
import java.util.List;

import nominassis2.exceptions.ObjectNotFoundException;
import nominassis2.modelo.HibernateUtil;
import nominassis2.modelo.vo.Empresas;
import nominassis2.modelo.vo.Trabajadorbbdd;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author pablo
 */
public class EmpresasDAO {

    private static EmpresasDAO instancia = null;
    SessionFactory sf = null;
    Session session = null;
    Transaction tx = null;

    private EmpresasDAO() {
        sf = HibernateUtil.getSessionFactory();
        session = sf.openSession();
    }

    public static EmpresasDAO getInstance() {
        if (instancia == null) {
            instancia = new EmpresasDAO();
        }
        return instancia;
    }

    public void actualizarEmpresas(int idEmpresa) throws ObjectNotFoundException, ExceptionInInitializerError {
        List empresasParaCambiar = this.getListaEmpresas(idEmpresa);
        EmpresasDAO.getInstance();
        try {
            for (Iterator iterator = empresasParaCambiar.iterator(); iterator.hasNext();) {
                tx = session.beginTransaction();
                Empresas empresa = (Empresas) iterator.next();
                empresa.setNombre(empresa.getNombre() + "2021");
                //empresa.setNombre(empresa.getNombre());
                session.saveOrUpdate(empresa);
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }

    }

    private List getListaEmpresas(int id) throws ObjectNotFoundException, ExceptionInInitializerError {
        EmpresasDAO.getInstance();;
        List<Trabajadorbbdd> results;
        try {
            tx = session.beginTransaction();
            String hql = "FROM Empresas E WHERE idEmpresa != :id";
            Query query = session.createQuery(hql);
            query.setParameter("id", id);
            results = query.list();
            if (results.isEmpty()) {
                throw new ObjectNotFoundException("No hay ninguna empresa en la base de datos");
            }
            tx.commit();
        } catch (ExceptionInInitializerError e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        return results;
    }

    public boolean existeEmpresa(Empresas e) {
        EmpresasDAO.getInstance();
        Query query = session.createQuery("Select e from Empresas e where e.cif=:param1");
        query.setParameter("param1", e.getCif());
        List<Empresas> l = query.list();
        if (!l.isEmpty()) {
            e.setIdEmpresa(l.get(0).getIdEmpresa());
            return true;
        }
        return false;
    }

    public void add(Empresas empresa) {
        EmpresasDAO.getInstance();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(empresa);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println(e.getMessage());
        } 
    }
    
    public void update(Empresas empresa) {
        EmpresasDAO.getInstance();
        tx = session.beginTransaction();
        session.saveOrUpdate(empresa);
        tx.commit();
    }
    
    public void cerrarSesion() {
        session.close();
    }
}
