/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominassis2.modelo.dao;

import java.util.LinkedList;
import java.util.List;
import nominassis2.modelo.HibernateUtil;
import nominassis2.modelo.vo.Nomina;
import nominassis2.modelo.vo.Trabajadorbbdd;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Pablo Javier Barrio Navarro
 *         Pablo de la Era Martínez
 *         Diego Fernández Velasco <3
 */
public class NominaDAO {
    private static NominaDAO instancia = null;
    SessionFactory sf = null;
    Session session = null;
    Transaction tx = null;

    private NominaDAO() {
        sf = HibernateUtil.getSessionFactory();
        session = sf.openSession();
    }

    public static NominaDAO getInstance() {
        if (instancia == null) {
            instancia = new NominaDAO();
        }
        return instancia;
    }

    public void add(LinkedList<Nomina> listaNominas) {
        
    }

    public void addDB(Nomina nomina) {
        NominaDAO.getInstance();
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(nomina);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println(e.getMessage());
        } 
    }

    public boolean buscarNomina(Nomina nomina) {
       NominaDAO.getInstance();
        Query query = session.createQuery("Select N FROM Nomina N WHERE (N.mes=:param1) AND (N.anio=:param2) AND (N.brutoNomina=:param4) AND (N.liquidoNomina=:param5)");
        query.setParameter("param1", nomina.getMes());
        query.setParameter("param2", nomina.getAnio());
        //query.setParameter("param3", nomina.getTrabajadorbbdd().getIdTrabajador());
        query.setParameter("param4", nomina.getBrutoNomina());
        query.setParameter("param5", nomina.getLiquidoNomina());
        List<Nomina> l = query.list();
        if (!l.isEmpty()) {
            nomina.setIdNomina(l.get(0).getIdNomina());
            return true;
        }
        return false;
    }
    
    public void update(Nomina nomina) {
        NominaDAO.getInstance();
        tx = session.beginTransaction();
        session.saveOrUpdate(nomina);
        tx.commit();
    }
    
    public void cerrarSesion() {
        session.close();
    }  
}
