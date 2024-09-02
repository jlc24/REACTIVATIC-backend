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

import bo.sddpi.reactivatic.modulos.entidades.Personas;
import bo.sddpi.reactivatic.modulos.entidades.Usuarios;

@Mapper
public interface IPersonasAod {

    @Select("SELECT * FROM personas where idpersona=#{id} ")
    @Results({
        @Result(property = "tipogenero", column = "idtipogenero", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposgenerosAod.dato")),
        @Result(property = "tipodocumento", column = "idtipodocumento", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposdocumentosAod.dato")),
        @Result(property = "tipoextension", column = "idtipoextension", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposextensionesAod.dato"))
    })
    Personas dato(Long id);
    
    @Select("SELECT p.idpersona, p.idtipogenero, p.idtipogenero as ifore1, p.primerapellido, p.segundoapellido, p.primernombre, p.dip, p.complementario, p.idtipodocumento, p.idtipoextension, p.direccion, p.telefono, p.celular, p.correo, p.estado, " +
        "td.idtipodocumento as ifore2, te.idtipoextension as ifore3, u.idusuario as ifore4, ur.idrol as ifore5 " +
        "FROM personas p " +
        "LEFT JOIN tiposdocumentos td ON td.idtipodocumento = p.idtipodocumento " +
        "LEFT JOIN tiposextensiones te ON te.idtipoextension = p.idtipoextension " +
        "LEFT JOIN usuarios u ON u.idpersona = p.idpersona " +
        "LEFT JOIN usuariosroles ur ON ur.idusuario = u.idusuario " +
        "WHERE p.idpersona = #{id}")
    @Results({
        @Result(property = "tipogenero", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposgenerosAod.dato")),
        @Result(property = "tipodocumento", column = "ifore2", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposdocumentosAod.dato")),
        @Result(property = "tipoextension", column = "ifore3", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposextensionesAod.dato")),
        @Result(property = "usuario", column = "ifore4", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IUsuariosAod.datousuario")),
        @Result(property = "rol", column = "ifore5", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato"))
    })
    Personas persona(Long id);

    @Options(useGeneratedKeys = true, keyProperty = "idpersona", keyColumn = "idpersona")
    @Insert("INSERT INTO personas(idtipogenero, primerapellido, segundoapellido, primernombre, dip, complementario, idtipodocumento, idtipoextension, direccion, telefono, celular, correo, formacion, estadocivil, hijos) VALUES (#{idtipogenero}, #{primerapellido}, #{segundoapellido}, #{primernombre}, #{dip}, #{complementario}, #{idtipodocumento}, #{idtipoextension}, #{direccion}, #{telefono}, #{celular}, #{correo}, #{formacion}, #{estadocivil}, #{hijos}) returning idpersona")
    void adicionar(Personas dato);

    @Update("UPDATE personas SET idtipogenero=#{idtipogenero}, primerapellido=#{primerapellido}, segundoapellido=#{segundoapellido}, primernombre=#{primernombre}, dip=#{dip}, complementario=#{complementario}, idtipodocumento=#{idtipodocumento}, idtipoextension=#{idtipoextension}, direccion=#{direccion}, telefono=#{telefono}, celular=#{celular}, correo=#{correo}, formacion=#{formacion}, estadocivil=#{estadocivil}, hijos=#{hijos} WHERE idpersona=#{idpersona} ")
    void modificar(Personas dato);

    @Update("UPDATE personas SET estado=#{estado} WHERE idpersona=#{idpersona}")
    void cambiarestado(Personas persona);

    @Select("SELECT * FROM usuarios u join personas p ON p.idpersona=u.idpersona WHERE u.idusuario=#{idusuario}")
    @Results({
        @Result(property = "tipogenero", column = "idtipogenero", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposgenerosAod.dato")),
        @Result(property = "tipodocumento", column = "idtipodocumento", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposdocumentosAod.dato")),
        @Result(property = "tipoextension", column = "idtipoextension", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposextensionesAod.dato")),
        @Result(property = "usuario", column = "idusuario", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IUsuariosAod.dato"))
    })
    Personas infoadicional(Long idusuario);

    @Select("SELECT idpersona FROM personas WHERE primerapellido=#{primerapellido} AND segundoapellido=#{segundoapellido} AND primernombre=#{primernombre} AND dip=#{dip}")
    Long verificarpersonaregistro(String primerapellido, String segundoapellido, String primernombre, String dip);

    @Delete("delete from personas where idpersona=#{id}")
    void eliminar(Long id);

    @Select("SELECT p.idpersona, p.primernombre, p.primerapellido, p.segundoapellido, p.dip " +
        "FROM usuariosroles ur " +
        "JOIN usuarios u using(idusuario) " +
        "JOIN personas p using(idpersona) " +
        "WHERE ur.idrol = #{idrol}")
    List<Personas> obtenerPersonasPorRol(Long idrol);
}
