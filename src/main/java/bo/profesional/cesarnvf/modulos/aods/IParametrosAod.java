package bo.profesional.cesarnvf.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.profesional.cesarnvf.modulos.entidades.Parametros;

@Mapper
public interface IParametrosAod {

    @Select("SELECT idparametro, idusuario, parametro, valor FROM parametros WHERE idusuario=#{idusuario} and parametro ilike '%'||#{buscar}||'%' ORDER BY parametro LIMIT #{cantidad} OFFSET #{pagina} ")
    List<Parametros> datos(Long idusuario, String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idparametro) FROM parametros where idusuario=#{idusuario} and parametro ilike '%'||#{buscar}||'%' ")
    Integer cantidad(Long idusuario, String buscar);

    @Select("SELECT idparametro, idusuario, parametro, valor FROM parametros where idparametro=#{id} ")
    Parametros dato(Long id);

    @Update("update parametros set idusuario=#{idusuario}, parametro=#{parametro}, valor=#{valor} where idparametro=#{idparametro} ")
    void modificar(Parametros dato);

    @Select("select valor from parametros where idusuario=#{idusuario} and parametro=#{parametro}")
    String parametro(Long idusuario, String parametro);

}





