package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modelo.Admin;
import com.example.demo.modelo.Cliente;
import com.example.demo.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	ClienteRepository repositorio;

	@Autowired
	EmailSenderService emailSenderService;

//	Register
//	Eliminar Cliente
//	Login
//	Olvide contrasenia
//	Cambiar Contrasenia
//	Todos clientes

	public String registerCliente(String documento, String nombre, String mail, String password, String direccion) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isEmpty()) {
			Cliente clienteNuevo = new Cliente(documento, nombre, mail, password, direccion);
			repositorio.save(clienteNuevo);
			emailSenderService.sendEmail("ignaciodorigo@gmail.com", "Registro en APP",
					nombre + " te has registrado exitosamente en la app");
			return "Registro exitoso";
		} else {
			return "Ese mail ya esta registrado";
		}
	}

//	No hace falta endpoint
	public String eliminarCliente(String mail) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isPresent()) {
			repositorio.deleteById(mail);
			return "Cliente eliminado";
		} else {
			return "No existe ese cliente";
		}
	}

	public String loginCliente(String mail, String password) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			if (cliente.getPassword().equals(password)) {
				return "Login exitoso";
			} else {
				return "Contrasena incorrecta";
			}

		} else {
			return "Error no existe";
		}
	}

	public String recuperarContrasenaCliente(String mail) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			String contrasenia = cliente.getPassword();
			emailSenderService.sendEmail("ignaciodorigo@gmail.com", "Recupero contrasenia en APP",
					"Tu contrasenia es : " + contrasenia);
			return "Envio de contrasenia al mail";
		} else {
			return "Error no existe";
		}
	}

	public String cambiarContraseniaCliente(String mail, String actual, String nueva1, String nueva2) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			String contrasenia = cliente.getPassword();
			if (contrasenia.equals(actual)) {
				if (nueva1.equals(nueva2)) {
					cliente.setPassword(nueva1);
					repositorio.save(cliente);
					emailSenderService.sendEmail("ignaciodorigo@gmail.com", "Cambio contrasenia en APP",
							"Has cambiado tu contrasenia, tu nueva contrasenia es: " + nueva1);
					return "Cambio contrasenia exitoso";
				} else {
					return "Las contrasenias no coinciden";
				}
			} else {
				return "Contrasenia actual incorrecta";
			}
		}
		return "Error cliente no encontrado";
	}

	public List<Cliente> clientes() {
		return repositorio.findAll();
	}

	public Cliente perfilCliente(String mail) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			return cliente;
		} else {
			return null;
		}
	}


}
