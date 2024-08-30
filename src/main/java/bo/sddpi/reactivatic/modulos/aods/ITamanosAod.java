package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Colores;
import bo.sddpi.reactivatic.modulos.entidades.Tamanos;

@Mapper
public interface ITamanosAod {

    @Select("SELECT * FROM tamanos WHERE idproducto=#{idproducto}")
    List<Tamanos> lista(Long idproducto);

    @Insert("INSERT INTO tamanos (idproducto, tamano) VALUES (#{idproducto}, #{tamano})")
    void adicionar(Tamanos tamano);

    @Delete("DELETE FROM tamanos WHERE idtamano=#{idtamano}")
    void eliminar(Long idtamano);
}
