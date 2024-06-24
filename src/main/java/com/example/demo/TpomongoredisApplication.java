package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.Producto;
import com.example.demo.service.AdminService;
import com.example.demo.service.ClienteService;
import com.example.demo.service.ProductoService;

@SpringBootApplication
public class TpomongoredisApplication implements CommandLineRunner {

	@Autowired
	AdminService adminService;

	@Autowired
	ProductoService productoService;

	@Autowired
	ClienteService clienteService;

	public static void main(String[] args) {
		SpringApplication.run(TpomongoredisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// SOLO SE USA PARA TESTEAR
//		 // Verificar la conexión a Redis
//       try {
//           redisTemplate.opsForValue().set("testKey", "testValue");
//           String value = redisTemplate.opsForValue().get("testKey");
//           System.out.println("Conexión establecida con Redis. Valor recuperado: " + value);
//       } catch (Exception e) {
//           System.err.println("Error al conectar con Redis: " + e.getMessage());
//       }

// ----------------------------- ADMIN -----------------------------

//		Agregar un Admin
//		String mail = "ignacio8@gmail.com";
//		String password = "nacho";
//		String nombre = "Ignacio";
//		String apellido = "Dorigo";
//		String documento = "42414241";
//		System.out.println(adminService.registerAdmin(mail, password, nombre, apellido, documento));

//		Eliminar un Admin
//		String mail = "nacho@gmail.com";
//		String password = "nacho";
//		System.out.println(adminService.eliminarAdmin(mail));

//		Login Admin
//		String mail = "nacho@gmail.com";
//		String password = "nacho";
//		System.out.println(adminService.loginAdmin(mail, password));

//		Olvide mi contrasenia
//		String mail = "nacho@gmail.com";
//		System.out.println(adminService.olvideContrasena(mail));

//		Cambiar contrasenia
//		String mail = "nacho@gmail.com";
//		String password = "nacho";
//		String nueva1 = "uade";
//		String nueva2 = "uade";
//		System.out.println(adminService.cambiarContraseniaAdmin(mail, password, nueva1, nueva2));
		

// ----------------------------- PRODUCTOS ----------------------------- 

//		Agregar Producto
//		Integer id = 8;
//		String nombre = "Iphone 1qq52 pro max";
//		Double precio = 1259.0;
//		Integer stock = 100;
//		String imagen = "https://http2.mlstatic.com/D_Q_NP_924631-MLA71783367058_092023-O.webp";
//		System.out.println(productoService.agregarProducto(id, nombre, precio, stock, imagen));

//		Eliminar Producto
//		Integer id = 2;
//		System.out.println(productoService.eliminarProducto(id));

//		Modificar Precio
//		Integer id = 1;
//		Double precioNuevo = 666.0;
//		System.out.println(productoService.modificarPrecio(id, precioNuevo));

//		Modificar Stock
//		Integer id = 1;
//		Integer nuevoStock = 777;
//		System.out.println(productoService.modificarStock(id, nuevoStock));

//		Mostrar productos
//		List<Producto> productos = productoService.productos();
//		System.out.println("---------------   PRODUCTOS    ----------------");
//		for (Producto producto : productos) {
//			System.out.println(producto);
//		}

// ----------------------------- CLIENTE ----------------------------- 

//		Register
//		String documento = "42414241";
//		String nombre = "Tomas Dorigo";
//		String mail = "tomasdorigo2994@gmail.com";
//		String password = "nacho";
//		String direccion = "UADE 1234";
//		System.out.println(clienteService.registerCliente(documento, nombre, mail, password, direccion));

//		Eliminar Cliente
//		String mail = "tomasdorigo@gmail.com";
//		System.out.println(clienteService.eliminarCliente(mail));

//		Recuperar Contrasenia Cliente
//		String mail = "tomasdorigo@gmail.com";
//		System.out.println(clienteService.recuperarContrasenaCliente(mail));

//		Login Cliente
//		String mail = "tomasdorigo@gmail.com";
//		String password = "nacho";
//		System.out.println(clienteService.loginCliente(mail, password));

//		Cambiar contrasenia cliente
//		String mail = "tomasdorigo@gmail.com";
//		String password = "abcd";
//		String nueva1 = "nacho";
//		String nueva2 = "nacho";
//		System.out.println(clienteService.cambiarContraseniaCliente(mail, password, nueva1, nueva2));

	}

}
