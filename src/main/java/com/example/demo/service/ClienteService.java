package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import com.example.demo.modelo.Admin;
import com.example.demo.modelo.Carrito;
import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.Detalle;
import com.example.demo.modelo.Factura;
import com.example.demo.modelo.Producto;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.FacturaRepository;
import com.example.demo.repository.ProductoRepository;

@Service
public class ClienteService {

	@Autowired
	ClienteRepository repositorio;

	@Autowired
	ProductoRepository productoRepository;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	FacturaRepository facturaRepository;

//	Register
//	Eliminar Cliente
//	Login
//	Olvide contrasenia
//	Cambiar Contrasenia
//	Todos clientes

	public void guardarEnRedis(Cliente cliente) {
		// Crear una fábrica de conexiones Lettuce
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
		connectionFactory.afterPropertiesSet(); // Inicializa la fábrica de conexiones

		// Crear una plantilla de Redis (clave , valor)
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory); // Configura la plantilla con la fábrica de conexiones
		template.setDefaultSerializer(StringRedisSerializer.UTF_8); // Establece el serializador para las claves y //
																	// valores
		template.afterPropertiesSet(); // Inicializa la plantilla de Redis

		String mailAdmin = cliente.getMail();
		String password = cliente.getPassword();
// 
		template.opsForValue().set(mailAdmin, password);

		connectionFactory.destroy();
		System.out.println("Guardado en Redis");
	}

	public boolean loginRedis(String mail, String password) {
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
		connectionFactory.afterPropertiesSet();

		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setDefaultSerializer(StringRedisSerializer.UTF_8);
		template.afterPropertiesSet();

//		Si encuentra el mail, devuelve el password
		String resultado = template.opsForValue().get(mail);
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

//  Listo
	public String registerCliente(String documento, String nombre, String mail, String password, String direccion) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isEmpty()) {
			Cliente clienteNuevo = new Cliente(documento, nombre, mail, password, direccion);
			repositorio.save(clienteNuevo);
			emailSenderService.sendEmail("ignaciodorigo@gmail.com", "Registro en APP",
					nombre + " te has registrado exitosamente en la app");
			guardarEnRedis(clienteNuevo);
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

//  Listo
	public String loginCliente(String mail, String password) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (loginRedis(mail, password) && clienteOptional.isPresent()) {
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

//	Listo
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

//	Listo endpoint falta visual
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
					guardarEnRedis(cliente);
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

//	Listo
	public List<Cliente> clientes() {
		return repositorio.findAll();
	}

//  Listo
	public Cliente perfilCliente(String mail) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			return cliente;
		} else {
			return null;
		}
	}

//	Ver Carrito
	public List<Detalle> verCarrito(String mail) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			return cliente.getCarrito().getDetalles();
		} else {
			return null;
		}
	}

//	Agregar Producto al carrito (Nuevo)
	public String agregarProductoCarrito(String mail, Integer id, Integer cantidad) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
//		Validamos que exista el cliente
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			Optional<Producto> productoOptional = productoRepository.findById(id);
//			Validamos que exista el producto
			if (productoOptional.isPresent()) {
				Producto p = productoOptional.get();
				Carrito carritoCliente = cliente.getCarrito();
//				Si esto devuelve true es porque encontro el producto en el carrito
				if (carritoCliente.productoEnCarrito(p)) {

//					Modificamos la cantidad
					carritoCliente.modificarCantidadProducto(p, cantidad);
					cliente.setCarrito(carritoCliente);
					repositorio.save(cliente);
					return "Stock modificado al carrito";

				} else {
//					Creamos un detalle y lo agregamos al carrito(lo seteamos y lo volvemos a guardar)
					Detalle detalle = new Detalle(p, cantidad);
					carritoCliente.agregarDetalleCarrito(detalle);
					cliente.setCarrito(carritoCliente);
					repositorio.save(cliente);
					return "Producto agregado al carrito";

				}

			} else {
				return "El ID del Producto no existe";
			}
		} else {
			return "El cliente no existe";
		}
	}

//	Eliminar Producto del carrito (FALTA)
	public String eliminarProductoCarrito(String mail, Integer id) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
//		Validamos que exista el cliente
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			Optional<Producto> productoOptional = productoRepository.findById(id);
//			Validamos que exista el producto
			if (productoOptional.isPresent()) {
				Producto producto = productoOptional.get();
				Carrito carritoCliente = cliente.getCarrito();
//				Preguntamos si el producto esta en el carrito
				if (carritoCliente.productoEnCarrito(producto)) {
					carritoCliente.eliminarDetalleCarrito(producto);
					cliente.setCarrito(carritoCliente);
					repositorio.save(cliente);
					return "Producto eliminado del carrito";
				} else {
					return "El producto no esta en el carrito";
				}
			} else {
				return "El producto no existe";
			}

		} else {
			return "El cliente no existe";
		}
	}

//	Vaciar Carrito
	public String vaciarCarrito(String mail) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			Carrito carritoVacio = new Carrito(new ArrayList<Detalle>());
			cliente.setCarrito(carritoVacio);
			repositorio.save(cliente);
			return "Carrito vacio";
		} else {
			return "El cliente no existe";
		}
	}

//	Confirmar carrito (Recibimos datos para la facutra, la creamos, y seteamos el carrito)

	public Double totalFacturaCliente(String mail) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			Double totalCarrito = cliente.getCarrito().totalCarrito();
			return totalCarrito;
		} else {
			return 0.0;
		}
	}

	public String confimarCarrito(String mail, String medioPago, String condicionFiscal) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			String nombreCliente = cliente.getNombre();
			String dniCliente = cliente.getDocumento();
			Carrito carritoCliente = cliente.getCarrito();
			Factura factura = new Factura(nombreCliente, dniCliente, medioPago, carritoCliente, condicionFiscal);
			facturaRepository.save(factura);
			ArrayList<Factura> facturas = cliente.getFacturas();
			facturas.add(factura);
			cliente.setFacturas(facturas);
			repositorio.save(cliente);
			List<Detalle> detalles = carritoCliente.getDetalles();
			for (Detalle detalle : detalles) {
				Producto producto = detalle.getProducto();
				producto.setStock(producto.getStock() - detalle.getCantidad());
				productoRepository.save(producto);
			}
			vaciarCarrito(mail);
			return "Carrito Facturado";
		} else {
			return "Cliente no encontrado";
		}
	}

	public List<Factura> verMisFacturas(String mail) {
		Optional<Cliente> clienteOptional = repositorio.findById(mail);
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			List<Factura> facturas = cliente.getFacturas();
			return facturas;
		} else {
			return null;
		}
	}
}
