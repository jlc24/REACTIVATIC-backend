package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.*;
// import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Tiposgeneros;

import java.util.List;

@Mapper
public interface ITiposgenerosAod {

    @Select("SELECT idtipogenero, tipogenero FROM tiposgeneros where idtipogenero=#{id} ")
    Tiposgeneros dato(Long id);

    @Select("SELECT * FROM tiposgeneros WHERE estado=true ORDER BY created_at")
    List<Tiposgeneros> listar();

    @Insert("INSERT INTO tiposgeneros (tipogenero) VALUES (#{tipogenero})")
    @Options(useGeneratedKeys = true, keyProperty = "idtipogenero")
    void insertar(Tiposgeneros tiposgeneros);

    @Update("UPDATE tiposgeneros SET tipogenero = #{tipogenero} WHERE idtipogenero = #{idtipogenero}")
    void actualizar(Tiposgeneros tiposgeneros);

    @Delete("DELETE FROM tiposgeneros WHERE idtipogenero = #{id}")
    void eliminar(Long id);

}