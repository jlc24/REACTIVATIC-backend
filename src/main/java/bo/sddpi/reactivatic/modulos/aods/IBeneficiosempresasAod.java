package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Beneficiosempresas;


@Mapper
public interface IBeneficiosempresasAod {
    
    @Select("SELECT * FROM beneficiosempresas be " +
            "JOIN empresas e ON e.idempresa=be.idempresa " +
            "JOIN rubros rb ON rb.idrubro = e.idrubro " +
            "JOIN representantes r ON r.idrepresentante=e.idrepresentante " +
            "JOIN personas p ON p.idpersona=r.idpersona " +
            "WHERE CONCAT(p.primernombre, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.dip) ILIKE '%'||#{buscar}||'%' " +
            "AND be.idbeneficio=#{idbeneficio} " +
            "ORDER BY rb.rubro, p.primerapellido  LIMIT #{cantidad} OFFSET #{pagina}")
        @Results({
                @Result(property = "empresa", column = "idempresa", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato")),
        })
    List<Beneficiosempresas> datos(Long idbeneficio, String buscar, Integer cantidad, Integer pagina);

    @Select("SELECT COUNT(be.idbeneficioempresa) FROM beneficiosempresas be " +
            "JOIN empresas e ON e.idempresa=be.idempresa " +
            "JOIN representantes r ON r.idrepresentante=e.idrepresentante " +
            "JOIN personas p ON p.idpersona=r.idpersona " +
            "WHERE CONCAT(p.primernombre, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.dip) ILIKE '%'||#{buscar}||'%' " +
            "AND be.idbeneficio=#{idbeneficio} ")
    Integer cantidad(Long idbeneficio, String buscar);

    @Select("SELECT r.rubro as rubro, COUNT(be.idbeneficioempresa) as cantidad " +
        "FROM beneficiosempresas be " +
        "JOIN empresas e ON e.idempresa = be.idempresa " +
        "JOIN rubros r ON r.idrubro = e.idrubro " +
        "JOIN representantes rep ON rep.idrepresentante = e.idrepresentante " +
        "JOIN personas p ON p.idpersona = rep.idpersona " +
        "WHERE be.idbeneficio = #{idbeneficio} " +
        "GROUP BY r.rubro " +
        "ORDER BY cantidad DESC")
    List<Map<String, Object>> cantidadPorRubro(Long idbeneficio, String buscar);
    
    @Insert("INSERT INTO beneficiosempresas (idbeneficio, idempresa) VALUES (#{idbeneficio}, #{idempresa})")
    @Options(useGeneratedKeys = true, keyProperty = "idbeneficioempresa")
    void adicionar(Beneficiosempresas beneficioempresa);

    @Delete("DELETE FROM beneficiosempresas WHERE idbeneficio=#{idbeneficio} AND idempresa=#{idempresa}")
    void eliminar(Beneficiosempresas benficioempresa);

    @Select("SELECT COUNT(idbeneficioempresa) FROM beneficiosempresas WHERE idbeneficio=#{idbeneficio}")
    Integer cantidadBeneficio(Long idbeneficio);

    @Select("SELECT e.empresa FROM empresas AS e " + 
            "INNER JOIN beneficiosempresas AS be ON be.idempresa=e.idempresa" +
            "INNER JOIN beneficios AS b ON b.idbeneficio=be.idbeneficio" +
            "WHERE b.idbeneficio=#{idbeneficio} ORDER BY e.empresa")
    List<String> buscarbeneficio(Long idbeneficio);

    @Select("SELECT b.beneficio FROM beneficios AS b " + 
            "INNER JOIN beneficiosempresas AS be ON be.idebeneficio=b.idebeneficio" +
            "WHERE be.idempresa=#{idempresa} ORDER BY b.beneficio")
    List<String> buscarempresa(Long idempresa);

    @Select("SELECT * FROM beneficiosempresas be " +
            "JOIN empresas e ON e.idempresa=be.idempresa " +
            "JOIN rubros rb ON rb.idrubro = e.idrubro " +
            "JOIN representantes r ON r.idrepresentante=e.idrepresentante " +
            "JOIN personas p ON p.idpersona=r.idpersona " +
            "WHERE be.idbeneficio=#{idbeneficio} " +
            "ORDER BY rb.rubro, p.primerapellido ")
        @Results({
                @Result(property = "empresa", column = "idempresa", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IEmpresasAod.dato")),
        })
    List<Beneficiosempresas> planillareg(Long idbeneficio);
}
