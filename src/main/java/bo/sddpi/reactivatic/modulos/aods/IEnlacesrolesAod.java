package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Enlacesroles;

import java.util.List;

@Mapper
public interface IEnlacesrolesAod {
    
    @Select("SELECT * FROM enlacesroles WHERE idenlacerol = #{id}")
    Enlacesroles dato(Long id);

    @Select("SELECT * FROM enlacesroles")
    List<Enlacesroles> listar();

    @Insert("INSERT INTO enlacesroles (idenlace, idrol) VALUES (#{enlace.idenlace}, #{rol.idrol})")
    void insertar(Enlacesroles enlaceRol);

    @Update("UPDATE enlacesroles SET idenlace = #{enlace.idenlace}, idrol = #{rol.idrol} WHERE idenlacerol = #{idenlacerol}")
    void actualizar(Enlacesroles enlaceRol);

    @Delete("DELETE FROM enlacesroles WHERE idenlacerol = #{id}")
    void eliminar(Long id);
}
