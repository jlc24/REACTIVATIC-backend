package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Empresas;
import bo.sddpi.reactivatic.modulos.entidades.Solicitudes;

@Mapper
public interface ISolicitudesAod {

    @Select("SELECT idsolicitud, idsolicitud as idfore2, idempresa, idempresa as idfore1, idcliente, solicitud, fecha, hora, solicitudes.estado FROM solicitudes join clientes using(idcliente) join personas using(idpersona) join usuarios using(idpersona) where idusuario=#{idusuario} ORDER BY fecha desc, hora desc LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "empresa", column = "idfore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato")),
        @Result(property = "solicitudproductos", column = "idfore2", many = @Many(select = "bo.sddpi.reactivatic.modulos.aods.ISolicitudesproductosAod.datosl" ))
    })
    List<Solicitudes> datos(Long idusuario, Integer pagina, Integer cantidad);

    @Select("SELECT count(idsolicitud) FROM solicitudes join clientes using(idcliente) join personas using(idpersona) join usuarios using(idpersona) where idusuario=#{idusuario} ")
    Integer cantidad(Long idusuario);

    @Select("SELECT s.idsolicitud, s.idsolicitud as ifore1, s.idcliente, s.solicitud, s.idempresa, s.estado, s.created_at, " +
            "(SELECT COUNT(*) FROM solicitudesproductos sp WHERE sp.idsolicitud = s.idsolicitud) AS cantidad_productos " +
            "FROM solicitudes s " +
            "JOIN empresas e using(idempresa) " +
            "JOIN representantes r using(idrepresentante) " +
            "JOIN personas p using(idpersona) " +
            "JOIN usuarios u using(idpersona) " +
            "WHERE u.idusuario=#{idusuario} " +
            "ORDER BY s.created_at DESC " +
            "LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "cliente", column = "idcliente", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IClientesAod.dato")),
        @Result(property = "cantidadProductos", column = "cantidad_productos"),
        @Result(property = "solicitudproductos", column = "ifore1", many = @Many(select = "bo.sddpi.reactivatic.modulos.aods.ISolicitudesproductosAod.datosl" ))
    })
    List<Solicitudes> datose(Long idusuario, Integer pagina, Integer cantidad);

    @Select("SELECT count(s.idsolicitud) " +
            "FROM solicitudes s " +
            "JOIN empresas e using(idempresa) " +
            "JOIN representantes r using(idrepresentante) " +
            "JOIN personas p using(idpersona) " +
            "JOIN usuarios u using(idpersona) " +
            "WHERE u.idusuario=#{idusuario} AND s.estado=false")
    Integer cantidadvp(Long idusuario);
    @Select("SELECT count(s.idsolicitud) " +
            "FROM solicitudes s " +
            "JOIN empresas e using(idempresa) " +
            "JOIN representantes r using(idrepresentante) " +
            "JOIN personas p using(idpersona) " +
            "JOIN usuarios u using(idpersona) " +
            "WHERE u.idusuario=#{idusuario} AND s.estado=true")
    Integer cantidadvr(Long idusuario);

    @Select("select * from procesasolicitud(#{idcliente}, #{idusuario})")
    Integer procesasolicitud(Long idcliente, Long idusuario);

    @Update("UPDATE solicitudes SET estado=true WHERE idsolicitud = #{idsolicitud}")
    void actualizarestado(Long idsolicitud);
    @Update("UPDATE solicitudesproductos SET estado=true WHERE idsolicitud = #{idsolicitud}")
    void actualizarestadoproductos(Long idsolicitud);

    @Select("select distinct correo as correo from carritos join productos using(idproducto) join empresas using(idempresa) where idcliente=#{idcliente}")
    List<Empresas> buscarporempresas(Long idcliente);

    @Select("SELECT idsolicitud, idsolicitud as idfore2, idempresa, idempresa as idfore1, idcliente, idcliente as idfore3, solicitud, fecha, hora, solicitudes.estado FROM solicitudes join clientes using(idcliente) join personas using(idpersona) join usuarios using(idpersona) where concat(primerapellido) ilike '%'||#{buscar}||'%' ORDER BY fecha desc, hora desc LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "empresa", column = "idfore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato")),
        @Result(property = "solicitudproductos", column = "idfore2", many = @Many(select = "bo.sddpi.reactivatic.modulos.aods.ISolicitudesproductosAod.datosl" )),
        @Result(property = "cliente", column = "idfore3", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IClientesAod.dato"))
    })
    List<Solicitudes> datosr(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idsolicitud) FROM solicitudes join clientes using(idcliente) join personas using(idpersona) join usuarios using(idpersona) where concat(primerapellido) ilike '%'||#{buscar}||'%'")
    Integer cantidadr(String buscar);

    @Select("SELECT idsolicitud, idsolicitud as idfore2, idempresa, idempresa as idfore1, idcliente, idcliente as idfore3, solicitud, fecha, hora, solicitudes.estado FROM solicitudes join clientes using(idcliente) join personas using(idpersona) join usuarios using(idpersona) where concat(primerapellido) ilike '%'||#{buscar}||'%' ORDER BY fecha desc, hora desc ")
    @Results({
        @Result(property = "empresa", column = "idfore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato")),
        @Result(property = "solicitudproductos", column = "idfore2", many = @Many(select = "bo.sddpi.reactivatic.modulos.aods.ISolicitudesproductosAod.datosl" )),
        @Result(property = "cliente", column = "idfore3", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IClientesAod.dato"))
    })
    List<Solicitudes> datosrepo(String buscar);
}


