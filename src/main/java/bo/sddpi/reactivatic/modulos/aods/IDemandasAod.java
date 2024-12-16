package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Demandas;

@Mapper
public interface IDemandasAod {

    @Select("SELECT * FROM demandas WHERE idempresa=#{empresa}")
    List<Demandas> demandas(Long empresa);

    @Select("SELECT * FROM ofertas WHERE idempresa=#{empresa} AND idoferta=#{id}")
    Demandas dato(Long empresa, Long id);

    @Select("SELECT * FROM demandas WHERE demanda=#{demanda}")
    Long verificardemanda(String demanda);

    @Insert("INSERT INTO demandas (idempresa, tipodemanda, demanda, estado) values (#{idempresa}, #{tipodemanda}, #{demanda}, true)")
    void adicionar(Demandas demanda);

    @Update("UPDATE demandas SET tipodemanda=#{tipodemanda}, demanda=#{demanda}")
    void modificar(Demandas demanda);

    @Delete("DELETE FROM demandas WHERE iddemanda=#{iddemanda}")
    void eliminar(Long iddemanda);
}
