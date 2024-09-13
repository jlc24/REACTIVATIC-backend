package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Rubros;

@Mapper
public interface IRubrosAod {

    @Select("SELECT * FROM rubros WHERE rubro ilike '%'||#{buscar}||'%' ORDER BY idrubro LIMIT #{cantidad} OFFSET #{pagina} ")
    List<Rubros> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idrubro) FROM rubros where rubro ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT idrubro, rubro FROM rubros where idrubro=#{id} ")
    Rubros dato(Long id);

    @Insert("insert into rubros(rubro) values (#{rubro})")
    void adicionar(Rubros dato);

    @Update("update rubros set rubro=#{rubro} where idrubro=#{idrubro} ")
    void modificar(Rubros dato);

    @Select("SELECT idrubro, rubro FROM rubros WHERE estado=true ORDER BY rubro")
    List<Rubros> datosl();

    @Select("SELECT idrubro, rubro FROM rubros WHERE estado=true AND rubro=#{rubro} ORDER BY rubro")
    List<Rubros> datoslrubro(String rubro);

    @Select("SELECT r.idrubro, r.rubro, COUNT(DISTINCT p.idproducto) AS cantidad " + 
            "FROM productos p " + 
            "JOIN empresas e ON p.idempresa = e.idempresa " + 
            "JOIN rubros r ON e.idrubro = r.idrubro " + 
            "GROUP BY r.idrubro, r.rubro " + 
            "ORDER BY r.rubro;")
    @Results({
        @Result(property = "idrubro", column = "idrubro"),
        @Result(property = "rubro", column = "rubro"),
        @Result(property = "cantidad", column = "cantidad")
    })
    List<Rubros> cantidadporrubro();

    @Update("UPDATE rubros SET estado=#{estado} WHERE idrubro=#{idrubro}")
    void cambiarestado(Rubros rubro);

    @Select("SELECT idrubro FROM rubros WHERE rubro=#{rubro}")
    Long verificarrubro(String rubro);
}
