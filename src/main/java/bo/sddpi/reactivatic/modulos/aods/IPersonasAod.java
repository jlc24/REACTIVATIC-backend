package bo.sddpi.reactivatic.modulos.aods;

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

@Mapper
public interface IPersonasAod {

    @Select("SELECT idpersona, idtipogenero, idtipogenero as ifore1, primerapellido, segundoapellido, primernombre, segundonombre, fechanacimiento, dip, direccion, telefono, celular, correo, estado FROM personas where idpersona=#{id} ")
    @Results({
        @Result(property = "tipogenero", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposgenerosAod.dato"))
    })
    Personas dato(Long id);

    @Options(useGeneratedKeys = true, keyProperty = "idpersona", keyColumn = "idpersona")
    @Insert("insert into personas(idtipogenero, primerapellido, segundoapellido, primernombre, segundonombre, fechanacimiento, dip, direccion, telefono, celular, correo, estado) values (#{idtipogenero}, #{primerapellido}, #{segundoapellido}, #{primernombre}, #{segundonombre}, #{fechanacimiento}, #{dip}, #{direccion}, #{telefono}, #{celular}, #{correo}, true) returning idpersona")
    void adicionar(Personas dato);

    @Update("update personas set idtipogenero=#{idtipogenero}, primerapellido=#{primerapellido}, segundoapellido=#{segundoapellido}, primernombre=#{primernombre}, segundonombre=#{segundonombre}, fechanacimiento=#{fechanacimiento}, dip=#{dip}, direccion=#{direccion}, telefono=#{telefono}, celular=#{celular}, correo=#{correo}, estado=#{estado} where idpersona=#{idpersona} ")
    void modificar(Personas dato);

    @Select("select idpersona, idtipogenero, primerapellido, segundoapellido, primernombre, segundonombre, fechanacimiento, dip, numerocomplementario, direccion, telefono, celular, correo from usuarios join personas using(idpersona) where idusuario=#{idusuario}")
    @Results({
        @Result(property = "tipogenero", column = "idtipogenero", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.ITiposgenerosAod.dato"))
    })
    Personas infoadicional(Long idusuario);

    @Delete("delete from personas where idpersona=#{id}")
    void eliminar(Long id);
}
