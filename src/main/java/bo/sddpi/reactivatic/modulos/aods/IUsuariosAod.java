package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Procesar;
import bo.sddpi.reactivatic.modulos.entidades.Usuarios;

@Mapper
public interface IUsuariosAod {

    @Select("SELECT u.idusuario, p.idpersona, p.primerapellido, p.segundoapellido, p.primernombre " +
            "FROM usuarios u " +
            "JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
            "JOIN personas p ON u.idpersona = p.idpersona " +
            //"WHERE ur.idrol NOT IN (2, 3) " +
            "WHERE ur.idrol NOT IN (7, 8) " +
            "ORDER BY p.primerapellido DESC ")
    @Results({
        @Result(property = "persona", column = "idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
    })
    List<Usuarios> lista();

    @Select("SELECT u.idusuario, u.idpersona, u.idpersona as ifore1, u.usuario, u.clave, u.estado, ur.idrol as ifore5, r.rol " +
        "FROM usuarios u " +
        "JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "JOIN personas p ON u.idpersona = p.idpersona " +
        "JOIN roles r ON ur.idrol = r.idrol " +
        //"WHERE ur.idrol NOT IN (2, 3) " +
        "WHERE ur.idrol NOT IN (7, 8) " +
        "AND concat(u.usuario, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.primernombre) ILIKE '%'||#{buscar}||'%' " +
        "ORDER BY u.created_at DESC " +
        "LIMIT #{cantidad} OFFSET #{pagina}")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "rol", column = "ifore5", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato")),
    })
    List<Usuarios> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT u.idusuario, u.idpersona, u.idpersona as ifore1, u.usuario, u.clave, u.estado, ur.idrol as ifore5, r.rol " +
        "FROM usuarios u " +
        "JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "JOIN personas p ON u.idpersona = p.idpersona " +
        "JOIN roles r ON ur.idrol = r.idrol " +
        //"WHERE (ur.idrol = 4 OR ur.idrol = 5 OR ur.idrol = 6 OR ur.idrol = 7) " +
        "WHERE idrol NOT IN (1, 6, 7, 8) " +
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
        //"WHERE (ur.idrol = 4 OR ur.idrol = 6 OR ur.idrol = 7) " +
        "WHERE idrol NOT IN (1, 2, 6, 7, 8) " +
        "AND concat(u.usuario, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.primernombre) ILIKE '%'||#{buscar}||'%' " +
        "ORDER BY u.usuario " +
        "LIMIT #{cantidad} OFFSET #{pagina}")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "rol", column = "ifore5", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato"))
    })
    List<Usuarios> datosdpeic(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT u.idusuario, u.idpersona, u.idpersona as ifore1, u.usuario, u.clave, u.estado, ur.idrol as ifore5, r.rol " +
        "FROM usuarios u " +
        "JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "JOIN personas p ON u.idpersona = p.idpersona " +
        "JOIN roles r ON ur.idrol = r.idrol " +
        //"WHERE ur.idrol NOT IN (1, 2, 3, 5, 7) " +
        "WHERE ur.idrol NOT IN (1, 2, 3, 7, 8) " +
        "AND concat(u.usuario, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.primernombre) ILIKE '%'||#{buscar}||'%' " +
        "ORDER BY u.usuario " +
        "LIMIT #{cantidad} OFFSET #{pagina}")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "rol", column = "ifore5", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato"))
    })
    List<Usuarios> datosreactivatic(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idusuario) " + 
        "FROM usuarios " + 
        "JOIN usuariosroles using(idusuario) " + 
        "JOIN personas using(idpersona) " + 
        //"WHERE idrol NOT IN (2, 3) " +
        "WHERE idrol NOT IN (7, 8) " +
        "AND concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre,' ',segundonombre) ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT count(idusuario) " + 
        "FROM usuarios " +
        "JOIN usuariosroles using(idusuario) " + 
        "JOIN personas using(idpersona) " +
        //"WHERE (idrol = 4 OR idrol = 5 OR idrol = 6 OR idrol = 7) " +
        "WHERE idrol NOT IN (1, 6, 7, 8) " +
        "AND concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre,' ',segundonombre) ilike '%'||#{buscar}||'%' ")
    Integer cantidadsddpi(String buscar);

    @Select("SELECT count(idusuario) " + 
        "FROM usuarios " +
        "JOIN usuariosroles using(idusuario) " + 
        "JOIN personas using(idpersona) " +
        //"WHERE (idrol = 4 OR idrol = 6 OR idrol = 7) " +
        "WHERE idrol NOT IN (1, 2, 6, 7, 8) " +
        "AND concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre,' ',segundonombre) ilike '%'||#{buscar}||'%' ")
    Integer cantidaddpeic(String buscar);

    @Select("SELECT count(idusuario) " +
        "FROM usuarios " +
        "JOIN usuariosroles using(idusuario) " +
        "JOIN personas using(idpersona) " +
        //"WHERE idrol NOT IN (1, 2, 3, 5, 7) " +
        "WHERE idrol NOT IN (1, 2, 3, 7, 8) " +
        "AND concat(usuario,' ',primerapellido,' ',segundoapellido,' ',primernombre,' ',segundonombre) ilike '%'||#{buscar}||'%' ")
    Integer cantidadreactivatic(String buscar);

    @Select("SELECT u.idusuario, u.idpersona, u.idpersona as ifore1, ur.idrol as ifore2, u.usuario, u.clave, u.estado, u.idcargo " +
        "FROM usuarios u " +
        "LEFT JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "WHERE u.estado = TRUE AND u.idusuario = #{id} LIMIT 1")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "rol", column = "ifore2", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato")),
        @Result(property = "cargo", column = "idcargo", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ICargosAod.dato"))
    })
    Usuarios dato(Long id);

    @Select("SELECT * FROM usuarios WHERE idusuario=#{id}")
    Usuarios datousuario(Long id);

    @Options(useGeneratedKeys = true, keyProperty = "idusuario", keyColumn = "idusuario")
    @Insert("INSERT INTO usuarios(idpersona, usuario, clave, estado, idcargo) VALUES (#{idpersona}, #{usuario}, crypt(#{clave},gen_salt('bf', 8)), true, #{idcargo}) returning idusuario")
    void adicionar(Usuarios dato);

    @Update("UPDATE usuarios SET idcargo=#{idcargo} WHERE idusuario=#{idusuario} ")
    void modificar(Usuarios dato);

    @Delete("DELETE FROM usuarios WHERE idusuario=#{id}")
    void eliminar(Long id);

    @Update("UPDATE usuarios SET clave=crypt(#{clave},gen_salt('bf', 8)) WHERE idusuario = #{idusuario}")
    void cambiarclave(Usuarios usuario);

    @Update("UPDATE usuarios SET estado=#{estado} WHERE idusuario=#{idusuario}")
    void cambiarestado(Usuarios usuario);

    @Select("SELECT idusuario, clave FROM usuarios WHERE estado AND usuario=#{usuario}")
    Usuarios verificausuario(String usuario);

    @Select("SELECT usuario FROM usuarios WHERE idusuario=#{idusuario}")
    String verificaruser(Long idusuario);

    @Select("SELECT EXISTS(SELECT 1 FROM usuarios WHERE estado = true AND idusuario = #{idusuario} AND usuario = #{usuario} AND clave = crypt(#{clave}, clave))")
    Boolean verificaclave(Usuarios usuario);

    @Select("SELECT u.idusuario, u.idpersona, u.idpersona as ifore1, u.usuario, u.clave, u.estado, ur.idrol as ifore5, r.rol " +
        "FROM usuarios u " +
        "JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "JOIN personas p ON u.idpersona = p.idpersona " +
        "JOIN roles r ON ur.idrol = r.idrol " +
        //"WHERE ur.idrol = 2 " +
        "WHERE ur.idrol = 7 " +
        "AND concat(u.usuario, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.primernombre) ILIKE '%'||#{buscar}||'%' " +
        "ORDER BY u.created_at DESC " +
        "LIMIT #{cantidad} OFFSET #{pagina}")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "rol", column = "ifore5", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato")),
        @Result(property = "tipogenero", column = "idtipogenero", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposgenerosAod.dato")),
        @Result(property = "tipodocumento", column = "idtipodocumento", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposdocumentosAod.dato")),
        @Result(property = "tipoextension", column = "idtipoextension", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposextensionesAod.dato"))
    })
    List<Usuarios> datosrep(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(u.idusuario) " +
        "FROM usuarios u " +
        "JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "JOIN personas p ON u.idpersona = p.idpersona " +
        "JOIN roles r ON ur.idrol = r.idrol " +
        //"WHERE ur.idrol=2 " +
        "WHERE ur.idrol = 7 " +
        "AND concat(usuario,primerapellido, segundoapellido, primernombre) ilike '%'||#{buscar}||'%' ")
    Integer cantidadrep(String buscar);

    @Select("SELECT u.idusuario, u.idpersona, u.idpersona as ifore1, ur.idrol as ifore2, u.usuario, u.clave, u.estado, u.idcargo " +
        "FROM usuarios u " +
        "JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "WHERE u.estado = TRUE AND ur.idrol=7 AND u.idpersona = #{id}")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "rol", column = "ifore2", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato")),
        @Result(property = "cargo", column = "idcargo", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ICargosAod.dato"))
    })
    Usuarios datorep(Long id);

    //ANALIZANDO...
    @Select("SELECT idusuario from usuarios where usuario=#{usuario}")
    Long verificausuarioregistro(String usuario);

    @Select("SELECT idusuario from usuarios where usuario=#{usuario}")
    Long verificaclienteregistro(String usuario);

    @Select("SELECT u.idusuario, u.idpersona, u.idpersona as ifore1, u.usuario, u.clave, u.estado, ur.idrol as ifore5, r.rol " +
        "FROM usuarios u " +
        "JOIN usuariosroles ur ON u.idusuario = ur.idusuario " +
        "JOIN personas p ON u.idpersona = p.idpersona " +
        "JOIN roles r ON ur.idrol = r.idrol " +
        //"WHERE (ur.idrol = 1 OR ur.idrol = 4 OR ur.idrol = 5 OR ur.idrol = 6) " +
        "WHERE (ur.idrol = 1 OR ur.idrol = 2 OR ur.idrol = 3 OR ur.idrol = 4 OR ur.idrol = 5) " +
        "AND concat(u.usuario, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.primernombre) ILIKE '%'||#{buscar}||'%' " +
        "ORDER BY u.idusuario ASC ")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "rol", column = "ifore5", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato")),
        @Result(property = "tipogenero", column = "idtipogenero", one = @One(select = "bo.sddpi.rectivatic.modulos.aods.ITiposgenerosAod.dato"))
    })
    List<Usuarios> datosrepo(String buscar);

    @Select("SELECT p.primerapellido, p.segundoapellido, p.primernombre, u.usuario, p.celular, p.direccion, p.correo, u.clave " +
        "FROM usuarios u " +
        "JOIN personas p ON p.idpersona=u.idpersona " +
        "JOIN usuariosroles ur ON ur.idusuario=u.idusuario " +
        //"WHERE idrol = 3 " +
        "WHERE idrol = 8 " +
        "AND u.usuario = #{usuario}")
    Procesar usuariocatalogo(Procesar dato);

    @Select("SELECT idusuario, usuarios.idpersona, usuarios.idpersona as ifore1, usuario, clave, usuarios.estado " +
        "FROM usuarios " +
        "JOIN usuariosroles using(idusuario) " +
        "JOIN personas using(idpersona) " +
        //"WHERE idrol=2 " +
        "WHERE idrol = 8 " +
        "AND usuarios.estado AND concat(usuario, primerapellido, segundoapellido, primernombre) ilike '%'||#{buscar}||'%' ")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    })
    List<Usuarios> datosreporep(String buscar);
}





