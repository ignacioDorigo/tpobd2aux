package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.modelo.Admin;
import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.Producto;
import com.example.demo.service.AdminService;
import com.example.demo.service.ClienteService;
import com.example.demo.service.ProductoService;

@RestController
@RequestMapping("/tpo")
public class Controller {

	@Autowired
	AdminService adminService;

	@Autowired
	ClienteService clienteService;

	@Autowired
	ProductoService productoService;

//	Todo lo de admin primero
//	LoginAdmin()

	@PostMapping("/loginAdmin")
	public ResponseEntity<String> loginAdmin(@RequestParam String mail, @RequestParam String password) {
		String respuesta = adminService.loginAdmin(mail, password);
		if (respuesta.equals("Login Exitoso")) {
			return ResponseEntity.ok(respuesta);
		} else {
			return ResponseEntity.status(401).body(respuesta);
		}
	}

	@PostMapping("/registerAdmin")
	public ResponseEntity<String> registerAdmin(@RequestParam String mail, @RequestParam String password,
			@RequestParam String nombre, @RequestParam String apellido, @RequestParam String documento) {
		String respuesta = adminService.registerAdmin(mail, password, nombre, apellido, documento);
		if (respuesta.equals("Register Exitoso")) {
			return ResponseEntity.ok(respuesta);
		} else {
			return ResponseEntity.status(401).body(respuesta);
		}
	}

	@GetMapping("/productos")
	public List<Producto> productos() {
		return productoService.productos();
	}

	@GetMapping("/clientes")
	public List<Cliente> clientes() {
		return clienteService.clientes();
	}

	@PostMapping("/crearProducto")
	public ResponseEntity<String> crearProducto(@RequestParam Integer id, @RequestParam String nombre,
			@RequestParam Double precio, @RequestParam Integer stock, @RequestParam String imagen) {
		String respuesta = productoService.agregarProducto(id, nombre, precio, stock, imagen);
		if (respuesta.equals("Producto agregado")) {
			return ResponseEntity.ok(respuesta);
		} else {
			return ResponseEntity.status(401).body(respuesta);
		}
	}

	@DeleteMapping("/eliminarProducto")
	public ResponseEntity<String> eliminarProducto(@RequestParam Integer id) {
		String respuesta = productoService.eliminarProducto(id);
		if (respuesta.equals("Producto eliminado")) {
			System.out.println("Producto eliminado");
			return ResponseEntity.ok(respuesta);
		} else {
			System.out.println("ENTRE ACA");
			return ResponseEntity.status(401).body(respuesta);
		}
	}

	@PutMapping("/actualizarPrecio")
	public ResponseEntity<String> actualizarPrecio(@RequestParam Integer id, @RequestParam Double precioNuevo) {
		String respuesta = productoService.modificarPrecio(id, precioNuevo);
		if (respuesta.equals("Precio actualizado")) {
			return ResponseEntity.ok(respuesta);
		} else {
			return ResponseEntity.status(401).body(respuesta);
		}
	}

	@PutMapping("/actualizarStock")
	public ResponseEntity<String> actualizarStock(@RequestParam Integer id, @RequestParam Integer stockNuevo) {
		String respuesta = productoService.modificarStock(id, stockNuevo);
		if (respuesta.equals("Stock actualizado")) {
			return ResponseEntity.ok(respuesta);
		} else {
			return ResponseEntity.status(401).body(respuesta);
		}
	}

	@PostMapping("/recuperarContraseniaAdmin")
	public ResponseEntity<String> recuperarContraseniaAdmin(@RequestParam String mail) {
		String respuesta = adminService.olvideContrasena(mail);
		if (respuesta.equals("Envio de contrasenia al mail")) {
			return ResponseEntity.ok(respuesta);
		} else {
			return ResponseEntity.status(401).body(respuesta);
		}
	}

	@GetMapping("/perfilAdmin")
	public Admin perfilAdmin(@RequestParam String mail) {
		return adminService.perfilAdmin(mail);
	}

	@PutMapping("/cambiarContraseniaAdmin")
	public ResponseEntity<String> cambiarContraseniaAdmin(@RequestParam String mail, @RequestParam String actual,
			@RequestParam String nueva1, @RequestParam String nueva2) {
		String respuesta = adminService.cambiarContraseniaAdmin(mail, actual, nueva1, nueva2);
		if (respuesta.equals("Cambio contrasenia exitoso")) {
			return ResponseEntity.ok(respuesta);
		} else {
			return ResponseEntity.status(401).body(respuesta);
		}
	}
}
