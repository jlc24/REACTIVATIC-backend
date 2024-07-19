package bo.sddpi.reactivatic.modulos.servicios.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import bo.sddpi.reactivatic.modulos.entidades.Correos;
import bo.sddpi.reactivatic.modulos.servicios.ICorreosServ;

@Service
public class CorreosServImpl implements ICorreosServ {

    @Autowired
	private JavaMailSender envio;

    @Override
    public boolean enviarcorreo(Correos correo) throws MessagingException {
        return sendEmailTool(correo.getContenido(), correo.getCorreo(), correo.getTema());
    }

    private boolean sendEmailTool(String contenido, String correo, String tema) throws MessagingException {
        boolean send = false;
		MimeMessage mensaje = envio.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, CharEncoding.UTF_8);
		try {
			helper.setTo(correo);
			helper.setText(contenido, true);
			helper.setSubject(tema);
			helper.addInline("logo", new ClassPathResource("logo.jpg"), "image/png");
			envio.send(mensaje);
			send = true;
		} catch (MessagingException e) {
            System.out.println("Error" + e.toString());

		}
		return send;
    }


}