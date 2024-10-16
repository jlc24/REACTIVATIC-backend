package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Representantes;

@Mapper
public interface IRepresentantesAod {

    @Insert("insert into representantes(idpersona) values (#{idpersona})")
    void adicionar(Representantes dato);

    @Select("SELECT r.idrepresentante, r.idpersona, p.primerapellido, p.segundoapellido, p.primernombre " +
        "FROM representantes r " +
        "JOIN personas p ON r.idpersona = p.idpersona " +
        "WHERE CONCAT(p.primernombre, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.dip) ILIKE '%'||#{buscar}||'%' ORDER BY p.created_at DESC LIMIT #{cantidad} OFFSET #{pagina}")
        @Results({
            @Result(property ="persona", column ="idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        })
    List<Representantes> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(r.idrepresentante) " +
        "FROM representantes r " +
        "JOIN personas p ON r.idpersona = p.idpersona " +
        "WHERE CONCAT(p.primernombre, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.dip) ILIKE '%'||#{buscar}||'%'")
    Integer cantidad(String buscar);

    @Select("SELECT r.idrepresentante, r.idpersona, p.primerapellido, p.segundoapellido, p.primernombre " +
        "FROM representantes r " +
        "JOIN personas p ON r.idpersona = p.idpersona " +
        "WHERE p.estado=true AND CONCAT(p.primernombre, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.dip, ' ') ILIKE '%' || #{buscar} || '%'")
        @Results({
            @Result(property ="persona", column ="idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        })
    List<Representantes> buscar(String buscar);

    @Select("SELECT DISTINCT r.idrepresentante, r.idpersona, p.primerapellido, p.segundoapellido, p.primernombre, p.dip, " +
        "EXISTS (SELECT 1 FROM beneficiosempresas be WHERE be.idempresa = e.idempresa AND be.idbeneficio = #{idbeneficio}) AS en_beneficio " +
        "FROM representantes r " +
        "JOIN personas p ON r.idpersona = p.idpersona " +
        "JOIN empresas e ON e.idrepresentante = r.idrepresentante " +
        "LEFT JOIN beneficiosempresas be ON be.idempresa = e.idempresa " +
        "WHERE CONCAT(p.primernombre, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.dip) ILIKE '%'||#{buscar}||'%' " +
        "AND p.estado=true " +
        "ORDER BY p.primerapellido LIMIT #{cantidad} OFFSET #{pagina}")
    @Results({
        @Result(property ="persona", column ="idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        @Result(property = "enBeneficio", column = "en_beneficio"),
    })
    List<Representantes> datosl(String buscar, Integer pagina, Integer cantidad, Long idbeneficio);

    @Select("SELECT COUNT(r.idrepresentante) " +
        "FROM representantes r " +
        "JOIN personas p ON r.idpersona = p.idpersona " +
        "JOIN empresas e ON e.idrepresentante = r.idrepresentante " +
        "WHERE CONCAT(p.primernombre, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.dip) ILIKE '%'||#{buscar}||'%' AND p.estado=true ")
    Integer cantidaddatosl(String buscar);

    @Select("SELECT * FROM representantes WHERE idrepresentante=#{id} ")
    @Results({
        @Result(property = "persona", column = "idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    })
    Representantes dato(Long id);

    @Delete("delete from representantes where idpersona=#{id}")
    void eliminar(Long id);

}
