package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;


@Mapper
public interface IBeneficiosempresasAod {
    
    @Select("SELECT e.empresa FROM empresas AS e " + 
            "INNER JOIN beneficiosempresas AS be ON be.idempresa=e.idempresa" +
            "INNER JOIN beneficios AS b ON b.idbeneficio=be.idbeneficio" +
            "WHERE b.idbeneficio=#{idbeneficio} ORDER BY e.empresa")
    List<String> buscarbeneficio(Long idbeneficio);

    @Select("SELECT b.beneficio FROM beneficios AS b " + 
            "INNER JOIN beneficiosempresas AS be ON be.idebeneficio=b.idebeneficio" +
            "WHERE be.idempresa=#{idempresa} ORDER BY b.beneficio")
    List<String> buscarempresa(Long idempresa);

    @Insert("INSERT INTO beneficiosempresas (idbeneficio, idempresa) VALUES (#{idbeneficio}, #{idempresa})")
    @Options(useGeneratedKeys = true, keyProperty = "idbeneficioempresa")
    void asignarbeneficio(Long idbeneficio, Long idempresa);

    @Delete("DELETE FROM beneficiosempresas Where idbeneficio=#{idbeneficio} AND idempresa=#{idempresa}")
    void eliminarempresabeneficio(Long idbeneficio, Long idempresa);

}
