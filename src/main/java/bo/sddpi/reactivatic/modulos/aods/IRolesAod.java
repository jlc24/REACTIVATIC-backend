package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.*;
// import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Roles;

import java.util.List;

@Mapper
public interface IRolesAod {

    @Select("select * from roles where idrol=#{id}")
    Roles dato(Long id);

    @Select("SELECT * FROM roles")
    List<Roles> listar();

    @Select("SELECT * FROM roles WHERE idrol = 1 OR idrol = 4 OR idrol = 5 OR idrol = 6")
    List<Roles> listaradmin();

    @Select("SELECT * FROM roles WHERE idrol = 4 OR idrol = 5 OR idrol = 6")
    List<Roles> listarsddpi();

    @Select("SELECT * FROM roles WHERE idrol = 4 OR idrol = 6")
    List<Roles> listarreactivatic();

    @Insert("INSERT INTO roles (rol) VALUES (#{rol})")
    @Options(useGeneratedKeys = true, keyProperty = "idrol")
    void insertar(Roles roles);

    @Update("UPDATE roles SET rol = #{rol} WHERE idrol = #{idrol}")
    void actualizar(Roles roles);

    @Delete("DELETE FROM roles WHERE idrol = #{id}")
    void eliminar(Long id);
}

