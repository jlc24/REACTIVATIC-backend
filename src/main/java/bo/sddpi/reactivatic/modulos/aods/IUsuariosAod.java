package bo.sddpi.reactivatic.modulos.aods;

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

import bo.sddpi.reactivatic.modulos.entidades.Procesar;
import bo.sddpi.reactivatic.modulos.entidades.Usuarios;

@Mapper
public interface IUsuariosAod {

    // @Select("SELECT idusuario, usuarios.idpersona, usuarios.idpersona as ifore1, usuario, clave, usuarios.estado FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) WHERE idrol=4 and usuarios.estado and concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre) ilike '%'||#{buscar}||'%' ORDER BY usuario LIMIT #{cantidad} OFFSET #{pagina} ")
    // @Results({
    //     @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    // })
    @Select("SELECT u.idusuario, u.idpersona, u.idpersona as ifore1, u.usuario, u.clave, u.estado, ur.idrol as ifore5, r.rol " +
        "FROM usuarios u " +
        "JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "JOIN personas p ON u.idpersona = p.idpersona " +
        "JOIN roles r ON ur.idrol = r.idrol " +
        "WHERE ur.idrol = 1 OR ur.idrol = 4 OR ur.idrol = 5 OR ur.idrol = 6 " +
        "AND concat(u.usuario, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.primernombre) ILIKE '%'||#{buscar}||'%' " +
        "ORDER BY u.usuario " +
        "LIMIT #{cantidad} OFFSET #{pagina}")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "rol", column = "ifore5", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato"))
    })
    List<Usuarios> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT u.idusuario, u.idpersona, u.idpersona as ifore1, u.usuario, u.clave, u.estado, ur.idrol as ifore5, r.rol " +
        "FROM usuarios u " +
        "JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "JOIN personas p ON u.idpersona = p.idpersona " +
        "JOIN roles r ON ur.idrol = r.idrol " +
        "WHERE ur.idrol = 4 OR ur.idrol = 5 OR ur.idrol = 6 " +
        "AND concat(u.usuario, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.primernombre) ILIKE '%'||#{buscar}||'%' " +
        "ORDER BY u.usuario " +
        "LIMIT #{cantidad} OFFSET #{pagina}")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "rol", column = "ifore5", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato"))
    })
    List<Usuarios> datossddpi(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT u.idusuario, u.idpersona, u.idpersona as ifore1, u.usuario, u.clave, u.estado, ur.idrol as ifore5, r.rol " +
        "FROM usuarios u " +
        "JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "JOIN personas p ON u.idpersona = p.idpersona " +
        "JOIN roles r ON ur.idrol = r.idrol " +
        "WHERE ur.idrol = 6 " +
        "AND concat(u.usuario, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.primernombre) ILIKE '%'||#{buscar}||'%' " +
        "ORDER BY u.usuario " +
        "LIMIT #{cantidad} OFFSET #{pagina}")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "rol", column = "ifore5", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato"))
    })
    List<Usuarios> datosreactivatic(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idusuario) FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) where idrol = 1 OR idrol = 4 OR idrol = 5 OR idrol = 6 and concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre,' ',segundonombre) ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT count(idusuario) FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) where idrol = 4 OR idrol = 5 OR idrol = 6 and concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre,' ',segundonombre) ilike '%'||#{buscar}||'%' ")
    Integer cantidadsddpi(String buscar);

    @Select("SELECT count(idusuario) FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) where idrol = 6 and concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre,' ',segundonombre) ilike '%'||#{buscar}||'%' ")
    Integer cantidadreactivatic(String buscar);

    // @Select("SELECT idusuario, idpersona, idpersona as ifore1, idrol as ifore2, usuario, clave, estado FROM usuarios WHERE estado and idusuario=#{id} ")
    // @Results({
    //     @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    // })
    // Usuarios dato(Long id);

    @Select("SELECT u.idusuario, u.idpersona, u.idpersona as ifore1, ur.idrol as ifore2, u.usuario, u.clave, u.estado " +
        "FROM usuarios u " +
        "LEFT JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "WHERE u.estado = TRUE AND u.idusuario = #{id}")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "rol", column = "ifore2", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato"))
    })
    Usuarios dato(Long id);

    @Select("SELECT * FROM usuarios WHERE idusuario=#{id}")
    Usuarios datousuario(Long id);

    @Options(useGeneratedKeys = true, keyProperty = "idusuario", keyColumn = "idusuario")
    @Insert("INSERT INTO usuarios(idpersona, usuario, clave, estado) VALUES (#{idpersona}, #{usuario}, crypt(#{clave},gen_salt('bf', 8)), #{estado}) returning idusuario")
    void adicionar(Usuarios dato);

    @Update("UPDATE usuarios SET idpersona=#{idpersona}, usuario=#{usuario}, clave=#{clave} WHERE idusuario=#{idusuario} ")
    void modificar(Usuarios dato);

    @Delete("DELETE FROM usuarios WHERE idusuario=#{id}")
    void eliminar(Long id);

    @Update("UPDATE usuarios SET usuario=#{usuario}, clave=crypt(#{clave},gen_salt('bf', 8)) WHERE idusuario = #{idusuario}")
    void cambiarclave(Usuarios usuario);

    @Update("UPDATE usuarios SET estado=#{estado} WHERE idusuario=#{idusuario}")
    void cambiarestado(Long idusuario, boolean estado);

    @Select("SELECT idusuario, clave FROM usuarios WHERE estado AND usuario=#{usuario}")
    Usuarios verificausuario(String usuario);

    //ANALIZANDO...
    @Select("SELECT idusuario, usuarios.idpersona, usuarios.idpersona as ifore1, usuario, clave, usuarios.estado FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) WHERE idrol=2 and usuarios.estado and concat(usuario, primerapellido, segundoapellido, primernombre) ilike '%'||#{buscar}||'%' ORDER BY usuario LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    })
    List<Usuarios> datosrep(String buscar, Integer pagina, Integer cantidad);

    //ANALIZANDO...
    @Select("SELECT count(idusuario) FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona)  where idrol=2 and usuarios.estado and concat(usuario,primerapellido, segundoapellido, primernombre) ilike '%'||#{buscar}||'%' ")
    Integer cantidadrep(String buscar);

    //ANALIZANDO...
    @Select("SELECT idusuario from usuarios where usuario=#{usuario}")
    Long verificausuarioregistro(String usuario);

    @Select("SELECT idusuario, usuarios.idpersona, usuarios.idpersona as ifore1, usuario, clave, usuarios.estado FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) WHERE idrol=4 and usuarios.estado and concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre,' ',segundonombre) ilike '%'||#{buscar}||'%' ORDER BY usuario")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    })
    List<Usuarios> datosrepo(String buscar);

    @Select("select primerapellido as nombre, celular, correo, clave from usuarios join personas using(idpersona) join usuariosroles using(idusuario) where idrol = 3 and celular::integer = #{celular} limit 1")
    Procesar usuariocatalogo(Procesar dato);

    @Select("SELECT idusuario, usuarios.idpersona, usuarios.idpersona as ifore1, usuario, clave, usuarios.estado FROM usuarios join usuariosroles using(idusuario) join personas using(idpersona) WHERE idrol=2 and usuarios.estado and concat(usuario,primerapellido, segundoapellido, primernombre, segundonombre) ilike '%'||#{buscar}||'%' ")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    })
    List<Usuarios> datosreporep(String buscar);
}





