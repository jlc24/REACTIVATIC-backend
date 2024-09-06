package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Roles;

import java.util.List;

@Mapper
public interface IRolesAod {

    @Select("select * from roles where idrol=#{id}")
    Roles dato(Long id);

    @Select("SELECT * FROM roles")
    List<Roles> listar();

    @Select("SELECT * FROM roles WHERE nombrerol ilike '%'||#{buscar}||'%' OR rol ilike '%'||#{buscar}||'%' LIMIT #{cantidad} OFFSET #{pagina}")
    List<Roles> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idrol) FROM roles WHERE nombrerol ilike '%'||#{buscar}||'%' OR rol ilike '%'||#{buscar}||'%'")
    Integer cantidad(String buscar);

    @Select("SELECT * FROM roles WHERE idrol = 1 OR idrol = 4 OR idrol = 5 OR idrol = 6 OR idrol = 7")
    //@Select("SELECT * FROM roles WHERE idrol = 1 OR idrol = 2 OR idrol = 3 OR idrol = 4 or idrol = 5")
    List<Roles> listaradmin();

    @Select("SELECT * FROM roles WHERE idrol = 4 OR idrol = 5 OR idrol = 6 OR idrol = 7")
    //@Select("SELECT * FROM roles WHERE idrol = 2 OR idrol = 3 OR idrol = 4 or idrol = 5")
    List<Roles> listarsddpi();

    @Select("SELECT * FROM roles WHERE idrol = 4 OR idrol = 5 OR idrol = 6 OR idrol = 7")
    //@Select("SELECT * FROM roles WHERE idrol = 3 OR idrol = 4 or idrol = 5")
    List<Roles> listardpeic();

    @Select("SELECT * FROM roles WHERE idrol = 4 OR idrol = 6")
    //@Select("SELECT * FROM roles WHERE idrol = 4 or idrol = 5")
    List<Roles> listarreactivatic();

    @Insert("INSERT INTO roles (rol, nombrerol) VALUES (#{rol}, #{nombrerol})")
    @Options(useGeneratedKeys = true, keyProperty = "idrol")
    void insertar(Roles roles);

    @Update("UPDATE roles SET rol=#{rol}, nombrerol=#{nombrerol} WHERE idrol = #{idrol}")
    void actualizar(Roles roles);

    @Delete("DELETE FROM roles WHERE idrol = #{id}")
    void eliminar(Long id);
}

