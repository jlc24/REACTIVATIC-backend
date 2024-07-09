package bo.profesional.cesarnvf.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import bo.profesional.cesarnvf.modulos.entidades.Solicitudesproductos;

@Mapper
public interface ISolicitudesproductosAod {

    @Select("SELECT idsolicitudproducto, idsolicitud, idproducto, idproducto as ifore1, cantidad, precioventa, total FROM solicitudesproductos WHERE idsolicitud=#{idsolicitud}")
    @Results({
        @Result(property = "producto", column = "ifore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IProductosAod.dato"))
    })
    List<Solicitudesproductos> datosl(Long idsolicitud);

}