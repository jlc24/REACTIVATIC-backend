package bo.sddpi.reactivatic.modulos.aods;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Negocios;

@Mapper
public interface INegociosAod {

    @Select("SELECT * FROM negocios WHERE idnegocio=#{idnegocio} AND idbeneficio=#{idbeneficio}")
    @Results({
        @Result(property = "beneficioempresa", column = "idbeneficioempresa", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IBeneficiosempresasAod.dato")),
        @Result(property = "persona", column = "idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    })
    Negocios dato(Long idnegocio, Long idbeneficio);

    @Select("SELECT * FROM negocios n " +
            "LEFT JOIN beneficios b ON b.idbeneficio = n.idbeneficio " +
            "LEFT JOIN beneficiosempresas be ON be.idbeneficioempresa = n.idbeneficioempresa " +
            "LEFT JOIN empresas e ON e.idempresa = be.idempresa " +
            "LEFT JOIN rubros r ON e.idrubro = r.idrubro " +
            "LEFT JOIN municipios m ON e.idmunicipio = m.idmunicipio " +
            "LEFT JOIN representantes rep ON e.idrepresentante = rep.idrepresentante " +
            "JOIN personas p ON p.idpersona = n.idpersona " +
            "LEFT JOIN clientes c ON c.idpersona = p.idpersona " +
            "WHERE n.idbeneficio=#{beneficio} AND CONCAT(COALESCE(e.empresa, ''), ' ', COALESCE(r.rubro, ''), ' ', COALESCE(m.municipio, ''), ' ', " +
            "COALESCE(p.primerNombre, ''), ' ', COALESCE(p.primerApellido, ''), ' ', COALESCE(p.segundoApellido, ''), ' ', " +
            "COALESCE(p.dip, '')) " +
            "ILIKE '%' || #{buscar} || '%' " +
            "ORDER BY " +
                "CASE " + 
                "    WHEN n.fecha = CURRENT_DATE THEN 0 " + 
                "    ELSE 1 " + 
                "END, " +
            "n.fecha, n.estadoempresa, n.estadopersona, n.horainicio  ASC ")  
    @Results({
         @Result(property = "beneficioempresa", column = "idbeneficioempresa", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IBeneficiosempresasAod.dato")),
         @Result(property = "persona", column = "idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    })
    List<Negocios> datos(String buscar, Long beneficio);

    @Select("SELECT COUNT(n.idnegocio) FROM negocios n " +
            "LEFT JOIN beneficios b ON b.idbeneficio = n.idbeneficio " +
            "LEFT JOIN beneficiosempresas be ON be.idbeneficioempresa = n.idbeneficioempresa " +
            "LEFT JOIN empresas e ON e.idempresa = be.idempresa " +
            "LEFT JOIN rubros r ON e.idrubro = r.idrubro " +
            "LEFT JOIN municipios m ON e.idmunicipio = m.idmunicipio " +
            "LEFT JOIN representantes rep ON e.idrepresentante = rep.idrepresentante " +
            "JOIN personas p ON p.idpersona = n.idpersona " +
            "LEFT JOIN clientes c ON c.idpersona = p.idpersona " +
            "WHERE n.idbeneficio=#{beneficio} AND CONCAT(COALESCE(e.empresa, ''), ' ', COALESCE(r.rubro, ''), ' ', COALESCE(m.municipio, ''), ' ', " +
            "COALESCE(p.primerNombre, ''), ' ', COALESCE(p.primerApellido, ''), ' ', COALESCE(p.segundoApellido, ''), ' ', " +
            "COALESCE(p.dip, '')) " +
            "ILIKE '%' || #{buscar} || '%' ")
    Integer cantidad(String buscar, Long beneficio);

    @Select("SELECT DISTINCT n.fecha FROM negocios n " +
            " JOIN beneficios b ON b.idbeneficio = n.idbeneficio " +
            " JOIN beneficiosempresas be ON be.idbeneficioempresa = n.idbeneficioempresa " +
            " JOIN empresas e ON e.idempresa = be.idempresa " +
            "WHERE e.idempresa=#{empresa} AND n.idbeneficio=#{beneficio} ORDER BY n.fecha ASC ")
    List<Negocios> fechas(Long empresa, Long beneficio);

    @Select("SELECT n.idnegocio, n.horainicio, n.horafin, n.idpersona FROM negocios n " +
            "JOIN beneficiosempresas be ON be.idbeneficioempresa = n.idbeneficioempresa " +
            " JOIN empresas e ON e.idempresa = be.idempresa " +
            "WHERE e.idempresa=#{empresa} AND n.fecha=#{fecha} AND n.idbeneficio=#{beneficio} ORDER BY n.horainicio ASC ")
    List<Negocios> horas(LocalDate fecha, Long empresa, Long beneficio);

    @Insert("INSERT INTO negocios (tipo, idbeneficio,  idbeneficioempresa, horainicio, horafin, duracion, mesa, fecha) "+
            "VALUES (#{tipo}, #{idbeneficio},  #{idbeneficioempresa}, #{horainicio}, #{horafin}, #{duracion}, #{mesa}, #{fecha}) ")
    void adicionar(Negocios negocio);

    @Update("UPDATE negocios SET tipo=#{tipo}, idpersona=#{idpersona}, estadoempresa=#{estadoempresa}, estadopersona=#{estadopersona}, mesa=#{mesa} WHERE idnegocio=#{idnegocio}")
    void modificar(Negocios negocio);

    @Delete("DELETE FROM negocios WHERE idnegocio=#{idnegocio}  ")
    void eliminar(Long idnegocio);

    @Delete("DELETE FROM negocios WHERE idbeneficio=#{idbeneficio} AND idbeneficioempresa=#{idbeneficioempresa} ")
    void eliminarempresa(Long idbeneficio, Long idbeneficioempresa);

    @Select("SELECT * FROM negocios n " +
            "LEFT JOIN beneficios b ON b.idbeneficio = n.idbeneficio " +
            "LEFT JOIN beneficiosempresas be ON be.idbeneficioempresa = n.idbeneficioempresa " +
            "LEFT JOIN empresas e ON e.idempresa = be.idempresa " +
            "LEFT JOIN rubros r ON e.idrubro = r.idrubro " +
            "LEFT JOIN municipios m ON e.idmunicipio = m.idmunicipio " +
            "LEFT JOIN representantes rep ON e.idrepresentante = rep.idrepresentante " +
            "JOIN personas p ON p.idpersona = n.idpersona " +
            "LEFT JOIN clientes c ON c.idpersona = p.idpersona " +
            "WHERE n.fechainicio=#{fecha} AND CONCAT(COALESCE(e.empresa, ''), ' ', COALESCE(r.rubro, ''), ' ', COALESCE(m.municipio, ''), ' ', " +
            "COALESCE(p.primerNombre, ''), ' ', COALESCE(p.primerApellido, ''), ' ', COALESCE(p.segundoApellido, ''), ' ', " +
            "COALESCE(p.dip, '')) " +
            "ILIKE '%' || #{buscar} || '%' " +
            "ORDER BY n.horainicio, n.estadoempresa, n.estadopersona ASC ")   
    @Results({
         @Result(property = "beneficioempresa", column = "idbeneficioempresa", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IBeneficiosempresasAod.dato")),
         @Result(property = "persona", column = "idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    })
    List<Negocios> negocios(String buscar, String fecha);

    @Select("SELECT mesa FROM negocios " +
            "WHERE idbeneficio =#{idbeneficio} AND fecha = #{fecha} " +
            "AND horainicio = #{hora} " +
            "AND idpersona IS NOT NULL " +
            "AND mesa IS NOT NULL;")
    List<Negocios> mesas(Long idbeneficio, LocalDate fecha, LocalTime hora);
}
