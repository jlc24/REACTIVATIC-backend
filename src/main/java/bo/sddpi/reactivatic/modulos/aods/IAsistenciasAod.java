package bo.sddpi.reactivatic.modulos.aods;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Asistencias;

@Mapper
public interface IAsistenciasAod {

    @Select("SELECT * FROM asistencias WHERE idbeneficio=#{idbeneficio}")
    Asistencias dato(Long idbeneficio);

    @Insert("INSERT INTO asistencias(idbeneficio, dias, duraciondias, duracioncurso) VALUES (#{idbeneficio}, #{dias}, #{duraciondias}, #{duracioncurso})")
    @Options(useGeneratedKeys = true, keyProperty = "idasistencia", keyColumn = "idasistencia")
    void adicionar(Asistencias dato);

    @Update("UPDATE asistencias SET dias=#{dias}, duraciondias=#{duraciondias}, duracioncurso=#{duracioncurso} WHERE idasistencia=#{idasistencia}")
    void modificar(Asistencias dato);
}
