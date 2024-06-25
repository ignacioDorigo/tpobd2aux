package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public String registerAdmin(String mail, String password, String nombre, String apellido, String documento) {
		Optional<Admin> adminOptional = repositorio.findById(mail);
		if (adminOptional.isEmpty()) {
			Admin nuevo = new Admin(mail, password, nombre, apellido, documento);
			repositorio.save(nuevo);
			emailSenderService.sendEmail("ferorrego67@gmail.com", "Registro en APP",
					"Te has registrado exitosamente en la app");
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
		if (adminOptional.isPresent()) {
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
