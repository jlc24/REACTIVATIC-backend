package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Beneficiosempresas;

@Mapper
public interface IBeneficiosempresasAod {
    
    @Select("SELECT e.empresa FROM empresas AS e " + 
            "INNER JOIN beneficiosempresas AS be ON be.idempresa=e.idempresa" +
            "INNER JOIN beneficios AS b ON b.idbeneficio=be.idbeneficio" +
            "WHERE b.idbeneficio=#{idbeneficio} ORDER BY e.empresa")
    List<Beneficiosempresas> buscarbeneficio(Beneficiosempresas beneficioempresa);

    @Select("SELECT b.beneficio FROM beneficios AS b " + 
            "INNER JOIN beneficiosempresas AS be ON be.idebeneficio=b.idebeneficio" +
            "WHERE be.idempresa=#{idempresa} ORDER BY b.beneficio")
    List<Beneficiosempresas> buscarempresa(Beneficiosempresas beneficioempresa);


}
