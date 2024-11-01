package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Asistenciasempresas;

@Mapper
public interface IAsistenciasempresasAod {

    @Insert("INSERT INTO asistenciasempresas(idasistencia, idbeneficioempresa, asistencia, fecha) VALUES (#{idasistencia}, #{idbeneficioempresa}, #{asistencia}, #{fecha})")
    @Options(useGeneratedKeys = true, keyProperty = "idasistenciaempresa", keyColumn = "idasistenciaempresa")
    void adicionar(Asistenciasempresas dato);

    @Update("UPDATE asistenciasempresas SET asistencia=#{asistencia} WHERE idasistenciaempresa=#{idasistenciaempresa} ")
    void modificarasistencia(Asistenciasempresas asistenciaempresa);

    @Delete("DELETE FROM asistenciaempresas WHERE idasistenciaempresa=#{id}")
    void eliminar(Long id);

    @Select("SELECT fecha FROM asistenciasempresas WHERE idasistencia=#{idasistencia} GROUP BY fecha ORDER BY fecha")
    List<Asistenciasempresas> fechas(Long idasistencia);

    @Select("SELECT * FROM asistenciasempresas WHERE idasistencia=#{idasistencia} ORDER BY idasistenciaempresa")
    List<Asistenciasempresas> datos(Long idasistencia);

}
