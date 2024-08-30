package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Precios;

@Mapper
public interface IPreciosAod {

    @Select("SELECT * FROM precios WHERE idproducto=#{idproducto}")
    List<Precios> lista(Long idproducto);

    @Insert("INSERT INTO precios (idproducto, precio, cantidad) VALUES (#{idproducto}, #{precio}, #{cantidad})")
    void adicionar(Precios precio);

    @Delete("DELETE FROM precios WHERE idprecio=#{idprecio}")
    void eliminar(Long idprecio);
}
