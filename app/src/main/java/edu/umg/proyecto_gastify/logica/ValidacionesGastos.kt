package edu.umg.proyecto_gastify.logica

// Devuelve un mensaje si encuentra un error.
// Devuelve null cuando los datos son válidos.
fun validarGasto(
    cantidad: String,
    categoria: String,
    descripcion: String
): String? {
    val monto = cantidad.toBigDecimalOrNull()

    if (monto == null) {
        return "Escribe un monto válido."
    }

    if (monto.signum() <= 0) {
        return "El monto debe ser mayor que cero."
    }

    // Comprueba que pueda representarse exactamente en centavos.
    try {
        monto.movePointRight(2).longValueExact()
    } catch (error: ArithmeticException) {
        return "El monto es demasiado grande o tiene más de dos decimales."
    }

    if (categoria.isBlank()) {
        return "Selecciona una categoría."
    }

    // Rechaza descripciones vacías o que solo tengan espacios.
    if (descripcion.isBlank()) {
        return "Escribe una descripción del gasto."
    }

    return null
}
