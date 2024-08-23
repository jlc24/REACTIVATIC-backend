package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Usuariosroles;

@Mapper
public interface IUsuariosrolesAod {

    @Insert("insert into usuariosroles(idusuario, idrol) values (#{idusuario}, #{idrol})")
    void adicionarusuariorol(Usuariosroles usuariorol);

    @Select("select idrol from usuariosroles where idusuario=#{idusuario}")
    @Results(value = {
        @Result(property = "rol", column = "idrol", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato"))
    })
    List<Usuariosroles> usuarioroles(Long idusuario);

    @Delete("delete from usuariosroles where idusuario=#{id}")
    void eliminar(Long id);

    

}