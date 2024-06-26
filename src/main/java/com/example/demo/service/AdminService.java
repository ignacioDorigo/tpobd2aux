package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import com.example.demo.TpomongoredisApplication;
import com.example.demo.modelo.Admin;
import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.Factura;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.FacturaRepository;

@Service
public class AdminService {

	@Autowired
	AdminRepository repositorio;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	FacturaRepository facturaRepository;

	public void guardarEnRedis(Admin admin) {
		// Crear una fábrica de conexiones Lettuce
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
		connectionFactory.afterPropertiesSet(); // Inicializa la fábrica de conexiones

		// Crear una plantilla de Redis (clave , valor)
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory); // Configura la plantilla con la fábrica de conexiones
		template.setDefaultSerializer(StringRedisSerializer.UTF_8); // Establece el serializador para las claves y //
																	// valores
		template.afterPropertiesSet(); // Inicializa la plantilla de Redis

		String mailAdmin = admin.getMail();
		String password = admin.getPassword();
//      Guardamos el objeto (clave , valor)
		template.opsForValue().set("admin:" + mailAdmin, password);
////      Mostrar objeto guardado
//		System.out.println(template.opsForValue().get("pedro@gmail.com"));
		// Cerrar la fábrica de conexiones
		connectionFactory.destroy();
		System.out.println("Guardado en Redis");
	}

	public boolean loginRedis(String mail, String password) {
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
		connectionFactory.afterPropertiesSet(); // Inicializa la fábrica de conexiones

		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setDefaultSerializer(StringRedisSerializer.UTF_8);
		template.afterPropertiesSet();

//		Si encuentra el mail, devuelve el password
		String clave = "admin:" + mail;
		String resultado = template.opsForValue().get(clave);
		if (resultado != null) {
			System.out.println("Mail Redis encontrado");
			if (resultado.equals(password)) {
				System.out.println("Login Exitoso en redis");
				return true;
			} else {
				System.out.println("La contrasenia es invalida");
				return false;
			}
		} else {
			System.out.println("Mail Redis No encontrado");
			return false;
		}

	}

	public String registerAdmin(String mail, String password, String nombre, String apellido, String documento) {
		Optional<Admin> adminOptional = repositorio.findById(mail);
		if (adminOptional.isEmpty()) {
			Admin nuevo = new Admin(mail, password, nombre, apellido, documento);
			repositorio.save(nuevo);
			emailSenderService.sendEmail("ferorrego67@gmail.com", "Registro en APP",
					"Te has registrado exitosamente en la app");
			guardarEnRedis(nuevo);
			return "Register Exitoso";
		} else {
			return "Ya existe ese mail";
		}
	}

	public String eliminarAdmin(String mail) {
		Optional<Admin> adminOptional = repositorio.findById(mail);
		if (adminOptional.isPresent()) {
			repositorio.deleteById(mail);
			return "Admin Eliminado";
		} else {
			return "Admin No Existe";

		}
	}

	public String loginAdmin(String mail, String password) {
		Optional<Admin> adminOptional = repositorio.findById(mail);
		if (loginRedis(mail, password) && adminOptional.isPresent()) {
			Admin admin = adminOptional.get();
			if (admin.getPassword().equals(password)) {
				return "Login Exitoso";
			} else {
				return "Contrasena incorrecta";
			}
		} else {
			return "Usuario No Registrado";
		}
	}

	public String olvideContrasena(String mail) {
		Optional<Admin> adminOptional = repositorio.findById(mail);
		if (adminOptional.isPresent()) {
			Admin admin = adminOptional.get();
			String contrasenia = admin.getPassword();
			emailSenderService.sendEmail("ferorrego67@gmail.com", "Recupero contrasenia en APP",
					"Tu contrasenia es : " + contrasenia);
			return "Envio de contrasenia al mail";
		} else {
			return "Admin encontrado";
		}
	}

	public Admin perfilAdmin(String mail) {
		Optional<Admin> adminOptional = repositorio.findById(mail);
		if (adminOptional.isPresent()) {
			Admin admin = adminOptional.get();
			return admin;
		} else {
			return null;
		}
	}

	public String cambiarContraseniaAdmin(String mail, String actual, String nueva1, String nueva2) {
		Optional<Admin> adminOptional = repositorio.findById(mail);
		if (adminOptional.isPresent()) {
			Admin admin = adminOptional.get();
			String contrasenia = admin.getPassword();
			if (contrasenia.equals(actual)) {
				if (nueva1.equals(nueva2)) {
					admin.setPassword(nueva1);
					repositorio.save(admin);
					emailSenderService.sendEmail("ferorrego67@gmail.com", "Cambio contrasenia en APP",
							"Has cambiado tu contrasenia, tu nueva contrasenia es: " + nueva1);
					guardarEnRedis(admin);
					return "Cambio contrasenia exitoso";

				} else {
					return "Las contrasenias nuevas no coinciden";
				}

			} else {
				return "No coincide la contrasenia actual";
			}
		} else {
			return "Admin no encontrado";
		}
	}

	public List<Factura> facturas() {
		return facturaRepository.findAll();
	}

}
