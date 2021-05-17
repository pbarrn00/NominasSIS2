package nominassis2.modelo.vo;
// Generated 12-mar-2021 17:34:57 by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Transient;

/**
 * Trabajadorbbdd generated by hbm2java
 */
public class Trabajadorbbdd implements java.io.Serializable {

    private int idTrabajador;
    private Categorias categorias;
    private Empresas empresas;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String nifnie;
    private String email;
    private Date fechaAlta;
    private String codigoCuenta;
    private String iban;
    private Set nominas = new HashSet(0);

    @Transient
    private String nombreEmpresa;
    @Transient
    private String cifEmpresa;
    @Transient
    private String categoria;
    @Transient
    private boolean prorrateo;

    public Trabajadorbbdd() {
    }

    public Trabajadorbbdd(int idTrabajador, Categorias categorias, Empresas empresas, String nombre, String apellido1, String nifnie) {
        this.idTrabajador = idTrabajador;
        this.categorias = categorias;
        this.empresas = empresas;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.nifnie = nifnie;
    }

    public Trabajadorbbdd(int idTrabajador, Categorias categorias, Empresas empresas, String nombre, String apellido1, String apellido2, String nifnie, String email, Date fechaAlta, String codigoCuenta, String iban, Set nominas) {
        this.idTrabajador = idTrabajador;
        this.categorias = categorias;
        this.empresas = empresas;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.nifnie = nifnie;
        this.email = email;
        this.fechaAlta = fechaAlta;
        this.codigoCuenta = codigoCuenta;
        this.iban = iban;
        this.nominas = nominas;
    }

    public Trabajadorbbdd(String nombreEmpresa, String cifEmpresa, String categoria, Date fechaAlta, String nombre, String apellido1, String apellido2, String nifnie, boolean prorrateo, String iban, String email, String codigoCuenta) {
        this.nombreEmpresa = nombreEmpresa;
        this.cifEmpresa = cifEmpresa;
        this.categoria = categoria;
        this.fechaAlta = fechaAlta;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.nifnie = nifnie;
        this.prorrateo = prorrateo;
        this.iban = iban;
        this.email = email;
        this.codigoCuenta = codigoCuenta;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public String getCifEmpresa() {
        return cifEmpresa;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isProrrateo() {
        return prorrateo;
    }

    public int getIdTrabajador() {
        return this.idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public Categorias getCategorias() {
        return this.categorias;
    }

    public void setCategorias(Categorias categorias) {
        this.categorias = categorias;
    }

    public Empresas getEmpresas() {
        return this.empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return this.apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return this.apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getNifnie() {
        return this.nifnie;
    }

    public void setNifnie(String nifnie) {
        this.nifnie = nifnie;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaAlta() {
        return this.fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getCodigoCuenta() {
        return this.codigoCuenta;
    }

    public void setCodigoCuenta(String codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }

    public String getIban() {
        return this.iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Set getNominas() {
        return this.nominas;
    }

    public void setNominas(Set nominas) {
        this.nominas = nominas;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Trabajadorbbdd other = (Trabajadorbbdd) obj;
        if (this.idTrabajador != other.idTrabajador) {
            return false;
        }
        if (this.prorrateo != other.prorrateo) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.apellido1, other.apellido1)) {
            return false;
        }
        if (!Objects.equals(this.apellido2, other.apellido2)) {
            return false;
        }
        if (!Objects.equals(this.nifnie, other.nifnie)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.codigoCuenta, other.codigoCuenta)) {
            return false;
        }
        if (!Objects.equals(this.iban, other.iban)) {
            return false;
        }
        if (!Objects.equals(this.nombreEmpresa, other.nombreEmpresa)) {
            return false;
        }
        if (!Objects.equals(this.cifEmpresa, other.cifEmpresa)) {
            return false;
        }
        if (!Objects.equals(this.categoria, other.categoria)) {
            return false;
        }
        if (!Objects.equals(this.categorias, other.categorias)) {
            return false;
        }
        if (!Objects.equals(this.empresas, other.empresas)) {
            return false;
        }
        if (!Objects.equals(this.fechaAlta, other.fechaAlta)) {
            return false;
        }
        if (!Objects.equals(this.nominas, other.nominas)) {
            return false;
        }
        return true;
    }
    
    
}
