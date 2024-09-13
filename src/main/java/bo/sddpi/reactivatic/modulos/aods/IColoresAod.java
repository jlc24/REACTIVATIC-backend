package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Colores;

@Mapper
public interface IColoresAod {

    @Select("SELECT * FROM colores WHERE idcolor=#{idcolor}")
    Colores dato(Long idcolor);
    
    @Select("SELECT * FROM colores WHERE idproducto=#{idproducto}")
    List<Colores> lista(Long idproducto);

    @Insert("INSERT INTO colores (idproducto, color, codigo) VALUES (#{idproducto}, #{color}, #{codigo})")
    void adicionar(Colores precio);

    @Delete("DELETE FROM colores WHERE idcolor=#{idcolor}")
    void eliminar(Long idcolor);
}
