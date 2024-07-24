package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Comentarios;

@Mapper
public interface IComentariosAod {
    
    @Select("SELECT * FROM comentarios WHERE idproducto=#{idproducto} ORDER BY created_at DESC LIMTT #{cantidad} OFFSET #{pagina}")
    List<Comentarios> buscarp(Long idproducto, Integer pagina, Integer cantidad);

    @Select("SELECT count(idcomentario) WHERE idproducto=#{idproducto}")
    Integer cantidadp(Comentarios idproducto);

    @Select("SELECT * FROM comentarios WHERE idcliente=#(idcliente) && idproducto=#{idproducto} ORDER BY created_at DESC LIMIT #{cantidad} OFFSET #{pagina}")
    List<Comentarios> buscarc(Long idcliente, Long idproducto, Integer pagina, Integer cantidad);

    @Select("SELECT count(idcomentario) WHERE idcliente=#{idcliente} && idprocuto=#{idproducto}")
    Integer cantidadc(Long idcliente, Long idproducto);

    @Insert("INSERT INTO comentarios (comentario, idcliente, valoracion, idproducto) VALUES (#{comentario}, #{idcliente}, #{valoracion}, #{idproduco})")
    void adicionar(Comentarios comentario);

    @Update("UPDATE comentarios SET estado=#{estado} WHERE idcomentario=#{idcomentario}")
    void cambiarestado(Comentarios comentario);

    @Delete("DELETE FROM comentarios WHERE idcomentario=#{idcomentario}")
    void eliminar(Comentarios comentario);
}
