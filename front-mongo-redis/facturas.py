<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Facturas</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            margin: 0;
            padding: 0;
            background-color: #ffffff;
        }

        .sidebar {
            height: 100vh;
            width: 200px;
            background-color: #037bff;
            color: #fff;
            padding-top: 40px;
            box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            text-align: center;
            align-items: stretch;
            position: fixed; /* Barra de navegación fija */
            top: 0;
            left: 0;
        }

        .sidebar a {
            display: block;
            padding: 10px 20px;
            text-decoration: none;
            color: #fff;
            font-weight: bold;
            transition: background-color 0.3s ease;
            width: 100%;
            box-sizing: border-box;
        }

        .sidebar a:hover {
            background-color: #0358a7;
        }

        .content {
            margin-left: 200px; /* Ajuste para el ancho de la barra lateral */
            padding: 20px;
            width: calc(100% - 200px); /* Ajuste para el ancho de la barra lateral */
        }

        .content h2 {
            color: #333;
            border-bottom: 2px solid #ccc;
            padding-bottom: 10px;
        }

        .content p {
            color: #000000;
            margin-bottom: 10px;
        }

        .card {
            border: 1px solid #ccc;
            border-radius: 5px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .card p {
            margin: 5px 0;
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <a href="PerfilAdmin.html?mail=${encodeURIComponent(mail)}" id="perfilLink">Perfil</a>
        <a href="CambiarContraseniaAdmin.html?mail=${encodeURIComponent(mail)}" id="cambiarContraseniaLink">Cambiar Contraseña</a>
        <a href="VerProductos.html?mail=${encodeURIComponent(mail)}" id="verProductosLink">Ver Productos</a>
        <a href="EliminarProducto.html?mail=${encodeURIComponent(mail)}" id="eliminarProductoLink">Eliminar Producto</a>
        <a href="AgregarProducto.html?mail=${encodeURIComponent(mail)}" id="agregarProductoLink">Agregar Producto</a>
        <a href="ModificarPrecio.html?mail=${encodeURIComponent(mail)}" id="modificarPrecioLink">Modificar Precio</a>
        <a href="ModificarStock.html?mail=${encodeURIComponent(mail)}" id="modificarStockLink">Modificar Stock</a>
        <a href="Clientes.html?mail=${encodeURIComponent(mail)}" id="clientesLink">Clientes</a>
        <a href="Facturas.html?mail=${encodeURIComponent(mail)}" id="facturasLink">Facturas</a>
        <a href="Index.html" id="cerrarSesionLink">Cerrar Sesión</a>
    </div>

    <div class="content">
        <h2>Facturas</h2>
        <div id="facturasContainer"></div>
    </div>

    <script>
        const urlParams = new URLSearchParams(window.location.search);
        const mail = urlParams.get('mail');
        console.log("email: ", mail);

        function obtenerFacturas() {
            fetch('http://localhost:8080/tpo/verFacturas')
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Error en la solicitud: ' + response.status);
                    }
                    return response.json();
                })
                .then(data => {
                    const facturasContainer = document.getElementById('facturasContainer');
                    data.forEach(factura => {
                        const card = document.createElement('div');
                        card.className = 'card';
                        card.innerHTML = `
                            <p><strong>Número de Factura:</strong> ${factura.numeroFactura}</p>
                            <p><strong>Nombre del Cliente:</strong> ${factura.nombreCliente}</p>
                            <p><strong>DNI del Cliente:</strong> ${factura.dniCliente}</p>
                            <p><strong>Medio de Pago:</strong> ${factura.medioPago}</p>
                            <p><strong>Condición Fiscal:</strong> ${factura.condicionFiscal}</p>
                            <p><strong>Total:</strong> ${factura.total}</p>
                            <p><strong>Fecha:</strong> ${factura.fecha}</p>
                        `;
                        facturasContainer.appendChild(card);
                    });
                })
                .catch(error => {
                    console.error('Error al obtener facturas:', error);
                    alert('Error al obtener facturas. Por favor, inténtelo nuevamente.');
                });
        }

        obtenerFacturas();

        document.getElementById('perfilLink').addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = `PerfilAdmin.html?mail=${encodeURIComponent(mail)}`;
        });

        document.getElementById('cambiarContraseniaLink').addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = `CambiarContraseniaAdmin.html?mail=${encodeURIComponent(mail)}`;
        });

        document.getElementById('verProductosLink').addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = `VerProductos.html?mail=${encodeURIComponent(mail)}`;
        });

        document.getElementById('eliminarProductoLink').addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = `EliminarProducto.html?mail=${encodeURIComponent(mail)}`;
        });

        document.getElementById('agregarProductoLink').addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = `AgregarProducto.html?mail=${encodeURIComponent(mail)}`;
        });

        document.getElementById('modificarPrecioLink').addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = `ModificarPrecio.html?mail=${encodeURIComponent(mail)}`;
        });

        document.getElementById('modificarStockLink').addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = `ModificarStock.html?mail=${encodeURIComponent(mail)}`;
        });

        document.getElementById('clientesLink').addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = `Clientes.html?mail=${encodeURIComponent(mail)}`;
        });

        document.getElementById('facturasLink').addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = `Facturas.html?mail=${encodeURIComponent(mail)}`;
        });

        document.getElementById('cerrarSesionLink').addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = `Index.html`;
        });
    </script>
</body>
</html>
