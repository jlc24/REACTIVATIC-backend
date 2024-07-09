package bo.profesional.cesarnvf.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.profesional.cesarnvf.modulos.entidades.Municipios;

@Mapper
public interface IMunicipiosAod {

    @Select("SELECT idmunicipio, municipio FROM municipios WHERE municipio ilike '%'||#{buscar}||'%' ORDER BY municipio LIMIT #{cantidad} OFFSET #{pagina} ")
    List<Municipios> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idmunicipio) FROM municipios where municipio ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT idmunicipio, municipio FROM municipios where idmunicipio=#{id} ")
    Municipios dato(Long id);

    @Insert("insert into municipios(municipio) values (#{municipio})")
    void adicionar(Municipios dato);

    @Update("update municipios set municipio=#{municipio} where idmunicipio=#{idmunicipio} ")
    void modificar(Municipios dato);

    @Select("SELECT idmunicipio, municipio FROM municipios ORDER BY municipio")
    List<Municipios> datosl();

}
