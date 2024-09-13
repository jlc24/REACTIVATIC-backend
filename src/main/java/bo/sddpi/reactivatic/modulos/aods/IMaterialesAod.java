package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Materiales;

@Mapper
public interface IMaterialesAod {

    @Select("SELECT * FROM materiales WHERE idmaterial=#{idmaterial}")
    Materiales dato(Long idmaterial);

    @Select("SELECT * FROM materiales WHERE idproducto=#{idproducto}")
    List<Materiales> lista(Long idproducto);

    @Insert("INSERT INTO materiales (idproducto, material) VALUES (#{idproducto}, #{material})")
    void adicionar(Materiales material);

    @Delete("DELETE FROM materiales WHERE idmaterial=#{idmaterial}")
    void eliminar(Long idmaterial);
}
