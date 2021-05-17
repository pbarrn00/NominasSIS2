/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominassis2.modelo.dao;

import java.util.List;
import nominassis2.modelo.HibernateUtil;
import nominassis2.modelo.vo.Categorias;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author pablo
 */
public class CategoriaDAO {
    private static CategoriaDAO instancia = null;
    SessionFactory sf = null;
    Session session = null;
    Transaction tx = null;

    private CategoriaDAO() {
        sf = HibernateUtil.getSessionFactory();
        session = sf.openSession();
    }

    public static CategoriaDAO getInstance() {
        if (instancia == null) {
            instancia = new CategoriaDAO();
        }
        return instancia;
    }

    public boolean existeCategoria(Categorias categoria) {
        CategoriaDAO.getInstance();
        Query query = session.createQuery("Select c from Categorias c where c.nombreCategoria=:param1");
        query.setParameter("param1", categoria.getNombreCategoria());
        List<Categorias> l = query.list();
        if (!l.isEmpty()) {
            categoria.setIdCategoria(l.get(0).getIdCategoria());
            return true;
        }
        return false;
    }

    public void add(Categorias categoria) {
        CategoriaDAO.getInstance();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(categoria);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println(e.getMessage());
        } 
    }
    
    public void update(Categorias categoria) {
        CategoriaDAO.getInstance();
        Categorias aux = getCategoriaBD(categoria.getIdCategoria());
        if (!categoria.equals(aux)) {
            aux.setSalarioBaseCategoria(categoria.getSalarioBaseCategoria());
            aux.setComplementoCategoria(categoria.getComplementoCategoria());
            tx = session.beginTransaction();
            session.saveOrUpdate(aux);
            tx.commit();
        }
        
    }
    
    private Categorias getCategoriaBD(int id){
        CategoriaDAO.getInstance();
        Query query = session.createQuery("Select c from Categorias c where c.idCategoria=:param1");
        query.setParameter("param1", id);
        List<Categorias> l = query.list();
        return l.get(0);
    }
    
    public Categorias getCategoriaNombreBD(String nombre){
        CategoriaDAO.getInstance();
        Query query = session.createQuery("Select c from Categorias c where c.nombreCategoria=:param1");
        query.setParameter("param1", nombre);
        List<Categorias> l = query.list();
        return l.get(0);
    }
    
    public void cerrarSesion() {
        session.close();
    }   
}
