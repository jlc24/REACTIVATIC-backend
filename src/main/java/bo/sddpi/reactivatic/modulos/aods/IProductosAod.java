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
import bo.sddpi.reactivatic.modulos.entidades.Reportes;

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
            "ORDER BY p.idproducto DESC " +
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

    @Update("UPDATE productos SET estado=#{estado} WHERE idproducto=#{idproducto}")
    void cambiarestado(Productos producto);

    @Select("<script>" +
                "SELECT p.idproducto, p.idempresa, e.idempresa as ifore1, p.producto, p.descripcion, " +
                "COALESCE(min_precio.min_precio, p.precioventa) AS min_precio, " +
                "COALESCE(max_precio.max_precio, p.precioventa) AS max_precio " +
                "FROM productos p " +
                "JOIN empresas e ON e.idempresa=p.idempresa " +
                "JOIN rubros r ON r.idrubro=e.idrubro " +
                "JOIN municipios m ON m.idmunicipio=e.idmunicipio " +
                "LEFT JOIN ( " +
                "    SELECT idproducto, MIN(precio) AS min_precio " +
                "    FROM precios " +
                "    GROUP BY idproducto " +
                ") min_precio ON min_precio.idproducto = p.idproducto " +
                "LEFT JOIN ( " +
                "    SELECT idproducto, MAX(precio) AS max_precio " +
                "    FROM precios " +
                "    GROUP BY idproducto " +
                ") max_precio ON max_precio.idproducto = p.idproducto " +
                "WHERE p.estado=true AND CONCAT(p.producto, ' ', r.rubro, ' ', m.municipio, ' ', e.empresa) ILIKE '%' || #{buscar} || '%' " +
                "<choose>" +
                "  <when test='orden == \"asc\"'>ORDER BY p.producto ASC</when>" +
                "  <when test='orden == \"desc\"'>ORDER BY p.producto DESC</when>" +
                "  <when test='orden == \"reciente\"'>ORDER BY p.created_at DESC</when>" +
                //"  <when test='orden == \"popular\"'>ORDER BY p.numeroVentas DESC</when>" +
                "  <otherwise>ORDER BY p.producto ASC</otherwise>" +
                "</choose>" +
                "LIMIT #{cantidad} OFFSET #{pagina}" +
                "</script>")
    @Results({
        @Result(property = "empresa", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato")),
        @Result(property = "minPrecio", column = "min_precio"),
        @Result(property = "maxPrecio", column = "max_precio")
    })
    List<Productos> datoscat(String buscar, Integer pagina, Integer cantidad, String orden);

    // @Select("SELECT count(idproducto) FROM productos join empresas using(idempresa) join subrubros using(idsubrubro) join rubros using(idrubro) where cantidad>0 and concat(rubro,empresa,producto) ilike '%'||#{buscar}||'%' ")
    @Select("<script>" +
                "SELECT count(p.idproducto) " +
                "FROM productos p " +
                "JOIN empresas e ON e.idempresa=p.idempresa " +
                "JOIN rubros r ON r.idrubro=e.idrubro " +
                "WHERE p.estado=true AND CONCAT(p.producto, ' ', r.rubro, ' ', e.empresa) ILIKE '%' || #{buscar} || '%' " +
                "</script>")
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

    @Select("SELECT p.idproducto, p.producto, COUNT(p.idproducto) as cantidad, " + 
            "COALESCE(min_precio.min_precio, p.precioventa) AS min_precio, " +
            "COALESCE(max_precio.max_precio, p.precioventa) AS max_precio " +
            "FROM solicitudesproductos sp " + 
            "JOIN productos p using(idproducto) " + 
            "LEFT JOIN ( " +
                "    SELECT idproducto, MIN(precio) AS min_precio " +
                "    FROM precios " +
                "    GROUP BY idproducto " +
                ") min_precio ON min_precio.idproducto = p.idproducto " +
                "LEFT JOIN ( " +
                "    SELECT idproducto, MAX(precio) AS max_precio " +
                "    FROM precios " +
                "    GROUP BY idproducto " +
                ") max_precio ON max_precio.idproducto = p.idproducto " +
            "GROUP BY p.idproducto, p.producto, min_precio, max_precio " + 
            "ORDER BY count(p.idproducto) DESC LIMIT 8;")
    @Results({
        //@Result(property = "empresa", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato"))
        @Result(property = "minPrecio", column = "min_precio"),
        @Result(property = "maxPrecio", column = "max_precio")
    })
    List<Productos> productosmasvendidos();

}







