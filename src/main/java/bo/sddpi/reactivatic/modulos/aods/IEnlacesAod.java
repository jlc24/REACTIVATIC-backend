package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Enlaces;
import java.util.List;

@Mapper
public interface IEnlacesAod {
    
    @Select("SELECT * FROM enlaces WHERE idenlace = #{id}")
    Enlaces dato(Long id);

    @Select("SELECT * FROM enlaces WHERE enlace ilike '%'||#{buscar}||'%' OR CAST(orden AS TEXT) ilike '%'||#{buscar}||'%' ORDER BY idcategoria ASC, orden ASC LIMIT #{cantidad} OFFSET #{pagina}")
    List<Enlaces> datos(String buscar, Integer pagina, Integer cantidad);
    
    @Select("SELECT count(idenlace) FROM enlaces WHERE enlace ilike '%'||#{buscar}||'%' OR CAST(orden AS TEXT) ilike '%'||#{buscar}||'%'")
    Integer cantidad(String buscar);

    @Select("SELECT * FROM enlaces WHERE idcategoria=#{idcategoria} ORDER BY orden ASC")
    List<Enlaces> listar(Long idcategoria);

    @Select("SELECT e.idenlace, e.idcategoria, e.enlace, e.ruta, e.orden, " +
        "CASE WHEN er.idrol IS NOT NULL THEN true ELSE false END AS estado " +
        "FROM enlaces e " +
        "LEFT JOIN enlacesroles er ON e.idenlace = er.idenlace AND er.idrol = #{idrol} ORDER BY idcategoria ASC, orden ASC")
        @Results({
            @Result(property = "categoria", column = "idcategoria", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ICategoriasAod.dato"))
        })
    List<Enlaces> listarEnlaces(long idrol);

    @Insert("INSERT INTO enlaces (idcategoria, enlace, ruta, iconoenlace, orden) VALUES (#{idcategoria}, #{enlace}, #{ruta}, #{iconoenlace}, #{orden})")
    @Options(useGeneratedKeys = true, keyProperty = "idenlace")
    void insertar(Enlaces enlace);

    @Update("UPDATE enlaces SET idcategoria = #{idcategoria}, enlace = #{enlace}, ruta = #{ruta}, iconoenlace = #{iconoenlace}, orden = #{orden} WHERE idenlace = #{idenlace}")
    void actualizar(Enlaces enlace);

    @Delete("DELETE FROM enlaces WHERE idenlace = #{id}")
    void eliminar(Long id);
}
