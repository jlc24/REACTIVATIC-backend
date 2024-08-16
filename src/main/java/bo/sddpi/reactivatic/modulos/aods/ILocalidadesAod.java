package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Localidades;

@Mapper
public interface ILocalidadesAod {

    @Select("SELECT idlocalidad, idmunicipio, localidad FROM localidades WHERE idmunicipio=#{id} ORDER BY localidad")
    List<Localidades> datos(Long id);

    @Select("SELECT idlocalidad, idmunicipio, idmunicipio as ifore1, localidad FROM localidades where idlocalidad=#{id} ")
    @Results({
        @Result(property = "municipio", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IMunicipiosAod.dato"))
    })
    Localidades dato(Long id);

    @Select("SELECT * FROM localidades WHERE idmunicipio=#{idmunicipio}")
    List<Localidades> listaLocalidades(long idmunicipio);

    @Insert("insert into localidades(idmunicipio, localidad) values (#{idmunicipio}, #{localidad})")
    void adicionar(Localidades dato);

    @Update("update localidades set idmunicipio=#{idmunicipio}, localidad=#{localidad} where idlocalidad=#{idlocalidad} ")
    void modificar(Localidades dato);

    @Select("select idlocalidad, municipio||'-'||localidad as localidad from localidades join municipios using(idmunicipio) order by municipio, localidad")
    List<Localidades> datosl();
}
