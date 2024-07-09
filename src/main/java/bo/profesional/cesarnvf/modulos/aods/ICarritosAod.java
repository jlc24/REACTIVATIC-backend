package bo.profesional.cesarnvf.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import bo.profesional.cesarnvf.modulos.entidades.Carritos;

@Mapper
public interface ICarritosAod {

    @Select("select carritos.idproducto, carritos.idproducto as ifore1, sum(carritos.cantidad) as cantidad from carritos join productos using(idproducto) where idcliente=#{idcliente} group by carritos.idproducto")
    @Results({
        @Result(property = "producto", column = "ifore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IProductosAod.dato"))
    })
    List<Carritos> datosl(Long idcliente);

    @Insert("insert into carritos(idcliente, idproducto, cantidad) values (#{idcliente}, #{idproducto}, #{cantidad})")
    void adicionar(Carritos dato);

    @Select("select count(*) as cantidad from carritos where idcliente=#{idcliente}")
    Carritos cantidadcarrito(Long idcliente);

    @Delete("delete from carritos where idcliente=#{idcliente} and idproducto=#{idproducto}")
    void eliminar(Long idcliente, Long idproducto);

    @Select("SELECT idcarrito, idcliente, idproducto, idproducto as idfore1, carritos.cantidad, carritos.fecha FROM carritos join productos using(idproducto) where concat(idcliente::varchar, producto, fecha) ilike '%'||#{buscar}||'%' order by fecha desc, idcliente desc LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "producto", column = "idfore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IProductosAod.dato")),
    })
    List<Carritos> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idcarrito) FROM carritos join productos using(idproducto) where concat(idcliente::varchar, producto, fecha) ilike '%'||#{buscar}||'%'")
    Integer cantidad(String buscar);

    @Select("SELECT idcarrito, idcliente, idproducto, idproducto as idfore1, carritos.cantidad, carritos.fecha FROM carritos join productos using(idproducto) where concat(idcliente::varchar, producto, fecha) ilike '%'||#{buscar}||'%' order by fecha desc, idcliente desc")
    @Results({
        @Result(property = "producto", column = "idfore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IProductosAod.dato")),
    })
    List<Carritos> datosrepo(String buscar);

}

