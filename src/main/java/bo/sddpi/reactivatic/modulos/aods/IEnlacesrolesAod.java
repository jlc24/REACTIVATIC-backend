package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Enlacesroles;

import java.util.List;

@Mapper
public interface IEnlacesrolesAod {
    
    @Select("SELECT * FROM enlacesroles WHERE idenlacerol = #{id}")
    Enlacesroles dato(long id);

    @Select("SELECT * FROM enlacesroles")
    List<Enlacesroles> listar();

    @Select("SELECT er.*, r.* FROM enlacesroles er " +
        "JOIN roles r ON er.idrol = r.idrol " +
        "WHERE er.idenlace = #{idenlace}")
        @Results({
            @Result(property = "rol", column = "idrol", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato"))
        })
    List<Enlacesroles> listarRoles(long idenlace);

    @Select("SELECT idenlacerol FROM enlacesroles WHERE idrol = #{idrol} AND idenlace=#{idenlace}")
    Long verificar(long idrol, long idenlace);

    @Insert("INSERT INTO enlacesroles (idenlace, idrol) VALUES (#{idenlace}, #{idrol})")
    @Options(useGeneratedKeys = true, keyProperty = "idenlacerol")
    void insertar(Enlacesroles enlaceRol);

    @Update("UPDATE enlacesroles SET idenlace = #{enlace.idenlace}, idrol = #{rol.idrol} WHERE idenlacerol = #{idenlacerol}")
    void actualizar(Enlacesroles enlaceRol);

    @Delete("DELETE FROM enlacesroles WHERE idenlacerol = #{idenlacerol}")
    void eliminar(Enlacesroles enlacesroles);
}
