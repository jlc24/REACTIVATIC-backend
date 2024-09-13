package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Carritos;

@Mapper
public interface ICarritosAod {

    @Select("SELECT c.idcarrito, c.idproducto, sum(c.cantidad) as cantidad, c.imagen, c.idprecio, c.idcolor, c.idmaterial, c.idtamano " +
            "FROM carritos c " +
            "JOIN productos p ON p.idproducto=c.idproducto " + 
            "LEFT JOIN precios pr ON pr.idprecio=c.idprecio " +
            "LEFT JOIN colores cl ON cl.idcolor=c.idcolor " +
            "LEFT JOIN materiales m ON m.idmaterial=c.idmaterial " +
            "LEFT JOIN tamanos t ON t.idtamano=c.idtamano " +
            "WHERE idcliente=#{idcliente} " +
            "GROUP BY c.idcarrito, c.idproducto, c.imagen, c.idprecio, c.idcolor, c.idmaterial, c.idtamano ")
    @Results({
        @Result(property = "producto", column = "idproducto", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IProductosAod.dato")),
        @Result(property = "precio", column = "idprecio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPreciosAod.dato")),
        @Result(property = "color", column = "idcolor", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IColoresAod.dato")),
        @Result(property = "material", column = "idmaterial", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IMaterialesAod.dato")),
        @Result(property = "tamano", column = "idtamano", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITamanosAod.dato")),
    })
    List<Carritos> datosl(Long idcliente);

    @Select("SELECT " + 
            "    (SELECT idprecio FROM precios WHERE idproducto = p.idproducto LIMIT 1) AS idprecio, " + 
            "    (SELECT idcolor FROM colores WHERE idproducto = p.idproducto LIMIT 1) AS idcolor, " + 
            "    (SELECT idmaterial FROM materiales WHERE idproducto = p.idproducto LIMIT 1) AS idmaterial, " + 
            "    (SELECT idtamano FROM tamanos WHERE idproducto = p.idproducto LIMIT 1) AS idtamano " + 
            "FROM productos p " + 
            "WHERE p.idproducto = #{idproducto}")
    Carritos atributosProducto(Long idproducto);

    @Select("SELECT SUM(cantidad) AS cantidad FROM carritos WHERE idcliente=#{idcliente}")
    Integer cantidadcarrito(Long idcliente);

    @Insert("INSERT INTO carritos(idcliente, idproducto, imagen, idprecio, idcolor, idmaterial, idtamano, cantidad) " +
            "VALUES (#{idcliente}, #{idproducto}, #{imagen}, #{idprecio}, #{idcolor}, #{idmaterial}, #{idtamano}, #{cantidad})")
    void adicionar(Carritos dato);

    @Select("SELECT idcarrito, cantidad " +
            "FROM carritos " +
            "WHERE idcliente=#{idcliente} AND imagen=#{imagen} AND idprecio=#{idprecio} AND idcolor=#{idcolor} AND idmaterial=#{idmaterial} AND idtamano=#{idtamano}")
    Carritos dato(Carritos carrito);

    @Update("UPDATE carritos SET cantidad=#{cantidad} WHERE idcarrito=#{idcarrito}")
    void actualizar(Carritos dato);

    @Delete("delete from carritos where idcarrito=#{idcarrito}")
    void eliminar(Long idcarrito);

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

