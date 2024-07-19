package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Categorias;

import java.util.List;

@Mapper
public interface ICategoriasAod {
    
    @Select("SELECT idcategoria, categoria, ruta, iconocategoria, orden FROM categorias WHERE idcategoria = #{id}")
    Categorias dato(Long id);

    @Select("SELECT idcategoria, categoria, ruta, iconocategoria, orden, estado, created_at FROM categorias WHERE categoria ilike '%'||#{buscar}||'%' ORDER BY orden LIMIT #{cantidad} OFFSET #{pagina}")
    List<Categorias> buscar(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idcategoria) FROM categorias WHERE categoria ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT idcategoria, categoria, ruta, iconocategoria, orden FROM categorias WHERE estado = true ORDER BY orden")
    List<Categorias> listar();

    @Insert("INSERT INTO categorias (categoria, ruta, iconocategoria, orden) VALUES (#{categoria}, #{ruta}, #{iconocategoria}, #{orden})")
    @Options(useGeneratedKeys = true, keyProperty = "idcategoria")
    void insertar(Categorias categorias);

    @Update("UPDATE categorias SET categoria = #{categoria}, ruta = #{ruta}, iconocategoria = #{iconocategoria}, orden = #{orden} WHERE idcategoria = #{idcategoria}")
    void actualizar(Categorias categorias);
    
    @Update("UPDATE categorias SET estado = #{estado} WHERE idcategoria = #{idcategoria}")
    void cambiarestado(Categorias categorias);

    @Delete("DELETE FROM categorias WHERE idcategoria = #{idcategoria}")
    void eliminar(Categorias categorias);
}