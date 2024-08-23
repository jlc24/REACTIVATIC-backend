package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Municipios;

@Mapper
public interface IMunicipiosAod {

    @Select("SELECT * FROM municipios WHERE municipio ilike '%'||#{buscar}||'%' ORDER BY municipio LIMIT #{cantidad} OFFSET #{pagina} ")
    List<Municipios> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idmunicipio) FROM municipios where municipio ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT idmunicipio, municipio FROM municipios where idmunicipio=#{id} ")
    Municipios dato(Long id);

    @Insert("insert into municipios(municipio) values (#{municipio})")
    void adicionar(Municipios dato);

    @Update("update municipios set municipio=#{municipio} where idmunicipio=#{idmunicipio} ")
    void modificar(Municipios dato);

    @Select("SELECT idmunicipio, municipio FROM municipios WHERE estado=true ORDER BY municipio")
    List<Municipios> datosl();

    @Select("SELECT idmunicipio FROM municipios WHERE municipio=#{municipio}")
    Long verificarmunicipio(String municipio);

    @Update("UPDATE municipios SET estado=#{estado} WHERE idmunicipio=#{idmunicipio}")
    void cambiarestado(Municipios municipio);
}
