package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Atributos;

@Mapper
public interface IAtributosAod {

    @Select("SELECT * FROM atributos WHERE idproducto=#{idproducto}")
    List<Atributos> lista(Long idproducto);

    @Insert("INSERT INTO atributos (idproducto, atributo, detalle) VALUES (#{idproducto}, #{atributo}, #{detalle})")
    void adicionar(Atributos atributo);

    @Delete("DELETE FROM atributos WHERE idatributo=#{idatributo}")
    void eliminar(Long idatributo);
}
