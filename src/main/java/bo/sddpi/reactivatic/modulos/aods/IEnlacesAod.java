package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.*;

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

    @Select("SELECT idenlace FROM enlaces WHERE enlace=#{enlace}")
    Long verificarenlace(String enlace);

    @Select("SELECT idenlace FROM enlaces WHERE ruta=#{ruta}")
    Long verificarenlaceruta(String ruta);
}
