package com.example.demo.modelo;

import java.util.ArrayList;

public class Carrito {

	private ArrayList<Detalle> detalles;

	public Carrito() {

	}

	public ArrayList<Detalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(ArrayList<Detalle> detalles) {
		this.detalles = detalles;
	}

	public Double totalCarrito() {
		Double total = 0.0;
		for (Detalle detalle : detalles) {
			total = total + detalle.totalDetalle();
		}
		return total;
	}

	@Override
	public String toString() {
		return "Carrito [detalles=" + detalles + "]";
	}

}
