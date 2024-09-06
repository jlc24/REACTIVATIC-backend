package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Productos;

@Mapper
public interface IProductosAod {

    @Select("SELECT * " +
            "FROM productos " +
            "WHERE idempresa=#{idempresa} " +
            "and concat(producto, descripcion) ilike '%'||#{buscar}||'%' ORDER BY idproducto DESC " +
            "LIMIT #{cantidad} OFFSET #{pagina} ")
    List<Productos> datos(Long idempresa, String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT p.* " +
            "FROM productos p " +
            "JOIN empresas e ON e.idempresa=p.idempresa " +
            "JOIN subrubros ur ON e.idsubrubro = ur.idsubrubro " +
            "JOIN rubros r ON ur.idrubro = r.idrubro " +
            "JOIN localidades l ON e.idlocalidad = l.idlocalidad " +
            "JOIN municipios m ON l.idmunicipio = m.idmunicipio " +
            "WHERE concat(p.producto, ' ', e.empresa, ' ', ur.subrubro, ' ', r.rubro, ' ', l.localidad, ' ', m.municipio, ' ') " +
            "ILIKE '%'||#{buscar}||'%' " +
            "AND r.rubro ILIKE '%'||#{rubro}||'%' " +
            "ORDER BY idproducto DESC " +
            "LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "empresa", column = "idempresa", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato"))
    })
    List<Productos> datosAdmin(String buscar, Integer pagina, Integer cantidad, String rubro);

    @Select("SELECT count(idproducto) FROM productos where idempresa=#{idempresa} and concat(producto, descripcion) ilike '%'||#{buscar}||'%' ")
    Integer cantidad(Long idempresa, String buscar);

    @Select("SELECT count(p.idproducto) " +
            "FROM productos p " +
            "JOIN empresas e ON e.idempresa=p.idempresa " +
            "JOIN subrubros ur ON e.idsubrubro = ur.idsubrubro " +
            "JOIN rubros r ON ur.idrubro = r.idrubro " +
            "JOIN localidades l ON e.idlocalidad = l.idlocalidad " +
            "JOIN municipios m ON l.idmunicipio = m.idmunicipio " +
            "WHERE concat(p.producto, ' ', e.empresa, ' ', ur.subrubro, ' ', r.rubro, ' ', l.localidad, ' ', m.municipio, ' ') " +
            "ILIKE '%'||#{buscar}||'%' " +
            "AND r.rubro ILIKE '%'||#{rubro}||'%' ")
    Integer cantidadAdmin(String buscar, String rubro);

    @Select("SELECT count(p.idproducto) FROM productos p JOIN empresas e ON e.idempresa=p.idempresa where concat(p.producto, p.descripcion) ilike '%'||#{buscar}||'%' ")
    Integer cantidadTotal(String buscar);

    @Select("SELECT idproducto, idempresa, idempresa as ifore1, producto, descripcion, preciocompra, precioventa, cantidad FROM productos where idproducto=#{id} ")
    @Results({
        @Result(property = "empresa", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato"))
    })
    Productos dato(Long id);

    @Options(useGeneratedKeys = true, keyProperty = "idproducto", keyColumn = "idproducto")
    @Insert("insert into productos(idempresa, producto, descripcion) values (#{idempresa}, #{producto}, #{descripcion}) returning idproducto")
    void adicionar(Productos dato);

    @Update("update productos set idempresa=#{idempresa}, producto=#{producto}, descripcion=#{descripcion} where idproducto=#{idproducto} ")
    void modificar(Productos dato);

    @Delete("DELETE FROM productos WHERE idproducto=#{idproducto}")
    void eliminar(Long idproducto);

    @Update("UPDATE personas SET estado=#{estado} WHERE idpersona=#{idpersona}")
    void cambiarestado(Productos producto);

    // @Select("SELECT idproducto, idempresa, idempresa as ifore1, producto, productos.descripcion, preciocompra, precioventa, cantidad FROM productos join empresas using(idempresa) join subrubros using(idsubrubro) join rubros using(idrubro) WHERE cantidad>0 and concat(rubro,empresa,producto) ilike '%'||#{buscar}||'%' ORDER BY producto LIMIT #{cantidad} OFFSET #{pagina} ")
    // @Results({
    //     @Result(property = "empresa", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato"))
    // })
    // List<Productos> datoscat(String buscar, Integer pagina, Integer cantidad);
    @Select("SELECT p.idproducto, p.idempresa, e.idempresa as ifore1, p.producto, p.descripcion, p.preciocompra, p.precioventa, p.cantidad " +
            "FROM productos p " +
            "JOIN empresas e ON e.idempresa=p.idempresa " +
            "JOIN subrubros sr ON sr.idsubrubro=e.idsubrubro " +
            "JOIN rubros r ON r.idrubro=sr.idrubro " +
            "WHERE  concat(p.producto, ' ', sr.subrubro, ' ', r.rubro, ' ', e.empresa) "+
            "ILIKE '%'||#{buscar}||'%' " +
            "ORDER BY p.producto LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "empresa", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato"))
    })
    List<Productos> datoscat(String buscar, Integer pagina, Integer cantidad);

    // @Select("SELECT count(idproducto) FROM productos join empresas using(idempresa) join subrubros using(idsubrubro) join rubros using(idrubro) where cantidad>0 and concat(rubro,empresa,producto) ilike '%'||#{buscar}||'%' ")
    @Select("SELECT count(p.idproducto) " +
            "FROM productos p " +
            "JOIN empresas e ON e.idempresa=p.idempresa " +
            "JOIN subrubros sr ON sr.idsubrubro=e.idsubrubro " +
            "JOIN rubros r ON r.idrubro=sr.idrubro " +
            "WHERE  concat(p.producto, ' ', sr.subrubro, ' ', r.rubro, ' ', e.empresa) "+
            "ILIKE '%'||#{buscar}||'%' ")
    Integer cantidadcat(String buscar);

    // @Select("SELECT idproducto, idempresa, idempresa as ifore1, producto, productos.descripcion, preciocompra, precioventa, cantidad FROM productos join empresas using(idempresa) join subrubros using(idsubrubro) join rubros using(idrubro) WHERE cantidad>0 and idproducto=#{id} ")
    @Select("SELECT p.idproducto, p.idempresa, e.idempresa as ifore1, p.producto, p.descripcion, p.preciocompra, p.precioventa, p.cantidad " +
            "FROM productos p " +
            "JOIN empresas e ON e.idempresa=p.idempresa " +
            "JOIN subrubros sr ON sr.idsubrubro=e.idsubrubro " +
            "JOIN rubros r ON r.idrubro=sr.idrubro " +
            "WHERE p.idproducto=#{id} ")
    @Results({
        @Result(property = "empresa", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato"))
    })
    Productos datocat(Long id);

}







