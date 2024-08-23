package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Cargos;

@Mapper
public interface ICargosAod {

    @Select("SELECT * FROM cargos WHERE idcargo=#{idcargo}")
    Cargos dato(Long idcargo);

    @Select("SELECT * FROM cargos WHERE cargo ilike '%'||#{buscar}||'%' ORDER BY idcargo DESC LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property = "rol", column = "idrol", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRolesAod.dato"))
    })
    List<Cargos> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idcargo) FROM cargos WHERE cargo ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT * FROM cargos WHERE idrol=#{idrol} ORDER BY idcargo ASC")
    List<Cargos> listar(Long idrol);

    @Insert("INSERT INTO cargos (idrol, cargo, estado) VALUES (#{idrol}, #{cargo}, true)")
    @Options(useGeneratedKeys = true, keyProperty = "idcargo")
    void insertar(Cargos cargo);

    @Update("UPDATE cargos SET idrol=#{idrol}, cargo=#{cargo}")
    void actualizar(Cargos cargo);

    @Delete("DELETE FROM cargos WHERE idcargo=#{idcargo}")
    void eliminar(Cargos cargo);
}
