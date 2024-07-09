package bo.profesional.cesarnvf.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.profesional.cesarnvf.modulos.entidades.Asociaciones;

@Mapper
public interface IAsociacionesAod {

    @Select("SELECT idasociacion, asociacion, descripcion, fechacreacion, representantelegal, direccion, telefono, celular, correo FROM asociaciones WHERE asociacion ilike '%'||#{buscar}||'%' ORDER BY asociacion LIMIT #{cantidad} OFFSET #{pagina} ")
    List<Asociaciones> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idasociacion) FROM asociaciones where asociacion ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT idasociacion, asociacion, descripcion, fechacreacion, representantelegal, direccion, telefono, celular, correo FROM asociaciones where idasociacion=#{id} ")
    Asociaciones dato(Long id);

    @Insert("insert into asociaciones(asociacion, descripcion, fechacreacion, representantelegal, direccion, telefono, celular, correo) values (#{asociacion}, #{descripcion}, #{fechacreacion}, #{representantelegal}, #{direccion}, #{telefono}, #{celular}, #{correo})")
    void adicionar(Asociaciones dato);

    @Update("update asociaciones set asociacion=#{asociacion}, descripcion=#{descripcion}, fechacreacion=#{fechacreacion}, representantelegal=#{representantelegal}, direccion=#{direccion}, telefono=#{telefono}, celular=#{celular}, correo=#{correo} where idasociacion=#{idasociacion} ")
    void modificar(Asociaciones dato);

    @Select("select idasociacion, asociacion from asociaciones order by asociacion")
    List<Asociaciones> datosl();

}






