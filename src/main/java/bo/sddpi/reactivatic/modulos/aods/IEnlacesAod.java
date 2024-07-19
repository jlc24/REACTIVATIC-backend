package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Enlaces;
import java.util.List;

@Mapper
public interface IEnlacesAod {
    
    @Select("SELECT e.idenlace, e.idcategoria, e.enlace, e.ruta, e.iconoenlace, e.orden, c.idcategoria, c.categoria, c.ruta, c.iconocategoria, c.orden " +
            "FROM enlaces e " +
            "LEFT JOIN categorias c ON e.idcategoria = c.idcategoria " +
            "WHERE e.idenlace = #{id}")
    Enlaces dato(Long id);

    @Select("SELECT e.idenlace, e.idcategoria, e.enlace, e.ruta, e.iconoenlace, e.orden, c.idcategoria, c.categoria, c.ruta, c.iconocategoria, c.orden " +
            "FROM enlaces e " +
            "LEFT JOIN categorias c ON e.idcategoria = c.idcategoria")
    List<Enlaces> listar();

    @Insert("INSERT INTO enlaces (idcategoria, enlace, ruta, iconoenlace, orden) VALUES (#{idcategoria}, #{enlace}, #{ruta}, #{iconoenlace}, #{orden})")
    @Options(useGeneratedKeys = true, keyProperty = "idenlace")
    void insertar(Enlaces enlaces);

    @Update("UPDATE enlaces SET idcategoria = #{idcategoria}, enlace = #{enlace}, ruta = #{ruta}, iconoenlace = #{iconoenlace}, orden = #{orden} WHERE idenlace = #{idenlace}")
    void actualizar(Enlaces enlaces);

    @Delete("DELETE FROM enlaces WHERE idenlace = #{id}")
    void eliminar(Long id);
}
