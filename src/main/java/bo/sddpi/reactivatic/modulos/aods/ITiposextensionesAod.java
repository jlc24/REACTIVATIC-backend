package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.*;
// import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Tiposextensiones;

import java.util.List;

@Mapper
public interface ITiposextensionesAod {

    @Select("SELECT idtipoextension, tipoextension, sigla FROM tiposextensiones where idtipoextension=#{id} ")
    Tiposextensiones dato(Long id);

    @Select("SELECT * FROM tiposextensiones WHERE estado=true ORDER BY created_at DESC")
    List<Tiposextensiones> listar();

    @Insert("INSERT INTO tiposextensiones (tipoextension, sigla) VALUES (#{tipoextension}, #{sigla})")
    @Options(useGeneratedKeys = true, keyProperty = "idtipoextension")
    void insertar(Tiposextensiones tiposextensiones);

    @Update("UPDATE tiposextensiones SET tipoextension = #{tipoextension}, sigla = #{sigla} WHERE idtipoextension = #{idtipoextension}")
    void actualizar(Tiposextensiones tiposextensiones);

    @Delete("DELETE FROM tiposextensiones WHERE idtipoextension = #{id}")
    void eliminar(Long id);
}