package bo.profesional.cesarnvf.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.profesional.cesarnvf.modulos.entidades.Productos;

@Mapper
public interface IProductosAod {

    @Select("SELECT idproducto, idempresa, producto, descripcion, preciocompra, precioventa, cantidad FROM productos WHERE idempresa=#{idempresa} and concat(producto, descripcion) ilike '%'||#{buscar}||'%' ORDER BY idproducto LIMIT #{cantidad} OFFSET #{pagina} ")
    List<Productos> datos(Long idempresa, String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idproducto) FROM productos where idempresa=#{idempresa} and concat(producto, descripcion) ilike '%'||#{buscar}||'%' ")
    Integer cantidad(Long idempresa, String buscar);

    @Select("SELECT idproducto, idempresa, idempresa as ifore1, producto, descripcion, preciocompra, precioventa, cantidad FROM productos where idproducto=#{id} ")
    @Results({
        @Result(property = "empresa", column = "ifore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IEmpresasAod.dato"))
    })
    Productos dato(Long id);

    @Options(useGeneratedKeys = true, keyProperty = "idproducto", keyColumn = "idproducto")
    @Insert("insert into productos(idempresa, producto, descripcion, preciocompra, precioventa, cantidad) values (#{idempresa}, #{producto}, #{descripcion}, #{preciocompra}, #{precioventa}, #{cantidad}) returning idproducto")
    void adicionar(Productos dato);

    @Update("update productos set idempresa=#{idempresa}, producto=#{producto}, descripcion=#{descripcion}, preciocompra=#{preciocompra}, precioventa=#{precioventa}, cantidad=#{cantidad} where idproducto=#{idproducto} ")
    void modificar(Productos dato);

    @Select("SELECT idproducto, idempresa, idempresa as ifore1, producto, productos.descripcion, preciocompra, precioventa, cantidad FROM productos join empresas using(idempresa) join subrubros using(idsubrubro) join rubros using(idrubro) WHERE cantidad>0 and concat(rubro,empresa,producto) ilike '%'||#{buscar}||'%' ORDER BY producto LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "empresa", column = "ifore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IEmpresasAod.dato"))
    })
    List<Productos> datoscat(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idproducto) FROM productos join empresas using(idempresa) join subrubros using(idsubrubro) join rubros using(idrubro) where cantidad>0 and concat(rubro,empresa,producto) ilike '%'||#{buscar}||'%' ")
    Integer cantidadcat(String buscar);

    @Select("SELECT idproducto, idempresa, idempresa as ifore1, producto, productos.descripcion, preciocompra, precioventa, cantidad FROM productos join empresas using(idempresa) join subrubros using(idsubrubro) join rubros using(idrubro) WHERE cantidad>0 and idproducto=#{id} ")
    @Results({
        @Result(property = "empresa", column = "ifore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IEmpresasAod.dato"))
    })
    Productos datocat(Long id);

}







