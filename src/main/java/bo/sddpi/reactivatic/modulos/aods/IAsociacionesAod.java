package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Asociaciones;

@Mapper
public interface IAsociacionesAod {

    @Select("SELECT * FROM asociaciones WHERE idasociacion=#{id} ")
    Asociaciones dato(Long id);

    @Select("SELECT * FROM asociaciones WHERE asociacion ilike '%'||#{buscar}||'%' ORDER BY asociacion LIMIT #{cantidad} OFFSET #{pagina} ")
    List<Asociaciones> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idasociacion) FROM asociaciones WHERE asociacion ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("select idasociacion, asociacion from asociaciones order by asociacion")
    List<Asociaciones> datosl();

    @Insert("INSERT INTO asociaciones(asociacion, descripcion, fechacreacion, representantelegal, direccion, telefono, celular, correo) VALUES (#{asociacion}, #{descripcion}, #{fechacreacion}, #{representantelegal}, #{direccion}, #{telefono}, #{celular}, #{correo})")
    @Options(useGeneratedKeys = true, keyProperty = "idasociacion")
    void adicionar(Asociaciones dato);

    @Update("UPDATE asociaciones SET asociacion=#{asociacion}, descripcion=#{descripcion}, fechacreacion=#{fechacreacion}, representantelegal=#{representantelegal}, direccion=#{direccion}, telefono=#{telefono}, celular=#{celular}, correo=#{correo} WHERE idasociacion=#{idasociacion} ")
    void modificar(Asociaciones dato);

    @Update("UPDATE categorias SET estado=#{estado} WHERE idasociacion=#{idasociacion}")
    void cambiarestado(Asociaciones asociaciones);

    @Delete("DELETE FROM asociaciones WHERE idasociacion=#{idasociacion}")
    void eliminar(Asociaciones asociaciones);
}






