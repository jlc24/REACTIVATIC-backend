package bo.profesional.cesarnvf.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.profesional.cesarnvf.modulos.entidades.Procesar;
import bo.profesional.cesarnvf.modulos.entidades.Usuarios;

@Mapper
public interface IUsuariosAod {

    @Select("SELECT idusuario, usuarios.idpersona, usuarios.idpersona as ifore1, usuario, clave, usuarios.estado FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) WHERE idrol=4 and usuarios.estado and concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre,' ',segundonombre) ilike '%'||#{buscar}||'%' ORDER BY usuario LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IPersonasAod.dato"))
    })
    List<Usuarios> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idusuario) FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) where idrol=4 and usuarios.estado and concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre,' ',segundonombre) ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT idusuario, idpersona, idpersona as ifore1, usuario, clave, estado FROM usuarios where estado and idusuario=#{id} ")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IPersonasAod.dato"))
    })
    Usuarios dato(Long id);

    @Options(useGeneratedKeys = true, keyProperty = "idusuario", keyColumn = "idusuario")
    @Insert("insert into usuarios(idpersona, usuario, clave, estado) values (#{idpersona}, #{usuario}, crypt(#{clave},gen_salt('bf', 8)), #{estado}) returning idusuario")
    void adicionar(Usuarios dato);

    @Update("update usuarios set idpersona=#{idpersona}, usuario=#{usuario}, clave=#{clave}, estado=#{estado} where idusuario=#{idusuario} ")
    void modificar(Usuarios dato);

    @Delete("delete from usuarios where idusuario=#{id}")
    void eliminar(Long id);

    @Update("update usuarios set clave=crypt(#{clave},gen_salt('bf', 8)) where idusuario = #{idusuario}")
    void cambiarclave(Usuarios usuario);

    @Select("select idusuario, clave from usuarios where estado and usuario=#{usuario}")
    Usuarios verificausuario(String usuario);

    @Select("SELECT idusuario, usuarios.idpersona, usuarios.idpersona as ifore1, usuario, clave, usuarios.estado FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) WHERE idrol=2 and usuarios.estado and concat(usuario,primerapellido, segundoapellido, primernombre, segundonombre) ilike '%'||#{buscar}||'%' ORDER BY usuario LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IPersonasAod.dato"))
    })
    List<Usuarios> datosrep(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idusuario) FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona)  where idrol=2 and usuarios.estado and concat(usuario,primerapellido, segundoapellido, primernombre, segundonombre) ilike '%'||#{buscar}||'%' ")
    Integer cantidadrep(String buscar);

    @Select("SELECT idusuario from usuarios where usuario=#{usuario}")
    Long verificausuarioregistro(String usuario);

    @Select("SELECT idusuario, usuarios.idpersona, usuarios.idpersona as ifore1, usuario, clave, usuarios.estado FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) WHERE idrol=4 and usuarios.estado and concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre,' ',segundonombre) ilike '%'||#{buscar}||'%' ORDER BY usuario")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IPersonasAod.dato"))
    })
    List<Usuarios> datosrepo(String buscar);

    @Select("select primerapellido as nombre, celular, correo, clave from usuarios join personas using(idpersona) join usuariosroles using(idusuario) where idrol = 3 and celular::integer = #{celular} limit 1")
    Procesar usuariocatalogo(Procesar dato);

    @Select("SELECT idusuario, usuarios.idpersona, usuarios.idpersona as ifore1, usuario, clave, usuarios.estado FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) WHERE idrol=2 and usuarios.estado and concat(usuario,primerapellido, segundoapellido, primernombre, segundonombre) ilike '%'||#{buscar}||'%' ")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IPersonasAod.dato"))
    })
    List<Usuarios> datosreporep(String buscar);
}





