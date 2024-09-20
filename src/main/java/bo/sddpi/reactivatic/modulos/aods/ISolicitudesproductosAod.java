package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Solicitudesproductos;

@Mapper
public interface ISolicitudesproductosAod {

    @Select("SELECT * FROM solicitudesproductos WHERE idsolicitud=#{idsolicitud}")
    @Results({
        @Result(property = "producto", column = "idproducto", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IProductosAod.dato")),
        @Result(property = "precio", column = "idprecio", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPreciosAod.dato")),
        @Result(property = "color", column = "idcolor", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IColoresAod.dato")),
        @Result(property = "material", column = "idmaterial", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IMaterialesAod.dato")),
        @Result(property = "tamano", column = "idtamano", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITamanosAod.dato")),

    })
    List<Solicitudesproductos> datosl(Long idsolicitud);

}