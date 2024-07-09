package bo.profesional.cesarnvf.modulos.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bo.profesional.cesarnvf.modulos.aods.IUsuariosAod;
import bo.profesional.cesarnvf.modulos.aods.IUsuariosrolesAod;
import bo.profesional.cesarnvf.modulos.entidades.Usuarios;
import bo.profesional.cesarnvf.modulos.entidades.Usuariosroles;

@Service
public class UsuariosServ implements UserDetailsService {

    @Autowired
    IUsuariosAod iUsuariosAod;

    @Autowired
    IUsuariosrolesAod iUsuariosrolesAod;

    @Override
    public UserDetails loadUserByUsername(String eusuario) throws UsernameNotFoundException {
        Usuarios usuario = iUsuariosAod.verificausuario(eusuario);
        if (usuario == null) {
            throw new UsernameNotFoundException("No se encontro al usuario: " + eusuario);
        } else {
            List<GrantedAuthority> roles = new ArrayList<>();
            List<Usuariosroles> usuarioroles = iUsuariosrolesAod.usuarioroles(usuario.getIdusuario());
            usuarioroles.forEach((usuariorol) -> {
                roles.add(new SimpleGrantedAuthority(usuariorol.getRol().getRol()));
            });
            return new User(Long.toString(usuario.getIdusuario()), usuario.getClave(), roles);
        }
    }

}