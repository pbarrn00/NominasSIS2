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
        Nomina aux = getNominaBD(nomina.getIdNomina());
        if(nomina.equals(aux)) {
            tx = session.beginTransaction();
            session.saveOrUpdate(actualizarNomina(nomina, aux));
            tx.commit();
        }
    }
    
    private Nomina getNominaBD(int id){
        CategoriaDAO.getInstance();
        Query query = session.createQuery("Select n from Nomina n where n.idNomina=:param1");
        query.setParameter("param1", id);
        List<Nomina> l = query.list();
        return l.get(0);
    }
    
    private Nomina actualizarNomina(Nomina nomina, Nomina aux) {
        aux.setNumeroTrienios(nomina.getNumeroTrienios());
        aux.setImporteTrienios(nomina.getImporteTrienios());
        aux.setImporteSalarioMes(nomina.getImporteSalarioMes());
        aux.setImporteComplementoMes(nomina.getImporteComplementoMes());
        aux.setValorProrrateo(nomina.getValorProrrateo());
        aux.setBrutoAnual(nomina.getBrutoAnual());
        aux.setIrpf(nomina.getIrpf());
        aux.setImporteIrpf(nomina.getIrpf());
        aux.setBaseEmpresario(nomina.getBaseEmpresario());
        aux.setSeguridadSocialEmpresario(nomina.getSeguridadSocialEmpresario());
        aux.setImporteSeguridadSocialEmpresario(nomina.getImporteSeguridadSocialEmpresario());
        aux.setDesempleoEmpresario(nomina.getDesempleoEmpresario());
        aux.setImporteDesempleoEmpresario(nomina.getImporteDesempleoEmpresario());
        aux.setFormacionEmpresario(nomina.getFormacionEmpresario());
        aux.setImporteFormacionEmpresario(nomina.getImporteFormacionEmpresario());
        aux.setAccidentesTrabajoEmpresario(nomina.getAccidentesTrabajoEmpresario());
        aux.setImporteAccidentesTrabajoEmpresario(nomina.getImporteAccidentesTrabajoEmpresario());
        aux.setFogasaempresario(nomina.getFogasaempresario());
        aux.setImporteFogasaempresario(nomina.getImporteFogasaempresario());
        aux.setSeguridadSocialTrabajador(nomina.getSeguridadSocialTrabajador());
        aux.setImporteSeguridadSocialTrabajador(nomina.getImporteSeguridadSocialTrabajador());
        aux.setDesempleoTrabajador(nomina.getDesempleoTrabajador());
        aux.setImporteDesempleoTrabajador(nomina.getImporteDesempleoTrabajador());
        aux.setFormacionTrabajador(nomina.getFormacionTrabajador());
        aux.setImporteFormacionTrabajador(nomina.getImporteFormacionTrabajador());
        aux.setCosteTotalEmpresario(nomina.getCosteTotalEmpresario());
        aux.setTrabajadorbbdd(aux.getTrabajadorbbdd());
        return aux;
    }
    
    public void cerrarSesion() {
        session.close();
    }  
}
