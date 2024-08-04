package bo.sddpi.reactivatic.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

//import bo.sddpi.reactivatic.modulos.aods.IPersonasAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosAod;
//import bo.sddpi.reactivatic.modulos.entidades.Personas;
import bo.sddpi.reactivatic.modulos.entidades.Usuarios;

@Component
public class InfoAdicionalToken implements TokenEnhancer{

	@Autowired
	//IPersonasAod iPersonasAod;
	IUsuariosAod iUsuariosAod;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> info = new HashMap<String, Object>();
		//Personas persona = iPersonasAod.infoadicional(Long.parseLong(authentication.getName()));
		Usuarios usuario = iUsuariosAod.dato(Long.parseLong(authentication.getName()));
		info.put("nombre", usuario.getPersona().getPrimerapellido()+ " "+ usuario.getPersona().getPrimernombre());
		info.put("nombrecliente", usuario.getPersona().getPrimernombre());
		info.put("rol", usuario.getRol().getNombrerol());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		return accessToken;
	}

}