package edu.umg.proyecto_gastify.logica


// Devuelve un mensaje de error o null si los datos son válidos.
fun validarIngreso(
    cantidad: String,
    fuente: String,
    descripcion: String
): String? {
    val monto = cantidad.toBigDecimalOrNull()

    if (monto == null) {
        return "Escribe un monto válido."
    }

    if (monto.signum() <= 0) {
        return "El monto debe ser mayor que cero."
    }

    try {
        monto.movePointRight(2).longValueExact()
    } catch (error: ArithmeticException) {
        return "El monto es demasiado grande o tiene más de dos decimales."
    }

    if (fuente.isBlank()) {
        return "Selecciona una fuente del ingreso."
    }

    if (descripcion.isBlank()) {
        return "Escribe una descripción del ingreso."
    }

    if (descripcion.length > 200) {
        return "La descripción no debe superar los 200 caracteres."
    }

    return null
}