package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.*;
// import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Tiposdocumentos;

import java.util.List;

@Mapper
public interface ITiposdocumentosAod {

    @Select("SELECT idtipodocumento, tipodocumento, documento FROM tiposdocumentos where idtipodocumento=#{id} ")
    Tiposdocumentos dato(Long id);

    @Select("SELECT * FROM tiposdocumentos WHERE estado=true ORDER BY created_at DESC")
    List<Tiposdocumentos> listar();

    @Insert("INSERT INTO tiposdocumentos (tipodocumento, documento) VALUES (#{tipodocumento}, #{documento})")
    @Options(useGeneratedKeys = true, keyProperty = "idtipodocumento")
    void insertar(Tiposdocumentos tiposdocumentos);

    @Update("UPDATE tiposdocumentos SET tipodocumento = #{tipodocumento}, documento = #{documento} WHERE idtipodocumento = #{idtipodocumento}")
    void actualizar(Tiposdocumentos tiposdocumentos);

    @Delete("DELETE FROM tiposdocumentos WHERE idtipodocumento = #{id}")
    void eliminar(Long id);
}