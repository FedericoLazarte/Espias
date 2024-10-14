package logica;

import java.util.Comparator;
import java.util.Objects;

public class Arista<T extends Comparable<T>> implements Comparable<Arista<T>> {
	private T verticeInicio;
	private T verticeDestino;
	private double peso;

	public Arista(T verticeInicio, T verticeDestino, double peso) {
		this.verticeInicio = verticeInicio;
		this.verticeDestino = verticeDestino;
		this.peso = peso;
	}

	public T obtenerVerticeInicio() {
		return this.verticeInicio;
	}

	public T obtenerVerticeDestino() {
		return this.verticeDestino;
	}

	public double obtenerPeso() {
		return this.peso;
	}

	public void cambiarPeso(double nuevoPeso) {
		pesoValido(nuevoPeso);
		this.peso = nuevoPeso;
	}

	@Override
	public boolean equals(Object objeto) {
		if (objeto instanceof Arista) {
			Arista<?> arista = (Arista<?>) objeto;
			return sonSimilares(arista);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(verticeInicio) + Objects.hash(verticeDestino);
	}

	@Override
	public int compareTo(Arista<T> otraArista) {
		int comparacion;
		if (this.peso != otraArista.peso) {
			comparacion = Double.compare(this.peso, otraArista.peso);
		} else if (!this.verticeInicio.equals(otraArista.verticeInicio)) {
			comparacion = this.verticeInicio.compareTo(otraArista.verticeInicio);
		} else {
			comparacion = this.verticeDestino.compareTo(otraArista.verticeDestino);
		}
		return comparacion;
	}

	@Override
	public String toString() {
		return "(" + this.verticeInicio + ")--" + this.peso + "-->(" + this.verticeDestino + ")";
	}

	public static <T extends Comparable<T>> Comparator<Arista<T>> aristaComparator() {
		return new Comparator<>() {
			@Override
			public int compare(Arista<T> ar1, Arista<T> ar2) {
				return ar1.compareTo(ar2);
			}
		};
	}

	private boolean sonSimilares(Arista<?> otraArista) {
		return sonIguales(otraArista) ||
				(this.verticeInicio.equals(otraArista.verticeDestino)
						&& this.verticeDestino.equals(otraArista.verticeInicio));
	}

	private boolean sonIguales(Arista<?> a) {
		return this.verticeInicio.equals(a.verticeInicio)
				&& this.verticeDestino.equals(a.verticeDestino);
	}

	private boolean pesoValido(double peso) {
		if (peso < 0 || peso > 1) {
			throw new IllegalArgumentException("El peso debe ser mayor o igual que 0 y menor o igual que 1");
		}
		return true;
	}
}