package com.swisherdominicana.molde.model;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "interacciones", schema = "public")
public class Interacciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memo;
    private Timestamp fecha;

    private Boolean activo = true;

    @ManyToOne(optional=false)
    @JoinColumn(name="usuario_id", insertable=true, updatable=false, nullable = false)
    private Usuario usuario;


    @ManyToOne(optional=false)
    @JoinColumn(name="cliente_id", insertable=true, updatable=false,nullable = false)
    private Cliente cliente;


    public Interacciones() {
    }

    public Interacciones(Long id, String memo, Timestamp fecha, Boolean activo) {
        this.id = id;
        this.memo = memo;
        this.fecha = fecha;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }


    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Interacciones{" +
                "id=" + id +
                ", memo='" + memo + '\'' +
                ", fecha=" + fecha +
                ", activo=" + activo +
                '}';
    }
}
